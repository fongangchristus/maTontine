package com.it4innov.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.CompteRIB} entity.
 */
@Schema(description = "The Employee entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteRIBDTO implements Serializable {

    private Long id;

    /**
     * /IBAN attribute.
     */
    @Schema(description = "/IBAN attribute.")
    private String iban;

    private String titulaireCompte;

    private Boolean verifier;

    private PersonneDTO adherent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTitulaireCompte() {
        return titulaireCompte;
    }

    public void setTitulaireCompte(String titulaireCompte) {
        this.titulaireCompte = titulaireCompte;
    }

    public Boolean getVerifier() {
        return verifier;
    }

    public void setVerifier(Boolean verifier) {
        this.verifier = verifier;
    }

    public PersonneDTO getAdherent() {
        return adherent;
    }

    public void setAdherent(PersonneDTO adherent) {
        this.adherent = adherent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteRIBDTO)) {
            return false;
        }

        CompteRIBDTO compteRIBDTO = (CompteRIBDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteRIBDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteRIBDTO{" +
            "id=" + getId() +
            ", iban='" + getIban() + "'" +
            ", titulaireCompte='" + getTitulaireCompte() + "'" +
            ", verifier='" + getVerifier() + "'" +
            ", adherent=" + getAdherent() +
            "}";
    }
}
