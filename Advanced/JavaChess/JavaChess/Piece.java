package JavaChess;

public enum Piece {
    W_KING('K', 10), W_QUEEN('Q', 9), W_KNIGHT('N', 3), W_BISHOP('B', 3), W_ROOK('R', 5), W_PAWN('P', 1),
    B_KING('k', 10), B_QUEEN('q', 9), B_KNIGHT('n', 3), B_BISHOP('b', 3), B_ROOK('r', 5), B_PAWN('p', 1),
    EMPTY(' ', 0);

    private final char symbol;
    private final int worth;
    Piece(Character symbol, int worth) {
        this.symbol = symbol;
        this.worth = worth;
    }

    public Character getSymbol() {
        return this.symbol;
    }

    public int getWorth() {
        return this.worth;
    }
}