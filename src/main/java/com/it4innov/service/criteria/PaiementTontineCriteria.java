package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.PaiementTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.PaiementTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiement-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementTontineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referencePaiement;

    private LongFilter cotisationTontineId;

    private LongFilter decaissementTontineId;

    private Boolean distinct;

    public PaiementTontineCriteria() {}

    public PaiementTontineCriteria(PaiementTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referencePaiement = other.referencePaiement == null ? null : other.referencePaiement.copy();
        this.cotisationTontineId = other.cotisationTontineId == null ? null : other.cotisationTontineId.copy();
        this.decaissementTontineId = other.decaissementTontineId == null ? null : other.decaissementTontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaiementTontineCriteria copy() {
        return new PaiementTontineCriteria(this);
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

    public LongFilter getCotisationTontineId() {
        return cotisationTontineId;
    }

    public LongFilter cotisationTontineId() {
        if (cotisationTontineId == null) {
            cotisationTontineId = new LongFilter();
        }
        return cotisationTontineId;
    }

    public void setCotisationTontineId(LongFilter cotisationTontineId) {
        this.cotisationTontineId = cotisationTontineId;
    }

    public LongFilter getDecaissementTontineId() {
        return decaissementTontineId;
    }

    public LongFilter decaissementTontineId() {
        if (decaissementTontineId == null) {
            decaissementTontineId = new LongFilter();
        }
        return decaissementTontineId;
    }

    public void setDecaissementTontineId(LongFilter decaissementTontineId) {
        this.decaissementTontineId = decaissementTontineId;
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
        final PaiementTontineCriteria that = (PaiementTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referencePaiement, that.referencePaiement) &&
            Objects.equals(cotisationTontineId, that.cotisationTontineId) &&
            Objects.equals(decaissementTontineId, that.decaissementTontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referencePaiement, cotisationTontineId, decaissementTontineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (referencePaiement != null ? "referencePaiement=" + referencePaiement + ", " : "") +
            (cotisationTontineId != null ? "cotisationTontineId=" + cotisationTontineId + ", " : "") +
            (decaissementTontineId != null ? "decaissementTontineId=" + decaissementTontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
