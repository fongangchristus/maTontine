package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.Langue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Association} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssociationDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeAssociation;

    private String denomination;

    private String slogan;

    private String logoPath;

    private String reglementPath;

    private String statutPath;

    private String description;

    private LocalDate dateCreation;

    private String fuseauHoraire;

    private Langue langue;

    private String presentation;

    private MonnaieDTO monnaie;

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

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getReglementPath() {
        return reglementPath;
    }

    public void setReglementPath(String reglementPath) {
        this.reglementPath = reglementPath;
    }

    public String getStatutPath() {
        return statutPath;
    }

    public void setStatutPath(String statutPath) {
        this.statutPath = statutPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getFuseauHoraire() {
        return fuseauHoraire;
    }

    public void setFuseauHoraire(String fuseauHoraire) {
        this.fuseauHoraire = fuseauHoraire;
    }

    public Langue getLangue() {
        return langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public MonnaieDTO getMonnaie() {
        return monnaie;
    }

    public void setMonnaie(MonnaieDTO monnaie) {
        this.monnaie = monnaie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssociationDTO)) {
            return false;
        }

        AssociationDTO associationDTO = (AssociationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, associationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssociationDTO{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", denomination='" + getDenomination() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", logoPath='" + getLogoPath() + "'" +
            ", reglementPath='" + getReglementPath() + "'" +
            ", statutPath='" + getStatutPath() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", fuseauHoraire='" + getFuseauHoraire() + "'" +
            ", langue='" + getLangue() + "'" +
            ", presentation='" + getPresentation() + "'" +
            ", monnaie=" + getMonnaie() +
            "}";
    }
}
