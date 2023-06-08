package com.it4innov.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Sanction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionDTO implements Serializable {

    private Long id;

    private String libelle;

    @NotNull
    private String matriculeAdherent;

    private LocalDate dateSanction;

    private String motifSanction;

    private String description;

    private String codeActivite;

    private SanctionConfigurationDTO sanctionConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getMatriculeAdherent() {
        return matriculeAdherent;
    }

    public void setMatriculeAdherent(String matriculeAdherent) {
        this.matriculeAdherent = matriculeAdherent;
    }

    public LocalDate getDateSanction() {
        return dateSanction;
    }

    public void setDateSanction(LocalDate dateSanction) {
        this.dateSanction = dateSanction;
    }

    public String getMotifSanction() {
        return motifSanction;
    }

    public void setMotifSanction(String motifSanction) {
        this.motifSanction = motifSanction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeActivite() {
        return codeActivite;
    }

    public void setCodeActivite(String codeActivite) {
        this.codeActivite = codeActivite;
    }

    public SanctionConfigurationDTO getSanctionConfig() {
        return sanctionConfig;
    }

    public void setSanctionConfig(SanctionConfigurationDTO sanctionConfig) {
        this.sanctionConfig = sanctionConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SanctionDTO)) {
            return false;
        }

        SanctionDTO sanctionDTO = (SanctionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sanctionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", matriculeAdherent='" + getMatriculeAdherent() + "'" +
            ", dateSanction='" + getDateSanction() + "'" +
            ", motifSanction='" + getMotifSanction() + "'" +
            ", description='" + getDescription() + "'" +
            ", codeActivite='" + getCodeActivite() + "'" +
            ", sanctionConfig=" + getSanctionConfig() +
            "}";
    }
}
