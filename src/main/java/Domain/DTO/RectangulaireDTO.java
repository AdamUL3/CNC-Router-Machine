package Domain.DTO;

import Domain.Rectangulaire;
import Domain.Utils.Ligne;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class RectangulaireDTO
 {
     private final UUID id;
     private final double profondeur;
     private final OutilDTO outil;
     private final CoordinatesDTO refPts;
     private final CoordinatesDTO coin1;
     private final CoordinatesDTO coin2;
     private final List<LigneDTO> coupeLignes = new ArrayList<>();
     private boolean valid;

     public RectangulaireDTO(Rectangulaire rectangulaire) {

         this.id = rectangulaire.getId();
         this.profondeur = rectangulaire.getProfondeur();
         this.outil = new OutilDTO(rectangulaire.getOutil());
         this.refPts = new CoordinatesDTO(rectangulaire.getPoint(1));
         this.coin1 = new CoordinatesDTO(rectangulaire.getPoint(2));
         this.coin2 = new CoordinatesDTO(rectangulaire.getPoint(3));
         for (Ligne ligne : rectangulaire.getLignes()) {
             this.coupeLignes.add(new LigneDTO(ligne));
         }
         try {
             this.valid = rectangulaire.isValid();
         } catch (Exception e) {
             this.valid = false;
         }
     }

     public UUID getId(){ return id; }

     public double getProfondeur() { return profondeur;}

     public OutilDTO getOutil() { return outil;}

     public CoordinatesDTO getPoint(int num) {
         return switch (num) {
             case 1 -> refPts;
             case 2 -> coin1;
             case 3 -> coin2;
             default -> throw new IllegalArgumentException("Le point n'existe pas pour la coupe rectangulaire.");
         };
     }

     public List<LigneDTO> getCoupeLignes() { return coupeLignes; }

     public boolean isValid() { return valid; }
}
