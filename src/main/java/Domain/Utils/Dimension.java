package Domain.Utils;

import java.io.Serializable;

public class Dimension implements Serializable {

    private static final long serialVersionUID = 1L;
    private double largeur;
    private double hauteur;

    public Dimension(double largeur, double hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public Dimension(Dimension dimension) {
        this.largeur = dimension.largeur;
        this.hauteur = dimension.hauteur;
    }

    public double getLargeur() {
        return largeur;
    }
    public double getHauteur() {
        return hauteur;
    }
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }
    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }
}
