package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.ContributionPot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContributionPotDTO implements Serializable {

    private Long id;

    private String identifiant;

    @NotNull
    private String matriculeContributeur;

    private Double montantContribution;

    private Instant dateContribution;

    private PotDTO pot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMatriculeContributeur() {
        return matriculeContributeur;
    }

    public void setMatriculeContributeur(String matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public Double getMontantContribution() {
        return montantContribution;
    }

    public void setMontantContribution(Double montantContribution) {
        this.montantContribution = montantContribution;
    }

    public Instant getDateContribution() {
        return dateContribution;
    }

    public void setDateContribution(Instant dateContribution) {
        this.dateContribution = dateContribution;
    }

    public PotDTO getPot() {
        return pot;
    }

    public void setPot(PotDTO pot) {
        this.pot = pot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContributionPotDTO)) {
            return false;
        }

        ContributionPotDTO contributionPotDTO = (ContributionPotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contributionPotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContributionPotDTO{" +
            "id=" + getId() +
            ", identifiant='" + getIdentifiant() + "'" +
            ", matriculeContributeur='" + getMatriculeContributeur() + "'" +
            ", montantContribution=" + getMontantContribution() +
            ", dateContribution='" + getDateContribution() + "'" +
            ", pot=" + getPot() +
            "}";
    }
}
