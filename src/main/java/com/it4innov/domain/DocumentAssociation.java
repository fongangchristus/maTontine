package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A DocumentAssociation.
 */
@Entity
@Table(name = "document_association")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAssociation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code_document")
    private String codeDocument;

    @Column(name = "libele")
    private String libele;

    @Column(name = "description")
    private String description;

    @Column(name = "date_enregistrement")
    private LocalDate dateEnregistrement;

    @Column(name = "date_archivage")
    private LocalDate dateArchivage;

    @Column(name = "version")
    private String version;

    @ManyToOne
    @JsonIgnoreProperties(value = { "exercises", "documentAssociations", "monnaie" }, allowSetters = true)
    private Association association;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DocumentAssociation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeDocument() {
        return this.codeDocument;
    }

    public DocumentAssociation codeDocument(String codeDocument) {
        this.setCodeDocument(codeDocument);
        return this;
    }

    public void setCodeDocument(String codeDocument) {
        this.codeDocument = codeDocument;
    }

    public String getLibele() {
        return this.libele;
    }

    public DocumentAssociation libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentAssociation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateEnregistrement() {
        return this.dateEnregistrement;
    }

    public DocumentAssociation dateEnregistrement(LocalDate dateEnregistrement) {
        this.setDateEnregistrement(dateEnregistrement);
        return this;
    }

    public void setDateEnregistrement(LocalDate dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public LocalDate getDateArchivage() {
        return this.dateArchivage;
    }

    public DocumentAssociation dateArchivage(LocalDate dateArchivage) {
        this.setDateArchivage(dateArchivage);
        return this;
    }

    public void setDateArchivage(LocalDate dateArchivage) {
        this.dateArchivage = dateArchivage;
    }

    public String getVersion() {
        return this.version;
    }

    public DocumentAssociation version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Association getAssociation() {
        return this.association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public DocumentAssociation association(Association association) {
        this.setAssociation(association);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentAssociation)) {
            return false;
        }
        return id != null && id.equals(((DocumentAssociation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAssociation{" +
            "id=" + getId() +
            ", codeDocument='" + getCodeDocument() + "'" +
            ", libele='" + getLibele() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            ", dateArchivage='" + getDateArchivage() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
