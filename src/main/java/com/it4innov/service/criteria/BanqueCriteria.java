package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Banque} entity. This class is used
 * in {@link com.it4innov.web.rest.BanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter libelle;

    private StringFilter description;

    private InstantFilter dateOuverture;

    private InstantFilter dateCloture;

    private LongFilter compteBanqueId;

    private LongFilter gestionnaireBanqueId;

    private Boolean distinct;

    public BanqueCriteria() {}

    public BanqueCriteria(BanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateOuverture = other.dateOuverture == null ? null : other.dateOuverture.copy();
        this.dateCloture = other.dateCloture == null ? null : other.dateCloture.copy();
        this.compteBanqueId = other.compteBanqueId == null ? null : other.compteBanqueId.copy();
        this.gestionnaireBanqueId = other.gestionnaireBanqueId == null ? null : other.gestionnaireBanqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BanqueCriteria copy() {
        return new BanqueCriteria(this);
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

    public InstantFilter getDateOuverture() {
        return dateOuverture;
    }

    public InstantFilter dateOuverture() {
        if (dateOuverture == null) {
            dateOuverture = new InstantFilter();
        }
        return dateOuverture;
    }

    public void setDateOuverture(InstantFilter dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public InstantFilter getDateCloture() {
        return dateCloture;
    }

    public InstantFilter dateCloture() {
        if (dateCloture == null) {
            dateCloture = new InstantFilter();
        }
        return dateCloture;
    }

    public void setDateCloture(InstantFilter dateCloture) {
        this.dateCloture = dateCloture;
    }

    public LongFilter getCompteBanqueId() {
        return compteBanqueId;
    }

    public LongFilter compteBanqueId() {
        if (compteBanqueId == null) {
            compteBanqueId = new LongFilter();
        }
        return compteBanqueId;
    }

    public void setCompteBanqueId(LongFilter compteBanqueId) {
        this.compteBanqueId = compteBanqueId;
    }

    public LongFilter getGestionnaireBanqueId() {
        return gestionnaireBanqueId;
    }

    public LongFilter gestionnaireBanqueId() {
        if (gestionnaireBanqueId == null) {
            gestionnaireBanqueId = new LongFilter();
        }
        return gestionnaireBanqueId;
    }

    public void setGestionnaireBanqueId(LongFilter gestionnaireBanqueId) {
        this.gestionnaireBanqueId = gestionnaireBanqueId;
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
        final BanqueCriteria that = (BanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateOuverture, that.dateOuverture) &&
            Objects.equals(dateCloture, that.dateCloture) &&
            Objects.equals(compteBanqueId, that.compteBanqueId) &&
            Objects.equals(gestionnaireBanqueId, that.gestionnaireBanqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeAssociation,
            libelle,
            description,
            dateOuverture,
            dateCloture,
            compteBanqueId,
            gestionnaireBanqueId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (dateOuverture != null ? "dateOuverture=" + dateOuverture + ", " : "") +
            (dateCloture != null ? "dateCloture=" + dateCloture + ", " : "") +
            (compteBanqueId != null ? "compteBanqueId=" + compteBanqueId + ", " : "") +
            (gestionnaireBanqueId != null ? "gestionnaireBanqueId=" + gestionnaireBanqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
