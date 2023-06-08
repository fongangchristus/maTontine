package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PaiementAdhesion.
 */
@Entity
@Table(name = "paiement_adhesion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementAdhesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reference_paiement", nullable = false)
    private String referencePaiement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formuleAdhesions", "paiementAdhesions" }, allowSetters = true)
    private Adhesion adhesion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaiementAdhesion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencePaiement() {
        return this.referencePaiement;
    }

    public PaiementAdhesion referencePaiement(String referencePaiement) {
        this.setReferencePaiement(referencePaiement);
        return this;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public Adhesion getAdhesion() {
        return this.adhesion;
    }

    public void setAdhesion(Adhesion adhesion) {
        this.adhesion = adhesion;
    }

    public PaiementAdhesion adhesion(Adhesion adhesion) {
        this.setAdhesion(adhesion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementAdhesion)) {
            return false;
        }
        return id != null && id.equals(((PaiementAdhesion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementAdhesion{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            "}";
    }
}
