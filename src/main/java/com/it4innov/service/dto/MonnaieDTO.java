package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.Monnaie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MonnaieDTO implements Serializable {

    private Long id;

    private String libele;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonnaieDTO)) {
            return false;
        }

        MonnaieDTO monnaieDTO = (MonnaieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, monnaieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MonnaieDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            "}";
    }
}
