package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.GestionnaireTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.repository.GestionnaireTontineRepository;
import com.it4innov.service.criteria.GestionnaireTontineCriteria;
import com.it4innov.service.dto.GestionnaireTontineDTO;
import com.it4innov.service.mapper.GestionnaireTontineMapper;
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
 * Integration tests for the {@link GestionnaireTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GestionnaireTontineResourceIT {

    private static final String DEFAULT_MATRICULE_ADHERENT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ADHERENT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TONTINE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TONTINE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_PRISE_FONCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PRISE_FONCTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PRISE_FONCTION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN_FONCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_FONCTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN_FONCTION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/gestionnaire-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GestionnaireTontineRepository gestionnaireTontineRepository;

    @Autowired
    private GestionnaireTontineMapper gestionnaireTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGestionnaireTontineMockMvc;

    private GestionnaireTontine gestionnaireTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GestionnaireTontine createEntity(EntityManager em) {
        GestionnaireTontine gestionnaireTontine = new GestionnaireTontine()
            .matriculeAdherent(DEFAULT_MATRICULE_ADHERENT)
            .codeTontine(DEFAULT_CODE_TONTINE)
            .datePriseFonction(DEFAULT_DATE_PRISE_FONCTION)
            .dateFinFonction(DEFAULT_DATE_FIN_FONCTION);
        return gestionnaireTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GestionnaireTontine createUpdatedEntity(EntityManager em) {
        GestionnaireTontine gestionnaireTontine = new GestionnaireTontine()
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeTontine(UPDATED_CODE_TONTINE)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION);
        return gestionnaireTontine;
    }

    @BeforeEach
    public void initTest() {
        gestionnaireTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createGestionnaireTontine() throws Exception {
        int databaseSizeBeforeCreate = gestionnaireTontineRepository.findAll().size();
        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);
        restGestionnaireTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeCreate + 1);
        GestionnaireTontine testGestionnaireTontine = gestionnaireTontineList.get(gestionnaireTontineList.size() - 1);
        assertThat(testGestionnaireTontine.getMatriculeAdherent()).isEqualTo(DEFAULT_MATRICULE_ADHERENT);
        assertThat(testGestionnaireTontine.getCodeTontine()).isEqualTo(DEFAULT_CODE_TONTINE);
        assertThat(testGestionnaireTontine.getDatePriseFonction()).isEqualTo(DEFAULT_DATE_PRISE_FONCTION);
        assertThat(testGestionnaireTontine.getDateFinFonction()).isEqualTo(DEFAULT_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void createGestionnaireTontineWithExistingId() throws Exception {
        // Create the GestionnaireTontine with an existing ID
        gestionnaireTontine.setId(1L);
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        int databaseSizeBeforeCreate = gestionnaireTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGestionnaireTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontines() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaireTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].codeTontine").value(hasItem(DEFAULT_CODE_TONTINE)))
            .andExpect(jsonPath("$.[*].datePriseFonction").value(hasItem(DEFAULT_DATE_PRISE_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateFinFonction").value(hasItem(DEFAULT_DATE_FIN_FONCTION.toString())));
    }

    @Test
    @Transactional
    void getGestionnaireTontine() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get the gestionnaireTontine
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, gestionnaireTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gestionnaireTontine.getId().intValue()))
            .andExpect(jsonPath("$.matriculeAdherent").value(DEFAULT_MATRICULE_ADHERENT))
            .andExpect(jsonPath("$.codeTontine").value(DEFAULT_CODE_TONTINE))
            .andExpect(jsonPath("$.datePriseFonction").value(DEFAULT_DATE_PRISE_FONCTION.toString()))
            .andExpect(jsonPath("$.dateFinFonction").value(DEFAULT_DATE_FIN_FONCTION.toString()));
    }

    @Test
    @Transactional
    void getGestionnaireTontinesByIdFiltering() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        Long id = gestionnaireTontine.getId();

        defaultGestionnaireTontineShouldBeFound("id.equals=" + id);
        defaultGestionnaireTontineShouldNotBeFound("id.notEquals=" + id);

        defaultGestionnaireTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGestionnaireTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultGestionnaireTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGestionnaireTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByMatriculeAdherentIsEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where matriculeAdherent equals to DEFAULT_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldBeFound("matriculeAdherent.equals=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the gestionnaireTontineList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldNotBeFound("matriculeAdherent.equals=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByMatriculeAdherentIsInShouldWork() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where matriculeAdherent in DEFAULT_MATRICULE_ADHERENT or UPDATED_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldBeFound("matriculeAdherent.in=" + DEFAULT_MATRICULE_ADHERENT + "," + UPDATED_MATRICULE_ADHERENT);

        // Get all the gestionnaireTontineList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldNotBeFound("matriculeAdherent.in=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByMatriculeAdherentIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where matriculeAdherent is not null
        defaultGestionnaireTontineShouldBeFound("matriculeAdherent.specified=true");

        // Get all the gestionnaireTontineList where matriculeAdherent is null
        defaultGestionnaireTontineShouldNotBeFound("matriculeAdherent.specified=false");
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByMatriculeAdherentContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where matriculeAdherent contains DEFAULT_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldBeFound("matriculeAdherent.contains=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the gestionnaireTontineList where matriculeAdherent contains UPDATED_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldNotBeFound("matriculeAdherent.contains=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByMatriculeAdherentNotContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where matriculeAdherent does not contain DEFAULT_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldNotBeFound("matriculeAdherent.doesNotContain=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the gestionnaireTontineList where matriculeAdherent does not contain UPDATED_MATRICULE_ADHERENT
        defaultGestionnaireTontineShouldBeFound("matriculeAdherent.doesNotContain=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByCodeTontineIsEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where codeTontine equals to DEFAULT_CODE_TONTINE
        defaultGestionnaireTontineShouldBeFound("codeTontine.equals=" + DEFAULT_CODE_TONTINE);

        // Get all the gestionnaireTontineList where codeTontine equals to UPDATED_CODE_TONTINE
        defaultGestionnaireTontineShouldNotBeFound("codeTontine.equals=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByCodeTontineIsInShouldWork() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where codeTontine in DEFAULT_CODE_TONTINE or UPDATED_CODE_TONTINE
        defaultGestionnaireTontineShouldBeFound("codeTontine.in=" + DEFAULT_CODE_TONTINE + "," + UPDATED_CODE_TONTINE);

        // Get all the gestionnaireTontineList where codeTontine equals to UPDATED_CODE_TONTINE
        defaultGestionnaireTontineShouldNotBeFound("codeTontine.in=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByCodeTontineIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where codeTontine is not null
        defaultGestionnaireTontineShouldBeFound("codeTontine.specified=true");

        // Get all the gestionnaireTontineList where codeTontine is null
        defaultGestionnaireTontineShouldNotBeFound("codeTontine.specified=false");
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByCodeTontineContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where codeTontine contains DEFAULT_CODE_TONTINE
        defaultGestionnaireTontineShouldBeFound("codeTontine.contains=" + DEFAULT_CODE_TONTINE);

        // Get all the gestionnaireTontineList where codeTontine contains UPDATED_CODE_TONTINE
        defaultGestionnaireTontineShouldNotBeFound("codeTontine.contains=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByCodeTontineNotContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where codeTontine does not contain DEFAULT_CODE_TONTINE
        defaultGestionnaireTontineShouldNotBeFound("codeTontine.doesNotContain=" + DEFAULT_CODE_TONTINE);

        // Get all the gestionnaireTontineList where codeTontine does not contain UPDATED_CODE_TONTINE
        defaultGestionnaireTontineShouldBeFound("codeTontine.doesNotContain=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction equals to DEFAULT_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.equals=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction equals to UPDATED_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.equals=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction in DEFAULT_DATE_PRISE_FONCTION or UPDATED_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.in=" + DEFAULT_DATE_PRISE_FONCTION + "," + UPDATED_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction equals to UPDATED_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.in=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction is not null
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.specified=true");

        // Get all the gestionnaireTontineList where datePriseFonction is null
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.specified=false");
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction is greater than or equal to DEFAULT_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.greaterThanOrEqual=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction is greater than or equal to UPDATED_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.greaterThanOrEqual=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction is less than or equal to DEFAULT_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.lessThanOrEqual=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction is less than or equal to SMALLER_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.lessThanOrEqual=" + SMALLER_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsLessThanSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction is less than DEFAULT_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.lessThan=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction is less than UPDATED_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.lessThan=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDatePriseFonctionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where datePriseFonction is greater than DEFAULT_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("datePriseFonction.greaterThan=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the gestionnaireTontineList where datePriseFonction is greater than SMALLER_DATE_PRISE_FONCTION
        defaultGestionnaireTontineShouldBeFound("datePriseFonction.greaterThan=" + SMALLER_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction equals to DEFAULT_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.equals=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction equals to UPDATED_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.equals=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction in DEFAULT_DATE_FIN_FONCTION or UPDATED_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.in=" + DEFAULT_DATE_FIN_FONCTION + "," + UPDATED_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction equals to UPDATED_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.in=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction is not null
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.specified=true");

        // Get all the gestionnaireTontineList where dateFinFonction is null
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.specified=false");
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction is greater than or equal to DEFAULT_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.greaterThanOrEqual=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction is greater than or equal to UPDATED_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.greaterThanOrEqual=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction is less than or equal to DEFAULT_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.lessThanOrEqual=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction is less than or equal to SMALLER_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.lessThanOrEqual=" + SMALLER_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsLessThanSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction is less than DEFAULT_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.lessThan=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction is less than UPDATED_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.lessThan=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByDateFinFonctionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        // Get all the gestionnaireTontineList where dateFinFonction is greater than DEFAULT_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldNotBeFound("dateFinFonction.greaterThan=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the gestionnaireTontineList where dateFinFonction is greater than SMALLER_DATE_FIN_FONCTION
        defaultGestionnaireTontineShouldBeFound("dateFinFonction.greaterThan=" + SMALLER_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllGestionnaireTontinesByTontineIsEqualToSomething() throws Exception {
        Tontine tontine;
        if (TestUtil.findAll(em, Tontine.class).isEmpty()) {
            gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);
            tontine = TontineResourceIT.createEntity(em);
        } else {
            tontine = TestUtil.findAll(em, Tontine.class).get(0);
        }
        em.persist(tontine);
        em.flush();
        gestionnaireTontine.setTontine(tontine);
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);
        Long tontineId = tontine.getId();

        // Get all the gestionnaireTontineList where tontine equals to tontineId
        defaultGestionnaireTontineShouldBeFound("tontineId.equals=" + tontineId);

        // Get all the gestionnaireTontineList where tontine equals to (tontineId + 1)
        defaultGestionnaireTontineShouldNotBeFound("tontineId.equals=" + (tontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGestionnaireTontineShouldBeFound(String filter) throws Exception {
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaireTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].codeTontine").value(hasItem(DEFAULT_CODE_TONTINE)))
            .andExpect(jsonPath("$.[*].datePriseFonction").value(hasItem(DEFAULT_DATE_PRISE_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateFinFonction").value(hasItem(DEFAULT_DATE_FIN_FONCTION.toString())));

        // Check, that the count call also returns 1
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGestionnaireTontineShouldNotBeFound(String filter) throws Exception {
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGestionnaireTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGestionnaireTontine() throws Exception {
        // Get the gestionnaireTontine
        restGestionnaireTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGestionnaireTontine() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();

        // Update the gestionnaireTontine
        GestionnaireTontine updatedGestionnaireTontine = gestionnaireTontineRepository.findById(gestionnaireTontine.getId()).get();
        // Disconnect from session so that the updates on updatedGestionnaireTontine are not directly saved in db
        em.detach(updatedGestionnaireTontine);
        updatedGestionnaireTontine
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeTontine(UPDATED_CODE_TONTINE)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION);
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(updatedGestionnaireTontine);

        restGestionnaireTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionnaireTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireTontine testGestionnaireTontine = gestionnaireTontineList.get(gestionnaireTontineList.size() - 1);
        assertThat(testGestionnaireTontine.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testGestionnaireTontine.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testGestionnaireTontine.getDatePriseFonction()).isEqualTo(UPDATED_DATE_PRISE_FONCTION);
        assertThat(testGestionnaireTontine.getDateFinFonction()).isEqualTo(UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void putNonExistingGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionnaireTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGestionnaireTontineWithPatch() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();

        // Update the gestionnaireTontine using partial update
        GestionnaireTontine partialUpdatedGestionnaireTontine = new GestionnaireTontine();
        partialUpdatedGestionnaireTontine.setId(gestionnaireTontine.getId());

        partialUpdatedGestionnaireTontine
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeTontine(UPDATED_CODE_TONTINE)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION);

        restGestionnaireTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestionnaireTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestionnaireTontine))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireTontine testGestionnaireTontine = gestionnaireTontineList.get(gestionnaireTontineList.size() - 1);
        assertThat(testGestionnaireTontine.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testGestionnaireTontine.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testGestionnaireTontine.getDatePriseFonction()).isEqualTo(UPDATED_DATE_PRISE_FONCTION);
        assertThat(testGestionnaireTontine.getDateFinFonction()).isEqualTo(DEFAULT_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void fullUpdateGestionnaireTontineWithPatch() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();

        // Update the gestionnaireTontine using partial update
        GestionnaireTontine partialUpdatedGestionnaireTontine = new GestionnaireTontine();
        partialUpdatedGestionnaireTontine.setId(gestionnaireTontine.getId());

        partialUpdatedGestionnaireTontine
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeTontine(UPDATED_CODE_TONTINE)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION);

        restGestionnaireTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestionnaireTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestionnaireTontine))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireTontine testGestionnaireTontine = gestionnaireTontineList.get(gestionnaireTontineList.size() - 1);
        assertThat(testGestionnaireTontine.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testGestionnaireTontine.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testGestionnaireTontine.getDatePriseFonction()).isEqualTo(UPDATED_DATE_PRISE_FONCTION);
        assertThat(testGestionnaireTontine.getDateFinFonction()).isEqualTo(UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void patchNonExistingGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gestionnaireTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGestionnaireTontine() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireTontineRepository.findAll().size();
        gestionnaireTontine.setId(count.incrementAndGet());

        // Create the GestionnaireTontine
        GestionnaireTontineDTO gestionnaireTontineDTO = gestionnaireTontineMapper.toDto(gestionnaireTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GestionnaireTontine in the database
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGestionnaireTontine() throws Exception {
        // Initialize the database
        gestionnaireTontineRepository.saveAndFlush(gestionnaireTontine);

        int databaseSizeBeforeDelete = gestionnaireTontineRepository.findAll().size();

        // Delete the gestionnaireTontine
        restGestionnaireTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, gestionnaireTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GestionnaireTontine> gestionnaireTontineList = gestionnaireTontineRepository.findAll();
        assertThat(gestionnaireTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
