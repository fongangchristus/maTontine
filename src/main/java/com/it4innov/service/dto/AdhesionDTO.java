package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.StatutAdhesion;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.Adhesion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdhesionDTO implements Serializable {

    private Long id;

    private StatutAdhesion statutAdhesion;

    private String matriculePersonne;

    private Instant dateDebutAdhesion;

    private Instant dateFinAdhesion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutAdhesion getStatutAdhesion() {
        return statutAdhesion;
    }

    public void setStatutAdhesion(StatutAdhesion statutAdhesion) {
        this.statutAdhesion = statutAdhesion;
    }

    public String getMatriculePersonne() {
        return matriculePersonne;
    }

    public void setMatriculePersonne(String matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public Instant getDateDebutAdhesion() {
        return dateDebutAdhesion;
    }

    public void setDateDebutAdhesion(Instant dateDebutAdhesion) {
        this.dateDebutAdhesion = dateDebutAdhesion;
    }

    public Instant getDateFinAdhesion() {
        return dateFinAdhesion;
    }

    public void setDateFinAdhesion(Instant dateFinAdhesion) {
        this.dateFinAdhesion = dateFinAdhesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdhesionDTO)) {
            return false;
        }

        AdhesionDTO adhesionDTO = (AdhesionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, adhesionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdhesionDTO{" +
            "id=" + getId() +
            ", statutAdhesion='" + getStatutAdhesion() + "'" +
            ", matriculePersonne='" + getMatriculePersonne() + "'" +
            ", dateDebutAdhesion='" + getDateDebutAdhesion() + "'" +
            ", dateFinAdhesion='" + getDateFinAdhesion() + "'" +
            "}";
    }
}
