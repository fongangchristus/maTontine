package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Fonction;
import com.it4innov.domain.FonctionAdherent;
import com.it4innov.domain.Personne;
import com.it4innov.repository.FonctionAdherentRepository;
import com.it4innov.service.criteria.FonctionAdherentCriteria;
import com.it4innov.service.dto.FonctionAdherentDTO;
import com.it4innov.service.mapper.FonctionAdherentMapper;
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
 * Integration tests for the {@link FonctionAdherentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FonctionAdherentResourceIT {

    private static final String DEFAULT_MATRICULE_ADHERENT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ADHERENT = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_FONCTION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_FONCTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_PRISE_FONCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PRISE_FONCTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_PRISE_FONCTION = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN_FONCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_FONCTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN_FONCTION = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/fonction-adherents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FonctionAdherentRepository fonctionAdherentRepository;

    @Autowired
    private FonctionAdherentMapper fonctionAdherentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFonctionAdherentMockMvc;

    private FonctionAdherent fonctionAdherent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FonctionAdherent createEntity(EntityManager em) {
        FonctionAdherent fonctionAdherent = new FonctionAdherent()
            .matriculeAdherent(DEFAULT_MATRICULE_ADHERENT)
            .codeFonction(DEFAULT_CODE_FONCTION)
            .datePriseFonction(DEFAULT_DATE_PRISE_FONCTION)
            .dateFinFonction(DEFAULT_DATE_FIN_FONCTION)
            .actif(DEFAULT_ACTIF);
        return fonctionAdherent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FonctionAdherent createUpdatedEntity(EntityManager em) {
        FonctionAdherent fonctionAdherent = new FonctionAdherent()
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeFonction(UPDATED_CODE_FONCTION)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION)
            .actif(UPDATED_ACTIF);
        return fonctionAdherent;
    }

    @BeforeEach
    public void initTest() {
        fonctionAdherent = createEntity(em);
    }

    @Test
    @Transactional
    void createFonctionAdherent() throws Exception {
        int databaseSizeBeforeCreate = fonctionAdherentRepository.findAll().size();
        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);
        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeCreate + 1);
        FonctionAdherent testFonctionAdherent = fonctionAdherentList.get(fonctionAdherentList.size() - 1);
        assertThat(testFonctionAdherent.getMatriculeAdherent()).isEqualTo(DEFAULT_MATRICULE_ADHERENT);
        assertThat(testFonctionAdherent.getCodeFonction()).isEqualTo(DEFAULT_CODE_FONCTION);
        assertThat(testFonctionAdherent.getDatePriseFonction()).isEqualTo(DEFAULT_DATE_PRISE_FONCTION);
        assertThat(testFonctionAdherent.getDateFinFonction()).isEqualTo(DEFAULT_DATE_FIN_FONCTION);
        assertThat(testFonctionAdherent.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void createFonctionAdherentWithExistingId() throws Exception {
        // Create the FonctionAdherent with an existing ID
        fonctionAdherent.setId(1L);
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        int databaseSizeBeforeCreate = fonctionAdherentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeAdherentIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionAdherentRepository.findAll().size();
        // set the field null
        fonctionAdherent.setMatriculeAdherent(null);

        // Create the FonctionAdherent, which fails.
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionAdherentRepository.findAll().size();
        // set the field null
        fonctionAdherent.setCodeFonction(null);

        // Create the FonctionAdherent, which fails.
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatePriseFonctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionAdherentRepository.findAll().size();
        // set the field null
        fonctionAdherent.setDatePriseFonction(null);

        // Create the FonctionAdherent, which fails.
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionAdherentRepository.findAll().size();
        // set the field null
        fonctionAdherent.setActif(null);

        // Create the FonctionAdherent, which fails.
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        restFonctionAdherentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFonctionAdherents() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionAdherent.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].codeFonction").value(hasItem(DEFAULT_CODE_FONCTION)))
            .andExpect(jsonPath("$.[*].datePriseFonction").value(hasItem(DEFAULT_DATE_PRISE_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateFinFonction").value(hasItem(DEFAULT_DATE_FIN_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));
    }

    @Test
    @Transactional
    void getFonctionAdherent() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get the fonctionAdherent
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL_ID, fonctionAdherent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fonctionAdherent.getId().intValue()))
            .andExpect(jsonPath("$.matriculeAdherent").value(DEFAULT_MATRICULE_ADHERENT))
            .andExpect(jsonPath("$.codeFonction").value(DEFAULT_CODE_FONCTION))
            .andExpect(jsonPath("$.datePriseFonction").value(DEFAULT_DATE_PRISE_FONCTION.toString()))
            .andExpect(jsonPath("$.dateFinFonction").value(DEFAULT_DATE_FIN_FONCTION.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()));
    }

    @Test
    @Transactional
    void getFonctionAdherentsByIdFiltering() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        Long id = fonctionAdherent.getId();

        defaultFonctionAdherentShouldBeFound("id.equals=" + id);
        defaultFonctionAdherentShouldNotBeFound("id.notEquals=" + id);

        defaultFonctionAdherentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFonctionAdherentShouldNotBeFound("id.greaterThan=" + id);

        defaultFonctionAdherentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFonctionAdherentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByMatriculeAdherentIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where matriculeAdherent equals to DEFAULT_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldBeFound("matriculeAdherent.equals=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the fonctionAdherentList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldNotBeFound("matriculeAdherent.equals=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByMatriculeAdherentIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where matriculeAdherent in DEFAULT_MATRICULE_ADHERENT or UPDATED_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldBeFound("matriculeAdherent.in=" + DEFAULT_MATRICULE_ADHERENT + "," + UPDATED_MATRICULE_ADHERENT);

        // Get all the fonctionAdherentList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldNotBeFound("matriculeAdherent.in=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByMatriculeAdherentIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where matriculeAdherent is not null
        defaultFonctionAdherentShouldBeFound("matriculeAdherent.specified=true");

        // Get all the fonctionAdherentList where matriculeAdherent is null
        defaultFonctionAdherentShouldNotBeFound("matriculeAdherent.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByMatriculeAdherentContainsSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where matriculeAdherent contains DEFAULT_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldBeFound("matriculeAdherent.contains=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the fonctionAdherentList where matriculeAdherent contains UPDATED_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldNotBeFound("matriculeAdherent.contains=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByMatriculeAdherentNotContainsSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where matriculeAdherent does not contain DEFAULT_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldNotBeFound("matriculeAdherent.doesNotContain=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the fonctionAdherentList where matriculeAdherent does not contain UPDATED_MATRICULE_ADHERENT
        defaultFonctionAdherentShouldBeFound("matriculeAdherent.doesNotContain=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByCodeFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where codeFonction equals to DEFAULT_CODE_FONCTION
        defaultFonctionAdherentShouldBeFound("codeFonction.equals=" + DEFAULT_CODE_FONCTION);

        // Get all the fonctionAdherentList where codeFonction equals to UPDATED_CODE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("codeFonction.equals=" + UPDATED_CODE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByCodeFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where codeFonction in DEFAULT_CODE_FONCTION or UPDATED_CODE_FONCTION
        defaultFonctionAdherentShouldBeFound("codeFonction.in=" + DEFAULT_CODE_FONCTION + "," + UPDATED_CODE_FONCTION);

        // Get all the fonctionAdherentList where codeFonction equals to UPDATED_CODE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("codeFonction.in=" + UPDATED_CODE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByCodeFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where codeFonction is not null
        defaultFonctionAdherentShouldBeFound("codeFonction.specified=true");

        // Get all the fonctionAdherentList where codeFonction is null
        defaultFonctionAdherentShouldNotBeFound("codeFonction.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByCodeFonctionContainsSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where codeFonction contains DEFAULT_CODE_FONCTION
        defaultFonctionAdherentShouldBeFound("codeFonction.contains=" + DEFAULT_CODE_FONCTION);

        // Get all the fonctionAdherentList where codeFonction contains UPDATED_CODE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("codeFonction.contains=" + UPDATED_CODE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByCodeFonctionNotContainsSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where codeFonction does not contain DEFAULT_CODE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("codeFonction.doesNotContain=" + DEFAULT_CODE_FONCTION);

        // Get all the fonctionAdherentList where codeFonction does not contain UPDATED_CODE_FONCTION
        defaultFonctionAdherentShouldBeFound("codeFonction.doesNotContain=" + UPDATED_CODE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction equals to DEFAULT_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.equals=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction equals to UPDATED_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.equals=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction in DEFAULT_DATE_PRISE_FONCTION or UPDATED_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.in=" + DEFAULT_DATE_PRISE_FONCTION + "," + UPDATED_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction equals to UPDATED_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.in=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction is not null
        defaultFonctionAdherentShouldBeFound("datePriseFonction.specified=true");

        // Get all the fonctionAdherentList where datePriseFonction is null
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction is greater than or equal to DEFAULT_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.greaterThanOrEqual=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction is greater than or equal to UPDATED_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.greaterThanOrEqual=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction is less than or equal to DEFAULT_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.lessThanOrEqual=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction is less than or equal to SMALLER_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.lessThanOrEqual=" + SMALLER_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsLessThanSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction is less than DEFAULT_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.lessThan=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction is less than UPDATED_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.lessThan=" + UPDATED_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDatePriseFonctionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where datePriseFonction is greater than DEFAULT_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldNotBeFound("datePriseFonction.greaterThan=" + DEFAULT_DATE_PRISE_FONCTION);

        // Get all the fonctionAdherentList where datePriseFonction is greater than SMALLER_DATE_PRISE_FONCTION
        defaultFonctionAdherentShouldBeFound("datePriseFonction.greaterThan=" + SMALLER_DATE_PRISE_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction equals to DEFAULT_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.equals=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction equals to UPDATED_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.equals=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction in DEFAULT_DATE_FIN_FONCTION or UPDATED_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.in=" + DEFAULT_DATE_FIN_FONCTION + "," + UPDATED_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction equals to UPDATED_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.in=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction is not null
        defaultFonctionAdherentShouldBeFound("dateFinFonction.specified=true");

        // Get all the fonctionAdherentList where dateFinFonction is null
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction is greater than or equal to DEFAULT_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.greaterThanOrEqual=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction is greater than or equal to UPDATED_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.greaterThanOrEqual=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction is less than or equal to DEFAULT_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.lessThanOrEqual=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction is less than or equal to SMALLER_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.lessThanOrEqual=" + SMALLER_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsLessThanSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction is less than DEFAULT_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.lessThan=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction is less than UPDATED_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.lessThan=" + UPDATED_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByDateFinFonctionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where dateFinFonction is greater than DEFAULT_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldNotBeFound("dateFinFonction.greaterThan=" + DEFAULT_DATE_FIN_FONCTION);

        // Get all the fonctionAdherentList where dateFinFonction is greater than SMALLER_DATE_FIN_FONCTION
        defaultFonctionAdherentShouldBeFound("dateFinFonction.greaterThan=" + SMALLER_DATE_FIN_FONCTION);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where actif equals to DEFAULT_ACTIF
        defaultFonctionAdherentShouldBeFound("actif.equals=" + DEFAULT_ACTIF);

        // Get all the fonctionAdherentList where actif equals to UPDATED_ACTIF
        defaultFonctionAdherentShouldNotBeFound("actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where actif in DEFAULT_ACTIF or UPDATED_ACTIF
        defaultFonctionAdherentShouldBeFound("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF);

        // Get all the fonctionAdherentList where actif equals to UPDATED_ACTIF
        defaultFonctionAdherentShouldNotBeFound("actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        // Get all the fonctionAdherentList where actif is not null
        defaultFonctionAdherentShouldBeFound("actif.specified=true");

        // Get all the fonctionAdherentList where actif is null
        defaultFonctionAdherentShouldNotBeFound("actif.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByAdherentIsEqualToSomething() throws Exception {
        Personne adherent;
        if (TestUtil.findAll(em, Personne.class).isEmpty()) {
            fonctionAdherentRepository.saveAndFlush(fonctionAdherent);
            adherent = PersonneResourceIT.createEntity(em);
        } else {
            adherent = TestUtil.findAll(em, Personne.class).get(0);
        }
        em.persist(adherent);
        em.flush();
        fonctionAdherent.setAdherent(adherent);
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);
        Long adherentId = adherent.getId();

        // Get all the fonctionAdherentList where adherent equals to adherentId
        defaultFonctionAdherentShouldBeFound("adherentId.equals=" + adherentId);

        // Get all the fonctionAdherentList where adherent equals to (adherentId + 1)
        defaultFonctionAdherentShouldNotBeFound("adherentId.equals=" + (adherentId + 1));
    }

    @Test
    @Transactional
    void getAllFonctionAdherentsByFonctionIsEqualToSomething() throws Exception {
        Fonction fonction;
        if (TestUtil.findAll(em, Fonction.class).isEmpty()) {
            fonctionAdherentRepository.saveAndFlush(fonctionAdherent);
            fonction = FonctionResourceIT.createEntity(em);
        } else {
            fonction = TestUtil.findAll(em, Fonction.class).get(0);
        }
        em.persist(fonction);
        em.flush();
        fonctionAdherent.setFonction(fonction);
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);
        Long fonctionId = fonction.getId();

        // Get all the fonctionAdherentList where fonction equals to fonctionId
        defaultFonctionAdherentShouldBeFound("fonctionId.equals=" + fonctionId);

        // Get all the fonctionAdherentList where fonction equals to (fonctionId + 1)
        defaultFonctionAdherentShouldNotBeFound("fonctionId.equals=" + (fonctionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFonctionAdherentShouldBeFound(String filter) throws Exception {
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonctionAdherent.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].codeFonction").value(hasItem(DEFAULT_CODE_FONCTION)))
            .andExpect(jsonPath("$.[*].datePriseFonction").value(hasItem(DEFAULT_DATE_PRISE_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].dateFinFonction").value(hasItem(DEFAULT_DATE_FIN_FONCTION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())));

        // Check, that the count call also returns 1
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFonctionAdherentShouldNotBeFound(String filter) throws Exception {
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFonctionAdherentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFonctionAdherent() throws Exception {
        // Get the fonctionAdherent
        restFonctionAdherentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFonctionAdherent() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();

        // Update the fonctionAdherent
        FonctionAdherent updatedFonctionAdherent = fonctionAdherentRepository.findById(fonctionAdherent.getId()).get();
        // Disconnect from session so that the updates on updatedFonctionAdherent are not directly saved in db
        em.detach(updatedFonctionAdherent);
        updatedFonctionAdherent
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeFonction(UPDATED_CODE_FONCTION)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION)
            .actif(UPDATED_ACTIF);
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(updatedFonctionAdherent);

        restFonctionAdherentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionAdherentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isOk());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
        FonctionAdherent testFonctionAdherent = fonctionAdherentList.get(fonctionAdherentList.size() - 1);
        assertThat(testFonctionAdherent.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testFonctionAdherent.getCodeFonction()).isEqualTo(UPDATED_CODE_FONCTION);
        assertThat(testFonctionAdherent.getDatePriseFonction()).isEqualTo(UPDATED_DATE_PRISE_FONCTION);
        assertThat(testFonctionAdherent.getDateFinFonction()).isEqualTo(UPDATED_DATE_FIN_FONCTION);
        assertThat(testFonctionAdherent.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void putNonExistingFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionAdherentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFonctionAdherentWithPatch() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();

        // Update the fonctionAdherent using partial update
        FonctionAdherent partialUpdatedFonctionAdherent = new FonctionAdherent();
        partialUpdatedFonctionAdherent.setId(fonctionAdherent.getId());

        restFonctionAdherentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonctionAdherent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonctionAdherent))
            )
            .andExpect(status().isOk());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
        FonctionAdherent testFonctionAdherent = fonctionAdherentList.get(fonctionAdherentList.size() - 1);
        assertThat(testFonctionAdherent.getMatriculeAdherent()).isEqualTo(DEFAULT_MATRICULE_ADHERENT);
        assertThat(testFonctionAdherent.getCodeFonction()).isEqualTo(DEFAULT_CODE_FONCTION);
        assertThat(testFonctionAdherent.getDatePriseFonction()).isEqualTo(DEFAULT_DATE_PRISE_FONCTION);
        assertThat(testFonctionAdherent.getDateFinFonction()).isEqualTo(DEFAULT_DATE_FIN_FONCTION);
        assertThat(testFonctionAdherent.getActif()).isEqualTo(DEFAULT_ACTIF);
    }

    @Test
    @Transactional
    void fullUpdateFonctionAdherentWithPatch() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();

        // Update the fonctionAdherent using partial update
        FonctionAdherent partialUpdatedFonctionAdherent = new FonctionAdherent();
        partialUpdatedFonctionAdherent.setId(fonctionAdherent.getId());

        partialUpdatedFonctionAdherent
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .codeFonction(UPDATED_CODE_FONCTION)
            .datePriseFonction(UPDATED_DATE_PRISE_FONCTION)
            .dateFinFonction(UPDATED_DATE_FIN_FONCTION)
            .actif(UPDATED_ACTIF);

        restFonctionAdherentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonctionAdherent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonctionAdherent))
            )
            .andExpect(status().isOk());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
        FonctionAdherent testFonctionAdherent = fonctionAdherentList.get(fonctionAdherentList.size() - 1);
        assertThat(testFonctionAdherent.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testFonctionAdherent.getCodeFonction()).isEqualTo(UPDATED_CODE_FONCTION);
        assertThat(testFonctionAdherent.getDatePriseFonction()).isEqualTo(UPDATED_DATE_PRISE_FONCTION);
        assertThat(testFonctionAdherent.getDateFinFonction()).isEqualTo(UPDATED_DATE_FIN_FONCTION);
        assertThat(testFonctionAdherent.getActif()).isEqualTo(UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void patchNonExistingFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fonctionAdherentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFonctionAdherent() throws Exception {
        int databaseSizeBeforeUpdate = fonctionAdherentRepository.findAll().size();
        fonctionAdherent.setId(count.incrementAndGet());

        // Create the FonctionAdherent
        FonctionAdherentDTO fonctionAdherentDTO = fonctionAdherentMapper.toDto(fonctionAdherent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionAdherentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionAdherentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FonctionAdherent in the database
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFonctionAdherent() throws Exception {
        // Initialize the database
        fonctionAdherentRepository.saveAndFlush(fonctionAdherent);

        int databaseSizeBeforeDelete = fonctionAdherentRepository.findAll().size();

        // Delete the fonctionAdherent
        restFonctionAdherentMockMvc
            .perform(delete(ENTITY_API_URL_ID, fonctionAdherent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FonctionAdherent> fonctionAdherentList = fonctionAdherentRepository.findAll();
        assertThat(fonctionAdherentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
