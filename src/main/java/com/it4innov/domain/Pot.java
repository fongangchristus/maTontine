package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.StatutPot;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Pot.
 */
@Entity
@Table(name = "pot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libele")
    private String libele;

    @NotNull
    @Column(name = "codepot", nullable = false)
    private String codepot;

    @Column(name = "montant_cible")
    private Double montantCible;

    @Column(name = "description")
    private String description;

    @Column(name = "date_debut_collecte")
    private LocalDate dateDebutCollecte;

    @Column(name = "date_fin_collecte")
    private LocalDate dateFinCollecte;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutPot statut;

    @OneToMany(mappedBy = "pot")
    @JsonIgnoreProperties(value = { "pot" }, allowSetters = true)
    private Set<ContributionPot> contributionPots = new HashSet<>();

    @OneToMany(mappedBy = "pot")
    @JsonIgnoreProperties(value = { "pot" }, allowSetters = true)
    private Set<CommentairePot> commentairePots = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "pots" }, allowSetters = true)
    private TypePot typePot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return this.libele;
    }

    public Pot libele(String libele) {
        this.setLibele(libele);
        return this;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getCodepot() {
        return this.codepot;
    }

    public Pot codepot(String codepot) {
        this.setCodepot(codepot);
        return this;
    }

    public void setCodepot(String codepot) {
        this.codepot = codepot;
    }

    public Double getMontantCible() {
        return this.montantCible;
    }

    public Pot montantCible(Double montantCible) {
        this.setMontantCible(montantCible);
        return this;
    }

    public void setMontantCible(Double montantCible) {
        this.montantCible = montantCible;
    }

    public String getDescription() {
        return this.description;
    }

    public Pot description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebutCollecte() {
        return this.dateDebutCollecte;
    }

    public Pot dateDebutCollecte(LocalDate dateDebutCollecte) {
        this.setDateDebutCollecte(dateDebutCollecte);
        return this;
    }

    public void setDateDebutCollecte(LocalDate dateDebutCollecte) {
        this.dateDebutCollecte = dateDebutCollecte;
    }

    public LocalDate getDateFinCollecte() {
        return this.dateFinCollecte;
    }

    public Pot dateFinCollecte(LocalDate dateFinCollecte) {
        this.setDateFinCollecte(dateFinCollecte);
        return this;
    }

    public void setDateFinCollecte(LocalDate dateFinCollecte) {
        this.dateFinCollecte = dateFinCollecte;
    }

    public StatutPot getStatut() {
        return this.statut;
    }

    public Pot statut(StatutPot statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutPot statut) {
        this.statut = statut;
    }

    public Set<ContributionPot> getContributionPots() {
        return this.contributionPots;
    }

    public void setContributionPots(Set<ContributionPot> contributionPots) {
        if (this.contributionPots != null) {
            this.contributionPots.forEach(i -> i.setPot(null));
        }
        if (contributionPots != null) {
            contributionPots.forEach(i -> i.setPot(this));
        }
        this.contributionPots = contributionPots;
    }

    public Pot contributionPots(Set<ContributionPot> contributionPots) {
        this.setContributionPots(contributionPots);
        return this;
    }

    public Pot addContributionPot(ContributionPot contributionPot) {
        this.contributionPots.add(contributionPot);
        contributionPot.setPot(this);
        return this;
    }

    public Pot removeContributionPot(ContributionPot contributionPot) {
        this.contributionPots.remove(contributionPot);
        contributionPot.setPot(null);
        return this;
    }

    public Set<CommentairePot> getCommentairePots() {
        return this.commentairePots;
    }

    public void setCommentairePots(Set<CommentairePot> commentairePots) {
        if (this.commentairePots != null) {
            this.commentairePots.forEach(i -> i.setPot(null));
        }
        if (commentairePots != null) {
            commentairePots.forEach(i -> i.setPot(this));
        }
        this.commentairePots = commentairePots;
    }

    public Pot commentairePots(Set<CommentairePot> commentairePots) {
        this.setCommentairePots(commentairePots);
        return this;
    }

    public Pot addCommentairePot(CommentairePot commentairePot) {
        this.commentairePots.add(commentairePot);
        commentairePot.setPot(this);
        return this;
    }

    public Pot removeCommentairePot(CommentairePot commentairePot) {
        this.commentairePots.remove(commentairePot);
        commentairePot.setPot(null);
        return this;
    }

    public TypePot getTypePot() {
        return this.typePot;
    }

    public void setTypePot(TypePot typePot) {
        this.typePot = typePot;
    }

    public Pot typePot(TypePot typePot) {
        this.setTypePot(typePot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pot)) {
            return false;
        }
        return id != null && id.equals(((Pot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pot{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", codepot='" + getCodepot() + "'" +
            ", montantCible=" + getMontantCible() +
            ", description='" + getDescription() + "'" +
            ", dateDebutCollecte='" + getDateDebutCollecte() + "'" +
            ", dateFinCollecte='" + getDateFinCollecte() + "'" +
            ", statut='" + getStatut() + "'" +
            "}";
    }
}
