package Domain;

import Domain.Utils.Coordinates;
import Domain.Utils.Ligne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// CLASSE COUPE
public abstract class Coupe implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private double profondeur;
    protected Outil outil;
    protected Panneau panneau;
    protected List<Ligne> ligneCoupe = new ArrayList<>();

    // Constructeur
    public Coupe(double profondeur, Outil outil, Panneau panneau) {
        this.id  = UUID.randomUUID();
        this.profondeur = profondeur;
        this.outil = outil;
        this.panneau = panneau;
    }

    public Coupe(Coupe coupe, CNC cnc) {
        this.id = coupe.id;
        this.profondeur = coupe.profondeur;
        this.outil = cnc.getCurrOutil();
        this.panneau = cnc.getPanneau();
        boolean found;
        for (Ligne ligne : coupe.ligneCoupe) {
            found = false;
            for (Ligne lig : cnc.getLignes()) {
                if (lig.getId().equals(ligne.getId())) {
                    this.ligneCoupe.add(lig);
                    found = true;
                    break;
                }
            }
            if (!found) { this.ligneCoupe.add(new Ligne(ligne)); }
        }
    }

    public List<Ligne> getLignes() { return ligneCoupe; }

    public abstract boolean isPointInOutline(Coordinates coordinates);

    public UUID getId(){
        return id;
    }

    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public Outil getOutil() {
        return outil;
    }

    public void setOutil(Outil outil) {
        this.outil = outil;
        updateCoupe();
    }

    public abstract void updateCoupe();

    public abstract boolean isValid();

    public abstract boolean isCoupeInPanneau();

    public boolean isCoupeInZoneInterdite() {
        for (ZoneInterdite zone : panneau.getZoneList()) {
            for (Ligne ligne : ligneCoupe) {
                if (zone.isLigneInZone(ligne)) { return true;}
            }
        }
        return false;
    }

    public Panneau getPanneau() { return panneau; }

    public void setPanneau(Panneau panneau) { this.panneau = panneau; }

    public void selfDelete() {
        for (Ligne l : ligneCoupe) {
            l.selfDelete();
        }
        this.id = null;
    }

    public abstract String getInformations();

}


