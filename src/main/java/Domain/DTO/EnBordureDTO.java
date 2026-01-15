package Domain.DTO;
import Domain.EnBordure;
import Domain.Utils.Ligne;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EnBordureDTO {
    private final DimensionDTO dimensionsFinales;
    private final OutilDTO outil;
    private final UUID id;
    private final double profondeur;
    private final List<LigneDTO> coupeLignes = new ArrayList<>();
    private boolean valid;

    public EnBordureDTO(EnBordure enBordure) {
        this.dimensionsFinales = new DimensionDTO(enBordure.getDimensionsFinales());
        this.id = enBordure.getId();
        this.outil = new OutilDTO(enBordure.getOutil());
        this.profondeur = enBordure.getProfondeur();
        for (Ligne ligne : enBordure.getLignes()) {
            coupeLignes.add(new LigneDTO(ligne));
        }
        try {
            this.valid = enBordure.isValid();
        } catch (Exception e) {
            this.valid = false;
        }
    }

    public DimensionDTO getDimensionsFinales() {
        return dimensionsFinales;
    }

    public UUID getId() {
        return id;
    }

    public OutilDTO getOutil() {
        return outil;
    }

    public double getProfondeur() {
        return profondeur;
    }

    public List<LigneDTO> getCoupeLignes() { return coupeLignes; }

    public boolean isValid() { return valid; }
}



