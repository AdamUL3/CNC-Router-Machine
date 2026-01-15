package Domain;

import Domain.Utils.Coordinates;
import Domain.Utils.Dimension;
import Domain.Utils.Ligne;

import java.io.Serializable;
import java.util.Locale;


public class EnBordure extends Coupe implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dimension nouvelleDimension;// Dimensions souhaitées après la coupe

    public EnBordure(Dimension nvdimension,
                     double profondeur, Outil outil, Panneau panneau) {
        super(profondeur, outil, panneau);
        this.nouvelleDimension = nvdimension;
        double diffY= (getPanneau().getPanneauHauteur() - nouvelleDimension.getHauteur()) / 2;
        double diffX= (getPanneau().getPanneauLargeur() - nouvelleDimension.getLargeur()) / 2;

        Ligne ligne1 = new Ligne(new Coordinates(diffX, diffY - outil.getDiametre()),
                new Coordinates(panneau.getPanneauLargeur()-diffX, diffY - outil.getDiametre()), outil.getDiametre());
        Ligne ligne2 = new Ligne(new Coordinates(panneau.getPanneauLargeur()-diffX + outil.getDiametre(), diffY ),
                new Coordinates(panneau.getPanneauLargeur()-diffX +outil.getDiametre(), panneau.getPanneauHauteur()-diffY), outil.getDiametre());
        Ligne ligne3 = new Ligne(new Coordinates(panneau.getPanneauLargeur()-diffX, panneau.getPanneauHauteur()-diffY + outil.getDiametre()),
                new Coordinates(diffX, panneau.getPanneauHauteur()-diffY + outil.getDiametre()), outil.getDiametre());
        Ligne ligne4 = new Ligne(new Coordinates(diffX - outil.getDiametre(), panneau.getPanneauHauteur()-diffY),
                new Coordinates(diffX -outil.getDiametre(), diffY), outil.getDiametre());

        ligneCoupe.add(ligne1);
        ligneCoupe.add(ligne2);
        ligneCoupe.add(ligne3);
        ligneCoupe.add(ligne4);
        isValid();
    }

    public EnBordure(EnBordure enbordure, CNC cnc) {
        super(enbordure, cnc);
        this.nouvelleDimension = enbordure.nouvelleDimension;
    }

    @Override
    public void updateCoupe(){
        double diffY= (getPanneau().getPanneauHauteur() - nouvelleDimension.getHauteur()) / 2;
        double diffX= (getPanneau().getPanneauLargeur() - nouvelleDimension.getLargeur()) / 2;

        Coordinates basgauche = new Coordinates(diffX - outil.getDiametre(), diffY - outil.getDiametre());
        Coordinates basdroite = new Coordinates(panneau.getPanneauLargeur()-diffX+ outil.getDiametre(), diffY - outil.getDiametre());
        Coordinates hautdroite = new Coordinates(panneau.getPanneauLargeur()-diffX + outil.getDiametre(), panneau.getPanneauHauteur()-diffY+outil.getDiametre());
        Coordinates hautgauche = new Coordinates(diffX - outil.getDiametre(), panneau.getPanneauHauteur()-diffY + outil.getDiametre());
        ligneCoupe.get(0).updateLigne(basgauche, basdroite, outil.getDiametre());
        ligneCoupe.get(1).updateLigne(basdroite, hautdroite, outil.getDiametre());
        ligneCoupe.get(2).updateLigne(hautdroite, hautgauche, outil.getDiametre());
        ligneCoupe.get(3).updateLigne(hautgauche, basgauche, outil.getDiametre());
    }

    public Dimension getDimensionsFinales() {
        return nouvelleDimension;
    }

    public void setDimensionsFinales(Dimension dimensionsFinales) {
        this.nouvelleDimension = dimensionsFinales;
        updateCoupe();
    }

    @Override
    public boolean isPointInOutline(Coordinates p) {
        for (Ligne ligne : ligneCoupe) {
            if (ligne.isPointInLigne(p)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isValid() {
        if (!isCoupeInPanneau()){
            throw new IllegalArgumentException("Les nouvelles dimensions doivent être inferieures à celles du panneau actuel.");
        }
        if (isCoupeInZoneInterdite()) {
            throw new IllegalArgumentException("La coupe se trouve dans une zone interdite.");
        }
        return true;
    }

    @Override
    public boolean isCoupeInPanneau() {
        return !(getPanneau().getPanneauHauteur() < nouvelleDimension.getHauteur()) && !(getPanneau().getPanneauLargeur() < nouvelleDimension.getLargeur());
    }

    @Override
    public String getInformations() {
        return "Coupe en bordure\nHauteur : "+ String.format(Locale.US, "%.3f", nouvelleDimension.getHauteur()) + "\nLargeur  : "+ String.format(Locale.US, "%.3f", nouvelleDimension.getLargeur());
    };
}
