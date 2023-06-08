package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.PaiementAdhesion} entity. This class is used
 * in {@link com.it4innov.web.rest.PaiementAdhesionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiement-adhesions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementAdhesionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referencePaiement;

    private LongFilter adhesionId;

    private Boolean distinct;

    public PaiementAdhesionCriteria() {}

    public PaiementAdhesionCriteria(PaiementAdhesionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referencePaiement = other.referencePaiement == null ? null : other.referencePaiement.copy();
        this.adhesionId = other.adhesionId == null ? null : other.adhesionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaiementAdhesionCriteria copy() {
        return new PaiementAdhesionCriteria(this);
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

    public LongFilter getAdhesionId() {
        return adhesionId;
    }

    public LongFilter adhesionId() {
        if (adhesionId == null) {
            adhesionId = new LongFilter();
        }
        return adhesionId;
    }

    public void setAdhesionId(LongFilter adhesionId) {
        this.adhesionId = adhesionId;
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
        final PaiementAdhesionCriteria that = (PaiementAdhesionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referencePaiement, that.referencePaiement) &&
            Objects.equals(adhesionId, that.adhesionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referencePaiement, adhesionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementAdhesionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (referencePaiement != null ? "referencePaiement=" + referencePaiement + ", " : "") +
            (adhesionId != null ? "adhesionId=" + adhesionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
