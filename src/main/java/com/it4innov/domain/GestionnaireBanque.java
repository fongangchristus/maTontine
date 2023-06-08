package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A GestionnaireBanque.
 */
@Entity
@Table(name = "gestionnaire_banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireBanque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule_membre")
    private String matriculeMembre;

    @ManyToOne
    @JsonIgnoreProperties(value = { "compteBanques", "gestionnaireBanques" }, allowSetters = true)
    private Banque banque;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GestionnaireBanque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeMembre() {
        return this.matriculeMembre;
    }

    public GestionnaireBanque matriculeMembre(String matriculeMembre) {
        this.setMatriculeMembre(matriculeMembre);
        return this;
    }

    public void setMatriculeMembre(String matriculeMembre) {
        this.matriculeMembre = matriculeMembre;
    }

    public Banque getBanque() {
        return this.banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public GestionnaireBanque banque(Banque banque) {
        this.setBanque(banque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GestionnaireBanque)) {
            return false;
        }
        return id != null && id.equals(((GestionnaireBanque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireBanque{" +
            "id=" + getId() +
            ", matriculeMembre='" + getMatriculeMembre() + "'" +
            "}";
    }
}
