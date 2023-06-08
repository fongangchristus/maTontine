package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.HistoriquePersonne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriquePersonneDTO implements Serializable {

    private Long id;

    private Instant dateAction;

    @NotNull
    private String matriculePersonne;

    private String action;

    private String result;

    private String description;

    private PersonneDTO personne;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAction() {
        return dateAction;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    public String getMatriculePersonne() {
        return matriculePersonne;
    }

    public void setMatriculePersonne(String matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PersonneDTO getPersonne() {
        return personne;
    }

    public void setPersonne(PersonneDTO personne) {
        this.personne = personne;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriquePersonneDTO)) {
            return false;
        }

        HistoriquePersonneDTO historiquePersonneDTO = (HistoriquePersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, historiquePersonneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriquePersonneDTO{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", matriculePersonne='" + getMatriculePersonne() + "'" +
            ", action='" + getAction() + "'" +
            ", result='" + getResult() + "'" +
            ", description='" + getDescription() + "'" +
            ", personne=" + getPersonne() +
            "}";
    }
}
