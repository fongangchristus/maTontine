package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.ContributionPot;
import com.it4innov.domain.Pot;
import com.it4innov.repository.ContributionPotRepository;
import com.it4innov.service.criteria.ContributionPotCriteria;
import com.it4innov.service.dto.ContributionPotDTO;
import com.it4innov.service.mapper.ContributionPotMapper;
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
 * Integration tests for the {@link ContributionPotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContributionPotResourceIT {

    private static final String DEFAULT_IDENTIFIANT = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_CONTRIBUTEUR = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_CONTRIBUTEUR = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_CONTRIBUTION = 1D;
    private static final Double UPDATED_MONTANT_CONTRIBUTION = 2D;
    private static final Double SMALLER_MONTANT_CONTRIBUTION = 1D - 1D;

    private static final Instant DEFAULT_DATE_CONTRIBUTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CONTRIBUTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contribution-pots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContributionPotRepository contributionPotRepository;

    @Autowired
    private ContributionPotMapper contributionPotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContributionPotMockMvc;

    private ContributionPot contributionPot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContributionPot createEntity(EntityManager em) {
        ContributionPot contributionPot = new ContributionPot()
            .identifiant(DEFAULT_IDENTIFIANT)
            .matriculeContributeur(DEFAULT_MATRICULE_CONTRIBUTEUR)
            .montantContribution(DEFAULT_MONTANT_CONTRIBUTION)
            .dateContribution(DEFAULT_DATE_CONTRIBUTION);
        return contributionPot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContributionPot createUpdatedEntity(EntityManager em) {
        ContributionPot contributionPot = new ContributionPot()
            .identifiant(UPDATED_IDENTIFIANT)
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .montantContribution(UPDATED_MONTANT_CONTRIBUTION)
            .dateContribution(UPDATED_DATE_CONTRIBUTION);
        return contributionPot;
    }

    @BeforeEach
    public void initTest() {
        contributionPot = createEntity(em);
    }

    @Test
    @Transactional
    void createContributionPot() throws Exception {
        int databaseSizeBeforeCreate = contributionPotRepository.findAll().size();
        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);
        restContributionPotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeCreate + 1);
        ContributionPot testContributionPot = contributionPotList.get(contributionPotList.size() - 1);
        assertThat(testContributionPot.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
        assertThat(testContributionPot.getMatriculeContributeur()).isEqualTo(DEFAULT_MATRICULE_CONTRIBUTEUR);
        assertThat(testContributionPot.getMontantContribution()).isEqualTo(DEFAULT_MONTANT_CONTRIBUTION);
        assertThat(testContributionPot.getDateContribution()).isEqualTo(DEFAULT_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void createContributionPotWithExistingId() throws Exception {
        // Create the ContributionPot with an existing ID
        contributionPot.setId(1L);
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        int databaseSizeBeforeCreate = contributionPotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionPotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeContributeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = contributionPotRepository.findAll().size();
        // set the field null
        contributionPot.setMatriculeContributeur(null);

        // Create the ContributionPot, which fails.
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        restContributionPotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContributionPots() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionPot.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT)))
            .andExpect(jsonPath("$.[*].matriculeContributeur").value(hasItem(DEFAULT_MATRICULE_CONTRIBUTEUR)))
            .andExpect(jsonPath("$.[*].montantContribution").value(hasItem(DEFAULT_MONTANT_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].dateContribution").value(hasItem(DEFAULT_DATE_CONTRIBUTION.toString())));
    }

    @Test
    @Transactional
    void getContributionPot() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get the contributionPot
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL_ID, contributionPot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contributionPot.getId().intValue()))
            .andExpect(jsonPath("$.identifiant").value(DEFAULT_IDENTIFIANT))
            .andExpect(jsonPath("$.matriculeContributeur").value(DEFAULT_MATRICULE_CONTRIBUTEUR))
            .andExpect(jsonPath("$.montantContribution").value(DEFAULT_MONTANT_CONTRIBUTION.doubleValue()))
            .andExpect(jsonPath("$.dateContribution").value(DEFAULT_DATE_CONTRIBUTION.toString()));
    }

    @Test
    @Transactional
    void getContributionPotsByIdFiltering() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        Long id = contributionPot.getId();

        defaultContributionPotShouldBeFound("id.equals=" + id);
        defaultContributionPotShouldNotBeFound("id.notEquals=" + id);

        defaultContributionPotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultContributionPotShouldNotBeFound("id.greaterThan=" + id);

        defaultContributionPotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultContributionPotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContributionPotsByIdentifiantIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where identifiant equals to DEFAULT_IDENTIFIANT
        defaultContributionPotShouldBeFound("identifiant.equals=" + DEFAULT_IDENTIFIANT);

        // Get all the contributionPotList where identifiant equals to UPDATED_IDENTIFIANT
        defaultContributionPotShouldNotBeFound("identifiant.equals=" + UPDATED_IDENTIFIANT);
    }

    @Test
    @Transactional
    void getAllContributionPotsByIdentifiantIsInShouldWork() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where identifiant in DEFAULT_IDENTIFIANT or UPDATED_IDENTIFIANT
        defaultContributionPotShouldBeFound("identifiant.in=" + DEFAULT_IDENTIFIANT + "," + UPDATED_IDENTIFIANT);

        // Get all the contributionPotList where identifiant equals to UPDATED_IDENTIFIANT
        defaultContributionPotShouldNotBeFound("identifiant.in=" + UPDATED_IDENTIFIANT);
    }

    @Test
    @Transactional
    void getAllContributionPotsByIdentifiantIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where identifiant is not null
        defaultContributionPotShouldBeFound("identifiant.specified=true");

        // Get all the contributionPotList where identifiant is null
        defaultContributionPotShouldNotBeFound("identifiant.specified=false");
    }

    @Test
    @Transactional
    void getAllContributionPotsByIdentifiantContainsSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where identifiant contains DEFAULT_IDENTIFIANT
        defaultContributionPotShouldBeFound("identifiant.contains=" + DEFAULT_IDENTIFIANT);

        // Get all the contributionPotList where identifiant contains UPDATED_IDENTIFIANT
        defaultContributionPotShouldNotBeFound("identifiant.contains=" + UPDATED_IDENTIFIANT);
    }

    @Test
    @Transactional
    void getAllContributionPotsByIdentifiantNotContainsSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where identifiant does not contain DEFAULT_IDENTIFIANT
        defaultContributionPotShouldNotBeFound("identifiant.doesNotContain=" + DEFAULT_IDENTIFIANT);

        // Get all the contributionPotList where identifiant does not contain UPDATED_IDENTIFIANT
        defaultContributionPotShouldBeFound("identifiant.doesNotContain=" + UPDATED_IDENTIFIANT);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMatriculeContributeurIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where matriculeContributeur equals to DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldBeFound("matriculeContributeur.equals=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the contributionPotList where matriculeContributeur equals to UPDATED_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldNotBeFound("matriculeContributeur.equals=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMatriculeContributeurIsInShouldWork() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where matriculeContributeur in DEFAULT_MATRICULE_CONTRIBUTEUR or UPDATED_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldBeFound(
            "matriculeContributeur.in=" + DEFAULT_MATRICULE_CONTRIBUTEUR + "," + UPDATED_MATRICULE_CONTRIBUTEUR
        );

        // Get all the contributionPotList where matriculeContributeur equals to UPDATED_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldNotBeFound("matriculeContributeur.in=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMatriculeContributeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where matriculeContributeur is not null
        defaultContributionPotShouldBeFound("matriculeContributeur.specified=true");

        // Get all the contributionPotList where matriculeContributeur is null
        defaultContributionPotShouldNotBeFound("matriculeContributeur.specified=false");
    }

    @Test
    @Transactional
    void getAllContributionPotsByMatriculeContributeurContainsSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where matriculeContributeur contains DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldBeFound("matriculeContributeur.contains=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the contributionPotList where matriculeContributeur contains UPDATED_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldNotBeFound("matriculeContributeur.contains=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMatriculeContributeurNotContainsSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where matriculeContributeur does not contain DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldNotBeFound("matriculeContributeur.doesNotContain=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the contributionPotList where matriculeContributeur does not contain UPDATED_MATRICULE_CONTRIBUTEUR
        defaultContributionPotShouldBeFound("matriculeContributeur.doesNotContain=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution equals to DEFAULT_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.equals=" + DEFAULT_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution equals to UPDATED_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.equals=" + UPDATED_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsInShouldWork() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution in DEFAULT_MONTANT_CONTRIBUTION or UPDATED_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.in=" + DEFAULT_MONTANT_CONTRIBUTION + "," + UPDATED_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution equals to UPDATED_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.in=" + UPDATED_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution is not null
        defaultContributionPotShouldBeFound("montantContribution.specified=true");

        // Get all the contributionPotList where montantContribution is null
        defaultContributionPotShouldNotBeFound("montantContribution.specified=false");
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution is greater than or equal to DEFAULT_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.greaterThanOrEqual=" + DEFAULT_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution is greater than or equal to UPDATED_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.greaterThanOrEqual=" + UPDATED_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution is less than or equal to DEFAULT_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.lessThanOrEqual=" + DEFAULT_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution is less than or equal to SMALLER_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.lessThanOrEqual=" + SMALLER_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsLessThanSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution is less than DEFAULT_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.lessThan=" + DEFAULT_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution is less than UPDATED_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.lessThan=" + UPDATED_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByMontantContributionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where montantContribution is greater than DEFAULT_MONTANT_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("montantContribution.greaterThan=" + DEFAULT_MONTANT_CONTRIBUTION);

        // Get all the contributionPotList where montantContribution is greater than SMALLER_MONTANT_CONTRIBUTION
        defaultContributionPotShouldBeFound("montantContribution.greaterThan=" + SMALLER_MONTANT_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByDateContributionIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where dateContribution equals to DEFAULT_DATE_CONTRIBUTION
        defaultContributionPotShouldBeFound("dateContribution.equals=" + DEFAULT_DATE_CONTRIBUTION);

        // Get all the contributionPotList where dateContribution equals to UPDATED_DATE_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("dateContribution.equals=" + UPDATED_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByDateContributionIsInShouldWork() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where dateContribution in DEFAULT_DATE_CONTRIBUTION or UPDATED_DATE_CONTRIBUTION
        defaultContributionPotShouldBeFound("dateContribution.in=" + DEFAULT_DATE_CONTRIBUTION + "," + UPDATED_DATE_CONTRIBUTION);

        // Get all the contributionPotList where dateContribution equals to UPDATED_DATE_CONTRIBUTION
        defaultContributionPotShouldNotBeFound("dateContribution.in=" + UPDATED_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void getAllContributionPotsByDateContributionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        // Get all the contributionPotList where dateContribution is not null
        defaultContributionPotShouldBeFound("dateContribution.specified=true");

        // Get all the contributionPotList where dateContribution is null
        defaultContributionPotShouldNotBeFound("dateContribution.specified=false");
    }

    @Test
    @Transactional
    void getAllContributionPotsByPotIsEqualToSomething() throws Exception {
        Pot pot;
        if (TestUtil.findAll(em, Pot.class).isEmpty()) {
            contributionPotRepository.saveAndFlush(contributionPot);
            pot = PotResourceIT.createEntity(em);
        } else {
            pot = TestUtil.findAll(em, Pot.class).get(0);
        }
        em.persist(pot);
        em.flush();
        contributionPot.setPot(pot);
        contributionPotRepository.saveAndFlush(contributionPot);
        Long potId = pot.getId();

        // Get all the contributionPotList where pot equals to potId
        defaultContributionPotShouldBeFound("potId.equals=" + potId);

        // Get all the contributionPotList where pot equals to (potId + 1)
        defaultContributionPotShouldNotBeFound("potId.equals=" + (potId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContributionPotShouldBeFound(String filter) throws Exception {
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionPot.getId().intValue())))
            .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT)))
            .andExpect(jsonPath("$.[*].matriculeContributeur").value(hasItem(DEFAULT_MATRICULE_CONTRIBUTEUR)))
            .andExpect(jsonPath("$.[*].montantContribution").value(hasItem(DEFAULT_MONTANT_CONTRIBUTION.doubleValue())))
            .andExpect(jsonPath("$.[*].dateContribution").value(hasItem(DEFAULT_DATE_CONTRIBUTION.toString())));

        // Check, that the count call also returns 1
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContributionPotShouldNotBeFound(String filter) throws Exception {
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContributionPotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContributionPot() throws Exception {
        // Get the contributionPot
        restContributionPotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContributionPot() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();

        // Update the contributionPot
        ContributionPot updatedContributionPot = contributionPotRepository.findById(contributionPot.getId()).get();
        // Disconnect from session so that the updates on updatedContributionPot are not directly saved in db
        em.detach(updatedContributionPot);
        updatedContributionPot
            .identifiant(UPDATED_IDENTIFIANT)
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .montantContribution(UPDATED_MONTANT_CONTRIBUTION)
            .dateContribution(UPDATED_DATE_CONTRIBUTION);
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(updatedContributionPot);

        restContributionPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contributionPotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
        ContributionPot testContributionPot = contributionPotList.get(contributionPotList.size() - 1);
        assertThat(testContributionPot.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testContributionPot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testContributionPot.getMontantContribution()).isEqualTo(UPDATED_MONTANT_CONTRIBUTION);
        assertThat(testContributionPot.getDateContribution()).isEqualTo(UPDATED_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void putNonExistingContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contributionPotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContributionPotWithPatch() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();

        // Update the contributionPot using partial update
        ContributionPot partialUpdatedContributionPot = new ContributionPot();
        partialUpdatedContributionPot.setId(contributionPot.getId());

        partialUpdatedContributionPot
            .identifiant(UPDATED_IDENTIFIANT)
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .montantContribution(UPDATED_MONTANT_CONTRIBUTION);

        restContributionPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContributionPot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContributionPot))
            )
            .andExpect(status().isOk());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
        ContributionPot testContributionPot = contributionPotList.get(contributionPotList.size() - 1);
        assertThat(testContributionPot.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testContributionPot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testContributionPot.getMontantContribution()).isEqualTo(UPDATED_MONTANT_CONTRIBUTION);
        assertThat(testContributionPot.getDateContribution()).isEqualTo(DEFAULT_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void fullUpdateContributionPotWithPatch() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();

        // Update the contributionPot using partial update
        ContributionPot partialUpdatedContributionPot = new ContributionPot();
        partialUpdatedContributionPot.setId(contributionPot.getId());

        partialUpdatedContributionPot
            .identifiant(UPDATED_IDENTIFIANT)
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .montantContribution(UPDATED_MONTANT_CONTRIBUTION)
            .dateContribution(UPDATED_DATE_CONTRIBUTION);

        restContributionPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContributionPot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContributionPot))
            )
            .andExpect(status().isOk());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
        ContributionPot testContributionPot = contributionPotList.get(contributionPotList.size() - 1);
        assertThat(testContributionPot.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
        assertThat(testContributionPot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testContributionPot.getMontantContribution()).isEqualTo(UPDATED_MONTANT_CONTRIBUTION);
        assertThat(testContributionPot.getDateContribution()).isEqualTo(UPDATED_DATE_CONTRIBUTION);
    }

    @Test
    @Transactional
    void patchNonExistingContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contributionPotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContributionPot() throws Exception {
        int databaseSizeBeforeUpdate = contributionPotRepository.findAll().size();
        contributionPot.setId(count.incrementAndGet());

        // Create the ContributionPot
        ContributionPotDTO contributionPotDTO = contributionPotMapper.toDto(contributionPot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContributionPotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contributionPotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContributionPot in the database
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContributionPot() throws Exception {
        // Initialize the database
        contributionPotRepository.saveAndFlush(contributionPot);

        int databaseSizeBeforeDelete = contributionPotRepository.findAll().size();

        // Delete the contributionPot
        restContributionPotMockMvc
            .perform(delete(ENTITY_API_URL_ID, contributionPot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContributionPot> contributionPotList = contributionPotRepository.findAll();
        assertThat(contributionPotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
