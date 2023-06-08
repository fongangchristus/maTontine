package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Task entity.\n@author The JHipster team.
 */
@Entity
@Table(name = "fonction_adherent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FonctionAdherent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule_adherent", nullable = false)
    private String matriculeAdherent;

    @NotNull
    @Column(name = "code_fonction", nullable = false)
    private String codeFonction;

    @NotNull
    @Column(name = "date_prise_fonction", nullable = false)
    private LocalDate datePriseFonction;

    @Column(name = "date_fin_fonction")
    private LocalDate dateFinFonction;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "adresse", "contacts", "compteRIBS", "historiquePersonnes", "presences", "fonctionAdherents" },
        allowSetters = true
    )
    private Personne adherent;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fonctionAdherents" }, allowSetters = true)
    private Fonction fonction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FonctionAdherent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherent() {
        return this.matriculeAdherent;
    }

    public FonctionAdherent matriculeAdherent(String matriculeAdherent) {
        this.setMatriculeAdherent(matriculeAdherent);
        return this;
    }

    public void setMatriculeAdherent(String matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public String getCodeFonction() {
        return this.codeFonction;
    }

    public FonctionAdherent codeFonction(String codeFonction) {
        this.setCodeFonction(codeFonction);
        return this;
    }

    public void setCodeFonction(String codeFonction) {
        this.codeFonction = codeFonction;
    }

    public LocalDate getDatePriseFonction() {
        return this.datePriseFonction;
    }

    public FonctionAdherent datePriseFonction(LocalDate datePriseFonction) {
        this.setDatePriseFonction(datePriseFonction);
        return this;
    }

    public void setDatePriseFonction(LocalDate datePriseFonction) {
        this.datePriseFonction = datePriseFonction;
    }

    public LocalDate getDateFinFonction() {
        return this.dateFinFonction;
    }

    public FonctionAdherent dateFinFonction(LocalDate dateFinFonction) {
        this.setDateFinFonction(dateFinFonction);
        return this;
    }

    public void setDateFinFonction(LocalDate dateFinFonction) {
        this.dateFinFonction = dateFinFonction;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public FonctionAdherent actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Personne getAdherent() {
        return this.adherent;
    }

    public void setAdherent(Personne personne) {
        this.adherent = personne;
    }

    public FonctionAdherent adherent(Personne personne) {
        this.setAdherent(personne);
        return this;
    }

    public Fonction getFonction() {
        return this.fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public FonctionAdherent fonction(Fonction fonction) {
        this.setFonction(fonction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FonctionAdherent)) {
            return false;
        }
        return id != null && id.equals(((FonctionAdherent) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FonctionAdherent{" +
            "id=" + getId() +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", codeFonction='" + getCodeFonction() + "'" +
            ", datePriseFonction='" + getDatePriseFonction() + "'" +
            ", dateFinFonction='" + getDateFinFonction() + "'" +
            ", actif='" + getActif() + "'" +
            "}";
    }
}
