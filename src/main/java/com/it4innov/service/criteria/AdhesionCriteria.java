package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.StatutAdhesion;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Adhesion} entity. This class is used
 * in {@link com.it4innov.web.rest.AdhesionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /adhesions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdhesionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutAdhesion
     */
    public static class StatutAdhesionFilter extends Filter<StatutAdhesion> {

        public StatutAdhesionFilter() {}

        public StatutAdhesionFilter(StatutAdhesionFilter filter) {
            super(filter);
        }

        @Override
        public StatutAdhesionFilter copy() {
            return new StatutAdhesionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatutAdhesionFilter statutAdhesion;

    private StringFilter matriculePersonne;

    private InstantFilter dateDebutAdhesion;

    private InstantFilter dateFinAdhesion;

    private LongFilter formuleAdhesionId;

    private LongFilter paiementAdhesionId;

    private Boolean distinct;

    public AdhesionCriteria() {}

    public AdhesionCriteria(AdhesionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.statutAdhesion = other.statutAdhesion == null ? null : other.statutAdhesion.copy();
        this.matriculePersonne = other.matriculePersonne == null ? null : other.matriculePersonne.copy();
        this.dateDebutAdhesion = other.dateDebutAdhesion == null ? null : other.dateDebutAdhesion.copy();
        this.dateFinAdhesion = other.dateFinAdhesion == null ? null : other.dateFinAdhesion.copy();
        this.formuleAdhesionId = other.formuleAdhesionId == null ? null : other.formuleAdhesionId.copy();
        this.paiementAdhesionId = other.paiementAdhesionId == null ? null : other.paiementAdhesionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AdhesionCriteria copy() {
        return new AdhesionCriteria(this);
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

    public StatutAdhesionFilter getStatutAdhesion() {
        return statutAdhesion;
    }

    public StatutAdhesionFilter statutAdhesion() {
        if (statutAdhesion == null) {
            statutAdhesion = new StatutAdhesionFilter();
        }
        return statutAdhesion;
    }

    public void setStatutAdhesion(StatutAdhesionFilter statutAdhesion) {
        this.statutAdhesion = statutAdhesion;
    }

    public StringFilter getMatriculePersonne() {
        return matriculePersonne;
    }

    public StringFilter matriculePersonne() {
        if (matriculePersonne == null) {
            matriculePersonne = new StringFilter();
        }
        return matriculePersonne;
    }

    public void setMatriculePersonne(StringFilter matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public InstantFilter getDateDebutAdhesion() {
        return dateDebutAdhesion;
    }

    public InstantFilter dateDebutAdhesion() {
        if (dateDebutAdhesion == null) {
            dateDebutAdhesion = new InstantFilter();
        }
        return dateDebutAdhesion;
    }

    public void setDateDebutAdhesion(InstantFilter dateDebutAdhesion) {
        this.dateDebutAdhesion = dateDebutAdhesion;
    }

    public InstantFilter getDateFinAdhesion() {
        return dateFinAdhesion;
    }

    public InstantFilter dateFinAdhesion() {
        if (dateFinAdhesion == null) {
            dateFinAdhesion = new InstantFilter();
        }
        return dateFinAdhesion;
    }

    public void setDateFinAdhesion(InstantFilter dateFinAdhesion) {
        this.dateFinAdhesion = dateFinAdhesion;
    }

    public LongFilter getFormuleAdhesionId() {
        return formuleAdhesionId;
    }

    public LongFilter formuleAdhesionId() {
        if (formuleAdhesionId == null) {
            formuleAdhesionId = new LongFilter();
        }
        return formuleAdhesionId;
    }

    public void setFormuleAdhesionId(LongFilter formuleAdhesionId) {
        this.formuleAdhesionId = formuleAdhesionId;
    }

    public LongFilter getPaiementAdhesionId() {
        return paiementAdhesionId;
    }

    public LongFilter paiementAdhesionId() {
        if (paiementAdhesionId == null) {
            paiementAdhesionId = new LongFilter();
        }
        return paiementAdhesionId;
    }

    public void setPaiementAdhesionId(LongFilter paiementAdhesionId) {
        this.paiementAdhesionId = paiementAdhesionId;
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
        final AdhesionCriteria that = (AdhesionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(statutAdhesion, that.statutAdhesion) &&
            Objects.equals(matriculePersonne, that.matriculePersonne) &&
            Objects.equals(dateDebutAdhesion, that.dateDebutAdhesion) &&
            Objects.equals(dateFinAdhesion, that.dateFinAdhesion) &&
            Objects.equals(formuleAdhesionId, that.formuleAdhesionId) &&
            Objects.equals(paiementAdhesionId, that.paiementAdhesionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            statutAdhesion,
            matriculePersonne,
            dateDebutAdhesion,
            dateFinAdhesion,
            formuleAdhesionId,
            paiementAdhesionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdhesionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (statutAdhesion != null ? "statutAdhesion=" + statutAdhesion + ", " : "") +
            (matriculePersonne != null ? "matriculePersonne=" + matriculePersonne + ", " : "") +
            (dateDebutAdhesion != null ? "dateDebutAdhesion=" + dateDebutAdhesion + ", " : "") +
            (dateFinAdhesion != null ? "dateFinAdhesion=" + dateFinAdhesion + ", " : "") +
            (formuleAdhesionId != null ? "formuleAdhesionId=" + formuleAdhesionId + ", " : "") +
            (paiementAdhesionId != null ? "paiementAdhesionId=" + paiementAdhesionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
