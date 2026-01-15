package Domain;

import Domain.Utils.Coordinates;
import Domain.Utils.Ligne;

import java.io.Serializable;
import java.util.Locale;

public class Parallele extends Coupe implements Serializable {
    private static final long serialVersionUID = 1L;
    private Ligne coupeRef;
    private double taille;

    public Parallele(Ligne couperef, double taille, Panneau panneau, double profondeur, Outil outil ) {
        super(profondeur, outil, panneau);
        this.coupeRef = couperef;
        this.taille = taille;
        Ligne unligne;
        if (coupeRef.isVertical()) {
            if(taille >0){
                unligne = new Ligne(new Coordinates(coupeRef.getPoint1().getX() + taille + outil.getDiametre(), 0), new Coordinates(coupeRef.getPoint1().getX() + taille +outil.getDiametre(), panneau.getPanneauHauteur()), outil.getDiametre());
            }
            else{
                unligne = new Ligne(new Coordinates(coupeRef.getPoint1().getX() + taille - outil.getDiametre(), 0), new Coordinates(coupeRef.getPoint1().getX() + taille -outil.getDiametre(), panneau.getPanneauHauteur()), outil.getDiametre());
            }
        }
        else {
            if(taille >0){
                unligne = new Ligne(new Coordinates(0, coupeRef.getPoint1().getY() + taille + outil.getDiametre()), new Coordinates(panneau.getPanneauLargeur(), coupeRef.getPoint1().getY() + taille + outil.getDiametre()), outil.getDiametre());
            }
            else{
                unligne = new Ligne(new Coordinates(0, coupeRef.getPoint1().getY() + taille - outil.getDiametre()), new Coordinates(panneau.getPanneauLargeur(), coupeRef.getPoint1().getY() + taille - outil.getDiametre()), outil.getDiametre());
            }
        }
        ligneCoupe.add(unligne);
        isValid();
    }

    public Parallele(Parallele parallele, CNC cnc) {
        super(parallele, cnc);
        this.coupeRef = new Ligne(parallele.coupeRef);
        this.taille = parallele.taille;
    }

    @Override
    public void updateCoupe() {
        if (coupeRef.isVertical()) {
            if(taille >0) {
                ligneCoupe.get(0).updateLigne(new Coordinates(coupeRef.getPoint1().getX() + taille + outil.getDiametre(), 0), new Coordinates(coupeRef.getPoint1().getX() + taille + outil.getDiametre(), panneau.getPanneauHauteur()), outil.getDiametre());
            }
            else{
                ligneCoupe.get(0).updateLigne(new Coordinates(coupeRef.getPoint1().getX() + taille - outil.getDiametre(), 0), new Coordinates(coupeRef.getPoint1().getX() + taille - outil.getDiametre(), panneau.getPanneauHauteur()), outil.getDiametre());
            }
        }
        else {
            if(taille >0){
                ligneCoupe.get(0).updateLigne(new Coordinates(0, coupeRef.getPoint1().getY() + taille  + outil.getDiametre()), new Coordinates(panneau.getPanneauLargeur(), coupeRef.getPoint1().getY() + taille  + outil.getDiametre()), outil.getDiametre());
            }
            else{
                ligneCoupe.get(0).updateLigne(new Coordinates(0, coupeRef.getPoint1().getY() + taille  - outil.getDiametre()), new Coordinates(panneau.getPanneauLargeur(), coupeRef.getPoint1().getY() + taille  - outil.getDiametre()), outil.getDiametre());
            }
            }
    }

    public void setTaille(double taille) {
        this.taille = taille;
        updateCoupe();
    }

    public double getTaille() { return taille; }

    public void setCoupeRef(Ligne ref) {
        this.coupeRef = ref;
        updateCoupe();
    }

    public Ligne getCoupeRef() { return coupeRef; }

    @Override
    public boolean isPointInOutline(Coordinates p) {
        return ligneCoupe.get(0).isPointInLigne(p);
    }

    @Override
    public boolean isValid()  {
        if (coupeRef.getId() == null) { throw new IllegalArgumentException("La coupe de référence est introuvable."); }
        if (!isCoupeInPanneau()) { throw new IllegalArgumentException("La coupe n'est pas dans le panneau."); }
        if (isCoupeInZoneInterdite()) { throw new IllegalArgumentException("La coupe se trouve dans une zone interdite."); }
        return true;
    }

    @Override
    public boolean isCoupeInPanneau() {
        return (panneau.isPointInPanneau(ligneCoupe.get(0).getPoint1()) && panneau.isPointInPanneau(ligneCoupe.get(0).getPoint2()));
    }

    @Override
    public String getInformations() {
        String type = "horizontale";
        if (coupeRef.isVertical()) {
            type = "verticale";
        }
        return "Coupe " + type + "\nDistance de la référence : " + String.format(Locale.US, "%.3f", Math.abs(taille));
    };
}

