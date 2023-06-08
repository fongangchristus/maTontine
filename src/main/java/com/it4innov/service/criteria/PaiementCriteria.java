package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.ModePaiement;
import com.it4innov.domain.enumeration.StatutPaiement;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Paiement} entity. This class is used
 * in {@link com.it4innov.web.rest.PaiementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ModePaiement
     */
    public static class ModePaiementFilter extends Filter<ModePaiement> {

        public ModePaiementFilter() {}

        public ModePaiementFilter(ModePaiementFilter filter) {
            super(filter);
        }

        @Override
        public ModePaiementFilter copy() {
            return new ModePaiementFilter(this);
        }
    }

    /**
     * Class for filtering StatutPaiement
     */
    public static class StatutPaiementFilter extends Filter<StatutPaiement> {

        public StatutPaiementFilter() {}

        public StatutPaiementFilter(StatutPaiementFilter filter) {
            super(filter);
        }

        @Override
        public StatutPaiementFilter copy() {
            return new StatutPaiementFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter referencePaiement;

    private StringFilter matriculecmptEmet;

    private StringFilter matriculecmptDest;

    private DoubleFilter montantPaiement;

    private InstantFilter datePaiement;

    private ModePaiementFilter modePaiement;

    private StatutPaiementFilter statutPaiement;

    private Boolean distinct;

    public PaiementCriteria() {}

    public PaiementCriteria(PaiementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.referencePaiement = other.referencePaiement == null ? null : other.referencePaiement.copy();
        this.matriculecmptEmet = other.matriculecmptEmet == null ? null : other.matriculecmptEmet.copy();
        this.matriculecmptDest = other.matriculecmptDest == null ? null : other.matriculecmptDest.copy();
        this.montantPaiement = other.montantPaiement == null ? null : other.montantPaiement.copy();
        this.datePaiement = other.datePaiement == null ? null : other.datePaiement.copy();
        this.modePaiement = other.modePaiement == null ? null : other.modePaiement.copy();
        this.statutPaiement = other.statutPaiement == null ? null : other.statutPaiement.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaiementCriteria copy() {
        return new PaiementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodeAssociation() {
        return codeAssociation;
    }

    public StringFilter codeAssociation() {
        if (codeAssociation == null) {
            codeAssociation = new StringFilter();
        }
        return codeAssociation;
    }

    public void setCodeAssociation(StringFilter codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public StringFilter getReferencePaiement() {
        return referencePaiement;
    }

    public StringFilter referencePaiement() {
        if (referencePaiement == null) {
            referencePaiement = new StringFilter();
        }
        return referencePaiement;
    }

    public void setReferencePaiement(StringFilter referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public StringFilter getMatriculecmptEmet() {
        return matriculecmptEmet;
    }

    public StringFilter matriculecmptEmet() {
        if (matriculecmptEmet == null) {
            matriculecmptEmet = new StringFilter();
        }
        return matriculecmptEmet;
    }

    public void setMatriculecmptEmet(StringFilter matriculecmptEmet) {
        this.matriculecmptEmet = matriculecmptEmet;
    }

    public StringFilter getMatriculecmptDest() {
        return matriculecmptDest;
    }

    public StringFilter matriculecmptDest() {
        if (matriculecmptDest == null) {
            matriculecmptDest = new StringFilter();
        }
        return matriculecmptDest;
    }

    public void setMatriculecmptDest(StringFilter matriculecmptDest) {
        this.matriculecmptDest = matriculecmptDest;
    }

    public DoubleFilter getMontantPaiement() {
        return montantPaiement;
    }

    public DoubleFilter montantPaiement() {
        if (montantPaiement == null) {
            montantPaiement = new DoubleFilter();
        }
        return montantPaiement;
    }

    public void setMontantPaiement(DoubleFilter montantPaiement) {
        this.montantPaiement = montantPaiement;
    }

    public InstantFilter getDatePaiement() {
        return datePaiement;
    }

    public InstantFilter datePaiement() {
        if (datePaiement == null) {
            datePaiement = new InstantFilter();
        }
        return datePaiement;
    }

    public void setDatePaiement(InstantFilter datePaiement) {
        this.datePaiement = datePaiement;
    }

    public ModePaiementFilter getModePaiement() {
        return modePaiement;
    }

    public ModePaiementFilter modePaiement() {
        if (modePaiement == null) {
            modePaiement = new ModePaiementFilter();
        }
        return modePaiement;
    }

    public void setModePaiement(ModePaiementFilter modePaiement) {
        this.modePaiement = modePaiement;
    }

    public StatutPaiementFilter getStatutPaiement() {
        return statutPaiement;
    }

    public StatutPaiementFilter statutPaiement() {
        if (statutPaiement == null) {
            statutPaiement = new StatutPaiementFilter();
        }
        return statutPaiement;
    }

    public void setStatutPaiement(StatutPaiementFilter statutPaiement) {
        this.statutPaiement = statutPaiement;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaiementCriteria that = (PaiementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(referencePaiement, that.referencePaiement) &&
            Objects.equals(matriculecmptEmet, that.matriculecmptEmet) &&
            Objects.equals(matriculecmptDest, that.matriculecmptDest) &&
            Objects.equals(montantPaiement, that.montantPaiement) &&
            Objects.equals(datePaiement, that.datePaiement) &&
            Objects.equals(modePaiement, that.modePaiement) &&
            Objects.equals(statutPaiement, that.statutPaiement) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeAssociation,
            referencePaiement,
            matriculecmptEmet,
            matriculecmptDest,
            montantPaiement,
            datePaiement,
            modePaiement,
            statutPaiement,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (referencePaiement != null ? "referencePaiement=" + referencePaiement + ", " : "") +
            (matriculecmptEmet != null ? "matriculecmptEmet=" + matriculecmptEmet + ", " : "") +
            (matriculecmptDest != null ? "matriculecmptDest=" + matriculecmptDest + ", " : "") +
            (montantPaiement != null ? "montantPaiement=" + montantPaiement + ", " : "") +
            (datePaiement != null ? "datePaiement=" + datePaiement + ", " : "") +
            (modePaiement != null ? "modePaiement=" + modePaiement + ", " : "") +
            (statutPaiement != null ? "statutPaiement=" + statutPaiement + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
