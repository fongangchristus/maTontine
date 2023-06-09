package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.StatutTontine;
import com.it4innov.domain.enumeration.TypePenalite;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Tontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TontineDTO implements Serializable {

    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @NotNull
    @Schema(description = "Un membre peux avoir plusieurs comptes/noms tontines", required = true)
    private String codeAssociation;

    private String libele;

    private Integer nombreTour;

    private Integer nombrePersonne;

    private Double margeBeneficiaire;

    private Double montantPart;

    private Double montantCagnote;

    private Double penaliteRetardCotisation;

    private TypePenalite typePenalite;

    private LocalDate dateCreation;

    private LocalDate datePremierTour;

    private LocalDate dateDernierTour;

    private StatutTontine statutTontine;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Integer getNombreTour() {
        return nombreTour;
    }

    public void setNombreTour(Integer nombreTour) {
        this.nombreTour = nombreTour;
    }

    public Integer getNombrePersonne() {
        return nombrePersonne;
    }

    public void setNombrePersonne(Integer nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public Double getMargeBeneficiaire() {
        return margeBeneficiaire;
    }

    public void setMargeBeneficiaire(Double margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public Double getMontantPart() {
        return montantPart;
    }

    public void setMontantPart(Double montantPart) {
        this.montantPart = montantPart;
    }

    public Double getMontantCagnote() {
        return montantCagnote;
    }

    public void setMontantCagnote(Double montantCagnote) {
        this.montantCagnote = montantCagnote;
    }

    public Double getPenaliteRetardCotisation() {
        return penaliteRetardCotisation;
    }

    public void setPenaliteRetardCotisation(Double penaliteRetardCotisation) {
        this.penaliteRetardCotisation = penaliteRetardCotisation;
    }

    public TypePenalite getTypePenalite() {
        return typePenalite;
    }

    public void setTypePenalite(TypePenalite typePenalite) {
        this.typePenalite = typePenalite;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDatePremierTour() {
        return datePremierTour;
    }

    public void setDatePremierTour(LocalDate datePremierTour) {
        this.datePremierTour = datePremierTour;
    }

    public LocalDate getDateDernierTour() {
        return dateDernierTour;
    }

    public void setDateDernierTour(LocalDate dateDernierTour) {
        this.dateDernierTour = dateDernierTour;
    }

    public StatutTontine getStatutTontine() {
        return statutTontine;
    }

    public void setStatutTontine(StatutTontine statutTontine) {
        this.statutTontine = statutTontine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TontineDTO)) {
            return false;
        }

        TontineDTO tontineDTO = (TontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TontineDTO{" +
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
