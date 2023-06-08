package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A GestionnaireTontine.
 */
@Entity
@Table(name = "gestionnaire_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "matricule_adherent")
    private String matriculeAdherent;

    @Column(name = "code_tontine")
    private String codeTontine;

    @Column(name = "date_prise_fonction")
    private LocalDate datePriseFonction;

    @Column(name = "date_fin_fonction")
    private LocalDate dateFinFonction;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sessionTontines", "gestionnaireTontines" }, allowSetters = true)
    private Tontine tontine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GestionnaireTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherent() {
        return this.matriculeAdherent;
    }

    public GestionnaireTontine matriculeAdherent(String matriculeAdherent) {
        this.setMatriculeAdherent(matriculeAdherent);
        return this;
    }

    public void setMatriculeAdherent(String matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public String getCodeTontine() {
        return this.codeTontine;
    }

    public GestionnaireTontine codeTontine(String codeTontine) {
        this.setCodeTontine(codeTontine);
        return this;
    }

    public void setCodeTontine(String codeTontine) {
        this.codeTontine = codeTontine;
    }

    public LocalDate getDatePriseFonction() {
        return this.datePriseFonction;
    }

    public GestionnaireTontine datePriseFonction(LocalDate datePriseFonction) {
        this.setDatePriseFonction(datePriseFonction);
        return this;
    }

    public void setDatePriseFonction(LocalDate datePriseFonction) {
        this.datePriseFonction = datePriseFonction;
    }

    public LocalDate getDateFinFonction() {
        return this.dateFinFonction;
    }

    public GestionnaireTontine dateFinFonction(LocalDate dateFinFonction) {
        this.setDateFinFonction(dateFinFonction);
        return this;
    }

    public void setDateFinFonction(LocalDate dateFinFonction) {
        this.dateFinFonction = dateFinFonction;
    }

    public Tontine getTontine() {
        return this.tontine;
    }

    public void setTontine(Tontine tontine) {
        this.tontine = tontine;
    }

    public GestionnaireTontine tontine(Tontine tontine) {
        this.setTontine(tontine);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GestionnaireTontine)) {
            return false;
        }
        return id != null && id.equals(((GestionnaireTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireTontine{" +
            "id=" + getId() +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", codeTontine='" + getCodeTontine() + "'" +
            ", datePriseFonction='" + getDatePriseFonction() + "'" +
            ", dateFinFonction='" + getDateFinFonction() + "'" +
            "}";
    }
}
