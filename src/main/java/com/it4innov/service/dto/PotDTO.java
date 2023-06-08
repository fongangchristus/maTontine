package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.StatutPot;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Pot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PotDTO implements Serializable {

    private Long id;

    private String libele;

    @NotNull
    private String codepot;

    private Double montantCible;

    private String description;

    private LocalDate dateDebutCollecte;

    private LocalDate dateFinCollecte;

    private StatutPot statut;

    private TypePotDTO typePot;

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

    public Double getMontantCible() {
        return montantCible;
    }

    public void setMontantCible(Double montantCible) {
        this.montantCible = montantCible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebutCollecte() {
        return dateDebutCollecte;
    }

    public void setDateDebutCollecte(LocalDate dateDebutCollecte) {
        this.dateDebutCollecte = dateDebutCollecte;
    }

    public LocalDate getDateFinCollecte() {
        return dateFinCollecte;
    }

    public void setDateFinCollecte(LocalDate dateFinCollecte) {
        this.dateFinCollecte = dateFinCollecte;
    }

    public StatutPot getStatut() {
        return statut;
    }

    public void setStatut(StatutPot statut) {
        this.statut = statut;
    }

    public TypePotDTO getTypePot() {
        return typePot;
    }

    public void setTypePot(TypePotDTO typePot) {
        this.typePot = typePot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PotDTO)) {
            return false;
        }

        PotDTO potDTO = (PotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, potDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", codepot='" + getCodepot() + "'" +
            ", montantCible=" + getMontantCible() +
            ", description='" + getDescription() + "'" +
            ", dateDebutCollecte='" + getDateDebutCollecte() + "'" +
            ", dateFinCollecte='" + getDateFinCollecte() + "'" +
            ", statut='" + getStatut() + "'" +
            ", typePot=" + getTypePot() +
            "}";
    }
}
