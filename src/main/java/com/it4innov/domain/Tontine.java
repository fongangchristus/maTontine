package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutTontine;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Tontine.
 */
@Entity
@Table(name = "tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @Column(name = "libele")
    private String libele;

    @Column(name = "nombre_tour")
    private Integer nombreTour;

    @Column(name = "nombre_max_personne")
    private Integer nombreMaxPersonne;

    @Column(name = "marge_beneficiaire")
    private Double margeBeneficiaire;

    @Column(name = "montant_part")
    private Double montantPart;

    @Column(name = "amande_echec")
    private Double amandeEchec;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_tontine")
    private StatutTontine statutTontine;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "tontine")
    @JsonIgnoreProperties(value = { "cotisationTontines", "decaissementTontines", "tontine" }, allowSetters = true)
    private Set<SessionTontine> sessionTontines = new HashSet<>();

    @OneToMany(mappedBy = "tontine")
    @JsonIgnoreProperties(value = { "tontine" }, allowSetters = true)
    private Set<GestionnaireTontine> gestionnaireTontines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public Tontine codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getLibele() {
        return this.libele;
    }

    public Tontine libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Integer getNombreTour() {
        return this.nombreTour;
    }

    public Tontine nombreTour(Integer nombreTour) {
        this.setNombreTour(nombreTour);
        return this;
    }

    public void setNombreTour(Integer nombreTour) {
        this.nombreTour = nombreTour;
    }

    public Integer getNombreMaxPersonne() {
        return this.nombreMaxPersonne;
    }

    public Tontine nombreMaxPersonne(Integer nombreMaxPersonne) {
        this.setNombreMaxPersonne(nombreMaxPersonne);
        return this;
    }

    public void setNombreMaxPersonne(Integer nombreMaxPersonne) {
        this.nombreMaxPersonne = nombreMaxPersonne;
    }

    public Double getMargeBeneficiaire() {
        return this.margeBeneficiaire;
    }

    public Tontine margeBeneficiaire(Double margeBeneficiaire) {
        this.setMargeBeneficiaire(margeBeneficiaire);
        return this;
    }

    public void setMargeBeneficiaire(Double margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public Double getMontantPart() {
        return this.montantPart;
    }

    public Tontine montantPart(Double montantPart) {
        this.setMontantPart(montantPart);
        return this;
    }

    public void setMontantPart(Double montantPart) {
        this.montantPart = montantPart;
    }

    public Double getAmandeEchec() {
        return this.amandeEchec;
    }

    public Tontine amandeEchec(Double amandeEchec) {
        this.setAmandeEchec(amandeEchec);
        return this;
    }

    public void setAmandeEchec(Double amandeEchec) {
        this.amandeEchec = amandeEchec;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Tontine dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Tontine dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutTontine getStatutTontine() {
        return this.statutTontine;
    }

    public Tontine statutTontine(StatutTontine statutTontine) {
        this.setStatutTontine(statutTontine);
        return this;
    }

    public void setStatutTontine(StatutTontine statutTontine) {
        this.statutTontine = statutTontine;
    }

    public String getDescription() {
        return this.description;
    }

    public Tontine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SessionTontine> getSessionTontines() {
        return this.sessionTontines;
    }

    public void setSessionTontines(Set<SessionTontine> sessionTontines) {
        if (this.sessionTontines != null) {
            this.sessionTontines.forEach(i -> i.setTontine(null));
        }
        if (sessionTontines != null) {
            sessionTontines.forEach(i -> i.setTontine(this));
        }
        this.sessionTontines = sessionTontines;
    }

    public Tontine sessionTontines(Set<SessionTontine> sessionTontines) {
        this.setSessionTontines(sessionTontines);
        return this;
    }

    public Tontine addSessionTontine(SessionTontine sessionTontine) {
        this.sessionTontines.add(sessionTontine);
        sessionTontine.setTontine(this);
        return this;
    }

    public Tontine removeSessionTontine(SessionTontine sessionTontine) {
        this.sessionTontines.remove(sessionTontine);
        sessionTontine.setTontine(null);
        return this;
    }

    public Set<GestionnaireTontine> getGestionnaireTontines() {
        return this.gestionnaireTontines;
    }

    public void setGestionnaireTontines(Set<GestionnaireTontine> gestionnaireTontines) {
        if (this.gestionnaireTontines != null) {
            this.gestionnaireTontines.forEach(i -> i.setTontine(null));
        }
        if (gestionnaireTontines != null) {
            gestionnaireTontines.forEach(i -> i.setTontine(this));
        }
        this.gestionnaireTontines = gestionnaireTontines;
    }

    public Tontine gestionnaireTontines(Set<GestionnaireTontine> gestionnaireTontines) {
        this.setGestionnaireTontines(gestionnaireTontines);
        return this;
    }

    public Tontine addGestionnaireTontine(GestionnaireTontine gestionnaireTontine) {
        this.gestionnaireTontines.add(gestionnaireTontine);
        gestionnaireTontine.setTontine(this);
        return this;
    }

    public Tontine removeGestionnaireTontine(GestionnaireTontine gestionnaireTontine) {
        this.gestionnaireTontines.remove(gestionnaireTontine);
        gestionnaireTontine.setTontine(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tontine)) {
            return false;
        }
        return id != null && id.equals(((Tontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tontine{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", libele='" + getLibele() + "'" +
            ", nombreTour=" + getNombreTour() +
            ", nombreMaxPersonne=" + getNombreMaxPersonne() +
            ", margeBeneficiaire=" + getMargeBeneficiaire() +
            ", montantPart=" + getMontantPart() +
            ", amandeEchec=" + getAmandeEchec() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", statutTontine='" + getStatutTontine() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
