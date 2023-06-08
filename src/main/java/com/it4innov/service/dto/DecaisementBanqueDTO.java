package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.DecaisementBanque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaisementBanqueDTO implements Serializable {

    private Long id;

    private String libelle;

    private Double montant;

    private Instant dateDecaissement;

    private Double montantDecaisse;

    private String commentaire;

    private CompteBanqueDTO compteBanque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Instant getDateDecaissement() {
        return dateDecaissement;
    }

    public void setDateDecaissement(Instant dateDecaissement) {
        this.dateDecaissement = dateDecaissement;
    }

    public Double getMontantDecaisse() {
        return montantDecaisse;
    }

    public void setMontantDecaisse(Double montantDecaisse) {
        this.montantDecaisse = montantDecaisse;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public CompteBanqueDTO getCompteBanque() {
        return compteBanque;
    }

    public void setCompteBanque(CompteBanqueDTO compteBanque) {
        this.compteBanque = compteBanque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecaisementBanqueDTO)) {
            return false;
        }

        DecaisementBanqueDTO decaisementBanqueDTO = (DecaisementBanqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, decaisementBanqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaisementBanqueDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateDecaissement='" + getDateDecaissement() + "'" +
            ", montantDecaisse=" + getMontantDecaisse() +
            ", commentaire='" + getCommentaire() + "'" +
            ", compteBanque=" + getCompteBanque() +
            "}";
    }
}
