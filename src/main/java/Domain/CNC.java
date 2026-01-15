package Domain;

import Domain.DTO.CNCDTO;
import Domain.Utils.*;

import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static Domain.Utils.GcodeCalculation.*;

public class CNC implements Serializable {


    private Outil currOutil = new Outil("Défaut", 2);
    private List<Outil> outilsList = new ArrayList<>();
    private final List<Coupe> coupeListe = new ArrayList<>();
    private double profondeur = 0.5;
    private Panneau panneau = null;
    private List<Intersection> ptsIntersectionsListe = new ArrayList<>();
    private static final long serialVersionUID = 1L;


    public CNC() {


        // Paramètres par défaut
        currOutil.setDeletable(false);
        this.addOutil(currOutil);

    }



    public CNC(CNC cnc) {
        this.profondeur = cnc.profondeur;
        for (Outil outil : cnc.outilsList) {
            Outil temp = new Outil(outil);
            if(temp.getId() == cnc.currOutil.getId()){
                this.currOutil = temp;
            }
            this.outilsList.add(temp);
        }
        if (cnc.panneau != null) {
            this.panneau = new Panneau(cnc.panneau);
        }

        for (Coupe coupe : cnc.coupeListe) {
            if (coupe instanceof EnBordure) {
                this.coupeListe.add(new EnBordure((EnBordure) coupe, this));
            } else if (coupe instanceof  Parallele) {
                this.coupeListe.add(new Parallele((Parallele) coupe, this));
            } else if (coupe instanceof Rectangulaire) {
                this.coupeListe.add(new Rectangulaire((Rectangulaire) coupe, this));
            } else if (coupe instanceof EnL) {
                this.coupeListe.add(new EnL((EnL) coupe, this));
            }
        }
        if (!cnc.getPtsIntersectionsListe().isEmpty()) {
            for (Intersection intersection : cnc.getPtsIntersectionsListe()) {
                this.ptsIntersectionsListe.add(new Intersection(intersection, this));
            }
            for (Coupe coupe : coupeListe) {
                if (coupe instanceof Rectangulaire rectangle) {
                    rectangle.setPoint(1, getIntersectionPtsByUUID(rectangle.getPoint(1).getId()));
                }
                else if (coupe instanceof EnL enl) {
                    enl.setRefs(getLigneByUuid(enl.getVerticalRef().getId()), getLigneByUuid(enl.getHorizontalRef().getId()));
                }
                else if  (coupe instanceof Parallele parallele) {
                    parallele.setCoupeRef(getLigneByUuid(parallele.getCoupeRef().getId()));
                }
                coupe.updateCoupe();
            }
        }
        updateAllCoupes();
    }

    // Getter et setter pour 'currOutil'
    public Outil getCurrOutil() {
        return currOutil;
    }

    public void setCurrOutil(Outil currOutil) {
        this.currOutil = currOutil;
    }

    // Accesseurs et mutateurs pour 'outilsList'
    public List<Outil> getOutilsList() {
        return outilsList;
    }

    public void setOutilsList(List<Outil> outilsList) {
        this.outilsList = outilsList;
    }


    // Accesseurs et mutateurs pour 'zoneListe'
    public List<ZoneInterdite> getZoneListe() {
        if (panneau == null) {
            List<ZoneInterdite> emptylist = new ArrayList<>();
        return emptylist ; }
        return panneau.getZoneList();
    }


    // Accesseurs et mutateurs pour 'coupeListe'
    public List<Coupe> getCoupeListe() {
        return coupeListe;
    }


    // Accesseurs et mutateurs pour 'profondeur'
    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    // Getter et setter pour 'panneau'

    public Panneau getPanneau() {
        return panneau;
    }

    public void setPanneau(Panneau panneau) {
        this.panneau = panneau;
        for (Coupe coupe : coupeListe) {
            coupe.selfDelete();
        }
        this.getCoupeListe().clear();
        updatePtsIntersection();
    }

    // Getter et setter pour un seul element


    public Outil getOutilByUuid(UUID uuid) {
        for (Outil outil : outilsList) {
            if (outil.getId().equals(uuid)) {
                return outil;
            }
        }
        return null;
    }

    public ZoneInterdite getZoneByUuid(UUID uuid) {
        for (ZoneInterdite zone : panneau.getZoneList()) {
            if (zone.getId().equals(uuid)) {
                return zone;
            }
        }
        return null;
    }

    public Coupe getCoupeByUuid(UUID uuid) {
        for (Coupe coupe : coupeListe) {
            if (coupe.getId().equals(uuid)) {
                return coupe;
            }
        }
        return null;
    }

    public Ligne getLigneByUuid(UUID uuid) {

        for (Ligne ligne : getLignes()) {
            if (ligne.getId().equals(uuid)) {
                return ligne;
            }
        }
        return null;
    }

    public void addOutil(Outil outil) {
        if (outilsList.size() >= 12) {
            throw new IllegalArgumentException("Le nombre d'outil ne peut dépasser 12.");
        }
        for (Outil p_outil : outilsList) {
            if (p_outil.getNom().equals(outil.getNom())) {
                throw new IllegalArgumentException("Un outil avec le même nom existe déjà.");
            }
        }
        outilsList.add(outil); // Ajoute l'outil après la boucle si aucun doublon n'a été trouvé
    }


