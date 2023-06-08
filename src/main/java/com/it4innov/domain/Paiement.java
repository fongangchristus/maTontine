package com.it4innov.domain;

import com.it4innov.domain.enumeration.ModePaiement;
import com.it4innov.domain.enumeration.StatutPaiement;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @Column(name = "reference_paiement")
    private String referencePaiement;

    @NotNull
    @Column(name = "matriculecmpt_emet", nullable = false)
    private String matriculecmptEmet;

    @NotNull
    @Column(name = "matriculecmpt_dest", nullable = false)
    private String matriculecmptDest;

    @NotNull
    @Column(name = "montant_paiement", nullable = false)
    private Double montantPaiement;

    @Column(name = "date_paiement")
    private Instant datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement")
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_paiement")
    private StatutPaiement statutPaiement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Paiement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public Paiement codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getReferencePaiement() {
        return this.referencePaiement;
    }

    public Paiement referencePaiement(String referencePaiement) {
        this.setReferencePaiement(referencePaiement);
        return this;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public String getMatriculecmptEmet() {
        return this.matriculecmptEmet;
    }

    public Paiement matriculecmptEmet(String matriculecmptEmet) {
        this.setMatriculecmptEmet(matriculecmptEmet);
        return this;
    }

    public void setMatriculecmptEmet(String matriculecmptEmet) {
        this.matriculecmptEmet = matriculecmptEmet;
    }

    public String getMatriculecmptDest() {
        return this.matriculecmptDest;
    }

    public Paiement matriculecmptDest(String matriculecmptDest) {
        this.setMatriculecmptDest(matriculecmptDest);
        return this;
    }

    public void setMatriculecmptDest(String matriculecmptDest) {
        this.matriculecmptDest = matriculecmptDest;
    }

    public Double getMontantPaiement() {
        return this.montantPaiement;
    }

    public Paiement montantPaiement(Double montantPaiement) {
        this.setMontantPaiement(montantPaiement);
        return this;
    }

    public void setMontantPaiement(Double montantPaiement) {
        this.montantPaiement = montantPaiement;
    }

    public Instant getDatePaiement() {
        return this.datePaiement;
    }

    public Paiement datePaiement(Instant datePaiement) {
        this.setDatePaiement(datePaiement);
        return this;
    }

    public void setDatePaiement(Instant datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiement getModePaiement() {
        return this.modePaiement;
    }

    public Paiement modePaiement(ModePaiement modePaiement) {
        this.setModePaiement(modePaiement);
        return this;
    }

    public void setModePaiement(ModePaiement modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutPaiement getStatutPaiement() {
        return this.statutPaiement;
    }

    public Paiement statutPaiement(StatutPaiement statutPaiement) {
        this.setStatutPaiement(statutPaiement);
        return this;
    }

    public void setStatutPaiement(StatutPaiement statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Paiement)) {
            return false;
        }
        return id != null && id.equals(((Paiement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Paiement{" +
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
