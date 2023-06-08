package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.GestionnaireTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.GestionnaireTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gestionnaire-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireTontineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matriculeAdherent;

    private StringFilter codeTontine;

    private LocalDateFilter datePriseFonction;

    private LocalDateFilter dateFinFonction;

    private LongFilter tontineId;

    private Boolean distinct;

    public GestionnaireTontineCriteria() {}

    public GestionnaireTontineCriteria(GestionnaireTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matriculeAdherent = other.matriculeAdherent == null ? null : other.matriculeAdherent.copy();
        this.codeTontine = other.codeTontine == null ? null : other.codeTontine.copy();
        this.datePriseFonction = other.datePriseFonction == null ? null : other.datePriseFonction.copy();
        this.dateFinFonction = other.dateFinFonction == null ? null : other.dateFinFonction.copy();
        this.tontineId = other.tontineId == null ? null : other.tontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GestionnaireTontineCriteria copy() {
        return new GestionnaireTontineCriteria(this);
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

    public StringFilter getMatriculeAdherent() {
        return matriculeAdherent;
    }

    public StringFilter matriculeAdherent() {
        if (matriculeAdherent == null) {
            matriculeAdherent = new StringFilter();
        }
        return matriculeAdherent;
    }

    public void setMatriculeAdherent(StringFilter matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
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

    public LocalDateFilter getDatePriseFonction() {
        return datePriseFonction;
    }

    public LocalDateFilter datePriseFonction() {
        if (datePriseFonction == null) {
            datePriseFonction = new LocalDateFilter();
        }
        return datePriseFonction;
    }

    public void setDatePriseFonction(LocalDateFilter datePriseFonction) {
        this.datePriseFonction = datePriseFonction;
    }

    public LocalDateFilter getDateFinFonction() {
        return dateFinFonction;
    }

    public LocalDateFilter dateFinFonction() {
        if (dateFinFonction == null) {
            dateFinFonction = new LocalDateFilter();
        }
        return dateFinFonction;
    }

    public void setDateFinFonction(LocalDateFilter dateFinFonction) {
        this.dateFinFonction = dateFinFonction;
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
        final GestionnaireTontineCriteria that = (GestionnaireTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matriculeAdherent, that.matriculeAdherent) &&
            Objects.equals(codeTontine, that.codeTontine) &&
            Objects.equals(datePriseFonction, that.datePriseFonction) &&
            Objects.equals(dateFinFonction, that.dateFinFonction) &&
            Objects.equals(tontineId, that.tontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matriculeAdherent, codeTontine, datePriseFonction, dateFinFonction, tontineId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matriculeAdherent != null ? "matriculeAdherent=" + matriculeAdherent + ", " : "") +
            (codeTontine != null ? "codeTontine=" + codeTontine + ", " : "") +
            (datePriseFonction != null ? "datePriseFonction=" + datePriseFonction + ", " : "") +
            (dateFinFonction != null ? "dateFinFonction=" + dateFinFonction + ", " : "") +
            (tontineId != null ? "tontineId=" + tontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
