package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.PaiementTontine;
import com.it4innov.repository.PaiementTontineRepository;
import com.it4innov.service.criteria.PaiementTontineCriteria;
import com.it4innov.service.dto.PaiementTontineDTO;
import com.it4innov.service.mapper.PaiementTontineMapper;
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
 * Integration tests for the {@link PaiementTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementTontineResourceIT {

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementTontineRepository paiementTontineRepository;

    @Autowired
    private PaiementTontineMapper paiementTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementTontineMockMvc;

    private PaiementTontine paiementTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementTontine createEntity(EntityManager em) {
        PaiementTontine paiementTontine = new PaiementTontine().referencePaiement(DEFAULT_REFERENCE_PAIEMENT);
        return paiementTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementTontine createUpdatedEntity(EntityManager em) {
        PaiementTontine paiementTontine = new PaiementTontine().referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        return paiementTontine;
    }

    @BeforeEach
    public void initTest() {
        paiementTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementTontine() throws Exception {
        int databaseSizeBeforeCreate = paiementTontineRepository.findAll().size();
        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);
        restPaiementTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementTontine testPaiementTontine = paiementTontineList.get(paiementTontineList.size() - 1);
        assertThat(testPaiementTontine.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void createPaiementTontineWithExistingId() throws Exception {
        // Create the PaiementTontine with an existing ID
        paiementTontine.setId(1L);
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        int databaseSizeBeforeCreate = paiementTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPaiementTontines() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));
    }

    @Test
    @Transactional
    void getPaiementTontine() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get the paiementTontine
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementTontine.getId().intValue()))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT));
    }

    @Test
    @Transactional
    void getPaiementTontinesByIdFiltering() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        Long id = paiementTontine.getId();

        defaultPaiementTontineShouldBeFound("id.equals=" + id);
        defaultPaiementTontineShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByReferencePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList where referencePaiement equals to DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldBeFound("referencePaiement.equals=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementTontineList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldNotBeFound("referencePaiement.equals=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByReferencePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList where referencePaiement in DEFAULT_REFERENCE_PAIEMENT or UPDATED_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldBeFound("referencePaiement.in=" + DEFAULT_REFERENCE_PAIEMENT + "," + UPDATED_REFERENCE_PAIEMENT);

        // Get all the paiementTontineList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldNotBeFound("referencePaiement.in=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByReferencePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList where referencePaiement is not null
        defaultPaiementTontineShouldBeFound("referencePaiement.specified=true");

        // Get all the paiementTontineList where referencePaiement is null
        defaultPaiementTontineShouldNotBeFound("referencePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByReferencePaiementContainsSomething() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList where referencePaiement contains DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldBeFound("referencePaiement.contains=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementTontineList where referencePaiement contains UPDATED_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldNotBeFound("referencePaiement.contains=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByReferencePaiementNotContainsSomething() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        // Get all the paiementTontineList where referencePaiement does not contain DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldNotBeFound("referencePaiement.doesNotContain=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementTontineList where referencePaiement does not contain UPDATED_REFERENCE_PAIEMENT
        defaultPaiementTontineShouldBeFound("referencePaiement.doesNotContain=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByCotisationTontineIsEqualToSomething() throws Exception {
        CotisationTontine cotisationTontine;
        if (TestUtil.findAll(em, CotisationTontine.class).isEmpty()) {
            paiementTontineRepository.saveAndFlush(paiementTontine);
            cotisationTontine = CotisationTontineResourceIT.createEntity(em);
        } else {
            cotisationTontine = TestUtil.findAll(em, CotisationTontine.class).get(0);
        }
        em.persist(cotisationTontine);
        em.flush();
        paiementTontine.setCotisationTontine(cotisationTontine);
        paiementTontineRepository.saveAndFlush(paiementTontine);
        Long cotisationTontineId = cotisationTontine.getId();

        // Get all the paiementTontineList where cotisationTontine equals to cotisationTontineId
        defaultPaiementTontineShouldBeFound("cotisationTontineId.equals=" + cotisationTontineId);

        // Get all the paiementTontineList where cotisationTontine equals to (cotisationTontineId + 1)
        defaultPaiementTontineShouldNotBeFound("cotisationTontineId.equals=" + (cotisationTontineId + 1));
    }

    @Test
    @Transactional
    void getAllPaiementTontinesByDecaissementTontineIsEqualToSomething() throws Exception {
        DecaissementTontine decaissementTontine;
        if (TestUtil.findAll(em, DecaissementTontine.class).isEmpty()) {
            paiementTontineRepository.saveAndFlush(paiementTontine);
            decaissementTontine = DecaissementTontineResourceIT.createEntity(em);
        } else {
            decaissementTontine = TestUtil.findAll(em, DecaissementTontine.class).get(0);
        }
        em.persist(decaissementTontine);
        em.flush();
        paiementTontine.setDecaissementTontine(decaissementTontine);
        paiementTontineRepository.saveAndFlush(paiementTontine);
        Long decaissementTontineId = decaissementTontine.getId();

        // Get all the paiementTontineList where decaissementTontine equals to decaissementTontineId
        defaultPaiementTontineShouldBeFound("decaissementTontineId.equals=" + decaissementTontineId);

        // Get all the paiementTontineList where decaissementTontine equals to (decaissementTontineId + 1)
        defaultPaiementTontineShouldNotBeFound("decaissementTontineId.equals=" + (decaissementTontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementTontineShouldBeFound(String filter) throws Exception {
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));

        // Check, that the count call also returns 1
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementTontineShouldNotBeFound(String filter) throws Exception {
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiementTontine() throws Exception {
        // Get the paiementTontine
        restPaiementTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementTontine() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();

        // Update the paiementTontine
        PaiementTontine updatedPaiementTontine = paiementTontineRepository.findById(paiementTontine.getId()).get();
        // Disconnect from session so that the updates on updatedPaiementTontine are not directly saved in db
        em.detach(updatedPaiementTontine);
        updatedPaiementTontine.referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(updatedPaiementTontine);

        restPaiementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
        PaiementTontine testPaiementTontine = paiementTontineList.get(paiementTontineList.size() - 1);
        assertThat(testPaiementTontine.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void putNonExistingPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementTontineWithPatch() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();

        // Update the paiementTontine using partial update
        PaiementTontine partialUpdatedPaiementTontine = new PaiementTontine();
        partialUpdatedPaiementTontine.setId(paiementTontine.getId());

        restPaiementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementTontine))
            )
            .andExpect(status().isOk());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
        PaiementTontine testPaiementTontine = paiementTontineList.get(paiementTontineList.size() - 1);
        assertThat(testPaiementTontine.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementTontineWithPatch() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();

        // Update the paiementTontine using partial update
        PaiementTontine partialUpdatedPaiementTontine = new PaiementTontine();
        partialUpdatedPaiementTontine.setId(paiementTontine.getId());

        partialUpdatedPaiementTontine.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementTontine))
            )
            .andExpect(status().isOk());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
        PaiementTontine testPaiementTontine = paiementTontineList.get(paiementTontineList.size() - 1);
        assertThat(testPaiementTontine.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementTontine() throws Exception {
        int databaseSizeBeforeUpdate = paiementTontineRepository.findAll().size();
        paiementTontine.setId(count.incrementAndGet());

        // Create the PaiementTontine
        PaiementTontineDTO paiementTontineDTO = paiementTontineMapper.toDto(paiementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementTontine in the database
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementTontine() throws Exception {
        // Initialize the database
        paiementTontineRepository.saveAndFlush(paiementTontine);

        int databaseSizeBeforeDelete = paiementTontineRepository.findAll().size();

        // Delete the paiementTontine
        restPaiementTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementTontine> paiementTontineList = paiementTontineRepository.findAll();
        assertThat(paiementTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
