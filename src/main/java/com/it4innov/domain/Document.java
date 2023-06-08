package com.it4innov.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "doc_key")
    private String docKey;

    @Column(name = "path")
    private String path;

    @Column(name = "type_document")
    private String typeDocument;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Document libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDocKey() {
        return this.docKey;
    }

    public Document docKey(String docKey) {
        this.setDocKey(docKey);
        return this;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getPath() {
        return this.path;
    }

    public Document path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTypeDocument() {
        return this.typeDocument;
    }

    public Document typeDocument(String typeDocument) {
        this.setTypeDocument(typeDocument);
        return this;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", docKey='" + getDocKey() + "'" +
            ", path='" + getPath() + "'" +
            ", typeDocument='" + getTypeDocument() + "'" +
            "}";
    }
}
