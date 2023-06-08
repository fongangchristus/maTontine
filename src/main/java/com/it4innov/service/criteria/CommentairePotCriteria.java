package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CommentairePot} entity. This class is used
 * in {@link com.it4innov.web.rest.CommentairePotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commentaire-pots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentairePotCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matriculeContributeur;

    private StringFilter identifiantPot;

    private StringFilter contenu;

    private InstantFilter dateComentaire;

    private LongFilter potId;

    private Boolean distinct;

    public CommentairePotCriteria() {}

    public CommentairePotCriteria(CommentairePotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matriculeContributeur = other.matriculeContributeur == null ? null : other.matriculeContributeur.copy();
        this.identifiantPot = other.identifiantPot == null ? null : other.identifiantPot.copy();
        this.contenu = other.contenu == null ? null : other.contenu.copy();
        this.dateComentaire = other.dateComentaire == null ? null : other.dateComentaire.copy();
        this.potId = other.potId == null ? null : other.potId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CommentairePotCriteria copy() {
        return new CommentairePotCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMatriculeContributeur() {
        return matriculeContributeur;
    }

    public StringFilter matriculeContributeur() {
        if (matriculeContributeur == null) {
            matriculeContributeur = new StringFilter();
        }
        return matriculeContributeur;
    }

    public void setMatriculeContributeur(StringFilter matriculeContributeur) {
        this.matriculeContributeur = matriculeContributeur;
    }

    public StringFilter getIdentifiantPot() {
        return identifiantPot;
    }

    public StringFilter identifiantPot() {
        if (identifiantPot == null) {
            identifiantPot = new StringFilter();
        }
        return identifiantPot;
    }

    public void setIdentifiantPot(StringFilter identifiantPot) {
        this.identifiantPot = identifiantPot;
    }

    public StringFilter getContenu() {
        return contenu;
    }

    public StringFilter contenu() {
        if (contenu == null) {
            contenu = new StringFilter();
        }
        return contenu;
    }

    public void setContenu(StringFilter contenu) {
        this.contenu = contenu;
    }

    public InstantFilter getDateComentaire() {
        return dateComentaire;
    }

    public InstantFilter dateComentaire() {
        if (dateComentaire == null) {
            dateComentaire = new InstantFilter();
        }
        return dateComentaire;
    }

    public void setDateComentaire(InstantFilter dateComentaire) {
        this.dateComentaire = dateComentaire;
    }

    public LongFilter getPotId() {
        return potId;
    }

    public LongFilter potId() {
        if (potId == null) {
            potId = new LongFilter();
        }
        return potId;
    }

    public void setPotId(LongFilter potId) {
        this.potId = potId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentairePotCriteria that = (CommentairePotCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(matriculeContributeur, that.matriculeContributeur) &&
            Objects.equals(identifiantPot, that.identifiantPot) &&
            Objects.equals(contenu, that.contenu) &&
            Objects.equals(dateComentaire, that.dateComentaire) &&
            Objects.equals(potId, that.potId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, matriculeContributeur, identifiantPot, contenu, dateComentaire, potId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentairePotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (matriculeContributeur != null ? "matriculeContributeur=" + matriculeContributeur + ", " : "") +
            (identifiantPot != null ? "identifiantPot=" + identifiantPot + ", " : "") +
            (contenu != null ? "contenu=" + contenu + ", " : "") +
            (dateComentaire != null ? "dateComentaire=" + dateComentaire + ", " : "") +
            (potId != null ? "potId=" + potId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
