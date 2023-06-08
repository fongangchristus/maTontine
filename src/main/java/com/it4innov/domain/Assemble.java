package com.it4innov.domain;

import com.it4innov.domain.enumeration.NatureAssemble;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Assemble.
 */
@Entity
@Table(name = "assemble")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Assemble implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @Column(name = "libele")
    private String libele;

    @Column(name = "en_ligne")
    private Boolean enLigne;

    @Column(name = "date_seance")
    private String dateSeance;

    @Column(name = "lieu_seance")
    private String lieuSeance;

    @Column(name = "matricule_membre_recoit")
    private String matriculeMembreRecoit;

    @Enumerated(EnumType.STRING)
    @Column(name = "nature")
    private NatureAssemble nature;

    @Column(name = "compte_rendu")
    private String compteRendu;

    @Column(name = "resume_assemble")
    private String resumeAssemble;

    @Column(name = "document_cr_path")
    private String documentCRPath;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Assemble id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public Assemble codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getLibele() {
        return this.libele;
    }

    public Assemble libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Boolean getEnLigne() {
        return this.enLigne;
    }

    public Assemble enLigne(Boolean enLigne) {
        this.setEnLigne(enLigne);
        return this;
    }

    public void setEnLigne(Boolean enLigne) {
        this.enLigne = enLigne;
    }

    public String getDateSeance() {
        return this.dateSeance;
    }

    public Assemble dateSeance(String dateSeance) {
        this.setDateSeance(dateSeance);
        return this;
    }

    public void setDateSeance(String dateSeance) {
        this.dateSeance = dateSeance;
    }

    public String getLieuSeance() {
        return this.lieuSeance;
    }

    public Assemble lieuSeance(String lieuSeance) {
        this.setLieuSeance(lieuSeance);
        return this;
    }

    public void setLieuSeance(String lieuSeance) {
        this.lieuSeance = lieuSeance;
    }

    public String getMatriculeMembreRecoit() {
        return this.matriculeMembreRecoit;
    }

    public Assemble matriculeMembreRecoit(String matriculeMembreRecoit) {
        this.setMatriculeMembreRecoit(matriculeMembreRecoit);
        return this;
    }

    public void setMatriculeMembreRecoit(String matriculeMembreRecoit) {
        this.matriculeMembreRecoit = matriculeMembreRecoit;
    }

    public NatureAssemble getNature() {
        return this.nature;
    }

    public Assemble nature(NatureAssemble nature) {
        this.setNature(nature);
        return this;
    }

    public void setNature(NatureAssemble nature) {
        this.nature = nature;
    }

    public String getCompteRendu() {
        return this.compteRendu;
    }

    public Assemble compteRendu(String compteRendu) {
        this.setCompteRendu(compteRendu);
        return this;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public String getResumeAssemble() {
        return this.resumeAssemble;
    }

    public Assemble resumeAssemble(String resumeAssemble) {
        this.setResumeAssemble(resumeAssemble);
        return this;
    }

    public void setResumeAssemble(String resumeAssemble) {
        this.resumeAssemble = resumeAssemble;
    }

    public String getDocumentCRPath() {
        return this.documentCRPath;
    }

    public Assemble documentCRPath(String documentCRPath) {
        this.setDocumentCRPath(documentCRPath);
        return this;
    }

    public void setDocumentCRPath(String documentCRPath) {
        this.documentCRPath = documentCRPath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assemble)) {
            return false;
        }
        return id != null && id.equals(((Assemble) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assemble{" +
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
