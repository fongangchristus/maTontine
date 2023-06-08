package com.it4innov.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PaiementBanque.
 */
@Entity
@Table(name = "paiement_banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementBanque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reference_paiement", nullable = false)
    private String referencePaiement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaiementBanque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencePaiement() {
        return this.referencePaiement;
    }

    public PaiementBanque referencePaiement(String referencePaiement) {
        this.setReferencePaiement(referencePaiement);
        return this;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementBanque)) {
            return false;
        }
        return id != null && id.equals(((PaiementBanque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementBanque{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            "}";
    }
}
