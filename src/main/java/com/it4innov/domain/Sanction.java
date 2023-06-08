package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Sanction.
 */
@Entity
@Table(name = "sanction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sanction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @NotNull
    @Column(name = "matricule_adherent", nullable = false)
    private String matriculeAdherent;

    @Column(name = "date_sanction")
    private LocalDate dateSanction;

    @Column(name = "motif_sanction")
    private String motifSanction;

    @Column(name = "description")
    private String description;

    @Column(name = "code_activite")
    private String codeActivite;

    @OneToMany(mappedBy = "sanction")
    @JsonIgnoreProperties(value = { "sanction" }, allowSetters = true)
    private Set<PaiementSanction> paiementSanctions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "sanctions" }, allowSetters = true)
    private SanctionConfiguration sanctionConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sanction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Sanction libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMatriculeAdherent() {
        return this.matriculeAdherent;
    }

    public Sanction matriculeAdherent(String matriculeAdherent) {
        this.setMatriculeAdherent(matriculeAdherent);
        return this;
    }

    public void setMatriculeAdherent(String matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public LocalDate getDateSanction() {
        return this.dateSanction;
    }

    public Sanction dateSanction(LocalDate dateSanction) {
        this.setDateSanction(dateSanction);
        return this;
    }

    public void setDateSanction(LocalDate dateSanction) {
        this.dateSanction = dateSanction;
    }

    public String getMotifSanction() {
        return this.motifSanction;
    }

    public Sanction motifSanction(String motifSanction) {
        this.setMotifSanction(motifSanction);
        return this;
    }

    public void setMotifSanction(String motifSanction) {
        this.motifSanction = motifSanction;
    }

    public String getDescription() {
        return this.description;
    }

    public Sanction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeActivite() {
        return this.codeActivite;
    }

    public Sanction codeActivite(String codeActivite) {
        this.setCodeActivite(codeActivite);
        return this;
    }

    public void setCodeActivite(String codeActivite) {
        this.codeActivite = codeActivite;
    }

    public Set<PaiementSanction> getPaiementSanctions() {
        return this.paiementSanctions;
    }

    public void setPaiementSanctions(Set<PaiementSanction> paiementSanctions) {
        if (this.paiementSanctions != null) {
            this.paiementSanctions.forEach(i -> i.setSanction(null));
        }
        if (paiementSanctions != null) {
            paiementSanctions.forEach(i -> i.setSanction(this));
        }
        this.paiementSanctions = paiementSanctions;
    }

    public Sanction paiementSanctions(Set<PaiementSanction> paiementSanctions) {
        this.setPaiementSanctions(paiementSanctions);
        return this;
    }

    public Sanction addPaiementSanction(PaiementSanction paiementSanction) {
        this.paiementSanctions.add(paiementSanction);
        paiementSanction.setSanction(this);
        return this;
    }

    public Sanction removePaiementSanction(PaiementSanction paiementSanction) {
        this.paiementSanctions.remove(paiementSanction);
        paiementSanction.setSanction(null);
        return this;
    }

    public SanctionConfiguration getSanctionConfig() {
        return this.sanctionConfig;
    }

    public void setSanctionConfig(SanctionConfiguration sanctionConfiguration) {
        this.sanctionConfig = sanctionConfiguration;
    }

    public Sanction sanctionConfig(SanctionConfiguration sanctionConfiguration) {
        this.setSanctionConfig(sanctionConfiguration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sanction)) {
            return false;
        }
        return id != null && id.equals(((Sanction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sanction{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", dateSanction='" + getDateSanction() + "'" +
            ", motifSanction='" + getMotifSanction() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeActivite='" + getCodeActivite() + "'" +
            "}";
    }
}
