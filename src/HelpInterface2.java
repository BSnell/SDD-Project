
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class HelpInterface2 extends JFrame implements ActionListener, MouseListener, KeyListener, MouseMotionListener {

    private final DisplayPanel displayPanel;
    private final DisplayGameData displayGameData;
    private final DisplayAnimator displayAnimator;

    private JButton back;
    private JButton startTutorial;

    Font btnFont;
    Border borderLine;
    Color btnColor;

    public HelpInterface2() throws IOException {
        setSize(1275, 665);//size of initial window /1245,960
        setLocation(50, 100);//location of initial window
        setTitle("Galileo GameFigures");//title of the initial window
        Container c = getContentPane();//container for JPanel items 

        displayGameData = new DisplayGameData();
        displayAnimator = new DisplayAnimator();
        displayPanel = new DisplayPanel(displayAnimator, displayGameData); //animator, gameData
        displayAnimator.setDisplayPanel(displayPanel);//sets the gamePanel object for animator to use //animator.setTutorialPanel(tutorialPanel)
        displayAnimator.setDisplayGameData(displayGameData);//sets gameData object for animator to us //animator.setGameData(gameData)
        c.add(displayPanel, "Center");//centers the gamePanel on the JPanel

        JPanel southPanel = new JPanel();

        btnFont = new Font("Bodoni MT Black", Font.ROMAN_BASELINE, 25);
        borderLine = new LineBorder(Color.BLUE, 5);
        btnColor = new Color(190, 175, 170);

        back = new JButton("Return");
        back.setVisible(true);
        back.setFont(btnFont);
        back.setBorder(borderLine);
        back.setBackground(btnColor);
        southPanel.add(back);

        btnFont = new Font("Bodoni MT Black", Font.ROMAN_BASELINE, 25);
        borderLine = new LineBorder(Color.BLUE, 5);
        btnColor = new Color(190, 175, 170);

        startTutorial = new JButton("Tutorial");
        startTutorial.setVisible(true);
        startTutorial.setFont(btnFont);
        startTutorial.setBorder(borderLine);
        startTutorial.setBackground(btnColor);
        southPanel.add(startTutorial);//

        back.addActionListener(this);
        back.setFocusable(false);

        startTutorial.addActionListener(this);
        startTutorial.setFocusable(false);

        c.add(southPanel, "South");

        displayPanel.startDisplay();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            DisplayAnimator.running = false;
            dispose();
        }
        if (e.getSource() == startTutorial) {
            try {
                JFrame practice = new Tutorial();
                practice.setResizable(false);
                practice.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(HelpInterface2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
