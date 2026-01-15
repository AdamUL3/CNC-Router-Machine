package Domain.DTO;

import Domain.EnL;
import Domain.Utils.Coordinates;
import Domain.Utils.Ligne;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnLDTO {
    private final UUID id;
    private final double profondeur;
    private final OutilDTO outil;
    private final CoordinatesDTO coin;
    private final CoordinatesDTO coinOppose;
    private final List<LigneDTO> coupeLignes = new ArrayList<>();
    private boolean valid;

    public EnLDTO(EnL enL) {
        this.id = enL.getId();
        this.profondeur = enL.getProfondeur();
        this.outil = new OutilDTO(enL.getOutil());
        this.coin = new CoordinatesDTO(enL.getCoin());
        this.coinOppose = new CoordinatesDTO(enL.getCoinOppose());
        for (Ligne ligne : enL.getLignes()) {
            coupeLignes.add(new LigneDTO(ligne));
        }
        try {
            this.valid = enL.isValid();
        } catch (Exception e) {
            this.valid = false;
        }
    }

    public UUID getId(){ return id; }

    public double getProfondeur() { return profondeur;}

    public OutilDTO getOutil() { return outil;}

    public CoordinatesDTO getCoin() { return coin; }

    public CoordinatesDTO getCoinOppose() {return coinOppose; }

    public List<LigneDTO> getCoupeLignes() { return coupeLignes; }

    public boolean isValid() { return valid; }
}