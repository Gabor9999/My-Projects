package beadando3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author mgabo
 */
public class MaciLaciGUI {
    private JFrame frame;
    private JLabel timetext;
    private GameEngine gameArea;

    public MaciLaciGUI() {
        frame = new JFrame("Maci Laci (KFC edition)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        gameArea = new GameEngine();
        timetext = new JLabel("Idő: 00:00");
        timetext.setFont(timetext.getFont().deriveFont(24.0f));
        gameArea.add(timetext);
        gameArea.Time();
        Timer timer = new Timer(1000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timetext.setText(gameArea.getTimeText());
            }
        });
        timer.start();
        frame.getContentPane().add(gameArea);
        frame.pack();
        frame.setSize(new Dimension(1600, 900));
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenuItem newGame = new JMenuItem("New Game");
        gameMenu.add(newGame);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            frame.getContentPane().remove(gameArea);         
            gameArea.reset();
            gameArea.setLevelNum(1);
            gameArea.restart();
            frame.getContentPane().add(gameArea,BorderLayout.CENTER);
            };
        });
        JMenuItem scoreMenuItem = new JMenuItem("Eredmények");
        gameMenu.add(scoreMenuItem);
        scoreMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
            try {
                HighScores highScores = new HighScores(10);
                String out = "";
                for(HighScore score : highScores.getHighScores()) {
                    out += "\n- " + score;
                }
                JOptionPane.showMessageDialog(frame, "Eredmények: \n" + out,"Eredmények",JOptionPane.INFORMATION_MESSAGE);
            }
            catch(SQLException e) {
                Logger.getLogger(MaciLaciGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
            }
            }
        });
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}
