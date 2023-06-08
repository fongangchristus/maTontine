package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.PaiementAdhesion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementAdhesionDTO implements Serializable {

    private Long id;

    @NotNull
    private String referencePaiement;

    private AdhesionDTO adhesion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
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
        if (!(o instanceof PaiementAdhesionDTO)) {
            return false;
        }

        PaiementAdhesionDTO paiementAdhesionDTO = (PaiementAdhesionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementAdhesionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementAdhesionDTO{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            ", adhesion=" + getAdhesion() +
            "}";
    }
}
