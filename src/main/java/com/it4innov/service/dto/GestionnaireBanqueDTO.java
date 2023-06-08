package com.it4innov.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.GestionnaireBanque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GestionnaireBanqueDTO implements Serializable {

    private Long id;

    private String matriculeMembre;

    private BanqueDTO banque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeMembre() {
        return matriculeMembre;
    }

    public void setMatriculeMembre(String matriculeMembre) {
        this.matriculeMembre = matriculeMembre;
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
        if (!(o instanceof GestionnaireBanqueDTO)) {
            return false;
        }

        GestionnaireBanqueDTO gestionnaireBanqueDTO = (GestionnaireBanqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, gestionnaireBanqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GestionnaireBanqueDTO{" +
            "id=" + getId() +
            ", matriculeMembre='" + getMatriculeMembre() + "'" +
            ", banque=" + getBanque() +
            "}";
    }
}
