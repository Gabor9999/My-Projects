/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando2;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author mgabo
 */
public class BoardGUI {
    private JButton[][] buttons;
    private Board board;
    private JPanel boardPanel;
    private ArrayList<Point> points;
    private int counter = 0;

    public BoardGUI(int boardSize1,int boardSize2) {
        board = new Board(boardSize1,boardSize2);
        boardPanel = new JPanel();
        points = new ArrayList<>();
        boardPanel.setLayout(new GridLayout(board.getBoardSize1(), board.getBoardSize2()));
        buttons = new JButton[board.getBoardSize1()][board.getBoardSize2()];
        for (int i = 0; i < board.getBoardSize1(); ++i) {
            for (int j = 0; j < board.getBoardSize2(); ++j) {
                JButton button = new JButton();
                button.addActionListener(new ButtonListener(i, j));
                button.setPreferredSize(new Dimension(60, 60));
                button.setFont(new Font("Arial", Font.PLAIN, 20));
                buttons[i][j] = button;
                boardPanel.add(button);
                points.add(new Point(i, j));
            }
        }
    }

    public void refresh(int x, int y) {
        for(int i = board.getBoardSize1() - 1; i >= 0;i--) {
            if(board.get(i, y).getCharacter().equals("")) {
                Field field = board.get(i, y);
                board.get(i, y).setCharacter(counter);
                JButton button = buttons[i][y];
                button.setText(field.getCharacter());
                counter++;
                break;
            }
        }
       }
    public void setEmpty() {
        for (int i = 0; i < board.getBoardSize1(); ++i) {
            for (int j = 0; j < board.getBoardSize2(); ++j) {
                Field field = board.get(i, j);
                board.get(i, j).setEmpty();
                JButton button = buttons[i][j];
                button.setText(field.getCharacter());
            }
        }
    }

    public JPanel getBoardPanel() {
        return boardPanel;
    }

    class ButtonListener implements ActionListener {

        private int x, y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board.get(x, y).getCharacter().equals("")) {
                    refresh(x,y);
                }
            if (!board.isWon().equals("")) {
                JOptionPane.showMessageDialog(boardPanel, board.isWon() + " Won","Game ended", JOptionPane.PLAIN_MESSAGE);
                counter = 0;
                setEmpty();
            }
            if (board.isDraw()) {
                JOptionPane.showMessageDialog(boardPanel, "The game ends in a draw","Game ended", JOptionPane.PLAIN_MESSAGE);
                counter = 0;
                setEmpty();
            }
        }          
        }
    }
