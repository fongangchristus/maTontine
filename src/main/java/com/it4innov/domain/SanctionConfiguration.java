package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.TypeSanction;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SanctionConfiguration.
 */
@Entity
@Table(name = "sanction_configuration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SanctionConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @NotNull
    @Column(name = "code_tontine", nullable = false)
    private String codeTontine;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeSanction type;

    @OneToMany(mappedBy = "sanctionConfig")
    @JsonIgnoreProperties(value = { "paiementSanctions", "sanctionConfig" }, allowSetters = true)
    private Set<Sanction> sanctions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SanctionConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public SanctionConfiguration codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getCodeTontine() {
        return this.codeTontine;
    }

    public SanctionConfiguration codeTontine(String codeTontine) {
        this.setCodeTontine(codeTontine);
        return this;
    }

    public void setCodeTontine(String codeTontine) {
        this.codeTontine = codeTontine;
    }

    public TypeSanction getType() {
        return this.type;
    }

    public SanctionConfiguration type(TypeSanction type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeSanction type) {
        this.type = type;
    }

    public Set<Sanction> getSanctions() {
        return this.sanctions;
    }

    public void setSanctions(Set<Sanction> sanctions) {
        if (this.sanctions != null) {
            this.sanctions.forEach(i -> i.setSanctionConfig(null));
        }
        if (sanctions != null) {
            sanctions.forEach(i -> i.setSanctionConfig(this));
        }
        this.sanctions = sanctions;
    }

    public SanctionConfiguration sanctions(Set<Sanction> sanctions) {
        this.setSanctions(sanctions);
        return this;
    }

    public SanctionConfiguration addSanction(Sanction sanction) {
        this.sanctions.add(sanction);
        sanction.setSanctionConfig(this);
        return this;
    }

    public SanctionConfiguration removeSanction(Sanction sanction) {
        this.sanctions.remove(sanction);
        sanction.setSanctionConfig(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SanctionConfiguration)) {
            return false;
        }
        return id != null && id.equals(((SanctionConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SanctionConfiguration{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", codeTontine='" + getCodeTontine() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
