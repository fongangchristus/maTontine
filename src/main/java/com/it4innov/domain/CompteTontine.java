package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CompteTontine.
 */
@Entity
@Table(name = "compte_tontine")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteTontine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Inscription à la tontine est materialisé par la creation d'un compte tontine\nUn membre peux avoir plusieurs comptes/noms tontines
     */
    @Column(name = "etat_de_compte")
    private Boolean etatDeCompte;

    @Column(name = "libele")
    private String libele;

    @Column(name = "odre_beneficiere")
    private Integer odreBeneficiere;

    @NotNull
    @Column(name = "matricule_compte", nullable = false)
    private String matriculeCompte;

    @NotNull
    @Column(name = "matricule_menbre", nullable = false)
    private String matriculeMenbre;

    @JsonIgnoreProperties(value = { "sessionTontines", "gestionnaireTontines" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Tontine tontine;

    @OneToMany(mappedBy = "compteTontine")
    @JsonIgnoreProperties(value = { "paiementTontines", "sessionTontine", "compteTontine" }, allowSetters = true)
    private Set<CotisationTontine> cotisationTontines = new HashSet<>();

    @OneToMany(mappedBy = "compteTontine")
    @JsonIgnoreProperties(value = { "paiementTontines", "tontine", "compteTontine" }, allowSetters = true)
    private Set<DecaissementTontine> decaissementTontines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompteTontine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEtatDeCompte() {
        return this.etatDeCompte;
    }

    public CompteTontine etatDeCompte(Boolean etatDeCompte) {
        this.setEtatDeCompte(etatDeCompte);
        return this;
    }

    public void setEtatDeCompte(Boolean etatDeCompte) {
        this.etatDeCompte = etatDeCompte;
    }

    public String getLibele() {
        return this.libele;
    }

    public CompteTontine libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Integer getOdreBeneficiere() {
        return this.odreBeneficiere;
    }

    public CompteTontine odreBeneficiere(Integer odreBeneficiere) {
        this.setOdreBeneficiere(odreBeneficiere);
        return this;
    }

    public void setOdreBeneficiere(Integer odreBeneficiere) {
        this.odreBeneficiere = odreBeneficiere;
    }

    public String getMatriculeCompte() {
        return this.matriculeCompte;
    }

    public CompteTontine matriculeCompte(String matriculeCompte) {
        this.setMatriculeCompte(matriculeCompte);
        return this;
    }

    public void setMatriculeCompte(String matriculeCompte) {
        this.matriculeCompte = matriculeCompte;
    }

    public String getMatriculeMenbre() {
        return this.matriculeMenbre;
    }

    public CompteTontine matriculeMenbre(String matriculeMenbre) {
        this.setMatriculeMenbre(matriculeMenbre);
        return this;
    }

    public void setMatriculeMenbre(String matriculeMenbre) {
        this.matriculeMenbre = matriculeMenbre;
    }

    public Tontine getTontine() {
        return this.tontine;
    }

    public void setTontine(Tontine tontine) {
        this.tontine = tontine;
    }

    public CompteTontine tontine(Tontine tontine) {
        this.setTontine(tontine);
        return this;
    }

    public Set<CotisationTontine> getCotisationTontines() {
        return this.cotisationTontines;
    }

    public void setCotisationTontines(Set<CotisationTontine> cotisationTontines) {
        if (this.cotisationTontines != null) {
            this.cotisationTontines.forEach(i -> i.setCompteTontine(null));
        }
        if (cotisationTontines != null) {
            cotisationTontines.forEach(i -> i.setCompteTontine(this));
        }
        this.cotisationTontines = cotisationTontines;
    }

    public CompteTontine cotisationTontines(Set<CotisationTontine> cotisationTontines) {
        this.setCotisationTontines(cotisationTontines);
        return this;
    }

    public CompteTontine addCotisationTontine(CotisationTontine cotisationTontine) {
        this.cotisationTontines.add(cotisationTontine);
        cotisationTontine.setCompteTontine(this);
        return this;
    }

    public CompteTontine removeCotisationTontine(CotisationTontine cotisationTontine) {
        this.cotisationTontines.remove(cotisationTontine);
        cotisationTontine.setCompteTontine(null);
        return this;
    }

    public Set<DecaissementTontine> getDecaissementTontines() {
        return this.decaissementTontines;
    }

    public void setDecaissementTontines(Set<DecaissementTontine> decaissementTontines) {
        if (this.decaissementTontines != null) {
            this.decaissementTontines.forEach(i -> i.setCompteTontine(null));
        }
        if (decaissementTontines != null) {
            decaissementTontines.forEach(i -> i.setCompteTontine(this));
        }
        this.decaissementTontines = decaissementTontines;
    }

    public CompteTontine decaissementTontines(Set<DecaissementTontine> decaissementTontines) {
        this.setDecaissementTontines(decaissementTontines);
        return this;
    }

    public CompteTontine addDecaissementTontine(DecaissementTontine decaissementTontine) {
        this.decaissementTontines.add(decaissementTontine);
        decaissementTontine.setCompteTontine(this);
        return this;
    }

    public CompteTontine removeDecaissementTontine(DecaissementTontine decaissementTontine) {
        this.decaissementTontines.remove(decaissementTontine);
        decaissementTontine.setCompteTontine(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteTontine)) {
            return false;
        }
        return id != null && id.equals(((CompteTontine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteTontine{" +
            "id=" + getId() +
            ", etatDeCompte='" + getEtatDeCompte() + "'" +
            ", libele='" + getLibele() + "'" +
            ", odreBeneficiere=" + getOdreBeneficiere() +
            ", matriculeCompte='" + getMatriculeCompte() + "'" +
            ", matriculeMenbre='" + getMatriculeMenbre() + "'" +
            "}";
    }
}
