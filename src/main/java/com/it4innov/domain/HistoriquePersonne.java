package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A HistoriquePersonne.
 */
@Entity
@Table(name = "historique_personne")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HistoriquePersonne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_action")
    private Instant dateAction;

    @NotNull
    @Column(name = "matricule_personne", nullable = false)
    private String matriculePersonne;

    @Column(name = "action")
    private String action;

    @Column(name = "result")
    private String result;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "adresse", "contacts", "compteRIBS", "historiquePersonnes", "presences", "fonctionAdherents" },
        allowSetters = true
    )
    private Personne personne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HistoriquePersonne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateAction() {
        return this.dateAction;
    }

    public HistoriquePersonne dateAction(Instant dateAction) {
        this.setDateAction(dateAction);
        return this;
    }

    public void setDateAction(Instant dateAction) {
        this.dateAction = dateAction;
    }

    public String getMatriculePersonne() {
        return this.matriculePersonne;
    }

    public HistoriquePersonne matriculePersonne(String matriculePersonne) {
        this.setMatriculePersonne(matriculePersonne);
        return this;
    }

    public void setMatriculePersonne(String matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public String getAction() {
        return this.action;
    }

    public HistoriquePersonne action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResult() {
        return this.result;
    }

    public HistoriquePersonne result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return this.description;
    }

    public HistoriquePersonne description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Personne getPersonne() {
        return this.personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public HistoriquePersonne personne(Personne personne) {
        this.setPersonne(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoriquePersonne)) {
            return false;
        }
        return id != null && id.equals(((HistoriquePersonne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoriquePersonne{" +
            "id=" + getId() +
            ", dateAction='" + getDateAction() + "'" +
            ", matriculePersonne='" + getMatriculePersonne() + "'" +
            ", action='" + getAction() + "'" +
            ", result='" + getResult() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
