package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A TypeEvenement.
 */
@Entity
@Table(name = "type_evenement")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeEvenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libele")
    private String libele;

    @Column(name = "observation")
    private String observation;

    @OneToMany(mappedBy = "typeEvenement")
    @JsonIgnoreProperties(value = { "typeEvenement" }, allowSetters = true)
    private Set<Evenement> evenements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeEvenement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public TypeEvenement libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getObservation() {
        return this.observation;
    }

    public TypeEvenement observation(String observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Set<Evenement> getEvenements() {
        return this.evenements;
    }

    public void setEvenements(Set<Evenement> evenements) {
        if (this.evenements != null) {
            this.evenements.forEach(i -> i.setTypeEvenement(null));
        }
        if (evenements != null) {
            evenements.forEach(i -> i.setTypeEvenement(this));
        }
        this.evenements = evenements;
    }

    public TypeEvenement evenements(Set<Evenement> evenements) {
        this.setEvenements(evenements);
        return this;
    }

    public TypeEvenement addEvenement(Evenement evenement) {
        this.evenements.add(evenement);
        evenement.setTypeEvenement(this);
        return this;
    }

    public TypeEvenement removeEvenement(Evenement evenement) {
        this.evenements.remove(evenement);
        evenement.setTypeEvenement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeEvenement)) {
            return false;
        }
        return id != null && id.equals(((TypeEvenement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeEvenement{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
