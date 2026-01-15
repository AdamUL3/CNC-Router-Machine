package Domain.DTO;

import Domain.Utils.Coordinates;
import Domain.Utils.Intersection;
import Domain.Utils.Ligne;

import java.util.UUID;

public class IntersectionDTO {
    private UUID id;
    private LigneDTO ligne1;
    private LigneDTO ligne2;
    private CoordinatesDTO ptsIntersection;
    private double width;

    public IntersectionDTO(Intersection intersection) {
        this.ligne1 = new LigneDTO(intersection.getLigne1());
        this.ligne2 = new LigneDTO(intersection.getLigne2());
        this.id = intersection.getId();
        this.ptsIntersection = new CoordinatesDTO(intersection.getIntersectionPts());
        this.width = intersection.getWidth();
    }

    public UUID getId() { return id;}

    public CoordinatesDTO getIntersectionPts() {
        return ptsIntersection;
    }

    public LigneDTO getLigne1() {
        return ligne1;
    }

    public LigneDTO getLigne2() {
        return ligne2;
    }

    public double getWidth() { return width; }
}
