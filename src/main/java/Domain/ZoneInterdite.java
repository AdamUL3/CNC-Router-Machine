package Domain;

import Domain.Utils.Coordinates;
import Domain.Utils.Dimension;
import Domain.Utils.Ligne;

import java.io.Serializable;
import java.util.UUID;



public class ZoneInterdite implements Serializable {
    private final UUID id;
    private Dimension m_dimension;
    private Coordinates point;
    private static final long serialVersionUID = 1L;



    public ZoneInterdite(Dimension dimension, Coordinates point) {
        this.id = UUID.randomUUID();
        this.m_dimension = dimension;
        this.point = point;
    }

    public ZoneInterdite(ZoneInterdite zoneInterdite) {
        this.id = zoneInterdite.id;
        this.m_dimension = new Dimension(zoneInterdite.m_dimension);
        this.point = new Coordinates(zoneInterdite.point);
    }

    public UUID getId() {
        return id;
    }

    public Coordinates getPoint() {
        return point;
    }

    public void setPoint(Coordinates point) {
        this.point = point;
    }

    public Dimension getDimension() {
        return m_dimension;
    }

    public void setDimension(Dimension dimension) {
        this.m_dimension = dimension;
    }


    public boolean isLigneInZone (Ligne ligne){
            double zoneGauche = point.getX();
            double zoneHaut = point.getY() + m_dimension.getHauteur();
            double zoneBas = point.getY();
            double zoneDroit = point.getX() + m_dimension.getLargeur();

            if (ligne.isVertical()) {
                if (zoneGauche <= ligne.getPoint1().getX() && ligne.getPoint1().getX() <= zoneDroit) {
                    if (zoneBas <= ligne.getPoint1().getY() && ligne.getPoint1().getY() <= zoneHaut) {
                        return true;
                    } else if (zoneBas <= ligne.getPoint2().getY() && ligne.getPoint2().getY() <= zoneHaut) {
                        return true;
                    } else if (ligne.getPoint1().getY() <= zoneBas && zoneHaut <= ligne.getPoint2().getY()) {
                        return true;
                    } else if (ligne.getPoint2().getY() <= zoneBas && zoneHaut <= ligne.getPoint1().getY()) {
                        return true;
                    }
                }
            }
            else { // Si la ligne n'est pas verticale (elle est horizontale)
                if (zoneBas <= ligne.getPoint1().getY() && ligne.getPoint1().getY() <= zoneHaut) {
                    if (zoneGauche <= ligne.getPoint1().getX() && ligne.getPoint1().getX() <= zoneDroit) {
                        return true;
                    } else if (zoneGauche <= ligne.getPoint2().getX() && ligne.getPoint2().getX() <= zoneDroit) {
                        return true;
                    } else if (ligne.getPoint1().getX() <= zoneGauche && zoneDroit <= ligne.getPoint2().getX()) {
                        return true;
                    } else if (ligne.getPoint2().getX() <= zoneGauche && zoneDroit <= ligne.getPoint1().getX()) {
                        return true;
                    }
                }
            }
        return false;
    }
}


