package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.StatutPot;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Pot} entity. This class is used
 * in {@link com.it4innov.web.rest.PotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PotCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutPot
     */
    public static class StatutPotFilter extends Filter<StatutPot> {

        public StatutPotFilter() {}

        public StatutPotFilter(StatutPotFilter filter) {
            super(filter);
        }

        @Override
        public StatutPotFilter copy() {
            return new StatutPotFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libele;

    private StringFilter codepot;

    private DoubleFilter montantCible;

    private StringFilter description;

    private LocalDateFilter dateDebutCollecte;

    private LocalDateFilter dateFinCollecte;

    private StatutPotFilter statut;

    private LongFilter contributionPotId;

    private LongFilter commentairePotId;

    private LongFilter typePotId;

    private Boolean distinct;

    public PotCriteria() {}

    public PotCriteria(PotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.codepot = other.codepot == null ? null : other.codepot.copy();
        this.montantCible = other.montantCible == null ? null : other.montantCible.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateDebutCollecte = other.dateDebutCollecte == null ? null : other.dateDebutCollecte.copy();
        this.dateFinCollecte = other.dateFinCollecte == null ? null : other.dateFinCollecte.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.contributionPotId = other.contributionPotId == null ? null : other.contributionPotId.copy();
        this.commentairePotId = other.commentairePotId == null ? null : other.commentairePotId.copy();
        this.typePotId = other.typePotId == null ? null : other.typePotId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PotCriteria copy() {
        return new PotCriteria(this);
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

    public StringFilter getCodepot() {
        return codepot;
    }

    public StringFilter codepot() {
        if (codepot == null) {
            codepot = new StringFilter();
        }
        return codepot;
    }

    public void setCodepot(StringFilter codepot) {
        this.codepot = codepot;
    }

    public DoubleFilter getMontantCible() {
        return montantCible;
    }

    public DoubleFilter montantCible() {
        if (montantCible == null) {
            montantCible = new DoubleFilter();
        }
        return montantCible;
    }

    public void setMontantCible(DoubleFilter montantCible) {
        this.montantCible = montantCible;
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

    public LocalDateFilter getDateDebutCollecte() {
        return dateDebutCollecte;
    }

    public LocalDateFilter dateDebutCollecte() {
        if (dateDebutCollecte == null) {
            dateDebutCollecte = new LocalDateFilter();
        }
        return dateDebutCollecte;
    }

    public void setDateDebutCollecte(LocalDateFilter dateDebutCollecte) {
        this.dateDebutCollecte = dateDebutCollecte;
    }

    public LocalDateFilter getDateFinCollecte() {
        return dateFinCollecte;
    }

    public LocalDateFilter dateFinCollecte() {
        if (dateFinCollecte == null) {
            dateFinCollecte = new LocalDateFilter();
        }
        return dateFinCollecte;
    }

    public void setDateFinCollecte(LocalDateFilter dateFinCollecte) {
        this.dateFinCollecte = dateFinCollecte;
    }

    public StatutPotFilter getStatut() {
        return statut;
    }

    public StatutPotFilter statut() {
        if (statut == null) {
            statut = new StatutPotFilter();
        }
        return statut;
    }

    public void setStatut(StatutPotFilter statut) {
        this.statut = statut;
    }

    public LongFilter getContributionPotId() {
        return contributionPotId;
    }

    public LongFilter contributionPotId() {
        if (contributionPotId == null) {
            contributionPotId = new LongFilter();
        }
        return contributionPotId;
    }

    public void setContributionPotId(LongFilter contributionPotId) {
        this.contributionPotId = contributionPotId;
    }

    public LongFilter getCommentairePotId() {
        return commentairePotId;
    }

    public LongFilter commentairePotId() {
        if (commentairePotId == null) {
            commentairePotId = new LongFilter();
        }
        return commentairePotId;
    }

    public void setCommentairePotId(LongFilter commentairePotId) {
        this.commentairePotId = commentairePotId;
    }

    public LongFilter getTypePotId() {
        return typePotId;
    }

    public LongFilter typePotId() {
        if (typePotId == null) {
            typePotId = new LongFilter();
        }
        return typePotId;
    }

    public void setTypePotId(LongFilter typePotId) {
        this.typePotId = typePotId;
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
        final PotCriteria that = (PotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(codepot, that.codepot) &&
            Objects.equals(montantCible, that.montantCible) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateDebutCollecte, that.dateDebutCollecte) &&
            Objects.equals(dateFinCollecte, that.dateFinCollecte) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(contributionPotId, that.contributionPotId) &&
            Objects.equals(commentairePotId, that.commentairePotId) &&
            Objects.equals(typePotId, that.typePotId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            libele,
            codepot,
            montantCible,
            description,
            dateDebutCollecte,
            dateFinCollecte,
            statut,
            contributionPotId,
            commentairePotId,
            typePotId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (codepot != null ? "codepot=" + codepot + ", " : "") +
            (montantCible != null ? "montantCible=" + montantCible + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (dateDebutCollecte != null ? "dateDebutCollecte=" + dateDebutCollecte + ", " : "") +
            (dateFinCollecte != null ? "dateFinCollecte=" + dateFinCollecte + ", " : "") +
            (statut != null ? "statut=" + statut + ", " : "") +
            (contributionPotId != null ? "contributionPotId=" + contributionPotId + ", " : "") +
            (commentairePotId != null ? "commentairePotId=" + commentairePotId + ", " : "") +
            (typePotId != null ? "typePotId=" + typePotId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
