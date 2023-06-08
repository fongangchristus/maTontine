package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A SessionTontine.
 */
@Entity
@Table(name = "session_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SessionTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @OneToMany(mappedBy = "sessionTontine")
    @JsonIgnoreProperties(value = { "paiementTontines", "sessionTontine", "compteTontine" }, allowSetters = true)
    private Set<CotisationTontine> cotisationTontines = new HashSet<>();

    @OneToMany(mappedBy = "tontine")
    @JsonIgnoreProperties(value = { "paiementTontines", "tontine", "compteTontine" }, allowSetters = true)
    private Set<DecaissementTontine> decaissementTontines = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sessionTontines", "gestionnaireTontines" }, allowSetters = true)
    private Tontine tontine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SessionTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public SessionTontine libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public SessionTontine dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public SessionTontine dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Set<CotisationTontine> getCotisationTontines() {
        return this.cotisationTontines;
    }

    public void setCotisationTontines(Set<CotisationTontine> cotisationTontines) {
        if (this.cotisationTontines != null) {
            this.cotisationTontines.forEach(i -> i.setSessionTontine(null));
        }
        if (cotisationTontines != null) {
            cotisationTontines.forEach(i -> i.setSessionTontine(this));
        }
        this.cotisationTontines = cotisationTontines;
    }

    public SessionTontine cotisationTontines(Set<CotisationTontine> cotisationTontines) {
        this.setCotisationTontines(cotisationTontines);
        return this;
    }

    public SessionTontine addCotisationTontine(CotisationTontine cotisationTontine) {
        this.cotisationTontines.add(cotisationTontine);
        cotisationTontine.setSessionTontine(this);
        return this;
    }

    public SessionTontine removeCotisationTontine(CotisationTontine cotisationTontine) {
        this.cotisationTontines.remove(cotisationTontine);
        cotisationTontine.setSessionTontine(null);
        return this;
    }

    public Set<DecaissementTontine> getDecaissementTontines() {
        return this.decaissementTontines;
    }

    public void setDecaissementTontines(Set<DecaissementTontine> decaissementTontines) {
        if (this.decaissementTontines != null) {
            this.decaissementTontines.forEach(i -> i.setTontine(null));
        }
        if (decaissementTontines != null) {
            decaissementTontines.forEach(i -> i.setTontine(this));
        }
        this.decaissementTontines = decaissementTontines;
    }

    public SessionTontine decaissementTontines(Set<DecaissementTontine> decaissementTontines) {
        this.setDecaissementTontines(decaissementTontines);
        return this;
    }

    public SessionTontine addDecaissementTontine(DecaissementTontine decaissementTontine) {
        this.decaissementTontines.add(decaissementTontine);
        decaissementTontine.setTontine(this);
        return this;
    }

    public SessionTontine removeDecaissementTontine(DecaissementTontine decaissementTontine) {
        this.decaissementTontines.remove(decaissementTontine);
        decaissementTontine.setTontine(null);
        return this;
    }

    public Tontine getTontine() {
        return this.tontine;
    }

    public void setTontine(Tontine tontine) {
        this.tontine = tontine;
    }

    public SessionTontine tontine(Tontine tontine) {
        this.setTontine(tontine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionTontine)) {
            return false;
        }
        return id != null && id.equals(((SessionTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SessionTontine{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
