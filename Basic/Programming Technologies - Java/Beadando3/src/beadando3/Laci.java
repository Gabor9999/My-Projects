package beadando3;

import java.awt.Image;

/**
 *
 * @author mgabo
 */
public class Laci extends Sprite {

    public Laci(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }

    public void moveX(int move) {
        if (x + move >= 0 && x + width + move + 20 <= 1600) {
            x += move;
        }
    }
    
    public void moveY(int move) {
        if (y + move >= 0 && y + height + move + 60 <= 900) {
            y += move;
        }
    }
}
