package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.EtatCotisation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.CotisationTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationTontineDTO implements Serializable {

    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @Schema(description = "Un membre peux avoir plusieurs comptes/noms tontines")
    private Double montantCotise;

    private String pieceJustifPath;

    private LocalDate dateCotisation;

    private LocalDate dateValidation;

    private String commentaire;

    private EtatCotisation etat;

    private SessionTontineDTO sessionTontine;

    private CompteTontineDTO compteTontine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantCotise() {
        return montantCotise;
    }

    public void setMontantCotise(Double montantCotise) {
        this.montantCotise = montantCotise;
    }

    public String getPieceJustifPath() {
        return pieceJustifPath;
    }

    public void setPieceJustifPath(String pieceJustifPath) {
        this.pieceJustifPath = pieceJustifPath;
    }

    public LocalDate getDateCotisation() {
        return dateCotisation;
    }

    public void setDateCotisation(LocalDate dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public EtatCotisation getEtat() {
        return etat;
    }

    public void setEtat(EtatCotisation etat) {
        this.etat = etat;
    }

    public SessionTontineDTO getSessionTontine() {
        return sessionTontine;
    }

    public void setSessionTontine(SessionTontineDTO sessionTontine) {
        this.sessionTontine = sessionTontine;
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
        if (!(o instanceof CotisationTontineDTO)) {
            return false;
        }

        CotisationTontineDTO cotisationTontineDTO = (CotisationTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cotisationTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationTontineDTO{" +
            "id=" + getId() +
            ", montantCotise=" + getMontantCotise() +
            ", pieceJustifPath='" + getPieceJustifPath() + "'" +
            ", dateCotisation='" + getDateCotisation() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", etat='" + getEtat() + "'" +
            ", sessionTontine=" + getSessionTontine() +
            ", compteTontine=" + getCompteTontine() +
            "}";
    }
}
