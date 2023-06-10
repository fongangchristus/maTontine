package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.Langue;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Association.
 */
@Entity
@Table(name = "association")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Association implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code_association", nullable = false)
    private String codeAssociation;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "slogan")
    private String slogan;

    @Column(name = "logo_path")
    private String logoPath;

    @Column(name = "reglement_path")
    private String reglementPath;

    @Column(name = "statut_path")
    private String statutPath;

    @Column(name = "description")
    private String description;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "fuseau_horaire")
    private String fuseauHoraire;

    @Enumerated(EnumType.STRING)
    @Column(name = "langue")
    private Langue langue;

    @Column(name = "presentation")
    private String presentation;

    @Column(name = "siege_social")
    private String siegeSocial;

    @Column(name = "email")
    private String email;

    @Column(name = "is_actif")
    private Boolean isActif;

    /**
     * A relationship
     */
    @OneToMany(mappedBy = "association")
    @JsonIgnoreProperties(value = { "association" }, allowSetters = true)
    private Set<Exercise> exercises = new HashSet<>();

    @OneToMany(mappedBy = "association")
    @JsonIgnoreProperties(value = { "association" }, allowSetters = true)
    private Set<DocumentAssociation> documentAssociations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "associations" }, allowSetters = true)
    private Monnaie monnaie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Association id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssociation() {
        return this.codeAssociation;
    }

    public Association codeAssociation(String codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getDenomination() {
        return this.denomination;
    }

    public Association denomination(String denomination) {
        this.setDenomination(denomination);
        return this;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getSlogan() {
        return this.slogan;
    }

    public Association slogan(String slogan) {
        this.setSlogan(slogan);
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public Association logoPath(String logoPath) {
        this.setLogoPath(logoPath);
        return this;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getReglementPath() {
        return this.reglementPath;
    }

    public Association reglementPath(String reglementPath) {
        this.setReglementPath(reglementPath);
        return this;
    }

    public void setReglementPath(String reglementPath) {
        this.reglementPath = reglementPath;
    }

    public String getStatutPath() {
        return this.statutPath;
    }

    public Association statutPath(String statutPath) {
        this.setStatutPath(statutPath);
        return this;
    }

    public void setStatutPath(String statutPath) {
        this.statutPath = statutPath;
    }

    public String getDescription() {
        return this.description;
    }

    public Association description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Association dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getFuseauHoraire() {
        return this.fuseauHoraire;
    }

    public Association fuseauHoraire(String fuseauHoraire) {
        this.setFuseauHoraire(fuseauHoraire);
        return this;
    }

    public void setFuseauHoraire(String fuseauHoraire) {
        this.fuseauHoraire = fuseauHoraire;
    }

    public Langue getLangue() {
        return this.langue;
    }

    public Association langue(Langue langue) {
        this.setLangue(langue);
        return this;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public String getPresentation() {
        return this.presentation;
    }

    public Association presentation(String presentation) {
        this.setPresentation(presentation);
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getSiegeSocial() {
        return this.siegeSocial;
    }

    public Association siegeSocial(String siegeSocial) {
        this.setSiegeSocial(siegeSocial);
        return this;
    }

    public void setSiegeSocial(String siegeSocial) {
        this.siegeSocial = siegeSocial;
    }

    public String getEmail() {
        return this.email;
    }

    public Association email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActif() {
        return this.isActif;
    }

    public Association isActif(Boolean isActif) {
        this.setIsActif(isActif);
        return this;
    }

    public void setIsActif(Boolean isActif) {
        this.isActif = isActif;
    }

    public Set<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        if (this.exercises != null) {
            this.exercises.forEach(i -> i.setAssociation(null));
        }
        if (exercises != null) {
            exercises.forEach(i -> i.setAssociation(this));
        }
        this.exercises = exercises;
    }

    public Association exercises(Set<Exercise> exercises) {
        this.setExercises(exercises);
        return this;
    }

    public Association addExercise(Exercise exercise) {
        this.exercises.add(exercise);
        exercise.setAssociation(this);
        return this;
    }

    public Association removeExercise(Exercise exercise) {
        this.exercises.remove(exercise);
        exercise.setAssociation(null);
        return this;
    }

    public Set<DocumentAssociation> getDocumentAssociations() {
        return this.documentAssociations;
    }

    public void setDocumentAssociations(Set<DocumentAssociation> documentAssociations) {
        if (this.documentAssociations != null) {
            this.documentAssociations.forEach(i -> i.setAssociation(null));
        }
        if (documentAssociations != null) {
            documentAssociations.forEach(i -> i.setAssociation(this));
        }
        this.documentAssociations = documentAssociations;
    }

    public Association documentAssociations(Set<DocumentAssociation> documentAssociations) {
        this.setDocumentAssociations(documentAssociations);
        return this;
    }

    public Association addDocumentAssociation(DocumentAssociation documentAssociation) {
        this.documentAssociations.add(documentAssociation);
        documentAssociation.setAssociation(this);
        return this;
    }

    public Association removeDocumentAssociation(DocumentAssociation documentAssociation) {
        this.documentAssociations.remove(documentAssociation);
        documentAssociation.setAssociation(null);
        return this;
    }

    public Monnaie getMonnaie() {
        return this.monnaie;
    }

    public void setMonnaie(Monnaie monnaie) {
        this.monnaie = monnaie;
    }

    public Association monnaie(Monnaie monnaie) {
        this.setMonnaie(monnaie);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Association)) {
            return false;
        }
        return id != null && id.equals(((Association) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Association{" +
            "id=" + getId() +
            ", codeAssociation='" + getCodeAssociation() + "'" +
            ", denomination='" + getDenomination() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", logoPath='" + getLogoPath() + "'" +
            ", reglementPath='" + getReglementPath() + "'" +
            ", statutPath='" + getStatutPath() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", fuseauHoraire='" + getFuseauHoraire() + "'" +
            ", langue='" + getLangue() + "'" +
            ", presentation='" + getPresentation() + "'" +
            ", siegeSocial='" + getSiegeSocial() + "'" +
            ", email='" + getEmail() + "'" +
            ", isActif='" + getIsActif() + "'" +
            "}";
    }
}
