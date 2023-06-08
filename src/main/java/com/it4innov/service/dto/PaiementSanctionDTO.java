package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.PaiementSanction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementSanctionDTO implements Serializable {

    private Long id;

    @NotNull
    private String referencePaiement;

    private SanctionDTO sanction;

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

    public SanctionDTO getSanction() {
        return sanction;
    }

    public void setSanction(SanctionDTO sanction) {
        this.sanction = sanction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementSanctionDTO)) {
            return false;
        }

        PaiementSanctionDTO paiementSanctionDTO = (PaiementSanctionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementSanctionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementSanctionDTO{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            ", sanction=" + getSanction() +
            "}";
    }
}
