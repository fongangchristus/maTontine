package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.DecaisementBanque} entity. This class is used
 * in {@link com.it4innov.web.rest.DecaisementBanqueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decaisement-banques?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaisementBanqueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private DoubleFilter montant;

    private InstantFilter dateDecaissement;

    private DoubleFilter montantDecaisse;

    private StringFilter commentaire;

    private LongFilter compteBanqueId;

    private Boolean distinct;

    public DecaisementBanqueCriteria() {}

    public DecaisementBanqueCriteria(DecaisementBanqueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.dateDecaissement = other.dateDecaissement == null ? null : other.dateDecaissement.copy();
        this.montantDecaisse = other.montantDecaisse == null ? null : other.montantDecaisse.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
        this.compteBanqueId = other.compteBanqueId == null ? null : other.compteBanqueId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DecaisementBanqueCriteria copy() {
        return new DecaisementBanqueCriteria(this);
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

    public InstantFilter getDateDecaissement() {
        return dateDecaissement;
    }

    public InstantFilter dateDecaissement() {
        if (dateDecaissement == null) {
            dateDecaissement = new InstantFilter();
        }
        return dateDecaissement;
    }

    public void setDateDecaissement(InstantFilter dateDecaissement) {
        this.dateDecaissement = dateDecaissement;
    }

    public DoubleFilter getMontantDecaisse() {
        return montantDecaisse;
    }

    public DoubleFilter montantDecaisse() {
        if (montantDecaisse == null) {
            montantDecaisse = new DoubleFilter();
        }
        return montantDecaisse;
    }

    public void setMontantDecaisse(DoubleFilter montantDecaisse) {
        this.montantDecaisse = montantDecaisse;
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
        final DecaisementBanqueCriteria that = (DecaisementBanqueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(dateDecaissement, that.dateDecaissement) &&
            Objects.equals(montantDecaisse, that.montantDecaisse) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(compteBanqueId, that.compteBanqueId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, montant, dateDecaissement, montantDecaisse, commentaire, compteBanqueId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaisementBanqueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (montant != null ? "montant=" + montant + ", " : "") +
            (dateDecaissement != null ? "dateDecaissement=" + dateDecaissement + ", " : "") +
            (montantDecaisse != null ? "montantDecaisse=" + montantDecaisse + ", " : "") +
            (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            (compteBanqueId != null ? "compteBanqueId=" + compteBanqueId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
