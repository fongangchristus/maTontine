package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.FichePresence} entity. This class is used
 * in {@link com.it4innov.web.rest.FichePresenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fiche-presences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichePresenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private InstantFilter dateJour;

    private StringFilter description;

    private StringFilter codeAssemble;

    private StringFilter codeEvenement;

    private LongFilter presenceId;

    private Boolean distinct;

    public FichePresenceCriteria() {}

    public FichePresenceCriteria(FichePresenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.dateJour = other.dateJour == null ? null : other.dateJour.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.codeAssemble = other.codeAssemble == null ? null : other.codeAssemble.copy();
        this.codeEvenement = other.codeEvenement == null ? null : other.codeEvenement.copy();
        this.presenceId = other.presenceId == null ? null : other.presenceId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FichePresenceCriteria copy() {
        return new FichePresenceCriteria(this);
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

    public InstantFilter getDateJour() {
        return dateJour;
    }

    public InstantFilter dateJour() {
        if (dateJour == null) {
            dateJour = new InstantFilter();
        }
        return dateJour;
    }

    public void setDateJour(InstantFilter dateJour) {
        this.dateJour = dateJour;
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

    public StringFilter getCodeAssemble() {
        return codeAssemble;
    }

    public StringFilter codeAssemble() {
        if (codeAssemble == null) {
            codeAssemble = new StringFilter();
        }
        return codeAssemble;
    }

    public void setCodeAssemble(StringFilter codeAssemble) {
        this.codeAssemble = codeAssemble;
    }

    public StringFilter getCodeEvenement() {
        return codeEvenement;
    }

    public StringFilter codeEvenement() {
        if (codeEvenement == null) {
            codeEvenement = new StringFilter();
        }
        return codeEvenement;
    }

    public void setCodeEvenement(StringFilter codeEvenement) {
        this.codeEvenement = codeEvenement;
    }

    public LongFilter getPresenceId() {
        return presenceId;
    }

    public LongFilter presenceId() {
        if (presenceId == null) {
            presenceId = new LongFilter();
        }
        return presenceId;
    }

    public void setPresenceId(LongFilter presenceId) {
        this.presenceId = presenceId;
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
        final FichePresenceCriteria that = (FichePresenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(dateJour, that.dateJour) &&
            Objects.equals(description, that.description) &&
            Objects.equals(codeAssemble, that.codeAssemble) &&
            Objects.equals(codeEvenement, that.codeEvenement) &&
            Objects.equals(presenceId, that.presenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, dateJour, description, codeAssemble, codeEvenement, presenceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichePresenceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (dateJour != null ? "dateJour=" + dateJour + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (codeAssemble != null ? "codeAssemble=" + codeAssemble + ", " : "") +
            (codeEvenement != null ? "codeEvenement=" + codeEvenement + ", " : "") +
            (presenceId != null ? "presenceId=" + presenceId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
