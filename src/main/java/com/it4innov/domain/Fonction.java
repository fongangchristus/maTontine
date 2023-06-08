package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * Fonctions des adhérent (membre, président sécrétaire ....)\n@author The JHipster team.
 */
@Entity
@Table(name = "fonction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Fonction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "fonction")
    @JsonIgnoreProperties(value = { "adherent", "fonction" }, allowSetters = true)
    private Set<FonctionAdherent> fonctionAdherents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fonction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Fonction title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Fonction description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FonctionAdherent> getFonctionAdherents() {
        return this.fonctionAdherents;
    }

    public void setFonctionAdherents(Set<FonctionAdherent> fonctionAdherents) {
        if (this.fonctionAdherents != null) {
            this.fonctionAdherents.forEach(i -> i.setFonction(null));
        }
        if (fonctionAdherents != null) {
            fonctionAdherents.forEach(i -> i.setFonction(this));
        }
        this.fonctionAdherents = fonctionAdherents;
    }

    public Fonction fonctionAdherents(Set<FonctionAdherent> fonctionAdherents) {
        this.setFonctionAdherents(fonctionAdherents);
        return this;
    }

    public Fonction addFonctionAdherent(FonctionAdherent fonctionAdherent) {
        this.fonctionAdherents.add(fonctionAdherent);
        fonctionAdherent.setFonction(this);
        return this;
    }

    public Fonction removeFonctionAdherent(FonctionAdherent fonctionAdherent) {
        this.fonctionAdherents.remove(fonctionAdherent);
        fonctionAdherent.setFonction(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fonction)) {
            return false;
        }
        return id != null && id.equals(((Fonction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fonction{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
