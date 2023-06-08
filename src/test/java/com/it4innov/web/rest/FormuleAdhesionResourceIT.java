package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Adhesion;
import com.it4innov.domain.FormuleAdhesion;
import com.it4innov.repository.FormuleAdhesionRepository;
import com.it4innov.service.criteria.FormuleAdhesionCriteria;
import com.it4innov.service.dto.FormuleAdhesionDTO;
import com.it4innov.service.mapper.FormuleAdhesionMapper;
import java.time.Instant;
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
 * Integration tests for the {@link FormuleAdhesionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormuleAdhesionResourceIT {

    private static final Boolean DEFAULT_ADHESION_PERIODIQUE = false;
    private static final Boolean UPDATED_ADHESION_PERIODIQUE = true;

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DUREE_ADHESION_MOIS = 1;
    private static final Integer UPDATED_DUREE_ADHESION_MOIS = 2;
    private static final Integer SMALLER_DUREE_ADHESION_MOIS = 1 - 1;

    private static final Boolean DEFAULT_MONTANT_LIBRE = false;
    private static final Boolean UPDATED_MONTANT_LIBRE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_TARIF = 1D;
    private static final Double UPDATED_TARIF = 2D;
    private static final Double SMALLER_TARIF = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/formule-adhesions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormuleAdhesionRepository formuleAdhesionRepository;

    @Autowired
    private FormuleAdhesionMapper formuleAdhesionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormuleAdhesionMockMvc;

    private FormuleAdhesion formuleAdhesion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormuleAdhesion createEntity(EntityManager em) {
        FormuleAdhesion formuleAdhesion = new FormuleAdhesion()
            .adhesionPeriodique(DEFAULT_ADHESION_PERIODIQUE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dureeAdhesionMois(DEFAULT_DUREE_ADHESION_MOIS)
            .montantLibre(DEFAULT_MONTANT_LIBRE)
            .description(DEFAULT_DESCRIPTION)
            .tarif(DEFAULT_TARIF);
        return formuleAdhesion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormuleAdhesion createUpdatedEntity(EntityManager em) {
        FormuleAdhesion formuleAdhesion = new FormuleAdhesion()
            .adhesionPeriodique(UPDATED_ADHESION_PERIODIQUE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeAdhesionMois(UPDATED_DUREE_ADHESION_MOIS)
            .montantLibre(UPDATED_MONTANT_LIBRE)
            .description(UPDATED_DESCRIPTION)
            .tarif(UPDATED_TARIF);
        return formuleAdhesion;
    }

    @BeforeEach
    public void initTest() {
        formuleAdhesion = createEntity(em);
    }

    @Test
    @Transactional
    void createFormuleAdhesion() throws Exception {
        int databaseSizeBeforeCreate = formuleAdhesionRepository.findAll().size();
        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);
        restFormuleAdhesionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeCreate + 1);
        FormuleAdhesion testFormuleAdhesion = formuleAdhesionList.get(formuleAdhesionList.size() - 1);
        assertThat(testFormuleAdhesion.getAdhesionPeriodique()).isEqualTo(DEFAULT_ADHESION_PERIODIQUE);
        assertThat(testFormuleAdhesion.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFormuleAdhesion.getDureeAdhesionMois()).isEqualTo(DEFAULT_DUREE_ADHESION_MOIS);
        assertThat(testFormuleAdhesion.getMontantLibre()).isEqualTo(DEFAULT_MONTANT_LIBRE);
        assertThat(testFormuleAdhesion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormuleAdhesion.getTarif()).isEqualTo(DEFAULT_TARIF);
    }

    @Test
    @Transactional
    void createFormuleAdhesionWithExistingId() throws Exception {
        // Create the FormuleAdhesion with an existing ID
        formuleAdhesion.setId(1L);
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        int databaseSizeBeforeCreate = formuleAdhesionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormuleAdhesionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesions() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formuleAdhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].adhesionPeriodique").value(hasItem(DEFAULT_ADHESION_PERIODIQUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dureeAdhesionMois").value(hasItem(DEFAULT_DUREE_ADHESION_MOIS)))
            .andExpect(jsonPath("$.[*].montantLibre").value(hasItem(DEFAULT_MONTANT_LIBRE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.doubleValue())));
    }

    @Test
    @Transactional
    void getFormuleAdhesion() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get the formuleAdhesion
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL_ID, formuleAdhesion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formuleAdhesion.getId().intValue()))
            .andExpect(jsonPath("$.adhesionPeriodique").value(DEFAULT_ADHESION_PERIODIQUE.booleanValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dureeAdhesionMois").value(DEFAULT_DUREE_ADHESION_MOIS))
            .andExpect(jsonPath("$.montantLibre").value(DEFAULT_MONTANT_LIBRE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.tarif").value(DEFAULT_TARIF.doubleValue()));
    }

    @Test
    @Transactional
    void getFormuleAdhesionsByIdFiltering() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        Long id = formuleAdhesion.getId();

        defaultFormuleAdhesionShouldBeFound("id.equals=" + id);
        defaultFormuleAdhesionShouldNotBeFound("id.notEquals=" + id);

        defaultFormuleAdhesionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormuleAdhesionShouldNotBeFound("id.greaterThan=" + id);

        defaultFormuleAdhesionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormuleAdhesionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByAdhesionPeriodiqueIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where adhesionPeriodique equals to DEFAULT_ADHESION_PERIODIQUE
        defaultFormuleAdhesionShouldBeFound("adhesionPeriodique.equals=" + DEFAULT_ADHESION_PERIODIQUE);

        // Get all the formuleAdhesionList where adhesionPeriodique equals to UPDATED_ADHESION_PERIODIQUE
        defaultFormuleAdhesionShouldNotBeFound("adhesionPeriodique.equals=" + UPDATED_ADHESION_PERIODIQUE);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByAdhesionPeriodiqueIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where adhesionPeriodique in DEFAULT_ADHESION_PERIODIQUE or UPDATED_ADHESION_PERIODIQUE
        defaultFormuleAdhesionShouldBeFound("adhesionPeriodique.in=" + DEFAULT_ADHESION_PERIODIQUE + "," + UPDATED_ADHESION_PERIODIQUE);

        // Get all the formuleAdhesionList where adhesionPeriodique equals to UPDATED_ADHESION_PERIODIQUE
        defaultFormuleAdhesionShouldNotBeFound("adhesionPeriodique.in=" + UPDATED_ADHESION_PERIODIQUE);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByAdhesionPeriodiqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where adhesionPeriodique is not null
        defaultFormuleAdhesionShouldBeFound("adhesionPeriodique.specified=true");

        // Get all the formuleAdhesionList where adhesionPeriodique is null
        defaultFormuleAdhesionShouldNotBeFound("adhesionPeriodique.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultFormuleAdhesionShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the formuleAdhesionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultFormuleAdhesionShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultFormuleAdhesionShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the formuleAdhesionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultFormuleAdhesionShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dateDebut is not null
        defaultFormuleAdhesionShouldBeFound("dateDebut.specified=true");

        // Get all the formuleAdhesionList where dateDebut is null
        defaultFormuleAdhesionShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois equals to DEFAULT_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.equals=" + DEFAULT_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois equals to UPDATED_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.equals=" + UPDATED_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois in DEFAULT_DUREE_ADHESION_MOIS or UPDATED_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.in=" + DEFAULT_DUREE_ADHESION_MOIS + "," + UPDATED_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois equals to UPDATED_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.in=" + UPDATED_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois is not null
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.specified=true");

        // Get all the formuleAdhesionList where dureeAdhesionMois is null
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois is greater than or equal to DEFAULT_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.greaterThanOrEqual=" + DEFAULT_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois is greater than or equal to UPDATED_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.greaterThanOrEqual=" + UPDATED_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois is less than or equal to DEFAULT_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.lessThanOrEqual=" + DEFAULT_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois is less than or equal to SMALLER_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.lessThanOrEqual=" + SMALLER_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsLessThanSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois is less than DEFAULT_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.lessThan=" + DEFAULT_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois is less than UPDATED_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.lessThan=" + UPDATED_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDureeAdhesionMoisIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where dureeAdhesionMois is greater than DEFAULT_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldNotBeFound("dureeAdhesionMois.greaterThan=" + DEFAULT_DUREE_ADHESION_MOIS);

        // Get all the formuleAdhesionList where dureeAdhesionMois is greater than SMALLER_DUREE_ADHESION_MOIS
        defaultFormuleAdhesionShouldBeFound("dureeAdhesionMois.greaterThan=" + SMALLER_DUREE_ADHESION_MOIS);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByMontantLibreIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where montantLibre equals to DEFAULT_MONTANT_LIBRE
        defaultFormuleAdhesionShouldBeFound("montantLibre.equals=" + DEFAULT_MONTANT_LIBRE);

        // Get all the formuleAdhesionList where montantLibre equals to UPDATED_MONTANT_LIBRE
        defaultFormuleAdhesionShouldNotBeFound("montantLibre.equals=" + UPDATED_MONTANT_LIBRE);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByMontantLibreIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where montantLibre in DEFAULT_MONTANT_LIBRE or UPDATED_MONTANT_LIBRE
        defaultFormuleAdhesionShouldBeFound("montantLibre.in=" + DEFAULT_MONTANT_LIBRE + "," + UPDATED_MONTANT_LIBRE);

        // Get all the formuleAdhesionList where montantLibre equals to UPDATED_MONTANT_LIBRE
        defaultFormuleAdhesionShouldNotBeFound("montantLibre.in=" + UPDATED_MONTANT_LIBRE);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByMontantLibreIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where montantLibre is not null
        defaultFormuleAdhesionShouldBeFound("montantLibre.specified=true");

        // Get all the formuleAdhesionList where montantLibre is null
        defaultFormuleAdhesionShouldNotBeFound("montantLibre.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where description equals to DEFAULT_DESCRIPTION
        defaultFormuleAdhesionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the formuleAdhesionList where description equals to UPDATED_DESCRIPTION
        defaultFormuleAdhesionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFormuleAdhesionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the formuleAdhesionList where description equals to UPDATED_DESCRIPTION
        defaultFormuleAdhesionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where description is not null
        defaultFormuleAdhesionShouldBeFound("description.specified=true");

        // Get all the formuleAdhesionList where description is null
        defaultFormuleAdhesionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where description contains DEFAULT_DESCRIPTION
        defaultFormuleAdhesionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the formuleAdhesionList where description contains UPDATED_DESCRIPTION
        defaultFormuleAdhesionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where description does not contain DEFAULT_DESCRIPTION
        defaultFormuleAdhesionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the formuleAdhesionList where description does not contain UPDATED_DESCRIPTION
        defaultFormuleAdhesionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif equals to DEFAULT_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.equals=" + DEFAULT_TARIF);

        // Get all the formuleAdhesionList where tarif equals to UPDATED_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.equals=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsInShouldWork() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif in DEFAULT_TARIF or UPDATED_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.in=" + DEFAULT_TARIF + "," + UPDATED_TARIF);

        // Get all the formuleAdhesionList where tarif equals to UPDATED_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.in=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsNullOrNotNull() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif is not null
        defaultFormuleAdhesionShouldBeFound("tarif.specified=true");

        // Get all the formuleAdhesionList where tarif is null
        defaultFormuleAdhesionShouldNotBeFound("tarif.specified=false");
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif is greater than or equal to DEFAULT_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.greaterThanOrEqual=" + DEFAULT_TARIF);

        // Get all the formuleAdhesionList where tarif is greater than or equal to UPDATED_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.greaterThanOrEqual=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif is less than or equal to DEFAULT_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.lessThanOrEqual=" + DEFAULT_TARIF);

        // Get all the formuleAdhesionList where tarif is less than or equal to SMALLER_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.lessThanOrEqual=" + SMALLER_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsLessThanSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif is less than DEFAULT_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.lessThan=" + DEFAULT_TARIF);

        // Get all the formuleAdhesionList where tarif is less than UPDATED_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.lessThan=" + UPDATED_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByTarifIsGreaterThanSomething() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        // Get all the formuleAdhesionList where tarif is greater than DEFAULT_TARIF
        defaultFormuleAdhesionShouldNotBeFound("tarif.greaterThan=" + DEFAULT_TARIF);

        // Get all the formuleAdhesionList where tarif is greater than SMALLER_TARIF
        defaultFormuleAdhesionShouldBeFound("tarif.greaterThan=" + SMALLER_TARIF);
    }

    @Test
    @Transactional
    void getAllFormuleAdhesionsByAdhesionIsEqualToSomething() throws Exception {
        Adhesion adhesion;
        if (TestUtil.findAll(em, Adhesion.class).isEmpty()) {
            formuleAdhesionRepository.saveAndFlush(formuleAdhesion);
            adhesion = AdhesionResourceIT.createEntity(em);
        } else {
            adhesion = TestUtil.findAll(em, Adhesion.class).get(0);
        }
        em.persist(adhesion);
        em.flush();
        formuleAdhesion.setAdhesion(adhesion);
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);
        Long adhesionId = adhesion.getId();

        // Get all the formuleAdhesionList where adhesion equals to adhesionId
        defaultFormuleAdhesionShouldBeFound("adhesionId.equals=" + adhesionId);

        // Get all the formuleAdhesionList where adhesion equals to (adhesionId + 1)
        defaultFormuleAdhesionShouldNotBeFound("adhesionId.equals=" + (adhesionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormuleAdhesionShouldBeFound(String filter) throws Exception {
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formuleAdhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].adhesionPeriodique").value(hasItem(DEFAULT_ADHESION_PERIODIQUE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dureeAdhesionMois").value(hasItem(DEFAULT_DUREE_ADHESION_MOIS)))
            .andExpect(jsonPath("$.[*].montantLibre").value(hasItem(DEFAULT_MONTANT_LIBRE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tarif").value(hasItem(DEFAULT_TARIF.doubleValue())));

        // Check, that the count call also returns 1
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormuleAdhesionShouldNotBeFound(String filter) throws Exception {
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormuleAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFormuleAdhesion() throws Exception {
        // Get the formuleAdhesion
        restFormuleAdhesionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormuleAdhesion() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();

        // Update the formuleAdhesion
        FormuleAdhesion updatedFormuleAdhesion = formuleAdhesionRepository.findById(formuleAdhesion.getId()).get();
        // Disconnect from session so that the updates on updatedFormuleAdhesion are not directly saved in db
        em.detach(updatedFormuleAdhesion);
        updatedFormuleAdhesion
            .adhesionPeriodique(UPDATED_ADHESION_PERIODIQUE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeAdhesionMois(UPDATED_DUREE_ADHESION_MOIS)
            .montantLibre(UPDATED_MONTANT_LIBRE)
            .description(UPDATED_DESCRIPTION)
            .tarif(UPDATED_TARIF);
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(updatedFormuleAdhesion);

        restFormuleAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formuleAdhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
        FormuleAdhesion testFormuleAdhesion = formuleAdhesionList.get(formuleAdhesionList.size() - 1);
        assertThat(testFormuleAdhesion.getAdhesionPeriodique()).isEqualTo(UPDATED_ADHESION_PERIODIQUE);
        assertThat(testFormuleAdhesion.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormuleAdhesion.getDureeAdhesionMois()).isEqualTo(UPDATED_DUREE_ADHESION_MOIS);
        assertThat(testFormuleAdhesion.getMontantLibre()).isEqualTo(UPDATED_MONTANT_LIBRE);
        assertThat(testFormuleAdhesion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormuleAdhesion.getTarif()).isEqualTo(UPDATED_TARIF);
    }

    @Test
    @Transactional
    void putNonExistingFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formuleAdhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormuleAdhesionWithPatch() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();

        // Update the formuleAdhesion using partial update
        FormuleAdhesion partialUpdatedFormuleAdhesion = new FormuleAdhesion();
        partialUpdatedFormuleAdhesion.setId(formuleAdhesion.getId());

        partialUpdatedFormuleAdhesion
            .adhesionPeriodique(UPDATED_ADHESION_PERIODIQUE)
            .montantLibre(UPDATED_MONTANT_LIBRE)
            .tarif(UPDATED_TARIF);

        restFormuleAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormuleAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormuleAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
        FormuleAdhesion testFormuleAdhesion = formuleAdhesionList.get(formuleAdhesionList.size() - 1);
        assertThat(testFormuleAdhesion.getAdhesionPeriodique()).isEqualTo(UPDATED_ADHESION_PERIODIQUE);
        assertThat(testFormuleAdhesion.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testFormuleAdhesion.getDureeAdhesionMois()).isEqualTo(DEFAULT_DUREE_ADHESION_MOIS);
        assertThat(testFormuleAdhesion.getMontantLibre()).isEqualTo(UPDATED_MONTANT_LIBRE);
        assertThat(testFormuleAdhesion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormuleAdhesion.getTarif()).isEqualTo(UPDATED_TARIF);
    }

    @Test
    @Transactional
    void fullUpdateFormuleAdhesionWithPatch() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();

        // Update the formuleAdhesion using partial update
        FormuleAdhesion partialUpdatedFormuleAdhesion = new FormuleAdhesion();
        partialUpdatedFormuleAdhesion.setId(formuleAdhesion.getId());

        partialUpdatedFormuleAdhesion
            .adhesionPeriodique(UPDATED_ADHESION_PERIODIQUE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dureeAdhesionMois(UPDATED_DUREE_ADHESION_MOIS)
            .montantLibre(UPDATED_MONTANT_LIBRE)
            .description(UPDATED_DESCRIPTION)
            .tarif(UPDATED_TARIF);

        restFormuleAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormuleAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormuleAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
        FormuleAdhesion testFormuleAdhesion = formuleAdhesionList.get(formuleAdhesionList.size() - 1);
        assertThat(testFormuleAdhesion.getAdhesionPeriodique()).isEqualTo(UPDATED_ADHESION_PERIODIQUE);
        assertThat(testFormuleAdhesion.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testFormuleAdhesion.getDureeAdhesionMois()).isEqualTo(UPDATED_DUREE_ADHESION_MOIS);
        assertThat(testFormuleAdhesion.getMontantLibre()).isEqualTo(UPDATED_MONTANT_LIBRE);
        assertThat(testFormuleAdhesion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormuleAdhesion.getTarif()).isEqualTo(UPDATED_TARIF);
    }

    @Test
    @Transactional
    void patchNonExistingFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formuleAdhesionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormuleAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = formuleAdhesionRepository.findAll().size();
        formuleAdhesion.setId(count.incrementAndGet());

        // Create the FormuleAdhesion
        FormuleAdhesionDTO formuleAdhesionDTO = formuleAdhesionMapper.toDto(formuleAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormuleAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formuleAdhesionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormuleAdhesion in the database
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormuleAdhesion() throws Exception {
        // Initialize the database
        formuleAdhesionRepository.saveAndFlush(formuleAdhesion);

        int databaseSizeBeforeDelete = formuleAdhesionRepository.findAll().size();

        // Delete the formuleAdhesion
        restFormuleAdhesionMockMvc
            .perform(delete(ENTITY_API_URL_ID, formuleAdhesion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormuleAdhesion> formuleAdhesionList = formuleAdhesionRepository.findAll();
        assertThat(formuleAdhesionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
