package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.TypeSanction;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.SanctionConfiguration} entity. This class is used
 * in {@link com.it4innov.web.rest.SanctionConfigurationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sanction-configurations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionConfigurationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeSanction
     */
    public static class TypeSanctionFilter extends Filter<TypeSanction> {

        public TypeSanctionFilter() {}

        public TypeSanctionFilter(TypeSanctionFilter filter) {
            super(filter);
        }

        @Override
        public TypeSanctionFilter copy() {
            return new TypeSanctionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter codeTontine;

    private TypeSanctionFilter type;

    private LongFilter sanctionId;

    private Boolean distinct;

    public SanctionConfigurationCriteria() {}

    public SanctionConfigurationCriteria(SanctionConfigurationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.codeTontine = other.codeTontine == null ? null : other.codeTontine.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.sanctionId = other.sanctionId == null ? null : other.sanctionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SanctionConfigurationCriteria copy() {
        return new SanctionConfigurationCriteria(this);
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

    public StringFilter getCodeTontine() {
        return codeTontine;
    }

    public StringFilter codeTontine() {
        if (codeTontine == null) {
            codeTontine = new StringFilter();
        }
        return codeTontine;
    }

    public void setCodeTontine(StringFilter codeTontine) {
        this.codeTontine = codeTontine;
    }

    public TypeSanctionFilter getType() {
        return type;
    }

    public TypeSanctionFilter type() {
        if (type == null) {
            type = new TypeSanctionFilter();
        }
        return type;
    }

    public void setType(TypeSanctionFilter type) {
        this.type = type;
    }

    public LongFilter getSanctionId() {
        return sanctionId;
    }

    public LongFilter sanctionId() {
        if (sanctionId == null) {
            sanctionId = new LongFilter();
        }
        return sanctionId;
    }

    public void setSanctionId(LongFilter sanctionId) {
        this.sanctionId = sanctionId;
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
        final SanctionConfigurationCriteria that = (SanctionConfigurationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(codeTontine, that.codeTontine) &&
            Objects.equals(type, that.type) &&
            Objects.equals(sanctionId, that.sanctionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeAssociation, codeTontine, type, sanctionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionConfigurationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (codeTontine != null ? "codeTontine=" + codeTontine + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (sanctionId != null ? "sanctionId=" + sanctionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
