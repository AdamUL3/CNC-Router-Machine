package Vue;


import Domain.DTO.*;
import Domain.Controleur;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.UUID;


public class DrawingPanel extends JPanel {
    int panelWidth = 900;
    int panelHeight = 600;


    private CNCDTO currentCNCDTO;
    private boolean MagnetiserlaGrille1;
    private Controleur controleur;
    private final double defaultScale = 0.8;
    private final double linewidthDefaultScale = 3;
    private double zoomFactor = 1;
    private double offsetX = 0;
    private String sizegrille="1";
    private String measurement="1";
    private double offsetY = 0;
    private boolean drawGrid;
    private boolean zooming = false;
    Timer zoomTimer = new Timer(10, e -> zooming = false);
    private UUID selectedElement = null;
    private UUID hoveredElement = null;
    private MouseEvent mousePosition;

    // CONSTRUCTEUR, QUI INCLUS LES LISTENERS ET LES PARAMÈTRES PAR DÉFAUT
    public DrawingPanel() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.LIGHT_GRAY);


        // GESTION DU ZOOM : NE PAS TOUCHER
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!zooming) {
                    zooming = true;
                    zoomTimer.restart();
                    double scaleAmount = (e.getPreciseWheelRotation() < 0) ? 1.1 : 0.9;

                    Point2D.Double beforeZoom = convertMouseCoord(e);

                    zoomFactor *= scaleAmount;

                    if (zoomFactor > 50) { zoomFactor = 50; }
                    else if (zoomFactor < 0.01) { zoomFactor = 0.01; }

                    Point2D.Double afterZoom = convertMouseCoord(e);

                    offsetX += (afterZoom.x - beforeZoom.x) * zoomFactor * defaultScale;
                    offsetY += (afterZoom.y - beforeZoom.y) * zoomFactor * defaultScale;

                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                checkHoveredElement(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleGridSnapping(e.getX(), e.getY()); // Call your method here
            }
        });
    }

    public void checkHoveredElement(MouseEvent e) {
        Point2D.Double mouseCoord = convertMouseCoord(e);
        UUID elem = controleur.getElemPanel(mouseCoord.x, mouseCoord.y);
        if (currentCNCDTO.getPanneau() != null) {
            if (!currentCNCDTO.getPanneau().getId().equals(elem)) {
                hoveredElement = elem;
                mousePosition = e;
                repaint();
                return;
            }
        }
        if (hoveredElement != null) {
            hoveredElement = null;
            mousePosition = null;
            repaint();
        }
    }


    public void setCurrentDTO(CNCDTO cncdto) {
        this.currentCNCDTO = cncdto;
    }

    public void setControleur(Controleur controleur) { this.controleur = controleur; }


    // FONCTION QUI CONVERTIT COORDONNÉS DE BASE EN COORDONÉES ZOOMÉS


    public Point2D.Double convertMouseCoord(MouseEvent e) {
        return PanelCoordToNormalCoord(e.getX(), getHeight() - e.getY());
    }

    private Point2D.Double PanelCoordToNormalCoord(double x, double y) {
        double worldX = (x - offsetX) / (zoomFactor * defaultScale);
        double worldY = (y - offsetY) / (zoomFactor * defaultScale);
        return new Point2D.Double(worldX, worldY);
    }

    // FONCTION QUI CONVERTIT COORDONÉES ZOOMÉS EN COORDONNÉES DE BASE
    private Point2D.Double NormalCoordToPanelCoord(double x, double y) {
        double panelX = x * zoomFactor * defaultScale + offsetX;
        double panelY = y * zoomFactor * defaultScale + offsetY;
        return new Point2D.Double(panelX, panelY);
    }

    private double DimensionToPanelCoord(double dim) {
        return dim * zoomFactor * defaultScale;
    }


    // FONCTION QUI S'OCCUPE DE L'AFFICHAGE (LE COEUR DU DRAWINGPANEL)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform originalTransform = g2.getTransform();
        g2.translate(0, getHeight());
        g2.scale(1, -1);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //System.out.println("Zoom : " + zoomFactor + ", Offset X : " + offsetX + ", Offset Y : " + offsetY);

        if (currentCNCDTO.getPanneau() != null) {
            drawPanneau(g2);
        }
        if (!currentCNCDTO.getParalleleListe().isEmpty()) {
            drawCoupesParallele(g2);
        }
        if (!currentCNCDTO.getEnBordureListe().isEmpty()) {
            drawCoupesEnBordure(g2);
        }
        if (!currentCNCDTO.getEnLListe().isEmpty()) {
            drawCoupesEnL(g2);
        }
        if (!currentCNCDTO.getRectangulaireListe().isEmpty()) {
            drawCoupesRectangulaire(g2);
        }
        if (!currentCNCDTO.getIntersectionListe().isEmpty()) {
            drawIntersection(g2);
        }
        if (!currentCNCDTO.getEnLListe().isEmpty()) {
            drawCoupesEnLPoints(g2);
        }
        if (!currentCNCDTO.getRectangulaireListe().isEmpty()) {
            drawCoupesRectangulairePoints(g2);
        }
        if (drawGrid){
            drawGrillefonct(g2);
        }
        if (currentCNCDTO.getPanneau() != null) {
            drawZoneInterdite(g2);
        }

        if (hoveredElement != null) {
            g2.setTransform(originalTransform);
            drawInfoBox(g2);
        }
    }

    public void drawLigne(Graphics2D g, LigneDTO ligne, UUID ref_id, boolean valid) {
        Point2D.Double p1 = NormalCoordToPanelCoord(ligne.getPoint1().getX(), ligne.getPoint1().getY());
        Point2D.Double p2 = NormalCoordToPanelCoord(ligne.getPoint2().getX(), ligne.getPoint2().getY());
        g.setColor(Color.BLACK);
        if (ref_id == currentCNCDTO.getPanneau().getId()) {
            if (selectedElement == ref_id) {
                g.setColor(Color.BLACK);
            } else {g.setColor(Color.GRAY); }
        }
        else if (!valid) {
            g.setColor(Color.RED);
            if (selectedElement == ref_id) { g.setColor(Color.ORANGE); }
        }
        else if (selectedElement == ref_id) {g.setColor(Color.GREEN);}
        g.setStroke(new BasicStroke((float) (ligne.getWidth()*zoomFactor*defaultScale)));
        if (ligne.isVertical()) {
            g.draw(new Line2D.Double(p1.getX(), p1.getY()+ligne.getWidth()/2*zoomFactor*defaultScale, p2.getX(), p2.getY()-ligne.getWidth()/2*zoomFactor*defaultScale));
        } else {
            g.draw(new Line2D.Double(p1.getX()+ligne.getWidth()/2*zoomFactor*defaultScale, p1.getY(), p2.getX()-ligne.getWidth()/2*zoomFactor*defaultScale, p2.getY()));
        }
    }

    private void drawInfoBox(Graphics2D g2) {
        String info = controleur.getCoupeInformation(hoveredElement);
        if (info == null || info.isEmpty()) return;
        FontMetrics fm = g2.getFontMetrics();

        String[] lines = info.split("\n");
        int boxHeight = fm.getHeight() * lines.length + 4;
        int boxWidth = 0;
        for (String line : lines) {
            boxWidth = Math.max(boxWidth, fm.stringWidth(line));
        }
        boxWidth += 10;

        int boxX = mousePosition.getX() + 10;
        int boxY = mousePosition.getY() + 10;

        g2.setColor(new Color(250, 250, 250));
        g2.fillRect(boxX, boxY, boxWidth, boxHeight);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(boxX, boxY, boxWidth, boxHeight);

        int textY = boxY + fm.getAscent() + 2;
        for (String line : lines) {
            g2.drawString(line, boxX + 5, textY);
            textY += fm.getHeight();
        }
    }

    public void drawPoint(Graphics2D g, CoordinatesDTO pts1, double width, UUID ref_id, boolean valid, Color custom_color, Double custom_width_division) {
        Point2D.Double pts = NormalCoordToPanelCoord(pts1.getX(), pts1.getY());
        g.setColor(Color.BLACK);
        if (custom_color != null && ref_id == selectedElement) { g.setColor(custom_color); }
        else if (ref_id == currentCNCDTO.getPanneau().getId()) {
            if (selectedElement == ref_id) {
                g.setColor(Color.BLACK);
            } else {g.setColor(Color.GRAY); }
        }
        else if (!valid) {
            g.setColor(Color.RED);
            if (selectedElement == ref_id) { g.setColor(Color.YELLOW); }
        }
        else if (selectedElement == ref_id) {g.setColor(Color.GREEN);}

        if (custom_width_division == null) { custom_width_division = 3.5; }
        if (selectedElement != ref_id) { custom_width_division = 3.5;}
        width = width * zoomFactor * defaultScale * linewidthDefaultScale/custom_width_division;
        g.fill(new Ellipse2D.Double(pts.getX() - width * 2, pts.getY() - width * 2, width * 4, width * 4));
    }

    // FONCTION QUI DESSINE LE PANNEAU
    private void drawPanneau(Graphics2D g) {

        Point2D.Double bottomLeftCorner = NormalCoordToPanelCoord(0, 0 );
        g.setColor(Color.BLACK);
        for (LigneDTO ligne : currentCNCDTO.getPanneau().getBordureList()) {
            drawLigne(g, ligne, currentCNCDTO.getPanneau().getId(), true);
        }

        double x_coord = DimensionToPanelCoord(currentCNCDTO.getPanneau().getPanneauLargeur());
        double y_coord = DimensionToPanelCoord(currentCNCDTO.getPanneau().getPanneauHauteur());
        g.setColor(Color.PINK);
        g.fill(new Rectangle2D.Double(bottomLeftCorner.getX(), bottomLeftCorner.getY(), x_coord, y_coord));
    }

    // FONCTION QUI DESSINE LES COUPES

    private void drawCoupesParallele(Graphics2D g) {
        for (ParalleleDTO parallele : currentCNCDTO.getParalleleListe()) {
            for (LigneDTO ligne : parallele.getCoupeLignes()) {
                drawLigne(g, ligne, parallele.getId(), parallele.isValid());
            }
        }
    }

    private void drawCoupesEnBordure(Graphics2D g) {
        for (EnBordureDTO enbordure : currentCNCDTO.getEnBordureListe()) {
            for (LigneDTO ligne : enbordure.getCoupeLignes()) {
                drawLigne(g, ligne, enbordure.getId(), enbordure.isValid());
            }
        }
    }

    private void drawCoupesEnL(Graphics2D g) {
        for (EnLDTO enl : currentCNCDTO.getEnLListe()) {
            for (LigneDTO ligne : enl.getCoupeLignes()) {
                drawLigne(g, ligne, enl.getId(), enl.isValid());
            }
        }
    }

    private void drawCoupesRectangulaire(Graphics2D g) {
        for (RectangulaireDTO rectangulaire : currentCNCDTO.getRectangulaireListe()) {
            for (LigneDTO ligne : rectangulaire.getCoupeLignes()) {
                drawLigne(g, ligne, rectangulaire.getId(), rectangulaire.isValid());
            }
        }
    }

    private void drawIntersection(Graphics2D g) {
        for (IntersectionDTO intersection : currentCNCDTO.getIntersectionListe()) {
            drawPoint(g, intersection.getIntersectionPts(), intersection.getWidth()/2, intersection.getId(), true, null, null);
        }
    }


    public void setSelectedElement(UUID id) {
        this.selectedElement = id;
        repaint();
    }

    public void resetZoom() {
        this.zoomFactor = 1.0;
        this.offsetX = 0;
        this.offsetY = 0;
        repaint();
    }

    private void drawCoupesRectangulairePoints(Graphics2D g) {
        for (RectangulaireDTO rectangulaire : currentCNCDTO.getRectangulaireListe()) {
            drawPoint(g, rectangulaire.getPoint(1), rectangulaire.getOutil().getDiametre(), rectangulaire.getId(), rectangulaire.isValid(), null, 2.0);
            drawPoint(g, rectangulaire.getPoint(2), rectangulaire.getOutil().getDiametre(), rectangulaire.getId(), rectangulaire.isValid(), Color.BLUE, 2.0);
            drawPoint(g, rectangulaire.getPoint(3), rectangulaire.getOutil().getDiametre(), rectangulaire.getId(), rectangulaire.isValid(), Color.YELLOW, 2.0);
        }
    }

    private void drawCoupesEnLPoints(Graphics2D g) {
        for (EnLDTO enl : currentCNCDTO.getEnLListe()) {
            drawPoint(g, enl.getCoin(), enl.getOutil().getDiametre(), enl.getId(), enl.isValid(), null, 2.0);
            drawPoint(g, enl.getCoinOppose(), enl.getOutil().getDiametre(), enl.getId(), enl.isValid(), null, 2.0);
        }
    }

    private void drawZoneInterdite(Graphics2D g) {
        for (ZoneInterditeDTO zone : currentCNCDTO.getPanneau().getZoneInterditeList()) {
            if (zone.getId().equals(selectedElement) || true) {
                Point2D.Double bottomLeftCorner = NormalCoordToPanelCoord(zone.getPoint().getX(), zone.getPoint().getY() );
                double x_coord = DimensionToPanelCoord(zone.getDimension().getLargeur());
                double y_coord = DimensionToPanelCoord(zone.getDimension().getHauteur());
                Color fillColor = new Color(0, 100, 255, 128);
                g.setColor(fillColor);
                g.fill(new Rectangle2D.Double(bottomLeftCorner.getX(), bottomLeftCorner.getY(), x_coord, y_coord));
            }
        }
    }

    private void drawGrillefonct(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Save the original transform and stroke
        Stroke originalStroke = g.getStroke();
        AffineTransform originalTransform = g.getTransform();

        // Use a fixed-width stroke for the grid lines
        g.setStroke(new BasicStroke(1)); // Line thickness remains constant

        // Get panel dimensions
        double panelWidth = getWidth();
        double panelHeight = getHeight();

        // Parse grid size from the sizegrille field
        double baseCellSize;
        try {
            baseCellSize = Double.parseDouble(sizegrille);
        } catch (NumberFormatException e) {
            baseCellSize = 20; // Default to 20 if parsing fails
        }

        // Convert grid size to mm if measurement is in "pouce"
        if ("pouce".equalsIgnoreCase(measurement)) {
            baseCellSize *= 25.4; // Convert inches to mm (1 inch = 25.4 mm)
        }

        // Scale the cell size based on zoom factor
        double scaledCellSize = baseCellSize * zoomFactor;

        // Ensure minimum and maximum grid cell sizes
        scaledCellSize = Math.max(scaledCellSize, 20); // Prevent cells from being too small
        scaledCellSize = Math.min(scaledCellSize, 200); // Prevent cells from being too large

        // Set the grid color
        g.setColor(Color.GRAY);

        // Draw vertical grid lines and write coordinates
        for (double x = 0; x <= panelWidth; x += scaledCellSize) {
            g.drawLine((int) x, 0, (int) x, (int) panelHeight);

            // Write X-coordinate at the top of the grid
            String coordinate = String.format("%.1f", x / scaledCellSize); // Convert to grid units

            // Reset transform to ensure text is not inverted
            g.setTransform(originalTransform);
            g.drawString(coordinate, (int) x + 2, 12); // Adjust position slightly for clarity
            g.setTransform(originalTransform); // Restore transform for grid drawing
        }

        // Draw horizontal grid lines and write coordinates
        for (double y = 0; y <= panelHeight; y += scaledCellSize) {
            g.drawLine(0, (int) y, (int) panelWidth, (int) y);

            // Write Y-coordinate at the left of the grid
            String coordinate = String.format("%.1f", y / scaledCellSize); // Convert to grid units

            // Reset transform to ensure text is not inverted
            g.setTransform(originalTransform);
            g.drawString(coordinate, 2, (int) y - 2); // Adjust position slightly for clarity
            g.setTransform(originalTransform); // Restore transform for grid drawing
        }

        // Restore the original stroke
        g.setStroke(originalStroke);
    }


    public void handleGridSnapping(int mouseX, int mouseY) {
        if (MagnetiserlaGrille1) {
            // Log raw mouse click coordinates
            System.out.println("Mouse Click at: (" + mouseX + ", " + mouseY + ")");


            // Define the grid size (in panel coordinates)
            int cellSize = 20;

            // Calculate the grid cell origin (bottom-left corner of the cell in panel coordinates)
            double gridX = Math.floor(mouseX / (double) cellSize) * cellSize;
            double gridY = Math.floor(mouseY / (double) cellSize) * cellSize;

            // Identify all four corners of the grid cell in panel coordinates
            Point2D.Double bottomLeft = new Point2D.Double(gridX, gridY);
            Point2D.Double bottomRight = new Point2D.Double(gridX + cellSize, gridY);
            Point2D.Double topLeft = new Point2D.Double(gridX, gridY + cellSize);
            Point2D.Double topRight = new Point2D.Double(gridX + cellSize, gridY + cellSize);

            System.out.println("Corners (Panel Coordinates):");
            System.out.println("bottomLeft: (" + bottomLeft.getX() + ", " + bottomLeft.getY() + ")");
            System.out.println("bottomRight: (" + bottomRight.getX() + ", " + bottomRight.getY() + ")");
            System.out.println("topLeft: (" + topLeft.getX() + ", " + topLeft.getY() + ")");
            System.out.println("topRight: (" + topRight.getX() + ", " + topRight.getY() + ")");

            // Calculate distances to each corner from the mouse click position
            double distanceToBottomLeft = bottomLeft.distance(mouseX, mouseY);
            double distanceToBottomRight = bottomRight.distance(mouseX, mouseY);
            double distanceToTopLeft = topLeft.distance(mouseX, mouseY);
            double distanceToTopRight = topRight.distance(mouseX, mouseY);

            // Find the nearest corner in panel coordinates
            Point2D.Double nearestCorner = bottomLeft;
            double minDistance = distanceToBottomLeft;

            if (distanceToBottomRight < minDistance) {
                nearestCorner = bottomRight;
                minDistance = distanceToBottomRight;
            }
            if (distanceToTopLeft < minDistance) {
                nearestCorner = topLeft;
                minDistance = distanceToTopLeft;
            }
            if (distanceToTopRight < minDistance) {
                nearestCorner = topRight;
            }

            repaint();
        }
    }

    public void setshowgrille(){
        drawGrid=true;
    }

    public void getsize(String size){
         sizegrille = size;
    }

    public void getmeasurement(String measurement1){
        measurement = measurement1;
    }

    public void hideGrid() {
        drawGrid = false; // Set the grid visibility to false
        repaint(); // Trigger a repaint to remove the grid from the panel
    }

    public void setMagnetiserlaGrille1() {
        MagnetiserlaGrille1 = !MagnetiserlaGrille1;
    }
}