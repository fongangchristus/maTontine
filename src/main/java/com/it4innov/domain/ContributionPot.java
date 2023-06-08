package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ContributionPot.
 */
@Entity
@Table(name = "contribution_pot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContributionPot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "identifiant")
    private String identifiant;

    @NotNull
    @Column(name = "matricule_contributeur", nullable = false)
    private String matriculeContributeur;

    @Column(name = "montant_contribution")
    private Double montantContribution;

    @Column(name = "date_contribution")
    private Instant dateContribution;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contributionPots", "commentairePots", "typePot" }, allowSetters = true)
    private Pot pot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContributionPot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return this.identifiant;
    }

    public ContributionPot identifiant(String identifiant) {
        this.setIdentifiant(identifiant);
        return this;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMatriculeContributeur() {
        return this.matriculeContributeur;
    }

    public ContributionPot matriculeContributeur(String matriculeContributeur) {
        this.setMatriculeContributeur(matriculeContributeur);
        return this;
    }

    public void setMatriculeContributeur(String matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public Double getMontantContribution() {
        return this.montantContribution;
    }

    public ContributionPot montantContribution(Double montantContribution) {
        this.setMontantContribution(montantContribution);
        return this;
    }

    public void setMontantContribution(Double montantContribution) {
        this.montantContribution = montantContribution;
    }

    public Instant getDateContribution() {
        return this.dateContribution;
    }

    public ContributionPot dateContribution(Instant dateContribution) {
        this.setDateContribution(dateContribution);
        return this;
    }

    public void setDateContribution(Instant dateContribution) {
        this.dateContribution = dateContribution;
    }

    public Pot getPot() {
        return this.pot;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    public ContributionPot pot(Pot pot) {
        this.setPot(pot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContributionPot)) {
            return false;
        }
        return id != null && id.equals(((ContributionPot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContributionPot{" +
            "id=" + getId() +
            ", identifiant='" + getIdentifiant() + "'" +
            ", matriculeContributeur='" + getMatriculeContributeur() + "'" +
            ", montantContribution=" + getMontantContribution() +
            ", dateContribution='" + getDateContribution() + "'" +
            "}";
    }
}
