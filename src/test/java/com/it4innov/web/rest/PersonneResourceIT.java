package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Adresse;
import com.it4innov.domain.CompteRIB;
import com.it4innov.domain.Contact;
import com.it4innov.domain.FonctionAdherent;
import com.it4innov.domain.HistoriquePersonne;
import com.it4innov.domain.Personne;
import com.it4innov.domain.Presence;
import com.it4innov.domain.enumeration.Sexe;
import com.it4innov.domain.enumeration.TypePersonne;
import com.it4innov.repository.PersonneRepository;
import com.it4innov.service.criteria.PersonneCriteria;
import com.it4innov.service.dto.PersonneDTO;
import com.it4innov.service.mapper.PersonneMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonneResourceIT {

    private static final Long DEFAULT_ID_USER = 1L;
    private static final Long UPDATED_ID_USER = 2L;
    private static final Long SMALLER_ID_USER = 1L - 1L;

    private static final Long DEFAULT_CODE_ASSOCIATION = 1L;
    private static final Long UPDATED_CODE_ASSOCIATION = 2L;
    private static final Long SMALLER_CODE_ASSOCIATION = 1L - 1L;

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_NAISSANCE = LocalDate.ofEpochDay(-1L);

    private static final Long DEFAULT_LIEU_NAISSANCE = 1L;
    private static final Long UPDATED_LIEU_NAISSANCE = 2L;
    private static final Long SMALLER_LIEU_NAISSANCE = 1L - 1L;

    private static final Instant DEFAULT_DATE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_SEXE = Sexe.FEMININ;

    private static final String DEFAULT_PHOTO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_INTEGRATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INTEGRATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ADMIN = false;
    private static final Boolean UPDATED_IS_ADMIN = true;

    private static final Boolean DEFAULT_IS_DONATEUR = false;
    private static final Boolean UPDATED_IS_DONATEUR = true;

    private static final Boolean DEFAULT_IS_BENEVOLE = false;
    private static final Boolean UPDATED_IS_BENEVOLE = true;

    private static final TypePersonne DEFAULT_TYPE_PERSONNE = TypePersonne.ADHERENT;
    private static final TypePersonne UPDATED_TYPE_PERSONNE = TypePersonne.NON_ADHERENT;

    private static final String ENTITY_API_URL = "/api/personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PersonneMapper personneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonneMockMvc;

    private Personne personne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createEntity(EntityManager em) {
        Personne personne = new Personne()
            .idUser(DEFAULT_ID_USER)
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .matricule(DEFAULT_MATRICULE)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .telephone(DEFAULT_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .profession(DEFAULT_PROFESSION)
            .sexe(DEFAULT_SEXE)
            .photoPath(DEFAULT_PHOTO_PATH)
            .dateIntegration(DEFAULT_DATE_INTEGRATION)
            .isAdmin(DEFAULT_IS_ADMIN)
            .isDonateur(DEFAULT_IS_DONATEUR)
            .isBenevole(DEFAULT_IS_BENEVOLE)
            .typePersonne(DEFAULT_TYPE_PERSONNE);
        return personne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Personne createUpdatedEntity(EntityManager em) {
        Personne personne = new Personne()
            .idUser(UPDATED_ID_USER)
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .profession(UPDATED_PROFESSION)
            .sexe(UPDATED_SEXE)
            .photoPath(UPDATED_PHOTO_PATH)
            .dateIntegration(UPDATED_DATE_INTEGRATION)
            .isAdmin(UPDATED_IS_ADMIN)
            .isDonateur(UPDATED_IS_DONATEUR)
            .isBenevole(UPDATED_IS_BENEVOLE)
            .typePersonne(UPDATED_TYPE_PERSONNE);
        return personne;
    }

    @BeforeEach
    public void initTest() {
        personne = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonne() throws Exception {
        int databaseSizeBeforeCreate = personneRepository.findAll().size();
        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isCreated());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate + 1);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getIdUser()).isEqualTo(DEFAULT_ID_USER);
        assertThat(testPersonne.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testPersonne.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testPersonne.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testPersonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testPersonne.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testPersonne.getDateInscription()).isEqualTo(DEFAULT_DATE_INSCRIPTION);
        assertThat(testPersonne.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testPersonne.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPersonne.getPhotoPath()).isEqualTo(DEFAULT_PHOTO_PATH);
        assertThat(testPersonne.getDateIntegration()).isEqualTo(DEFAULT_DATE_INTEGRATION);
        assertThat(testPersonne.getIsAdmin()).isEqualTo(DEFAULT_IS_ADMIN);
        assertThat(testPersonne.getIsDonateur()).isEqualTo(DEFAULT_IS_DONATEUR);
        assertThat(testPersonne.getIsBenevole()).isEqualTo(DEFAULT_IS_BENEVOLE);
        assertThat(testPersonne.getTypePersonne()).isEqualTo(DEFAULT_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void createPersonneWithExistingId() throws Exception {
        // Create the Personne with an existing ID
        personne.setId(1L);
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        int databaseSizeBeforeCreate = personneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = personneRepository.findAll().size();
        // set the field null
        personne.setTelephone(null);

        // Create the Personne, which fails.
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        restPersonneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isBadRequest());

        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonnes() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUser").value(hasItem(DEFAULT_ID_USER.intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION.intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE.intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].photoPath").value(hasItem(DEFAULT_PHOTO_PATH)))
            .andExpect(jsonPath("$.[*].dateIntegration").value(hasItem(DEFAULT_DATE_INTEGRATION.toString())))
            .andExpect(jsonPath("$.[*].isAdmin").value(hasItem(DEFAULT_IS_ADMIN.booleanValue())))
            .andExpect(jsonPath("$.[*].isDonateur").value(hasItem(DEFAULT_IS_DONATEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].isBenevole").value(hasItem(DEFAULT_IS_BENEVOLE.booleanValue())))
            .andExpect(jsonPath("$.[*].typePersonne").value(hasItem(DEFAULT_TYPE_PERSONNE.toString())));
    }

    @Test
    @Transactional
    void getPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get the personne
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, personne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personne.getId().intValue()))
            .andExpect(jsonPath("$.idUser").value(DEFAULT_ID_USER.intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION.intValue()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE.intValue()))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.photoPath").value(DEFAULT_PHOTO_PATH))
            .andExpect(jsonPath("$.dateIntegration").value(DEFAULT_DATE_INTEGRATION.toString()))
            .andExpect(jsonPath("$.isAdmin").value(DEFAULT_IS_ADMIN.booleanValue()))
            .andExpect(jsonPath("$.isDonateur").value(DEFAULT_IS_DONATEUR.booleanValue()))
            .andExpect(jsonPath("$.isBenevole").value(DEFAULT_IS_BENEVOLE.booleanValue()))
            .andExpect(jsonPath("$.typePersonne").value(DEFAULT_TYPE_PERSONNE.toString()));
    }

    @Test
    @Transactional
    void getPersonnesByIdFiltering() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        Long id = personne.getId();

        defaultPersonneShouldBeFound("id.equals=" + id);
        defaultPersonneShouldNotBeFound("id.notEquals=" + id);

        defaultPersonneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonneShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser equals to DEFAULT_ID_USER
        defaultPersonneShouldBeFound("idUser.equals=" + DEFAULT_ID_USER);

        // Get all the personneList where idUser equals to UPDATED_ID_USER
        defaultPersonneShouldNotBeFound("idUser.equals=" + UPDATED_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser in DEFAULT_ID_USER or UPDATED_ID_USER
        defaultPersonneShouldBeFound("idUser.in=" + DEFAULT_ID_USER + "," + UPDATED_ID_USER);

        // Get all the personneList where idUser equals to UPDATED_ID_USER
        defaultPersonneShouldNotBeFound("idUser.in=" + UPDATED_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser is not null
        defaultPersonneShouldBeFound("idUser.specified=true");

        // Get all the personneList where idUser is null
        defaultPersonneShouldNotBeFound("idUser.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser is greater than or equal to DEFAULT_ID_USER
        defaultPersonneShouldBeFound("idUser.greaterThanOrEqual=" + DEFAULT_ID_USER);

        // Get all the personneList where idUser is greater than or equal to UPDATED_ID_USER
        defaultPersonneShouldNotBeFound("idUser.greaterThanOrEqual=" + UPDATED_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser is less than or equal to DEFAULT_ID_USER
        defaultPersonneShouldBeFound("idUser.lessThanOrEqual=" + DEFAULT_ID_USER);

        // Get all the personneList where idUser is less than or equal to SMALLER_ID_USER
        defaultPersonneShouldNotBeFound("idUser.lessThanOrEqual=" + SMALLER_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsLessThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser is less than DEFAULT_ID_USER
        defaultPersonneShouldNotBeFound("idUser.lessThan=" + DEFAULT_ID_USER);

        // Get all the personneList where idUser is less than UPDATED_ID_USER
        defaultPersonneShouldBeFound("idUser.lessThan=" + UPDATED_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByIdUserIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where idUser is greater than DEFAULT_ID_USER
        defaultPersonneShouldNotBeFound("idUser.greaterThan=" + DEFAULT_ID_USER);

        // Get all the personneList where idUser is greater than SMALLER_ID_USER
        defaultPersonneShouldBeFound("idUser.greaterThan=" + SMALLER_ID_USER);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation is not null
        defaultPersonneShouldBeFound("codeAssociation.specified=true");

        // Get all the personneList where codeAssociation is null
        defaultPersonneShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation is greater than or equal to DEFAULT_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.greaterThanOrEqual=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation is greater than or equal to UPDATED_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.greaterThanOrEqual=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation is less than or equal to DEFAULT_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.lessThanOrEqual=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation is less than or equal to SMALLER_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.lessThanOrEqual=" + SMALLER_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsLessThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation is less than DEFAULT_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.lessThan=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation is less than UPDATED_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.lessThan=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByCodeAssociationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where codeAssociation is greater than DEFAULT_CODE_ASSOCIATION
        defaultPersonneShouldNotBeFound("codeAssociation.greaterThan=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the personneList where codeAssociation is greater than SMALLER_CODE_ASSOCIATION
        defaultPersonneShouldBeFound("codeAssociation.greaterThan=" + SMALLER_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where matricule equals to DEFAULT_MATRICULE
        defaultPersonneShouldBeFound("matricule.equals=" + DEFAULT_MATRICULE);

        // Get all the personneList where matricule equals to UPDATED_MATRICULE
        defaultPersonneShouldNotBeFound("matricule.equals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllPersonnesByMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where matricule in DEFAULT_MATRICULE or UPDATED_MATRICULE
        defaultPersonneShouldBeFound("matricule.in=" + DEFAULT_MATRICULE + "," + UPDATED_MATRICULE);

        // Get all the personneList where matricule equals to UPDATED_MATRICULE
        defaultPersonneShouldNotBeFound("matricule.in=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllPersonnesByMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where matricule is not null
        defaultPersonneShouldBeFound("matricule.specified=true");

        // Get all the personneList where matricule is null
        defaultPersonneShouldNotBeFound("matricule.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByMatriculeContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where matricule contains DEFAULT_MATRICULE
        defaultPersonneShouldBeFound("matricule.contains=" + DEFAULT_MATRICULE);

        // Get all the personneList where matricule contains UPDATED_MATRICULE
        defaultPersonneShouldNotBeFound("matricule.contains=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllPersonnesByMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where matricule does not contain DEFAULT_MATRICULE
        defaultPersonneShouldNotBeFound("matricule.doesNotContain=" + DEFAULT_MATRICULE);

        // Get all the personneList where matricule does not contain UPDATED_MATRICULE
        defaultPersonneShouldBeFound("matricule.doesNotContain=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom equals to DEFAULT_NOM
        defaultPersonneShouldBeFound("nom.equals=" + DEFAULT_NOM);

        // Get all the personneList where nom equals to UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom in DEFAULT_NOM or UPDATED_NOM
        defaultPersonneShouldBeFound("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM);

        // Get all the personneList where nom equals to UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom is not null
        defaultPersonneShouldBeFound("nom.specified=true");

        // Get all the personneList where nom is null
        defaultPersonneShouldNotBeFound("nom.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByNomContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom contains DEFAULT_NOM
        defaultPersonneShouldBeFound("nom.contains=" + DEFAULT_NOM);

        // Get all the personneList where nom contains UPDATED_NOM
        defaultPersonneShouldNotBeFound("nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where nom does not contain DEFAULT_NOM
        defaultPersonneShouldNotBeFound("nom.doesNotContain=" + DEFAULT_NOM);

        // Get all the personneList where nom does not contain UPDATED_NOM
        defaultPersonneShouldBeFound("nom.doesNotContain=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom equals to DEFAULT_PRENOM
        defaultPersonneShouldBeFound("prenom.equals=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom equals to UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom in DEFAULT_PRENOM or UPDATED_PRENOM
        defaultPersonneShouldBeFound("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM);

        // Get all the personneList where prenom equals to UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom is not null
        defaultPersonneShouldBeFound("prenom.specified=true");

        // Get all the personneList where prenom is null
        defaultPersonneShouldNotBeFound("prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom contains DEFAULT_PRENOM
        defaultPersonneShouldBeFound("prenom.contains=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom contains UPDATED_PRENOM
        defaultPersonneShouldNotBeFound("prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where prenom does not contain DEFAULT_PRENOM
        defaultPersonneShouldNotBeFound("prenom.doesNotContain=" + DEFAULT_PRENOM);

        // Get all the personneList where prenom does not contain UPDATED_PRENOM
        defaultPersonneShouldBeFound("prenom.doesNotContain=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllPersonnesByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where telephone equals to DEFAULT_TELEPHONE
        defaultPersonneShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the personneList where telephone equals to UPDATED_TELEPHONE
        defaultPersonneShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultPersonneShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the personneList where telephone equals to UPDATED_TELEPHONE
        defaultPersonneShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where telephone is not null
        defaultPersonneShouldBeFound("telephone.specified=true");

        // Get all the personneList where telephone is null
        defaultPersonneShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where telephone contains DEFAULT_TELEPHONE
        defaultPersonneShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the personneList where telephone contains UPDATED_TELEPHONE
        defaultPersonneShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where telephone does not contain DEFAULT_TELEPHONE
        defaultPersonneShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the personneList where telephone does not contain UPDATED_TELEPHONE
        defaultPersonneShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email equals to DEFAULT_EMAIL
        defaultPersonneShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the personneList where email equals to UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPersonneShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the personneList where email equals to UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email is not null
        defaultPersonneShouldBeFound("email.specified=true");

        // Get all the personneList where email is null
        defaultPersonneShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email contains DEFAULT_EMAIL
        defaultPersonneShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the personneList where email contains UPDATED_EMAIL
        defaultPersonneShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where email does not contain DEFAULT_EMAIL
        defaultPersonneShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the personneList where email does not contain UPDATED_EMAIL
        defaultPersonneShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance equals to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.equals=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.equals=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance in DEFAULT_DATE_NAISSANCE or UPDATED_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.in=" + DEFAULT_DATE_NAISSANCE + "," + UPDATED_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance equals to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.in=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is not null
        defaultPersonneShouldBeFound("dateNaissance.specified=true");

        // Get all the personneList where dateNaissance is null
        defaultPersonneShouldNotBeFound("dateNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is greater than or equal to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.greaterThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is greater than or equal to UPDATED_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.greaterThanOrEqual=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is less than or equal to DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.lessThanOrEqual=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is less than or equal to SMALLER_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.lessThanOrEqual=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is less than DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.lessThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is less than UPDATED_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.lessThan=" + UPDATED_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateNaissance is greater than DEFAULT_DATE_NAISSANCE
        defaultPersonneShouldNotBeFound("dateNaissance.greaterThan=" + DEFAULT_DATE_NAISSANCE);

        // Get all the personneList where dateNaissance is greater than SMALLER_DATE_NAISSANCE
        defaultPersonneShouldBeFound("dateNaissance.greaterThan=" + SMALLER_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance equals to DEFAULT_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.equals=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.equals=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance in DEFAULT_LIEU_NAISSANCE or UPDATED_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.in=" + DEFAULT_LIEU_NAISSANCE + "," + UPDATED_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance equals to UPDATED_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.in=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance is not null
        defaultPersonneShouldBeFound("lieuNaissance.specified=true");

        // Get all the personneList where lieuNaissance is null
        defaultPersonneShouldNotBeFound("lieuNaissance.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance is greater than or equal to DEFAULT_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.greaterThanOrEqual=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance is greater than or equal to UPDATED_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.greaterThanOrEqual=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance is less than or equal to DEFAULT_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.lessThanOrEqual=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance is less than or equal to SMALLER_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.lessThanOrEqual=" + SMALLER_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsLessThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance is less than DEFAULT_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.lessThan=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance is less than UPDATED_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.lessThan=" + UPDATED_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByLieuNaissanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where lieuNaissance is greater than DEFAULT_LIEU_NAISSANCE
        defaultPersonneShouldNotBeFound("lieuNaissance.greaterThan=" + DEFAULT_LIEU_NAISSANCE);

        // Get all the personneList where lieuNaissance is greater than SMALLER_LIEU_NAISSANCE
        defaultPersonneShouldBeFound("lieuNaissance.greaterThan=" + SMALLER_LIEU_NAISSANCE);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateInscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateInscription equals to DEFAULT_DATE_INSCRIPTION
        defaultPersonneShouldBeFound("dateInscription.equals=" + DEFAULT_DATE_INSCRIPTION);

        // Get all the personneList where dateInscription equals to UPDATED_DATE_INSCRIPTION
        defaultPersonneShouldNotBeFound("dateInscription.equals=" + UPDATED_DATE_INSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateInscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateInscription in DEFAULT_DATE_INSCRIPTION or UPDATED_DATE_INSCRIPTION
        defaultPersonneShouldBeFound("dateInscription.in=" + DEFAULT_DATE_INSCRIPTION + "," + UPDATED_DATE_INSCRIPTION);

        // Get all the personneList where dateInscription equals to UPDATED_DATE_INSCRIPTION
        defaultPersonneShouldNotBeFound("dateInscription.in=" + UPDATED_DATE_INSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateInscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateInscription is not null
        defaultPersonneShouldBeFound("dateInscription.specified=true");

        // Get all the personneList where dateInscription is null
        defaultPersonneShouldNotBeFound("dateInscription.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByProfessionIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where profession equals to DEFAULT_PROFESSION
        defaultPersonneShouldBeFound("profession.equals=" + DEFAULT_PROFESSION);

        // Get all the personneList where profession equals to UPDATED_PROFESSION
        defaultPersonneShouldNotBeFound("profession.equals=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllPersonnesByProfessionIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where profession in DEFAULT_PROFESSION or UPDATED_PROFESSION
        defaultPersonneShouldBeFound("profession.in=" + DEFAULT_PROFESSION + "," + UPDATED_PROFESSION);

        // Get all the personneList where profession equals to UPDATED_PROFESSION
        defaultPersonneShouldNotBeFound("profession.in=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllPersonnesByProfessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where profession is not null
        defaultPersonneShouldBeFound("profession.specified=true");

        // Get all the personneList where profession is null
        defaultPersonneShouldNotBeFound("profession.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByProfessionContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where profession contains DEFAULT_PROFESSION
        defaultPersonneShouldBeFound("profession.contains=" + DEFAULT_PROFESSION);

        // Get all the personneList where profession contains UPDATED_PROFESSION
        defaultPersonneShouldNotBeFound("profession.contains=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllPersonnesByProfessionNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where profession does not contain DEFAULT_PROFESSION
        defaultPersonneShouldNotBeFound("profession.doesNotContain=" + DEFAULT_PROFESSION);

        // Get all the personneList where profession does not contain UPDATED_PROFESSION
        defaultPersonneShouldBeFound("profession.doesNotContain=" + UPDATED_PROFESSION);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sexe equals to DEFAULT_SEXE
        defaultPersonneShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the personneList where sexe equals to UPDATED_SEXE
        defaultPersonneShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultPersonneShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the personneList where sexe equals to UPDATED_SEXE
        defaultPersonneShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    void getAllPersonnesBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where sexe is not null
        defaultPersonneShouldBeFound("sexe.specified=true");

        // Get all the personneList where sexe is null
        defaultPersonneShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByPhotoPathIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where photoPath equals to DEFAULT_PHOTO_PATH
        defaultPersonneShouldBeFound("photoPath.equals=" + DEFAULT_PHOTO_PATH);

        // Get all the personneList where photoPath equals to UPDATED_PHOTO_PATH
        defaultPersonneShouldNotBeFound("photoPath.equals=" + UPDATED_PHOTO_PATH);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhotoPathIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where photoPath in DEFAULT_PHOTO_PATH or UPDATED_PHOTO_PATH
        defaultPersonneShouldBeFound("photoPath.in=" + DEFAULT_PHOTO_PATH + "," + UPDATED_PHOTO_PATH);

        // Get all the personneList where photoPath equals to UPDATED_PHOTO_PATH
        defaultPersonneShouldNotBeFound("photoPath.in=" + UPDATED_PHOTO_PATH);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhotoPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where photoPath is not null
        defaultPersonneShouldBeFound("photoPath.specified=true");

        // Get all the personneList where photoPath is null
        defaultPersonneShouldNotBeFound("photoPath.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByPhotoPathContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where photoPath contains DEFAULT_PHOTO_PATH
        defaultPersonneShouldBeFound("photoPath.contains=" + DEFAULT_PHOTO_PATH);

        // Get all the personneList where photoPath contains UPDATED_PHOTO_PATH
        defaultPersonneShouldNotBeFound("photoPath.contains=" + UPDATED_PHOTO_PATH);
    }

    @Test
    @Transactional
    void getAllPersonnesByPhotoPathNotContainsSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where photoPath does not contain DEFAULT_PHOTO_PATH
        defaultPersonneShouldNotBeFound("photoPath.doesNotContain=" + DEFAULT_PHOTO_PATH);

        // Get all the personneList where photoPath does not contain UPDATED_PHOTO_PATH
        defaultPersonneShouldBeFound("photoPath.doesNotContain=" + UPDATED_PHOTO_PATH);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateIntegrationIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateIntegration equals to DEFAULT_DATE_INTEGRATION
        defaultPersonneShouldBeFound("dateIntegration.equals=" + DEFAULT_DATE_INTEGRATION);

        // Get all the personneList where dateIntegration equals to UPDATED_DATE_INTEGRATION
        defaultPersonneShouldNotBeFound("dateIntegration.equals=" + UPDATED_DATE_INTEGRATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateIntegrationIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateIntegration in DEFAULT_DATE_INTEGRATION or UPDATED_DATE_INTEGRATION
        defaultPersonneShouldBeFound("dateIntegration.in=" + DEFAULT_DATE_INTEGRATION + "," + UPDATED_DATE_INTEGRATION);

        // Get all the personneList where dateIntegration equals to UPDATED_DATE_INTEGRATION
        defaultPersonneShouldNotBeFound("dateIntegration.in=" + UPDATED_DATE_INTEGRATION);
    }

    @Test
    @Transactional
    void getAllPersonnesByDateIntegrationIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where dateIntegration is not null
        defaultPersonneShouldBeFound("dateIntegration.specified=true");

        // Get all the personneList where dateIntegration is null
        defaultPersonneShouldNotBeFound("dateIntegration.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByIsAdminIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isAdmin equals to DEFAULT_IS_ADMIN
        defaultPersonneShouldBeFound("isAdmin.equals=" + DEFAULT_IS_ADMIN);

        // Get all the personneList where isAdmin equals to UPDATED_IS_ADMIN
        defaultPersonneShouldNotBeFound("isAdmin.equals=" + UPDATED_IS_ADMIN);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsAdminIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isAdmin in DEFAULT_IS_ADMIN or UPDATED_IS_ADMIN
        defaultPersonneShouldBeFound("isAdmin.in=" + DEFAULT_IS_ADMIN + "," + UPDATED_IS_ADMIN);

        // Get all the personneList where isAdmin equals to UPDATED_IS_ADMIN
        defaultPersonneShouldNotBeFound("isAdmin.in=" + UPDATED_IS_ADMIN);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsAdminIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isAdmin is not null
        defaultPersonneShouldBeFound("isAdmin.specified=true");

        // Get all the personneList where isAdmin is null
        defaultPersonneShouldNotBeFound("isAdmin.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByIsDonateurIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isDonateur equals to DEFAULT_IS_DONATEUR
        defaultPersonneShouldBeFound("isDonateur.equals=" + DEFAULT_IS_DONATEUR);

        // Get all the personneList where isDonateur equals to UPDATED_IS_DONATEUR
        defaultPersonneShouldNotBeFound("isDonateur.equals=" + UPDATED_IS_DONATEUR);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsDonateurIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isDonateur in DEFAULT_IS_DONATEUR or UPDATED_IS_DONATEUR
        defaultPersonneShouldBeFound("isDonateur.in=" + DEFAULT_IS_DONATEUR + "," + UPDATED_IS_DONATEUR);

        // Get all the personneList where isDonateur equals to UPDATED_IS_DONATEUR
        defaultPersonneShouldNotBeFound("isDonateur.in=" + UPDATED_IS_DONATEUR);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsDonateurIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isDonateur is not null
        defaultPersonneShouldBeFound("isDonateur.specified=true");

        // Get all the personneList where isDonateur is null
        defaultPersonneShouldNotBeFound("isDonateur.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByIsBenevoleIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isBenevole equals to DEFAULT_IS_BENEVOLE
        defaultPersonneShouldBeFound("isBenevole.equals=" + DEFAULT_IS_BENEVOLE);

        // Get all the personneList where isBenevole equals to UPDATED_IS_BENEVOLE
        defaultPersonneShouldNotBeFound("isBenevole.equals=" + UPDATED_IS_BENEVOLE);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsBenevoleIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isBenevole in DEFAULT_IS_BENEVOLE or UPDATED_IS_BENEVOLE
        defaultPersonneShouldBeFound("isBenevole.in=" + DEFAULT_IS_BENEVOLE + "," + UPDATED_IS_BENEVOLE);

        // Get all the personneList where isBenevole equals to UPDATED_IS_BENEVOLE
        defaultPersonneShouldNotBeFound("isBenevole.in=" + UPDATED_IS_BENEVOLE);
    }

    @Test
    @Transactional
    void getAllPersonnesByIsBenevoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where isBenevole is not null
        defaultPersonneShouldBeFound("isBenevole.specified=true");

        // Get all the personneList where isBenevole is null
        defaultPersonneShouldNotBeFound("isBenevole.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByTypePersonneIsEqualToSomething() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where typePersonne equals to DEFAULT_TYPE_PERSONNE
        defaultPersonneShouldBeFound("typePersonne.equals=" + DEFAULT_TYPE_PERSONNE);

        // Get all the personneList where typePersonne equals to UPDATED_TYPE_PERSONNE
        defaultPersonneShouldNotBeFound("typePersonne.equals=" + UPDATED_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllPersonnesByTypePersonneIsInShouldWork() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where typePersonne in DEFAULT_TYPE_PERSONNE or UPDATED_TYPE_PERSONNE
        defaultPersonneShouldBeFound("typePersonne.in=" + DEFAULT_TYPE_PERSONNE + "," + UPDATED_TYPE_PERSONNE);

        // Get all the personneList where typePersonne equals to UPDATED_TYPE_PERSONNE
        defaultPersonneShouldNotBeFound("typePersonne.in=" + UPDATED_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllPersonnesByTypePersonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        // Get all the personneList where typePersonne is not null
        defaultPersonneShouldBeFound("typePersonne.specified=true");

        // Get all the personneList where typePersonne is null
        defaultPersonneShouldNotBeFound("typePersonne.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonnesByAdresseIsEqualToSomething() throws Exception {
        Adresse adresse;
        if (TestUtil.findAll(em, Adresse.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            adresse = AdresseResourceIT.createEntity(em);
        } else {
            adresse = TestUtil.findAll(em, Adresse.class).get(0);
        }
        em.persist(adresse);
        em.flush();
        personne.setAdresse(adresse);
        personneRepository.saveAndFlush(personne);
        Long adresseId = adresse.getId();

        // Get all the personneList where adresse equals to adresseId
        defaultPersonneShouldBeFound("adresseId.equals=" + adresseId);

        // Get all the personneList where adresse equals to (adresseId + 1)
        defaultPersonneShouldNotBeFound("adresseId.equals=" + (adresseId + 1));
    }

    @Test
    @Transactional
    void getAllPersonnesByContactIsEqualToSomething() throws Exception {
        Contact contact;
        if (TestUtil.findAll(em, Contact.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            contact = ContactResourceIT.createEntity(em);
        } else {
            contact = TestUtil.findAll(em, Contact.class).get(0);
        }
        em.persist(contact);
        em.flush();
        personne.addContact(contact);
        personneRepository.saveAndFlush(personne);
        Long contactId = contact.getId();

        // Get all the personneList where contact equals to contactId
        defaultPersonneShouldBeFound("contactId.equals=" + contactId);

        // Get all the personneList where contact equals to (contactId + 1)
        defaultPersonneShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }

    @Test
    @Transactional
    void getAllPersonnesByCompteRIBIsEqualToSomething() throws Exception {
        CompteRIB compteRIB;
        if (TestUtil.findAll(em, CompteRIB.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            compteRIB = CompteRIBResourceIT.createEntity(em);
        } else {
            compteRIB = TestUtil.findAll(em, CompteRIB.class).get(0);
        }
        em.persist(compteRIB);
        em.flush();
        personne.addCompteRIB(compteRIB);
        personneRepository.saveAndFlush(personne);
        Long compteRIBId = compteRIB.getId();

        // Get all the personneList where compteRIB equals to compteRIBId
        defaultPersonneShouldBeFound("compteRIBId.equals=" + compteRIBId);

        // Get all the personneList where compteRIB equals to (compteRIBId + 1)
        defaultPersonneShouldNotBeFound("compteRIBId.equals=" + (compteRIBId + 1));
    }

    @Test
    @Transactional
    void getAllPersonnesByHistoriquePersonneIsEqualToSomething() throws Exception {
        HistoriquePersonne historiquePersonne;
        if (TestUtil.findAll(em, HistoriquePersonne.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            historiquePersonne = HistoriquePersonneResourceIT.createEntity(em);
        } else {
            historiquePersonne = TestUtil.findAll(em, HistoriquePersonne.class).get(0);
        }
        em.persist(historiquePersonne);
        em.flush();
        personne.addHistoriquePersonne(historiquePersonne);
        personneRepository.saveAndFlush(personne);
        Long historiquePersonneId = historiquePersonne.getId();

        // Get all the personneList where historiquePersonne equals to historiquePersonneId
        defaultPersonneShouldBeFound("historiquePersonneId.equals=" + historiquePersonneId);

        // Get all the personneList where historiquePersonne equals to (historiquePersonneId + 1)
        defaultPersonneShouldNotBeFound("historiquePersonneId.equals=" + (historiquePersonneId + 1));
    }

    @Test
    @Transactional
    void getAllPersonnesByPresenceIsEqualToSomething() throws Exception {
        Presence presence;
        if (TestUtil.findAll(em, Presence.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            presence = PresenceResourceIT.createEntity(em);
        } else {
            presence = TestUtil.findAll(em, Presence.class).get(0);
        }
        em.persist(presence);
        em.flush();
        personne.addPresence(presence);
        personneRepository.saveAndFlush(personne);
        Long presenceId = presence.getId();

        // Get all the personneList where presence equals to presenceId
        defaultPersonneShouldBeFound("presenceId.equals=" + presenceId);

        // Get all the personneList where presence equals to (presenceId + 1)
        defaultPersonneShouldNotBeFound("presenceId.equals=" + (presenceId + 1));
    }

    @Test
    @Transactional
    void getAllPersonnesByFonctionAdherentIsEqualToSomething() throws Exception {
        FonctionAdherent fonctionAdherent;
        if (TestUtil.findAll(em, FonctionAdherent.class).isEmpty()) {
            personneRepository.saveAndFlush(personne);
            fonctionAdherent = FonctionAdherentResourceIT.createEntity(em);
        } else {
            fonctionAdherent = TestUtil.findAll(em, FonctionAdherent.class).get(0);
        }
        em.persist(fonctionAdherent);
        em.flush();
        personne.addFonctionAdherent(fonctionAdherent);
        personneRepository.saveAndFlush(personne);
        Long fonctionAdherentId = fonctionAdherent.getId();

        // Get all the personneList where fonctionAdherent equals to fonctionAdherentId
        defaultPersonneShouldBeFound("fonctionAdherentId.equals=" + fonctionAdherentId);

        // Get all the personneList where fonctionAdherent equals to (fonctionAdherentId + 1)
        defaultPersonneShouldNotBeFound("fonctionAdherentId.equals=" + (fonctionAdherentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonneShouldBeFound(String filter) throws Exception {
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personne.getId().intValue())))
            .andExpect(jsonPath("$.[*].idUser").value(hasItem(DEFAULT_ID_USER.intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION.intValue())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE.intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].photoPath").value(hasItem(DEFAULT_PHOTO_PATH)))
            .andExpect(jsonPath("$.[*].dateIntegration").value(hasItem(DEFAULT_DATE_INTEGRATION.toString())))
            .andExpect(jsonPath("$.[*].isAdmin").value(hasItem(DEFAULT_IS_ADMIN.booleanValue())))
            .andExpect(jsonPath("$.[*].isDonateur").value(hasItem(DEFAULT_IS_DONATEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].isBenevole").value(hasItem(DEFAULT_IS_BENEVOLE.booleanValue())))
            .andExpect(jsonPath("$.[*].typePersonne").value(hasItem(DEFAULT_TYPE_PERSONNE.toString())));

        // Check, that the count call also returns 1
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonneShouldNotBeFound(String filter) throws Exception {
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonne() throws Exception {
        // Get the personne
        restPersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne
        Personne updatedPersonne = personneRepository.findById(personne.getId()).get();
        // Disconnect from session so that the updates on updatedPersonne are not directly saved in db
        em.detach(updatedPersonne);
        updatedPersonne
            .idUser(UPDATED_ID_USER)
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .profession(UPDATED_PROFESSION)
            .sexe(UPDATED_SEXE)
            .photoPath(UPDATED_PHOTO_PATH)
            .dateIntegration(UPDATED_DATE_INTEGRATION)
            .isAdmin(UPDATED_IS_ADMIN)
            .isDonateur(UPDATED_IS_DONATEUR)
            .isBenevole(UPDATED_IS_BENEVOLE)
            .typePersonne(UPDATED_TYPE_PERSONNE);
        PersonneDTO personneDTO = personneMapper.toDto(updatedPersonne);

        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getIdUser()).isEqualTo(UPDATED_ID_USER);
        assertThat(testPersonne.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testPersonne.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPersonne.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testPersonne.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testPersonne.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPersonne.getPhotoPath()).isEqualTo(UPDATED_PHOTO_PATH);
        assertThat(testPersonne.getDateIntegration()).isEqualTo(UPDATED_DATE_INTEGRATION);
        assertThat(testPersonne.getIsAdmin()).isEqualTo(UPDATED_IS_ADMIN);
        assertThat(testPersonne.getIsDonateur()).isEqualTo(UPDATED_IS_DONATEUR);
        assertThat(testPersonne.getIsBenevole()).isEqualTo(UPDATED_IS_BENEVOLE);
        assertThat(testPersonne.getTypePersonne()).isEqualTo(UPDATED_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void putNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne
            .idUser(UPDATED_ID_USER)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .profession(UPDATED_PROFESSION)
            .sexe(UPDATED_SEXE)
            .dateIntegration(UPDATED_DATE_INTEGRATION);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getIdUser()).isEqualTo(UPDATED_ID_USER);
        assertThat(testPersonne.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testPersonne.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testPersonne.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testPersonne.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPersonne.getDateInscription()).isEqualTo(DEFAULT_DATE_INSCRIPTION);
        assertThat(testPersonne.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testPersonne.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPersonne.getPhotoPath()).isEqualTo(DEFAULT_PHOTO_PATH);
        assertThat(testPersonne.getDateIntegration()).isEqualTo(UPDATED_DATE_INTEGRATION);
        assertThat(testPersonne.getIsAdmin()).isEqualTo(DEFAULT_IS_ADMIN);
        assertThat(testPersonne.getIsDonateur()).isEqualTo(DEFAULT_IS_DONATEUR);
        assertThat(testPersonne.getIsBenevole()).isEqualTo(DEFAULT_IS_BENEVOLE);
        assertThat(testPersonne.getTypePersonne()).isEqualTo(DEFAULT_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void fullUpdatePersonneWithPatch() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeUpdate = personneRepository.findAll().size();

        // Update the personne using partial update
        Personne partialUpdatedPersonne = new Personne();
        partialUpdatedPersonne.setId(personne.getId());

        partialUpdatedPersonne
            .idUser(UPDATED_ID_USER)
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .matricule(UPDATED_MATRICULE)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .email(UPDATED_EMAIL)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .profession(UPDATED_PROFESSION)
            .sexe(UPDATED_SEXE)
            .photoPath(UPDATED_PHOTO_PATH)
            .dateIntegration(UPDATED_DATE_INTEGRATION)
            .isAdmin(UPDATED_IS_ADMIN)
            .isDonateur(UPDATED_IS_DONATEUR)
            .isBenevole(UPDATED_IS_BENEVOLE)
            .typePersonne(UPDATED_TYPE_PERSONNE);

        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonne))
            )
            .andExpect(status().isOk());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
        Personne testPersonne = personneList.get(personneList.size() - 1);
        assertThat(testPersonne.getIdUser()).isEqualTo(UPDATED_ID_USER);
        assertThat(testPersonne.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testPersonne.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testPersonne.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testPersonne.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testPersonne.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testPersonne.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonne.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testPersonne.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testPersonne.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testPersonne.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testPersonne.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPersonne.getPhotoPath()).isEqualTo(UPDATED_PHOTO_PATH);
        assertThat(testPersonne.getDateIntegration()).isEqualTo(UPDATED_DATE_INTEGRATION);
        assertThat(testPersonne.getIsAdmin()).isEqualTo(UPDATED_IS_ADMIN);
        assertThat(testPersonne.getIsDonateur()).isEqualTo(UPDATED_IS_DONATEUR);
        assertThat(testPersonne.getIsBenevole()).isEqualTo(UPDATED_IS_BENEVOLE);
        assertThat(testPersonne.getTypePersonne()).isEqualTo(UPDATED_TYPE_PERSONNE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonne() throws Exception {
        int databaseSizeBeforeUpdate = personneRepository.findAll().size();
        personne.setId(count.incrementAndGet());

        // Create the Personne
        PersonneDTO personneDTO = personneMapper.toDto(personne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Personne in the database
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonne() throws Exception {
        // Initialize the database
        personneRepository.saveAndFlush(personne);

        int databaseSizeBeforeDelete = personneRepository.findAll().size();

        // Delete the personne
        restPersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, personne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Personne> personneList = personneRepository.findAll();
        assertThat(personneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
