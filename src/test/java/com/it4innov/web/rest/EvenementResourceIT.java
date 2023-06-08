package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Evenement;
import com.it4innov.domain.TypeEvenement;
import com.it4innov.repository.EvenementRepository;
import com.it4innov.service.criteria.EvenementCriteria;
import com.it4innov.service.dto.EvenementDTO;
import com.it4innov.service.mapper.EvenementMapper;
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
 * Integration tests for the {@link EvenementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvenementResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_CODEPOT = "AAAAAAAAAA";
    private static final String UPDATED_CODEPOT = "BBBBBBBBBB";

    private static final String DEFAULT_MONTANT_PAYER = "AAAAAAAAAA";
    private static final String UPDATED_MONTANT_PAYER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_BUDGET = 1D;
    private static final Double UPDATED_BUDGET = 2D;
    private static final Double SMALLER_BUDGET = 1D - 1D;

    private static final Instant DEFAULT_DATE_EVENEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_EVENEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/evenements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvenementRepository evenementRepository;

    @Autowired
    private EvenementMapper evenementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvenementMockMvc;

    private Evenement evenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .libele(DEFAULT_LIBELE)
            .codepot(DEFAULT_CODEPOT)
            .montantPayer(DEFAULT_MONTANT_PAYER)
            .description(DEFAULT_DESCRIPTION)
            .budget(DEFAULT_BUDGET)
            .dateEvenement(DEFAULT_DATE_EVENEMENT);
        return evenement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createUpdatedEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantPayer(UPDATED_MONTANT_PAYER)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .dateEvenement(UPDATED_DATE_EVENEMENT);
        return evenement;
    }

    @BeforeEach
    public void initTest() {
        evenement = createEntity(em);
    }

    @Test
    @Transactional
    void createEvenement() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();
        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);
        restEvenementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate + 1);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testEvenement.getCodepot()).isEqualTo(DEFAULT_CODEPOT);
        assertThat(testEvenement.getMontantPayer()).isEqualTo(DEFAULT_MONTANT_PAYER);
        assertThat(testEvenement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEvenement.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testEvenement.getDateEvenement()).isEqualTo(DEFAULT_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void createEvenementWithExistingId() throws Exception {
        // Create the Evenement with an existing ID
        evenement.setId(1L);
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvenementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEvenements() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].codepot").value(hasItem(DEFAULT_CODEPOT)))
            .andExpect(jsonPath("$.[*].montantPayer").value(hasItem(DEFAULT_MONTANT_PAYER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEvenement").value(hasItem(DEFAULT_DATE_EVENEMENT.toString())));
    }

    @Test
    @Transactional
    void getEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get the evenement
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL_ID, evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evenement.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.codepot").value(DEFAULT_CODEPOT))
            .andExpect(jsonPath("$.montantPayer").value(DEFAULT_MONTANT_PAYER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.doubleValue()))
            .andExpect(jsonPath("$.dateEvenement").value(DEFAULT_DATE_EVENEMENT.toString()));
    }

    @Test
    @Transactional
    void getEvenementsByIdFiltering() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        Long id = evenement.getId();

        defaultEvenementShouldBeFound("id.equals=" + id);
        defaultEvenementShouldNotBeFound("id.notEquals=" + id);

        defaultEvenementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEvenementShouldNotBeFound("id.greaterThan=" + id);

        defaultEvenementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEvenementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEvenementsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where libele equals to DEFAULT_LIBELE
        defaultEvenementShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the evenementList where libele equals to UPDATED_LIBELE
        defaultEvenementShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllEvenementsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultEvenementShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the evenementList where libele equals to UPDATED_LIBELE
        defaultEvenementShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllEvenementsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where libele is not null
        defaultEvenementShouldBeFound("libele.specified=true");

        // Get all the evenementList where libele is null
        defaultEvenementShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where libele contains DEFAULT_LIBELE
        defaultEvenementShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the evenementList where libele contains UPDATED_LIBELE
        defaultEvenementShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllEvenementsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where libele does not contain DEFAULT_LIBELE
        defaultEvenementShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the evenementList where libele does not contain UPDATED_LIBELE
        defaultEvenementShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllEvenementsByCodepotIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where codepot equals to DEFAULT_CODEPOT
        defaultEvenementShouldBeFound("codepot.equals=" + DEFAULT_CODEPOT);

        // Get all the evenementList where codepot equals to UPDATED_CODEPOT
        defaultEvenementShouldNotBeFound("codepot.equals=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllEvenementsByCodepotIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where codepot in DEFAULT_CODEPOT or UPDATED_CODEPOT
        defaultEvenementShouldBeFound("codepot.in=" + DEFAULT_CODEPOT + "," + UPDATED_CODEPOT);

        // Get all the evenementList where codepot equals to UPDATED_CODEPOT
        defaultEvenementShouldNotBeFound("codepot.in=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllEvenementsByCodepotIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where codepot is not null
        defaultEvenementShouldBeFound("codepot.specified=true");

        // Get all the evenementList where codepot is null
        defaultEvenementShouldNotBeFound("codepot.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByCodepotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where codepot contains DEFAULT_CODEPOT
        defaultEvenementShouldBeFound("codepot.contains=" + DEFAULT_CODEPOT);

        // Get all the evenementList where codepot contains UPDATED_CODEPOT
        defaultEvenementShouldNotBeFound("codepot.contains=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllEvenementsByCodepotNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where codepot does not contain DEFAULT_CODEPOT
        defaultEvenementShouldNotBeFound("codepot.doesNotContain=" + DEFAULT_CODEPOT);

        // Get all the evenementList where codepot does not contain UPDATED_CODEPOT
        defaultEvenementShouldBeFound("codepot.doesNotContain=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllEvenementsByMontantPayerIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where montantPayer equals to DEFAULT_MONTANT_PAYER
        defaultEvenementShouldBeFound("montantPayer.equals=" + DEFAULT_MONTANT_PAYER);

        // Get all the evenementList where montantPayer equals to UPDATED_MONTANT_PAYER
        defaultEvenementShouldNotBeFound("montantPayer.equals=" + UPDATED_MONTANT_PAYER);
    }

    @Test
    @Transactional
    void getAllEvenementsByMontantPayerIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where montantPayer in DEFAULT_MONTANT_PAYER or UPDATED_MONTANT_PAYER
        defaultEvenementShouldBeFound("montantPayer.in=" + DEFAULT_MONTANT_PAYER + "," + UPDATED_MONTANT_PAYER);

        // Get all the evenementList where montantPayer equals to UPDATED_MONTANT_PAYER
        defaultEvenementShouldNotBeFound("montantPayer.in=" + UPDATED_MONTANT_PAYER);
    }

    @Test
    @Transactional
    void getAllEvenementsByMontantPayerIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where montantPayer is not null
        defaultEvenementShouldBeFound("montantPayer.specified=true");

        // Get all the evenementList where montantPayer is null
        defaultEvenementShouldNotBeFound("montantPayer.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByMontantPayerContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where montantPayer contains DEFAULT_MONTANT_PAYER
        defaultEvenementShouldBeFound("montantPayer.contains=" + DEFAULT_MONTANT_PAYER);

        // Get all the evenementList where montantPayer contains UPDATED_MONTANT_PAYER
        defaultEvenementShouldNotBeFound("montantPayer.contains=" + UPDATED_MONTANT_PAYER);
    }

    @Test
    @Transactional
    void getAllEvenementsByMontantPayerNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where montantPayer does not contain DEFAULT_MONTANT_PAYER
        defaultEvenementShouldNotBeFound("montantPayer.doesNotContain=" + DEFAULT_MONTANT_PAYER);

        // Get all the evenementList where montantPayer does not contain UPDATED_MONTANT_PAYER
        defaultEvenementShouldBeFound("montantPayer.doesNotContain=" + UPDATED_MONTANT_PAYER);
    }

    @Test
    @Transactional
    void getAllEvenementsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description equals to DEFAULT_DESCRIPTION
        defaultEvenementShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description equals to UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEvenementsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultEvenementShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the evenementList where description equals to UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEvenementsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description is not null
        defaultEvenementShouldBeFound("description.specified=true");

        // Get all the evenementList where description is null
        defaultEvenementShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description contains DEFAULT_DESCRIPTION
        defaultEvenementShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description contains UPDATED_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEvenementsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where description does not contain DEFAULT_DESCRIPTION
        defaultEvenementShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the evenementList where description does not contain UPDATED_DESCRIPTION
        defaultEvenementShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget equals to DEFAULT_BUDGET
        defaultEvenementShouldBeFound("budget.equals=" + DEFAULT_BUDGET);

        // Get all the evenementList where budget equals to UPDATED_BUDGET
        defaultEvenementShouldNotBeFound("budget.equals=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget in DEFAULT_BUDGET or UPDATED_BUDGET
        defaultEvenementShouldBeFound("budget.in=" + DEFAULT_BUDGET + "," + UPDATED_BUDGET);

        // Get all the evenementList where budget equals to UPDATED_BUDGET
        defaultEvenementShouldNotBeFound("budget.in=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget is not null
        defaultEvenementShouldBeFound("budget.specified=true");

        // Get all the evenementList where budget is null
        defaultEvenementShouldNotBeFound("budget.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget is greater than or equal to DEFAULT_BUDGET
        defaultEvenementShouldBeFound("budget.greaterThanOrEqual=" + DEFAULT_BUDGET);

        // Get all the evenementList where budget is greater than or equal to UPDATED_BUDGET
        defaultEvenementShouldNotBeFound("budget.greaterThanOrEqual=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget is less than or equal to DEFAULT_BUDGET
        defaultEvenementShouldBeFound("budget.lessThanOrEqual=" + DEFAULT_BUDGET);

        // Get all the evenementList where budget is less than or equal to SMALLER_BUDGET
        defaultEvenementShouldNotBeFound("budget.lessThanOrEqual=" + SMALLER_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsLessThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget is less than DEFAULT_BUDGET
        defaultEvenementShouldNotBeFound("budget.lessThan=" + DEFAULT_BUDGET);

        // Get all the evenementList where budget is less than UPDATED_BUDGET
        defaultEvenementShouldBeFound("budget.lessThan=" + UPDATED_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByBudgetIsGreaterThanSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where budget is greater than DEFAULT_BUDGET
        defaultEvenementShouldNotBeFound("budget.greaterThan=" + DEFAULT_BUDGET);

        // Get all the evenementList where budget is greater than SMALLER_BUDGET
        defaultEvenementShouldBeFound("budget.greaterThan=" + SMALLER_BUDGET);
    }

    @Test
    @Transactional
    void getAllEvenementsByDateEvenementIsEqualToSomething() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateEvenement equals to DEFAULT_DATE_EVENEMENT
        defaultEvenementShouldBeFound("dateEvenement.equals=" + DEFAULT_DATE_EVENEMENT);

        // Get all the evenementList where dateEvenement equals to UPDATED_DATE_EVENEMENT
        defaultEvenementShouldNotBeFound("dateEvenement.equals=" + UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllEvenementsByDateEvenementIsInShouldWork() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateEvenement in DEFAULT_DATE_EVENEMENT or UPDATED_DATE_EVENEMENT
        defaultEvenementShouldBeFound("dateEvenement.in=" + DEFAULT_DATE_EVENEMENT + "," + UPDATED_DATE_EVENEMENT);

        // Get all the evenementList where dateEvenement equals to UPDATED_DATE_EVENEMENT
        defaultEvenementShouldNotBeFound("dateEvenement.in=" + UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void getAllEvenementsByDateEvenementIsNullOrNotNull() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList where dateEvenement is not null
        defaultEvenementShouldBeFound("dateEvenement.specified=true");

        // Get all the evenementList where dateEvenement is null
        defaultEvenementShouldNotBeFound("dateEvenement.specified=false");
    }

    @Test
    @Transactional
    void getAllEvenementsByTypeEvenementIsEqualToSomething() throws Exception {
        TypeEvenement typeEvenement;
        if (TestUtil.findAll(em, TypeEvenement.class).isEmpty()) {
            evenementRepository.saveAndFlush(evenement);
            typeEvenement = TypeEvenementResourceIT.createEntity(em);
        } else {
            typeEvenement = TestUtil.findAll(em, TypeEvenement.class).get(0);
        }
        em.persist(typeEvenement);
        em.flush();
        evenement.setTypeEvenement(typeEvenement);
        evenementRepository.saveAndFlush(evenement);
        Long typeEvenementId = typeEvenement.getId();

        // Get all the evenementList where typeEvenement equals to typeEvenementId
        defaultEvenementShouldBeFound("typeEvenementId.equals=" + typeEvenementId);

        // Get all the evenementList where typeEvenement equals to (typeEvenementId + 1)
        defaultEvenementShouldNotBeFound("typeEvenementId.equals=" + (typeEvenementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEvenementShouldBeFound(String filter) throws Exception {
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].codepot").value(hasItem(DEFAULT_CODEPOT)))
            .andExpect(jsonPath("$.[*].montantPayer").value(hasItem(DEFAULT_MONTANT_PAYER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.doubleValue())))
            .andExpect(jsonPath("$.[*].dateEvenement").value(hasItem(DEFAULT_DATE_EVENEMENT.toString())));

        // Check, that the count call also returns 1
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEvenementShouldNotBeFound(String filter) throws Exception {
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvenement() throws Exception {
        // Get the evenement
        restEvenementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement
        Evenement updatedEvenement = evenementRepository.findById(evenement.getId()).get();
        // Disconnect from session so that the updates on updatedEvenement are not directly saved in db
        em.detach(updatedEvenement);
        updatedEvenement
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantPayer(UPDATED_MONTANT_PAYER)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .dateEvenement(UPDATED_DATE_EVENEMENT);
        EvenementDTO evenementDTO = evenementMapper.toDto(updatedEvenement);

        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evenementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testEvenement.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testEvenement.getMontantPayer()).isEqualTo(UPDATED_MONTANT_PAYER);
        assertThat(testEvenement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvenement.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testEvenement.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void putNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evenementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvenementWithPatch() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement using partial update
        Evenement partialUpdatedEvenement = new Evenement();
        partialUpdatedEvenement.setId(evenement.getId());

        partialUpdatedEvenement
            .codepot(UPDATED_CODEPOT)
            .montantPayer(UPDATED_MONTANT_PAYER)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .dateEvenement(UPDATED_DATE_EVENEMENT);

        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvenement))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testEvenement.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testEvenement.getMontantPayer()).isEqualTo(UPDATED_MONTANT_PAYER);
        assertThat(testEvenement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvenement.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testEvenement.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void fullUpdateEvenementWithPatch() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement using partial update
        Evenement partialUpdatedEvenement = new Evenement();
        partialUpdatedEvenement.setId(evenement.getId());

        partialUpdatedEvenement
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantPayer(UPDATED_MONTANT_PAYER)
            .description(UPDATED_DESCRIPTION)
            .budget(UPDATED_BUDGET)
            .dateEvenement(UPDATED_DATE_EVENEMENT);

        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvenement))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testEvenement.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testEvenement.getMontantPayer()).isEqualTo(UPDATED_MONTANT_PAYER);
        assertThat(testEvenement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEvenement.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testEvenement.getDateEvenement()).isEqualTo(UPDATED_DATE_EVENEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evenementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // Create the Evenement
        EvenementDTO evenementDTO = evenementMapper.toDto(evenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evenementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeDelete = evenementRepository.findAll().size();

        // Delete the evenement
        restEvenementMockMvc
            .perform(delete(ENTITY_API_URL_ID, evenement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
