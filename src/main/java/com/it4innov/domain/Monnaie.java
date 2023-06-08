package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Monnaie.
 */
@Entity
@Table(name = "monnaie")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Monnaie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libele")
    private String libele;

    @OneToMany(mappedBy = "monnaie")
    @JsonIgnoreProperties(value = { "exercises", "documentAssociations", "monnaie" }, allowSetters = true)
    private Set<Association> associations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Monnaie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public Monnaie libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Set<Association> getAssociations() {
        return this.associations;
    }

    public void setAssociations(Set<Association> associations) {
        if (this.associations != null) {
            this.associations.forEach(i -> i.setMonnaie(null));
        }
        if (associations != null) {
            associations.forEach(i -> i.setMonnaie(this));
        }
        this.associations = associations;
    }

    public Monnaie associations(Set<Association> associations) {
        this.setAssociations(associations);
        return this;
    }

    public Monnaie addAssociation(Association association) {
        this.associations.add(association);
        association.setMonnaie(this);
        return this;
    }

    public Monnaie removeAssociation(Association association) {
        this.associations.remove(association);
        association.setMonnaie(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Monnaie)) {
            return false;
        }
        return id != null && id.equals(((Monnaie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Monnaie{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            "}";
    }
}
