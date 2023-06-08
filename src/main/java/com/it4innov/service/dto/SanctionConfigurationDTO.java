package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.TypeSanction;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.SanctionConfiguration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionConfigurationDTO implements Serializable {

    private Long id;

    @NotNull
    private String codeAssociation;

    @NotNull
    private String codeTontine;

    private TypeSanction type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getCodeTontine() {
        return codeTontine;
    }

    public void setCodeTontine(String codeTontine) {
        this.codeTontine = codeTontine;
    }

    public TypeSanction getType() {
        return type;
    }

    public void setType(TypeSanction type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SanctionConfigurationDTO)) {
            return false;
        }

        SanctionConfigurationDTO sanctionConfigurationDTO = (SanctionConfigurationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sanctionConfigurationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionConfigurationDTO{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", codeTontine='" + getCodeTontine() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
