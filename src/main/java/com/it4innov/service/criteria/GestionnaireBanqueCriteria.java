package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.GestionnaireBanque} entity. This class is used
 * in {@link com.it4innov.web.rest.GestionnaireBanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gestionnaire-banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireBanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matriculeMembre;

    private LongFilter banqueId;

    private Boolean distinct;

    public GestionnaireBanqueCriteria() {}

    public GestionnaireBanqueCriteria(GestionnaireBanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matriculeMembre = other.matriculeMembre == null ? null : other.matriculeMembre.copy();
        this.banqueId = other.banqueId == null ? null : other.banqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GestionnaireBanqueCriteria copy() {
        return new GestionnaireBanqueCriteria(this);
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

    public StringFilter getMatriculeMembre() {
        return matriculeMembre;
    }

    public StringFilter matriculeMembre() {
        if (matriculeMembre == null) {
            matriculeMembre = new StringFilter();
        }
        return matriculeMembre;
    }

    public void setMatriculeMembre(StringFilter matriculeMembre) {
        this.matriculeMembre = matriculeMembre;
    }

    public LongFilter getBanqueId() {
        return banqueId;
    }

    public LongFilter banqueId() {
        if (banqueId == null) {
            banqueId = new LongFilter();
        }
        return banqueId;
    }

    public void setBanqueId(LongFilter banqueId) {
        this.banqueId = banqueId;
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
        final GestionnaireBanqueCriteria that = (GestionnaireBanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matriculeMembre, that.matriculeMembre) &&
            Objects.equals(banqueId, that.banqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matriculeMembre, banqueId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireBanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matriculeMembre != null ? "matriculeMembre=" + matriculeMembre + ", " : "") +
            (banqueId != null ? "banqueId=" + banqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
