package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.PaiementSanction;
import com.it4innov.domain.Sanction;
import com.it4innov.repository.PaiementSanctionRepository;
import com.it4innov.service.criteria.PaiementSanctionCriteria;
import com.it4innov.service.dto.PaiementSanctionDTO;
import com.it4innov.service.mapper.PaiementSanctionMapper;
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
 * Integration tests for the {@link PaiementSanctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementSanctionResourceIT {

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-sanctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementSanctionRepository paiementSanctionRepository;

    @Autowired
    private PaiementSanctionMapper paiementSanctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementSanctionMockMvc;

    private PaiementSanction paiementSanction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementSanction createEntity(EntityManager em) {
        PaiementSanction paiementSanction = new PaiementSanction().referencePaiement(DEFAULT_REFERENCE_PAIEMENT);
        return paiementSanction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementSanction createUpdatedEntity(EntityManager em) {
        PaiementSanction paiementSanction = new PaiementSanction().referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        return paiementSanction;
    }

    @BeforeEach
    public void initTest() {
        paiementSanction = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementSanction() throws Exception {
        int databaseSizeBeforeCreate = paiementSanctionRepository.findAll().size();
        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);
        restPaiementSanctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementSanction testPaiementSanction = paiementSanctionList.get(paiementSanctionList.size() - 1);
        assertThat(testPaiementSanction.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void createPaiementSanctionWithExistingId() throws Exception {
        // Create the PaiementSanction with an existing ID
        paiementSanction.setId(1L);
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        int databaseSizeBeforeCreate = paiementSanctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementSanctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferencePaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementSanctionRepository.findAll().size();
        // set the field null
        paiementSanction.setReferencePaiement(null);

        // Create the PaiementSanction, which fails.
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        restPaiementSanctionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiementSanctions() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementSanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));
    }

    @Test
    @Transactional
    void getPaiementSanction() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get the paiementSanction
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementSanction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementSanction.getId().intValue()))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT));
    }

    @Test
    @Transactional
    void getPaiementSanctionsByIdFiltering() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        Long id = paiementSanction.getId();

        defaultPaiementSanctionShouldBeFound("id.equals=" + id);
        defaultPaiementSanctionShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementSanctionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementSanctionShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementSanctionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementSanctionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsByReferencePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList where referencePaiement equals to DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldBeFound("referencePaiement.equals=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementSanctionList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldNotBeFound("referencePaiement.equals=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsByReferencePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList where referencePaiement in DEFAULT_REFERENCE_PAIEMENT or UPDATED_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldBeFound("referencePaiement.in=" + DEFAULT_REFERENCE_PAIEMENT + "," + UPDATED_REFERENCE_PAIEMENT);

        // Get all the paiementSanctionList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldNotBeFound("referencePaiement.in=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsByReferencePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList where referencePaiement is not null
        defaultPaiementSanctionShouldBeFound("referencePaiement.specified=true");

        // Get all the paiementSanctionList where referencePaiement is null
        defaultPaiementSanctionShouldNotBeFound("referencePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsByReferencePaiementContainsSomething() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList where referencePaiement contains DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldBeFound("referencePaiement.contains=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementSanctionList where referencePaiement contains UPDATED_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldNotBeFound("referencePaiement.contains=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsByReferencePaiementNotContainsSomething() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        // Get all the paiementSanctionList where referencePaiement does not contain DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldNotBeFound("referencePaiement.doesNotContain=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementSanctionList where referencePaiement does not contain UPDATED_REFERENCE_PAIEMENT
        defaultPaiementSanctionShouldBeFound("referencePaiement.doesNotContain=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementSanctionsBySanctionIsEqualToSomething() throws Exception {
        Sanction sanction;
        if (TestUtil.findAll(em, Sanction.class).isEmpty()) {
            paiementSanctionRepository.saveAndFlush(paiementSanction);
            sanction = SanctionResourceIT.createEntity(em);
        } else {
            sanction = TestUtil.findAll(em, Sanction.class).get(0);
        }
        em.persist(sanction);
        em.flush();
        paiementSanction.setSanction(sanction);
        paiementSanctionRepository.saveAndFlush(paiementSanction);
        Long sanctionId = sanction.getId();

        // Get all the paiementSanctionList where sanction equals to sanctionId
        defaultPaiementSanctionShouldBeFound("sanctionId.equals=" + sanctionId);

        // Get all the paiementSanctionList where sanction equals to (sanctionId + 1)
        defaultPaiementSanctionShouldNotBeFound("sanctionId.equals=" + (sanctionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementSanctionShouldBeFound(String filter) throws Exception {
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementSanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));

        // Check, that the count call also returns 1
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementSanctionShouldNotBeFound(String filter) throws Exception {
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiementSanction() throws Exception {
        // Get the paiementSanction
        restPaiementSanctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementSanction() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();

        // Update the paiementSanction
        PaiementSanction updatedPaiementSanction = paiementSanctionRepository.findById(paiementSanction.getId()).get();
        // Disconnect from session so that the updates on updatedPaiementSanction are not directly saved in db
        em.detach(updatedPaiementSanction);
        updatedPaiementSanction.referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(updatedPaiementSanction);

        restPaiementSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementSanctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
        PaiementSanction testPaiementSanction = paiementSanctionList.get(paiementSanctionList.size() - 1);
        assertThat(testPaiementSanction.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void putNonExistingPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementSanctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementSanctionWithPatch() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();

        // Update the paiementSanction using partial update
        PaiementSanction partialUpdatedPaiementSanction = new PaiementSanction();
        partialUpdatedPaiementSanction.setId(paiementSanction.getId());

        partialUpdatedPaiementSanction.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementSanction))
            )
            .andExpect(status().isOk());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
        PaiementSanction testPaiementSanction = paiementSanctionList.get(paiementSanctionList.size() - 1);
        assertThat(testPaiementSanction.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementSanctionWithPatch() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();

        // Update the paiementSanction using partial update
        PaiementSanction partialUpdatedPaiementSanction = new PaiementSanction();
        partialUpdatedPaiementSanction.setId(paiementSanction.getId());

        partialUpdatedPaiementSanction.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementSanction))
            )
            .andExpect(status().isOk());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
        PaiementSanction testPaiementSanction = paiementSanctionList.get(paiementSanctionList.size() - 1);
        assertThat(testPaiementSanction.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementSanctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementSanction() throws Exception {
        int databaseSizeBeforeUpdate = paiementSanctionRepository.findAll().size();
        paiementSanction.setId(count.incrementAndGet());

        // Create the PaiementSanction
        PaiementSanctionDTO paiementSanctionDTO = paiementSanctionMapper.toDto(paiementSanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementSanctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementSanction in the database
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementSanction() throws Exception {
        // Initialize the database
        paiementSanctionRepository.saveAndFlush(paiementSanction);

        int databaseSizeBeforeDelete = paiementSanctionRepository.findAll().size();

        // Delete the paiementSanction
        restPaiementSanctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementSanction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementSanction> paiementSanctionList = paiementSanctionRepository.findAll();
        assertThat(paiementSanctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
