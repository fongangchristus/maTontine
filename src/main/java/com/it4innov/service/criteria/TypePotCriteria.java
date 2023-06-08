package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.TypePot} entity. This class is used
 * in {@link com.it4innov.web.rest.TypePotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /type-pots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypePotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter descrption;

    private LongFilter potId;

    private Boolean distinct;

    public TypePotCriteria() {}

    public TypePotCriteria(TypePotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.descrption = other.descrption == null ? null : other.descrption.copy();
        this.potId = other.potId == null ? null : other.potId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TypePotCriteria copy() {
        return new TypePotCriteria(this);
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

    public StringFilter getDescrption() {
        return descrption;
    }

    public StringFilter descrption() {
        if (descrption == null) {
            descrption = new StringFilter();
        }
        return descrption;
    }

    public void setDescrption(StringFilter descrption) {
        this.descrption = descrption;
    }

    public LongFilter getPotId() {
        return potId;
    }

    public LongFilter potId() {
        if (potId == null) {
            potId = new LongFilter();
        }
        return potId;
    }

    public void setPotId(LongFilter potId) {
        this.potId = potId;
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
        final TypePotCriteria that = (TypePotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(descrption, that.descrption) &&
            Objects.equals(potId, that.potId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libele, descrption, potId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (descrption != null ? "descrption=" + descrption + ", " : "") +
            (potId != null ? "potId=" + potId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
