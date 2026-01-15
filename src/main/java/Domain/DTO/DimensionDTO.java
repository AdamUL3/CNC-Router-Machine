package Domain.DTO;

import Domain.Utils.Dimension;

public class DimensionDTO {

    private double largeur;
    private double hauteur;

    public DimensionDTO(Dimension dimension) {
        this.largeur = dimension.getLargeur();
        this.hauteur = dimension.getHauteur();
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteur() {
        return hauteur;
    }
}
