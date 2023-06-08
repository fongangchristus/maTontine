package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.TypeEvenement} entity. This class is used
 * in {@link com.it4innov.web.rest.TypeEvenementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /type-evenements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeEvenementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter observation;

    private LongFilter evenementId;

    private Boolean distinct;

    public TypeEvenementCriteria() {}

    public TypeEvenementCriteria(TypeEvenementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.evenementId = other.evenementId == null ? null : other.evenementId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TypeEvenementCriteria copy() {
        return new TypeEvenementCriteria(this);
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

    public StringFilter getLibele() {
        return libele;
    }

    public StringFilter libele() {
        if (libele == null) {
            libele = new StringFilter();
        }
        return libele;
    }

    public void setLibele(StringFilter libele) {
        this.libele = libele;
    }

    public StringFilter getObservation() {
        return observation;
    }

    public StringFilter observation() {
        if (observation == null) {
            observation = new StringFilter();
        }
        return observation;
    }

    public void setObservation(StringFilter observation) {
        this.observation = observation;
    }

    public LongFilter getEvenementId() {
        return evenementId;
    }

    public LongFilter evenementId() {
        if (evenementId == null) {
            evenementId = new LongFilter();
        }
        return evenementId;
    }

    public void setEvenementId(LongFilter evenementId) {
        this.evenementId = evenementId;
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
        final TypeEvenementCriteria that = (TypeEvenementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(evenementId, that.evenementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libele, observation, evenementId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeEvenementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (observation != null ? "observation=" + observation + ", " : "") +
            (evenementId != null ? "evenementId=" + evenementId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
