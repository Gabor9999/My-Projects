package beadando3;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 *
 * @author mgabo
 */
public class Level {

    // each brick is 40x20, so there can be at most 20 bricks side by side
    // the last 10 rows will be empty, so there can be at most 20 rows of bricks
    private final int KOSAR_WIDTH = 100;
    private final int KOSAR_HEIGHT = 100;
    private final int AKADALY_WIDTH = 100;
    private final int AKADALY_HEIGHT = 100;
    private final int OR_WIDTH = 50;
    private final int OR_HEIGHT = 120;
    ArrayList<Kosar> kosarak;
    ArrayList<Akadaly> akadalyok;
    ArrayList<Or> orok;
    private Timer ortime;

    public Level(String levelPath) throws IOException 
    {
        loadLevel(levelPath);
    }

    public void loadLevel(String levelPath) throws FileNotFoundException, IOException 
    {
        BufferedReader br = new BufferedReader(new FileReader(levelPath));
        kosarak = new ArrayList<>();
        akadalyok = new ArrayList<>();
        orok = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String [] elems = line.split(" ");
                if (elems[0].charAt(0) == 'K') {
                    Image image = new ImageIcon("kfc2.png").getImage();
                    kosarak.add(new Kosar(Integer.parseInt(elems[1]),Integer.parseInt(elems[2]), KOSAR_WIDTH, KOSAR_HEIGHT, image));
                }
                if (elems[0].charAt(0) == 'A') {
                    Image image2 = new ImageIcon("burger.png").getImage();
                    akadalyok.add(new Akadaly(Integer.parseInt(elems[1]),Integer.parseInt(elems[2]),AKADALY_WIDTH,AKADALY_HEIGHT,image2));
                }
                if (elems[0].charAt(0) == 'O') {
                    Image image3 = new ImageIcon("human2.png").getImage();
                    if(elems[3].equals("-") ) {
                        orok.add(new Or(Integer.parseInt(elems[1]),Integer.parseInt(elems[2]),OR_WIDTH,OR_HEIGHT,true,image3));
                    }
                    else {
                        orok.add(new Or(Integer.parseInt(elems[1]),Integer.parseInt(elems[2]),OR_WIDTH,OR_HEIGHT,false,image3));
                    }
                }
        }
        orIndul();
    }
    
    public void orIndul() {
        ortime = new Timer(1000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for (Or ors : orok) {
                    ors.move();
                    for (Akadaly akadaly : akadalyok) {
                        if(ors.collides(akadaly)) {
                            if(ors.getDecide()) {
                                ors.cVelx();
                            }
                            else {
                                ors.cVely();
                            }
                            break;
                    }}
                }
            }
        });
        ortime.start();
    }
    
    public void pause() {
        ortime.stop();
    }
    public void starter() {
        orIndul();
    }
    
    public void collidesK(Laci laci) {
        Kosar collidedWithK = null;
        for (Kosar kosar : kosarak) {
            if (laci.collides(kosar)) {
                collidedWithK = kosar;
                break;
            }
        }
        if (collidedWithK != null) {
            kosarak.remove(collidedWithK);
        } 
    }
    public void collidesA (Laci laci,int val,String move) {
        Akadaly collidedWithA = null;
        for (Akadaly akadaly : akadalyok) {
            if (laci.collides(akadaly)) {
                collidedWithA = akadaly;
                break;
            }
        }
        if (collidedWithA != null) {
            if (move.equals("left")) {
                laci.x += val;
            }
            if (move.equals("right")) {
                laci.x -= val;
            }
            if (move.equals("up")) {
                laci.y += val;
            }
            if (move.equals("down")) {
                laci.y -= val;
            }
        }
    }
    
    public boolean collidesO (Laci laci) {
        for (Or ors : orok) {
            if (ors.collides(laci)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isOver() {
        return kosarak.isEmpty();
    }

    public void draw(Graphics g) {
        for (Kosar kosar : kosarak) {
            kosar.draw(g);
        }
        for (Akadaly akadaly : akadalyok) {
            akadaly.draw(g);
        }
        for (Or ors : orok) {
            ors.draw(g);
        }
    }

}
