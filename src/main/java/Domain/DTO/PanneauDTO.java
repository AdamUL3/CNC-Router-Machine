package Domain.DTO;

import Domain.Panneau;
import Domain.Utils.Ligne;
import Domain.ZoneInterdite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PanneauDTO {
    private final UUID id;
    private final DimensionDTO panneauDimension;
    private final double epaisseur;
    private final List<LigneDTO> bordureList = new ArrayList<>();
    private final List<ZoneInterditeDTO> zoneInterditeList = new ArrayList<>();

    public PanneauDTO(Panneau panneau) {
        this.id = panneau.getId();
        this.panneauDimension = new DimensionDTO(panneau.getPanneauDimension());
        this.epaisseur = panneau.getEpaisseur();
        for (Ligne ligne : panneau.getBordureList()) {
            this.bordureList.add(new LigneDTO(ligne));
        }
        for (ZoneInterdite zone : panneau.getZoneList()) {
            this.zoneInterditeList.add(new ZoneInterditeDTO(zone));
        }

    }

    public UUID getId() { return id; }

    public DimensionDTO getPanneauDimension() {
        return panneauDimension;
    }

    public double getPanneauLargeur() { return panneauDimension.getLargeur(); }

    public double getPanneauHauteur() { return panneauDimension.getHauteur(); }


    public List<LigneDTO> getBordureList() { return bordureList; }

    public List<ZoneInterditeDTO> getZoneInterditeList() { return zoneInterditeList; }

    public double getEpaisseur() { return epaisseur; }
}