package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CompteTontine} entity. This class is used
 * in {@link com.it4innov.web.rest.CompteTontineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compte-tontines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteTontineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter etatDeCompte;

    private StringFilter libele;

    private IntegerFilter odreBeneficiere;

    private StringFilter matriculeCompte;

    private StringFilter matriculeMenbre;

    private LongFilter tontineId;

    private LongFilter cotisationTontineId;

    private LongFilter decaissementTontineId;

    private Boolean distinct;

    public CompteTontineCriteria() {}

    public CompteTontineCriteria(CompteTontineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.etatDeCompte = other.etatDeCompte == null ? null : other.etatDeCompte.copy();
        this.libele = other.libele == null ? null : other.libele.copy();
        this.odreBeneficiere = other.odreBeneficiere == null ? null : other.odreBeneficiere.copy();
        this.matriculeCompte = other.matriculeCompte == null ? null : other.matriculeCompte.copy();
        this.matriculeMenbre = other.matriculeMenbre == null ? null : other.matriculeMenbre.copy();
        this.tontineId = other.tontineId == null ? null : other.tontineId.copy();
        this.cotisationTontineId = other.cotisationTontineId == null ? null : other.cotisationTontineId.copy();
        this.decaissementTontineId = other.decaissementTontineId == null ? null : other.decaissementTontineId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompteTontineCriteria copy() {
        return new CompteTontineCriteria(this);
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

    public BooleanFilter getEtatDeCompte() {
        return etatDeCompte;
    }

    public BooleanFilter etatDeCompte() {
        if (etatDeCompte == null) {
            etatDeCompte = new BooleanFilter();
        }
        return etatDeCompte;
    }

    public void setEtatDeCompte(BooleanFilter etatDeCompte) {
        this.etatDeCompte = etatDeCompte;
    }

    public StringFilter getLibele() {
        return libele;
    }

    public StringFilter libele() {
        if (libele == null) {
            libele = new StringFilter();
        }
        return libele;
    }

    public void setLibele(StringFilter libele) {
        this.libele = libele;
    }

    public IntegerFilter getOdreBeneficiere() {
        return odreBeneficiere;
    }

    public IntegerFilter odreBeneficiere() {
        if (odreBeneficiere == null) {
            odreBeneficiere = new IntegerFilter();
        }
        return odreBeneficiere;
    }

    public void setOdreBeneficiere(IntegerFilter odreBeneficiere) {
        this.odreBeneficiere = odreBeneficiere;
    }

    public StringFilter getMatriculeCompte() {
        return matriculeCompte;
    }

    public StringFilter matriculeCompte() {
        if (matriculeCompte == null) {
            matriculeCompte = new StringFilter();
        }
        return matriculeCompte;
    }

    public void setMatriculeCompte(StringFilter matriculeCompte) {
        this.matriculeCompte = matriculeCompte;
    }

    public StringFilter getMatriculeMenbre() {
        return matriculeMenbre;
    }

    public StringFilter matriculeMenbre() {
        if (matriculeMenbre == null) {
            matriculeMenbre = new StringFilter();
        }
        return matriculeMenbre;
    }

    public void setMatriculeMenbre(StringFilter matriculeMenbre) {
        this.matriculeMenbre = matriculeMenbre;
    }

    public LongFilter getTontineId() {
        return tontineId;
    }

    public LongFilter tontineId() {
        if (tontineId == null) {
            tontineId = new LongFilter();
        }
        return tontineId;
    }

    public void setTontineId(LongFilter tontineId) {
        this.tontineId = tontineId;
    }

    public LongFilter getCotisationTontineId() {
        return cotisationTontineId;
    }

    public LongFilter cotisationTontineId() {
        if (cotisationTontineId == null) {
            cotisationTontineId = new LongFilter();
        }
        return cotisationTontineId;
    }

    public void setCotisationTontineId(LongFilter cotisationTontineId) {
        this.cotisationTontineId = cotisationTontineId;
    }

    public LongFilter getDecaissementTontineId() {
        return decaissementTontineId;
    }

    public LongFilter decaissementTontineId() {
        if (decaissementTontineId == null) {
            decaissementTontineId = new LongFilter();
        }
        return decaissementTontineId;
    }

    public void setDecaissementTontineId(LongFilter decaissementTontineId) {
        this.decaissementTontineId = decaissementTontineId;
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
        final CompteTontineCriteria that = (CompteTontineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(etatDeCompte, that.etatDeCompte) &&
            Objects.equals(libele, that.libele) &&
            Objects.equals(odreBeneficiere, that.odreBeneficiere) &&
            Objects.equals(matriculeCompte, that.matriculeCompte) &&
            Objects.equals(matriculeMenbre, that.matriculeMenbre) &&
            Objects.equals(tontineId, that.tontineId) &&
            Objects.equals(cotisationTontineId, that.cotisationTontineId) &&
            Objects.equals(decaissementTontineId, that.decaissementTontineId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            etatDeCompte,
            libele,
            odreBeneficiere,
            matriculeCompte,
            matriculeMenbre,
            tontineId,
            cotisationTontineId,
            decaissementTontineId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteTontineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (etatDeCompte != null ? "etatDeCompte=" + etatDeCompte + ", " : "") +
            (libele != null ? "libele=" + libele + ", " : "") +
            (odreBeneficiere != null ? "odreBeneficiere=" + odreBeneficiere + ", " : "") +
            (matriculeCompte != null ? "matriculeCompte=" + matriculeCompte + ", " : "") +
            (matriculeMenbre != null ? "matriculeMenbre=" + matriculeMenbre + ", " : "") +
            (tontineId != null ? "tontineId=" + tontineId + ", " : "") +
            (cotisationTontineId != null ? "cotisationTontineId=" + cotisationTontineId + ", " : "") +
            (decaissementTontineId != null ? "decaissementTontineId=" + decaissementTontineId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
