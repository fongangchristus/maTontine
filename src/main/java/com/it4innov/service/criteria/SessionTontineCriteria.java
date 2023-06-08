package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.SessionTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.SessionTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /session-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTontineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private LongFilter cotisationTontineId;

    private LongFilter decaissementTontineId;

    private LongFilter tontineId;

    private Boolean distinct;

    public SessionTontineCriteria() {}

    public SessionTontineCriteria(SessionTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.cotisationTontineId = other.cotisationTontineId == null ? null : other.cotisationTontineId.copy();
        this.decaissementTontineId = other.decaissementTontineId == null ? null : other.decaissementTontineId.copy();
        this.tontineId = other.tontineId == null ? null : other.tontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SessionTontineCriteria copy() {
        return new SessionTontineCriteria(this);
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
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

    public LongFilter getTontineId() {
        return tontineId;
    }

    public LongFilter tontineId() {
        if (tontineId == null) {
            tontineId = new LongFilter();
        }
        return tontineId;
    }

    public void setTontineId(LongFilter tontineId) {
        this.tontineId = tontineId;
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
        final SessionTontineCriteria that = (SessionTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(cotisationTontineId, that.cotisationTontineId) &&
            Objects.equals(decaissementTontineId, that.decaissementTontineId) &&
            Objects.equals(tontineId, that.tontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, dateDebut, dateFin, cotisationTontineId, decaissementTontineId, tontineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
            (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
            (cotisationTontineId != null ? "cotisationTontineId=" + cotisationTontineId + ", " : "") +
            (decaissementTontineId != null ? "decaissementTontineId=" + decaissementTontineId + ", " : "") +
            (tontineId != null ? "tontineId=" + tontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
