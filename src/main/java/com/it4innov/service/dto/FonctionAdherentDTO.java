package com.it4innov.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.FonctionAdherent} entity.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FonctionAdherentDTO implements Serializable {

    private Long id;

    @NotNull
    private String matriculeAdherent;

    @NotNull
    private String codeFonction;

    @NotNull
    private LocalDate datePriseFonction;

    private LocalDate dateFinFonction;

    @NotNull
    private Boolean actif;

    private PersonneDTO adherent;

    private FonctionDTO fonction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherent() {
        return matriculeAdherent;
    }

    public void setMatriculeAdherent(String matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public String getCodeFonction() {
        return codeFonction;
    }

    public void setCodeFonction(String codeFonction) {
        this.codeFonction = codeFonction;
    }

    public LocalDate getDatePriseFonction() {
        return datePriseFonction;
    }

    public void setDatePriseFonction(LocalDate datePriseFonction) {
        this.datePriseFonction = datePriseFonction;
    }

    public LocalDate getDateFinFonction() {
        return dateFinFonction;
    }

    public void setDateFinFonction(LocalDate dateFinFonction) {
        this.dateFinFonction = dateFinFonction;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public PersonneDTO getAdherent() {
        return adherent;
    }

    public void setAdherent(PersonneDTO adherent) {
        this.adherent = adherent;
    }

    public FonctionDTO getFonction() {
        return fonction;
    }

    public void setFonction(FonctionDTO fonction) {
        this.fonction = fonction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FonctionAdherentDTO)) {
            return false;
        }

        FonctionAdherentDTO fonctionAdherentDTO = (FonctionAdherentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fonctionAdherentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FonctionAdherentDTO{" +
            "id=" + getId() +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", codeFonction='" + getCodeFonction() + "'" +
            ", datePriseFonction='" + getDatePriseFonction() + "'" +
            ", dateFinFonction='" + getDateFinFonction() + "'" +
            ", actif='" + getActif() + "'" +
            ", adherent=" + getAdherent() +
            ", fonction=" + getFonction() +
            "}";
    }
}
