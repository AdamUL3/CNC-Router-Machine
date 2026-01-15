package Domain;

import Domain.Utils.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;



public class Panneau implements Serializable {
    private UUID id;
    private Dimension panneauDimension;
    private double epaisseur;
    private final double largeurligne = 3;
    private final List<Ligne> bordureList = new ArrayList<>();
    private final List<ZoneInterdite> zoneListe = new ArrayList<>();
    private static final long serialVersionUID = 1L;


    public Panneau(Dimension panneauDimension, double p_epaisseur) {
        this.id = UUID.randomUUID();
        this.panneauDimension = panneauDimension;
        this.epaisseur = p_epaisseur;
        updatePanneau();
    }

    public Panneau(Panneau panneau) {
        this.id = panneau.id;
        this.panneauDimension = panneau.panneauDimension;
        this.epaisseur = panneau.epaisseur;
        for (Ligne ligne : panneau.bordureList) {
            this.bordureList.add(new Ligne(ligne));
        }
        for (ZoneInterdite zoneInterdite : panneau.zoneListe) {
            this.zoneListe.add(new ZoneInterdite(zoneInterdite));
        }
    }


    public void updatePanneau() {
        bordureList.clear();
        bordureList.add(new Ligne(new Coordinates(0,0), new Coordinates(panneauDimension.getLargeur(), 0), largeurligne));
        bordureList.add(new Ligne(new Coordinates(0,0), new Coordinates(0, panneauDimension.getHauteur()), largeurligne));
        bordureList.add(new Ligne(new Coordinates(panneauDimension.getLargeur(), 0), new Coordinates(panneauDimension.getLargeur(), panneauDimension.getHauteur()), largeurligne));
        bordureList.add(new Ligne(new Coordinates(0, panneauDimension.getHauteur()), new Coordinates(panneauDimension.getLargeur(), panneauDimension.getHauteur()), largeurligne));
    }

    public List<Ligne> getBordureList() { return bordureList; }

    public UUID getId() { return id; }

    public Dimension getPanneauDimension() {
        return panneauDimension;
    }

    public boolean isPointInPanneau(Coordinates coordinates) {

        for (Ligne ligne : bordureList) {
            if (ligne.isPointInLigne(coordinates)) { return true;}
        }
        return coordinates.getX() >= 0 && coordinates.getX() <= panneauDimension.getLargeur() &&
                coordinates.getY() >= 0 && coordinates.getY() <= panneauDimension.getHauteur();
    }


    public double getPanneauLargeur() { return panneauDimension.getLargeur(); }

    public double getPanneauHauteur() { return panneauDimension.getHauteur(); }


    public void setPanneauDimension(Dimension panneauDimension) {
        this.panneauDimension = panneauDimension;
        updatePanneau();
    }

    public void setEpaisseur(double epaisseur) { this.epaisseur = epaisseur; }

    public double getEpaisseur() { return epaisseur; }


    public void addZone(ZoneInterdite zone) { zoneListe.add(zone); }

    public List<ZoneInterdite> getZoneList() { return zoneListe; }

    public void removeZone(ZoneInterdite zone) { zoneListe.remove(zone); }

}
