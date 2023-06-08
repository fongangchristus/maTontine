package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A FormuleAdhesion.
 */
@Entity
@Table(name = "formule_adhesion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormuleAdhesion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "adhesion_periodique")
    private Boolean adhesionPeriodique;

    @Column(name = "date_debut")
    private Instant dateDebut;

    @Column(name = "duree_adhesion_mois")
    private Integer dureeAdhesionMois;

    @Column(name = "montant_libre")
    private Boolean montantLibre;

    @Column(name = "description")
    private String description;

    @Column(name = "tarif")
    private Double tarif;

    @ManyToOne
    @JsonIgnoreProperties(value = { "formuleAdhesions", "paiementAdhesions" }, allowSetters = true)
    private Adhesion adhesion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormuleAdhesion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAdhesionPeriodique() {
        return this.adhesionPeriodique;
    }

    public FormuleAdhesion adhesionPeriodique(Boolean adhesionPeriodique) {
        this.setAdhesionPeriodique(adhesionPeriodique);
        return this;
    }

    public void setAdhesionPeriodique(Boolean adhesionPeriodique) {
        this.adhesionPeriodique = adhesionPeriodique;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public FormuleAdhesion dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Integer getDureeAdhesionMois() {
        return this.dureeAdhesionMois;
    }

    public FormuleAdhesion dureeAdhesionMois(Integer dureeAdhesionMois) {
        this.setDureeAdhesionMois(dureeAdhesionMois);
        return this;
    }

    public void setDureeAdhesionMois(Integer dureeAdhesionMois) {
        this.dureeAdhesionMois = dureeAdhesionMois;
    }

    public Boolean getMontantLibre() {
        return this.montantLibre;
    }

    public FormuleAdhesion montantLibre(Boolean montantLibre) {
        this.setMontantLibre(montantLibre);
        return this;
    }

    public void setMontantLibre(Boolean montantLibre) {
        this.montantLibre = montantLibre;
    }

    public String getDescription() {
        return this.description;
    }

    public FormuleAdhesion description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTarif() {
        return this.tarif;
    }

    public FormuleAdhesion tarif(Double tarif) {
        this.setTarif(tarif);
        return this;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public Adhesion getAdhesion() {
        return this.adhesion;
    }

    public void setAdhesion(Adhesion adhesion) {
        this.adhesion = adhesion;
    }

    public FormuleAdhesion adhesion(Adhesion adhesion) {
        this.setAdhesion(adhesion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormuleAdhesion)) {
            return false;
        }
        return id != null && id.equals(((FormuleAdhesion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormuleAdhesion{" +
            "id=" + getId() +
            ", adhesionPeriodique='" + getAdhesionPeriodique() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dureeAdhesionMois=" + getDureeAdhesionMois() +
            ", montantLibre='" + getMontantLibre() + "'" +
            ", description='" + getDescription() + "'" +
            ", tarif=" + getTarif() +
            "}";
    }
}
