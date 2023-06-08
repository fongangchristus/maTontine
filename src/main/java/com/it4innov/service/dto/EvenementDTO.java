package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.Evenement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvenementDTO implements Serializable {

    private Long id;

    private String libele;

    private String codepot;

    private String montantPayer;

    private String description;

    private Double budget;

    private Instant dateEvenement;

    private TypeEvenementDTO typeEvenement;

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

    public String getCodepot() {
        return codepot;
    }

    public void setCodepot(String codepot) {
        this.codepot = codepot;
    }

    public String getMontantPayer() {
        return montantPayer;
    }

    public void setMontantPayer(String montantPayer) {
        this.montantPayer = montantPayer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Instant getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(Instant dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public TypeEvenementDTO getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(TypeEvenementDTO typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EvenementDTO)) {
            return false;
        }

        EvenementDTO evenementDTO = (EvenementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, evenementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvenementDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", codepot='" + getCodepot() + "'" +
            ", montantPayer='" + getMontantPayer() + "'" +
            ", description='" + getDescription() + "'" +
            ", budget=" + getBudget() +
            ", dateEvenement='" + getDateEvenement() + "'" +
            ", typeEvenement=" + getTypeEvenement() +
            "}";
    }
}
