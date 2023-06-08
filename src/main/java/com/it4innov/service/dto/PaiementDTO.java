package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.ModePaiement;
import com.it4innov.domain.enumeration.StatutPaiement;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Paiement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeAssociation;

    private String referencePaiement;

    @NotNull
    private String matriculecmptEmet;

    @NotNull
    private String matriculecmptDest;

    @NotNull
    private Double montantPaiement;

    private Instant datePaiement;

    private ModePaiement modePaiement;

    private StatutPaiement statutPaiement;

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

    public String getReferencePaiement() {
        return referencePaiement;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public String getMatriculecmptEmet() {
        return matriculecmptEmet;
    }

    public void setMatriculecmptEmet(String matriculecmptEmet) {
        this.matriculecmptEmet = matriculecmptEmet;
    }

    public String getMatriculecmptDest() {
        return matriculecmptDest;
    }

    public void setMatriculecmptDest(String matriculecmptDest) {
        this.matriculecmptDest = matriculecmptDest;
    }

    public Double getMontantPaiement() {
        return montantPaiement;
    }

    public void setMontantPaiement(Double montantPaiement) {
        this.montantPaiement = montantPaiement;
    }

    public Instant getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutPaiement getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementDTO)) {
            return false;
        }

        PaiementDTO paiementDTO = (PaiementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paiementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementDTO{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            ", matriculecmptEmet='" + getMatriculecmptEmet() + "'" +
            ", matriculecmptDest='" + getMatriculecmptDest() + "'" +
            ", montantPaiement=" + getMontantPaiement() +
            ", datePaiement='" + getDatePaiement() + "'" +
            ", modePaiement='" + getModePaiement() + "'" +
            ", statutPaiement='" + getStatutPaiement() + "'" +
            "}";
    }
}
