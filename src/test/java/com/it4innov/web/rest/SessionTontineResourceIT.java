package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.repository.SessionTontineRepository;
import com.it4innov.service.criteria.SessionTontineCriteria;
import com.it4innov.service.dto.SessionTontineDTO;
import com.it4innov.service.mapper.SessionTontineMapper;
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
 * Integration tests for the {@link SessionTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionTontineResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/session-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionTontineRepository sessionTontineRepository;

    @Autowired
    private SessionTontineMapper sessionTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionTontineMockMvc;

    private SessionTontine sessionTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionTontine createEntity(EntityManager em) {
        SessionTontine sessionTontine = new SessionTontine()
            .libelle(DEFAULT_LIBELLE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return sessionTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionTontine createUpdatedEntity(EntityManager em) {
        SessionTontine sessionTontine = new SessionTontine()
            .libelle(UPDATED_LIBELLE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return sessionTontine;
    }

    @BeforeEach
    public void initTest() {
        sessionTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionTontine() throws Exception {
        int databaseSizeBeforeCreate = sessionTontineRepository.findAll().size();
        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);
        restSessionTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeCreate + 1);
        SessionTontine testSessionTontine = sessionTontineList.get(sessionTontineList.size() - 1);
        assertThat(testSessionTontine.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSessionTontine.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testSessionTontine.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createSessionTontineWithExistingId() throws Exception {
        // Create the SessionTontine with an existing ID
        sessionTontine.setId(1L);
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        int databaseSizeBeforeCreate = sessionTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSessionTontines() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getSessionTontine() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get the sessionTontine
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionTontine.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getSessionTontinesByIdFiltering() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        Long id = sessionTontine.getId();

        defaultSessionTontineShouldBeFound("id.equals=" + id);
        defaultSessionTontineShouldNotBeFound("id.notEquals=" + id);

        defaultSessionTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSessionTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultSessionTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSessionTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where libelle equals to DEFAULT_LIBELLE
        defaultSessionTontineShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sessionTontineList where libelle equals to UPDATED_LIBELLE
        defaultSessionTontineShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSessionTontineShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sessionTontineList where libelle equals to UPDATED_LIBELLE
        defaultSessionTontineShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where libelle is not null
        defaultSessionTontineShouldBeFound("libelle.specified=true");

        // Get all the sessionTontineList where libelle is null
        defaultSessionTontineShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSessionTontinesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where libelle contains DEFAULT_LIBELLE
        defaultSessionTontineShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sessionTontineList where libelle contains UPDATED_LIBELLE
        defaultSessionTontineShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where libelle does not contain DEFAULT_LIBELLE
        defaultSessionTontineShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sessionTontineList where libelle does not contain UPDATED_LIBELLE
        defaultSessionTontineShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut is not null
        defaultSessionTontineShouldBeFound("dateDebut.specified=true");

        // Get all the sessionTontineList where dateDebut is null
        defaultSessionTontineShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultSessionTontineShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the sessionTontineList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultSessionTontineShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin equals to DEFAULT_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the sessionTontineList where dateFin equals to UPDATED_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the sessionTontineList where dateFin equals to UPDATED_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin is not null
        defaultSessionTontineShouldBeFound("dateFin.specified=true");

        // Get all the sessionTontineList where dateFin is null
        defaultSessionTontineShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the sessionTontineList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the sessionTontineList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin is less than DEFAULT_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the sessionTontineList where dateFin is less than UPDATED_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        // Get all the sessionTontineList where dateFin is greater than DEFAULT_DATE_FIN
        defaultSessionTontineShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the sessionTontineList where dateFin is greater than SMALLER_DATE_FIN
        defaultSessionTontineShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllSessionTontinesByCotisationTontineIsEqualToSomething() throws Exception {
        CotisationTontine cotisationTontine;
        if (TestUtil.findAll(em, CotisationTontine.class).isEmpty()) {
            sessionTontineRepository.saveAndFlush(sessionTontine);
            cotisationTontine = CotisationTontineResourceIT.createEntity(em);
        } else {
            cotisationTontine = TestUtil.findAll(em, CotisationTontine.class).get(0);
        }
        em.persist(cotisationTontine);
        em.flush();
        sessionTontine.addCotisationTontine(cotisationTontine);
        sessionTontineRepository.saveAndFlush(sessionTontine);
        Long cotisationTontineId = cotisationTontine.getId();

        // Get all the sessionTontineList where cotisationTontine equals to cotisationTontineId
        defaultSessionTontineShouldBeFound("cotisationTontineId.equals=" + cotisationTontineId);

        // Get all the sessionTontineList where cotisationTontine equals to (cotisationTontineId + 1)
        defaultSessionTontineShouldNotBeFound("cotisationTontineId.equals=" + (cotisationTontineId + 1));
    }

    @Test
    @Transactional
    void getAllSessionTontinesByDecaissementTontineIsEqualToSomething() throws Exception {
        DecaissementTontine decaissementTontine;
        if (TestUtil.findAll(em, DecaissementTontine.class).isEmpty()) {
            sessionTontineRepository.saveAndFlush(sessionTontine);
            decaissementTontine = DecaissementTontineResourceIT.createEntity(em);
        } else {
            decaissementTontine = TestUtil.findAll(em, DecaissementTontine.class).get(0);
        }
        em.persist(decaissementTontine);
        em.flush();
        sessionTontine.addDecaissementTontine(decaissementTontine);
        sessionTontineRepository.saveAndFlush(sessionTontine);
        Long decaissementTontineId = decaissementTontine.getId();

        // Get all the sessionTontineList where decaissementTontine equals to decaissementTontineId
        defaultSessionTontineShouldBeFound("decaissementTontineId.equals=" + decaissementTontineId);

        // Get all the sessionTontineList where decaissementTontine equals to (decaissementTontineId + 1)
        defaultSessionTontineShouldNotBeFound("decaissementTontineId.equals=" + (decaissementTontineId + 1));
    }

    @Test
    @Transactional
    void getAllSessionTontinesByTontineIsEqualToSomething() throws Exception {
        Tontine tontine;
        if (TestUtil.findAll(em, Tontine.class).isEmpty()) {
            sessionTontineRepository.saveAndFlush(sessionTontine);
            tontine = TontineResourceIT.createEntity(em);
        } else {
            tontine = TestUtil.findAll(em, Tontine.class).get(0);
        }
        em.persist(tontine);
        em.flush();
        sessionTontine.setTontine(tontine);
        sessionTontineRepository.saveAndFlush(sessionTontine);
        Long tontineId = tontine.getId();

        // Get all the sessionTontineList where tontine equals to tontineId
        defaultSessionTontineShouldBeFound("tontineId.equals=" + tontineId);

        // Get all the sessionTontineList where tontine equals to (tontineId + 1)
        defaultSessionTontineShouldNotBeFound("tontineId.equals=" + (tontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSessionTontineShouldBeFound(String filter) throws Exception {
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));

        // Check, that the count call also returns 1
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSessionTontineShouldNotBeFound(String filter) throws Exception {
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSessionTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSessionTontine() throws Exception {
        // Get the sessionTontine
        restSessionTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSessionTontine() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();

        // Update the sessionTontine
        SessionTontine updatedSessionTontine = sessionTontineRepository.findById(sessionTontine.getId()).get();
        // Disconnect from session so that the updates on updatedSessionTontine are not directly saved in db
        em.detach(updatedSessionTontine);
        updatedSessionTontine.libelle(UPDATED_LIBELLE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(updatedSessionTontine);

        restSessionTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
        SessionTontine testSessionTontine = sessionTontineList.get(sessionTontineList.size() - 1);
        assertThat(testSessionTontine.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSessionTontine.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSessionTontine.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionTontineWithPatch() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();

        // Update the sessionTontine using partial update
        SessionTontine partialUpdatedSessionTontine = new SessionTontine();
        partialUpdatedSessionTontine.setId(sessionTontine.getId());

        partialUpdatedSessionTontine.dateDebut(UPDATED_DATE_DEBUT);

        restSessionTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionTontine))
            )
            .andExpect(status().isOk());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
        SessionTontine testSessionTontine = sessionTontineList.get(sessionTontineList.size() - 1);
        assertThat(testSessionTontine.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSessionTontine.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSessionTontine.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateSessionTontineWithPatch() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();

        // Update the sessionTontine using partial update
        SessionTontine partialUpdatedSessionTontine = new SessionTontine();
        partialUpdatedSessionTontine.setId(sessionTontine.getId());

        partialUpdatedSessionTontine.libelle(UPDATED_LIBELLE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restSessionTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionTontine))
            )
            .andExpect(status().isOk());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
        SessionTontine testSessionTontine = sessionTontineList.get(sessionTontineList.size() - 1);
        assertThat(testSessionTontine.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSessionTontine.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testSessionTontine.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionTontine() throws Exception {
        int databaseSizeBeforeUpdate = sessionTontineRepository.findAll().size();
        sessionTontine.setId(count.incrementAndGet());

        // Create the SessionTontine
        SessionTontineDTO sessionTontineDTO = sessionTontineMapper.toDto(sessionTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionTontine in the database
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionTontine() throws Exception {
        // Initialize the database
        sessionTontineRepository.saveAndFlush(sessionTontine);

        int databaseSizeBeforeDelete = sessionTontineRepository.findAll().size();

        // Delete the sessionTontine
        restSessionTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionTontine> sessionTontineList = sessionTontineRepository.findAll();
        assertThat(sessionTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
