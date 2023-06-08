package com.it4innov.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.CompteTontine} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteTontineDTO implements Serializable {

    private Long id;

    /**
     * Inscription à la tontine est materialisé par la creation d'un compte tontine\nUn membre peux avoir plusieurs comptes/noms tontines
     */
    @Schema(
        description = "Inscription à la tontine est materialisé par la creation d'un compte tontine\nUn membre peux avoir plusieurs comptes/noms tontines"
    )
    private Boolean etatDeCompte;

    private String libele;

    private Integer odreBeneficiere;

    @NotNull
    private String matriculeCompte;

    @NotNull
    private String matriculeMenbre;

    private TontineDTO tontine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEtatDeCompte() {
        return etatDeCompte;
    }

    public void setEtatDeCompte(Boolean etatDeCompte) {
        this.etatDeCompte = etatDeCompte;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public Integer getOdreBeneficiere() {
        return odreBeneficiere;
    }

    public void setOdreBeneficiere(Integer odreBeneficiere) {
        this.odreBeneficiere = odreBeneficiere;
    }

    public String getMatriculeCompte() {
        return matriculeCompte;
    }

    public void setMatriculeCompte(String matriculeCompte) {
        this.matriculeCompte = matriculeCompte;
    }

    public String getMatriculeMenbre() {
        return matriculeMenbre;
    }

    public void setMatriculeMenbre(String matriculeMenbre) {
        this.matriculeMenbre = matriculeMenbre;
    }

    public TontineDTO getTontine() {
        return tontine;
    }

    public void setTontine(TontineDTO tontine) {
        this.tontine = tontine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteTontineDTO)) {
            return false;
        }

        CompteTontineDTO compteTontineDTO = (CompteTontineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteTontineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteTontineDTO{" +
            "id=" + getId() +
            ", etatDeCompte='" + getEtatDeCompte() + "'" +
            ", libele='" + getLibele() + "'" +
            ", odreBeneficiere=" + getOdreBeneficiere() +
            ", matriculeCompte='" + getMatriculeCompte() + "'" +
            ", matriculeMenbre='" + getMatriculeMenbre() + "'" +
            ", tontine=" + getTontine() +
            "}";
    }
}
