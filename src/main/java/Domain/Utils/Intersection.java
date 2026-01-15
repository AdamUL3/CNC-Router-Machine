package Domain.Utils;

import Domain.CNC;

import java.io.Serializable;
import java.util.UUID;

public class Intersection implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Ligne ligne1;
    private Ligne ligne2;
    private Coordinates ptsIntersection;
    private double width;

    public Intersection(Ligne ligne1, Ligne ligne2) {
        this.id = UUID.randomUUID();
        this.ligne1 = ligne1;
        this.ligne2 = ligne2;
        updateIntersectionPts();
    }

    public Intersection(Intersection intersection, CNC cnc) {
        this.id = intersection.id;

        this.ligne1 = cnc.getLigneByUuid(intersection.ligne1.getId());
        this.ligne2 = cnc.getLigneByUuid(intersection.ligne2.getId());
        this.ptsIntersection = new Coordinates(intersection.ptsIntersection);
        this.width = intersection.width;
    }



    public UUID getId() { return id;}

    public void updateIntersectionPts() {
        if (ligne1.getId() == null || ligne2.getId() == null) {
            if (ptsIntersection != null) {
                ptsIntersection.selfDelete();
                ptsIntersection = null;
            }
            return;
        }

        Coordinates temppts = ligne1.getIntersectionPoint(ligne2);

        if (temppts == null) {
            if (ptsIntersection != null) {
                ptsIntersection.selfDelete();
                ptsIntersection = null;
            }
            return;
        }

        if (ptsIntersection == null) {
            ptsIntersection = new Coordinates(temppts.getX(), temppts.getY());
        } else {
            ptsIntersection.setX(temppts.getX());
            ptsIntersection.setY(temppts.getY());
        }
        this.width = Math.max(ligne1.getWidth(), ligne2.getWidth());
    }


    public boolean isIntersection() {
        updateIntersectionPts();
        if (ptsIntersection == null) { return false; }
        return ptsIntersection.getId() != null;
    }

    public Coordinates getIntersectionPts() {
        updateIntersectionPts();
        return ptsIntersection;
    }

    public Ligne getLigne1() {
        return ligne1;
    }

    public Ligne getLigne2() {
        return ligne2;
    }

    public void selfDelete() { this.id = null; }


    public boolean isPtsInIntersection(double x, double y) {
        if (!isIntersection()) {
            return false;
        }

        double tolerance = width / 2;

        return Math.abs(ptsIntersection.getX() - x) <= tolerance &&
                Math.abs(ptsIntersection.getY() - y) <= tolerance;
    }

    public double getWidth() { return width; }
}
