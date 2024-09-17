package beadando3;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.sql.SQLException;

/**
 *
 * @author mgabo
 */
public class GameEngine extends JPanel {

    private final int FPS = 240;
    private final int LACI_MOZOG = 10;
    private final int HEART_WIDTH = 50;
    private final int HEART_HEIGHT = 50;
    private Timer newFrameTimer;
    private Laci laci;
    private Heart heart1;
    private Heart heart2;
    private Heart heart3;
    private boolean paused = false;
    private Image background;
    private int levelNum = 1;
    private int hearts = 3;
    private Level level;
    private String lastMove = "";
    private Timer timer;
    private int cnt = 0;
    private String timetext = "";
    private boolean active = true;
    
    public GameEngine() {
        super();
        background = new ImageIcon("floor.jfif").getImage();
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastMove = "left";
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastMove = "right";
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastMove = "down";
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                lastMove = "up";
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), "escape");
        this.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(paused) {
                    paused = !paused;
                    timer.start();
                }
                else {
                    paused = !paused;
                    timer.stop();
                }
            }
        });
        restart();
        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        newFrameTimer.start();
    }

    public void restart() {
        try {
            level = new Level("level" + levelNum + ".txt");
        } catch (IOException ex) {
            Logger.getLogger(GameEngine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Image laciImage = new ImageIcon("macilaci.png").getImage();
        if (levelNum == 7 || levelNum == 9) {
            laci = new Laci(750, 425, 80, 140, laciImage);
        }
        else {
            laci = new Laci(1500, 0, 80, 140, laciImage);
        }
        Image heartImage = new ImageIcon("heart.png").getImage();
        heart1 = new Heart(5,5,HEART_WIDTH,HEART_HEIGHT,heartImage);
        heart2 = new Heart(60,5,HEART_WIDTH,HEART_HEIGHT,heartImage);
        heart3 = new Heart(115,5,HEART_WIDTH,HEART_HEIGHT,heartImage);
    }
    
    public void Time() {
    timer = new Timer(1000,new ActionListener(){
        @Override
            public void actionPerformed(ActionEvent e)
            {
                cnt++;
                int p = cnt/60;
                int mp = cnt%60;
                if (p == 0) {
                    if (mp < 10) {
                        timetext = "Idő: 00:0" + String.valueOf(mp);
                    }
                    else {
                        timetext = "Idő: 00:" + String.valueOf(mp);
                    }
                }
                if(p < 10) {
                    if (mp < 10) {
                        timetext = "Idő: 0" + String.valueOf(p) + ":0" + String.valueOf(mp);
                    }
                    else {
                        timetext = "Idő: 0" + String.valueOf(p) + ":" + String.valueOf(mp);
                    }
                }
                else {
                    if (mp < 10) {
                        timetext = "Idő: " + String.valueOf(p) + ":0" + String.valueOf(mp);
                    }
                    else {
                        timetext = "Idő: " + String.valueOf(p) + ":" + String.valueOf(mp);
                    }
                }
            }
        });
    timer.start();
    }
    
    public void win() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Gratul�lok, Nyert�l :)","J�t�k v�ge",JOptionPane.INFORMATION_MESSAGE);
        String nev = JOptionPane.showInputDialog(this,"N�v: ");
        try {
            HighScores highScores = new HighScores(10);
            highScores.putHighScore(nev, levelNum);
        } catch (SQLException e) {
            Logger.getLogger(MaciLaciGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        System.exit(0);
    }
    public void lose() {
        String nev = JOptionPane.showInputDialog(this,"N�v: ");
        try {
            HighScores highScores = new HighScores(10);
            highScores.putHighScore(nev, levelNum);
        } catch (SQLException e) {
            Logger.getLogger(MaciLaciGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        reset();
        levelNum = 1;
        hearts = 3;
        restart();
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(background, 0, 0, 1600, 900, null);
        level.draw(grphcs);
        laci.draw(grphcs);
        if (hearts == 3) {
            heart1.draw(grphcs);
            heart2.draw(grphcs);
            heart3.draw(grphcs);
        }
        if (hearts == 2) {
            heart1.draw(grphcs);
            heart2.draw(grphcs);
        }
        if (hearts == 1) {
            heart1.draw(grphcs);
        }
    }

    class NewFrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                switch (lastMove) {
                    case "left": laci.moveX(-LACI_MOZOG);break;
                    case "right": laci.moveX(LACI_MOZOG);break;
                    case "up": laci.moveY(-LACI_MOZOG);break;
                    case "down": laci.moveY(LACI_MOZOG);break;
                }
                if(active == false){
                    level.starter();
                    active = true;
                }
                level.collidesA(laci,LACI_MOZOG,lastMove);
                lastMove = "";
                level.collidesK(laci);
                if (level.collidesO(laci)) {
                    hearts--;
                    if (hearts == 0) {
                        lose();
                    }
                    else {
                        restart();
                    }
                }
            } else {
                level.pause();
                active = false;
            }
            if (level.isOver()) {
                levelNum++;
                if(levelNum == 11) {
                    win();
                }                
                restart();
            }
            repaint();
        }
    }
    public void reset() {
        cnt = 0;
    }
    public String getTimeText() {
        return timetext;
    }
    public void setLevelNum(int i) {
        levelNum = i;
    }
}


