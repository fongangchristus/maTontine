package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.DocumentAssociation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentAssociationDTO implements Serializable {

    private Long id;

    private String codeDocument;

    private String libele;

    private String description;

    private LocalDate dateEnregistrement;

    private LocalDate dateArchivage;

    private String version;

    private AssociationDTO association;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeDocument() {
        return codeDocument;
    }

    public void setCodeDocument(String codeDocument) {
        this.codeDocument = codeDocument;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDate dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public LocalDate getDateArchivage() {
        return dateArchivage;
    }

    public void setDateArchivage(LocalDate dateArchivage) {
        this.dateArchivage = dateArchivage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AssociationDTO getAssociation() {
        return association;
    }

    public void setAssociation(AssociationDTO association) {
        this.association = association;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentAssociationDTO)) {
            return false;
        }

        DocumentAssociationDTO documentAssociationDTO = (DocumentAssociationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentAssociationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentAssociationDTO{" +
            "id=" + getId() +
            ", codeDocument='" + getCodeDocument() + "'" +
            ", libele='" + getLibele() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateEnregistrement='" + getDateEnregistrement() + "'" +
            ", dateArchivage='" + getDateArchivage() + "'" +
            ", version='" + getVersion() + "'" +
            ", association=" + getAssociation() +
            "}";
    }
}
