package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A PaiementTontine.
 */
@Entity
@Table(name = "paiement_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaiementTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference_paiement")
    private String referencePaiement;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paiementTontines", "sessionTontine", "compteTontine" }, allowSetters = true)
    private CotisationTontine cotisationTontine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "paiementTontines", "tontine", "compteTontine" }, allowSetters = true)
    private DecaissementTontine decaissementTontine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaiementTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencePaiement() {
        return this.referencePaiement;
    }

    public PaiementTontine referencePaiement(String referencePaiement) {
        this.setReferencePaiement(referencePaiement);
        return this;
    }

    public void setReferencePaiement(String referencePaiement) {
        this.referencePaiement = referencePaiement;
    }

    public CotisationTontine getCotisationTontine() {
        return this.cotisationTontine;
    }

    public void setCotisationTontine(CotisationTontine cotisationTontine) {
        this.cotisationTontine = cotisationTontine;
    }

    public PaiementTontine cotisationTontine(CotisationTontine cotisationTontine) {
        this.setCotisationTontine(cotisationTontine);
        return this;
    }

    public DecaissementTontine getDecaissementTontine() {
        return this.decaissementTontine;
    }

    public void setDecaissementTontine(DecaissementTontine decaissementTontine) {
        this.decaissementTontine = decaissementTontine;
    }

    public PaiementTontine decaissementTontine(DecaissementTontine decaissementTontine) {
        this.setDecaissementTontine(decaissementTontine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaiementTontine)) {
            return false;
        }
        return id != null && id.equals(((PaiementTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaiementTontine{" +
            "id=" + getId() +
            ", referencePaiement='" + getReferencePaiement() + "'" +
            "}";
    }
}
