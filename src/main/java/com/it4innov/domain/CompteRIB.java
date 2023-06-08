package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * The Employee entity.
 */
@Entity
@Table(name = "compte_rib")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteRIB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * /IBAN attribute.
     */
    @Column(name = "iban")
    private String iban;

    @Column(name = "titulaire_compte")
    private String titulaireCompte;

    @Column(name = "verifier")
    private Boolean verifier;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "adresse", "contacts", "compteRIBS", "historiquePersonnes", "presences", "fonctionAdherents" },
        allowSetters = true
    )
    private Personne adherent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompteRIB id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return this.iban;
    }

    public CompteRIB iban(String iban) {
        this.setIban(iban);
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTitulaireCompte() {
        return this.titulaireCompte;
    }

    public CompteRIB titulaireCompte(String titulaireCompte) {
        this.setTitulaireCompte(titulaireCompte);
        return this;
    }

    public void setTitulaireCompte(String titulaireCompte) {
        this.titulaireCompte = titulaireCompte;
    }

    public Boolean getVerifier() {
        return this.verifier;
    }

    public CompteRIB verifier(Boolean verifier) {
        this.setVerifier(verifier);
        return this;
    }

    public void setVerifier(Boolean verifier) {
        this.verifier = verifier;
    }

    public Personne getAdherent() {
        return this.adherent;
    }

    public void setAdherent(Personne personne) {
        this.adherent = personne;
    }

    public CompteRIB adherent(Personne personne) {
        this.setAdherent(personne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteRIB)) {
            return false;
        }
        return id != null && id.equals(((CompteRIB) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteRIB{" +
            "id=" + getId() +
            ", iban='" + getIban() + "'" +
            ", titulaireCompte='" + getTitulaireCompte() + "'" +
            ", verifier='" + getVerifier() + "'" +
            "}";
    }
}
