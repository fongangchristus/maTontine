package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Evenement} entity. This class is used
 * in {@link com.it4innov.web.rest.EvenementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evenements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EvenementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter codepot;

    private StringFilter montantPayer;

    private StringFilter description;

    private DoubleFilter budget;

    private InstantFilter dateEvenement;

    private LongFilter typeEvenementId;

    private Boolean distinct;

    public EvenementCriteria() {}

    public EvenementCriteria(EvenementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.codepot = other.codepot == null ? null : other.codepot.copy();
        this.montantPayer = other.montantPayer == null ? null : other.montantPayer.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.budget = other.budget == null ? null : other.budget.copy();
        this.dateEvenement = other.dateEvenement == null ? null : other.dateEvenement.copy();
        this.typeEvenementId = other.typeEvenementId == null ? null : other.typeEvenementId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EvenementCriteria copy() {
        return new EvenementCriteria(this);
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

    public StringFilter getCodepot() {
        return codepot;
    }

    public StringFilter codepot() {
        if (codepot == null) {
            codepot = new StringFilter();
        }
        return codepot;
    }

    public void setCodepot(StringFilter codepot) {
        this.codepot = codepot;
    }

    public StringFilter getMontantPayer() {
        return montantPayer;
    }

    public StringFilter montantPayer() {
        if (montantPayer == null) {
            montantPayer = new StringFilter();
        }
        return montantPayer;
    }

    public void setMontantPayer(StringFilter montantPayer) {
        this.montantPayer = montantPayer;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getBudget() {
        return budget;
    }

    public DoubleFilter budget() {
        if (budget == null) {
            budget = new DoubleFilter();
        }
        return budget;
    }

    public void setBudget(DoubleFilter budget) {
        this.budget = budget;
    }

    public InstantFilter getDateEvenement() {
        return dateEvenement;
    }

    public InstantFilter dateEvenement() {
        if (dateEvenement == null) {
            dateEvenement = new InstantFilter();
        }
        return dateEvenement;
    }

    public void setDateEvenement(InstantFilter dateEvenement) {
        this.dateEvenement = dateEvenement;
    }

    public LongFilter getTypeEvenementId() {
        return typeEvenementId;
    }

    public LongFilter typeEvenementId() {
        if (typeEvenementId == null) {
            typeEvenementId = new LongFilter();
        }
        return typeEvenementId;
    }

    public void setTypeEvenementId(LongFilter typeEvenementId) {
        this.typeEvenementId = typeEvenementId;
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
        final EvenementCriteria that = (EvenementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(codepot, that.codepot) &&
            Objects.equals(montantPayer, that.montantPayer) &&
            Objects.equals(description, that.description) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(dateEvenement, that.dateEvenement) &&
            Objects.equals(typeEvenementId, that.typeEvenementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libele, codepot, montantPayer, description, budget, dateEvenement, typeEvenementId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EvenementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (codepot != null ? "codepot=" + codepot + ", " : "") +
            (montantPayer != null ? "montantPayer=" + montantPayer + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (budget != null ? "budget=" + budget + ", " : "") +
            (dateEvenement != null ? "dateEvenement=" + dateEvenement + ", " : "") +
            (typeEvenementId != null ? "typeEvenementId=" + typeEvenementId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
