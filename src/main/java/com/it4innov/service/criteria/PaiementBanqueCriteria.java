package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.PaiementBanque} entity. This class is used
 * in {@link com.it4innov.web.rest.PaiementBanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /paiement-banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementBanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter referencePaiement;

    private Boolean distinct;

    public PaiementBanqueCriteria() {}

    public PaiementBanqueCriteria(PaiementBanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.referencePaiement = other.referencePaiement == null ? null : other.referencePaiement.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaiementBanqueCriteria copy() {
        return new PaiementBanqueCriteria(this);
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
        final PaiementBanqueCriteria that = (PaiementBanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(referencePaiement, that.referencePaiement) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, referencePaiement, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementBanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (referencePaiement != null ? "referencePaiement=" + referencePaiement + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
