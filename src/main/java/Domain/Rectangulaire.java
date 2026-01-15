package Domain;

import Domain.Utils.Coordinates;
import Domain.Utils.Ligne;

import java.io.Serializable;
import java.util.Locale;

public class Rectangulaire extends Coupe implements Serializable {

    private static final long serialVersionUID = 1L;
    private Coordinates refPts;
    private Coordinates coin1;
    private Coordinates coin2;

    public Rectangulaire(Coordinates p_refPts, Coordinates p_coin1, Coordinates p_coin2, double profondeur, Outil p_outil, Panneau panneau) {
        super(profondeur, p_outil, panneau);

        this.refPts = p_refPts;
        this.coin1 = p_coin1;
        this.coin2 = p_coin2;
        this.coin1.setRefPts(refPts);
        this.coin2.setRefPts(refPts);
        ligneCoupe.add(new Ligne(coin1, new Coordinates(coin1.getX(), coin2.getY()), outil.getDiametre()));
        ligneCoupe.add(new Ligne(coin1, new Coordinates(coin2.getX(), coin1.getY()), outil.getDiametre()));
        ligneCoupe.add(new Ligne(coin2, new Coordinates(coin2.getX(), coin1.getY()), outil.getDiametre()));
        ligneCoupe.add(new Ligne(coin2, new Coordinates(coin1.getX(), coin2.getY()), outil.getDiametre()));
        isValid();
    }

    public Rectangulaire(Rectangulaire rectangulaire, CNC cnc) {
        super(rectangulaire, cnc);
        this.refPts = new Coordinates(rectangulaire.refPts);
        this.coin1 = new Coordinates(rectangulaire.coin1);
        this.coin2 = new Coordinates(rectangulaire.coin2);
    }

    @Override
    public void updateCoupe() {
        this.coin1 = new Coordinates(coin1.getX(), coin1.getY());
        this.coin1.setRefPts(this.refPts);
        this.coin2 = new Coordinates(coin2.getX(), coin2.getY());
        this.coin2.setRefPts(this.refPts);

        ligneCoupe.get(0).updateLigne(coin1, new Coordinates(coin1.getX(), coin2.getY()), outil.getDiametre()); // Vertical line at coin1's X
        ligneCoupe.get(1).updateLigne(coin1, new Coordinates(coin2.getX(), coin1.getY()), outil.getDiametre()); // Horizontal line at coin1's Y
        ligneCoupe.get(2).updateLigne(coin2, new Coordinates(coin2.getX(), coin1.getY()), outil.getDiametre()); // Vertical line at coin2's X
        ligneCoupe.get(3).updateLigne(coin2, new Coordinates(coin1.getX(), coin2.getY()), outil.getDiametre()); // Horizontal line at coin2's Y
    }

    @Override
    public boolean isValid() {
        if (refPts.getId() == null) { throw new IllegalArgumentException("Le point de référence est introuvable."); }
        if (!isCoupeInPanneau()) { throw new IllegalArgumentException("La coupe n'est pas dans le panneau."); }
        if (isCoupeInZoneInterdite()) { throw new IllegalArgumentException("La coupe se trouve dans une zone interdite."); }
        return true;
    }


    @Override
    public boolean isPointInOutline(Coordinates p) {
        for (Ligne ligne : ligneCoupe) {
            if (ligne.isPointInLigne(p)) {
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean isCoupeInPanneau() {
        return panneau.isPointInPanneau(refPts) && panneau.isPointInPanneau(coin1) && panneau.isPointInPanneau(coin2);
    }

    public void setPoint(int num, Coordinates coordinates) {
        switch(num) {
            case 1:
                this.refPts = coordinates;
                break;
            case 2:
                this.coin1 = coordinates;
                break;
            case 3:
                this.coin2 = coordinates;
                break;
            default:
                throw new IllegalArgumentException("Le point n'existe pas pour la coupe rectangulaire.");
        }
        updateCoupe();
    }


    public Coordinates getPoint(int num) {
        return switch (num) {
            case 1 -> refPts;
            case 2 -> coin1;
            case 3 -> coin2;
            default -> throw new IllegalArgumentException("Le point n'existe pas pour la coupe rectangulaire.");
        };
    }

    @Override
    public String getInformations() {
        return "Coupe rectangulaire\nHauteur: "+String.format(Locale.US, "%.3f", Math.abs(coin1.getY()-coin2.getY())) + "\nLargeur  : "+ String.format(Locale.US, "%.3f", Math.abs(coin1.getX()-coin2.getX()));
    };

}
