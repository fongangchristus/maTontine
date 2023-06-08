package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.StatutExercice;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Exercise} entity. This class is used
 * in {@link com.it4innov.web.rest.ExerciseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /exercises?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExerciseCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutExercice
     */
    public static class StatutExerciceFilter extends Filter<StatutExercice> {

        public StatutExerciceFilter() {}

        public StatutExerciceFilter(StatutExerciceFilter filter) {
            super(filter);
        }

        @Override
        public StatutExerciceFilter copy() {
            return new StatutExerciceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter observation;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StatutExerciceFilter statut;

    private LongFilter associationId;

    private Boolean distinct;

    public ExerciseCriteria() {}

    public ExerciseCriteria(ExerciseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.observation = other.observation == null ? null : other.observation.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.associationId = other.associationId == null ? null : other.associationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ExerciseCriteria copy() {
        return new ExerciseCriteria(this);
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

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public LocalDateFilter dateDebut() {
        if (dateDebut == null) {
            dateDebut = new LocalDateFilter();
        }
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public LocalDateFilter dateFin() {
        if (dateFin == null) {
            dateFin = new LocalDateFilter();
        }
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
    }

    public StatutExerciceFilter getStatut() {
        return statut;
    }

    public StatutExerciceFilter statut() {
        if (statut == null) {
            statut = new StatutExerciceFilter();
        }
        return statut;
    }

    public void setStatut(StatutExerciceFilter statut) {
        this.statut = statut;
    }

    public LongFilter getAssociationId() {
        return associationId;
    }

    public LongFilter associationId() {
        if (associationId == null) {
            associationId = new LongFilter();
        }
        return associationId;
    }

    public void setAssociationId(LongFilter associationId) {
        this.associationId = associationId;
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
        final ExerciseCriteria that = (ExerciseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(associationId, that.associationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libele, observation, dateDebut, dateFin, statut, associationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExerciseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (observation != null ? "observation=" + observation + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
            (statut != null ? "statut=" + statut + ", " : "") +
            (associationId != null ? "associationId=" + associationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
