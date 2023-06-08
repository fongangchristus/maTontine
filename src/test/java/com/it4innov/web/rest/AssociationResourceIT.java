package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Association;
import com.it4innov.domain.DocumentAssociation;
import com.it4innov.domain.Exercise;
import com.it4innov.domain.Monnaie;
import com.it4innov.domain.enumeration.Langue;
import com.it4innov.repository.AssociationRepository;
import com.it4innov.service.criteria.AssociationCriteria;
import com.it4innov.service.dto.AssociationDTO;
import com.it4innov.service.mapper.AssociationMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link AssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssociationResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_DENOMINATION = "AAAAAAAAAA";
    private static final String UPDATED_DENOMINATION = "BBBBBBBBBB";

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_REGLEMENT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_REGLEMENT_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_STATUT_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_FUSEAU_HORAIRE = "AAAAAAAAAA";
    private static final String UPDATED_FUSEAU_HORAIRE = "BBBBBBBBBB";

    private static final Langue DEFAULT_LANGUE = Langue.FRENCH;
    private static final Langue UPDATED_LANGUE = Langue.ENGLISH;

    private static final String DEFAULT_PRESENTATION = "AAAAAAAAAA";
    private static final String UPDATED_PRESENTATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssociationRepository associationRepository;

    @Autowired
    private AssociationMapper associationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssociationMockMvc;

    private Association association;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Association createEntity(EntityManager em) {
        Association association = new Association()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .denomination(DEFAULT_DENOMINATION)
            .slogan(DEFAULT_SLOGAN)
            .logoPath(DEFAULT_LOGO_PATH)
            .reglementPath(DEFAULT_REGLEMENT_PATH)
            .statutPath(DEFAULT_STATUT_PATH)
            .description(DEFAULT_DESCRIPTION)
            .dateCreation(DEFAULT_DATE_CREATION)
            .fuseauHoraire(DEFAULT_FUSEAU_HORAIRE)
            .langue(DEFAULT_LANGUE)
            .presentation(DEFAULT_PRESENTATION);
        return association;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Association createUpdatedEntity(EntityManager em) {
        Association association = new Association()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .denomination(UPDATED_DENOMINATION)
            .slogan(UPDATED_SLOGAN)
            .logoPath(UPDATED_LOGO_PATH)
            .reglementPath(UPDATED_REGLEMENT_PATH)
            .statutPath(UPDATED_STATUT_PATH)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .fuseauHoraire(UPDATED_FUSEAU_HORAIRE)
            .langue(UPDATED_LANGUE)
            .presentation(UPDATED_PRESENTATION);
        return association;
    }

    @BeforeEach
    public void initTest() {
        association = createEntity(em);
    }

    @Test
    @Transactional
    void createAssociation() throws Exception {
        int databaseSizeBeforeCreate = associationRepository.findAll().size();
        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);
        restAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate + 1);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testAssociation.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testAssociation.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testAssociation.getLogoPath()).isEqualTo(DEFAULT_LOGO_PATH);
        assertThat(testAssociation.getReglementPath()).isEqualTo(DEFAULT_REGLEMENT_PATH);
        assertThat(testAssociation.getStatutPath()).isEqualTo(DEFAULT_STATUT_PATH);
        assertThat(testAssociation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssociation.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAssociation.getFuseauHoraire()).isEqualTo(DEFAULT_FUSEAU_HORAIRE);
        assertThat(testAssociation.getLangue()).isEqualTo(DEFAULT_LANGUE);
        assertThat(testAssociation.getPresentation()).isEqualTo(DEFAULT_PRESENTATION);
    }

    @Test
    @Transactional
    void createAssociationWithExistingId() throws Exception {
        // Create the Association with an existing ID
        association.setId(1L);
        AssociationDTO associationDTO = associationMapper.toDto(association);

        int databaseSizeBeforeCreate = associationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = associationRepository.findAll().size();
        // set the field null
        association.setCodeAssociation(null);

        // Create the Association, which fails.
        AssociationDTO associationDTO = associationMapper.toDto(association);

        restAssociationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssociations() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(association.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].reglementPath").value(hasItem(DEFAULT_REGLEMENT_PATH)))
            .andExpect(jsonPath("$.[*].statutPath").value(hasItem(DEFAULT_STATUT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].fuseauHoraire").value(hasItem(DEFAULT_FUSEAU_HORAIRE)))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE.toString())))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION)));
    }

    @Test
    @Transactional
    void getAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get the association
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL_ID, association.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(association.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.denomination").value(DEFAULT_DENOMINATION))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.logoPath").value(DEFAULT_LOGO_PATH))
            .andExpect(jsonPath("$.reglementPath").value(DEFAULT_REGLEMENT_PATH))
            .andExpect(jsonPath("$.statutPath").value(DEFAULT_STATUT_PATH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.fuseauHoraire").value(DEFAULT_FUSEAU_HORAIRE))
            .andExpect(jsonPath("$.langue").value(DEFAULT_LANGUE.toString()))
            .andExpect(jsonPath("$.presentation").value(DEFAULT_PRESENTATION));
    }

    @Test
    @Transactional
    void getAssociationsByIdFiltering() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        Long id = association.getId();

        defaultAssociationShouldBeFound("id.equals=" + id);
        defaultAssociationShouldNotBeFound("id.notEquals=" + id);

        defaultAssociationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssociationShouldNotBeFound("id.greaterThan=" + id);

        defaultAssociationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssociationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssociationsByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultAssociationShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the associationList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultAssociationShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultAssociationShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the associationList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultAssociationShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where codeAssociation is not null
        defaultAssociationShouldBeFound("codeAssociation.specified=true");

        // Get all the associationList where codeAssociation is null
        defaultAssociationShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultAssociationShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the associationList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultAssociationShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultAssociationShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the associationList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultAssociationShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDenominationIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where denomination equals to DEFAULT_DENOMINATION
        defaultAssociationShouldBeFound("denomination.equals=" + DEFAULT_DENOMINATION);

        // Get all the associationList where denomination equals to UPDATED_DENOMINATION
        defaultAssociationShouldNotBeFound("denomination.equals=" + UPDATED_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDenominationIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where denomination in DEFAULT_DENOMINATION or UPDATED_DENOMINATION
        defaultAssociationShouldBeFound("denomination.in=" + DEFAULT_DENOMINATION + "," + UPDATED_DENOMINATION);

        // Get all the associationList where denomination equals to UPDATED_DENOMINATION
        defaultAssociationShouldNotBeFound("denomination.in=" + UPDATED_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDenominationIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where denomination is not null
        defaultAssociationShouldBeFound("denomination.specified=true");

        // Get all the associationList where denomination is null
        defaultAssociationShouldNotBeFound("denomination.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByDenominationContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where denomination contains DEFAULT_DENOMINATION
        defaultAssociationShouldBeFound("denomination.contains=" + DEFAULT_DENOMINATION);

        // Get all the associationList where denomination contains UPDATED_DENOMINATION
        defaultAssociationShouldNotBeFound("denomination.contains=" + UPDATED_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDenominationNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where denomination does not contain DEFAULT_DENOMINATION
        defaultAssociationShouldNotBeFound("denomination.doesNotContain=" + DEFAULT_DENOMINATION);

        // Get all the associationList where denomination does not contain UPDATED_DENOMINATION
        defaultAssociationShouldBeFound("denomination.doesNotContain=" + UPDATED_DENOMINATION);
    }

    @Test
    @Transactional
    void getAllAssociationsBySloganIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where slogan equals to DEFAULT_SLOGAN
        defaultAssociationShouldBeFound("slogan.equals=" + DEFAULT_SLOGAN);

        // Get all the associationList where slogan equals to UPDATED_SLOGAN
        defaultAssociationShouldNotBeFound("slogan.equals=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllAssociationsBySloganIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where slogan in DEFAULT_SLOGAN or UPDATED_SLOGAN
        defaultAssociationShouldBeFound("slogan.in=" + DEFAULT_SLOGAN + "," + UPDATED_SLOGAN);

        // Get all the associationList where slogan equals to UPDATED_SLOGAN
        defaultAssociationShouldNotBeFound("slogan.in=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllAssociationsBySloganIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where slogan is not null
        defaultAssociationShouldBeFound("slogan.specified=true");

        // Get all the associationList where slogan is null
        defaultAssociationShouldNotBeFound("slogan.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsBySloganContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where slogan contains DEFAULT_SLOGAN
        defaultAssociationShouldBeFound("slogan.contains=" + DEFAULT_SLOGAN);

        // Get all the associationList where slogan contains UPDATED_SLOGAN
        defaultAssociationShouldNotBeFound("slogan.contains=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllAssociationsBySloganNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where slogan does not contain DEFAULT_SLOGAN
        defaultAssociationShouldNotBeFound("slogan.doesNotContain=" + DEFAULT_SLOGAN);

        // Get all the associationList where slogan does not contain UPDATED_SLOGAN
        defaultAssociationShouldBeFound("slogan.doesNotContain=" + UPDATED_SLOGAN);
    }

    @Test
    @Transactional
    void getAllAssociationsByLogoPathIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where logoPath equals to DEFAULT_LOGO_PATH
        defaultAssociationShouldBeFound("logoPath.equals=" + DEFAULT_LOGO_PATH);

        // Get all the associationList where logoPath equals to UPDATED_LOGO_PATH
        defaultAssociationShouldNotBeFound("logoPath.equals=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByLogoPathIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where logoPath in DEFAULT_LOGO_PATH or UPDATED_LOGO_PATH
        defaultAssociationShouldBeFound("logoPath.in=" + DEFAULT_LOGO_PATH + "," + UPDATED_LOGO_PATH);

        // Get all the associationList where logoPath equals to UPDATED_LOGO_PATH
        defaultAssociationShouldNotBeFound("logoPath.in=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByLogoPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where logoPath is not null
        defaultAssociationShouldBeFound("logoPath.specified=true");

        // Get all the associationList where logoPath is null
        defaultAssociationShouldNotBeFound("logoPath.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByLogoPathContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where logoPath contains DEFAULT_LOGO_PATH
        defaultAssociationShouldBeFound("logoPath.contains=" + DEFAULT_LOGO_PATH);

        // Get all the associationList where logoPath contains UPDATED_LOGO_PATH
        defaultAssociationShouldNotBeFound("logoPath.contains=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByLogoPathNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where logoPath does not contain DEFAULT_LOGO_PATH
        defaultAssociationShouldNotBeFound("logoPath.doesNotContain=" + DEFAULT_LOGO_PATH);

        // Get all the associationList where logoPath does not contain UPDATED_LOGO_PATH
        defaultAssociationShouldBeFound("logoPath.doesNotContain=" + UPDATED_LOGO_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByReglementPathIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where reglementPath equals to DEFAULT_REGLEMENT_PATH
        defaultAssociationShouldBeFound("reglementPath.equals=" + DEFAULT_REGLEMENT_PATH);

        // Get all the associationList where reglementPath equals to UPDATED_REGLEMENT_PATH
        defaultAssociationShouldNotBeFound("reglementPath.equals=" + UPDATED_REGLEMENT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByReglementPathIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where reglementPath in DEFAULT_REGLEMENT_PATH or UPDATED_REGLEMENT_PATH
        defaultAssociationShouldBeFound("reglementPath.in=" + DEFAULT_REGLEMENT_PATH + "," + UPDATED_REGLEMENT_PATH);

        // Get all the associationList where reglementPath equals to UPDATED_REGLEMENT_PATH
        defaultAssociationShouldNotBeFound("reglementPath.in=" + UPDATED_REGLEMENT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByReglementPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where reglementPath is not null
        defaultAssociationShouldBeFound("reglementPath.specified=true");

        // Get all the associationList where reglementPath is null
        defaultAssociationShouldNotBeFound("reglementPath.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByReglementPathContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where reglementPath contains DEFAULT_REGLEMENT_PATH
        defaultAssociationShouldBeFound("reglementPath.contains=" + DEFAULT_REGLEMENT_PATH);

        // Get all the associationList where reglementPath contains UPDATED_REGLEMENT_PATH
        defaultAssociationShouldNotBeFound("reglementPath.contains=" + UPDATED_REGLEMENT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByReglementPathNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where reglementPath does not contain DEFAULT_REGLEMENT_PATH
        defaultAssociationShouldNotBeFound("reglementPath.doesNotContain=" + DEFAULT_REGLEMENT_PATH);

        // Get all the associationList where reglementPath does not contain UPDATED_REGLEMENT_PATH
        defaultAssociationShouldBeFound("reglementPath.doesNotContain=" + UPDATED_REGLEMENT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByStatutPathIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where statutPath equals to DEFAULT_STATUT_PATH
        defaultAssociationShouldBeFound("statutPath.equals=" + DEFAULT_STATUT_PATH);

        // Get all the associationList where statutPath equals to UPDATED_STATUT_PATH
        defaultAssociationShouldNotBeFound("statutPath.equals=" + UPDATED_STATUT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByStatutPathIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where statutPath in DEFAULT_STATUT_PATH or UPDATED_STATUT_PATH
        defaultAssociationShouldBeFound("statutPath.in=" + DEFAULT_STATUT_PATH + "," + UPDATED_STATUT_PATH);

        // Get all the associationList where statutPath equals to UPDATED_STATUT_PATH
        defaultAssociationShouldNotBeFound("statutPath.in=" + UPDATED_STATUT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByStatutPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where statutPath is not null
        defaultAssociationShouldBeFound("statutPath.specified=true");

        // Get all the associationList where statutPath is null
        defaultAssociationShouldNotBeFound("statutPath.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByStatutPathContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where statutPath contains DEFAULT_STATUT_PATH
        defaultAssociationShouldBeFound("statutPath.contains=" + DEFAULT_STATUT_PATH);

        // Get all the associationList where statutPath contains UPDATED_STATUT_PATH
        defaultAssociationShouldNotBeFound("statutPath.contains=" + UPDATED_STATUT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByStatutPathNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where statutPath does not contain DEFAULT_STATUT_PATH
        defaultAssociationShouldNotBeFound("statutPath.doesNotContain=" + DEFAULT_STATUT_PATH);

        // Get all the associationList where statutPath does not contain UPDATED_STATUT_PATH
        defaultAssociationShouldBeFound("statutPath.doesNotContain=" + UPDATED_STATUT_PATH);
    }

    @Test
    @Transactional
    void getAllAssociationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where description equals to DEFAULT_DESCRIPTION
        defaultAssociationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the associationList where description equals to UPDATED_DESCRIPTION
        defaultAssociationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAssociationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the associationList where description equals to UPDATED_DESCRIPTION
        defaultAssociationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where description is not null
        defaultAssociationShouldBeFound("description.specified=true");

        // Get all the associationList where description is null
        defaultAssociationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where description contains DEFAULT_DESCRIPTION
        defaultAssociationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the associationList where description contains UPDATED_DESCRIPTION
        defaultAssociationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where description does not contain DEFAULT_DESCRIPTION
        defaultAssociationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the associationList where description does not contain UPDATED_DESCRIPTION
        defaultAssociationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the associationList where dateCreation equals to UPDATED_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the associationList where dateCreation equals to UPDATED_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation is not null
        defaultAssociationShouldBeFound("dateCreation.specified=true");

        // Get all the associationList where dateCreation is null
        defaultAssociationShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the associationList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the associationList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the associationList where dateCreation is less than UPDATED_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultAssociationShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the associationList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultAssociationShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByFuseauHoraireIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where fuseauHoraire equals to DEFAULT_FUSEAU_HORAIRE
        defaultAssociationShouldBeFound("fuseauHoraire.equals=" + DEFAULT_FUSEAU_HORAIRE);

        // Get all the associationList where fuseauHoraire equals to UPDATED_FUSEAU_HORAIRE
        defaultAssociationShouldNotBeFound("fuseauHoraire.equals=" + UPDATED_FUSEAU_HORAIRE);
    }

    @Test
    @Transactional
    void getAllAssociationsByFuseauHoraireIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where fuseauHoraire in DEFAULT_FUSEAU_HORAIRE or UPDATED_FUSEAU_HORAIRE
        defaultAssociationShouldBeFound("fuseauHoraire.in=" + DEFAULT_FUSEAU_HORAIRE + "," + UPDATED_FUSEAU_HORAIRE);

        // Get all the associationList where fuseauHoraire equals to UPDATED_FUSEAU_HORAIRE
        defaultAssociationShouldNotBeFound("fuseauHoraire.in=" + UPDATED_FUSEAU_HORAIRE);
    }

    @Test
    @Transactional
    void getAllAssociationsByFuseauHoraireIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where fuseauHoraire is not null
        defaultAssociationShouldBeFound("fuseauHoraire.specified=true");

        // Get all the associationList where fuseauHoraire is null
        defaultAssociationShouldNotBeFound("fuseauHoraire.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByFuseauHoraireContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where fuseauHoraire contains DEFAULT_FUSEAU_HORAIRE
        defaultAssociationShouldBeFound("fuseauHoraire.contains=" + DEFAULT_FUSEAU_HORAIRE);

        // Get all the associationList where fuseauHoraire contains UPDATED_FUSEAU_HORAIRE
        defaultAssociationShouldNotBeFound("fuseauHoraire.contains=" + UPDATED_FUSEAU_HORAIRE);
    }

    @Test
    @Transactional
    void getAllAssociationsByFuseauHoraireNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where fuseauHoraire does not contain DEFAULT_FUSEAU_HORAIRE
        defaultAssociationShouldNotBeFound("fuseauHoraire.doesNotContain=" + DEFAULT_FUSEAU_HORAIRE);

        // Get all the associationList where fuseauHoraire does not contain UPDATED_FUSEAU_HORAIRE
        defaultAssociationShouldBeFound("fuseauHoraire.doesNotContain=" + UPDATED_FUSEAU_HORAIRE);
    }

    @Test
    @Transactional
    void getAllAssociationsByLangueIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where langue equals to DEFAULT_LANGUE
        defaultAssociationShouldBeFound("langue.equals=" + DEFAULT_LANGUE);

        // Get all the associationList where langue equals to UPDATED_LANGUE
        defaultAssociationShouldNotBeFound("langue.equals=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllAssociationsByLangueIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where langue in DEFAULT_LANGUE or UPDATED_LANGUE
        defaultAssociationShouldBeFound("langue.in=" + DEFAULT_LANGUE + "," + UPDATED_LANGUE);

        // Get all the associationList where langue equals to UPDATED_LANGUE
        defaultAssociationShouldNotBeFound("langue.in=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllAssociationsByLangueIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where langue is not null
        defaultAssociationShouldBeFound("langue.specified=true");

        // Get all the associationList where langue is null
        defaultAssociationShouldNotBeFound("langue.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByPresentationIsEqualToSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where presentation equals to DEFAULT_PRESENTATION
        defaultAssociationShouldBeFound("presentation.equals=" + DEFAULT_PRESENTATION);

        // Get all the associationList where presentation equals to UPDATED_PRESENTATION
        defaultAssociationShouldNotBeFound("presentation.equals=" + UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByPresentationIsInShouldWork() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where presentation in DEFAULT_PRESENTATION or UPDATED_PRESENTATION
        defaultAssociationShouldBeFound("presentation.in=" + DEFAULT_PRESENTATION + "," + UPDATED_PRESENTATION);

        // Get all the associationList where presentation equals to UPDATED_PRESENTATION
        defaultAssociationShouldNotBeFound("presentation.in=" + UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByPresentationIsNullOrNotNull() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where presentation is not null
        defaultAssociationShouldBeFound("presentation.specified=true");

        // Get all the associationList where presentation is null
        defaultAssociationShouldNotBeFound("presentation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssociationsByPresentationContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where presentation contains DEFAULT_PRESENTATION
        defaultAssociationShouldBeFound("presentation.contains=" + DEFAULT_PRESENTATION);

        // Get all the associationList where presentation contains UPDATED_PRESENTATION
        defaultAssociationShouldNotBeFound("presentation.contains=" + UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByPresentationNotContainsSomething() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        // Get all the associationList where presentation does not contain DEFAULT_PRESENTATION
        defaultAssociationShouldNotBeFound("presentation.doesNotContain=" + DEFAULT_PRESENTATION);

        // Get all the associationList where presentation does not contain UPDATED_PRESENTATION
        defaultAssociationShouldBeFound("presentation.doesNotContain=" + UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void getAllAssociationsByExerciseIsEqualToSomething() throws Exception {
        Exercise exercise;
        if (TestUtil.findAll(em, Exercise.class).isEmpty()) {
            associationRepository.saveAndFlush(association);
            exercise = ExerciseResourceIT.createEntity(em);
        } else {
            exercise = TestUtil.findAll(em, Exercise.class).get(0);
        }
        em.persist(exercise);
        em.flush();
        association.addExercise(exercise);
        associationRepository.saveAndFlush(association);
        Long exerciseId = exercise.getId();

        // Get all the associationList where exercise equals to exerciseId
        defaultAssociationShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the associationList where exercise equals to (exerciseId + 1)
        defaultAssociationShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    @Test
    @Transactional
    void getAllAssociationsByDocumentAssociationIsEqualToSomething() throws Exception {
        DocumentAssociation documentAssociation;
        if (TestUtil.findAll(em, DocumentAssociation.class).isEmpty()) {
            associationRepository.saveAndFlush(association);
            documentAssociation = DocumentAssociationResourceIT.createEntity(em);
        } else {
            documentAssociation = TestUtil.findAll(em, DocumentAssociation.class).get(0);
        }
        em.persist(documentAssociation);
        em.flush();
        association.addDocumentAssociation(documentAssociation);
        associationRepository.saveAndFlush(association);
        Long documentAssociationId = documentAssociation.getId();

        // Get all the associationList where documentAssociation equals to documentAssociationId
        defaultAssociationShouldBeFound("documentAssociationId.equals=" + documentAssociationId);

        // Get all the associationList where documentAssociation equals to (documentAssociationId + 1)
        defaultAssociationShouldNotBeFound("documentAssociationId.equals=" + (documentAssociationId + 1));
    }

    @Test
    @Transactional
    void getAllAssociationsByMonnaieIsEqualToSomething() throws Exception {
        Monnaie monnaie;
        if (TestUtil.findAll(em, Monnaie.class).isEmpty()) {
            associationRepository.saveAndFlush(association);
            monnaie = MonnaieResourceIT.createEntity(em);
        } else {
            monnaie = TestUtil.findAll(em, Monnaie.class).get(0);
        }
        em.persist(monnaie);
        em.flush();
        association.setMonnaie(monnaie);
        associationRepository.saveAndFlush(association);
        Long monnaieId = monnaie.getId();

        // Get all the associationList where monnaie equals to monnaieId
        defaultAssociationShouldBeFound("monnaieId.equals=" + monnaieId);

        // Get all the associationList where monnaie equals to (monnaieId + 1)
        defaultAssociationShouldNotBeFound("monnaieId.equals=" + (monnaieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssociationShouldBeFound(String filter) throws Exception {
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(association.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].denomination").value(hasItem(DEFAULT_DENOMINATION)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].logoPath").value(hasItem(DEFAULT_LOGO_PATH)))
            .andExpect(jsonPath("$.[*].reglementPath").value(hasItem(DEFAULT_REGLEMENT_PATH)))
            .andExpect(jsonPath("$.[*].statutPath").value(hasItem(DEFAULT_STATUT_PATH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].fuseauHoraire").value(hasItem(DEFAULT_FUSEAU_HORAIRE)))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE.toString())))
            .andExpect(jsonPath("$.[*].presentation").value(hasItem(DEFAULT_PRESENTATION)));

        // Check, that the count call also returns 1
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssociationShouldNotBeFound(String filter) throws Exception {
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssociationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssociation() throws Exception {
        // Get the association
        restAssociationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Update the association
        Association updatedAssociation = associationRepository.findById(association.getId()).get();
        // Disconnect from session so that the updates on updatedAssociation are not directly saved in db
        em.detach(updatedAssociation);
        updatedAssociation
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .denomination(UPDATED_DENOMINATION)
            .slogan(UPDATED_SLOGAN)
            .logoPath(UPDATED_LOGO_PATH)
            .reglementPath(UPDATED_REGLEMENT_PATH)
            .statutPath(UPDATED_STATUT_PATH)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .fuseauHoraire(UPDATED_FUSEAU_HORAIRE)
            .langue(UPDATED_LANGUE)
            .presentation(UPDATED_PRESENTATION);
        AssociationDTO associationDTO = associationMapper.toDto(updatedAssociation);

        restAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, associationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testAssociation.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testAssociation.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testAssociation.getLogoPath()).isEqualTo(UPDATED_LOGO_PATH);
        assertThat(testAssociation.getReglementPath()).isEqualTo(UPDATED_REGLEMENT_PATH);
        assertThat(testAssociation.getStatutPath()).isEqualTo(UPDATED_STATUT_PATH);
        assertThat(testAssociation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssociation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAssociation.getFuseauHoraire()).isEqualTo(UPDATED_FUSEAU_HORAIRE);
        assertThat(testAssociation.getLangue()).isEqualTo(UPDATED_LANGUE);
        assertThat(testAssociation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void putNonExistingAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, associationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(associationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssociationWithPatch() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Update the association using partial update
        Association partialUpdatedAssociation = new Association();
        partialUpdatedAssociation.setId(association.getId());

        partialUpdatedAssociation
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .slogan(UPDATED_SLOGAN)
            .logoPath(UPDATED_LOGO_PATH)
            .statutPath(UPDATED_STATUT_PATH)
            .dateCreation(UPDATED_DATE_CREATION)
            .fuseauHoraire(UPDATED_FUSEAU_HORAIRE)
            .presentation(UPDATED_PRESENTATION);

        restAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociation))
            )
            .andExpect(status().isOk());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testAssociation.getDenomination()).isEqualTo(DEFAULT_DENOMINATION);
        assertThat(testAssociation.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testAssociation.getLogoPath()).isEqualTo(UPDATED_LOGO_PATH);
        assertThat(testAssociation.getReglementPath()).isEqualTo(DEFAULT_REGLEMENT_PATH);
        assertThat(testAssociation.getStatutPath()).isEqualTo(UPDATED_STATUT_PATH);
        assertThat(testAssociation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAssociation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAssociation.getFuseauHoraire()).isEqualTo(UPDATED_FUSEAU_HORAIRE);
        assertThat(testAssociation.getLangue()).isEqualTo(DEFAULT_LANGUE);
        assertThat(testAssociation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void fullUpdateAssociationWithPatch() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        int databaseSizeBeforeUpdate = associationRepository.findAll().size();

        // Update the association using partial update
        Association partialUpdatedAssociation = new Association();
        partialUpdatedAssociation.setId(association.getId());

        partialUpdatedAssociation
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .denomination(UPDATED_DENOMINATION)
            .slogan(UPDATED_SLOGAN)
            .logoPath(UPDATED_LOGO_PATH)
            .reglementPath(UPDATED_REGLEMENT_PATH)
            .statutPath(UPDATED_STATUT_PATH)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .fuseauHoraire(UPDATED_FUSEAU_HORAIRE)
            .langue(UPDATED_LANGUE)
            .presentation(UPDATED_PRESENTATION);

        restAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssociation))
            )
            .andExpect(status().isOk());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
        Association testAssociation = associationList.get(associationList.size() - 1);
        assertThat(testAssociation.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testAssociation.getDenomination()).isEqualTo(UPDATED_DENOMINATION);
        assertThat(testAssociation.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testAssociation.getLogoPath()).isEqualTo(UPDATED_LOGO_PATH);
        assertThat(testAssociation.getReglementPath()).isEqualTo(UPDATED_REGLEMENT_PATH);
        assertThat(testAssociation.getStatutPath()).isEqualTo(UPDATED_STATUT_PATH);
        assertThat(testAssociation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAssociation.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAssociation.getFuseauHoraire()).isEqualTo(UPDATED_FUSEAU_HORAIRE);
        assertThat(testAssociation.getLangue()).isEqualTo(UPDATED_LANGUE);
        assertThat(testAssociation.getPresentation()).isEqualTo(UPDATED_PRESENTATION);
    }

    @Test
    @Transactional
    void patchNonExistingAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, associationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssociation() throws Exception {
        int databaseSizeBeforeUpdate = associationRepository.findAll().size();
        association.setId(count.incrementAndGet());

        // Create the Association
        AssociationDTO associationDTO = associationMapper.toDto(association);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(associationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Association in the database
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssociation() throws Exception {
        // Initialize the database
        associationRepository.saveAndFlush(association);

        int databaseSizeBeforeDelete = associationRepository.findAll().size();

        // Delete the association
        restAssociationMockMvc
            .perform(delete(ENTITY_API_URL_ID, association.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Association> associationList = associationRepository.findAll();
        assertThat(associationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
