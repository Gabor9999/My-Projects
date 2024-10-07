package JavaChess;

import javax.swing.*;  
import java.awt.event.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Board board = new Board(true);
            ChessBoard chessBoard = new ChessBoard(board);

            JMenuBar mb = new JMenuBar();  
            JMenu menu = new JMenu("Menu");  
            JMenuItem WhitePieces = new JMenuItem("Play as White");
            WhitePieces.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){
                    Board newboard = new Board(true);
                    ChessBoard newChessBoard = new ChessBoard(newboard);
                    frame.getContentPane().removeAll();
                    frame.add(newChessBoard);
                    frame.pack();
                }  
                });
            JMenuItem BlackPieces = new JMenuItem("Play as Black");
            BlackPieces.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    Board newboard = new Board(false);
                    ChessBoard newChessBoard = new ChessBoard(newboard);
                    frame.getContentPane().removeAll();
                    frame.add(newChessBoard);
                    frame.pack();
                }  
                });
            JMenuItem Exit = new JMenuItem("Exit");
            Exit.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    System.exit(0);
                }  
                });
            menu.add(WhitePieces);
            menu.add(BlackPieces);
            menu.add(Exit);
            mb.add(menu);
            frame.setJMenuBar(mb);

            frame.add(chessBoard);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
