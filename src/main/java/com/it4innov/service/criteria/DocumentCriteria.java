package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Document} entity. This class is used
 * in {@link com.it4innov.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private StringFilter docKey;

    private StringFilter path;

    private StringFilter typeDocument;

    private Boolean distinct;

    public DocumentCriteria() {}

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.docKey = other.docKey == null ? null : other.docKey.copy();
        this.path = other.path == null ? null : other.path.copy();
        this.typeDocument = other.typeDocument == null ? null : other.typeDocument.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getDocKey() {
        return docKey;
    }

    public StringFilter docKey() {
        if (docKey == null) {
            docKey = new StringFilter();
        }
        return docKey;
    }

    public void setDocKey(StringFilter docKey) {
        this.docKey = docKey;
    }

    public StringFilter getPath() {
        return path;
    }

    public StringFilter path() {
        if (path == null) {
            path = new StringFilter();
        }
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getTypeDocument() {
        return typeDocument;
    }

    public StringFilter typeDocument() {
        if (typeDocument == null) {
            typeDocument = new StringFilter();
        }
        return typeDocument;
    }

    public void setTypeDocument(StringFilter typeDocument) {
        this.typeDocument = typeDocument;
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
        final DocumentCriteria that = (DocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(docKey, that.docKey) &&
            Objects.equals(path, that.path) &&
            Objects.equals(typeDocument, that.typeDocument) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, libelle, docKey, path, typeDocument, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (docKey != null ? "docKey=" + docKey + ", " : "") +
            (path != null ? "path=" + path + ", " : "") +
            (typeDocument != null ? "typeDocument=" + typeDocument + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
