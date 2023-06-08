package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.EtatCotisation;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A CotisationTontine.
 */
@Entity
@Table(name = "cotisation_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CotisationTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Un membre peux avoir plusieurs comptes/noms tontines
     */
    @Column(name = "montant_cotise")
    private Double montantCotise;

    @Column(name = "piece_justif_path")
    private String pieceJustifPath;

    @Column(name = "date_cotisation")
    private LocalDate dateCotisation;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(name = "commentaire")
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat")
    private EtatCotisation etat;

    @OneToMany(mappedBy = "cotisationTontine")
    @JsonIgnoreProperties(value = { "cotisationTontine", "decaissementTontine" }, allowSetters = true)
    private Set<PaiementTontine> paiementTontines = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "cotisationTontines", "decaissementTontines", "tontine" }, allowSetters = true)
    private SessionTontine sessionTontine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tontine", "cotisationTontines", "decaissementTontines" }, allowSetters = true)
    private CompteTontine compteTontine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CotisationTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontantCotise() {
        return this.montantCotise;
    }

    public CotisationTontine montantCotise(Double montantCotise) {
        this.setMontantCotise(montantCotise);
        return this;
    }

    public void setMontantCotise(Double montantCotise) {
        this.montantCotise = montantCotise;
    }

    public String getPieceJustifPath() {
        return this.pieceJustifPath;
    }

    public CotisationTontine pieceJustifPath(String pieceJustifPath) {
        this.setPieceJustifPath(pieceJustifPath);
        return this;
    }

    public void setPieceJustifPath(String pieceJustifPath) {
        this.pieceJustifPath = pieceJustifPath;
    }

    public LocalDate getDateCotisation() {
        return this.dateCotisation;
    }

    public CotisationTontine dateCotisation(LocalDate dateCotisation) {
        this.setDateCotisation(dateCotisation);
        return this;
    }

    public void setDateCotisation(LocalDate dateCotisation) {
        this.dateCotisation = dateCotisation;
    }

    public LocalDate getDateValidation() {
        return this.dateValidation;
    }

    public CotisationTontine dateValidation(LocalDate dateValidation) {
        this.setDateValidation(dateValidation);
        return this;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public CotisationTontine commentaire(String commentaire) {
        this.setCommentaire(commentaire);
        return this;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public EtatCotisation getEtat() {
        return this.etat;
    }

    public CotisationTontine etat(EtatCotisation etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(EtatCotisation etat) {
        this.etat = etat;
    }

    public Set<PaiementTontine> getPaiementTontines() {
        return this.paiementTontines;
    }

    public void setPaiementTontines(Set<PaiementTontine> paiementTontines) {
        if (this.paiementTontines != null) {
            this.paiementTontines.forEach(i -> i.setCotisationTontine(null));
        }
        if (paiementTontines != null) {
            paiementTontines.forEach(i -> i.setCotisationTontine(this));
        }
        this.paiementTontines = paiementTontines;
    }

    public CotisationTontine paiementTontines(Set<PaiementTontine> paiementTontines) {
        this.setPaiementTontines(paiementTontines);
        return this;
    }

    public CotisationTontine addPaiementTontine(PaiementTontine paiementTontine) {
        this.paiementTontines.add(paiementTontine);
        paiementTontine.setCotisationTontine(this);
        return this;
    }

    public CotisationTontine removePaiementTontine(PaiementTontine paiementTontine) {
        this.paiementTontines.remove(paiementTontine);
        paiementTontine.setCotisationTontine(null);
        return this;
    }

    public SessionTontine getSessionTontine() {
        return this.sessionTontine;
    }

    public void setSessionTontine(SessionTontine sessionTontine) {
        this.sessionTontine = sessionTontine;
    }

    public CotisationTontine sessionTontine(SessionTontine sessionTontine) {
        this.setSessionTontine(sessionTontine);
        return this;
    }

    public CompteTontine getCompteTontine() {
        return this.compteTontine;
    }

    public void setCompteTontine(CompteTontine compteTontine) {
        this.compteTontine = compteTontine;
    }

    public CotisationTontine compteTontine(CompteTontine compteTontine) {
        this.setCompteTontine(compteTontine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CotisationTontine)) {
            return false;
        }
        return id != null && id.equals(((CotisationTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CotisationTontine{" +
            "id=" + getId() +
            ", montantCotise=" + getMontantCotise() +
            ", pieceJustifPath='" + getPieceJustifPath() + "'" +
            ", dateCotisation='" + getDateCotisation() + "'" +
            ", dateValidation='" + getDateValidation() + "'" +
            ", commentaire='" + getCommentaire() + "'" +
            ", etat='" + getEtat() + "'" +
            "}";
    }
}
