package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.NatureAssemble;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Assemble} entity. This class is used
 * in {@link com.it4innov.web.rest.AssembleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /assembles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssembleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NatureAssemble
     */
    public static class NatureAssembleFilter extends Filter<NatureAssemble> {

        public NatureAssembleFilter() {}

        public NatureAssembleFilter(NatureAssembleFilter filter) {
            super(filter);
        }

        @Override
        public NatureAssembleFilter copy() {
            return new NatureAssembleFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeAssociation;

    private StringFilter libele;

    private BooleanFilter enLigne;

    private StringFilter dateSeance;

    private StringFilter lieuSeance;

    private StringFilter matriculeMembreRecoit;

    private NatureAssembleFilter nature;

    private StringFilter compteRendu;

    private StringFilter resumeAssemble;

    private StringFilter documentCRPath;

    private Boolean distinct;

    public AssembleCriteria() {}

    public AssembleCriteria(AssembleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.enLigne = other.enLigne == null ? null : other.enLigne.copy();
        this.dateSeance = other.dateSeance == null ? null : other.dateSeance.copy();
        this.lieuSeance = other.lieuSeance == null ? null : other.lieuSeance.copy();
        this.matriculeMembreRecoit = other.matriculeMembreRecoit == null ? null : other.matriculeMembreRecoit.copy();
        this.nature = other.nature == null ? null : other.nature.copy();
        this.compteRendu = other.compteRendu == null ? null : other.compteRendu.copy();
        this.resumeAssemble = other.resumeAssemble == null ? null : other.resumeAssemble.copy();
        this.documentCRPath = other.documentCRPath == null ? null : other.documentCRPath.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AssembleCriteria copy() {
        return new AssembleCriteria(this);
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

    public BooleanFilter getEnLigne() {
        return enLigne;
    }

    public BooleanFilter enLigne() {
        if (enLigne == null) {
            enLigne = new BooleanFilter();
        }
        return enLigne;
    }

    public void setEnLigne(BooleanFilter enLigne) {
        this.enLigne = enLigne;
    }

    public StringFilter getDateSeance() {
        return dateSeance;
    }

    public StringFilter dateSeance() {
        if (dateSeance == null) {
            dateSeance = new StringFilter();
        }
        return dateSeance;
    }

    public void setDateSeance(StringFilter dateSeance) {
        this.dateSeance = dateSeance;
    }

    public StringFilter getLieuSeance() {
        return lieuSeance;
    }

    public StringFilter lieuSeance() {
        if (lieuSeance == null) {
            lieuSeance = new StringFilter();
        }
        return lieuSeance;
    }

    public void setLieuSeance(StringFilter lieuSeance) {
        this.lieuSeance = lieuSeance;
    }

    public StringFilter getMatriculeMembreRecoit() {
        return matriculeMembreRecoit;
    }

    public StringFilter matriculeMembreRecoit() {
        if (matriculeMembreRecoit == null) {
            matriculeMembreRecoit = new StringFilter();
        }
        return matriculeMembreRecoit;
    }

    public void setMatriculeMembreRecoit(StringFilter matriculeMembreRecoit) {
        this.matriculeMembreRecoit = matriculeMembreRecoit;
    }

    public NatureAssembleFilter getNature() {
        return nature;
    }

    public NatureAssembleFilter nature() {
        if (nature == null) {
            nature = new NatureAssembleFilter();
        }
        return nature;
    }

    public void setNature(NatureAssembleFilter nature) {
        this.nature = nature;
    }

    public StringFilter getCompteRendu() {
        return compteRendu;
    }

    public StringFilter compteRendu() {
        if (compteRendu == null) {
            compteRendu = new StringFilter();
        }
        return compteRendu;
    }

    public void setCompteRendu(StringFilter compteRendu) {
        this.compteRendu = compteRendu;
    }

    public StringFilter getResumeAssemble() {
        return resumeAssemble;
    }

    public StringFilter resumeAssemble() {
        if (resumeAssemble == null) {
            resumeAssemble = new StringFilter();
        }
        return resumeAssemble;
    }

    public void setResumeAssemble(StringFilter resumeAssemble) {
        this.resumeAssemble = resumeAssemble;
    }

    public StringFilter getDocumentCRPath() {
        return documentCRPath;
    }

    public StringFilter documentCRPath() {
        if (documentCRPath == null) {
            documentCRPath = new StringFilter();
        }
        return documentCRPath;
    }

    public void setDocumentCRPath(StringFilter documentCRPath) {
        this.documentCRPath = documentCRPath;
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
        final AssembleCriteria that = (AssembleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(enLigne, that.enLigne) &&
            Objects.equals(dateSeance, that.dateSeance) &&
            Objects.equals(lieuSeance, that.lieuSeance) &&
            Objects.equals(matriculeMembreRecoit, that.matriculeMembreRecoit) &&
            Objects.equals(nature, that.nature) &&
            Objects.equals(compteRendu, that.compteRendu) &&
            Objects.equals(resumeAssemble, that.resumeAssemble) &&
            Objects.equals(documentCRPath, that.documentCRPath) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codeAssociation,
            libele,
            enLigne,
            dateSeance,
            lieuSeance,
            matriculeMembreRecoit,
            nature,
            compteRendu,
            resumeAssemble,
            documentCRPath,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssembleCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (enLigne != null ? "enLigne=" + enLigne + ", " : "") +
            (dateSeance != null ? "dateSeance=" + dateSeance + ", " : "") +
            (lieuSeance != null ? "lieuSeance=" + lieuSeance + ", " : "") +
            (matriculeMembreRecoit != null ? "matriculeMembreRecoit=" + matriculeMembreRecoit + ", " : "") +
            (nature != null ? "nature=" + nature + ", " : "") +
            (compteRendu != null ? "compteRendu=" + compteRendu + ", " : "") +
            (resumeAssemble != null ? "resumeAssemble=" + resumeAssemble + ", " : "") +
            (documentCRPath != null ? "documentCRPath=" + documentCRPath + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
