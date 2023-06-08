package com.it4innov.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.DecaissementTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaissementTontineDTO implements Serializable {

    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @Schema(description = "Un membre peux avoir plusieurs comptes/noms tontines")
    private String libele;

    private LocalDate dateDecaissement;

    private Double montantDecaisse;

    private String commentaire;

    private SessionTontineDTO tontine;

    private CompteTontineDTO compteTontine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public LocalDate getDateDecaissement() {
        return dateDecaissement;
    }

    public void setDateDecaissement(LocalDate dateDecaissement) {
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

    public SessionTontineDTO getTontine() {
        return tontine;
    }

    public void setTontine(SessionTontineDTO tontine) {
        this.tontine = tontine;
    }

    public CompteTontineDTO getCompteTontine() {
        return compteTontine;
    }

    public void setCompteTontine(CompteTontineDTO compteTontine) {
        this.compteTontine = compteTontine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecaissementTontineDTO)) {
            return false;
        }

        DecaissementTontineDTO decaissementTontineDTO = (DecaissementTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, decaissementTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaissementTontineDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", dateDecaissement='" + getDateDecaissement() + "'" +
            ", montantDecaisse=" + getMontantDecaisse() +
            ", commentaire='" + getCommentaire() + "'" +
            ", tontine=" + getTontine() +
            ", compteTontine=" + getCompteTontine() +
            "}";
    }
}
