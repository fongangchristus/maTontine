package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.FormuleAdhesion} entity. This class is used
 * in {@link com.it4innov.web.rest.FormuleAdhesionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /formule-adhesions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormuleAdhesionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter adhesionPeriodique;

    private InstantFilter dateDebut;

    private IntegerFilter dureeAdhesionMois;

    private BooleanFilter montantLibre;

    private StringFilter description;

    private DoubleFilter tarif;

    private LongFilter adhesionId;

    private Boolean distinct;

    public FormuleAdhesionCriteria() {}

    public FormuleAdhesionCriteria(FormuleAdhesionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.adhesionPeriodique = other.adhesionPeriodique == null ? null : other.adhesionPeriodique.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dureeAdhesionMois = other.dureeAdhesionMois == null ? null : other.dureeAdhesionMois.copy();
        this.montantLibre = other.montantLibre == null ? null : other.montantLibre.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.tarif = other.tarif == null ? null : other.tarif.copy();
        this.adhesionId = other.adhesionId == null ? null : other.adhesionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FormuleAdhesionCriteria copy() {
        return new FormuleAdhesionCriteria(this);
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

    public BooleanFilter getAdhesionPeriodique() {
        return adhesionPeriodique;
    }

    public BooleanFilter adhesionPeriodique() {
        if (adhesionPeriodique == null) {
            adhesionPeriodique = new BooleanFilter();
        }
        return adhesionPeriodique;
    }

    public void setAdhesionPeriodique(BooleanFilter adhesionPeriodique) {
        this.adhesionPeriodique = adhesionPeriodique;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public InstantFilter dateDebut() {
        if (dateDebut == null) {
            dateDebut = new InstantFilter();
        }
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public IntegerFilter getDureeAdhesionMois() {
        return dureeAdhesionMois;
    }

    public IntegerFilter dureeAdhesionMois() {
        if (dureeAdhesionMois == null) {
            dureeAdhesionMois = new IntegerFilter();
        }
        return dureeAdhesionMois;
    }

    public void setDureeAdhesionMois(IntegerFilter dureeAdhesionMois) {
        this.dureeAdhesionMois = dureeAdhesionMois;
    }

    public BooleanFilter getMontantLibre() {
        return montantLibre;
    }

    public BooleanFilter montantLibre() {
        if (montantLibre == null) {
            montantLibre = new BooleanFilter();
        }
        return montantLibre;
    }

    public void setMontantLibre(BooleanFilter montantLibre) {
        this.montantLibre = montantLibre;
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

    public DoubleFilter getTarif() {
        return tarif;
    }

    public DoubleFilter tarif() {
        if (tarif == null) {
            tarif = new DoubleFilter();
        }
        return tarif;
    }

    public void setTarif(DoubleFilter tarif) {
        this.tarif = tarif;
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
        final FormuleAdhesionCriteria that = (FormuleAdhesionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(adhesionPeriodique, that.adhesionPeriodique) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dureeAdhesionMois, that.dureeAdhesionMois) &&
            Objects.equals(montantLibre, that.montantLibre) &&
            Objects.equals(description, that.description) &&
            Objects.equals(tarif, that.tarif) &&
            Objects.equals(adhesionId, that.adhesionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, adhesionPeriodique, dateDebut, dureeAdhesionMois, montantLibre, description, tarif, adhesionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormuleAdhesionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (adhesionPeriodique != null ? "adhesionPeriodique=" + adhesionPeriodique + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dureeAdhesionMois != null ? "dureeAdhesionMois=" + dureeAdhesionMois + ", " : "") +
            (montantLibre != null ? "montantLibre=" + montantLibre + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (tarif != null ? "tarif=" + tarif + ", " : "") +
            (adhesionId != null ? "adhesionId=" + adhesionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
