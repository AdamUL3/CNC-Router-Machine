package Domain;
import Domain.DTO.CNCDTO;
import Domain.Utils.Coordinates;
import Domain.Utils.Dimension;
import Domain.Utils.Intersection;
import Domain.Utils.Ligne;

import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Controleur {


    private CNC cnc;
    private final Historique historique;
    boolean isMoving = false;

    // CONSTRUCTEUR

    public Controleur() {
        this.cnc = new CNC();
        this.historique = new Historique(new CNC(cnc));
    }

    // PROFONDEUR

    public void setProfondeur(double profondeur) {
        cnc.setProfondeur(profondeur);
        saveState();
    }

    // OUTILS

    public UUID addOutil(String nom, double diametre) {
        Outil outil = new Outil(nom, diametre);
        cnc.addOutil(outil);
        cnc.setCurrOutil(outil);
        saveState();
        return outil.getId();
    }

    public void suppOutil(UUID id) {
        Outil outilasup = cnc.getOutilByUuid(id);
        if (outilasup == cnc.getCurrOutil()) {
            cnc.setCurrOutil(cnc.getOutilsList().get(0));
        }
        for (Coupe coupe : cnc.getCoupeListe()) {
            if (coupe.getOutil() == outilasup) {
                coupe.setOutil(cnc.getCurrOutil());
            }
        }
        cnc.suppOutil(outilasup);
        saveState();
    }

    public void setCurrOutil(UUID id) {
        cnc.setCurrOutil(cnc.getOutilByUuid(id));
        saveState();
    }

    public void modifOutil(UUID id, String nom, Double diametre) {
        Outil outil = cnc.getOutilByUuid(id);
        if (outil != null) {
            if (nom != null) {
                outil.setNom(nom);
            }
            if (diametre != null) {
                outil.setDiametre(diametre);
            }
            cnc.updateAllCoupes();
            saveState();
        }
    }

    public void modifCoupeOutil(UUID coupe_id, UUID outil_id) {
        cnc.getCoupeByUuid(coupe_id).setOutil(cnc.getOutilByUuid(outil_id));
        cnc.updateAllCoupes();
        saveState();
    }

    public void modifCoupeProfondeur(UUID coupe_id, Double profondeur) {
        Coupe lacoupe = cnc.getCoupeByUuid(coupe_id);
        if (lacoupe != null && profondeur != null) {
            lacoupe.setProfondeur(profondeur);
            //saveState();
            //System.out.println("savestate modifCoupeProfondeur");
        }
    }

    // ELEMENT DU PANNEAU

    public UUID getElemPanel(double pointX, double pointY) {
        Coordinates pts =  new Coordinates(pointX, pointY);
        for (Coupe coupe : cnc.getCoupeListe()) {
            if (coupe.isPointInOutline(pts)) {
                return coupe.getId();
            }
        }

        if (cnc.getPanneau() != null) {
            if ( cnc.getPanneau().isPointInPanneau(pts)) {
                return cnc.getPanneau().getId();
            }
        }
        return null;
    }

    public List<UUID> getLignePanel(double pointX, double pointY) {
        Coordinates pts =  new Coordinates(pointX, pointY);
        List<UUID> ligneliste = new ArrayList<>();
        for (Ligne ligne : cnc.getLignes()) {
            if (ligne.isPointInLigne(pts)) {
                ligneliste.add(ligne.getId());
            }
        }
        return ligneliste;
    }

    public UUID getIntersectionPanel(double pointx, double pointy) {
        for (Intersection it : cnc.getPtsIntersectionsListe()) {
            if (it.isPtsInIntersection(pointx, pointy)) {
                return it.getIntersectionPts().getId();
            }
        }
        return null;
    }


    // ZONES INTERDITES

    public  UUID addZone(double PointX1, double PointY1, double PointX2 ,double PointY2) {
        Dimension zonedimension = new Dimension(Math.abs(PointX2-PointX1), Math.abs(PointY2-PointY1));
        Coordinates zonepoint = new Coordinates(PointX1, PointY1);
        ZoneInterdite zone = new ZoneInterdite(zonedimension, zonepoint);
        cnc.addZone(zone);
        cnc.updateAllCoupes();
        saveState();
        return zone.getId();
    }

    public void suppZone(UUID id) {
        cnc.suppZone(cnc.getZoneByUuid(id));
        cnc.updateAllCoupes();
        saveState();
    }

    public void modifZone(UUID id, double PointX1, double PointY1, double PointX2 ,double PointY2) {
        ZoneInterdite zone = cnc.getZoneByUuid(id);
        if (zone != null) {
            Dimension zonedimension = new Dimension(Math.abs(PointX2-PointX1), Math.abs(PointY2-PointY1));
            Coordinates zonepoint = new Coordinates(PointX1, PointY1);
            zone.setPoint(zonepoint);
            zone.setDimension(zonedimension);
            cnc.updateAllCoupes();
            saveState();
        }
    }

    public void suppCoupe(UUID id) {
        cnc.suppCoupe(cnc.getCoupeByUuid(id));
        cnc.updateAllCoupes();
        System.out.println("supp coupe");
        saveState();

    }

    public UUID addEnBordure(double largeur, double hauteur) {
        EnBordure enBordure = new EnBordure(new Dimension(largeur, hauteur), cnc.getProfondeur(), cnc.getCurrOutil(), cnc.getPanneau());
        cnc.addCoupe(enBordure);
        saveState();
        return enBordure.getId();
    }

    public UUID addParallele(UUID id_ligne, double taille) {
        Parallele parallele = new Parallele(cnc.getLigneByUuid(id_ligne), taille, cnc.getPanneau(), cnc.getProfondeur(), cnc.getCurrOutil());
        cnc.addCoupe(parallele);
        saveState();
        return parallele.getId();
    }

    public UUID addRectangulaire(double refx, double refy, double pt1x, double pt1y, double pt2x, double pt2y) {
        UUID refptsid = getIntersectionPanel(refx, refy);
        if (refptsid == null) { throw new IllegalArgumentException("Le point de référence est invalide.");}
        Coordinates refpts= cnc.getIntersectionPtsByUUID(refptsid);
        Rectangulaire rectangulaire = new Rectangulaire(refpts, new Coordinates(pt1x, pt1y), new Coordinates(pt2x, pt2y), cnc.getProfondeur(), cnc.getCurrOutil(), cnc.getPanneau());
        cnc.addCoupe(rectangulaire);
        saveState();
        return rectangulaire.getId();
    }

    public UUID addEnL(List<UUID> lignesref, double coinx, double coiny) {
        Ligne vertical = null;
        Ligne horizontal = null;
        for (UUID id : lignesref) {
            Ligne ligne = cnc.getLigneByUuid(id);
            if (ligne.isVertical() && vertical == null) {
                vertical = ligne;
            } else if (!ligne.isVertical() && horizontal == null) {
                horizontal = ligne;
            }
        }
        if (vertical == null || horizontal == null) { throw new IllegalArgumentException("Le deuxième point est invalide."); }
        EnL enl = new EnL(new Coordinates(coinx, coiny), horizontal, vertical, cnc.getProfondeur(), cnc.getCurrOutil(), cnc.getPanneau());
        cnc.addCoupe(enl);
        System.out.println("add enl");
        saveState();
        return enl.getId();
    }

    public void modifEnL(UUID id, Double x1, Double y1, Double x2, Double y2) {
        EnL enl = (EnL) cnc.getCoupeByUuid(id);
        if (enl != null) {
            if ((x1 != null) && (y1 != null)) {
                enl.setCoin(new Coordinates(x1, y1));
            }
            if ((x2 != null) && (y2 != null)) {
                List<UUID> lignesref = getLignePanel(x2, y2);
                Ligne vertical = null;
                Ligne horizontal = null;
                for (UUID id1 : lignesref) {
                    Ligne ligne = cnc.getLigneByUuid(id1);
                    if (ligne.isVertical() && vertical == null) {
                        vertical = ligne;
                    } else if (!ligne.isVertical() && horizontal == null) {
                        horizontal = ligne;
                    }
                }
                if (horizontal == null || vertical == null) {
                    throw new IllegalArgumentException("Le deuxième point est invalide.");
                }
                enl.setRefs(vertical, horizontal);
            }
            cnc.updateAllCoupes();
            System.out.println("modif enl");
            if (!isMoving) { saveState(); }
        }
    }


    public void modifParallele(UUID id, UUID ref_ligne, Double taille) {
        Parallele parallele = (Parallele) cnc.getCoupeByUuid(id);
        if (parallele != null) {
            if (ref_ligne != null) {
                parallele.setCoupeRef(cnc.getLigneByUuid(ref_ligne));
            }
            if (taille != null) {
                parallele.setTaille(taille);
            }
            cnc.updateAllCoupes();
            System.out.println("modif parallele");
            if (!isMoving) { saveState(); }
        }
    }

    public void modifEnBordure(UUID p_id, Double largeur, Double hauteur) {
        EnBordure enbordure = (EnBordure) cnc.getCoupeByUuid(p_id);
        if (enbordure != null) {
            if (largeur == null){
                largeur= enbordure.getDimensionsFinales().getLargeur();
            }
            if (hauteur == null){
                hauteur= enbordure.getDimensionsFinales().getHauteur();
            }
            enbordure.setDimensionsFinales( new Dimension(largeur, hauteur));
            cnc.updateAllCoupes();
            System.out.println("modif enbordure");
        }
    }

    public void modifRectangulaire(UUID p_id, Double x1, Double y1,
                                   Double x2, Double y2, Double x3, Double y3)
    {
        Rectangulaire rectangulaire = (Rectangulaire) cnc.getCoupeByUuid(p_id);
        if (rectangulaire != null) {
            if (x1 != null && y1 != null) {
                UUID refptsid = getIntersectionPanel(x1, y1);
                if (refptsid == null) { throw new IllegalArgumentException("Le point de référence est invalide.");}
                rectangulaire.setPoint(1, cnc.getIntersectionPtsByUUID(refptsid));
            }
            if (x2 != null && y2 != null) {
                rectangulaire.setPoint(2, new Coordinates(x2, y2));
            }
            if (x3 != null && y3 != null) {
                rectangulaire.setPoint(3, new Coordinates(x3, y3));
            }
            cnc.updateAllCoupes();
            if (!isMoving) {saveState(); }
        }
    }


    // CNCDTO

    public CNCDTO getCNC() { return cnc.createCNCDTO(); }

    public boolean isCNCValid() { return cnc.isValid(); }

    // PANNEAU

    public void addPanneau(double x, double y, double epaisseur) {
        Dimension dimensionPanneau = new Dimension(x,y);
        cnc.setPanneau(new Panneau(dimensionPanneau, epaisseur));
        cnc.setProfondeur(epaisseur + 0.5);
        saveState();
    }


    public boolean undo() {
        if (historique.hasUndoState()) {
            cnc = historique.popLastState();
            System.out.println("Undo Done.");
            return true;
        }
        System.out.println("No Undo.");
        return false;
    }
    public boolean redo() {
        if (historique.hasRedoState()) {
            cnc = historique.popLastUndoState();
            System.out.println("Redo done.");
            return true;
        }
        System.out.println("No Redo");
        return false;
    }

    public void saveState() {
        historique.saveState(new CNC(cnc));
        System.out.println("Saved current state.");
    }

    public void convertToGCODE(String pathconvertToGCODE) throws IOException {
        cnc.exporter(pathconvertToGCODE);
    }
    public void save(String path) throws IOException {
        cnc.sauvegarder(path);
    }
    public void load(String path) throws IOException {
        CNC loadedCNC = CNC.charger(path);
        if (loadedCNC != null) {
            this.cnc = loadedCNC;
            saveState();
        } else {
            System.err.println("Impossible de charger l'objet CNC de: " + path);
        }
    }

    public String getCoupeInformation(UUID p_id) {
        Coupe coupe = cnc.getCoupeByUuid(p_id);
        if (coupe != null) {
            return coupe.getInformations();
        }
        return null;
    }


    public void finalizeMove(UUID coupeId) {
        Coupe coupe = cnc.getCoupeByUuid(coupeId);
        if (coupe != null) {
            // Update the CNC model to reflect the final position of the coupe
            cnc.updateAllCoupes();
            saveState(); // Save the state to the history after the move is finalized
            System.out.println("Finalized move for coupe ID: " + coupeId);
        } else {
            System.err.println("Invalid coupe ID: " + coupeId);
        }
    }

    public void setMoving(boolean b) {
        isMoving = b;
    }


}