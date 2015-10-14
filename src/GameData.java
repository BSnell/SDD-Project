/*
 handles the management of the objects to be rendered and when to remove them. 
 Also controls the spawning of enemies and firing of enemies
 */

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

enum PHASE {

    ONE, TWO, THREE
};

public class GameData {

    public static int MAXHEALTH = 30;
    final List<GameFigure> figures;
    int count = 0;
    boolean FINISHED = false;
    boolean bossTime = false;
    boolean BOSS = false;
    boolean isStarting = true;
    private int enemiesSpawned;
    private static PHASE phase;
    //public int health = MAXHEALTH;
    ScoreObserver score = new ScoreObserver();
    String separator = System.getProperty("file.separator");
    String imagePath = System.getProperty("user.dir");
    String explosion = imagePath + separator + "images" + separator + "BigExplosionSoundEffect.mp3";//load game over screen from image file
    String missilelaunch = imagePath + separator + "images" + separator + "SoundEffectMissileLaunch.mp3";
    String collide = imagePath + separator + "images" + separator + "collide.mp3";
    private boolean stateChanged = false;
    public Thread Stage1Spawner;
    public Thread Stage2Spawner;
    public Thread Stage3Spawner;

    public GameData() throws IOException {

        figures = Collections.synchronizedList(new ArrayList<GameFigure>());
        //spawn player ship and add to figures list here
        GameFigureFactory factory = new Factory("Ship");//example of player ship spawn
        figures.add(factory.createFigure());//adds player ship to figures
        setStage1Spawner();//spawner thread for enemies
        setStage2Spawner();
        setStage3Spawner();
        Stage1Spawner.start();//starts the enemy spawner
        startFiring();//thread that has enemies fire
    }

    public void setStateChanged(int stage) throws IOException, InterruptedException {
        Stage1Spawner.interrupt();
        Stage2Spawner.interrupt();
        Stage3Spawner.interrupt();
        removeEnemies();
        if (stage == 1) {
            setStage1Spawner();
            Stage1Spawner.start();
        } else if (stage == 2) {
            setStage2Spawner();
            Stage2Spawner.start();
        } else {
            setStage3Spawner();
            Stage3Spawner.start();
        }
    }

    public void removeEnemies() {
        ArrayList<GameFigure> remove = new ArrayList<>();
        for (int i = 1; i < figures.size(); i++) {
            remove.add(figures.get(i));
        }
        figures.removeAll(remove);
    }

