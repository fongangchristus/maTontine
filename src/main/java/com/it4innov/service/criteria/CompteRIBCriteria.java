package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.CompteRIB} entity. This class is used
 * in {@link com.it4innov.web.rest.CompteRIBResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /compte-ribs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteRIBCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter iban;

    private StringFilter titulaireCompte;

    private BooleanFilter verifier;

    private LongFilter adherentId;

    private Boolean distinct;

    public CompteRIBCriteria() {}

    public CompteRIBCriteria(CompteRIBCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.iban = other.iban == null ? null : other.iban.copy();
        this.titulaireCompte = other.titulaireCompte == null ? null : other.titulaireCompte.copy();
        this.verifier = other.verifier == null ? null : other.verifier.copy();
        this.adherentId = other.adherentId == null ? null : other.adherentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompteRIBCriteria copy() {
        return new CompteRIBCriteria(this);
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

    public StringFilter getIban() {
        return iban;
    }

    public StringFilter iban() {
        if (iban == null) {
            iban = new StringFilter();
        }
        return iban;
    }

    public void setIban(StringFilter iban) {
        this.iban = iban;
    }

    public StringFilter getTitulaireCompte() {
        return titulaireCompte;
    }

    public StringFilter titulaireCompte() {
        if (titulaireCompte == null) {
            titulaireCompte = new StringFilter();
        }
        return titulaireCompte;
    }

    public void setTitulaireCompte(StringFilter titulaireCompte) {
        this.titulaireCompte = titulaireCompte;
    }

    public BooleanFilter getVerifier() {
        return verifier;
    }

    public BooleanFilter verifier() {
        if (verifier == null) {
            verifier = new BooleanFilter();
        }
        return verifier;
    }

    public void setVerifier(BooleanFilter verifier) {
        this.verifier = verifier;
    }

    public LongFilter getAdherentId() {
        return adherentId;
    }

    public LongFilter adherentId() {
        if (adherentId == null) {
            adherentId = new LongFilter();
        }
        return adherentId;
    }

    public void setAdherentId(LongFilter adherentId) {
        this.adherentId = adherentId;
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
        final CompteRIBCriteria that = (CompteRIBCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(iban, that.iban) &&
            Objects.equals(titulaireCompte, that.titulaireCompte) &&
            Objects.equals(verifier, that.verifier) &&
            Objects.equals(adherentId, that.adherentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iban, titulaireCompte, verifier, adherentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteRIBCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (iban != null ? "iban=" + iban + ", " : "") +
            (titulaireCompte != null ? "titulaireCompte=" + titulaireCompte + ", " : "") +
            (verifier != null ? "verifier=" + verifier + ", " : "") +
            (adherentId != null ? "adherentId=" + adherentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
