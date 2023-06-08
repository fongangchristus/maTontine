package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.CotisationBanque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationBanqueDTO implements Serializable {

    private Long id;

    private String libelle;

    private Double montant;

    private Instant dateCotisation;

    private Double montantCotise;

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

    public Instant getDateCotisation() {
        return dateCotisation;
    }

    public void setDateCotisation(Instant dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public Double getMontantCotise() {
        return montantCotise;
    }

    public void setMontantCotise(Double montantCotise) {
        this.montantCotise = montantCotise;
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
        if (!(o instanceof CotisationBanqueDTO)) {
            return false;
        }

        CotisationBanqueDTO cotisationBanqueDTO = (CotisationBanqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cotisationBanqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationBanqueDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", montant=" + getMontant() +
            ", dateCotisation='" + getDateCotisation() + "'" +
            ", montantCotise=" + getMontantCotise() +
            ", commentaire='" + getCommentaire() + "'" +
            ", compteBanque=" + getCompteBanque() +
            "}";
    }
}