    public final void setStage1Spawner() throws IOException {
        final Stage1 stage1 = new Stage1();
        count = 0;
        Stage1Spawner = new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;) {//loop spawns enemies, add condition to stop the spawning of enemies
                    while (count < 6) {
                        if(Thread.interrupted()){
                            return;
                        }
                        Enemy enemy = stage1.getEnemy1();
                        figures.add(enemy);
                        count++;
                        if (count == 6) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                return;
                            }
                        }
                    }
                    while (count >= 6 && count <= 15) {
                        if(Thread.interrupted()){
                            return;
                        }
                        Enemy enemy = stage1.getEnemy2();
                        figures.add(enemy);
                        count++;
                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException ex) {
                            return;
                        }
                    }
                    while (count >= 15) {
                        if(Thread.interrupted()){
                            return;
                        }
                        Enemy enemy = stage1.getEnemy3();
                        figures.add(enemy);
                        count++;
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException ex) {
                            return;
                        }
                    }
                    count++;
                }
            }
        });

        
    }

    public final void setStage2Spawner() throws IOException {
        final Stage2 stage2 = new Stage2();
        count = 0;
        Stage2Spawner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 35 && !Thread.interrupted()) {//loop spawns enemies
                    spawnEnemy();
                    try {
                        Thread.sleep(750);//interval in which enemies are spawned
                    } catch (InterruptedException ex) {
                        return;
                    }
                    count++;
                }
            }
        });
        
    }

    public final void setStage3Spawner() throws IOException {
        final Stage3 stage3 = new Stage3();
        count = 0;
        Stage3Spawner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < 35 && !Thread.interrupted()) {//loop spawns enemies
                    spawnEnemy();
                    try {
                        Thread.sleep(750);//interval in which enemies are spawned
                    } catch (InterruptedException ex) {
                        return;
                    }
                    count++;
                }
            }
        });
        
    }

    public int getHealth() {

        return score.health;
    }

    public static PHASE getphase() {
        return phase;
    }

    public static void setphase(PHASE p) {
        phase = p;
    }

    public void spawnEnemy() {
        //use the GameFigureFactory to create new instances of enemy objects
        GameFigureFactory factory = new Factory("Enemy");
        figures.add(factory.createFigure());
    }

    public void spawnBoss() {
        //seperate method to spawn boss 
    }

    public final void startFiring() {
        Thread enemyShoot;
        enemyShoot = new Thread(new Runnable() {
            @Override
            public void run() {
                GameFigure f;
                while (!FINISHED) {//FINISHED indicated whether the game is over or not. Enemies will fire if on the screen as long as the game is not over
                    for (int i = 0; i < figures.size(); i++) {
                        int temp = 1 + (int) (Math.random() * 100); //random generator for enemies to fire randomly
                        if (temp <= 90 && temp >= 30) {//if random number is between 90 and 30, enemy will fire
                            f = figures.get(i);//get enemy to fire
                            if (f.isPlayer() == 1 || f.isPlayer() == 2) {//check is the object collected from list is the player, do not fire if player.
                                Missile2 m = new Missile2(f.getXofMissileShoot(), f.getYofMissileShoot(), f.getMyType());
                                //System.out.println(f.getType());
                                ThreadPlayer.play(missilelaunch);
                                synchronized (figures) {
                                    figures.add(m);
                                }
                            }
                        }
                        try {
                            Thread.sleep(100);//interval between enemy fire
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GameData.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        );
        enemyShoot.start();//start the enemy shooting thread.
    }

    private void addPower(int x, int y) {
        PowerUp pw = new PowerUp(4);
        pw.setLocation(x, y + 5);
        pw.setReleased(true);
        pw.setEnabled(true);
        pw.updateState(1);
        figures.add(pw);
    }

    private void playerCheck(GameFigure player) {
        GameFigure g;
        for (int i = 0; i < figures.size(); i++) {

        }

    }

    private void enemyCheck(GameFigure f) {

    }

    private void playMissileCheck(GameFigure f) {
        GameFigure g;
        synchronized (figures) {
            for (int i = 0; i < figures.size(); i++) {
                g = figures.get(i);
                if (!f.equals(g) && g.isPlayer() == 1) {
                    if (f.collision().intersects(g.collision())) {
                        f.updateState(GameFigure.STATE_DONE);
                        g.Health(1);//subtract 1 from Enemy's health

                        if (g.getState() == GameFigure.STATE_DONE) {
                            f.registerObserver(score);//update score upone each enemy destroyed
                            addPower((int) f.getXcoor(), (int) f.getYcoor()); // release POWER
                            f.notifyObservers(5);
                        }
                        //System.out.println("Score :  " + score.score);
                    }
                }
            }
        }
    }

    private void enemMissileCheck(GameFigure f) {
        GameFigure g;
        synchronized (figures) {
            for (int i = 0; i < figures.size(); i++) {
                g = figures.get(i);
                if (!f.equals(g) && g.isPlayer() == 0) {
                    if (f.collision().intersects(g.collision())) {
                        f.updateState(GameFigure.STATE_DONE);
                        g.Health(1);//subtract 1 from Player's health                    
                    }
                }
            }
        }
    }

    private void powCheck(GameFigure f) {
        synchronized (figures) {

            GameFigure g;
            for (int i = 0; i < figures.size(); i++) {
                g = figures.get(i);
                if (!g.equals(f) && g.isPlayer() == 0 && f.getState() == GameFigure.STATE_TRAVELING) {
                    if (f.collision().intersects(g.collision())) {
                        f.updateState(GameFigure.STATE_DONE);
                        g.Health(-1);//subtract 1 from Player's health
                        //System.out.println("Collision");
                    }
                }
            }

        }
    }

    public void update() {
        List<GameFigure> remove = new ArrayList<>();//list of all game figures marked for removal
        GameFigure f;
        synchronized (figures) {
            for (int i = 0; i < figures.size(); i++) {
                f = figures.get(i);
                f.update(); // call update from all classes.

                if (f.isMissile() == 0) { // if player's missile
                    playMissileCheck(f);
                } else if (f.isMissile() == 1) { // if enemy's missile
                    enemMissileCheck(f);
                } else if (f.isPlayer() == 31) { // if power 
                    powCheck(f);
                }
                /*
                 else if(f.isPlayer() == 30){
                 powerCheck(f);
                 }
                 */

                if (f.getState() == GameFigure.STATE_DONE) {
                    remove.add(f);
                }

                if (figures.get(0).isPlayer() == 0) {//if player is destoyed, end game
                } else {
                    FINISHED = true;
                }
            }

            figures.removeAll(remove);
        }
    }
}
