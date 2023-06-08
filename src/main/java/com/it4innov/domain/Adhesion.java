package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutAdhesion;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Adhesion.
 */
@Entity
@Table(name = "adhesion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Adhesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut_adhesion")
    private StatutAdhesion statutAdhesion;

    @Column(name = "matricule_personne")
    private String matriculePersonne;

    @Column(name = "date_debut_adhesion")
    private Instant dateDebutAdhesion;

    @Column(name = "date_fin_adhesion")
    private Instant dateFinAdhesion;

    @OneToMany(mappedBy = "adhesion")
    @JsonIgnoreProperties(value = { "adhesion" }, allowSetters = true)
    private Set<FormuleAdhesion> formuleAdhesions = new HashSet<>();

    @OneToMany(mappedBy = "adhesion")
    @JsonIgnoreProperties(value = { "adhesion" }, allowSetters = true)
    private Set<PaiementAdhesion> paiementAdhesions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Adhesion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatutAdhesion getStatutAdhesion() {
        return this.statutAdhesion;
    }

    public Adhesion statutAdhesion(StatutAdhesion statutAdhesion) {
        this.setStatutAdhesion(statutAdhesion);
        return this;
    }

    public void setStatutAdhesion(StatutAdhesion statutAdhesion) {
        this.statutAdhesion = statutAdhesion;
    }

    public String getMatriculePersonne() {
        return this.matriculePersonne;
    }

    public Adhesion matriculePersonne(String matriculePersonne) {
        this.setMatriculePersonne(matriculePersonne);
        return this;
    }

    public void setMatriculePersonne(String matriculePersonne) {
        this.matriculePersonne = matriculePersonne;
    }

    public Instant getDateDebutAdhesion() {
        return this.dateDebutAdhesion;
    }

    public Adhesion dateDebutAdhesion(Instant dateDebutAdhesion) {
        this.setDateDebutAdhesion(dateDebutAdhesion);
        return this;
    }

    public void setDateDebutAdhesion(Instant dateDebutAdhesion) {
        this.dateDebutAdhesion = dateDebutAdhesion;
    }

    public Instant getDateFinAdhesion() {
        return this.dateFinAdhesion;
    }

    public Adhesion dateFinAdhesion(Instant dateFinAdhesion) {
        this.setDateFinAdhesion(dateFinAdhesion);
        return this;
    }

    public void setDateFinAdhesion(Instant dateFinAdhesion) {
        this.dateFinAdhesion = dateFinAdhesion;
    }

    public Set<FormuleAdhesion> getFormuleAdhesions() {
        return this.formuleAdhesions;
    }

    public void setFormuleAdhesions(Set<FormuleAdhesion> formuleAdhesions) {
        if (this.formuleAdhesions != null) {
            this.formuleAdhesions.forEach(i -> i.setAdhesion(null));
        }
        if (formuleAdhesions != null) {
            formuleAdhesions.forEach(i -> i.setAdhesion(this));
        }
        this.formuleAdhesions = formuleAdhesions;
    }

    public Adhesion formuleAdhesions(Set<FormuleAdhesion> formuleAdhesions) {
        this.setFormuleAdhesions(formuleAdhesions);
        return this;
    }

    public Adhesion addFormuleAdhesion(FormuleAdhesion formuleAdhesion) {
        this.formuleAdhesions.add(formuleAdhesion);
        formuleAdhesion.setAdhesion(this);
        return this;
    }

    public Adhesion removeFormuleAdhesion(FormuleAdhesion formuleAdhesion) {
        this.formuleAdhesions.remove(formuleAdhesion);
        formuleAdhesion.setAdhesion(null);
        return this;
    }

    public Set<PaiementAdhesion> getPaiementAdhesions() {
        return this.paiementAdhesions;
    }

    public void setPaiementAdhesions(Set<PaiementAdhesion> paiementAdhesions) {
        if (this.paiementAdhesions != null) {
            this.paiementAdhesions.forEach(i -> i.setAdhesion(null));
        }
        if (paiementAdhesions != null) {
            paiementAdhesions.forEach(i -> i.setAdhesion(this));
        }
        this.paiementAdhesions = paiementAdhesions;
    }

    public Adhesion paiementAdhesions(Set<PaiementAdhesion> paiementAdhesions) {
        this.setPaiementAdhesions(paiementAdhesions);
        return this;
    }

    public Adhesion addPaiementAdhesion(PaiementAdhesion paiementAdhesion) {
        this.paiementAdhesions.add(paiementAdhesion);
        paiementAdhesion.setAdhesion(this);
        return this;
    }

    public Adhesion removePaiementAdhesion(PaiementAdhesion paiementAdhesion) {
        this.paiementAdhesions.remove(paiementAdhesion);
        paiementAdhesion.setAdhesion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adhesion)) {
            return false;
        }
        return id != null && id.equals(((Adhesion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Adhesion{" +
            "id=" + getId() +
            ", statutAdhesion='" + getStatutAdhesion() + "'" +
            ", matriculePersonne='" + getMatriculePersonne() + "'" +
            ", dateDebutAdhesion='" + getDateDebutAdhesion() + "'" +
            ", dateFinAdhesion='" + getDateFinAdhesion() + "'" +
            "}";
    }
}
