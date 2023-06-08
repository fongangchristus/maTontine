package com.it4innov.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.Fonction} entity.
 */
@Schema(description = "Fonctions des adhérent (membre, président sécrétaire ....)\n@author The JHipster team.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FonctionDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FonctionDTO)) {
            return false;
        }

        FonctionDTO fonctionDTO = (FonctionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fonctionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FonctionDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
