package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.TypePot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypePotDTO implements Serializable {

    private Long id;

    @NotNull
    private String libele;

    private String descrption;

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

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypePotDTO)) {
            return false;
        }

        TypePotDTO typePotDTO = (TypePotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typePotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypePotDTO{" +
            "id=" + getId() +
            ", libele='" + getLibele() + "'" +
            ", descrption='" + getDescrption() + "'" +
            "}";
    }
}
