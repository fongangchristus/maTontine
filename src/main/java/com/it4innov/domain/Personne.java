package com.it4innov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.it4innov.domain.enumeration.Sexe;
import com.it4innov.domain.enumeration.TypePersonne;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Personne.
 */
@Entity
@Table(name = "personne")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Personne implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "code_association")
    private Long codeAssociation;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private Long lieuNaissance;

    @Column(name = "date_inscription")
    private Instant dateInscription;

    @Column(name = "profession")
    private String profession;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private Sexe sexe;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "date_integration")
    private Instant dateIntegration;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "is_donateur")
    private Boolean isDonateur;

    @Column(name = "is_benevole")
    private Boolean isBenevole;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_personne")
    private TypePersonne typePersonne;

    @OneToOne
    @JoinColumn(unique = true)
    private Adresse adresse;

    @OneToMany(mappedBy = "adherent")
    @JsonIgnoreProperties(value = { "adherent" }, allowSetters = true)
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "adherent")
    @JsonIgnoreProperties(value = { "adherent" }, allowSetters = true)
    private Set<CompteRIB> compteRIBS = new HashSet<>();

    @OneToMany(mappedBy = "personne")
    @JsonIgnoreProperties(value = { "personne" }, allowSetters = true)
    private Set<HistoriquePersonne> historiquePersonnes = new HashSet<>();

    @OneToMany(mappedBy = "adherant")
    @JsonIgnoreProperties(value = { "fichePresence", "adherant" }, allowSetters = true)
    private Set<Presence> presences = new HashSet<>();

    @OneToMany(mappedBy = "adherent")
    @JsonIgnoreProperties(value = { "adherent", "fonction" }, allowSetters = true)
    private Set<FonctionAdherent> fonctionAdherents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Personne id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return this.idUser;
    }

    public Personne idUser(Long idUser) {
        this.setIdUser(idUser);
        return this;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getCodeAssociation() {
        return this.codeAssociation;
    }

    public Personne codeAssociation(Long codeAssociation) {
        this.setCodeAssociation(codeAssociation);
        return this;
    }

    public void setCodeAssociation(Long codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Personne matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return this.nom;
    }

    public Personne nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Personne prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return this.dateNaissance;
    }

    public Personne dateNaissance(LocalDate dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Long getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Personne lieuNaissance(Long lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(Long lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public Personne dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getProfession() {
        return this.profession;
    }

    public Personne profession(String profession) {
        this.setProfession(profession);
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Sexe getSexe() {
        return this.sexe;
    }

    public Personne sexe(Sexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public Personne photoPath(String photoPath) {
        this.setPhotoPath(photoPath);
        return this;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Instant getDateIntegration() {
        return this.dateIntegration;
    }

    public Personne dateIntegration(Instant dateIntegration) {
        this.setDateIntegration(dateIntegration);
        return this;
    }

    public void setDateIntegration(Instant dateIntegration) {
        this.dateIntegration = dateIntegration;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public Personne isAdmin(Boolean isAdmin) {
        this.setIsAdmin(isAdmin);
        return this;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsDonateur() {
        return this.isDonateur;
    }

    public Personne isDonateur(Boolean isDonateur) {
        this.setIsDonateur(isDonateur);
        return this;
    }

    public void setIsDonateur(Boolean isDonateur) {
        this.isDonateur = isDonateur;
    }

    public Boolean getIsBenevole() {
        return this.isBenevole;
    }

    public Personne isBenevole(Boolean isBenevole) {
        this.setIsBenevole(isBenevole);
        return this;
    }

    public void setIsBenevole(Boolean isBenevole) {
        this.isBenevole = isBenevole;
    }

    public TypePersonne getTypePersonne() {
        return this.typePersonne;
    }

    public Personne typePersonne(TypePersonne typePersonne) {
        this.setTypePersonne(typePersonne);
        return this;
    }

    public void setTypePersonne(TypePersonne typePersonne) {
        this.typePersonne = typePersonne;
    }

    public Adresse getAdresse() {
        return this.adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Personne adresse(Adresse adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if (this.contacts != null) {
            this.contacts.forEach(i -> i.setAdherent(null));
        }
        if (contacts != null) {
            contacts.forEach(i -> i.setAdherent(this));
        }
        this.contacts = contacts;
    }

    public Personne contacts(Set<Contact> contacts) {
        this.setContacts(contacts);
        return this;
    }

    public Personne addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setAdherent(this);
        return this;
    }

    public Personne removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setAdherent(null);
        return this;
    }

    public Set<CompteRIB> getCompteRIBS() {
        return this.compteRIBS;
    }

    public void setCompteRIBS(Set<CompteRIB> compteRIBS) {
        if (this.compteRIBS != null) {
            this.compteRIBS.forEach(i -> i.setAdherent(null));
        }
        if (compteRIBS != null) {
            compteRIBS.forEach(i -> i.setAdherent(this));
        }
        this.compteRIBS = compteRIBS;
    }

    public Personne compteRIBS(Set<CompteRIB> compteRIBS) {
        this.setCompteRIBS(compteRIBS);
        return this;
    }

    public Personne addCompteRIB(CompteRIB compteRIB) {
        this.compteRIBS.add(compteRIB);
        compteRIB.setAdherent(this);
        return this;
    }

    public Personne removeCompteRIB(CompteRIB compteRIB) {
        this.compteRIBS.remove(compteRIB);
        compteRIB.setAdherent(null);
        return this;
    }

    public Set<HistoriquePersonne> getHistoriquePersonnes() {
        return this.historiquePersonnes;
    }

    public void setHistoriquePersonnes(Set<HistoriquePersonne> historiquePersonnes) {
        if (this.historiquePersonnes != null) {
            this.historiquePersonnes.forEach(i -> i.setPersonne(null));
        }
        if (historiquePersonnes != null) {
            historiquePersonnes.forEach(i -> i.setPersonne(this));
        }
        this.historiquePersonnes = historiquePersonnes;
    }

    public Personne historiquePersonnes(Set<HistoriquePersonne> historiquePersonnes) {
        this.setHistoriquePersonnes(historiquePersonnes);
        return this;
    }

    public Personne addHistoriquePersonne(HistoriquePersonne historiquePersonne) {
        this.historiquePersonnes.add(historiquePersonne);
        historiquePersonne.setPersonne(this);
        return this;
    }

    public Personne removeHistoriquePersonne(HistoriquePersonne historiquePersonne) {
        this.historiquePersonnes.remove(historiquePersonne);
        historiquePersonne.setPersonne(null);
        return this;
    }

    public Set<Presence> getPresences() {
        return this.presences;
    }

    public void setPresences(Set<Presence> presences) {
        if (this.presences != null) {
            this.presences.forEach(i -> i.setAdherant(null));
        }
        if (presences != null) {
            presences.forEach(i -> i.setAdherant(this));
        }
        this.presences = presences;
    }

    public Personne presences(Set<Presence> presences) {
        this.setPresences(presences);
        return this;
    }

    public Personne addPresence(Presence presence) {
        this.presences.add(presence);
        presence.setAdherant(this);
        return this;
    }

    public Personne removePresence(Presence presence) {
        this.presences.remove(presence);
        presence.setAdherant(null);
        return this;
    }

    public Set<FonctionAdherent> getFonctionAdherents() {
        return this.fonctionAdherents;
    }

    public void setFonctionAdherents(Set<FonctionAdherent> fonctionAdherents) {
        if (this.fonctionAdherents != null) {
            this.fonctionAdherents.forEach(i -> i.setAdherent(null));
        }
        if (fonctionAdherents != null) {
            fonctionAdherents.forEach(i -> i.setAdherent(this));
        }
        this.fonctionAdherents = fonctionAdherents;
    }

    public Personne fonctionAdherents(Set<FonctionAdherent> fonctionAdherents) {
        this.setFonctionAdherents(fonctionAdherents);
        return this;
    }

    public Personne addFonctionAdherent(FonctionAdherent fonctionAdherent) {
        this.fonctionAdherents.add(fonctionAdherent);
        fonctionAdherent.setAdherent(this);
        return this;
    }

    public Personne removeFonctionAdherent(FonctionAdherent fonctionAdherent) {
        this.fonctionAdherents.remove(fonctionAdherent);
        fonctionAdherent.setAdherent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Personne)) {
            return false;
        }
        return id != null && id.equals(((Personne) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Personne{" +
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
            "}";
    }
}
