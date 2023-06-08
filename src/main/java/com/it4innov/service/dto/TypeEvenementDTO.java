package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.TypeEvenement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeEvenementDTO implements Serializable {

    private Long id;

    private String libele;

    private String observation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeEvenementDTO)) {
            return false;
        }

        TypeEvenementDTO typeEvenementDTO = (TypeEvenementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeEvenementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeEvenementDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", observation='" + getObservation() + "'" +
            "}";
    }
}
