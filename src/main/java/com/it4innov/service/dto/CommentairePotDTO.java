package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.CommentairePot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentairePotDTO implements Serializable {

    private Long id;

    @NotNull
    private String matriculeContributeur;

    @NotNull
    private String identifiantPot;

    private String contenu;

    private Instant dateComentaire;

    private PotDTO pot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeContributeur() {
        return matriculeContributeur;
    }

    public void setMatriculeContributeur(String matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public String getIdentifiantPot() {
        return identifiantPot;
    }

    public void setIdentifiantPot(String identifiantPot) {
        this.identifiantPot = identifiantPot;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDateComentaire() {
        return dateComentaire;
    }

    public void setDateComentaire(Instant dateComentaire) {
        this.dateComentaire = dateComentaire;
    }

    public PotDTO getPot() {
        return pot;
    }

    public void setPot(PotDTO pot) {
        this.pot = pot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentairePotDTO)) {
            return false;
        }

        CommentairePotDTO commentairePotDTO = (CommentairePotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentairePotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentairePotDTO{" +
            "id=" + getId() +
            ", matriculeContributeur='" + getMatriculeContributeur() + "'" +
            ", identifiantPot='" + getIdentifiantPot() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", dateComentaire='" + getDateComentaire() + "'" +
            ", pot=" + getPot() +
            "}";
    }
}
