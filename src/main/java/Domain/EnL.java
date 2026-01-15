package Domain;

import Domain.Utils.*;

import java.io.Serializable;
import java.util.Locale;

public class EnL extends Coupe implements Serializable {
    private static final long serialVersionUID = 1L;
    private Coordinates coin;
    private Coordinates coinOppose;
    private Coordinates extremiteX;
    private Coordinates extremiteY;
    private Ligne horizontalRef;
    private Ligne verticalRef;

    public EnL(Coordinates lecoin, Ligne horizontalref, Ligne verticalref, double profondeur, Outil outil, Panneau panneau) {
        super(profondeur, outil, panneau);
        this.horizontalRef = horizontalref;
        this.verticalRef = verticalref;
        this.coin = lecoin;
        // Coin opposé
        this.coinOppose = horizontalRef.getIntersectionPoint(verticalRef);
        this.coin.setRefPts(coinOppose);
        this.extremiteX = new Coordinates(verticalRef.getPoint1().getX(), coin.getY());
        this.ligneCoupe.add(new Ligne(coin, extremiteX, outil.getDiametre()));
        // La ligne verticale
        this.extremiteY = new Coordinates(coin.getX(), horizontalRef.getPoint1().getY());
        this.ligneCoupe.add(new Ligne(coin, extremiteY, outil.getDiametre()));
        isValid();
    }

    public EnL(EnL enl, CNC cnc) {
        super(enl, cnc);
        this.horizontalRef = new Ligne(enl.horizontalRef);
        this.verticalRef = new Ligne(enl.verticalRef);
        this.coin = new Coordinates(enl.coin);
        this.coinOppose = new Coordinates(enl.coinOppose);
        this.coin.setRefPts(coinOppose);
        this.extremiteX = new Coordinates(enl.extremiteX);
        this.extremiteY = new Coordinates(enl.extremiteY);
        updateCoupe();
    }


    @Override
    public void updateCoupe() {
        // Coin opposé
        Coordinates temppts = horizontalRef.getIntersectionPoint(verticalRef);
        if (temppts != null) {
            this.coinOppose.setX(temppts.getX());
            this.coinOppose.setY(temppts.getY());
        } else {this.coinOppose.selfDelete(); }
        this.coin.setRefPts(coinOppose);
        this.extremiteX = new Coordinates(verticalRef.getPoint1().getX(), coin.getY());
        this.extremiteY = new Coordinates(coin.getX(), horizontalRef.getPoint1().getY());
        // La ligne horizontale
        this.ligneCoupe.get(0).updateLigne(coin, extremiteX, outil.getDiametre());
        // La ligne verticale
        this.ligneCoupe.get(1).updateLigne(coin, extremiteY, outil.getDiametre());
    }

    public Coordinates getCoin() {
        return coin;
    }

    public void setCoin(Coordinates coin) {
        this.coin = coin;
        updateCoupe();
    }

    public Coordinates getCoinOppose() { return coinOppose; }

    public void setRefs(Ligne verticalRef, Ligne horizontalRef) {
        this.horizontalRef = horizontalRef;
        this.verticalRef = verticalRef;
        this.coin.setRefPts(null);
        updateCoupe();
    }

    public Ligne getHorizontalRef() {
        return horizontalRef;
    }

    public Ligne getVerticalRef() {
        return verticalRef;
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
        return panneau.isPointInPanneau(coin) && panneau.isPointInPanneau(extremiteX) && panneau.isPointInPanneau(extremiteY);
    }

    @Override
    public boolean isValid() {
        if (horizontalRef.getId() == null || verticalRef.getId() == null) { throw new IllegalArgumentException("Une ou plusieurs ligne(s) de référence sont introuvable."); }
        if (!horizontalRef.checkIntersection(verticalRef)) { throw new IllegalArgumentException("Les deux lignes de références ne se croisent pas."); }
        if (!verticalRef.isPointInLigne(extremiteX) || !horizontalRef.isPointInLigne(extremiteY)) { throw new IllegalArgumentException("Une ou plusieurs ligne(s) de référence n'est pas vis à vis le coin du L."); }
        if (!isCoupeInPanneau()) { throw new IllegalArgumentException("La coupe n'est pas dans le panneau."); }
        if (isCoupeInZoneInterdite()) { throw new IllegalArgumentException("La coupe se trouve dans une zone interdite."); }
        return true;
    }

    @Override
    public String getInformations() {
        return "Coupe en L\nHauteur : "+ String.format(Locale.US, "%.3f", Math.abs(coin.getY()-coinOppose.getY())) + "\nLargeur  eh: "+ String.format(Locale.US, "%.3f", Math.abs(coin.getX()-coinOppose.getX()));
    };

}