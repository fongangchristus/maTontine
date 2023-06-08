package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.GestionnaireTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.domain.enumeration.StatutTontine;
import com.it4innov.repository.TontineRepository;
import com.it4innov.service.criteria.TontineCriteria;
import com.it4innov.service.dto.TontineDTO;
import com.it4innov.service.mapper.TontineMapper;
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
 * Integration tests for the {@link TontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TontineResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NOMBRE_TOUR = 1;
    private static final Integer UPDATED_NOMBRE_TOUR = 2;
    private static final Integer SMALLER_NOMBRE_TOUR = 1 - 1;

    private static final Integer DEFAULT_NOMBRE_MAX_PERSONNE = 1;
    private static final Integer UPDATED_NOMBRE_MAX_PERSONNE = 2;
    private static final Integer SMALLER_NOMBRE_MAX_PERSONNE = 1 - 1;

    private static final Double DEFAULT_MARGE_BENEFICIAIRE = 1D;
    private static final Double UPDATED_MARGE_BENEFICIAIRE = 2D;
    private static final Double SMALLER_MARGE_BENEFICIAIRE = 1D - 1D;

    private static final Double DEFAULT_MONTANT_PART = 1D;
    private static final Double UPDATED_MONTANT_PART = 2D;
    private static final Double SMALLER_MONTANT_PART = 1D - 1D;

    private static final Double DEFAULT_AMANDE_ECHEC = 1D;
    private static final Double UPDATED_AMANDE_ECHEC = 2D;
    private static final Double SMALLER_AMANDE_ECHEC = 1D - 1D;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final StatutTontine DEFAULT_STATUT_TONTINE = StatutTontine.CREE;
    private static final StatutTontine UPDATED_STATUT_TONTINE = StatutTontine.OUVERTE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TontineRepository tontineRepository;

    @Autowired
    private TontineMapper tontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTontineMockMvc;

    private Tontine tontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tontine createEntity(EntityManager em) {
        Tontine tontine = new Tontine()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .libele(DEFAULT_LIBELE)
            .nombreTour(DEFAULT_NOMBRE_TOUR)
            .nombreMaxPersonne(DEFAULT_NOMBRE_MAX_PERSONNE)
            .margeBeneficiaire(DEFAULT_MARGE_BENEFICIAIRE)
            .montantPart(DEFAULT_MONTANT_PART)
            .amandeEchec(DEFAULT_AMANDE_ECHEC)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .statutTontine(DEFAULT_STATUT_TONTINE)
            .description(DEFAULT_DESCRIPTION);
        return tontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tontine createUpdatedEntity(EntityManager em) {
        Tontine tontine = new Tontine()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .nombreTour(UPDATED_NOMBRE_TOUR)
            .nombreMaxPersonne(UPDATED_NOMBRE_MAX_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .amandeEchec(UPDATED_AMANDE_ECHEC)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statutTontine(UPDATED_STATUT_TONTINE)
            .description(UPDATED_DESCRIPTION);
        return tontine;
    }

    @BeforeEach
    public void initTest() {
        tontine = createEntity(em);
    }

    @Test
    @Transactional
    void createTontine() throws Exception {
        int databaseSizeBeforeCreate = tontineRepository.findAll().size();
        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);
        restTontineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tontineDTO)))
            .andExpect(status().isCreated());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeCreate + 1);
        Tontine testTontine = tontineList.get(tontineList.size() - 1);
        assertThat(testTontine.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testTontine.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testTontine.getNombreTour()).isEqualTo(DEFAULT_NOMBRE_TOUR);
        assertThat(testTontine.getNombreMaxPersonne()).isEqualTo(DEFAULT_NOMBRE_MAX_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(DEFAULT_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(DEFAULT_MONTANT_PART);
        assertThat(testTontine.getAmandeEchec()).isEqualTo(DEFAULT_AMANDE_ECHEC);
        assertThat(testTontine.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testTontine.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testTontine.getStatutTontine()).isEqualTo(DEFAULT_STATUT_TONTINE);
        assertThat(testTontine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createTontineWithExistingId() throws Exception {
        // Create the Tontine with an existing ID
        tontine.setId(1L);
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        int databaseSizeBeforeCreate = tontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTontineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tontineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = tontineRepository.findAll().size();
        // set the field null
        tontine.setCodeAssociation(null);

        // Create the Tontine, which fails.
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        restTontineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tontineDTO)))
            .andExpect(status().isBadRequest());

        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTontines() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList
        restTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].nombreTour").value(hasItem(DEFAULT_NOMBRE_TOUR)))
            .andExpect(jsonPath("$.[*].nombreMaxPersonne").value(hasItem(DEFAULT_NOMBRE_MAX_PERSONNE)))
            .andExpect(jsonPath("$.[*].margeBeneficiaire").value(hasItem(DEFAULT_MARGE_BENEFICIAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].montantPart").value(hasItem(DEFAULT_MONTANT_PART.doubleValue())))
            .andExpect(jsonPath("$.[*].amandeEchec").value(hasItem(DEFAULT_AMANDE_ECHEC.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statutTontine").value(hasItem(DEFAULT_STATUT_TONTINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getTontine() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get the tontine
        restTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, tontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tontine.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.nombreTour").value(DEFAULT_NOMBRE_TOUR))
            .andExpect(jsonPath("$.nombreMaxPersonne").value(DEFAULT_NOMBRE_MAX_PERSONNE))
            .andExpect(jsonPath("$.margeBeneficiaire").value(DEFAULT_MARGE_BENEFICIAIRE.doubleValue()))
            .andExpect(jsonPath("$.montantPart").value(DEFAULT_MONTANT_PART.doubleValue()))
            .andExpect(jsonPath("$.amandeEchec").value(DEFAULT_AMANDE_ECHEC.doubleValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.statutTontine").value(DEFAULT_STATUT_TONTINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getTontinesByIdFiltering() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        Long id = tontine.getId();

        defaultTontineShouldBeFound("id.equals=" + id);
        defaultTontineShouldNotBeFound("id.notEquals=" + id);

        defaultTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTontinesByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultTontineShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the tontineList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultTontineShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllTontinesByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultTontineShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the tontineList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultTontineShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllTontinesByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where codeAssociation is not null
        defaultTontineShouldBeFound("codeAssociation.specified=true");

        // Get all the tontineList where codeAssociation is null
        defaultTontineShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultTontineShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the tontineList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultTontineShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllTontinesByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultTontineShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the tontineList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultTontineShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllTontinesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where libele equals to DEFAULT_LIBELE
        defaultTontineShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the tontineList where libele equals to UPDATED_LIBELE
        defaultTontineShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTontinesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultTontineShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the tontineList where libele equals to UPDATED_LIBELE
        defaultTontineShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTontinesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where libele is not null
        defaultTontineShouldBeFound("libele.specified=true");

        // Get all the tontineList where libele is null
        defaultTontineShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where libele contains DEFAULT_LIBELE
        defaultTontineShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the tontineList where libele contains UPDATED_LIBELE
        defaultTontineShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTontinesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where libele does not contain DEFAULT_LIBELE
        defaultTontineShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the tontineList where libele does not contain UPDATED_LIBELE
        defaultTontineShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour equals to DEFAULT_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.equals=" + DEFAULT_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour equals to UPDATED_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.equals=" + UPDATED_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour in DEFAULT_NOMBRE_TOUR or UPDATED_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.in=" + DEFAULT_NOMBRE_TOUR + "," + UPDATED_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour equals to UPDATED_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.in=" + UPDATED_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour is not null
        defaultTontineShouldBeFound("nombreTour.specified=true");

        // Get all the tontineList where nombreTour is null
        defaultTontineShouldNotBeFound("nombreTour.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour is greater than or equal to DEFAULT_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.greaterThanOrEqual=" + DEFAULT_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour is greater than or equal to UPDATED_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.greaterThanOrEqual=" + UPDATED_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour is less than or equal to DEFAULT_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.lessThanOrEqual=" + DEFAULT_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour is less than or equal to SMALLER_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.lessThanOrEqual=" + SMALLER_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour is less than DEFAULT_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.lessThan=" + DEFAULT_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour is less than UPDATED_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.lessThan=" + UPDATED_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreTourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreTour is greater than DEFAULT_NOMBRE_TOUR
        defaultTontineShouldNotBeFound("nombreTour.greaterThan=" + DEFAULT_NOMBRE_TOUR);

        // Get all the tontineList where nombreTour is greater than SMALLER_NOMBRE_TOUR
        defaultTontineShouldBeFound("nombreTour.greaterThan=" + SMALLER_NOMBRE_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne equals to DEFAULT_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.equals=" + DEFAULT_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne equals to UPDATED_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.equals=" + UPDATED_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne in DEFAULT_NOMBRE_MAX_PERSONNE or UPDATED_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.in=" + DEFAULT_NOMBRE_MAX_PERSONNE + "," + UPDATED_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne equals to UPDATED_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.in=" + UPDATED_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne is not null
        defaultTontineShouldBeFound("nombreMaxPersonne.specified=true");

        // Get all the tontineList where nombreMaxPersonne is null
        defaultTontineShouldNotBeFound("nombreMaxPersonne.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne is greater than or equal to DEFAULT_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.greaterThanOrEqual=" + DEFAULT_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne is greater than or equal to UPDATED_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.greaterThanOrEqual=" + UPDATED_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne is less than or equal to DEFAULT_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.lessThanOrEqual=" + DEFAULT_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne is less than or equal to SMALLER_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.lessThanOrEqual=" + SMALLER_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne is less than DEFAULT_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.lessThan=" + DEFAULT_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne is less than UPDATED_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.lessThan=" + UPDATED_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombreMaxPersonneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombreMaxPersonne is greater than DEFAULT_NOMBRE_MAX_PERSONNE
        defaultTontineShouldNotBeFound("nombreMaxPersonne.greaterThan=" + DEFAULT_NOMBRE_MAX_PERSONNE);

        // Get all the tontineList where nombreMaxPersonne is greater than SMALLER_NOMBRE_MAX_PERSONNE
        defaultTontineShouldBeFound("nombreMaxPersonne.greaterThan=" + SMALLER_NOMBRE_MAX_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire equals to DEFAULT_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.equals=" + DEFAULT_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire equals to UPDATED_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.equals=" + UPDATED_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire in DEFAULT_MARGE_BENEFICIAIRE or UPDATED_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.in=" + DEFAULT_MARGE_BENEFICIAIRE + "," + UPDATED_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire equals to UPDATED_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.in=" + UPDATED_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire is not null
        defaultTontineShouldBeFound("margeBeneficiaire.specified=true");

        // Get all the tontineList where margeBeneficiaire is null
        defaultTontineShouldNotBeFound("margeBeneficiaire.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire is greater than or equal to DEFAULT_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.greaterThanOrEqual=" + DEFAULT_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire is greater than or equal to UPDATED_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.greaterThanOrEqual=" + UPDATED_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire is less than or equal to DEFAULT_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.lessThanOrEqual=" + DEFAULT_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire is less than or equal to SMALLER_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.lessThanOrEqual=" + SMALLER_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire is less than DEFAULT_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.lessThan=" + DEFAULT_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire is less than UPDATED_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.lessThan=" + UPDATED_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMargeBeneficiaireIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where margeBeneficiaire is greater than DEFAULT_MARGE_BENEFICIAIRE
        defaultTontineShouldNotBeFound("margeBeneficiaire.greaterThan=" + DEFAULT_MARGE_BENEFICIAIRE);

        // Get all the tontineList where margeBeneficiaire is greater than SMALLER_MARGE_BENEFICIAIRE
        defaultTontineShouldBeFound("margeBeneficiaire.greaterThan=" + SMALLER_MARGE_BENEFICIAIRE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart equals to DEFAULT_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.equals=" + DEFAULT_MONTANT_PART);

        // Get all the tontineList where montantPart equals to UPDATED_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.equals=" + UPDATED_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart in DEFAULT_MONTANT_PART or UPDATED_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.in=" + DEFAULT_MONTANT_PART + "," + UPDATED_MONTANT_PART);

        // Get all the tontineList where montantPart equals to UPDATED_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.in=" + UPDATED_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart is not null
        defaultTontineShouldBeFound("montantPart.specified=true");

        // Get all the tontineList where montantPart is null
        defaultTontineShouldNotBeFound("montantPart.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart is greater than or equal to DEFAULT_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.greaterThanOrEqual=" + DEFAULT_MONTANT_PART);

        // Get all the tontineList where montantPart is greater than or equal to UPDATED_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.greaterThanOrEqual=" + UPDATED_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart is less than or equal to DEFAULT_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.lessThanOrEqual=" + DEFAULT_MONTANT_PART);

        // Get all the tontineList where montantPart is less than or equal to SMALLER_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.lessThanOrEqual=" + SMALLER_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart is less than DEFAULT_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.lessThan=" + DEFAULT_MONTANT_PART);

        // Get all the tontineList where montantPart is less than UPDATED_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.lessThan=" + UPDATED_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantPartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantPart is greater than DEFAULT_MONTANT_PART
        defaultTontineShouldNotBeFound("montantPart.greaterThan=" + DEFAULT_MONTANT_PART);

        // Get all the tontineList where montantPart is greater than SMALLER_MONTANT_PART
        defaultTontineShouldBeFound("montantPart.greaterThan=" + SMALLER_MONTANT_PART);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec equals to DEFAULT_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.equals=" + DEFAULT_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec equals to UPDATED_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.equals=" + UPDATED_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec in DEFAULT_AMANDE_ECHEC or UPDATED_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.in=" + DEFAULT_AMANDE_ECHEC + "," + UPDATED_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec equals to UPDATED_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.in=" + UPDATED_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec is not null
        defaultTontineShouldBeFound("amandeEchec.specified=true");

        // Get all the tontineList where amandeEchec is null
        defaultTontineShouldNotBeFound("amandeEchec.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec is greater than or equal to DEFAULT_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.greaterThanOrEqual=" + DEFAULT_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec is greater than or equal to UPDATED_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.greaterThanOrEqual=" + UPDATED_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec is less than or equal to DEFAULT_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.lessThanOrEqual=" + DEFAULT_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec is less than or equal to SMALLER_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.lessThanOrEqual=" + SMALLER_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec is less than DEFAULT_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.lessThan=" + DEFAULT_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec is less than UPDATED_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.lessThan=" + UPDATED_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByAmandeEchecIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where amandeEchec is greater than DEFAULT_AMANDE_ECHEC
        defaultTontineShouldNotBeFound("amandeEchec.greaterThan=" + DEFAULT_AMANDE_ECHEC);

        // Get all the tontineList where amandeEchec is greater than SMALLER_AMANDE_ECHEC
        defaultTontineShouldBeFound("amandeEchec.greaterThan=" + SMALLER_AMANDE_ECHEC);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the tontineList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the tontineList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut is not null
        defaultTontineShouldBeFound("dateDebut.specified=true");

        // Get all the tontineList where dateDebut is null
        defaultTontineShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the tontineList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the tontineList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the tontineList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultTontineShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the tontineList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultTontineShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin equals to DEFAULT_DATE_FIN
        defaultTontineShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the tontineList where dateFin equals to UPDATED_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultTontineShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the tontineList where dateFin equals to UPDATED_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin is not null
        defaultTontineShouldBeFound("dateFin.specified=true");

        // Get all the tontineList where dateFin is null
        defaultTontineShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultTontineShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the tontineList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultTontineShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the tontineList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin is less than DEFAULT_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the tontineList where dateFin is less than UPDATED_DATE_FIN
        defaultTontineShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateFin is greater than DEFAULT_DATE_FIN
        defaultTontineShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the tontineList where dateFin is greater than SMALLER_DATE_FIN
        defaultTontineShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTontinesByStatutTontineIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where statutTontine equals to DEFAULT_STATUT_TONTINE
        defaultTontineShouldBeFound("statutTontine.equals=" + DEFAULT_STATUT_TONTINE);

        // Get all the tontineList where statutTontine equals to UPDATED_STATUT_TONTINE
        defaultTontineShouldNotBeFound("statutTontine.equals=" + UPDATED_STATUT_TONTINE);
    }

    @Test
    @Transactional
    void getAllTontinesByStatutTontineIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where statutTontine in DEFAULT_STATUT_TONTINE or UPDATED_STATUT_TONTINE
        defaultTontineShouldBeFound("statutTontine.in=" + DEFAULT_STATUT_TONTINE + "," + UPDATED_STATUT_TONTINE);

        // Get all the tontineList where statutTontine equals to UPDATED_STATUT_TONTINE
        defaultTontineShouldNotBeFound("statutTontine.in=" + UPDATED_STATUT_TONTINE);
    }

    @Test
    @Transactional
    void getAllTontinesByStatutTontineIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where statutTontine is not null
        defaultTontineShouldBeFound("statutTontine.specified=true");

        // Get all the tontineList where statutTontine is null
        defaultTontineShouldNotBeFound("statutTontine.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where description equals to DEFAULT_DESCRIPTION
        defaultTontineShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the tontineList where description equals to UPDATED_DESCRIPTION
        defaultTontineShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTontinesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTontineShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the tontineList where description equals to UPDATED_DESCRIPTION
        defaultTontineShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTontinesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where description is not null
        defaultTontineShouldBeFound("description.specified=true");

        // Get all the tontineList where description is null
        defaultTontineShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where description contains DEFAULT_DESCRIPTION
        defaultTontineShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the tontineList where description contains UPDATED_DESCRIPTION
        defaultTontineShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTontinesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where description does not contain DEFAULT_DESCRIPTION
        defaultTontineShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the tontineList where description does not contain UPDATED_DESCRIPTION
        defaultTontineShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTontinesBySessionTontineIsEqualToSomething() throws Exception {
        SessionTontine sessionTontine;
        if (TestUtil.findAll(em, SessionTontine.class).isEmpty()) {
            tontineRepository.saveAndFlush(tontine);
            sessionTontine = SessionTontineResourceIT.createEntity(em);
        } else {
            sessionTontine = TestUtil.findAll(em, SessionTontine.class).get(0);
        }
        em.persist(sessionTontine);
        em.flush();
        tontine.addSessionTontine(sessionTontine);
        tontineRepository.saveAndFlush(tontine);
        Long sessionTontineId = sessionTontine.getId();

        // Get all the tontineList where sessionTontine equals to sessionTontineId
        defaultTontineShouldBeFound("sessionTontineId.equals=" + sessionTontineId);

        // Get all the tontineList where sessionTontine equals to (sessionTontineId + 1)
        defaultTontineShouldNotBeFound("sessionTontineId.equals=" + (sessionTontineId + 1));
    }

    @Test
    @Transactional
    void getAllTontinesByGestionnaireTontineIsEqualToSomething() throws Exception {
        GestionnaireTontine gestionnaireTontine;
        if (TestUtil.findAll(em, GestionnaireTontine.class).isEmpty()) {
            tontineRepository.saveAndFlush(tontine);
            gestionnaireTontine = GestionnaireTontineResourceIT.createEntity(em);
        } else {
            gestionnaireTontine = TestUtil.findAll(em, GestionnaireTontine.class).get(0);
        }
        em.persist(gestionnaireTontine);
        em.flush();
        tontine.addGestionnaireTontine(gestionnaireTontine);
        tontineRepository.saveAndFlush(tontine);
        Long gestionnaireTontineId = gestionnaireTontine.getId();

        // Get all the tontineList where gestionnaireTontine equals to gestionnaireTontineId
        defaultTontineShouldBeFound("gestionnaireTontineId.equals=" + gestionnaireTontineId);

        // Get all the tontineList where gestionnaireTontine equals to (gestionnaireTontineId + 1)
        defaultTontineShouldNotBeFound("gestionnaireTontineId.equals=" + (gestionnaireTontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTontineShouldBeFound(String filter) throws Exception {
        restTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].nombreTour").value(hasItem(DEFAULT_NOMBRE_TOUR)))
            .andExpect(jsonPath("$.[*].nombreMaxPersonne").value(hasItem(DEFAULT_NOMBRE_MAX_PERSONNE)))
            .andExpect(jsonPath("$.[*].margeBeneficiaire").value(hasItem(DEFAULT_MARGE_BENEFICIAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].montantPart").value(hasItem(DEFAULT_MONTANT_PART.doubleValue())))
            .andExpect(jsonPath("$.[*].amandeEchec").value(hasItem(DEFAULT_AMANDE_ECHEC.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statutTontine").value(hasItem(DEFAULT_STATUT_TONTINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTontineShouldNotBeFound(String filter) throws Exception {
        restTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTontine() throws Exception {
        // Get the tontine
        restTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTontine() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();

        // Update the tontine
        Tontine updatedTontine = tontineRepository.findById(tontine.getId()).get();
        // Disconnect from session so that the updates on updatedTontine are not directly saved in db
        em.detach(updatedTontine);
        updatedTontine
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .nombreTour(UPDATED_NOMBRE_TOUR)
            .nombreMaxPersonne(UPDATED_NOMBRE_MAX_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .amandeEchec(UPDATED_AMANDE_ECHEC)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statutTontine(UPDATED_STATUT_TONTINE)
            .description(UPDATED_DESCRIPTION);
        TontineDTO tontineDTO = tontineMapper.toDto(updatedTontine);

        restTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
        Tontine testTontine = tontineList.get(tontineList.size() - 1);
        assertThat(testTontine.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTontine.getNombreTour()).isEqualTo(UPDATED_NOMBRE_TOUR);
        assertThat(testTontine.getNombreMaxPersonne()).isEqualTo(UPDATED_NOMBRE_MAX_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(UPDATED_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(UPDATED_MONTANT_PART);
        assertThat(testTontine.getAmandeEchec()).isEqualTo(UPDATED_AMANDE_ECHEC);
        assertThat(testTontine.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testTontine.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTontine.getStatutTontine()).isEqualTo(UPDATED_STATUT_TONTINE);
        assertThat(testTontine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tontineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTontineWithPatch() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();

        // Update the tontine using partial update
        Tontine partialUpdatedTontine = new Tontine();
        partialUpdatedTontine.setId(tontine.getId());

        partialUpdatedTontine
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .amandeEchec(UPDATED_AMANDE_ECHEC)
            .dateFin(UPDATED_DATE_FIN);

        restTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTontine))
            )
            .andExpect(status().isOk());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
        Tontine testTontine = tontineList.get(tontineList.size() - 1);
        assertThat(testTontine.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTontine.getNombreTour()).isEqualTo(DEFAULT_NOMBRE_TOUR);
        assertThat(testTontine.getNombreMaxPersonne()).isEqualTo(DEFAULT_NOMBRE_MAX_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(DEFAULT_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(DEFAULT_MONTANT_PART);
        assertThat(testTontine.getAmandeEchec()).isEqualTo(UPDATED_AMANDE_ECHEC);
        assertThat(testTontine.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testTontine.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTontine.getStatutTontine()).isEqualTo(DEFAULT_STATUT_TONTINE);
        assertThat(testTontine.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateTontineWithPatch() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();

        // Update the tontine using partial update
        Tontine partialUpdatedTontine = new Tontine();
        partialUpdatedTontine.setId(tontine.getId());

        partialUpdatedTontine
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .nombreTour(UPDATED_NOMBRE_TOUR)
            .nombreMaxPersonne(UPDATED_NOMBRE_MAX_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .amandeEchec(UPDATED_AMANDE_ECHEC)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statutTontine(UPDATED_STATUT_TONTINE)
            .description(UPDATED_DESCRIPTION);

        restTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTontine))
            )
            .andExpect(status().isOk());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
        Tontine testTontine = tontineList.get(tontineList.size() - 1);
        assertThat(testTontine.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTontine.getNombreTour()).isEqualTo(UPDATED_NOMBRE_TOUR);
        assertThat(testTontine.getNombreMaxPersonne()).isEqualTo(UPDATED_NOMBRE_MAX_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(UPDATED_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(UPDATED_MONTANT_PART);
        assertThat(testTontine.getAmandeEchec()).isEqualTo(UPDATED_AMANDE_ECHEC);
        assertThat(testTontine.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testTontine.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTontine.getStatutTontine()).isEqualTo(UPDATED_STATUT_TONTINE);
        assertThat(testTontine.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTontine() throws Exception {
        int databaseSizeBeforeUpdate = tontineRepository.findAll().size();
        tontine.setId(count.incrementAndGet());

        // Create the Tontine
        TontineDTO tontineDTO = tontineMapper.toDto(tontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTontineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tontine in the database
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTontine() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        int databaseSizeBeforeDelete = tontineRepository.findAll().size();

        // Delete the tontine
        restTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, tontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tontine> tontineList = tontineRepository.findAll();
        assertThat(tontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
