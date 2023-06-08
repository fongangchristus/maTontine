package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Sanction;
import com.it4innov.domain.SanctionConfiguration;
import com.it4innov.domain.enumeration.TypeSanction;
import com.it4innov.repository.SanctionConfigurationRepository;
import com.it4innov.service.criteria.SanctionConfigurationCriteria;
import com.it4innov.service.dto.SanctionConfigurationDTO;
import com.it4innov.service.mapper.SanctionConfigurationMapper;
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
 * Integration tests for the {@link SanctionConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanctionConfigurationResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TONTINE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TONTINE = "BBBBBBBBBB";

    private static final TypeSanction DEFAULT_TYPE = TypeSanction.RETARD_PRESENCE;
    private static final TypeSanction UPDATED_TYPE = TypeSanction.ECHEC_TONTINE;

    private static final String ENTITY_API_URL = "/api/sanction-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SanctionConfigurationRepository sanctionConfigurationRepository;

    @Autowired
    private SanctionConfigurationMapper sanctionConfigurationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanctionConfigurationMockMvc;

    private SanctionConfiguration sanctionConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SanctionConfiguration createEntity(EntityManager em) {
        SanctionConfiguration sanctionConfiguration = new SanctionConfiguration()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .codeTontine(DEFAULT_CODE_TONTINE)
            .type(DEFAULT_TYPE);
        return sanctionConfiguration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SanctionConfiguration createUpdatedEntity(EntityManager em) {
        SanctionConfiguration sanctionConfiguration = new SanctionConfiguration()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .codeTontine(UPDATED_CODE_TONTINE)
            .type(UPDATED_TYPE);
        return sanctionConfiguration;
    }

    @BeforeEach
    public void initTest() {
        sanctionConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    void createSanctionConfiguration() throws Exception {
        int databaseSizeBeforeCreate = sanctionConfigurationRepository.findAll().size();
        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);
        restSanctionConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        SanctionConfiguration testSanctionConfiguration = sanctionConfigurationList.get(sanctionConfigurationList.size() - 1);
        assertThat(testSanctionConfiguration.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testSanctionConfiguration.getCodeTontine()).isEqualTo(DEFAULT_CODE_TONTINE);
        assertThat(testSanctionConfiguration.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createSanctionConfigurationWithExistingId() throws Exception {
        // Create the SanctionConfiguration with an existing ID
        sanctionConfiguration.setId(1L);
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        int databaseSizeBeforeCreate = sanctionConfigurationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanctionConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = sanctionConfigurationRepository.findAll().size();
        // set the field null
        sanctionConfiguration.setCodeAssociation(null);

        // Create the SanctionConfiguration, which fails.
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        restSanctionConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeTontineIsRequired() throws Exception {
        int databaseSizeBeforeTest = sanctionConfigurationRepository.findAll().size();
        // set the field null
        sanctionConfiguration.setCodeTontine(null);

        // Create the SanctionConfiguration, which fails.
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        restSanctionConfigurationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurations() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].codeTontine").value(hasItem(DEFAULT_CODE_TONTINE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getSanctionConfiguration() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get the sanctionConfiguration
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, sanctionConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanctionConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.codeTontine").value(DEFAULT_CODE_TONTINE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getSanctionConfigurationsByIdFiltering() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        Long id = sanctionConfiguration.getId();

        defaultSanctionConfigurationShouldBeFound("id.equals=" + id);
        defaultSanctionConfigurationShouldNotBeFound("id.notEquals=" + id);

        defaultSanctionConfigurationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSanctionConfigurationShouldNotBeFound("id.greaterThan=" + id);

        defaultSanctionConfigurationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSanctionConfigurationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the sanctionConfigurationList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the sanctionConfigurationList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeAssociation is not null
        defaultSanctionConfigurationShouldBeFound("codeAssociation.specified=true");

        // Get all the sanctionConfigurationList where codeAssociation is null
        defaultSanctionConfigurationShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the sanctionConfigurationList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the sanctionConfigurationList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultSanctionConfigurationShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeTontineIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeTontine equals to DEFAULT_CODE_TONTINE
        defaultSanctionConfigurationShouldBeFound("codeTontine.equals=" + DEFAULT_CODE_TONTINE);

        // Get all the sanctionConfigurationList where codeTontine equals to UPDATED_CODE_TONTINE
        defaultSanctionConfigurationShouldNotBeFound("codeTontine.equals=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeTontineIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeTontine in DEFAULT_CODE_TONTINE or UPDATED_CODE_TONTINE
        defaultSanctionConfigurationShouldBeFound("codeTontine.in=" + DEFAULT_CODE_TONTINE + "," + UPDATED_CODE_TONTINE);

        // Get all the sanctionConfigurationList where codeTontine equals to UPDATED_CODE_TONTINE
        defaultSanctionConfigurationShouldNotBeFound("codeTontine.in=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeTontineIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeTontine is not null
        defaultSanctionConfigurationShouldBeFound("codeTontine.specified=true");

        // Get all the sanctionConfigurationList where codeTontine is null
        defaultSanctionConfigurationShouldNotBeFound("codeTontine.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeTontineContainsSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeTontine contains DEFAULT_CODE_TONTINE
        defaultSanctionConfigurationShouldBeFound("codeTontine.contains=" + DEFAULT_CODE_TONTINE);

        // Get all the sanctionConfigurationList where codeTontine contains UPDATED_CODE_TONTINE
        defaultSanctionConfigurationShouldNotBeFound("codeTontine.contains=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByCodeTontineNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where codeTontine does not contain DEFAULT_CODE_TONTINE
        defaultSanctionConfigurationShouldNotBeFound("codeTontine.doesNotContain=" + DEFAULT_CODE_TONTINE);

        // Get all the sanctionConfigurationList where codeTontine does not contain UPDATED_CODE_TONTINE
        defaultSanctionConfigurationShouldBeFound("codeTontine.doesNotContain=" + UPDATED_CODE_TONTINE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where type equals to DEFAULT_TYPE
        defaultSanctionConfigurationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the sanctionConfigurationList where type equals to UPDATED_TYPE
        defaultSanctionConfigurationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSanctionConfigurationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the sanctionConfigurationList where type equals to UPDATED_TYPE
        defaultSanctionConfigurationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        // Get all the sanctionConfigurationList where type is not null
        defaultSanctionConfigurationShouldBeFound("type.specified=true");

        // Get all the sanctionConfigurationList where type is null
        defaultSanctionConfigurationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionConfigurationsBySanctionIsEqualToSomething() throws Exception {
        Sanction sanction;
        if (TestUtil.findAll(em, Sanction.class).isEmpty()) {
            sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);
            sanction = SanctionResourceIT.createEntity(em);
        } else {
            sanction = TestUtil.findAll(em, Sanction.class).get(0);
        }
        em.persist(sanction);
        em.flush();
        sanctionConfiguration.addSanction(sanction);
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);
        Long sanctionId = sanction.getId();

        // Get all the sanctionConfigurationList where sanction equals to sanctionId
        defaultSanctionConfigurationShouldBeFound("sanctionId.equals=" + sanctionId);

        // Get all the sanctionConfigurationList where sanction equals to (sanctionId + 1)
        defaultSanctionConfigurationShouldNotBeFound("sanctionId.equals=" + (sanctionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSanctionConfigurationShouldBeFound(String filter) throws Exception {
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanctionConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].codeTontine").value(hasItem(DEFAULT_CODE_TONTINE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));

        // Check, that the count call also returns 1
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSanctionConfigurationShouldNotBeFound(String filter) throws Exception {
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSanctionConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSanctionConfiguration() throws Exception {
        // Get the sanctionConfiguration
        restSanctionConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSanctionConfiguration() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();

        // Update the sanctionConfiguration
        SanctionConfiguration updatedSanctionConfiguration = sanctionConfigurationRepository.findById(sanctionConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedSanctionConfiguration are not directly saved in db
        em.detach(updatedSanctionConfiguration);
        updatedSanctionConfiguration.codeAssociation(UPDATED_CODE_ASSOCIATION).codeTontine(UPDATED_CODE_TONTINE).type(UPDATED_TYPE);
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(updatedSanctionConfiguration);

        restSanctionConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SanctionConfiguration testSanctionConfiguration = sanctionConfigurationList.get(sanctionConfigurationList.size() - 1);
        assertThat(testSanctionConfiguration.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testSanctionConfiguration.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testSanctionConfiguration.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanctionConfigurationWithPatch() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();

        // Update the sanctionConfiguration using partial update
        SanctionConfiguration partialUpdatedSanctionConfiguration = new SanctionConfiguration();
        partialUpdatedSanctionConfiguration.setId(sanctionConfiguration.getId());

        partialUpdatedSanctionConfiguration.codeAssociation(UPDATED_CODE_ASSOCIATION).codeTontine(UPDATED_CODE_TONTINE);

        restSanctionConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SanctionConfiguration testSanctionConfiguration = sanctionConfigurationList.get(sanctionConfigurationList.size() - 1);
        assertThat(testSanctionConfiguration.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testSanctionConfiguration.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testSanctionConfiguration.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSanctionConfigurationWithPatch() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();

        // Update the sanctionConfiguration using partial update
        SanctionConfiguration partialUpdatedSanctionConfiguration = new SanctionConfiguration();
        partialUpdatedSanctionConfiguration.setId(sanctionConfiguration.getId());

        partialUpdatedSanctionConfiguration.codeAssociation(UPDATED_CODE_ASSOCIATION).codeTontine(UPDATED_CODE_TONTINE).type(UPDATED_TYPE);

        restSanctionConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanctionConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanctionConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SanctionConfiguration testSanctionConfiguration = sanctionConfigurationList.get(sanctionConfigurationList.size() - 1);
        assertThat(testSanctionConfiguration.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testSanctionConfiguration.getCodeTontine()).isEqualTo(UPDATED_CODE_TONTINE);
        assertThat(testSanctionConfiguration.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanctionConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanctionConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = sanctionConfigurationRepository.findAll().size();
        sanctionConfiguration.setId(count.incrementAndGet());

        // Create the SanctionConfiguration
        SanctionConfigurationDTO sanctionConfigurationDTO = sanctionConfigurationMapper.toDto(sanctionConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SanctionConfiguration in the database
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanctionConfiguration() throws Exception {
        // Initialize the database
        sanctionConfigurationRepository.saveAndFlush(sanctionConfiguration);

        int databaseSizeBeforeDelete = sanctionConfigurationRepository.findAll().size();

        // Delete the sanctionConfiguration
        restSanctionConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanctionConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SanctionConfiguration> sanctionConfigurationList = sanctionConfigurationRepository.findAll();
        assertThat(sanctionConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
