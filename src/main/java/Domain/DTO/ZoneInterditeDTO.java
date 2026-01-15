package Domain.DTO;

import Domain.ZoneInterdite;

import java.util.UUID;

public class ZoneInterditeDTO {
    private final UUID id;
    private final DimensionDTO m_dimension;
    private final CoordinatesDTO point;

    public ZoneInterditeDTO(ZoneInterdite zoneinterdite) {
        this.id = zoneinterdite.getId();
        this.m_dimension = new DimensionDTO(zoneinterdite.getDimension());
        this.point = new CoordinatesDTO(zoneinterdite.getPoint());
    }

    public UUID getId(){
        return id;
    }

    public CoordinatesDTO getPoint() {
        return point;
    }

    public DimensionDTO getDimension() {
        return m_dimension;
    }
}
