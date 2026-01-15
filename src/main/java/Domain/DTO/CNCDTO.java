package Domain.DTO;
import Domain.*;
import Domain.Utils.Intersection;

import java.util.ArrayList;
import java.util.List;

public class CNCDTO {
    private final OutilDTO currOutil;
    private final List<OutilDTO> outilsList;
    private final List<ZoneInterditeDTO> zoneListe;


    private final List<EnLDTO> enLListe;
    private final List<EnBordureDTO> enbordureListe;
    private final List<ParalleleDTO> paralleleListe;
    private final List<RectangulaireDTO> rectangulaireListe;
    private final List<IntersectionDTO>  intersectionListe;

    private PanneauDTO panneau = null;
    private final double profondeur;

    public CNCDTO(CNC cnc) {
        this.currOutil = new OutilDTO(cnc.getCurrOutil());

        this.outilsList = new ArrayList<>();
        for (Outil outil : cnc.getOutilsList()) {
            this.outilsList.add(new OutilDTO(outil));
        }
        this.zoneListe = new ArrayList<>();
        for (ZoneInterdite zoneInterdite : cnc.getZoneListe()) {
            this.zoneListe.add(new ZoneInterditeDTO(zoneInterdite));
        }
        this.enLListe = new ArrayList<>();
        this.enbordureListe = new ArrayList<>();
        this.paralleleListe = new ArrayList<>();
        this.rectangulaireListe = new ArrayList<>();
        this.intersectionListe = new ArrayList<>();

        for (Coupe coupe : cnc.getCoupeListe()){
            if (coupe instanceof EnL){
                this.enLListe.add(new EnLDTO((EnL) coupe));
            }
            else if (coupe instanceof EnBordure){
                this.enbordureListe.add(new EnBordureDTO((EnBordure) coupe));
            }
            else if (coupe instanceof Parallele){
                this.paralleleListe.add(new ParalleleDTO((Parallele) coupe));
            }
            else if (coupe instanceof Rectangulaire){
                this.rectangulaireListe.add(new RectangulaireDTO((Rectangulaire) coupe));
            }
        }
        this.profondeur = cnc.getProfondeur();
        if (cnc.getPanneau() != null) {
            this.panneau = new PanneauDTO(cnc.getPanneau());
        }
        for (Intersection i : cnc.getPtsIntersectionsListe()) {
            this.intersectionListe.add(new IntersectionDTO(i));
        }
    }

    public double getProfondeur() {
        return profondeur;
    }

    public OutilDTO getCurrOutil() {
        return currOutil;
    }

    public List<OutilDTO> getOutilsList() {
        return outilsList;
    }

    public List<ZoneInterditeDTO> getZoneListe() {
        return zoneListe;
    }

    public List<EnLDTO> getEnLListe() {
        return enLListe;
    }

    public List<EnBordureDTO> getEnBordureListe() {
        return enbordureListe;
    }

    public List<ParalleleDTO> getParalleleListe() {
        return paralleleListe;
    }

    public List<RectangulaireDTO> getRectangulaireListe() {
        return rectangulaireListe;
    }

    public PanneauDTO getPanneau() {
        return panneau;
    }

    public List<IntersectionDTO> getIntersectionListe() { return intersectionListe; }

}
