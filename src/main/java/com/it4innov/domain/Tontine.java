package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutTontine;
import com.it4innov.domain.enumeration.TypePenalite;
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

    @Column(name = "nombre_personne")
    private Integer nombrePersonne;

    @Column(name = "marge_beneficiaire")
    private Double margeBeneficiaire;

    @Column(name = "montant_part")
    private Double montantPart;

    @Column(name = "montant_cagnote")
    private Double montantCagnote;

    @Column(name = "penalite_retard_cotisation")
    private Double penaliteRetardCotisation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_penalite")
    private TypePenalite typePenalite;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_premier_tour")
    private LocalDate datePremierTour;

    @Column(name = "date_dernier_tour")
    private LocalDate dateDernierTour;

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

    public Integer getNombrePersonne() {
        return this.nombrePersonne;
    }

    public Tontine nombrePersonne(Integer nombrePersonne) {
        this.setNombrePersonne(nombrePersonne);
        return this;
    }

    public void setNombrePersonne(Integer nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
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

    public Double getMontantCagnote() {
        return this.montantCagnote;
    }

    public Tontine montantCagnote(Double montantCagnote) {
        this.setMontantCagnote(montantCagnote);
        return this;
    }

    public void setMontantCagnote(Double montantCagnote) {
        this.montantCagnote = montantCagnote;
    }

    public Double getPenaliteRetardCotisation() {
        return this.penaliteRetardCotisation;
    }

    public Tontine penaliteRetardCotisation(Double penaliteRetardCotisation) {
        this.setPenaliteRetardCotisation(penaliteRetardCotisation);
        return this;
    }

    public void setPenaliteRetardCotisation(Double penaliteRetardCotisation) {
        this.penaliteRetardCotisation = penaliteRetardCotisation;
    }

    public TypePenalite getTypePenalite() {
        return this.typePenalite;
    }

    public Tontine typePenalite(TypePenalite typePenalite) {
        this.setTypePenalite(typePenalite);
        return this;
    }

    public void setTypePenalite(TypePenalite typePenalite) {
        this.typePenalite = typePenalite;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Tontine dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDatePremierTour() {
        return this.datePremierTour;
    }

    public Tontine datePremierTour(LocalDate datePremierTour) {
        this.setDatePremierTour(datePremierTour);
        return this;
    }

    public void setDatePremierTour(LocalDate datePremierTour) {
        this.datePremierTour = datePremierTour;
    }

    public LocalDate getDateDernierTour() {
        return this.dateDernierTour;
    }

    public Tontine dateDernierTour(LocalDate dateDernierTour) {
        this.setDateDernierTour(dateDernierTour);
        return this;
    }

    public void setDateDernierTour(LocalDate dateDernierTour) {
        this.dateDernierTour = dateDernierTour;
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
            ", nombrePersonne=" + getNombrePersonne() +
            ", margeBeneficiaire=" + getMargeBeneficiaire() +
            ", montantPart=" + getMontantPart() +
            ", montantCagnote=" + getMontantCagnote() +
            ", penaliteRetardCotisation=" + getPenaliteRetardCotisation() +
            ", typePenalite='" + getTypePenalite() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", datePremierTour='" + getDatePremierTour() + "'" +
            ", dateDernierTour='" + getDateDernierTour() + "'" +
            ", statutTontine='" + getStatutTontine() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
