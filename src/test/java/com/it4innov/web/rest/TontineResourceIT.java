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
import com.it4innov.domain.enumeration.TypePenalite;
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

    private static final Integer DEFAULT_NOMBRE_PERSONNE = 1;
    private static final Integer UPDATED_NOMBRE_PERSONNE = 2;
    private static final Integer SMALLER_NOMBRE_PERSONNE = 1 - 1;

    private static final Double DEFAULT_MARGE_BENEFICIAIRE = 1D;
    private static final Double UPDATED_MARGE_BENEFICIAIRE = 2D;
    private static final Double SMALLER_MARGE_BENEFICIAIRE = 1D - 1D;

    private static final Double DEFAULT_MONTANT_PART = 1D;
    private static final Double UPDATED_MONTANT_PART = 2D;
    private static final Double SMALLER_MONTANT_PART = 1D - 1D;

    private static final Double DEFAULT_MONTANT_CAGNOTE = 1D;
    private static final Double UPDATED_MONTANT_CAGNOTE = 2D;
    private static final Double SMALLER_MONTANT_CAGNOTE = 1D - 1D;

    private static final Double DEFAULT_PENALITE_RETARD_COTISATION = 1D;
    private static final Double UPDATED_PENALITE_RETARD_COTISATION = 2D;
    private static final Double SMALLER_PENALITE_RETARD_COTISATION = 1D - 1D;

    private static final TypePenalite DEFAULT_TYPE_PENALITE = TypePenalite.FORFAIT;
    private static final TypePenalite UPDATED_TYPE_PENALITE = TypePenalite.POURCENTAGE;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_CREATION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_PREMIER_TOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PREMIER_TOUR = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PREMIER_TOUR = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_DERNIER_TOUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DERNIER_TOUR = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DERNIER_TOUR = LocalDate.ofEpochDay(-1L);

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
            .nombrePersonne(DEFAULT_NOMBRE_PERSONNE)
            .margeBeneficiaire(DEFAULT_MARGE_BENEFICIAIRE)
            .montantPart(DEFAULT_MONTANT_PART)
            .montantCagnote(DEFAULT_MONTANT_CAGNOTE)
            .penaliteRetardCotisation(DEFAULT_PENALITE_RETARD_COTISATION)
            .typePenalite(DEFAULT_TYPE_PENALITE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .datePremierTour(DEFAULT_DATE_PREMIER_TOUR)
            .dateDernierTour(DEFAULT_DATE_DERNIER_TOUR)
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
            .nombrePersonne(UPDATED_NOMBRE_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .montantCagnote(UPDATED_MONTANT_CAGNOTE)
            .penaliteRetardCotisation(UPDATED_PENALITE_RETARD_COTISATION)
            .typePenalite(UPDATED_TYPE_PENALITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .datePremierTour(UPDATED_DATE_PREMIER_TOUR)
            .dateDernierTour(UPDATED_DATE_DERNIER_TOUR)
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
        assertThat(testTontine.getNombrePersonne()).isEqualTo(DEFAULT_NOMBRE_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(DEFAULT_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(DEFAULT_MONTANT_PART);
        assertThat(testTontine.getMontantCagnote()).isEqualTo(DEFAULT_MONTANT_CAGNOTE);
        assertThat(testTontine.getPenaliteRetardCotisation()).isEqualTo(DEFAULT_PENALITE_RETARD_COTISATION);
        assertThat(testTontine.getTypePenalite()).isEqualTo(DEFAULT_TYPE_PENALITE);
        assertThat(testTontine.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testTontine.getDatePremierTour()).isEqualTo(DEFAULT_DATE_PREMIER_TOUR);
        assertThat(testTontine.getDateDernierTour()).isEqualTo(DEFAULT_DATE_DERNIER_TOUR);
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
            .andExpect(jsonPath("$.[*].nombrePersonne").value(hasItem(DEFAULT_NOMBRE_PERSONNE)))
            .andExpect(jsonPath("$.[*].margeBeneficiaire").value(hasItem(DEFAULT_MARGE_BENEFICIAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].montantPart").value(hasItem(DEFAULT_MONTANT_PART.doubleValue())))
            .andExpect(jsonPath("$.[*].montantCagnote").value(hasItem(DEFAULT_MONTANT_CAGNOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].penaliteRetardCotisation").value(hasItem(DEFAULT_PENALITE_RETARD_COTISATION.doubleValue())))
            .andExpect(jsonPath("$.[*].typePenalite").value(hasItem(DEFAULT_TYPE_PENALITE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].datePremierTour").value(hasItem(DEFAULT_DATE_PREMIER_TOUR.toString())))
            .andExpect(jsonPath("$.[*].dateDernierTour").value(hasItem(DEFAULT_DATE_DERNIER_TOUR.toString())))
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
            .andExpect(jsonPath("$.nombrePersonne").value(DEFAULT_NOMBRE_PERSONNE))
            .andExpect(jsonPath("$.margeBeneficiaire").value(DEFAULT_MARGE_BENEFICIAIRE.doubleValue()))
            .andExpect(jsonPath("$.montantPart").value(DEFAULT_MONTANT_PART.doubleValue()))
            .andExpect(jsonPath("$.montantCagnote").value(DEFAULT_MONTANT_CAGNOTE.doubleValue()))
            .andExpect(jsonPath("$.penaliteRetardCotisation").value(DEFAULT_PENALITE_RETARD_COTISATION.doubleValue()))
            .andExpect(jsonPath("$.typePenalite").value(DEFAULT_TYPE_PENALITE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.datePremierTour").value(DEFAULT_DATE_PREMIER_TOUR.toString()))
            .andExpect(jsonPath("$.dateDernierTour").value(DEFAULT_DATE_DERNIER_TOUR.toString()))
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
    void getAllTontinesByNombrePersonneIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne equals to DEFAULT_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.equals=" + DEFAULT_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne equals to UPDATED_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.equals=" + UPDATED_NOMBRE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne in DEFAULT_NOMBRE_PERSONNE or UPDATED_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.in=" + DEFAULT_NOMBRE_PERSONNE + "," + UPDATED_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne equals to UPDATED_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.in=" + UPDATED_NOMBRE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne is not null
        defaultTontineShouldBeFound("nombrePersonne.specified=true");

        // Get all the tontineList where nombrePersonne is null
        defaultTontineShouldNotBeFound("nombrePersonne.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne is greater than or equal to DEFAULT_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.greaterThanOrEqual=" + DEFAULT_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne is greater than or equal to UPDATED_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.greaterThanOrEqual=" + UPDATED_NOMBRE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne is less than or equal to DEFAULT_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.lessThanOrEqual=" + DEFAULT_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne is less than or equal to SMALLER_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.lessThanOrEqual=" + SMALLER_NOMBRE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne is less than DEFAULT_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.lessThan=" + DEFAULT_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne is less than UPDATED_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.lessThan=" + UPDATED_NOMBRE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllTontinesByNombrePersonneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where nombrePersonne is greater than DEFAULT_NOMBRE_PERSONNE
        defaultTontineShouldNotBeFound("nombrePersonne.greaterThan=" + DEFAULT_NOMBRE_PERSONNE);

        // Get all the tontineList where nombrePersonne is greater than SMALLER_NOMBRE_PERSONNE
        defaultTontineShouldBeFound("nombrePersonne.greaterThan=" + SMALLER_NOMBRE_PERSONNE);
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
    void getAllTontinesByMontantCagnoteIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote equals to DEFAULT_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.equals=" + DEFAULT_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote equals to UPDATED_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.equals=" + UPDATED_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote in DEFAULT_MONTANT_CAGNOTE or UPDATED_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.in=" + DEFAULT_MONTANT_CAGNOTE + "," + UPDATED_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote equals to UPDATED_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.in=" + UPDATED_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote is not null
        defaultTontineShouldBeFound("montantCagnote.specified=true");

        // Get all the tontineList where montantCagnote is null
        defaultTontineShouldNotBeFound("montantCagnote.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote is greater than or equal to DEFAULT_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.greaterThanOrEqual=" + DEFAULT_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote is greater than or equal to UPDATED_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.greaterThanOrEqual=" + UPDATED_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote is less than or equal to DEFAULT_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.lessThanOrEqual=" + DEFAULT_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote is less than or equal to SMALLER_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.lessThanOrEqual=" + SMALLER_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote is less than DEFAULT_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.lessThan=" + DEFAULT_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote is less than UPDATED_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.lessThan=" + UPDATED_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByMontantCagnoteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where montantCagnote is greater than DEFAULT_MONTANT_CAGNOTE
        defaultTontineShouldNotBeFound("montantCagnote.greaterThan=" + DEFAULT_MONTANT_CAGNOTE);

        // Get all the tontineList where montantCagnote is greater than SMALLER_MONTANT_CAGNOTE
        defaultTontineShouldBeFound("montantCagnote.greaterThan=" + SMALLER_MONTANT_CAGNOTE);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation equals to DEFAULT_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound("penaliteRetardCotisation.equals=" + DEFAULT_PENALITE_RETARD_COTISATION);

        // Get all the tontineList where penaliteRetardCotisation equals to UPDATED_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.equals=" + UPDATED_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation in DEFAULT_PENALITE_RETARD_COTISATION or UPDATED_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound(
            "penaliteRetardCotisation.in=" + DEFAULT_PENALITE_RETARD_COTISATION + "," + UPDATED_PENALITE_RETARD_COTISATION
        );

        // Get all the tontineList where penaliteRetardCotisation equals to UPDATED_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.in=" + UPDATED_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation is not null
        defaultTontineShouldBeFound("penaliteRetardCotisation.specified=true");

        // Get all the tontineList where penaliteRetardCotisation is null
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation is greater than or equal to DEFAULT_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound("penaliteRetardCotisation.greaterThanOrEqual=" + DEFAULT_PENALITE_RETARD_COTISATION);

        // Get all the tontineList where penaliteRetardCotisation is greater than or equal to UPDATED_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.greaterThanOrEqual=" + UPDATED_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation is less than or equal to DEFAULT_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound("penaliteRetardCotisation.lessThanOrEqual=" + DEFAULT_PENALITE_RETARD_COTISATION);

        // Get all the tontineList where penaliteRetardCotisation is less than or equal to SMALLER_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.lessThanOrEqual=" + SMALLER_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation is less than DEFAULT_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.lessThan=" + DEFAULT_PENALITE_RETARD_COTISATION);

        // Get all the tontineList where penaliteRetardCotisation is less than UPDATED_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound("penaliteRetardCotisation.lessThan=" + UPDATED_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByPenaliteRetardCotisationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where penaliteRetardCotisation is greater than DEFAULT_PENALITE_RETARD_COTISATION
        defaultTontineShouldNotBeFound("penaliteRetardCotisation.greaterThan=" + DEFAULT_PENALITE_RETARD_COTISATION);

        // Get all the tontineList where penaliteRetardCotisation is greater than SMALLER_PENALITE_RETARD_COTISATION
        defaultTontineShouldBeFound("penaliteRetardCotisation.greaterThan=" + SMALLER_PENALITE_RETARD_COTISATION);
    }

    @Test
    @Transactional
    void getAllTontinesByTypePenaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where typePenalite equals to DEFAULT_TYPE_PENALITE
        defaultTontineShouldBeFound("typePenalite.equals=" + DEFAULT_TYPE_PENALITE);

        // Get all the tontineList where typePenalite equals to UPDATED_TYPE_PENALITE
        defaultTontineShouldNotBeFound("typePenalite.equals=" + UPDATED_TYPE_PENALITE);
    }

    @Test
    @Transactional
    void getAllTontinesByTypePenaliteIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where typePenalite in DEFAULT_TYPE_PENALITE or UPDATED_TYPE_PENALITE
        defaultTontineShouldBeFound("typePenalite.in=" + DEFAULT_TYPE_PENALITE + "," + UPDATED_TYPE_PENALITE);

        // Get all the tontineList where typePenalite equals to UPDATED_TYPE_PENALITE
        defaultTontineShouldNotBeFound("typePenalite.in=" + UPDATED_TYPE_PENALITE);
    }

    @Test
    @Transactional
    void getAllTontinesByTypePenaliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where typePenalite is not null
        defaultTontineShouldBeFound("typePenalite.specified=true");

        // Get all the tontineList where typePenalite is null
        defaultTontineShouldNotBeFound("typePenalite.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation equals to DEFAULT_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.equals=" + DEFAULT_DATE_CREATION);

        // Get all the tontineList where dateCreation equals to UPDATED_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation in DEFAULT_DATE_CREATION or UPDATED_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION);

        // Get all the tontineList where dateCreation equals to UPDATED_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.in=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation is not null
        defaultTontineShouldBeFound("dateCreation.specified=true");

        // Get all the tontineList where dateCreation is null
        defaultTontineShouldNotBeFound("dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation is greater than or equal to DEFAULT_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.greaterThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the tontineList where dateCreation is greater than or equal to UPDATED_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.greaterThanOrEqual=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation is less than or equal to DEFAULT_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.lessThanOrEqual=" + DEFAULT_DATE_CREATION);

        // Get all the tontineList where dateCreation is less than or equal to SMALLER_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.lessThanOrEqual=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation is less than DEFAULT_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.lessThan=" + DEFAULT_DATE_CREATION);

        // Get all the tontineList where dateCreation is less than UPDATED_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.lessThan=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDateCreationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateCreation is greater than DEFAULT_DATE_CREATION
        defaultTontineShouldNotBeFound("dateCreation.greaterThan=" + DEFAULT_DATE_CREATION);

        // Get all the tontineList where dateCreation is greater than SMALLER_DATE_CREATION
        defaultTontineShouldBeFound("dateCreation.greaterThan=" + SMALLER_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour equals to DEFAULT_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.equals=" + DEFAULT_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour equals to UPDATED_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.equals=" + UPDATED_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour in DEFAULT_DATE_PREMIER_TOUR or UPDATED_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.in=" + DEFAULT_DATE_PREMIER_TOUR + "," + UPDATED_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour equals to UPDATED_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.in=" + UPDATED_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour is not null
        defaultTontineShouldBeFound("datePremierTour.specified=true");

        // Get all the tontineList where datePremierTour is null
        defaultTontineShouldNotBeFound("datePremierTour.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour is greater than or equal to DEFAULT_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.greaterThanOrEqual=" + DEFAULT_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour is greater than or equal to UPDATED_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.greaterThanOrEqual=" + UPDATED_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour is less than or equal to DEFAULT_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.lessThanOrEqual=" + DEFAULT_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour is less than or equal to SMALLER_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.lessThanOrEqual=" + SMALLER_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour is less than DEFAULT_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.lessThan=" + DEFAULT_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour is less than UPDATED_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.lessThan=" + UPDATED_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDatePremierTourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where datePremierTour is greater than DEFAULT_DATE_PREMIER_TOUR
        defaultTontineShouldNotBeFound("datePremierTour.greaterThan=" + DEFAULT_DATE_PREMIER_TOUR);

        // Get all the tontineList where datePremierTour is greater than SMALLER_DATE_PREMIER_TOUR
        defaultTontineShouldBeFound("datePremierTour.greaterThan=" + SMALLER_DATE_PREMIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour equals to DEFAULT_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.equals=" + DEFAULT_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour equals to UPDATED_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.equals=" + UPDATED_DATE_DERNIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsInShouldWork() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour in DEFAULT_DATE_DERNIER_TOUR or UPDATED_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.in=" + DEFAULT_DATE_DERNIER_TOUR + "," + UPDATED_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour equals to UPDATED_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.in=" + UPDATED_DATE_DERNIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsNullOrNotNull() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour is not null
        defaultTontineShouldBeFound("dateDernierTour.specified=true");

        // Get all the tontineList where dateDernierTour is null
        defaultTontineShouldNotBeFound("dateDernierTour.specified=false");
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour is greater than or equal to DEFAULT_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.greaterThanOrEqual=" + DEFAULT_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour is greater than or equal to UPDATED_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.greaterThanOrEqual=" + UPDATED_DATE_DERNIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour is less than or equal to DEFAULT_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.lessThanOrEqual=" + DEFAULT_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour is less than or equal to SMALLER_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.lessThanOrEqual=" + SMALLER_DATE_DERNIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsLessThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour is less than DEFAULT_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.lessThan=" + DEFAULT_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour is less than UPDATED_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.lessThan=" + UPDATED_DATE_DERNIER_TOUR);
    }

    @Test
    @Transactional
    void getAllTontinesByDateDernierTourIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tontineRepository.saveAndFlush(tontine);

        // Get all the tontineList where dateDernierTour is greater than DEFAULT_DATE_DERNIER_TOUR
        defaultTontineShouldNotBeFound("dateDernierTour.greaterThan=" + DEFAULT_DATE_DERNIER_TOUR);

        // Get all the tontineList where dateDernierTour is greater than SMALLER_DATE_DERNIER_TOUR
        defaultTontineShouldBeFound("dateDernierTour.greaterThan=" + SMALLER_DATE_DERNIER_TOUR);
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
            .andExpect(jsonPath("$.[*].nombrePersonne").value(hasItem(DEFAULT_NOMBRE_PERSONNE)))
            .andExpect(jsonPath("$.[*].margeBeneficiaire").value(hasItem(DEFAULT_MARGE_BENEFICIAIRE.doubleValue())))
            .andExpect(jsonPath("$.[*].montantPart").value(hasItem(DEFAULT_MONTANT_PART.doubleValue())))
            .andExpect(jsonPath("$.[*].montantCagnote").value(hasItem(DEFAULT_MONTANT_CAGNOTE.doubleValue())))
            .andExpect(jsonPath("$.[*].penaliteRetardCotisation").value(hasItem(DEFAULT_PENALITE_RETARD_COTISATION.doubleValue())))
            .andExpect(jsonPath("$.[*].typePenalite").value(hasItem(DEFAULT_TYPE_PENALITE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].datePremierTour").value(hasItem(DEFAULT_DATE_PREMIER_TOUR.toString())))
            .andExpect(jsonPath("$.[*].dateDernierTour").value(hasItem(DEFAULT_DATE_DERNIER_TOUR.toString())))
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
            .nombrePersonne(UPDATED_NOMBRE_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .montantCagnote(UPDATED_MONTANT_CAGNOTE)
            .penaliteRetardCotisation(UPDATED_PENALITE_RETARD_COTISATION)
            .typePenalite(UPDATED_TYPE_PENALITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .datePremierTour(UPDATED_DATE_PREMIER_TOUR)
            .dateDernierTour(UPDATED_DATE_DERNIER_TOUR)
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
        assertThat(testTontine.getNombrePersonne()).isEqualTo(UPDATED_NOMBRE_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(UPDATED_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(UPDATED_MONTANT_PART);
        assertThat(testTontine.getMontantCagnote()).isEqualTo(UPDATED_MONTANT_CAGNOTE);
        assertThat(testTontine.getPenaliteRetardCotisation()).isEqualTo(UPDATED_PENALITE_RETARD_COTISATION);
        assertThat(testTontine.getTypePenalite()).isEqualTo(UPDATED_TYPE_PENALITE);
        assertThat(testTontine.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testTontine.getDatePremierTour()).isEqualTo(UPDATED_DATE_PREMIER_TOUR);
        assertThat(testTontine.getDateDernierTour()).isEqualTo(UPDATED_DATE_DERNIER_TOUR);
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
            .montantCagnote(UPDATED_MONTANT_CAGNOTE)
            .typePenalite(UPDATED_TYPE_PENALITE)
            .dateDernierTour(UPDATED_DATE_DERNIER_TOUR);

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
        assertThat(testTontine.getNombrePersonne()).isEqualTo(DEFAULT_NOMBRE_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(DEFAULT_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(DEFAULT_MONTANT_PART);
        assertThat(testTontine.getMontantCagnote()).isEqualTo(UPDATED_MONTANT_CAGNOTE);
        assertThat(testTontine.getPenaliteRetardCotisation()).isEqualTo(DEFAULT_PENALITE_RETARD_COTISATION);
        assertThat(testTontine.getTypePenalite()).isEqualTo(UPDATED_TYPE_PENALITE);
        assertThat(testTontine.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testTontine.getDatePremierTour()).isEqualTo(DEFAULT_DATE_PREMIER_TOUR);
        assertThat(testTontine.getDateDernierTour()).isEqualTo(UPDATED_DATE_DERNIER_TOUR);
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
            .nombrePersonne(UPDATED_NOMBRE_PERSONNE)
            .margeBeneficiaire(UPDATED_MARGE_BENEFICIAIRE)
            .montantPart(UPDATED_MONTANT_PART)
            .montantCagnote(UPDATED_MONTANT_CAGNOTE)
            .penaliteRetardCotisation(UPDATED_PENALITE_RETARD_COTISATION)
            .typePenalite(UPDATED_TYPE_PENALITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .datePremierTour(UPDATED_DATE_PREMIER_TOUR)
            .dateDernierTour(UPDATED_DATE_DERNIER_TOUR)
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
        assertThat(testTontine.getNombrePersonne()).isEqualTo(UPDATED_NOMBRE_PERSONNE);
        assertThat(testTontine.getMargeBeneficiaire()).isEqualTo(UPDATED_MARGE_BENEFICIAIRE);
        assertThat(testTontine.getMontantPart()).isEqualTo(UPDATED_MONTANT_PART);
        assertThat(testTontine.getMontantCagnote()).isEqualTo(UPDATED_MONTANT_CAGNOTE);
        assertThat(testTontine.getPenaliteRetardCotisation()).isEqualTo(UPDATED_PENALITE_RETARD_COTISATION);
        assertThat(testTontine.getTypePenalite()).isEqualTo(UPDATED_TYPE_PENALITE);
        assertThat(testTontine.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testTontine.getDatePremierTour()).isEqualTo(UPDATED_DATE_PREMIER_TOUR);
        assertThat(testTontine.getDateDernierTour()).isEqualTo(UPDATED_DATE_DERNIER_TOUR);
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
