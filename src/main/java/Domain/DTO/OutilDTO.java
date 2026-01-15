package Domain.DTO;

import Domain.Outil;

import java.util.UUID;


public class OutilDTO {

    private final UUID id;
    private final double m_diametre;
    private final String m_nom;

    public OutilDTO(Outil outil) {
        this.id = outil.getId();
        this.m_diametre = outil.getDiametre();
        this.m_nom = outil.getNom();
    }

    public UUID getId(){
        return id;
    }

    public double getDiametre(){
        return m_diametre;
    }

    public String getNom(){
        return m_nom;
    }


}
