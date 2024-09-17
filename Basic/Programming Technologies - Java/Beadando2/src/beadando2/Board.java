/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando2;

import java.awt.Point;
//import java.util.ArrayList;
/**
 *
 * @author mgabo
 */
public class Board {
    private Field[][] board;
    private final int boardSize1;
    private final int boardSize2;

    public Board(int boardSize1,int boardSize2) {
        this.boardSize1 = boardSize1;
        this.boardSize2 = boardSize2;
        board = new Field[this.boardSize1][this.boardSize2];
        for (int i = 0; i < this.boardSize1; ++i) {
            for (int j = 0; j < this.boardSize2; ++j) {
                board[i][j] = new Field();
            }
        }
    }
    public String isWon() {
        for (int i = boardSize1 - 1; i >= 0; i--) {
            for (int j = 0; j < boardSize2; j++) {
                if (j <= boardSize2 - 4) {
                    //sor
                    if (board[i][j].getCharacter().equals(board[i][j+1].getCharacter()) && board[i][j+2].getCharacter().equals(board[i][j+3].getCharacter())
                            && board[i][j].getCharacter().equals(board[i][j+3].getCharacter()) && !(board[i][j].getCharacter().equals(""))) {
                        return board[i][j].getCharacter();
                    }
                    //átló1
                    if (i > 2) {
                        if (board[i][j].getCharacter().equals(board[i-1][j+1].getCharacter()) && board[i-2][j+2].getCharacter().equals(board[i-3][j+3].getCharacter())
                            && board[i][j].getCharacter().equals(board[i-3][j+3].getCharacter()) && !(board[i][j].getCharacter().equals(""))) {
                        return board[i][j].getCharacter();
                        }
                    }
                    //átló2
                    if (i < boardSize1 - 3) {
                        if (board[i][j].getCharacter().equals(board[i+1][j+1].getCharacter()) && board[i+2][j+2].getCharacter().equals(board[i+3][j+3].getCharacter())
                            && board[i][j].getCharacter().equals(board[i+3][j+3].getCharacter()) && !(board[i][j].getCharacter().equals(""))) {
                        return board[i][j].getCharacter();
                        }
                    }
                }
            }
        }
        return "";
    }
    
    public boolean isDraw() {
        for (int i = 0; i < boardSize1; i++) {
            for (int j = 0; j < boardSize2; j++) {
                if (board[i][j].getCharacter().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Field get(int x, int y) {
        return board[x][y];
    }
    
    public Field get(Point point) {
        int x = (int)point.getX();
        int y = (int)point.getY();
        return get(x, y);
    }

    public int getBoardSize1() {
        return boardSize1;
    }
    
    public int getBoardSize2() {
        return boardSize2;
    }

}
