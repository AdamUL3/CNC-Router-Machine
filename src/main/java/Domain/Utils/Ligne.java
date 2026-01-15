package Domain.Utils;

import java.util.UUID;
import java.io.Serializable;


public class Ligne implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Coordinates point1;
    private Coordinates point2;
    private boolean vertical;
    private double width;

    public Ligne(Coordinates point1, Coordinates point2, double p_width) {
        this.id = UUID.randomUUID();
        this.point1 = point1;
        this.point2 = point2;
        this.width = p_width*2;
        checkVertical();

    }

    public Ligne(Ligne ligne) {
        if (ligne == null) { return; }
        this.id = ligne.id;
        this.point1 = new Coordinates(ligne.point1);
        this.point2 = new Coordinates(ligne.point2);
        this.vertical = ligne.vertical;
        this.width = ligne.width;
    }


    public UUID getId() { return id; }

    public boolean isPointInLigne(Coordinates point){
        double p, p2, origine, destination, axecoord;

        if (this.vertical) {
            p = point.getX();
            p2 = point.getY();
            origine = point1.getY();
            destination = point2.getY();
            axecoord = point1.getX();
        }
        else {
            p = point.getY();
            p2 = point.getX();
            origine = point1.getX();
            destination = point2.getX();
            axecoord = point2.getY();
        }
        if (axecoord - width/2 < p && p < axecoord +  width/2) {
            double min = Math.min(origine, destination) - width/2;
            double max = Math.max(origine, destination) + width/2;
            return min < p2 && p2 < max;
        }
        return false;
    }

    public Coordinates getPoint1() { return point1; }

    public Coordinates getPoint2() { return point2; }

    public void updateLigne(Coordinates point1, Coordinates point2, double p_width) {
        this.point1 = point1;
        this.point2 = point2;
        this.width = p_width*2;
        checkVertical();

    }
    public void checkVertical() {
        if(point1.getX() == point2.getX()){
            if (point1.getY() > point2.getY()) {
                Coordinates temppts = point1;
                point1 = point2;
                point2 = temppts;
            }
            this.vertical = true;
        } else if (point1.getY() == point2.getY()) {
            if (point1.getX() > point2.getX()) {
                Coordinates temppts = point1;
                point1 = point2;
                point2 = temppts;
            }
            this.vertical = false;
        } else {  throw new IllegalArgumentException("Le point n'est ni vertical ni horizontal"); }
    }

    public double getWidth() { return width; }

    public boolean isVertical() { return vertical; }

    public Dimension getDimension() {
        return new Dimension(Math.abs(point1.getX() - point2.getX()), Math.abs(point1.getY() - point2.getY()));
    }

    public void deplacerLigneX(double x) {
        this.point1.setX(point1.getX()+x);
        this.point2.setX(point2.getX()+x);
    }

    public void deplacerLigneY(double y) {
        this.point1.setY(point1.getY()+y);
        this.point2.setY(point2.getY()+y);
    }


    public boolean checkIntersection(Ligne otherLine) {
        // Si l'une est verticale et l'autre est horizontale
        if (otherLine == null) {
            return false;
        }
        else if (this.vertical && !otherLine.vertical) {
            return this.point1.getX() >= Math.min(otherLine.point1.getX(), otherLine.point2.getX()) &&
                    this.point1.getX() <= Math.max(otherLine.point1.getX(), otherLine.point2.getX()) &&
                    otherLine.point1.getY() >= Math.min(this.point1.getY(), this.point2.getY()) &&
                    otherLine.point1.getY() <= Math.max(this.point1.getY(), this.point2.getY());
        }
        // Si l'une est horizontale et l'autre est verticale
        else if (!this.vertical && otherLine.vertical) {
            return otherLine.point1.getX() >= Math.min(this.point1.getX(), this.point2.getX()) &&
                    otherLine.point1.getX() <= Math.max(this.point1.getX(), this.point2.getX()) &&
                    this.point1.getY() >= Math.min(otherLine.point1.getY(), otherLine.point2.getY()) &&
                    this.point1.getY() <= Math.max(otherLine.point1.getY(), otherLine.point2.getY());
        }
        return false;
    }


    public Coordinates getIntersectionPoint(Ligne otherLine) {
        if (!checkIntersection(otherLine)) {
            return null;
        }

        if (this.vertical && !otherLine.vertical) {
            double x = this.point1.getX();
            double y = otherLine.point1.getY();
            return new Coordinates(x, y);
        } else if (!this.vertical && otherLine.vertical) {
            double x = otherLine.point1.getX();
            double y = this.point1.getY();
            return new Coordinates(x, y);
        }

        return null;
    }

    public void selfDelete() {
        this.id = null;
    }
}
