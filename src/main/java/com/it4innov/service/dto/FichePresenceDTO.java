package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.FichePresence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichePresenceDTO implements Serializable {

    private Long id;

    private String libelle;

    private Instant dateJour;

    private String description;

    private String codeAssemble;

    private String codeEvenement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Instant getDateJour() {
        return dateJour;
    }

    public void setDateJour(Instant dateJour) {
        this.dateJour = dateJour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeAssemble() {
        return codeAssemble;
    }

    public void setCodeAssemble(String codeAssemble) {
        this.codeAssemble = codeAssemble;
    }

    public String getCodeEvenement() {
        return codeEvenement;
    }

    public void setCodeEvenement(String codeEvenement) {
        this.codeEvenement = codeEvenement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichePresenceDTO)) {
            return false;
        }

        FichePresenceDTO fichePresenceDTO = (FichePresenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fichePresenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichePresenceDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateJour='" + getDateJour() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeAssemble='" + getCodeAssemble() + "'" +
            ", codeEvenement='" + getCodeEvenement() + "'" +
            "}";
    }
}
