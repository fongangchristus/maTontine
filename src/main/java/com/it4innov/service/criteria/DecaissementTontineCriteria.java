package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.DecaissementTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.DecaissementTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decaissement-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaissementTontineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private LocalDateFilter dateDecaissement;

    private DoubleFilter montantDecaisse;

    private StringFilter commentaire;

    private LongFilter paiementTontineId;

    private LongFilter tontineId;

    private LongFilter compteTontineId;

    private Boolean distinct;

    public DecaissementTontineCriteria() {}

    public DecaissementTontineCriteria(DecaissementTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.dateDecaissement = other.dateDecaissement == null ? null : other.dateDecaissement.copy();
        this.montantDecaisse = other.montantDecaisse == null ? null : other.montantDecaisse.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
        this.paiementTontineId = other.paiementTontineId == null ? null : other.paiementTontineId.copy();
        this.tontineId = other.tontineId == null ? null : other.tontineId.copy();
        this.compteTontineId = other.compteTontineId == null ? null : other.compteTontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DecaissementTontineCriteria copy() {
        return new DecaissementTontineCriteria(this);
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

    public LocalDateFilter getDateDecaissement() {
        return dateDecaissement;
    }

    public LocalDateFilter dateDecaissement() {
        if (dateDecaissement == null) {
            dateDecaissement = new LocalDateFilter();
        }
        return dateDecaissement;
    }

    public void setDateDecaissement(LocalDateFilter dateDecaissement) {
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

    public LongFilter getPaiementTontineId() {
        return paiementTontineId;
    }

    public LongFilter paiementTontineId() {
        if (paiementTontineId == null) {
            paiementTontineId = new LongFilter();
        }
        return paiementTontineId;
    }

    public void setPaiementTontineId(LongFilter paiementTontineId) {
        this.paiementTontineId = paiementTontineId;
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

    public LongFilter getCompteTontineId() {
        return compteTontineId;
    }

    public LongFilter compteTontineId() {
        if (compteTontineId == null) {
            compteTontineId = new LongFilter();
        }
        return compteTontineId;
    }

    public void setCompteTontineId(LongFilter compteTontineId) {
        this.compteTontineId = compteTontineId;
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
        final DecaissementTontineCriteria that = (DecaissementTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(dateDecaissement, that.dateDecaissement) &&
            Objects.equals(montantDecaisse, that.montantDecaisse) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(paiementTontineId, that.paiementTontineId) &&
            Objects.equals(tontineId, that.tontineId) &&
            Objects.equals(compteTontineId, that.compteTontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            libele,
            dateDecaissement,
            montantDecaisse,
            commentaire,
            paiementTontineId,
            tontineId,
            compteTontineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaissementTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (dateDecaissement != null ? "dateDecaissement=" + dateDecaissement + ", " : "") +
            (montantDecaisse != null ? "montantDecaisse=" + montantDecaisse + ", " : "") +
            (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            (paiementTontineId != null ? "paiementTontineId=" + paiementTontineId + ", " : "") +
            (tontineId != null ? "tontineId=" + tontineId + ", " : "") +
            (compteTontineId != null ? "compteTontineId=" + compteTontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
