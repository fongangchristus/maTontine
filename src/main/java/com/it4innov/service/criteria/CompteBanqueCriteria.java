package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CompteBanque} entity. This class is used
 * in {@link com.it4innov.web.rest.CompteBanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compte-banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteBanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private StringFilter description;

    private StringFilter matriculeAdherant;

    private DoubleFilter montantDisponnible;

    private LongFilter cotisationBanqueId;

    private LongFilter decaisementBanqueId;

    private LongFilter banqueId;

    private Boolean distinct;

    public CompteBanqueCriteria() {}

    public CompteBanqueCriteria(CompteBanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.matriculeAdherant = other.matriculeAdherant == null ? null : other.matriculeAdherant.copy();
        this.montantDisponnible = other.montantDisponnible == null ? null : other.montantDisponnible.copy();
        this.cotisationBanqueId = other.cotisationBanqueId == null ? null : other.cotisationBanqueId.copy();
        this.decaisementBanqueId = other.decaisementBanqueId == null ? null : other.decaisementBanqueId.copy();
        this.banqueId = other.banqueId == null ? null : other.banqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompteBanqueCriteria copy() {
        return new CompteBanqueCriteria(this);
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

    public StringFilter getMatriculeAdherant() {
        return matriculeAdherant;
    }

    public StringFilter matriculeAdherant() {
        if (matriculeAdherant == null) {
            matriculeAdherant = new StringFilter();
        }
        return matriculeAdherant;
    }

    public void setMatriculeAdherant(StringFilter matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public DoubleFilter getMontantDisponnible() {
        return montantDisponnible;
    }

    public DoubleFilter montantDisponnible() {
        if (montantDisponnible == null) {
            montantDisponnible = new DoubleFilter();
        }
        return montantDisponnible;
    }

    public void setMontantDisponnible(DoubleFilter montantDisponnible) {
        this.montantDisponnible = montantDisponnible;
    }

    public LongFilter getCotisationBanqueId() {
        return cotisationBanqueId;
    }

    public LongFilter cotisationBanqueId() {
        if (cotisationBanqueId == null) {
            cotisationBanqueId = new LongFilter();
        }
        return cotisationBanqueId;
    }

    public void setCotisationBanqueId(LongFilter cotisationBanqueId) {
        this.cotisationBanqueId = cotisationBanqueId;
    }

    public LongFilter getDecaisementBanqueId() {
        return decaisementBanqueId;
    }

    public LongFilter decaisementBanqueId() {
        if (decaisementBanqueId == null) {
            decaisementBanqueId = new LongFilter();
        }
        return decaisementBanqueId;
    }

    public void setDecaisementBanqueId(LongFilter decaisementBanqueId) {
        this.decaisementBanqueId = decaisementBanqueId;
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
        final CompteBanqueCriteria that = (CompteBanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(matriculeAdherant, that.matriculeAdherant) &&
            Objects.equals(montantDisponnible, that.montantDisponnible) &&
            Objects.equals(cotisationBanqueId, that.cotisationBanqueId) &&
            Objects.equals(decaisementBanqueId, that.decaisementBanqueId) &&
            Objects.equals(banqueId, that.banqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            libelle,
            description,
            matriculeAdherant,
            montantDisponnible,
            cotisationBanqueId,
            decaisementBanqueId,
            banqueId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteBanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (matriculeAdherant != null ? "matriculeAdherant=" + matriculeAdherant + ", " : "") +
            (montantDisponnible != null ? "montantDisponnible=" + montantDisponnible + ", " : "") +
            (cotisationBanqueId != null ? "cotisationBanqueId=" + cotisationBanqueId + ", " : "") +
            (decaisementBanqueId != null ? "decaisementBanqueId=" + decaisementBanqueId + ", " : "") +
            (banqueId != null ? "banqueId=" + banqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
