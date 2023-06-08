package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.NatureAssemble;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Assemble} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssembleDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeAssociation;

    private String libele;

    private Boolean enLigne;

    private String dateSeance;

    private String lieuSeance;

    private String matriculeMembreRecoit;

    private NatureAssemble nature;

    private String compteRendu;

    private String resumeAssemble;

    private String documentCRPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Boolean getEnLigne() {
        return enLigne;
    }

    public void setEnLigne(Boolean enLigne) {
        this.enLigne = enLigne;
    }

    public String getDateSeance() {
        return dateSeance;
    }

    public void setDateSeance(String dateSeance) {
        this.dateSeance = dateSeance;
    }

    public String getLieuSeance() {
        return lieuSeance;
    }

    public void setLieuSeance(String lieuSeance) {
        this.lieuSeance = lieuSeance;
    }

    public String getMatriculeMembreRecoit() {
        return matriculeMembreRecoit;
    }

    public void setMatriculeMembreRecoit(String matriculeMembreRecoit) {
        this.matriculeMembreRecoit = matriculeMembreRecoit;
    }

    public NatureAssemble getNature() {
        return nature;
    }

    public void setNature(NatureAssemble nature) {
        this.nature = nature;
    }

    public String getCompteRendu() {
        return compteRendu;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public String getResumeAssemble() {
        return resumeAssemble;
    }

    public void setResumeAssemble(String resumeAssemble) {
        this.resumeAssemble = resumeAssemble;
    }

    public String getDocumentCRPath() {
        return documentCRPath;
    }

    public void setDocumentCRPath(String documentCRPath) {
        this.documentCRPath = documentCRPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssembleDTO)) {
            return false;
        }

        AssembleDTO assembleDTO = (AssembleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assembleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssembleDTO{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", libele='" + getLibele() + "'" +
            ", enLigne='" + getEnLigne() + "'" +
            ", dateSeance='" + getDateSeance() + "'" +
            ", lieuSeance='" + getLieuSeance() + "'" +
            ", matriculeMembreRecoit='" + getMatriculeMembreRecoit() + "'" +
            ", nature='" + getNature() + "'" +
            ", compteRendu='" + getCompteRendu() + "'" +
            ", resumeAssemble='" + getResumeAssemble() + "'" +
            ", documentCRPath='" + getDocumentCRPath() + "'" +
            "}";
    }
}
