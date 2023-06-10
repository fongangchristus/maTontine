package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.Langue;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Association} entity. This class is used
 * in {@link com.it4innov.web.rest.AssociationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /associations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssociationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Langue
     */
    public static class LangueFilter extends Filter<Langue> {

        public LangueFilter() {}

        public LangueFilter(LangueFilter filter) {
            super(filter);
        }

        @Override
        public LangueFilter copy() {
            return new LangueFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter denomination;

    private StringFilter slogan;

    private StringFilter logoPath;

    private StringFilter reglementPath;

    private StringFilter statutPath;

    private StringFilter description;

    private LocalDateFilter dateCreation;

    private StringFilter fuseauHoraire;

    private LangueFilter langue;

    private StringFilter presentation;

    private StringFilter siegeSocial;

    private StringFilter email;

    private BooleanFilter isActif;

    private LongFilter exerciseId;

    private LongFilter documentAssociationId;

    private LongFilter monnaieId;

    private Boolean distinct;

    public AssociationCriteria() {}

    public AssociationCriteria(AssociationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.denomination = other.denomination == null ? null : other.denomination.copy();
        this.slogan = other.slogan == null ? null : other.slogan.copy();
        this.logoPath = other.logoPath == null ? null : other.logoPath.copy();
        this.reglementPath = other.reglementPath == null ? null : other.reglementPath.copy();
        this.statutPath = other.statutPath == null ? null : other.statutPath.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateCreation = other.dateCreation == null ? null : other.dateCreation.copy();
        this.fuseauHoraire = other.fuseauHoraire == null ? null : other.fuseauHoraire.copy();
        this.langue = other.langue == null ? null : other.langue.copy();
        this.presentation = other.presentation == null ? null : other.presentation.copy();
        this.siegeSocial = other.siegeSocial == null ? null : other.siegeSocial.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.isActif = other.isActif == null ? null : other.isActif.copy();
        this.exerciseId = other.exerciseId == null ? null : other.exerciseId.copy();
        this.documentAssociationId = other.documentAssociationId == null ? null : other.documentAssociationId.copy();
        this.monnaieId = other.monnaieId == null ? null : other.monnaieId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssociationCriteria copy() {
        return new AssociationCriteria(this);
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

    public StringFilter getDenomination() {
        return denomination;
    }

    public StringFilter denomination() {
        if (denomination == null) {
            denomination = new StringFilter();
        }
        return denomination;
    }

    public void setDenomination(StringFilter denomination) {
        this.denomination = denomination;
    }

    public StringFilter getSlogan() {
        return slogan;
    }

    public StringFilter slogan() {
        if (slogan == null) {
            slogan = new StringFilter();
        }
        return slogan;
    }

    public void setSlogan(StringFilter slogan) {
        this.slogan = slogan;
    }

    public StringFilter getLogoPath() {
        return logoPath;
    }

    public StringFilter logoPath() {
        if (logoPath == null) {
            logoPath = new StringFilter();
        }
        return logoPath;
    }

    public void setLogoPath(StringFilter logoPath) {
        this.logoPath = logoPath;
    }

    public StringFilter getReglementPath() {
        return reglementPath;
    }

    public StringFilter reglementPath() {
        if (reglementPath == null) {
            reglementPath = new StringFilter();
        }
        return reglementPath;
    }

    public void setReglementPath(StringFilter reglementPath) {
        this.reglementPath = reglementPath;
    }

    public StringFilter getStatutPath() {
        return statutPath;
    }

    public StringFilter statutPath() {
        if (statutPath == null) {
            statutPath = new StringFilter();
        }
        return statutPath;
    }

    public void setStatutPath(StringFilter statutPath) {
        this.statutPath = statutPath;
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

    public StringFilter getFuseauHoraire() {
        return fuseauHoraire;
    }

    public StringFilter fuseauHoraire() {
        if (fuseauHoraire == null) {
            fuseauHoraire = new StringFilter();
        }
        return fuseauHoraire;
    }

    public void setFuseauHoraire(StringFilter fuseauHoraire) {
        this.fuseauHoraire = fuseauHoraire;
    }

    public LangueFilter getLangue() {
        return langue;
    }

    public LangueFilter langue() {
        if (langue == null) {
            langue = new LangueFilter();
        }
        return langue;
    }

    public void setLangue(LangueFilter langue) {
        this.langue = langue;
    }

    public StringFilter getPresentation() {
        return presentation;
    }

    public StringFilter presentation() {
        if (presentation == null) {
            presentation = new StringFilter();
        }
        return presentation;
    }

    public void setPresentation(StringFilter presentation) {
        this.presentation = presentation;
    }

    public StringFilter getSiegeSocial() {
        return siegeSocial;
    }

    public StringFilter siegeSocial() {
        if (siegeSocial == null) {
            siegeSocial = new StringFilter();
        }
        return siegeSocial;
    }

    public void setSiegeSocial(StringFilter siegeSocial) {
        this.siegeSocial = siegeSocial;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public BooleanFilter getIsActif() {
        return isActif;
    }

    public BooleanFilter isActif() {
        if (isActif == null) {
            isActif = new BooleanFilter();
        }
        return isActif;
    }

    public void setIsActif(BooleanFilter isActif) {
        this.isActif = isActif;
    }

    public LongFilter getExerciseId() {
        return exerciseId;
    }

    public LongFilter exerciseId() {
        if (exerciseId == null) {
            exerciseId = new LongFilter();
        }
        return exerciseId;
    }

    public void setExerciseId(LongFilter exerciseId) {
        this.exerciseId = exerciseId;
    }

    public LongFilter getDocumentAssociationId() {
        return documentAssociationId;
    }

    public LongFilter documentAssociationId() {
        if (documentAssociationId == null) {
            documentAssociationId = new LongFilter();
        }
        return documentAssociationId;
    }

    public void setDocumentAssociationId(LongFilter documentAssociationId) {
        this.documentAssociationId = documentAssociationId;
    }

    public LongFilter getMonnaieId() {
        return monnaieId;
    }

    public LongFilter monnaieId() {
        if (monnaieId == null) {
            monnaieId = new LongFilter();
        }
        return monnaieId;
    }

    public void setMonnaieId(LongFilter monnaieId) {
        this.monnaieId = monnaieId;
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
        final AssociationCriteria that = (AssociationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(denomination, that.denomination) &&
            Objects.equals(slogan, that.slogan) &&
            Objects.equals(logoPath, that.logoPath) &&
            Objects.equals(reglementPath, that.reglementPath) &&
            Objects.equals(statutPath, that.statutPath) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(fuseauHoraire, that.fuseauHoraire) &&
            Objects.equals(langue, that.langue) &&
            Objects.equals(presentation, that.presentation) &&
            Objects.equals(siegeSocial, that.siegeSocial) &&
            Objects.equals(email, that.email) &&
            Objects.equals(isActif, that.isActif) &&
            Objects.equals(exerciseId, that.exerciseId) &&
            Objects.equals(documentAssociationId, that.documentAssociationId) &&
            Objects.equals(monnaieId, that.monnaieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeAssociation,
            denomination,
            slogan,
            logoPath,
            reglementPath,
            statutPath,
            description,
            dateCreation,
            fuseauHoraire,
            langue,
            presentation,
            siegeSocial,
            email,
            isActif,
            exerciseId,
            documentAssociationId,
            monnaieId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssociationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (denomination != null ? "denomination=" + denomination + ", " : "") +
            (slogan != null ? "slogan=" + slogan + ", " : "") +
            (logoPath != null ? "logoPath=" + logoPath + ", " : "") +
            (reglementPath != null ? "reglementPath=" + reglementPath + ", " : "") +
            (statutPath != null ? "statutPath=" + statutPath + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (dateCreation != null ? "dateCreation=" + dateCreation + ", " : "") +
            (fuseauHoraire != null ? "fuseauHoraire=" + fuseauHoraire + ", " : "") +
            (langue != null ? "langue=" + langue + ", " : "") +
            (presentation != null ? "presentation=" + presentation + ", " : "") +
            (siegeSocial != null ? "siegeSocial=" + siegeSocial + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (isActif != null ? "isActif=" + isActif + ", " : "") +
            (exerciseId != null ? "exerciseId=" + exerciseId + ", " : "") +
            (documentAssociationId != null ? "documentAssociationId=" + documentAssociationId + ", " : "") +
            (monnaieId != null ? "monnaieId=" + monnaieId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
