package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Banque.
 */
@Entity
@Table(name = "banque")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Banque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @Column(name = "date_ouverture")
    private Instant dateOuverture;

    @Column(name = "date_cloture")
    private Instant dateCloture;

    @OneToMany(mappedBy = "banque")
    @JsonIgnoreProperties(value = { "cotisationBanques", "decaisementBanques", "banque" }, allowSetters = true)
    private Set<CompteBanque> compteBanques = new HashSet<>();

    @OneToMany(mappedBy = "banque")
    @JsonIgnoreProperties(value = { "banque" }, allowSetters = true)
    private Set<GestionnaireBanque> gestionnaireBanques = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Banque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public Banque codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Banque libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return this.description;
    }

    public Banque description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateOuverture() {
        return this.dateOuverture;
    }

    public Banque dateOuverture(Instant dateOuverture) {
        this.setDateOuverture(dateOuverture);
        return this;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public Instant getDateCloture() {
        return this.dateCloture;
    }

    public Banque dateCloture(Instant dateCloture) {
        this.setDateCloture(dateCloture);
        return this;
    }

    public void setDateCloture(Instant dateCloture) {
        this.dateCloture = dateCloture;
    }

    public Set<CompteBanque> getCompteBanques() {
        return this.compteBanques;
    }

    public void setCompteBanques(Set<CompteBanque> compteBanques) {
        if (this.compteBanques != null) {
            this.compteBanques.forEach(i -> i.setBanque(null));
        }
        if (compteBanques != null) {
            compteBanques.forEach(i -> i.setBanque(this));
        }
        this.compteBanques = compteBanques;
    }

    public Banque compteBanques(Set<CompteBanque> compteBanques) {
        this.setCompteBanques(compteBanques);
        return this;
    }

    public Banque addCompteBanque(CompteBanque compteBanque) {
        this.compteBanques.add(compteBanque);
        compteBanque.setBanque(this);
        return this;
    }

    public Banque removeCompteBanque(CompteBanque compteBanque) {
        this.compteBanques.remove(compteBanque);
        compteBanque.setBanque(null);
        return this;
    }

    public Set<GestionnaireBanque> getGestionnaireBanques() {
        return this.gestionnaireBanques;
    }

    public void setGestionnaireBanques(Set<GestionnaireBanque> gestionnaireBanques) {
        if (this.gestionnaireBanques != null) {
            this.gestionnaireBanques.forEach(i -> i.setBanque(null));
        }
        if (gestionnaireBanques != null) {
            gestionnaireBanques.forEach(i -> i.setBanque(this));
        }
        this.gestionnaireBanques = gestionnaireBanques;
    }

    public Banque gestionnaireBanques(Set<GestionnaireBanque> gestionnaireBanques) {
        this.setGestionnaireBanques(gestionnaireBanques);
        return this;
    }

    public Banque addGestionnaireBanque(GestionnaireBanque gestionnaireBanque) {
        this.gestionnaireBanques.add(gestionnaireBanque);
        gestionnaireBanque.setBanque(this);
        return this;
    }

    public Banque removeGestionnaireBanque(GestionnaireBanque gestionnaireBanque) {
        this.gestionnaireBanques.remove(gestionnaireBanque);
        gestionnaireBanque.setBanque(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Banque)) {
            return false;
        }
        return id != null && id.equals(((Banque) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Banque{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateOuverture='" + getDateOuverture() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            "}";
    }
}
