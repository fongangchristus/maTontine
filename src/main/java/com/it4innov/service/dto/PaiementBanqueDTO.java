package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.PaiementBanque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementBanqueDTO implements Serializable {

    private Long id;

    @NotNull
    private String referencePaiement;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementBanqueDTO)) {
            return false;
        }

        PaiementBanqueDTO paiementBanqueDTO = (PaiementBanqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementBanqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementBanqueDTO{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            "}";
    }
}
