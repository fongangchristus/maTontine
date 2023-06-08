package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.PaiementTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.domain.enumeration.EtatCotisation;
import com.it4innov.repository.CotisationTontineRepository;
import com.it4innov.service.criteria.CotisationTontineCriteria;
import com.it4innov.service.dto.CotisationTontineDTO;
import com.it4innov.service.mapper.CotisationTontineMapper;
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
 * Integration tests for the {@link CotisationTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CotisationTontineResourceIT {

    private static final Double DEFAULT_MONTANT_COTISE = 1D;
    private static final Double UPDATED_MONTANT_COTISE = 2D;
    private static final Double SMALLER_MONTANT_COTISE = 1D - 1D;

    private static final String DEFAULT_PIECE_JUSTIF_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PIECE_JUSTIF_PATH = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_COTISATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COTISATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_COTISATION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_VALIDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_VALIDATION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final EtatCotisation DEFAULT_ETAT = EtatCotisation.ECHEC;
    private static final EtatCotisation UPDATED_ETAT = EtatCotisation.COTISE;

    private static final String ENTITY_API_URL = "/api/cotisation-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CotisationTontineRepository cotisationTontineRepository;

    @Autowired
    private CotisationTontineMapper cotisationTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCotisationTontineMockMvc;

    private CotisationTontine cotisationTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CotisationTontine createEntity(EntityManager em) {
        CotisationTontine cotisationTontine = new CotisationTontine()
            .montantCotise(DEFAULT_MONTANT_COTISE)
            .pieceJustifPath(DEFAULT_PIECE_JUSTIF_PATH)
            .dateCotisation(DEFAULT_DATE_COTISATION)
            .dateValidation(DEFAULT_DATE_VALIDATION)
            .commentaire(DEFAULT_COMMENTAIRE)
            .etat(DEFAULT_ETAT);
        return cotisationTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CotisationTontine createUpdatedEntity(EntityManager em) {
        CotisationTontine cotisationTontine = new CotisationTontine()
            .montantCotise(UPDATED_MONTANT_COTISE)
            .pieceJustifPath(UPDATED_PIECE_JUSTIF_PATH)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .etat(UPDATED_ETAT);
        return cotisationTontine;
    }

    @BeforeEach
    public void initTest() {
        cotisationTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createCotisationTontine() throws Exception {
        int databaseSizeBeforeCreate = cotisationTontineRepository.findAll().size();
        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);
        restCotisationTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeCreate + 1);
        CotisationTontine testCotisationTontine = cotisationTontineList.get(cotisationTontineList.size() - 1);
        assertThat(testCotisationTontine.getMontantCotise()).isEqualTo(DEFAULT_MONTANT_COTISE);
        assertThat(testCotisationTontine.getPieceJustifPath()).isEqualTo(DEFAULT_PIECE_JUSTIF_PATH);
        assertThat(testCotisationTontine.getDateCotisation()).isEqualTo(DEFAULT_DATE_COTISATION);
        assertThat(testCotisationTontine.getDateValidation()).isEqualTo(DEFAULT_DATE_VALIDATION);
        assertThat(testCotisationTontine.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
        assertThat(testCotisationTontine.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void createCotisationTontineWithExistingId() throws Exception {
        // Create the CotisationTontine with an existing ID
        cotisationTontine.setId(1L);
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        int databaseSizeBeforeCreate = cotisationTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCotisationTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCotisationTontines() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cotisationTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantCotise").value(hasItem(DEFAULT_MONTANT_COTISE.doubleValue())))
            .andExpect(jsonPath("$.[*].pieceJustifPath").value(hasItem(DEFAULT_PIECE_JUSTIF_PATH)))
            .andExpect(jsonPath("$.[*].dateCotisation").value(hasItem(DEFAULT_DATE_COTISATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));
    }

    @Test
    @Transactional
    void getCotisationTontine() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get the cotisationTontine
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, cotisationTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cotisationTontine.getId().intValue()))
            .andExpect(jsonPath("$.montantCotise").value(DEFAULT_MONTANT_COTISE.doubleValue()))
            .andExpect(jsonPath("$.pieceJustifPath").value(DEFAULT_PIECE_JUSTIF_PATH))
            .andExpect(jsonPath("$.dateCotisation").value(DEFAULT_DATE_COTISATION.toString()))
            .andExpect(jsonPath("$.dateValidation").value(DEFAULT_DATE_VALIDATION.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()));
    }

    @Test
    @Transactional
    void getCotisationTontinesByIdFiltering() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        Long id = cotisationTontine.getId();

        defaultCotisationTontineShouldBeFound("id.equals=" + id);
        defaultCotisationTontineShouldNotBeFound("id.notEquals=" + id);

        defaultCotisationTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCotisationTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultCotisationTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCotisationTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise equals to DEFAULT_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.equals=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise equals to UPDATED_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.equals=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise in DEFAULT_MONTANT_COTISE or UPDATED_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.in=" + DEFAULT_MONTANT_COTISE + "," + UPDATED_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise equals to UPDATED_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.in=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise is not null
        defaultCotisationTontineShouldBeFound("montantCotise.specified=true");

        // Get all the cotisationTontineList where montantCotise is null
        defaultCotisationTontineShouldNotBeFound("montantCotise.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise is greater than or equal to DEFAULT_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.greaterThanOrEqual=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise is greater than or equal to UPDATED_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.greaterThanOrEqual=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise is less than or equal to DEFAULT_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.lessThanOrEqual=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise is less than or equal to SMALLER_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.lessThanOrEqual=" + SMALLER_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsLessThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise is less than DEFAULT_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.lessThan=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise is less than UPDATED_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.lessThan=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByMontantCotiseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where montantCotise is greater than DEFAULT_MONTANT_COTISE
        defaultCotisationTontineShouldNotBeFound("montantCotise.greaterThan=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationTontineList where montantCotise is greater than SMALLER_MONTANT_COTISE
        defaultCotisationTontineShouldBeFound("montantCotise.greaterThan=" + SMALLER_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPieceJustifPathIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where pieceJustifPath equals to DEFAULT_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldBeFound("pieceJustifPath.equals=" + DEFAULT_PIECE_JUSTIF_PATH);

        // Get all the cotisationTontineList where pieceJustifPath equals to UPDATED_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldNotBeFound("pieceJustifPath.equals=" + UPDATED_PIECE_JUSTIF_PATH);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPieceJustifPathIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where pieceJustifPath in DEFAULT_PIECE_JUSTIF_PATH or UPDATED_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldBeFound("pieceJustifPath.in=" + DEFAULT_PIECE_JUSTIF_PATH + "," + UPDATED_PIECE_JUSTIF_PATH);

        // Get all the cotisationTontineList where pieceJustifPath equals to UPDATED_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldNotBeFound("pieceJustifPath.in=" + UPDATED_PIECE_JUSTIF_PATH);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPieceJustifPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where pieceJustifPath is not null
        defaultCotisationTontineShouldBeFound("pieceJustifPath.specified=true");

        // Get all the cotisationTontineList where pieceJustifPath is null
        defaultCotisationTontineShouldNotBeFound("pieceJustifPath.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPieceJustifPathContainsSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where pieceJustifPath contains DEFAULT_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldBeFound("pieceJustifPath.contains=" + DEFAULT_PIECE_JUSTIF_PATH);

        // Get all the cotisationTontineList where pieceJustifPath contains UPDATED_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldNotBeFound("pieceJustifPath.contains=" + UPDATED_PIECE_JUSTIF_PATH);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPieceJustifPathNotContainsSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where pieceJustifPath does not contain DEFAULT_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldNotBeFound("pieceJustifPath.doesNotContain=" + DEFAULT_PIECE_JUSTIF_PATH);

        // Get all the cotisationTontineList where pieceJustifPath does not contain UPDATED_PIECE_JUSTIF_PATH
        defaultCotisationTontineShouldBeFound("pieceJustifPath.doesNotContain=" + UPDATED_PIECE_JUSTIF_PATH);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation equals to DEFAULT_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.equals=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation equals to UPDATED_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.equals=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation in DEFAULT_DATE_COTISATION or UPDATED_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.in=" + DEFAULT_DATE_COTISATION + "," + UPDATED_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation equals to UPDATED_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.in=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation is not null
        defaultCotisationTontineShouldBeFound("dateCotisation.specified=true");

        // Get all the cotisationTontineList where dateCotisation is null
        defaultCotisationTontineShouldNotBeFound("dateCotisation.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation is greater than or equal to DEFAULT_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.greaterThanOrEqual=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation is greater than or equal to UPDATED_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.greaterThanOrEqual=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation is less than or equal to DEFAULT_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.lessThanOrEqual=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation is less than or equal to SMALLER_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.lessThanOrEqual=" + SMALLER_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsLessThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation is less than DEFAULT_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.lessThan=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation is less than UPDATED_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.lessThan=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateCotisationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateCotisation is greater than DEFAULT_DATE_COTISATION
        defaultCotisationTontineShouldNotBeFound("dateCotisation.greaterThan=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationTontineList where dateCotisation is greater than SMALLER_DATE_COTISATION
        defaultCotisationTontineShouldBeFound("dateCotisation.greaterThan=" + SMALLER_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation equals to DEFAULT_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.equals=" + DEFAULT_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation equals to UPDATED_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.equals=" + UPDATED_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation in DEFAULT_DATE_VALIDATION or UPDATED_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.in=" + DEFAULT_DATE_VALIDATION + "," + UPDATED_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation equals to UPDATED_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.in=" + UPDATED_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation is not null
        defaultCotisationTontineShouldBeFound("dateValidation.specified=true");

        // Get all the cotisationTontineList where dateValidation is null
        defaultCotisationTontineShouldNotBeFound("dateValidation.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation is greater than or equal to DEFAULT_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.greaterThanOrEqual=" + DEFAULT_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation is greater than or equal to UPDATED_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.greaterThanOrEqual=" + UPDATED_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation is less than or equal to DEFAULT_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.lessThanOrEqual=" + DEFAULT_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation is less than or equal to SMALLER_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.lessThanOrEqual=" + SMALLER_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsLessThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation is less than DEFAULT_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.lessThan=" + DEFAULT_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation is less than UPDATED_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.lessThan=" + UPDATED_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByDateValidationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where dateValidation is greater than DEFAULT_DATE_VALIDATION
        defaultCotisationTontineShouldNotBeFound("dateValidation.greaterThan=" + DEFAULT_DATE_VALIDATION);

        // Get all the cotisationTontineList where dateValidation is greater than SMALLER_DATE_VALIDATION
        defaultCotisationTontineShouldBeFound("dateValidation.greaterThan=" + SMALLER_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultCotisationTontineShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationTontineList where commentaire equals to UPDATED_COMMENTAIRE
        defaultCotisationTontineShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultCotisationTontineShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the cotisationTontineList where commentaire equals to UPDATED_COMMENTAIRE
        defaultCotisationTontineShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where commentaire is not null
        defaultCotisationTontineShouldBeFound("commentaire.specified=true");

        // Get all the cotisationTontineList where commentaire is null
        defaultCotisationTontineShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where commentaire contains DEFAULT_COMMENTAIRE
        defaultCotisationTontineShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationTontineList where commentaire contains UPDATED_COMMENTAIRE
        defaultCotisationTontineShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultCotisationTontineShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationTontineList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultCotisationTontineShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where etat equals to DEFAULT_ETAT
        defaultCotisationTontineShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the cotisationTontineList where etat equals to UPDATED_ETAT
        defaultCotisationTontineShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultCotisationTontineShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the cotisationTontineList where etat equals to UPDATED_ETAT
        defaultCotisationTontineShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        // Get all the cotisationTontineList where etat is not null
        defaultCotisationTontineShouldBeFound("etat.specified=true");

        // Get all the cotisationTontineList where etat is null
        defaultCotisationTontineShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByPaiementTontineIsEqualToSomething() throws Exception {
        PaiementTontine paiementTontine;
        if (TestUtil.findAll(em, PaiementTontine.class).isEmpty()) {
            cotisationTontineRepository.saveAndFlush(cotisationTontine);
            paiementTontine = PaiementTontineResourceIT.createEntity(em);
        } else {
            paiementTontine = TestUtil.findAll(em, PaiementTontine.class).get(0);
        }
        em.persist(paiementTontine);
        em.flush();
        cotisationTontine.addPaiementTontine(paiementTontine);
        cotisationTontineRepository.saveAndFlush(cotisationTontine);
        Long paiementTontineId = paiementTontine.getId();

        // Get all the cotisationTontineList where paiementTontine equals to paiementTontineId
        defaultCotisationTontineShouldBeFound("paiementTontineId.equals=" + paiementTontineId);

        // Get all the cotisationTontineList where paiementTontine equals to (paiementTontineId + 1)
        defaultCotisationTontineShouldNotBeFound("paiementTontineId.equals=" + (paiementTontineId + 1));
    }

    @Test
    @Transactional
    void getAllCotisationTontinesBySessionTontineIsEqualToSomething() throws Exception {
        SessionTontine sessionTontine;
        if (TestUtil.findAll(em, SessionTontine.class).isEmpty()) {
            cotisationTontineRepository.saveAndFlush(cotisationTontine);
            sessionTontine = SessionTontineResourceIT.createEntity(em);
        } else {
            sessionTontine = TestUtil.findAll(em, SessionTontine.class).get(0);
        }
        em.persist(sessionTontine);
        em.flush();
        cotisationTontine.setSessionTontine(sessionTontine);
        cotisationTontineRepository.saveAndFlush(cotisationTontine);
        Long sessionTontineId = sessionTontine.getId();

        // Get all the cotisationTontineList where sessionTontine equals to sessionTontineId
        defaultCotisationTontineShouldBeFound("sessionTontineId.equals=" + sessionTontineId);

        // Get all the cotisationTontineList where sessionTontine equals to (sessionTontineId + 1)
        defaultCotisationTontineShouldNotBeFound("sessionTontineId.equals=" + (sessionTontineId + 1));
    }

    @Test
    @Transactional
    void getAllCotisationTontinesByCompteTontineIsEqualToSomething() throws Exception {
        CompteTontine compteTontine;
        if (TestUtil.findAll(em, CompteTontine.class).isEmpty()) {
            cotisationTontineRepository.saveAndFlush(cotisationTontine);
            compteTontine = CompteTontineResourceIT.createEntity(em);
        } else {
            compteTontine = TestUtil.findAll(em, CompteTontine.class).get(0);
        }
        em.persist(compteTontine);
        em.flush();
        cotisationTontine.setCompteTontine(compteTontine);
        cotisationTontineRepository.saveAndFlush(cotisationTontine);
        Long compteTontineId = compteTontine.getId();

        // Get all the cotisationTontineList where compteTontine equals to compteTontineId
        defaultCotisationTontineShouldBeFound("compteTontineId.equals=" + compteTontineId);

        // Get all the cotisationTontineList where compteTontine equals to (compteTontineId + 1)
        defaultCotisationTontineShouldNotBeFound("compteTontineId.equals=" + (compteTontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCotisationTontineShouldBeFound(String filter) throws Exception {
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cotisationTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].montantCotise").value(hasItem(DEFAULT_MONTANT_COTISE.doubleValue())))
            .andExpect(jsonPath("$.[*].pieceJustifPath").value(hasItem(DEFAULT_PIECE_JUSTIF_PATH)))
            .andExpect(jsonPath("$.[*].dateCotisation").value(hasItem(DEFAULT_DATE_COTISATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())));

        // Check, that the count call also returns 1
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCotisationTontineShouldNotBeFound(String filter) throws Exception {
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCotisationTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCotisationTontine() throws Exception {
        // Get the cotisationTontine
        restCotisationTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCotisationTontine() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();

        // Update the cotisationTontine
        CotisationTontine updatedCotisationTontine = cotisationTontineRepository.findById(cotisationTontine.getId()).get();
        // Disconnect from session so that the updates on updatedCotisationTontine are not directly saved in db
        em.detach(updatedCotisationTontine);
        updatedCotisationTontine
            .montantCotise(UPDATED_MONTANT_COTISE)
            .pieceJustifPath(UPDATED_PIECE_JUSTIF_PATH)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .etat(UPDATED_ETAT);
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(updatedCotisationTontine);

        restCotisationTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cotisationTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
        CotisationTontine testCotisationTontine = cotisationTontineList.get(cotisationTontineList.size() - 1);
        assertThat(testCotisationTontine.getMontantCotise()).isEqualTo(UPDATED_MONTANT_COTISE);
        assertThat(testCotisationTontine.getPieceJustifPath()).isEqualTo(UPDATED_PIECE_JUSTIF_PATH);
        assertThat(testCotisationTontine.getDateCotisation()).isEqualTo(UPDATED_DATE_COTISATION);
        assertThat(testCotisationTontine.getDateValidation()).isEqualTo(UPDATED_DATE_VALIDATION);
        assertThat(testCotisationTontine.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testCotisationTontine.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void putNonExistingCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cotisationTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCotisationTontineWithPatch() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();

        // Update the cotisationTontine using partial update
        CotisationTontine partialUpdatedCotisationTontine = new CotisationTontine();
        partialUpdatedCotisationTontine.setId(cotisationTontine.getId());

        partialUpdatedCotisationTontine
            .montantCotise(UPDATED_MONTANT_COTISE)
            .pieceJustifPath(UPDATED_PIECE_JUSTIF_PATH)
            .commentaire(UPDATED_COMMENTAIRE);

        restCotisationTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisationTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisationTontine))
            )
            .andExpect(status().isOk());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
        CotisationTontine testCotisationTontine = cotisationTontineList.get(cotisationTontineList.size() - 1);
        assertThat(testCotisationTontine.getMontantCotise()).isEqualTo(UPDATED_MONTANT_COTISE);
        assertThat(testCotisationTontine.getPieceJustifPath()).isEqualTo(UPDATED_PIECE_JUSTIF_PATH);
        assertThat(testCotisationTontine.getDateCotisation()).isEqualTo(DEFAULT_DATE_COTISATION);
        assertThat(testCotisationTontine.getDateValidation()).isEqualTo(DEFAULT_DATE_VALIDATION);
        assertThat(testCotisationTontine.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testCotisationTontine.getEtat()).isEqualTo(DEFAULT_ETAT);
    }

    @Test
    @Transactional
    void fullUpdateCotisationTontineWithPatch() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();

        // Update the cotisationTontine using partial update
        CotisationTontine partialUpdatedCotisationTontine = new CotisationTontine();
        partialUpdatedCotisationTontine.setId(cotisationTontine.getId());

        partialUpdatedCotisationTontine
            .montantCotise(UPDATED_MONTANT_COTISE)
            .pieceJustifPath(UPDATED_PIECE_JUSTIF_PATH)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .commentaire(UPDATED_COMMENTAIRE)
            .etat(UPDATED_ETAT);

        restCotisationTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisationTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisationTontine))
            )
            .andExpect(status().isOk());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
        CotisationTontine testCotisationTontine = cotisationTontineList.get(cotisationTontineList.size() - 1);
        assertThat(testCotisationTontine.getMontantCotise()).isEqualTo(UPDATED_MONTANT_COTISE);
        assertThat(testCotisationTontine.getPieceJustifPath()).isEqualTo(UPDATED_PIECE_JUSTIF_PATH);
        assertThat(testCotisationTontine.getDateCotisation()).isEqualTo(UPDATED_DATE_COTISATION);
        assertThat(testCotisationTontine.getDateValidation()).isEqualTo(UPDATED_DATE_VALIDATION);
        assertThat(testCotisationTontine.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
        assertThat(testCotisationTontine.getEtat()).isEqualTo(UPDATED_ETAT);
    }

    @Test
    @Transactional
    void patchNonExistingCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cotisationTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCotisationTontine() throws Exception {
        int databaseSizeBeforeUpdate = cotisationTontineRepository.findAll().size();
        cotisationTontine.setId(count.incrementAndGet());

        // Create the CotisationTontine
        CotisationTontineDTO cotisationTontineDTO = cotisationTontineMapper.toDto(cotisationTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CotisationTontine in the database
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCotisationTontine() throws Exception {
        // Initialize the database
        cotisationTontineRepository.saveAndFlush(cotisationTontine);

        int databaseSizeBeforeDelete = cotisationTontineRepository.findAll().size();

        // Delete the cotisationTontine
        restCotisationTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, cotisationTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CotisationTontine> cotisationTontineList = cotisationTontineRepository.findAll();
        assertThat(cotisationTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
