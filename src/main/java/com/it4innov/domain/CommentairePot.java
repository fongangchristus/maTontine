package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A CommentairePot.
 */
@Entity
@Table(name = "commentaire_pot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentairePot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "matricule_contributeur", nullable = false)
    private String matriculeContributeur;

    @NotNull
    @Column(name = "identifiant_pot", nullable = false)
    private String identifiantPot;

    @Column(name = "contenu")
    private String contenu;

    @Column(name = "date_comentaire")
    private Instant dateComentaire;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contributionPots", "commentairePots", "typePot" }, allowSetters = true)
    private Pot pot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CommentairePot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeContributeur() {
        return this.matriculeContributeur;
    }

    public CommentairePot matriculeContributeur(String matriculeContributeur) {
        this.setMatriculeContributeur(matriculeContributeur);
        return this;
    }

    public void setMatriculeContributeur(String matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public String getIdentifiantPot() {
        return this.identifiantPot;
    }

    public CommentairePot identifiantPot(String identifiantPot) {
        this.setIdentifiantPot(identifiantPot);
        return this;
    }

    public void setIdentifiantPot(String identifiantPot) {
        this.identifiantPot = identifiantPot;
    }

    public String getContenu() {
        return this.contenu;
    }

    public CommentairePot contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateComentaire() {
        return this.dateComentaire;
    }

    public CommentairePot dateComentaire(Instant dateComentaire) {
        this.setDateComentaire(dateComentaire);
        return this;
    }

    public void setDateComentaire(Instant dateComentaire) {
        this.dateComentaire = dateComentaire;
    }

    public Pot getPot() {
        return this.pot;
    }

    public void setPot(Pot pot) {
        this.pot = pot;
    }

    public CommentairePot pot(Pot pot) {
        this.setPot(pot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentairePot)) {
            return false;
        }
        return id != null && id.equals(((CommentairePot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentairePot{" +
            "id=" + getId() +
            ", matriculeContributeur='" + getMatriculeContributeur() + "'" +
            ", identifiantPot='" + getIdentifiantPot() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", dateComentaire='" + getDateComentaire() + "'" +
            "}";
    }
}
