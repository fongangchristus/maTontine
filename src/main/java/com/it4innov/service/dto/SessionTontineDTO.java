package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.SessionTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTontineDTO implements Serializable {

    private Long id;

    private String libelle;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private TontineDTO tontine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
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
        if (!(o instanceof SessionTontineDTO)) {
            return false;
        }

        SessionTontineDTO sessionTontineDTO = (SessionTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sessionTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTontineDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", tontine=" + getTontine() +
            "}";
    }
}
