package javaapplication8;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TableExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Table Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Sprite 1", "Sprite 2", "Sprite 3"};
        Object[][] data = new Object[3][3];

        // create some example data
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                BufferedImage sprite = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
                Graphics g = sprite.getGraphics();
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, 32, 32);
                data[i][j] = sprite;
            }
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        // set the custom renderer for the cells
        table.setDefaultRenderer(Object.class, new SpriteRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private static class SpriteRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof BufferedImage) {
                BufferedImage sprite = (BufferedImage) value;

                if (component.getGraphics() != null) {
                    int x = component.getX() + (component.getWidth() - sprite.getWidth()) / 2;
                    int y = component.getY() + (component.getHeight() - sprite.getHeight()) / 2;
                    Graphics g = component.getGraphics();
                    g.drawImage(sprite, x, y, null);
                }
            }

            return component;
        }
    }
}