package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.EtatCotisation;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CotisationTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.CotisationTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cotisation-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationTontineCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EtatCotisation
     */
    public static class EtatCotisationFilter extends Filter<EtatCotisation> {

        public EtatCotisationFilter() {}

        public EtatCotisationFilter(EtatCotisationFilter filter) {
            super(filter);
        }

        @Override
        public EtatCotisationFilter copy() {
            return new EtatCotisationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter montantCotise;

    private StringFilter pieceJustifPath;

    private LocalDateFilter dateCotisation;

    private LocalDateFilter dateValidation;

    private StringFilter commentaire;

    private EtatCotisationFilter etat;

    private LongFilter paiementTontineId;

    private LongFilter sessionTontineId;

    private LongFilter compteTontineId;

    private Boolean distinct;

    public CotisationTontineCriteria() {}

    public CotisationTontineCriteria(CotisationTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.montantCotise = other.montantCotise == null ? null : other.montantCotise.copy();
        this.pieceJustifPath = other.pieceJustifPath == null ? null : other.pieceJustifPath.copy();
        this.dateCotisation = other.dateCotisation == null ? null : other.dateCotisation.copy();
        this.dateValidation = other.dateValidation == null ? null : other.dateValidation.copy();
        this.commentaire = other.commentaire == null ? null : other.commentaire.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.paiementTontineId = other.paiementTontineId == null ? null : other.paiementTontineId.copy();
        this.sessionTontineId = other.sessionTontineId == null ? null : other.sessionTontineId.copy();
        this.compteTontineId = other.compteTontineId == null ? null : other.compteTontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CotisationTontineCriteria copy() {
        return new CotisationTontineCriteria(this);
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

    public StringFilter getPieceJustifPath() {
        return pieceJustifPath;
    }

    public StringFilter pieceJustifPath() {
        if (pieceJustifPath == null) {
            pieceJustifPath = new StringFilter();
        }
        return pieceJustifPath;
    }

    public void setPieceJustifPath(StringFilter pieceJustifPath) {
        this.pieceJustifPath = pieceJustifPath;
    }

    public LocalDateFilter getDateCotisation() {
        return dateCotisation;
    }

    public LocalDateFilter dateCotisation() {
        if (dateCotisation == null) {
            dateCotisation = new LocalDateFilter();
        }
        return dateCotisation;
    }

    public void setDateCotisation(LocalDateFilter dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public LocalDateFilter getDateValidation() {
        return dateValidation;
    }

    public LocalDateFilter dateValidation() {
        if (dateValidation == null) {
            dateValidation = new LocalDateFilter();
        }
        return dateValidation;
    }

    public void setDateValidation(LocalDateFilter dateValidation) {
        this.dateValidation = dateValidation;
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

    public EtatCotisationFilter getEtat() {
        return etat;
    }

    public EtatCotisationFilter etat() {
        if (etat == null) {
            etat = new EtatCotisationFilter();
        }
        return etat;
    }

    public void setEtat(EtatCotisationFilter etat) {
        this.etat = etat;
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

    public LongFilter getSessionTontineId() {
        return sessionTontineId;
    }

    public LongFilter sessionTontineId() {
        if (sessionTontineId == null) {
            sessionTontineId = new LongFilter();
        }
        return sessionTontineId;
    }

    public void setSessionTontineId(LongFilter sessionTontineId) {
        this.sessionTontineId = sessionTontineId;
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
        final CotisationTontineCriteria that = (CotisationTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(montantCotise, that.montantCotise) &&
            Objects.equals(pieceJustifPath, that.pieceJustifPath) &&
            Objects.equals(dateCotisation, that.dateCotisation) &&
            Objects.equals(dateValidation, that.dateValidation) &&
            Objects.equals(commentaire, that.commentaire) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(paiementTontineId, that.paiementTontineId) &&
            Objects.equals(sessionTontineId, that.sessionTontineId) &&
            Objects.equals(compteTontineId, that.compteTontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            montantCotise,
            pieceJustifPath,
            dateCotisation,
            dateValidation,
            commentaire,
            etat,
            paiementTontineId,
            sessionTontineId,
            compteTontineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (montantCotise != null ? "montantCotise=" + montantCotise + ", " : "") +
            (pieceJustifPath != null ? "pieceJustifPath=" + pieceJustifPath + ", " : "") +
            (dateCotisation != null ? "dateCotisation=" + dateCotisation + ", " : "") +
            (dateValidation != null ? "dateValidation=" + dateValidation + ", " : "") +
            (commentaire != null ? "commentaire=" + commentaire + ", " : "") +
            (etat != null ? "etat=" + etat + ", " : "") +
            (paiementTontineId != null ? "paiementTontineId=" + paiementTontineId + ", " : "") +
            (sessionTontineId != null ? "sessionTontineId=" + sessionTontineId + ", " : "") +
            (compteTontineId != null ? "compteTontineId=" + compteTontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
