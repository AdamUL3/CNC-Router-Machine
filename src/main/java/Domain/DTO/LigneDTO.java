package Domain.DTO;

import Domain.Utils.Ligne;

import java.util.UUID;

public class LigneDTO {
    private final UUID id;
    private final CoordinatesDTO point1;
    private final CoordinatesDTO point2;
    private final boolean vertical;
    private final double width;

    public LigneDTO(Ligne ligne) {
        this.id = ligne.getId();
        this.point1 = new CoordinatesDTO(ligne.getPoint1());
        this.point2 = new CoordinatesDTO(ligne.getPoint2());
        this.vertical = ligne.isVertical();
        this.width = ligne.getWidth();
    }

    public UUID getId() { return id; }

    public CoordinatesDTO getPoint1() { return point1; }

    public CoordinatesDTO getPoint2() { return point2; }

    public double getWidth() { return width; }

    public boolean isVertical() { return vertical; }
}
