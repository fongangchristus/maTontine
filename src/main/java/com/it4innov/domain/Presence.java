package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutPresence;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Presence.
 */
@Entity
@Table(name = "presence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Presence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule_adherant", nullable = false)
    private String matriculeAdherant;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_presence")
    private StatutPresence statutPresence;

    @ManyToOne
    @JsonIgnoreProperties(value = { "presences" }, allowSetters = true)
    private FichePresence fichePresence;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "adresse", "contacts", "compteRIBS", "historiquePersonnes", "presences", "fonctionAdherents" },
        allowSetters = true
    )
    private Personne adherant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Presence id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherant() {
        return this.matriculeAdherant;
    }

    public Presence matriculeAdherant(String matriculeAdherant) {
        this.setMatriculeAdherant(matriculeAdherant);
        return this;
    }

    public void setMatriculeAdherant(String matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public StatutPresence getStatutPresence() {
        return this.statutPresence;
    }

    public Presence statutPresence(StatutPresence statutPresence) {
        this.setStatutPresence(statutPresence);
        return this;
    }

    public void setStatutPresence(StatutPresence statutPresence) {
        this.statutPresence = statutPresence;
    }

    public FichePresence getFichePresence() {
        return this.fichePresence;
    }

    public void setFichePresence(FichePresence fichePresence) {
        this.fichePresence = fichePresence;
    }

    public Presence fichePresence(FichePresence fichePresence) {
        this.setFichePresence(fichePresence);
        return this;
    }

    public Personne getAdherant() {
        return this.adherant;
    }

    public void setAdherant(Personne personne) {
        this.adherant = personne;
    }

    public Presence adherant(Personne personne) {
        this.setAdherant(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presence)) {
            return false;
        }
        return id != null && id.equals(((Presence) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presence{" +
            "id=" + getId() +
            ", matriculeAdherant='" + getMatriculeAdherant() + "'" +
            ", statutPresence='" + getStatutPresence() + "'" +
            "}";
    }
}
