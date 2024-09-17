package beadando3;

import java.awt.Image;
import java.awt.Rectangle;
/**
 *
 * @author mgabo
 */
public class Or extends Sprite {
    private int velx = 20;
    private int vely = 20;
    private boolean decide;

    public Or(int x, int y, int width, int height,boolean decide_, Image image) {
        super(x, y, width, height, image);
        this.decide = decide_;
    }
    @Override
    public boolean collides(Sprite other) {
        Rectangle rect = new Rectangle(x, y, width*2, height+width);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);        
        return rect.intersects(otherRect);
    }
    
    public void move() {
        boolean a = (x - Math.abs(velx) >= 0 && x + width + Math.abs(velx) + 20 <= 1600);
        boolean b = (y - Math.abs(vely) >= 0 && y + height + Math.abs(vely) + 60 <= 900);
        if (a && b) {
            if(decide){
                x += velx;
            }
            else {
                y += vely;
            }
        }
        else if(a) {
            vely = -vely;
            y += vely;
        }
        else if(b) {
            velx = -velx;
            x += velx;
        }
        else {
           velx = -velx;
           vely = -vely;
           x += velx;
           y += vely;
        }
    }
    
    public void cVelx() {
        velx = -velx;
        x += velx;
    }
    public void cVely() {
        vely = -vely;
        y += vely;
    }
    public boolean getDecide() {
        return decide;
    }
}