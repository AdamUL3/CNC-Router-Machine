package Domain.Utils;

import java.io.Serializable;
import java.util.UUID;
import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private double x;
    private double y;
    private Coordinates refPts = null;

    public Coordinates(double x, double y) {
        this.id = UUID.randomUUID();
        this.x = x;
        this.y = y;
    }

    public Coordinates(Coordinates pts) {
        this.id = pts.id;
        this.x = pts.x;
        this.y = pts.y;
        if (pts.refPts != null) {
            this.refPts = new Coordinates(pts.refPts);
        }
    }

    public void setRefPts(Coordinates pts) {
        if (this.refPts != null) {
            this.x = getX();
            this.y = getY();
        }
        this.refPts = pts;
        if (pts != null) {
            this.x = x - pts.getX();
            this.y = y - pts.getY();
        }
    }

    public double getX() {
        if (refPts == null) {
            return x;
        }
        return refPts.getX() + x;
    }

    public double getY() {
        if (refPts == null) {
            return y;
        }
        return refPts.getY() + y;
    }

    public void setX(double x) {
        if (refPts == null) {
            this.x = x;
            return;
        }
        this.x = x - refPts.getX();
    }

    public void setY(double y) {
        if (refPts == null) {
            this.y = y;
            return;
        }
        this.y = y - refPts.getY();
    }

    public UUID getId() { return id; }

    public void selfDelete() { this.id = null; }






}
