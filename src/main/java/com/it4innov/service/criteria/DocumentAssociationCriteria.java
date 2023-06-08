package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.DocumentAssociation} entity. This class is used
 * in {@link com.it4innov.web.rest.DocumentAssociationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /document-associations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAssociationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codeDocument;

    private StringFilter libele;

    private StringFilter description;

    private LocalDateFilter dateEnregistrement;

    private LocalDateFilter dateArchivage;

    private StringFilter version;

    private LongFilter associationId;

    private Boolean distinct;

    public DocumentAssociationCriteria() {}

    public DocumentAssociationCriteria(DocumentAssociationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codeDocument = other.codeDocument == null ? null : other.codeDocument.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateEnregistrement = other.dateEnregistrement == null ? null : other.dateEnregistrement.copy();
        this.dateArchivage = other.dateArchivage == null ? null : other.dateArchivage.copy();
        this.version = other.version == null ? null : other.version.copy();
        this.associationId = other.associationId == null ? null : other.associationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DocumentAssociationCriteria copy() {
        return new DocumentAssociationCriteria(this);
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

    public StringFilter getCodeDocument() {
        return codeDocument;
    }

    public StringFilter codeDocument() {
        if (codeDocument == null) {
            codeDocument = new StringFilter();
        }
        return codeDocument;
    }

    public void setCodeDocument(StringFilter codeDocument) {
        this.codeDocument = codeDocument;
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

    public LocalDateFilter getDateEnregistrement() {
        return dateEnregistrement;
    }

    public LocalDateFilter dateEnregistrement() {
        if (dateEnregistrement == null) {
            dateEnregistrement = new LocalDateFilter();
        }
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDateFilter dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public LocalDateFilter getDateArchivage() {
        return dateArchivage;
    }

    public LocalDateFilter dateArchivage() {
        if (dateArchivage == null) {
            dateArchivage = new LocalDateFilter();
        }
        return dateArchivage;
    }

    public void setDateArchivage(LocalDateFilter dateArchivage) {
        this.dateArchivage = dateArchivage;
    }

    public StringFilter getVersion() {
        return version;
    }

    public StringFilter version() {
        if (version == null) {
            version = new StringFilter();
        }
        return version;
    }

    public void setVersion(StringFilter version) {
        this.version = version;
    }

    public LongFilter getAssociationId() {
        return associationId;
    }

    public LongFilter associationId() {
        if (associationId == null) {
            associationId = new LongFilter();
        }
        return associationId;
    }

    public void setAssociationId(LongFilter associationId) {
        this.associationId = associationId;
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
        final DocumentAssociationCriteria that = (DocumentAssociationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codeDocument, that.codeDocument) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateEnregistrement, that.dateEnregistrement) &&
            Objects.equals(dateArchivage, that.dateArchivage) &&
            Objects.equals(version, that.version) &&
            Objects.equals(associationId, that.associationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeDocument, libele, description, dateEnregistrement, dateArchivage, version, associationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAssociationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codeDocument != null ? "codeDocument=" + codeDocument + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (dateEnregistrement != null ? "dateEnregistrement=" + dateEnregistrement + ", " : "") +
            (dateArchivage != null ? "dateArchivage=" + dateArchivage + ", " : "") +
            (version != null ? "version=" + version + ", " : "") +
            (associationId != null ? "associationId=" + associationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
