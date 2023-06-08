package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TypePot.
 */
@Entity
@Table(name = "type_pot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypePot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "libele", nullable = false)
    private String libele;

    @Column(name = "descrption")
    private String descrption;

    @OneToMany(mappedBy = "typePot")
    @JsonIgnoreProperties(value = { "contributionPots", "commentairePots", "typePot" }, allowSetters = true)
    private Set<Pot> pots = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypePot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public TypePot libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getDescrption() {
        return this.descrption;
    }

    public TypePot descrption(String descrption) {
        this.setDescrption(descrption);
        return this;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public Set<Pot> getPots() {
        return this.pots;
    }

    public void setPots(Set<Pot> pots) {
        if (this.pots != null) {
            this.pots.forEach(i -> i.setTypePot(null));
        }
        if (pots != null) {
            pots.forEach(i -> i.setTypePot(this));
        }
        this.pots = pots;
    }

    public TypePot pots(Set<Pot> pots) {
        this.setPots(pots);
        return this;
    }

    public TypePot addPot(Pot pot) {
        this.pots.add(pot);
        pot.setTypePot(this);
        return this;
    }

    public TypePot removePot(Pot pot) {
        this.pots.remove(pot);
        pot.setTypePot(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePot)) {
            return false;
        }
        return id != null && id.equals(((TypePot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePot{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", descrption='" + getDescrption() + "'" +
            "}";
    }
}
