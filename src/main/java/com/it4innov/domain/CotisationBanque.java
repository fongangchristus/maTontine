package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A CotisationBanque.
 */
@Entity
@Table(name = "cotisation_banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationBanque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "montant")
    private Double montant;

    @Column(name = "date_cotisation")
    private Instant dateCotisation;

    @Column(name = "montant_cotise")
    private Double montantCotise;

    @Column(name = "commentaire")
    private String commentaire;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cotisationBanques", "decaisementBanques", "banque" }, allowSetters = true)
    private CompteBanque compteBanque;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CotisationBanque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public CotisationBanque libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return this.montant;
    }

    public CotisationBanque montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Instant getDateCotisation() {
        return this.dateCotisation;
    }

    public CotisationBanque dateCotisation(Instant dateCotisation) {
        this.setDateCotisation(dateCotisation);
        return this;
    }

    public void setDateCotisation(Instant dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public Double getMontantCotise() {
        return this.montantCotise;
    }

    public CotisationBanque montantCotise(Double montantCotise) {
        this.setMontantCotise(montantCotise);
        return this;
    }

    public void setMontantCotise(Double montantCotise) {
        this.montantCotise = montantCotise;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public CotisationBanque commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CompteBanque getCompteBanque() {
        return this.compteBanque;
    }

    public void setCompteBanque(CompteBanque compteBanque) {
        this.compteBanque = compteBanque;
    }

    public CotisationBanque compteBanque(CompteBanque compteBanque) {
        this.setCompteBanque(compteBanque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CotisationBanque)) {
            return false;
        }
        return id != null && id.equals(((CotisationBanque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationBanque{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateCotisation='" + getDateCotisation() + "'" +
            ", montantCotise=" + getMontantCotise() +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
