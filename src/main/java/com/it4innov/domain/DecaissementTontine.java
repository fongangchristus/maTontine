package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A DecaissementTontine.
 */
@Entity
@Table(name = "decaissement_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecaissementTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @Column(name = "libele")
    private String libele;

    @Column(name = "date_decaissement")
    private LocalDate dateDecaissement;

    @Column(name = "montant_decaisse")
    private Double montantDecaisse;

    @Column(name = "commentaire")
    private String commentaire;

    @OneToMany(mappedBy = "decaissementTontine")
    @JsonIgnoreProperties(value = { "cotisationTontine", "decaissementTontine" }, allowSetters = true)
    private Set<PaiementTontine> paiementTontines = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cotisationTontines", "decaissementTontines", "tontine" }, allowSetters = true)
    private SessionTontine tontine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tontine", "cotisationTontines", "decaissementTontines" }, allowSetters = true)
    private CompteTontine compteTontine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DecaissementTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public DecaissementTontine libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public LocalDate getDateDecaissement() {
        return this.dateDecaissement;
    }

    public DecaissementTontine dateDecaissement(LocalDate dateDecaissement) {
        this.setDateDecaissement(dateDecaissement);
        return this;
    }

    public void setDateDecaissement(LocalDate dateDecaissement) {
        this.dateDecaissement = dateDecaissement;
    }

    public Double getMontantDecaisse() {
        return this.montantDecaisse;
    }

    public DecaissementTontine montantDecaisse(Double montantDecaisse) {
        this.setMontantDecaisse(montantDecaisse);
        return this;
    }

    public void setMontantDecaisse(Double montantDecaisse) {
        this.montantDecaisse = montantDecaisse;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public DecaissementTontine commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Set<PaiementTontine> getPaiementTontines() {
        return this.paiementTontines;
    }

    public void setPaiementTontines(Set<PaiementTontine> paiementTontines) {
        if (this.paiementTontines != null) {
            this.paiementTontines.forEach(i -> i.setDecaissementTontine(null));
        }
        if (paiementTontines != null) {
            paiementTontines.forEach(i -> i.setDecaissementTontine(this));
        }
        this.paiementTontines = paiementTontines;
    }

    public DecaissementTontine paiementTontines(Set<PaiementTontine> paiementTontines) {
        this.setPaiementTontines(paiementTontines);
        return this;
    }

    public DecaissementTontine addPaiementTontine(PaiementTontine paiementTontine) {
        this.paiementTontines.add(paiementTontine);
        paiementTontine.setDecaissementTontine(this);
        return this;
    }

    public DecaissementTontine removePaiementTontine(PaiementTontine paiementTontine) {
        this.paiementTontines.remove(paiementTontine);
        paiementTontine.setDecaissementTontine(null);
        return this;
    }

    public SessionTontine getTontine() {
        return this.tontine;
    }

    public void setTontine(SessionTontine sessionTontine) {
        this.tontine = sessionTontine;
    }

    public DecaissementTontine tontine(SessionTontine sessionTontine) {
        this.setTontine(sessionTontine);
        return this;
    }

    public CompteTontine getCompteTontine() {
        return this.compteTontine;
    }

    public void setCompteTontine(CompteTontine compteTontine) {
        this.compteTontine = compteTontine;
    }

    public DecaissementTontine compteTontine(CompteTontine compteTontine) {
        this.setCompteTontine(compteTontine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecaissementTontine)) {
            return false;
        }
        return id != null && id.equals(((DecaissementTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecaissementTontine{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", dateDecaissement='" + getDateDecaissement() + "'" +
            ", montantDecaisse=" + getMontantDecaisse() +
            ", commentaire='" + getCommentaire() + "'" +
            "}";
    }
}
