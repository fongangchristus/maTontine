package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.ContributionPot} entity. This class is used
 * in {@link com.it4innov.web.rest.ContributionPotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contribution-pots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContributionPotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identifiant;

    private StringFilter matriculeContributeur;

    private DoubleFilter montantContribution;

    private InstantFilter dateContribution;

    private LongFilter potId;

    private Boolean distinct;

    public ContributionPotCriteria() {}

    public ContributionPotCriteria(ContributionPotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.identifiant = other.identifiant == null ? null : other.identifiant.copy();
        this.matriculeContributeur = other.matriculeContributeur == null ? null : other.matriculeContributeur.copy();
        this.montantContribution = other.montantContribution == null ? null : other.montantContribution.copy();
        this.dateContribution = other.dateContribution == null ? null : other.dateContribution.copy();
        this.potId = other.potId == null ? null : other.potId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ContributionPotCriteria copy() {
        return new ContributionPotCriteria(this);
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

    public StringFilter getIdentifiant() {
        return identifiant;
    }

    public StringFilter identifiant() {
        if (identifiant == null) {
            identifiant = new StringFilter();
        }
        return identifiant;
    }

    public void setIdentifiant(StringFilter identifiant) {
        this.identifiant = identifiant;
    }

    public StringFilter getMatriculeContributeur() {
        return matriculeContributeur;
    }

    public StringFilter matriculeContributeur() {
        if (matriculeContributeur == null) {
            matriculeContributeur = new StringFilter();
        }
        return matriculeContributeur;
    }

    public void setMatriculeContributeur(StringFilter matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public DoubleFilter getMontantContribution() {
        return montantContribution;
    }

    public DoubleFilter montantContribution() {
        if (montantContribution == null) {
            montantContribution = new DoubleFilter();
        }
        return montantContribution;
    }

    public void setMontantContribution(DoubleFilter montantContribution) {
        this.montantContribution = montantContribution;
    }

    public InstantFilter getDateContribution() {
        return dateContribution;
    }

    public InstantFilter dateContribution() {
        if (dateContribution == null) {
            dateContribution = new InstantFilter();
        }
        return dateContribution;
    }

    public void setDateContribution(InstantFilter dateContribution) {
        this.dateContribution = dateContribution;
    }

    public LongFilter getPotId() {
        return potId;
    }

    public LongFilter potId() {
        if (potId == null) {
            potId = new LongFilter();
        }
        return potId;
    }

    public void setPotId(LongFilter potId) {
        this.potId = potId;
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
        final ContributionPotCriteria that = (ContributionPotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identifiant, that.identifiant) &&
            Objects.equals(matriculeContributeur, that.matriculeContributeur) &&
            Objects.equals(montantContribution, that.montantContribution) &&
            Objects.equals(dateContribution, that.dateContribution) &&
            Objects.equals(potId, that.potId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifiant, matriculeContributeur, montantContribution, dateContribution, potId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContributionPotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (identifiant != null ? "identifiant=" + identifiant + ", " : "") +
            (matriculeContributeur != null ? "matriculeContributeur=" + matriculeContributeur + ", " : "") +
            (montantContribution != null ? "montantContribution=" + montantContribution + ", " : "") +
            (dateContribution != null ? "dateContribution=" + dateContribution + ", " : "") +
            (potId != null ? "potId=" + potId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
