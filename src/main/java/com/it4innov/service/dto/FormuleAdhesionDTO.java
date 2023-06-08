package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.FormuleAdhesion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormuleAdhesionDTO implements Serializable {

    private Long id;

    private Boolean adhesionPeriodique;

    private Instant dateDebut;

    private Integer dureeAdhesionMois;

    private Boolean montantLibre;

    private String description;

    private Double tarif;

    private AdhesionDTO adhesion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdhesionPeriodique() {
        return adhesionPeriodique;
    }

    public void setAdhesionPeriodique(Boolean adhesionPeriodique) {
        this.adhesionPeriodique = adhesionPeriodique;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Integer getDureeAdhesionMois() {
        return dureeAdhesionMois;
    }

    public void setDureeAdhesionMois(Integer dureeAdhesionMois) {
        this.dureeAdhesionMois = dureeAdhesionMois;
    }

    public Boolean getMontantLibre() {
        return montantLibre;
    }

    public void setMontantLibre(Boolean montantLibre) {
        this.montantLibre = montantLibre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public AdhesionDTO getAdhesion() {
        return adhesion;
    }

    public void setAdhesion(AdhesionDTO adhesion) {
        this.adhesion = adhesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormuleAdhesionDTO)) {
            return false;
        }

        FormuleAdhesionDTO formuleAdhesionDTO = (FormuleAdhesionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, formuleAdhesionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormuleAdhesionDTO{" +
            "id=" + getId() +
            ", adhesionPeriodique='" + getAdhesionPeriodique() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dureeAdhesionMois=" + getDureeAdhesionMois() +
            ", montantLibre='" + getMontantLibre() + "'" +
            ", description='" + getDescription() + "'" +
            ", tarif=" + getTarif() +
            ", adhesion=" + getAdhesion() +
            "}";
    }
}
