package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.GestionnaireTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireTontineDTO implements Serializable {

    private Long id;

    private String matriculeAdherent;

    private String codeTontine;

    private LocalDate datePriseFonction;

    private LocalDate dateFinFonction;

    private TontineDTO tontine;

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

    public String getCodeTontine() {
        return codeTontine;
    }

    public void setCodeTontine(String codeTontine) {
        this.codeTontine = codeTontine;
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

    public TontineDTO getTontine() {
        return tontine;
    }

    public void setTontine(TontineDTO tontine) {
        this.tontine = tontine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GestionnaireTontineDTO)) {
            return false;
        }

        GestionnaireTontineDTO gestionnaireTontineDTO = (GestionnaireTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gestionnaireTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireTontineDTO{" +
            "id=" + getId() +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", codeTontine='" + getCodeTontine() + "'" +
            ", datePriseFonction='" + getDatePriseFonction() + "'" +
            ", dateFinFonction='" + getDateFinFonction() + "'" +
            ", tontine=" + getTontine() +
            "}";
    }
}
