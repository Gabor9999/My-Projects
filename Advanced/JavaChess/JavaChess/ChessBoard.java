package JavaChess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChessBoard extends JPanel{
    private final Piece[][] board;
    private final Map<Piece, String> pieceImages;
    private final ChessSquare[][] buttons;
    private final boolean playingWhite;
    private boolean whiteTurn;
    private boolean isChecked;
    private int blackDoublePush;
    private int whiteDoublePush;
    private int blackKingPos;
    private int whiteKingPos;
    private ArrayList<Integer> possibleMoves;
    private Map<Integer, ArrayList<Integer>> allPossibleMoves;
    private int expectedMove;

    public ChessBoard(Board chessBoard) {
        this.board = chessBoard.getBoard();
        this.pieceImages = loadPieceImages();
        this.buttons = new ChessSquare[8][8];
        this.playingWhite = chessBoard.getColor();
        this.whiteTurn = this.playingWhite;
        this.blackDoublePush = -1;
        this.whiteDoublePush = -1;
        this.expectedMove = -1;
        if(playingWhite) {
            this.blackKingPos = 3;
            this.whiteKingPos = 73;
        } else {
            this.whiteKingPos = 3;
            this.blackKingPos = 73;
        }
        possibleMoves = new ArrayList<>();
        allPossibleMoves = new HashMap<>();
        this.isChecked = false;
        setLayout(new GridLayout(8, 8));
        createBoard();
    }

    private Map<Piece, String> loadPieceImages() {
        Map<Piece, String> images = new HashMap<>();
        images.put(Piece.W_ROOK, "images/white-rook.png");
        images.put(Piece.W_KNIGHT, "images/white-knight.png");
        images.put(Piece.W_BISHOP, "images/white-bishop.png");
        images.put(Piece.W_QUEEN, "images/white-queen.png");
        images.put(Piece.W_KING, "images/white-king.png");
        images.put(Piece.W_PAWN, "images/white-pawn.png");

        images.put(Piece.B_ROOK, "images/black-rook.png");
        images.put(Piece.B_KNIGHT, "images/black-knight.png");
        images.put(Piece.B_BISHOP, "images/black-bishop.png");
        images.put(Piece.B_QUEEN, "images/black-queen.png");
        images.put(Piece.B_KING, "images/black-king.png");
        images.put(Piece.B_PAWN, "images/black-pawn.png");

        return images;
    }

    private void addMoveIfValid(int target_row, int target_col, int original_row, int original_col) {
        if (target_row >= 0 && target_row < 8 && target_col >= 0 && target_col < 8) {
            Character targetpiece = board[target_row][target_col].getSymbol();
            Integer target_num = target_row*10+target_col;
            if ((whiteTurn && Character.isLowerCase(targetpiece)) || 
                (!whiteTurn && Character.isUpperCase(targetpiece)) || targetpiece == ' ') {
                if(isChecked) {
                    Piece temp = board[target_row][target_col];
                    Piece og_temp = board[original_row][original_col];
                    board[target_row][target_col] = og_temp;
                    board[original_row][original_col] = Piece.EMPTY;
                    Integer kingPosition = whiteTurn ? (og_temp == Piece.W_KING ? target_num : whiteKingPos) : (og_temp == Piece.B_KING ? target_num : blackKingPos);
                    whiteTurn = !whiteTurn;
                    isChecked = false;
                    ArrayList<Integer> tempPosMoves = new ArrayList<>(possibleMoves);
                    Map<Integer, ArrayList<Integer>> tempMoves = new HashMap<>(allPossibleMoves);
                    boolean res = checkForCheck(kingPosition);
                    board[target_row][target_col] = temp;
                    board[original_row][original_col] = og_temp;
                    whiteTurn = !whiteTurn;
                    isChecked = true;
                    possibleMoves = tempPosMoves;
                    allPossibleMoves = tempMoves;
                    if(res) {
                        return;
                    }
                }
                possibleMoves.add(target_num);
            }
        }
    }

    private void printBoard() {
        System.out.println("--------------------------------------------------");
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                System.out.print(board[r][c].getSymbol() + " ");
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
    }

    // TODO
    // Glitch: Castle Through Check Cancel
    // Glitch: w_b takes b_p
    // Glitch: double move to checing piece
    private void handlePawnMoves(int cur_row, int cur_col, boolean mainPlay) {
        int forward = mainPlay ? -1:1;
        int startRow = mainPlay ? 6:1;

        addMoveIfValid(cur_row + forward, cur_col, cur_row, cur_col);

        if (cur_row == startRow && board[cur_row + forward][cur_col] == Piece.EMPTY && board[cur_row + 2*forward][cur_col] == Piece.EMPTY) {
            addMoveIfValid(cur_row + 2 * forward, cur_col, cur_row, cur_col);
        }
        if(cur_col != 0) {
            if(board[cur_row + forward][cur_col-1] != Piece.EMPTY) {
                addMoveIfValid(cur_row + forward, cur_col - 1, cur_row, cur_col);
            }
            else if(cur_row == 4 || cur_row == 3) {
                if(whiteTurn) {
                    if(blackDoublePush == cur_col - 1) {
                        addMoveIfValid(cur_row + forward, cur_col - 1, cur_row, cur_col);
                    }
                } else if (whiteDoublePush == cur_col - 1) {
                    addMoveIfValid(cur_row + forward, cur_col - 1, cur_row, cur_col);
                }
            }
        }
        if(cur_col != 7) {
            if(board[cur_row + forward][cur_col+1] != Piece.EMPTY) {
                addMoveIfValid(cur_row + forward, cur_col + 1, cur_row, cur_col);
            }
            else if(cur_row == 4 || cur_row == 3) {
                if(whiteTurn) {
                    if(blackDoublePush == cur_col + 1) {
                        addMoveIfValid(cur_row + forward, cur_col + 1, cur_row, cur_col);
                    }
                } else if (whiteDoublePush == cur_col + 1) {
                    addMoveIfValid(cur_row + forward, cur_col + 1, cur_row, cur_col);
                }
            }
        }
    }

    private void handleRookMoves(int row, int col, int lim, int inc) {
        for(int get_row = row; get_row != lim; get_row += inc) {
            addMoveIfValid(get_row, col-inc, row, col);
            if(board[get_row][col-inc] != Piece.EMPTY) {
                break;
            }
        }
        for(int get_col = col; get_col != lim; get_col += inc) {
            addMoveIfValid(row-inc, get_col, row, col);
            if(board[row-inc][get_col] != Piece.EMPTY) {
                break;
            }
        }
    }

    private void handleKnightMoves(int cur_row, int cur_col) {
        addMoveIfValid(cur_row+1, cur_col-2, cur_row, cur_col);
        addMoveIfValid(cur_row+1, cur_col+2, cur_row, cur_col);
        addMoveIfValid(cur_row-1, cur_col+2, cur_row, cur_col);
        addMoveIfValid(cur_row-1, cur_col-2, cur_row, cur_col);
        addMoveIfValid(cur_row+2, cur_col-1, cur_row, cur_col);
        addMoveIfValid(cur_row+2, cur_col+1, cur_row, cur_col);
        addMoveIfValid(cur_row-2, cur_col-1, cur_row, cur_col);
        addMoveIfValid(cur_row-2, cur_col+1, cur_row, cur_col);
    }

    private void handleBishopMoves(int Row, int Col, int inc1, int inc2) {
        int lim1 = inc1 > 0 ? 8 : -1;
        int lim2 = inc2 > 0 ? 8 : -1;
        int og_row = Row;
        int og_col = Col;
        while(lim1 != Row && lim2 != Col){
            addMoveIfValid(Row, Col, og_row, og_col);
            if(board[Row][Col] != Piece.EMPTY) {
                break;
            }
            Row += inc1;
            Col += inc2;
        }
    }

    private void handleQueenMoves(int cur_row, int cur_col) {
        handleBishopMoves(cur_row+1,cur_col+1,1,1);
        handleBishopMoves(cur_row+1,cur_col-1,1,-1);
        handleBishopMoves(cur_row-1,cur_col+1,-1,1);
        handleBishopMoves(cur_row-1,cur_col-1,-1,-1);
        handleRookMoves(cur_row+1,cur_col+1, 8, 1);
        handleRookMoves(cur_row-1,cur_col-1, -1, -1);
    }

    private void handleKingMoves(int cur_row, int cur_col) {
        addMoveIfValid(cur_row+1, cur_col-1, cur_row, cur_col);
        addMoveIfValid(cur_row+1, cur_col, cur_row, cur_col);
        addMoveIfValid(cur_row+1, cur_col+1, cur_row, cur_col);
        addMoveIfValid(cur_row, cur_col-1, cur_row, cur_col);
        addMoveIfValid(cur_row, cur_col+1, cur_row, cur_col);
        addMoveIfValid(cur_row-1, cur_col-1, cur_row, cur_col);
        addMoveIfValid(cur_row-1, cur_col+1, cur_row, cur_col);
        addMoveIfValid(cur_row-1, cur_col, cur_row, cur_col);
        if((cur_row == 0 || cur_row == 7) && cur_col == 3) {
            boolean canCastle = true;
            for(int i = 2; i > 0; i--) {
                if(board[cur_row][i] != Piece.EMPTY || checkForCheck(cur_row * 10 + i)) {
                    canCastle = false;
                    break;
                }
            }
            if(canCastle && board[cur_row][0].getWorth() == 5) {
                possibleMoves.add(cur_row *10 + cur_col - 2);
            }
            canCastle = true;
            for(int i = 4; i < 7; i++) {
                if(board[cur_row][i] != Piece.EMPTY) {
                    canCastle = false;
                    break;
                }
            }
            if(canCastle && board[cur_row][7].getWorth() == 5) {
                if(!checkForCheck(cur_row * 10 + 4) && !checkForCheck(cur_row * 10 + 5)) {
                    possibleMoves.add(cur_row *10 + cur_col + 2);
                }  
            }

        }
    }

    private void promotePawn(char symbol, Consumer<Piece> callback) {
        JFrame frame = new JFrame("Promote the pawn");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 4));

        ChessSquare p1 = new ChessSquare();
        p1.setFocusPainted(false);
        ChessSquare p2 = new ChessSquare();
        ChessSquare p3 = new ChessSquare();
        ChessSquare p4 = new ChessSquare();

        if(Character.isUpperCase(symbol)) {
            p1.setIcon(new ImageIcon(pieceImages.get(Piece.W_KNIGHT)));
            p1.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.W_KNIGHT);
                    frame.dispose();
                }  
                });
            p2.setIcon(new ImageIcon(pieceImages.get(Piece.W_BISHOP)));
            p2.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.W_BISHOP);
                    frame.dispose();
                }  
                });
            p3.setIcon(new ImageIcon(pieceImages.get(Piece.W_ROOK)));
            p3.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.W_ROOK);
                    frame.dispose();
                }  
                });
            p4.setIcon(new ImageIcon(pieceImages.get(Piece.W_QUEEN)));
            p4.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.W_QUEEN);
                    frame.dispose();
                }  
                });
        } else {
            p1.setIcon(new ImageIcon(pieceImages.get(Piece.B_KNIGHT)));
            p1.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.B_KNIGHT);
                    frame.dispose();
                }  
                });
            p2.setIcon(new ImageIcon(pieceImages.get(Piece.B_BISHOP)));
            p2.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.B_BISHOP);
                    frame.dispose();
                }  
                });
            p3.setIcon(new ImageIcon(pieceImages.get(Piece.B_ROOK)));
            p3.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.B_ROOK);
                    frame.dispose();
                }  
                });
            p4.setIcon(new ImageIcon(pieceImages.get(Piece.B_QUEEN)));
            p4.addActionListener(new ActionListener(){  
                public void actionPerformed(ActionEvent e){  
                    callback.accept(Piece.B_QUEEN);
                    frame.dispose();
                }  
                });
        }
        frame.add(p1);
        frame.add(p2);
        frame.add(p3);
        frame.add(p4);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); 
    }

    private boolean checkForCheck(int kingPosition) {
        getAllValidMoves();
        for (Integer key : allPossibleMoves.keySet()) {
            for (int move : allPossibleMoves.get(key)) {
                if (move == kingPosition) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean checkForMate() {
        for (ArrayList<Integer> value : allPossibleMoves.values()) {
            if (!value.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void makeMove(int cur_row, int cur_col) {
        System.out.println(allPossibleMoves);
        final int og_row = (int)(expectedMove / 10);
        final int og_col = expectedMove % 10;
        //pieceMoves.remove(0);
        if(allPossibleMoves.get(expectedMove).contains(cur_row*10+cur_col)) {
            Piece piece = board[og_row][og_col];
            if(piece.getWorth() == 1 && (cur_row == 0 || cur_row == 7)) {
                promotePawn(piece.getSymbol(), selectedPiece -> {
                    board[cur_row][cur_col] = selectedPiece;
                    buttons[cur_row][cur_col].setIcon(new ImageIcon(pieceImages.get(selectedPiece)));
                });
            } else {
                if (piece.getWorth() == 1) {
                    if(Math.abs(og_row - cur_row) == 2) {
                        if(Character.isUpperCase(piece.getSymbol())) {
                            whiteDoublePush = cur_col;
                        } else {
                            blackDoublePush = cur_col;
                        }
                    }
                    if(board[cur_row][cur_col] == Piece.EMPTY && board[og_row][cur_col] != Piece.EMPTY) {
                        board[og_row][cur_col] = Piece.EMPTY;
                        buttons[og_row][cur_col].setIcon(null);
                    }
                } else if(piece.getWorth() == 10) {
                    if(Math.abs(og_col - cur_col) == 2) {
                        if(cur_col < 3) {
                            board[cur_row][cur_col+1] = board[cur_row][0];
                            buttons[cur_row][cur_col+1].setIcon(new ImageIcon(pieceImages.get(board[cur_row][cur_col+1])));
                            board[cur_row][0] = Piece.EMPTY;
                            buttons[cur_row][0].setIcon(null);
                        } else {
                            board[cur_row][cur_col-1] = board[cur_row][7];
                            buttons[cur_row][cur_col-1].setIcon(new ImageIcon(pieceImages.get(board[cur_row][cur_col-1])));
                            board[cur_row][7] = Piece.EMPTY;
                            buttons[cur_row][7].setIcon(null);
                        }
                    }
                    if(Character.isUpperCase(piece.getSymbol())) {
                        whiteKingPos = cur_row * 10 + cur_col;
                    } else {
                        blackKingPos = cur_row * 10 + cur_col;
                    }
                }
                board[cur_row][cur_col] = piece;
                buttons[cur_row][cur_col].setIcon(new ImageIcon(pieceImages.get(piece)));
            }
            board[og_row][og_col] = Piece.EMPTY;
            buttons[og_row][og_col].setIcon(null);
            for(Integer move : allPossibleMoves.get(expectedMove)) {
                buttons[(int) (move / 10)][move % 10].setShowCircle(false);
            }
            if(whiteTurn) {
                isChecked = checkForCheck(blackKingPos);
                blackDoublePush = -1;
            } else {
                isChecked = checkForCheck(whiteKingPos);
                whiteDoublePush = -1;  
            }
            whiteTurn = !whiteTurn;
            getAllValidMoves();
            repaint();
            if(checkForMate()) {
                JFrame frame = new JFrame("Game ended");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new GridLayout(1, 4));

                JLabel label = new JLabel();

                String text = isChecked ? (whiteTurn ? "Black won by checkmate" : "White won by checkmate") : "Draw by stalemate";
 
                label.setText(text);

                frame.add(label);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true); 
            }
        } else {
            for(Integer move : allPossibleMoves.get(expectedMove)) {
                buttons[(int) (move / 10)][move % 10].setShowCircle(false);
            }
        }
        expectedMove = -1;
    }

    private void getAllValidMoves() {
        allPossibleMoves.clear();
        /*Piece temp = board[cur_row][cur_col];
        board[cur_row][cur_col] = Piece.EMPTY;
        Integer kingPosition = Character.isUpperCase(symbol) ? whiteKingPos : blackKingPos;
        if(Character.toUpperCase(symbol) != 'K' && checkForCheck(kingPosition)) {
            return possibleMoves;
        }
        board[cur_row][cur_col] = temp;
        System.out.println("BREAK");
        printBoard();*/
        for (int rows = 0; rows < 8; rows++) {
            for (int cols = 0; cols < 8; cols++) {
                Character piece = board[rows][cols].getSymbol();
                
                if (piece == ' ' || (!whiteTurn && Character.isUpperCase(piece)) ||
                    (whiteTurn && Character.isLowerCase(piece))) {
                    continue;
                }
                //System.out.println("Row: " + rows + " " + "Col: " + cols + " -> " + whiteTurn);
                possibleMoves.clear();
                switch (piece) {
                    case 'P':
                        if(playingWhite) {
                            handlePawnMoves(rows, cols,true);
                        } else {
                            handlePawnMoves(rows, cols,false);
                        }
                        break;
                    case 'p':
                        if(playingWhite) {
                            handlePawnMoves(rows, cols,false);
                        } else {
                            handlePawnMoves(rows, cols,true);
                        }
                        break;
                    case 'R':
                    case 'r':
                        handleRookMoves(rows+1,cols+1, 8, 1);
                        handleRookMoves(rows-1,cols-1, -1, -1);
                        break;
                    case 'n':
                    case 'N':
                        handleKnightMoves(rows, cols);
                        break;
                    case 'b':
                    case 'B':
                        handleBishopMoves(rows+1,cols+1,1,1);
                        handleBishopMoves(rows+1,cols-1,1,-1);
                        handleBishopMoves(rows-1,cols+1,-1,1);
                        handleBishopMoves(rows-1,cols-1,-1,-1);
                        break;
                    case 'k':
                    case 'K':
                        handleKingMoves(rows, cols);
                        break;
                    case 'q':
                    case 'Q':
                        handleQueenMoves(rows, cols);
                        break;
                    default:
                        break;
                }
                allPossibleMoves.put(rows*10+cols, new ArrayList<>(possibleMoves));
            }
        }
    }

    private void createBoard() {
        boolean isDarkSquare = false;
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                ChessSquare square = new ChessSquare();
                square.setFocusPainted(false);
                square.setBorderPainted(false);

                if (isDarkSquare) {
                    square.setBackground(new Color(139, 69, 19));
                } else {
                    square.setBackground(new Color(222, 184, 135));
                }
                isDarkSquare = !isDarkSquare;

                Piece piece = board[row][col];
                if (piece != Piece.EMPTY) {
                    square.setIcon(new ImageIcon(pieceImages.get(piece)));
                }

                final int cur_row = row;
                final int cur_col = col;

                buttons[row][col] = square;

                square.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(expectedMove != -1) {
                            makeMove(cur_row, cur_col);
                            return;
                        }
                        Character pieceSymbol = board[cur_row][cur_col].getSymbol();
                        if(pieceSymbol == ' ' || (Character.isUpperCase(pieceSymbol) && !whiteTurn) || (!Character.isUpperCase(pieceSymbol) && whiteTurn)) {return;}
                        expectedMove = cur_row*10 + cur_col;
                        for(Integer move : allPossibleMoves.get(expectedMove)) {
                            if(board[(int) (move / 10)][move % 10] == Piece.EMPTY) {
                                buttons[(int) (move / 10)][move % 10].setShowCircle(true);
                            }
                        }
                        /*
                        pieceMoves.add(cur_row*10 + cur_col);
                        pieceMoves.addAll(getValidMoves(pieceSymbol, cur_row, cur_col, true));*/
                    }
                });
                add(square);
            }

            isDarkSquare = !isDarkSquare;
        }
        getAllValidMoves();
    }
}