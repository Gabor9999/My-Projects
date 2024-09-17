package Week2;

public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void shift(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void mirror(int x, int y) {
        this.x = 2 * x - this.x;
        this.y = 2 * y - this.y;
    }
}
