package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.StatutPresence;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.it4innov.domain.Presence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PresenceDTO implements Serializable {

    private Long id;

    @NotNull
    private String matriculeAdherant;

    private StatutPresence statutPresence;

    private FichePresenceDTO fichePresence;

    private PersonneDTO adherant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatriculeAdherant() {
        return matriculeAdherant;
    }

    public void setMatriculeAdherant(String matriculeAdherant) {
        this.matriculeAdherant = matriculeAdherant;
    }

    public StatutPresence getStatutPresence() {
        return statutPresence;
    }

    public void setStatutPresence(StatutPresence statutPresence) {
        this.statutPresence = statutPresence;
    }

    public FichePresenceDTO getFichePresence() {
        return fichePresence;
    }

    public void setFichePresence(FichePresenceDTO fichePresence) {
        this.fichePresence = fichePresence;
    }

    public PersonneDTO getAdherant() {
        return adherant;
    }

    public void setAdherant(PersonneDTO adherant) {
        this.adherant = adherant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresenceDTO)) {
            return false;
        }

        PresenceDTO presenceDTO = (PresenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, presenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceDTO{" +
            "id=" + getId() +
            ", matriculeAdherant='" + getMatriculeAdherant() + "'" +
            ", statutPresence='" + getStatutPresence() + "'" +
            ", fichePresence=" + getFichePresence() +
            ", adherant=" + getAdherant() +
            "}";
    }
}
