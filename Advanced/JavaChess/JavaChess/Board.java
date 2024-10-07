package JavaChess;

public class Board {
    private final Piece[][] board;
    private final boolean playingWhite;

    public Board(boolean isWhite) {
        this.board = new Piece[8][8];
        this.playingWhite = isWhite;
        initializeBoard(isWhite);
    }

    private void initializeBoard(boolean isWhite) {
        if(isWhite) {
            // Black pieces
            board[0] = new Piece[]{Piece.B_ROOK, Piece.B_KNIGHT, Piece.B_BISHOP, Piece.B_KING, Piece.B_QUEEN, Piece.B_BISHOP, Piece.B_KNIGHT, Piece.B_ROOK};
            board[1] = new Piece[]{Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN};
            // White pieces
            board[6] = new Piece[]{Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN};
            board[7] = new Piece[]{Piece.W_ROOK, Piece.W_KNIGHT, Piece.W_BISHOP, Piece.W_KING, Piece.W_QUEEN, Piece.W_BISHOP, Piece.W_KNIGHT, Piece.W_ROOK};
        } else {
            // White pieces
            board[0] = new Piece[]{Piece.W_ROOK, Piece.W_KNIGHT, Piece.W_BISHOP, Piece.W_KING, Piece.W_QUEEN, Piece.W_BISHOP, Piece.W_KNIGHT, Piece.W_ROOK};
            board[1] = new Piece[]{Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN, Piece.W_PAWN};
            // Black pieces
            board[6] = new Piece[]{Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN, Piece.B_PAWN};
            board[7] = new Piece[]{Piece.B_ROOK, Piece.B_KNIGHT, Piece.B_BISHOP, Piece.B_KING, Piece.B_QUEEN, Piece.B_BISHOP, Piece.B_KNIGHT, Piece.B_ROOK};
        }

        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }
    }

    public Piece[][] getBoard() {
        return board;
    }

    public boolean getColor() {
        return playingWhite;
    }
}
