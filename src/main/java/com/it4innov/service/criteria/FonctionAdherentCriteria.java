package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.FonctionAdherent} entity. This class is used
 * in {@link com.it4innov.web.rest.FonctionAdherentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fonction-adherents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FonctionAdherentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matriculeAdherent;

    private StringFilter codeFonction;

    private LocalDateFilter datePriseFonction;

    private LocalDateFilter dateFinFonction;

    private BooleanFilter actif;

    private LongFilter adherentId;

    private LongFilter fonctionId;

    private Boolean distinct;

    public FonctionAdherentCriteria() {}

    public FonctionAdherentCriteria(FonctionAdherentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matriculeAdherent = other.matriculeAdherent == null ? null : other.matriculeAdherent.copy();
        this.codeFonction = other.codeFonction == null ? null : other.codeFonction.copy();
        this.datePriseFonction = other.datePriseFonction == null ? null : other.datePriseFonction.copy();
        this.dateFinFonction = other.dateFinFonction == null ? null : other.dateFinFonction.copy();
        this.actif = other.actif == null ? null : other.actif.copy();
        this.adherentId = other.adherentId == null ? null : other.adherentId.copy();
        this.fonctionId = other.fonctionId == null ? null : other.fonctionId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FonctionAdherentCriteria copy() {
        return new FonctionAdherentCriteria(this);
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

    public StringFilter getCodeFonction() {
        return codeFonction;
    }

    public StringFilter codeFonction() {
        if (codeFonction == null) {
            codeFonction = new StringFilter();
        }
        return codeFonction;
    }

    public void setCodeFonction(StringFilter codeFonction) {
        this.codeFonction = codeFonction;
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

    public BooleanFilter getActif() {
        return actif;
    }

    public BooleanFilter actif() {
        if (actif == null) {
            actif = new BooleanFilter();
        }
        return actif;
    }

    public void setActif(BooleanFilter actif) {
        this.actif = actif;
    }

    public LongFilter getAdherentId() {
        return adherentId;
    }

    public LongFilter adherentId() {
        if (adherentId == null) {
            adherentId = new LongFilter();
        }
        return adherentId;
    }

    public void setAdherentId(LongFilter adherentId) {
        this.adherentId = adherentId;
    }

    public LongFilter getFonctionId() {
        return fonctionId;
    }

    public LongFilter fonctionId() {
        if (fonctionId == null) {
            fonctionId = new LongFilter();
        }
        return fonctionId;
    }

    public void setFonctionId(LongFilter fonctionId) {
        this.fonctionId = fonctionId;
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
        final FonctionAdherentCriteria that = (FonctionAdherentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matriculeAdherent, that.matriculeAdherent) &&
            Objects.equals(codeFonction, that.codeFonction) &&
            Objects.equals(datePriseFonction, that.datePriseFonction) &&
            Objects.equals(dateFinFonction, that.dateFinFonction) &&
            Objects.equals(actif, that.actif) &&
            Objects.equals(adherentId, that.adherentId) &&
            Objects.equals(fonctionId, that.fonctionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            matriculeAdherent,
            codeFonction,
            datePriseFonction,
            dateFinFonction,
            actif,
            adherentId,
            fonctionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FonctionAdherentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matriculeAdherent != null ? "matriculeAdherent=" + matriculeAdherent + ", " : "") +
            (codeFonction != null ? "codeFonction=" + codeFonction + ", " : "") +
            (datePriseFonction != null ? "datePriseFonction=" + datePriseFonction + ", " : "") +
            (dateFinFonction != null ? "dateFinFonction=" + dateFinFonction + ", " : "") +
            (actif != null ? "actif=" + actif + ", " : "") +
            (adherentId != null ? "adherentId=" + adherentId + ", " : "") +
            (fonctionId != null ? "fonctionId=" + fonctionId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
