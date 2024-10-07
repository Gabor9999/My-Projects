package JavaChess;

import javax.swing.*;
import java.awt.*;

public class ChessSquare extends JButton{
    private boolean showCircle;

    public ChessSquare() {
        super();
        this.showCircle = false;
        setLayout(new BorderLayout());
    }

    public void setShowCircle(boolean showCircle) {
        this.showCircle = showCircle;
        repaint();
    }

    public boolean getShowCircle() {
        return this.showCircle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // First, paint the original button (background, icon, etc.)

        if (showCircle) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 120));
            
            int diameter = getHeight() / 5;
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            g2.fillOval(x, y, diameter, diameter);
        }
    }
}
