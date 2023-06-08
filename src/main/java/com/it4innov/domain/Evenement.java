package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A Evenement.
 */
@Entity
@Table(name = "evenement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libele")
    private String libele;

    @Column(name = "codepot")
    private String codepot;

    @Column(name = "montant_payer")
    private String montantPayer;

    @Column(name = "description")
    private String description;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "date_evenement")
    private Instant dateEvenement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "evenements" }, allowSetters = true)
    private TypeEvenement typeEvenement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evenement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public Evenement libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getCodepot() {
        return this.codepot;
    }

    public Evenement codepot(String codepot) {
        this.setCodepot(codepot);
        return this;
    }

    public void setCodepot(String codepot) {
        this.codepot = codepot;
    }

    public String getMontantPayer() {
        return this.montantPayer;
    }

    public Evenement montantPayer(String montantPayer) {
        this.setMontantPayer(montantPayer);
        return this;
    }

    public void setMontantPayer(String montantPayer) {
        this.montantPayer = montantPayer;
    }

    public String getDescription() {
        return this.description;
    }

    public Evenement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBudget() {
        return this.budget;
    }

    public Evenement budget(Double budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Instant getDateEvenement() {
        return this.dateEvenement;
    }

    public Evenement dateEvenement(Instant dateEvenement) {
        this.setDateEvenement(dateEvenement);
        return this;
    }

    public void setDateEvenement(Instant dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public TypeEvenement getTypeEvenement() {
        return this.typeEvenement;
    }

    public void setTypeEvenement(TypeEvenement typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public Evenement typeEvenement(TypeEvenement typeEvenement) {
        this.setTypeEvenement(typeEvenement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evenement)) {
            return false;
        }
        return id != null && id.equals(((Evenement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evenement{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", codepot='" + getCodepot() + "'" +
            ", montantPayer='" + getMontantPayer() + "'" +
            ", description='" + getDescription() + "'" +
            ", budget=" + getBudget() +
            ", dateEvenement='" + getDateEvenement() + "'" +
            "}";
    }
}
