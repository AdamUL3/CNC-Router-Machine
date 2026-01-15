package Domain.DTO;

import Domain.Utils.Coordinates;
import java.util.UUID;

public class CoordinatesDTO {
    private final UUID id;
    private final double x;
    private final double y;

    public CoordinatesDTO(Coordinates coordinates) {
        this.id = coordinates.getId();
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public UUID getId() { return id; }
}
