/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author mgabo
 */
 
public class AmobaGUI {

    private JFrame frame;
    private BoardGUI boardGUI;
    private boolean isEmpty = true;

    public AmobaGUI() {
        frame = new JFrame("Amoba");
        frame.setSize(600,300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenuItem newGame = new JMenuItem("New Game");
        gameMenu.add(newGame);
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int boardSize1 = 0;
                int boardSize2 = 0;
                if (isEmpty == false) {
                frame.getContentPane().remove(boardGUI.getBoardPanel());}
                try {
                    boardSize2 = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter the number of columns (min. 4): "));
                    if (boardSize2 < 4) {
                        JOptionPane.showMessageDialog(frame, "Must be at least 4 columns for a valid solution","Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                    boardSize1 = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter the number of rows (min. 2): "));
                    if (boardSize1 < 2) {
                        JOptionPane.showMessageDialog(frame, "Must be at least 2 rows for a valid solution","Error", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Wrong format","Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                boardGUI = new BoardGUI(boardSize1,boardSize2);
                isEmpty = false;
                frame.getContentPane().add(boardGUI.getBoardPanel(),BorderLayout.CENTER);
                frame.pack();
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