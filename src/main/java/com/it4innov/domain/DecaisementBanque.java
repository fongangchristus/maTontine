package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A DecaisementBanque.
 */
@Entity
@Table(name = "decaisement_banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaisementBanque implements Serializable {

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

    @Column(name = "date_decaissement")
    private Instant dateDecaissement;

    @Column(name = "montant_decaisse")
    private Double montantDecaisse;

    @Column(name = "commentaire")
    private String commentaire;

    @ManyToOne
    @JsonIgnoreProperties(value = { "cotisationBanques", "decaisementBanques", "banque" }, allowSetters = true)
    private CompteBanque compteBanque;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DecaisementBanque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public DecaisementBanque libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return this.montant;
    }

    public DecaisementBanque montant(Double montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Instant getDateDecaissement() {
        return this.dateDecaissement;
    }

    public DecaisementBanque dateDecaissement(Instant dateDecaissement) {
        this.setDateDecaissement(dateDecaissement);
        return this;
    }

    public void setDateDecaissement(Instant dateDecaissement) {
        this.dateDecaissement = dateDecaissement;
    }

    public Double getMontantDecaisse() {
        return this.montantDecaisse;
    }

    public DecaisementBanque montantDecaisse(Double montantDecaisse) {
        this.setMontantDecaisse(montantDecaisse);
        return this;
    }

    public void setMontantDecaisse(Double montantDecaisse) {
        this.montantDecaisse = montantDecaisse;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public DecaisementBanque commentaire(String commentaire) {
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

    public DecaisementBanque compteBanque(CompteBanque compteBanque) {
        this.setCompteBanque(compteBanque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecaisementBanque)) {
            return false;
        }
        return id != null && id.equals(((DecaisementBanque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaisementBanque{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateDecaissement='" + getDateDecaissement() + "'" +
            ", montantDecaisse=" + getMontantDecaisse() +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
