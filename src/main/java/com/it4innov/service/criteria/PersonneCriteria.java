package com.it4innov.service.criteria;

import com.it4innov.domain.enumeration.Sexe;
import com.it4innov.domain.enumeration.TypePersonne;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.it4innov.domain.Personne} entity. This class is used
 * in {@link com.it4innov.web.rest.PersonneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personnes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonneCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {

        public SexeFilter() {}

        public SexeFilter(SexeFilter filter) {
            super(filter);
        }

        @Override
        public SexeFilter copy() {
            return new SexeFilter(this);
        }
    }

    /**
     * Class for filtering TypePersonne
     */
    public static class TypePersonneFilter extends Filter<TypePersonne> {

        public TypePersonneFilter() {}

        public TypePersonneFilter(TypePersonneFilter filter) {
            super(filter);
        }

        @Override
        public TypePersonneFilter copy() {
            return new TypePersonneFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter idUser;

    private LongFilter codeAssociation;

    private StringFilter matricule;

    private StringFilter nom;

    private StringFilter prenom;

    private LocalDateFilter dateNaissance;

    private LongFilter lieuNaissance;

    private InstantFilter dateInscription;

    private StringFilter profession;

    private SexeFilter sexe;

    private StringFilter photoPath;

    private InstantFilter dateIntegration;

    private BooleanFilter isAdmin;

    private BooleanFilter isDonateur;

    private BooleanFilter isBenevole;

    private TypePersonneFilter typePersonne;

    private LongFilter adresseId;

    private LongFilter contactId;

    private LongFilter compteRIBId;

    private LongFilter historiquePersonneId;

    private LongFilter presenceId;

    private LongFilter fonctionAdherentId;

    private Boolean distinct;

    public PersonneCriteria() {}

    public PersonneCriteria(PersonneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.idUser = other.idUser == null ? null : other.idUser.copy();
        this.codeAssociation = other.codeAssociation == null ? null : other.codeAssociation.copy();
        this.matricule = other.matricule == null ? null : other.matricule.copy();
        this.nom = other.nom == null ? null : other.nom.copy();
        this.prenom = other.prenom == null ? null : other.prenom.copy();
        this.dateNaissance = other.dateNaissance == null ? null : other.dateNaissance.copy();
        this.lieuNaissance = other.lieuNaissance == null ? null : other.lieuNaissance.copy();
        this.dateInscription = other.dateInscription == null ? null : other.dateInscription.copy();
        this.profession = other.profession == null ? null : other.profession.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.photoPath = other.photoPath == null ? null : other.photoPath.copy();
        this.dateIntegration = other.dateIntegration == null ? null : other.dateIntegration.copy();
        this.isAdmin = other.isAdmin == null ? null : other.isAdmin.copy();
        this.isDonateur = other.isDonateur == null ? null : other.isDonateur.copy();
        this.isBenevole = other.isBenevole == null ? null : other.isBenevole.copy();
        this.typePersonne = other.typePersonne == null ? null : other.typePersonne.copy();
        this.adresseId = other.adresseId == null ? null : other.adresseId.copy();
        this.contactId = other.contactId == null ? null : other.contactId.copy();
        this.compteRIBId = other.compteRIBId == null ? null : other.compteRIBId.copy();
        this.historiquePersonneId = other.historiquePersonneId == null ? null : other.historiquePersonneId.copy();
        this.presenceId = other.presenceId == null ? null : other.presenceId.copy();
        this.fonctionAdherentId = other.fonctionAdherentId == null ? null : other.fonctionAdherentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PersonneCriteria copy() {
        return new PersonneCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getIdUser() {
        return idUser;
    }

    public LongFilter idUser() {
        if (idUser == null) {
            idUser = new LongFilter();
        }
        return idUser;
    }

    public void setIdUser(LongFilter idUser) {
        this.idUser = idUser;
    }

    public LongFilter getCodeAssociation() {
        return codeAssociation;
    }

    public LongFilter codeAssociation() {
        if (codeAssociation == null) {
            codeAssociation = new LongFilter();
        }
        return codeAssociation;
    }

    public void setCodeAssociation(LongFilter codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public StringFilter getMatricule() {
        return matricule;
    }

    public StringFilter matricule() {
        if (matricule == null) {
            matricule = new StringFilter();
        }
        return matricule;
    }

    public void setMatricule(StringFilter matricule) {
        this.matricule = matricule;
    }

    public StringFilter getNom() {
        return nom;
    }

    public StringFilter nom() {
        if (nom == null) {
            nom = new StringFilter();
        }
        return nom;
    }

    public void setNom(StringFilter nom) {
        this.nom = nom;
    }

    public StringFilter getPrenom() {
        return prenom;
    }

    public StringFilter prenom() {
        if (prenom == null) {
            prenom = new StringFilter();
        }
        return prenom;
    }

    public void setPrenom(StringFilter prenom) {
        this.prenom = prenom;
    }

    public LocalDateFilter getDateNaissance() {
        return dateNaissance;
    }

    public LocalDateFilter dateNaissance() {
        if (dateNaissance == null) {
            dateNaissance = new LocalDateFilter();
        }
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateFilter dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public LongFilter getLieuNaissance() {
        return lieuNaissance;
    }

    public LongFilter lieuNaissance() {
        if (lieuNaissance == null) {
            lieuNaissance = new LongFilter();
        }
        return lieuNaissance;
    }

    public void setLieuNaissance(LongFilter lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public InstantFilter getDateInscription() {
        return dateInscription;
    }

    public InstantFilter dateInscription() {
        if (dateInscription == null) {
            dateInscription = new InstantFilter();
        }
        return dateInscription;
    }

    public void setDateInscription(InstantFilter dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StringFilter getProfession() {
        return profession;
    }

    public StringFilter profession() {
        if (profession == null) {
            profession = new StringFilter();
        }
        return profession;
    }

    public void setProfession(StringFilter profession) {
        this.profession = profession;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public SexeFilter sexe() {
        if (sexe == null) {
            sexe = new SexeFilter();
        }
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public StringFilter getPhotoPath() {
        return photoPath;
    }

    public StringFilter photoPath() {
        if (photoPath == null) {
            photoPath = new StringFilter();
        }
        return photoPath;
    }

    public void setPhotoPath(StringFilter photoPath) {
        this.photoPath = photoPath;
    }

    public InstantFilter getDateIntegration() {
        return dateIntegration;
    }

    public InstantFilter dateIntegration() {
        if (dateIntegration == null) {
            dateIntegration = new InstantFilter();
        }
        return dateIntegration;
    }

    public void setDateIntegration(InstantFilter dateIntegration) {
        this.dateIntegration = dateIntegration;
    }

    public BooleanFilter getIsAdmin() {
        return isAdmin;
    }

    public BooleanFilter isAdmin() {
        if (isAdmin == null) {
            isAdmin = new BooleanFilter();
        }
        return isAdmin;
    }

    public void setIsAdmin(BooleanFilter isAdmin) {
        this.isAdmin = isAdmin;
    }

    public BooleanFilter getIsDonateur() {
        return isDonateur;
    }

    public BooleanFilter isDonateur() {
        if (isDonateur == null) {
            isDonateur = new BooleanFilter();
        }
        return isDonateur;
    }

    public void setIsDonateur(BooleanFilter isDonateur) {
        this.isDonateur = isDonateur;
    }

    public BooleanFilter getIsBenevole() {
        return isBenevole;
    }

    public BooleanFilter isBenevole() {
        if (isBenevole == null) {
            isBenevole = new BooleanFilter();
        }
        return isBenevole;
    }

    public void setIsBenevole(BooleanFilter isBenevole) {
        this.isBenevole = isBenevole;
    }

    public TypePersonneFilter getTypePersonne() {
        return typePersonne;
    }

    public TypePersonneFilter typePersonne() {
        if (typePersonne == null) {
            typePersonne = new TypePersonneFilter();
        }
        return typePersonne;
    }

    public void setTypePersonne(TypePersonneFilter typePersonne) {
        this.typePersonne = typePersonne;
    }

    public LongFilter getAdresseId() {
        return adresseId;
    }

    public LongFilter adresseId() {
        if (adresseId == null) {
            adresseId = new LongFilter();
        }
        return adresseId;
    }

    public void setAdresseId(LongFilter adresseId) {
        this.adresseId = adresseId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public LongFilter contactId() {
        if (contactId == null) {
            contactId = new LongFilter();
        }
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getCompteRIBId() {
        return compteRIBId;
    }

    public LongFilter compteRIBId() {
        if (compteRIBId == null) {
            compteRIBId = new LongFilter();
        }
        return compteRIBId;
    }

    public void setCompteRIBId(LongFilter compteRIBId) {
        this.compteRIBId = compteRIBId;
    }

    public LongFilter getHistoriquePersonneId() {
        return historiquePersonneId;
    }

    public LongFilter historiquePersonneId() {
        if (historiquePersonneId == null) {
            historiquePersonneId = new LongFilter();
        }
        return historiquePersonneId;
    }

    public void setHistoriquePersonneId(LongFilter historiquePersonneId) {
        this.historiquePersonneId = historiquePersonneId;
    }

    public LongFilter getPresenceId() {
        return presenceId;
    }

    public LongFilter presenceId() {
        if (presenceId == null) {
            presenceId = new LongFilter();
        }
        return presenceId;
    }

    public void setPresenceId(LongFilter presenceId) {
        this.presenceId = presenceId;
    }

    public LongFilter getFonctionAdherentId() {
        return fonctionAdherentId;
    }

    public LongFilter fonctionAdherentId() {
        if (fonctionAdherentId == null) {
            fonctionAdherentId = new LongFilter();
        }
        return fonctionAdherentId;
    }

    public void setFonctionAdherentId(LongFilter fonctionAdherentId) {
        this.fonctionAdherentId = fonctionAdherentId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonneCriteria that = (PersonneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(idUser, that.idUser) &&
            Objects.equals(codeAssociation, that.codeAssociation) &&
            Objects.equals(matricule, that.matricule) &&
            Objects.equals(nom, that.nom) &&
            Objects.equals(prenom, that.prenom) &&
            Objects.equals(dateNaissance, that.dateNaissance) &&
            Objects.equals(lieuNaissance, that.lieuNaissance) &&
            Objects.equals(dateInscription, that.dateInscription) &&
            Objects.equals(profession, that.profession) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(photoPath, that.photoPath) &&
            Objects.equals(dateIntegration, that.dateIntegration) &&
            Objects.equals(isAdmin, that.isAdmin) &&
            Objects.equals(isDonateur, that.isDonateur) &&
            Objects.equals(isBenevole, that.isBenevole) &&
            Objects.equals(typePersonne, that.typePersonne) &&
            Objects.equals(adresseId, that.adresseId) &&
            Objects.equals(contactId, that.contactId) &&
            Objects.equals(compteRIBId, that.compteRIBId) &&
            Objects.equals(historiquePersonneId, that.historiquePersonneId) &&
            Objects.equals(presenceId, that.presenceId) &&
            Objects.equals(fonctionAdherentId, that.fonctionAdherentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            idUser,
            codeAssociation,
            matricule,
            nom,
            prenom,
            dateNaissance,
            lieuNaissance,
            dateInscription,
            profession,
            sexe,
            photoPath,
            dateIntegration,
            isAdmin,
            isDonateur,
            isBenevole,
            typePersonne,
            adresseId,
            contactId,
            compteRIBId,
            historiquePersonneId,
            presenceId,
            fonctionAdherentId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (idUser != null ? "idUser=" + idUser + ", " : "") +
            (codeAssociation != null ? "codeAssociation=" + codeAssociation + ", " : "") +
            (matricule != null ? "matricule=" + matricule + ", " : "") +
            (nom != null ? "nom=" + nom + ", " : "") +
            (prenom != null ? "prenom=" + prenom + ", " : "") +
            (dateNaissance != null ? "dateNaissance=" + dateNaissance + ", " : "") +
            (lieuNaissance != null ? "lieuNaissance=" + lieuNaissance + ", " : "") +
            (dateInscription != null ? "dateInscription=" + dateInscription + ", " : "") +
            (profession != null ? "profession=" + profession + ", " : "") +
            (sexe != null ? "sexe=" + sexe + ", " : "") +
            (photoPath != null ? "photoPath=" + photoPath + ", " : "") +
            (dateIntegration != null ? "dateIntegration=" + dateIntegration + ", " : "") +
            (isAdmin != null ? "isAdmin=" + isAdmin + ", " : "") +
            (isDonateur != null ? "isDonateur=" + isDonateur + ", " : "") +
            (isBenevole != null ? "isBenevole=" + isBenevole + ", " : "") +
            (typePersonne != null ? "typePersonne=" + typePersonne + ", " : "") +
            (adresseId != null ? "adresseId=" + adresseId + ", " : "") +
            (contactId != null ? "contactId=" + contactId + ", " : "") +
            (compteRIBId != null ? "compteRIBId=" + compteRIBId + ", " : "") +
            (historiquePersonneId != null ? "historiquePersonneId=" + historiquePersonneId + ", " : "") +
            (presenceId != null ? "presenceId=" + presenceId + ", " : "") +
            (fonctionAdherentId != null ? "fonctionAdherentId=" + fonctionAdherentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