    public void suppOutil(Outil outil) {
        if (!outil.isDeletable()) {
            throw new IllegalArgumentException("Vous ne pouvez pas supprimer cet outil.");
        }
        if (outilsList.size() == 1) {
            throw new IllegalArgumentException("Il ne peut pas y avoir aucun outil.");
        } else {
            outilsList.remove(outil);
        }
    }

    public void addZone(ZoneInterdite zone) {
        if (panneau != null) {
            panneau.addZone(zone);
        }
        else {
            throw new IllegalArgumentException("Il n'y a aucun panneau !");
        }
    }

    public void suppZone(ZoneInterdite zone) {
        if (panneau.getZoneList().isEmpty()) {
            throw new IllegalArgumentException("Il n'y a aucune zone interdite à supprimer.");
        } else {
            panneau.removeZone(zone);
        }
    }

    public void suppCoupe(Coupe coupe) {
        if (coupeListe.isEmpty()) {
            throw new IllegalArgumentException("Il n'y a aucune coupe à supprimer.");
        } else {
            coupeListe.remove(coupe);
            coupe.selfDelete();
        }
    }

    public void addCoupe(Coupe coupe) {
        if (!coupe.isCoupeInPanneau()) {
            throw new IllegalArgumentException("La coupe est pas dans le panneau.");
        }
        coupeListe.add(coupe);
        updateAllCoupes();
    }

    public CNCDTO createCNCDTO() {
        return new CNCDTO(this);
    }

    public void updateAllCoupes() {
        for (Coupe coupe : coupeListe) {
            try {
                updatePtsIntersection();
                coupe.updateCoupe();
            }catch (Exception e) {}
        }
        updatePtsIntersection();
    }


    public List<Ligne> getLignes() {
        List<Ligne> lignes = new ArrayList<>();
        for (Coupe coupe : coupeListe) {
            for (Ligne l : coupe.getLignes()) {
                if (l.getId() != null) {
                    lignes.add(l);
                }
            }
        }
        if (panneau == null) { return lignes; }
        for (Ligne li : panneau.getBordureList()) {
            if (li.getId() != null) {
                lignes.add(li);
            }
        }
        return lignes;
    }

    public Intersection getIntersectionByLignes(Ligne ligne1, Ligne ligne2) {
        for (Intersection inter : ptsIntersectionsListe) {
            if (inter.getLigne1().getId() == ligne1.getId() && inter.getLigne2().getId() == ligne2.getId()) {
                return inter;
            } else if (inter.getLigne2().getId() == ligne1.getId() && inter.getLigne1().getId() == ligne2.getId()) {
                return inter;
            }
        }
        return null;
    }

    public void updatePtsIntersection() {
        for (Ligne li : getLignes()) {
            for (Ligne li2 : getLignes()) {
                if (li.getId() != li2.getId()) {
                    Intersection inter = getIntersectionByLignes(li, li2);
                    if (inter == null) {
                        Intersection inter2 = new Intersection(li, li2);
                        if (inter2.isIntersection()) {
                            ptsIntersectionsListe.add(inter2);
                        }
                    }
                }
            }
        }

        if (!ptsIntersectionsListe.isEmpty()) {
            Iterator<Intersection> iterator = ptsIntersectionsListe.iterator();
            while (iterator.hasNext()) {
                Intersection inter = iterator.next();
                if (!inter.isIntersection()) {
                    inter.selfDelete();
                    iterator.remove();
                }
            }
        }
    }


    public List<Intersection> getPtsIntersectionsListe() { return ptsIntersectionsListe; }

    public Coordinates getIntersectionPtsByUUID(UUID uuid) {
        for (Intersection inter : ptsIntersectionsListe) {
            if (inter.getIntersectionPts().getId() == uuid) {
                return inter.getIntersectionPts();
            }
        }
        return null;
    }

    public boolean isValid() {
        for (Coupe coupe : coupeListe) {
            try {
                coupe.isValid();
            }
            catch (Exception e) { return false;}
        }
        return true;
    }

    public void exporter(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            writer.println("%");
            writer.println("O1");

            writer.println("G90 G21 G64 P0.01");
            writer.println("G0 Z10");

            for (Coupe coupe : coupeListe) {
                for (Ligne ligne : coupe.getLignes()) {
                    Coordinates start = ligne.getPoint1();
                    Coordinates end = ligne.getPoint2();

                    writer.printf("G0 X%.3f Y%.3f\n", start.getX(), start.getY());

                    writer.printf("G1 X%.3f Y%.3f F1000\n", end.getX(), end.getY());
                }
            }

            writer.println("M30");
            writer.println("%");
        }
    }
    public void sauvegarder(String fileName) {
        if (!fileName.endsWith(".cnc")) {
            fileName += ".cnc";
        }

        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(this);

            System.out.println("Sauvegarde réussie vers " + fileName);
        } catch (IOException e) {
            System.err.println("Une erreur s'est produite durant la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static CNC charger(String fileName) {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            return (CNC) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur durant le chargement : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}







