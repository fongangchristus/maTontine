package com.it4innov.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Sanction} entity. This class is used
 * in {@link com.it4innov.web.rest.SanctionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sanctions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter libelle;

    private StringFilter matriculeAdherent;

    private LocalDateFilter dateSanction;

    private StringFilter motifSanction;

    private StringFilter description;

    private StringFilter codeActivite;

    private LongFilter paiementSanctionId;

    private LongFilter sanctionConfigId;

    private Boolean distinct;

    public SanctionCriteria() {}

    public SanctionCriteria(SanctionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.libelle = other.libelle == null ? null : other.libelle.copy();
        this.matriculeAdherent = other.matriculeAdherent == null ? null : other.matriculeAdherent.copy();
        this.dateSanction = other.dateSanction == null ? null : other.dateSanction.copy();
        this.motifSanction = other.motifSanction == null ? null : other.motifSanction.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.codeActivite = other.codeActivite == null ? null : other.codeActivite.copy();
        this.paiementSanctionId = other.paiementSanctionId == null ? null : other.paiementSanctionId.copy();
        this.sanctionConfigId = other.sanctionConfigId == null ? null : other.sanctionConfigId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SanctionCriteria copy() {
        return new SanctionCriteria(this);
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

    public StringFilter getLibelle() {
        return libelle;
    }

    public StringFilter libelle() {
        if (libelle == null) {
            libelle = new StringFilter();
        }
        return libelle;
    }

    public void setLibelle(StringFilter libelle) {
        this.libelle = libelle;
    }

    public StringFilter getMatriculeAdherent() {
        return matriculeAdherent;
    }

    public StringFilter matriculeAdherent() {
        if (matriculeAdherent == null) {
            matriculeAdherent = new StringFilter();
        }
        return matriculeAdherent;
    }

    public void setMatriculeAdherent(StringFilter matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public LocalDateFilter getDateSanction() {
        return dateSanction;
    }

    public LocalDateFilter dateSanction() {
        if (dateSanction == null) {
            dateSanction = new LocalDateFilter();
        }
        return dateSanction;
    }

    public void setDateSanction(LocalDateFilter dateSanction) {
        this.dateSanction = dateSanction;
    }

    public StringFilter getMotifSanction() {
        return motifSanction;
    }

    public StringFilter motifSanction() {
        if (motifSanction == null) {
            motifSanction = new StringFilter();
        }
        return motifSanction;
    }

    public void setMotifSanction(StringFilter motifSanction) {
        this.motifSanction = motifSanction;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getCodeActivite() {
        return codeActivite;
    }

    public StringFilter codeActivite() {
        if (codeActivite == null) {
            codeActivite = new StringFilter();
        }
        return codeActivite;
    }

    public void setCodeActivite(StringFilter codeActivite) {
        this.codeActivite = codeActivite;
    }

    public LongFilter getPaiementSanctionId() {
        return paiementSanctionId;
    }

    public LongFilter paiementSanctionId() {
        if (paiementSanctionId == null) {
            paiementSanctionId = new LongFilter();
        }
        return paiementSanctionId;
    }

    public void setPaiementSanctionId(LongFilter paiementSanctionId) {
        this.paiementSanctionId = paiementSanctionId;
    }

    public LongFilter getSanctionConfigId() {
        return sanctionConfigId;
    }

    public LongFilter sanctionConfigId() {
        if (sanctionConfigId == null) {
            sanctionConfigId = new LongFilter();
        }
        return sanctionConfigId;
    }

    public void setSanctionConfigId(LongFilter sanctionConfigId) {
        this.sanctionConfigId = sanctionConfigId;
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
        final SanctionCriteria that = (SanctionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(libelle, that.libelle) &&
            Objects.equals(matriculeAdherent, that.matriculeAdherent) &&
            Objects.equals(dateSanction, that.dateSanction) &&
            Objects.equals(motifSanction, that.motifSanction) &&
            Objects.equals(description, that.description) &&
            Objects.equals(codeActivite, that.codeActivite) &&
            Objects.equals(paiementSanctionId, that.paiementSanctionId) &&
            Objects.equals(sanctionConfigId, that.sanctionConfigId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            libelle,
            matriculeAdherent,
            dateSanction,
            motifSanction,
            description,
            codeActivite,
            paiementSanctionId,
            sanctionConfigId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (libelle != null ? "libelle=" + libelle + ", " : "") +
            (matriculeAdherent != null ? "matriculeAdherent=" + matriculeAdherent + ", " : "") +
            (dateSanction != null ? "dateSanction=" + dateSanction + ", " : "") +
            (motifSanction != null ? "motifSanction=" + motifSanction + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (codeActivite != null ? "codeActivite=" + codeActivite + ", " : "") +
            (paiementSanctionId != null ? "paiementSanctionId=" + paiementSanctionId + ", " : "") +
            (sanctionConfigId != null ? "sanctionConfigId=" + sanctionConfigId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
