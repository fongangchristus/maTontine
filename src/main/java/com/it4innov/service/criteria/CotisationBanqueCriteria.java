package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CotisationBanque} entity. This class is used
 * in {@link com.it4innov.web.rest.CotisationBanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cotisation-banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationBanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private DoubleFilter montant;

    private InstantFilter dateCotisation;

    private DoubleFilter montantCotise;

    private StringFilter commentaire;

    private LongFilter compteBanqueId;

    private Boolean distinct;

    public CotisationBanqueCriteria() {}

    public CotisationBanqueCriteria(CotisationBanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.dateCotisation = other.dateCotisation == null ? null : other.dateCotisation.copy();
        this.montantCotise = other.montantCotise == null ? null : other.montantCotise.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
        this.compteBanqueId = other.compteBanqueId == null ? null : other.compteBanqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CotisationBanqueCriteria copy() {
        return new CotisationBanqueCriteria(this);
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

    public DoubleFilter getMontant() {
        return montant;
    }

    public DoubleFilter montant() {
        if (montant == null) {
            montant = new DoubleFilter();
        }
        return montant;
    }

    public void setMontant(DoubleFilter montant) {
        this.montant = montant;
    }

    public InstantFilter getDateCotisation() {
        return dateCotisation;
    }

    public InstantFilter dateCotisation() {
        if (dateCotisation == null) {
            dateCotisation = new InstantFilter();
        }
        return dateCotisation;
    }

    public void setDateCotisation(InstantFilter dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public DoubleFilter getMontantCotise() {
        return montantCotise;
    }

    public DoubleFilter montantCotise() {
        if (montantCotise == null) {
            montantCotise = new DoubleFilter();
        }
        return montantCotise;
    }

    public void setMontantCotise(DoubleFilter montantCotise) {
        this.montantCotise = montantCotise;
    }

    public StringFilter getCommentaire() {
        return commentaire;
    }

    public StringFilter commentaire() {
        if (commentaire == null) {
            commentaire = new StringFilter();
        }
        return commentaire;
    }

    public void setCommentaire(StringFilter commentaire) {
        this.commentaire = commentaire;
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
        final CotisationBanqueCriteria that = (CotisationBanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(dateCotisation, that.dateCotisation) &&
            Objects.equals(montantCotise, that.montantCotise) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(compteBanqueId, that.compteBanqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, montant, dateCotisation, montantCotise, commentaire, compteBanqueId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationBanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (montant != null ? "montant=" + montant + ", " : "") +
            (dateCotisation != null ? "dateCotisation=" + dateCotisation + ", " : "") +
            (montantCotise != null ? "montantCotise=" + montantCotise + ", " : "") +
            (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            (compteBanqueId != null ? "compteBanqueId=" + compteBanqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
