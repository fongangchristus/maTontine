package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.PaiementTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementTontineDTO implements Serializable {

    private Long id;

    private String referencePaiement;

    private CotisationTontineDTO cotisationTontine;

    private DecaissementTontineDTO decaissementTontine;

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

    public CotisationTontineDTO getCotisationTontine() {
        return cotisationTontine;
    }

    public void setCotisationTontine(CotisationTontineDTO cotisationTontine) {
        this.cotisationTontine = cotisationTontine;
    }

    public DecaissementTontineDTO getDecaissementTontine() {
        return decaissementTontine;
    }

    public void setDecaissementTontine(DecaissementTontineDTO decaissementTontine) {
        this.decaissementTontine = decaissementTontine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementTontineDTO)) {
            return false;
        }

        PaiementTontineDTO paiementTontineDTO = (PaiementTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementTontineDTO{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            ", cotisationTontine=" + getCotisationTontine() +
            ", decaissementTontine=" + getDecaissementTontine() +
            "}";
    }
}
