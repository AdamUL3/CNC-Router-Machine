package Domain.DTO;

import Domain.Parallele;
import Domain.Utils.Ligne;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParalleleDTO {
    private final UUID id;
    private final double profondeur;
    private final OutilDTO outil;
    private final LigneDTO coupeRef;
    private final double taille;
    private final List<LigneDTO> coupeLignes = new ArrayList<>();
    private boolean valid;

    public ParalleleDTO(Parallele parallele) {
        this.id = parallele.getId();
        this.profondeur = parallele.getProfondeur();
        this.outil = new OutilDTO(parallele.getOutil());
        this.coupeRef = new LigneDTO(parallele.getCoupeRef());
        for (Ligne ligne : parallele.getLignes()) {
            this.coupeLignes.add(new LigneDTO(ligne));
        }
        this.taille = parallele.getTaille();
        try {
            this.valid = parallele.isValid();
        } catch (Exception e) {
            this.valid = false;

        }
    }
    public UUID getId() {
        return id;
    }

    public OutilDTO getOutil() {
        return outil;
    }

    public LigneDTO getCoupeRef() { return coupeRef; }

    public double getProfondeur() { return profondeur; }

    public double getTaille() { return taille; }

    public List<LigneDTO> getCoupeLignes() { return coupeLignes; }

    public boolean isValid() { return valid; }
}