package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.CompteBanque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteBanqueDTO implements Serializable {

    private Long id;

    private String libelle;

    private String description;

    private String matriculeAdherant;

    private Double montantDisponnible;

    private BanqueDTO banque;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMatriculeAdherant() {
        return matriculeAdherant;
    }

    public void setMatriculeAdherant(String matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public Double getMontantDisponnible() {
        return montantDisponnible;
    }

    public void setMontantDisponnible(Double montantDisponnible) {
        this.montantDisponnible = montantDisponnible;
    }

    public BanqueDTO getBanque() {
        return banque;
    }

    public void setBanque(BanqueDTO banque) {
        this.banque = banque;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteBanqueDTO)) {
            return false;
        }

        CompteBanqueDTO compteBanqueDTO = (CompteBanqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteBanqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteBanqueDTO{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", description='" + getDescription() + "'" +
            ", matriculeAdherant='" + getMatriculeAdherant() + "'" +
            ", montantDisponnible=" + getMontantDisponnible() +
            ", banque=" + getBanque() +
            "}";
    }
}
