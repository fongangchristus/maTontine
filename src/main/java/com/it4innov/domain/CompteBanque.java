package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A CompteBanque.
 */
@Entity
@Table(name = "compte_banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteBanque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @Column(name = "matricule_adherant")
    private String matriculeAdherant;

    @Column(name = "montant_disponnible")
    private Double montantDisponnible;

    @OneToMany(mappedBy = "compteBanque")
    @JsonIgnoreProperties(value = { "compteBanque" }, allowSetters = true)
    private Set<CotisationBanque> cotisationBanques = new HashSet<>();

    @OneToMany(mappedBy = "compteBanque")
    @JsonIgnoreProperties(value = { "compteBanque" }, allowSetters = true)
    private Set<DecaisementBanque> decaisementBanques = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "compteBanques", "gestionnaireBanques" }, allowSetters = true)
    private Banque banque;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompteBanque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public CompteBanque libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return this.description;
    }

    public CompteBanque description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatriculeAdherant() {
        return this.matriculeAdherant;
    }

    public CompteBanque matriculeAdherant(String matriculeAdherant) {
        this.setMatriculeAdherant(matriculeAdherant);
        return this;
    }

    public void setMatriculeAdherant(String matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public Double getMontantDisponnible() {
        return this.montantDisponnible;
    }

    public CompteBanque montantDisponnible(Double montantDisponnible) {
        this.setMontantDisponnible(montantDisponnible);
        return this;
    }

    public void setMontantDisponnible(Double montantDisponnible) {
        this.montantDisponnible = montantDisponnible;
    }

    public Set<CotisationBanque> getCotisationBanques() {
        return this.cotisationBanques;
    }

    public void setCotisationBanques(Set<CotisationBanque> cotisationBanques) {
        if (this.cotisationBanques != null) {
            this.cotisationBanques.forEach(i -> i.setCompteBanque(null));
        }
        if (cotisationBanques != null) {
            cotisationBanques.forEach(i -> i.setCompteBanque(this));
        }
        this.cotisationBanques = cotisationBanques;
    }

    public CompteBanque cotisationBanques(Set<CotisationBanque> cotisationBanques) {
        this.setCotisationBanques(cotisationBanques);
        return this;
    }

    public CompteBanque addCotisationBanque(CotisationBanque cotisationBanque) {
        this.cotisationBanques.add(cotisationBanque);
        cotisationBanque.setCompteBanque(this);
        return this;
    }

    public CompteBanque removeCotisationBanque(CotisationBanque cotisationBanque) {
        this.cotisationBanques.remove(cotisationBanque);
        cotisationBanque.setCompteBanque(null);
        return this;
    }

    public Set<DecaisementBanque> getDecaisementBanques() {
        return this.decaisementBanques;
    }

    public void setDecaisementBanques(Set<DecaisementBanque> decaisementBanques) {
        if (this.decaisementBanques != null) {
            this.decaisementBanques.forEach(i -> i.setCompteBanque(null));
        }
        if (decaisementBanques != null) {
            decaisementBanques.forEach(i -> i.setCompteBanque(this));
        }
        this.decaisementBanques = decaisementBanques;
    }

    public CompteBanque decaisementBanques(Set<DecaisementBanque> decaisementBanques) {
        this.setDecaisementBanques(decaisementBanques);
        return this;
    }

    public CompteBanque addDecaisementBanque(DecaisementBanque decaisementBanque) {
        this.decaisementBanques.add(decaisementBanque);
        decaisementBanque.setCompteBanque(this);
        return this;
    }

    public CompteBanque removeDecaisementBanque(DecaisementBanque decaisementBanque) {
        this.decaisementBanques.remove(decaisementBanque);
        decaisementBanque.setCompteBanque(null);
        return this;
    }

    public Banque getBanque() {
        return this.banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    public CompteBanque banque(Banque banque) {
        this.setBanque(banque);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteBanque)) {
            return false;
        }
        return id != null && id.equals(((CompteBanque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteBanque{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", matriculeAdherant='" + getMatriculeAdherant() + "'" +
            ", montantDisponnible=" + getMontantDisponnible() +
            "}";
    }
}
