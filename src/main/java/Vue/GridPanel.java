package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class GridPanel extends JPanel {
    private final int cellSize; // Size of grid cells (constant)
    private final Color gridColor = Color.GRAY; // Grid line color

    public GridPanel(int cellSize,Graphics2D g) {
        this.cellSize = cellSize; // Fixed grid cell size
        setBackground(Color.WHITE); // Background color
        drawGrid(g);
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(gridColor); // Set grid color
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Draw vertical grid lines
        for (int x = 0; x <= panelWidth; x += cellSize) {
            g.drawLine(x, 0, x, panelHeight);
        }

        // Draw horizontal grid lines
        for (int y = 0; y <= panelHeight; y += cellSize) {
            g.drawLine(0, y, panelWidth, y);
        }
    }

    private void drawCoordinates(Graphics2D g) {
        g.setColor(Color.BLACK);
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Draw X-axis coordinates
        for (int x = 0; x <= panelWidth; x += cellSize) {
            g.drawString(String.valueOf(x / cellSize), x + 2, 12);
        }

        // Draw Y-axis coordinates
        for (int y = 0; y <= panelHeight; y += cellSize) {
            g.drawString(String.valueOf(y / cellSize), 2, y - 2);
        }
    }

    // Snap a point to the nearest grid corner
    public Point2D.Double snapToGrid(double mouseX, double mouseY) {
        double snappedX = Math.round(mouseX / cellSize) * cellSize;
        double snappedY = Math.round(mouseY / cellSize) * cellSize;
        return new Point2D.Double(snappedX, snappedY);
    }
}
