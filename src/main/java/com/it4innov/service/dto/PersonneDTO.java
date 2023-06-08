package com.it4innov.service.dto;

import com.it4innov.domain.enumeration.Sexe;
import com.it4innov.domain.enumeration.TypePersonne;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.it4innov.domain.Personne} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonneDTO implements Serializable {

    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.")
    private Long idUser;

    private Long codeAssociation;

    private String matricule;

    private String nom;

    private String prenom;

    private LocalDate dateNaissance;

    private Long lieuNaissance;

    private Instant dateInscription;

    private String profession;

    private Sexe sexe;

    private String photoPath;

    private Instant dateIntegration;

    private Boolean isAdmin;

    private Boolean isDonateur;

    private Boolean isBenevole;

    private TypePersonne typePersonne;

    private AdresseDTO adresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(Long codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(Long lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Instant getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Instant getDateIntegration() {
        return dateIntegration;
    }

    public void setDateIntegration(Instant dateIntegration) {
        this.dateIntegration = dateIntegration;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsDonateur() {
        return isDonateur;
    }

    public void setIsDonateur(Boolean isDonateur) {
        this.isDonateur = isDonateur;
    }

    public Boolean getIsBenevole() {
        return isBenevole;
    }

    public void setIsBenevole(Boolean isBenevole) {
        this.isBenevole = isBenevole;
    }

    public TypePersonne getTypePersonne() {
        return typePersonne;
    }

    public void setTypePersonne(TypePersonne typePersonne) {
        this.typePersonne = typePersonne;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonneDTO)) {
            return false;
        }

        PersonneDTO personneDTO = (PersonneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneDTO{" +
            "id=" + getId() +
            ", idUser=" + getIdUser() +
            ", codeAssociation=" + getCodeAssociation() +
            ", matricule='" + getMatricule() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance=" + getLieuNaissance() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", profession='" + getProfession() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", photoPath='" + getPhotoPath() + "'" +
            ", dateIntegration='" + getDateIntegration() + "'" +
            ", isAdmin='" + getIsAdmin() + "'" +
            ", isDonateur='" + getIsDonateur() + "'" +
            ", isBenevole='" + getIsBenevole() + "'" +
            ", typePersonne='" + getTypePersonne() + "'" +
            ", adresse=" + getAdresse() +
            "}";
    }
}
