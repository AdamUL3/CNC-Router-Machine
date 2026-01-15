package Domain;

import java.util.UUID;
import java.io.Serializable;


public class Outil implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private double m_diametre;
    private String m_nom;
    private boolean deletable = true;

    public Outil(String p_nom, double p_diametre) {
        this.id = UUID.randomUUID();
        this.m_diametre = p_diametre;
        this.m_nom = p_nom;
    }

    public Outil(Outil outil) {
        this.id = outil.id;
        this.m_diametre = outil.m_diametre;
        this.m_nom = outil.m_nom;
        this.deletable = outil.deletable;
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

    public void setDiametre(double p_diametre){
        this.m_diametre = p_diametre;
    }

    public void setNom(String p_nom){
        if (p_nom.isEmpty()) { throw new IllegalArgumentException("L'outil ne peut pas avoir un nom vide."); }
        this.m_nom = p_nom;
    }

    public void setDeletable(boolean del) { this.deletable = del; }

    public boolean isDeletable() { return deletable; }

}
