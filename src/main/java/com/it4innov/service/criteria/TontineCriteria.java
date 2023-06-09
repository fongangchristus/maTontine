package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.StatutTontine;
import com.it4innov.domain.enumeration.TypePenalite;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Tontine} entity. This class is used
 * in {@link com.it4innov.web.rest.TontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TontineCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypePenalite
     */
    public static class TypePenaliteFilter extends Filter<TypePenalite> {

        public TypePenaliteFilter() {}

        public TypePenaliteFilter(TypePenaliteFilter filter) {
            super(filter);
        }

        @Override
        public TypePenaliteFilter copy() {
            return new TypePenaliteFilter(this);
        }
    }

    /**
     * Class for filtering StatutTontine
     */
    public static class StatutTontineFilter extends Filter<StatutTontine> {

        public StatutTontineFilter() {}

        public StatutTontineFilter(StatutTontineFilter filter) {
            super(filter);
        }

        @Override
        public StatutTontineFilter copy() {
            return new StatutTontineFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter libele;

    private IntegerFilter nombreTour;

    private IntegerFilter nombrePersonne;

    private DoubleFilter margeBeneficiaire;

    private DoubleFilter montantPart;

    private DoubleFilter montantCagnote;

    private DoubleFilter penaliteRetardCotisation;

    private TypePenaliteFilter typePenalite;

    private LocalDateFilter dateCreation;

    private LocalDateFilter datePremierTour;

    private LocalDateFilter dateDernierTour;

    private StatutTontineFilter statutTontine;

    private StringFilter description;

    private LongFilter sessionTontineId;

    private LongFilter gestionnaireTontineId;

    private Boolean distinct;

    public TontineCriteria() {}

    public TontineCriteria(TontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.nombreTour = other.nombreTour == null ? null : other.nombreTour.copy();
        this.nombrePersonne = other.nombrePersonne == null ? null : other.nombrePersonne.copy();
        this.margeBeneficiaire = other.margeBeneficiaire == null ? null : other.margeBeneficiaire.copy();
        this.montantPart = other.montantPart == null ? null : other.montantPart.copy();
        this.montantCagnote = other.montantCagnote == null ? null : other.montantCagnote.copy();
        this.penaliteRetardCotisation = other.penaliteRetardCotisation == null ? null : other.penaliteRetardCotisation.copy();
        this.typePenalite = other.typePenalite == null ? null : other.typePenalite.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.datePremierTour = other.datePremierTour == null ? null : other.datePremierTour.copy();
        this.dateDernierTour = other.dateDernierTour == null ? null : other.dateDernierTour.copy();
        this.statutTontine = other.statutTontine == null ? null : other.statutTontine.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.sessionTontineId = other.sessionTontineId == null ? null : other.sessionTontineId.copy();
        this.gestionnaireTontineId = other.gestionnaireTontineId == null ? null : other.gestionnaireTontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TontineCriteria copy() {
        return new TontineCriteria(this);
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

    public IntegerFilter getNombreTour() {
        return nombreTour;
    }

    public IntegerFilter nombreTour() {
        if (nombreTour == null) {
            nombreTour = new IntegerFilter();
        }
        return nombreTour;
    }

    public void setNombreTour(IntegerFilter nombreTour) {
        this.nombreTour = nombreTour;
    }

    public IntegerFilter getNombrePersonne() {
        return nombrePersonne;
    }

    public IntegerFilter nombrePersonne() {
        if (nombrePersonne == null) {
            nombrePersonne = new IntegerFilter();
        }
        return nombrePersonne;
    }

    public void setNombrePersonne(IntegerFilter nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public DoubleFilter getMargeBeneficiaire() {
        return margeBeneficiaire;
    }

    public DoubleFilter margeBeneficiaire() {
        if (margeBeneficiaire == null) {
            margeBeneficiaire = new DoubleFilter();
        }
        return margeBeneficiaire;
    }

    public void setMargeBeneficiaire(DoubleFilter margeBeneficiaire) {
        this.margeBeneficiaire = margeBeneficiaire;
    }

    public DoubleFilter getMontantPart() {
        return montantPart;
    }

    public DoubleFilter montantPart() {
        if (montantPart == null) {
            montantPart = new DoubleFilter();
        }
        return montantPart;
    }

    public void setMontantPart(DoubleFilter montantPart) {
        this.montantPart = montantPart;
    }

    public DoubleFilter getMontantCagnote() {
        return montantCagnote;
    }

    public DoubleFilter montantCagnote() {
        if (montantCagnote == null) {
            montantCagnote = new DoubleFilter();
        }
        return montantCagnote;
    }

    public void setMontantCagnote(DoubleFilter montantCagnote) {
        this.montantCagnote = montantCagnote;
    }

    public DoubleFilter getPenaliteRetardCotisation() {
        return penaliteRetardCotisation;
    }

    public DoubleFilter penaliteRetardCotisation() {
        if (penaliteRetardCotisation == null) {
            penaliteRetardCotisation = new DoubleFilter();
        }
        return penaliteRetardCotisation;
    }

    public void setPenaliteRetardCotisation(DoubleFilter penaliteRetardCotisation) {
        this.penaliteRetardCotisation = penaliteRetardCotisation;
    }

    public TypePenaliteFilter getTypePenalite() {
        return typePenalite;
    }

    public TypePenaliteFilter typePenalite() {
        if (typePenalite == null) {
            typePenalite = new TypePenaliteFilter();
        }
        return typePenalite;
    }

    public void setTypePenalite(TypePenaliteFilter typePenalite) {
        this.typePenalite = typePenalite;
    }

    public LocalDateFilter getDateCreation() {
        return dateCreation;
    }

    public LocalDateFilter dateCreation() {
        if (dateCreation == null) {
            dateCreation = new LocalDateFilter();
        }
        return dateCreation;
    }

    public void setDateCreation(LocalDateFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateFilter getDatePremierTour() {
        return datePremierTour;
    }

    public LocalDateFilter datePremierTour() {
        if (datePremierTour == null) {
            datePremierTour = new LocalDateFilter();
        }
        return datePremierTour;
    }

    public void setDatePremierTour(LocalDateFilter datePremierTour) {
        this.datePremierTour = datePremierTour;
    }

    public LocalDateFilter getDateDernierTour() {
        return dateDernierTour;
    }

    public LocalDateFilter dateDernierTour() {
        if (dateDernierTour == null) {
            dateDernierTour = new LocalDateFilter();
        }
        return dateDernierTour;
    }

    public void setDateDernierTour(LocalDateFilter dateDernierTour) {
        this.dateDernierTour = dateDernierTour;
    }

    public StatutTontineFilter getStatutTontine() {
        return statutTontine;
    }

    public StatutTontineFilter statutTontine() {
        if (statutTontine == null) {
            statutTontine = new StatutTontineFilter();
        }
        return statutTontine;
    }

    public void setStatutTontine(StatutTontineFilter statutTontine) {
        this.statutTontine = statutTontine;
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

    public LongFilter getGestionnaireTontineId() {
        return gestionnaireTontineId;
    }

    public LongFilter gestionnaireTontineId() {
        if (gestionnaireTontineId == null) {
            gestionnaireTontineId = new LongFilter();
        }
        return gestionnaireTontineId;
    }

    public void setGestionnaireTontineId(LongFilter gestionnaireTontineId) {
        this.gestionnaireTontineId = gestionnaireTontineId;
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
        final TontineCriteria that = (TontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(nombreTour, that.nombreTour) &&
            Objects.equals(nombrePersonne, that.nombrePersonne) &&
            Objects.equals(margeBeneficiaire, that.margeBeneficiaire) &&
            Objects.equals(montantPart, that.montantPart) &&
            Objects.equals(montantCagnote, that.montantCagnote) &&
            Objects.equals(penaliteRetardCotisation, that.penaliteRetardCotisation) &&
            Objects.equals(typePenalite, that.typePenalite) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(datePremierTour, that.datePremierTour) &&
            Objects.equals(dateDernierTour, that.dateDernierTour) &&
            Objects.equals(statutTontine, that.statutTontine) &&
            Objects.equals(description, that.description) &&
            Objects.equals(sessionTontineId, that.sessionTontineId) &&
            Objects.equals(gestionnaireTontineId, that.gestionnaireTontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeAssociation,
            libele,
            nombreTour,
            nombrePersonne,
            margeBeneficiaire,
            montantPart,
            montantCagnote,
            penaliteRetardCotisation,
            typePenalite,
            dateCreation,
            datePremierTour,
            dateDernierTour,
            statutTontine,
            description,
            sessionTontineId,
            gestionnaireTontineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (nombreTour != null ? "nombreTour=" + nombreTour + ", " : "") +
            (nombrePersonne != null ? "nombrePersonne=" + nombrePersonne + ", " : "") +
            (margeBeneficiaire != null ? "margeBeneficiaire=" + margeBeneficiaire + ", " : "") +
            (montantPart != null ? "montantPart=" + montantPart + ", " : "") +
            (montantCagnote != null ? "montantCagnote=" + montantCagnote + ", " : "") +
            (penaliteRetardCotisation != null ? "penaliteRetardCotisation=" + penaliteRetardCotisation + ", " : "") +
            (typePenalite != null ? "typePenalite=" + typePenalite + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (datePremierTour != null ? "datePremierTour=" + datePremierTour + ", " : "") +
            (dateDernierTour != null ? "dateDernierTour=" + dateDernierTour + ", " : "") +
            (statutTontine != null ? "statutTontine=" + statutTontine + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (sessionTontineId != null ? "sessionTontineId=" + sessionTontineId + ", " : "") +
            (gestionnaireTontineId != null ? "gestionnaireTontineId=" + gestionnaireTontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
