package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Adhesion;
import com.it4innov.domain.PaiementAdhesion;
import com.it4innov.repository.PaiementAdhesionRepository;
import com.it4innov.service.criteria.PaiementAdhesionCriteria;
import com.it4innov.service.dto.PaiementAdhesionDTO;
import com.it4innov.service.mapper.PaiementAdhesionMapper;
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
 * Integration tests for the {@link PaiementAdhesionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementAdhesionResourceIT {

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-adhesions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementAdhesionRepository paiementAdhesionRepository;

    @Autowired
    private PaiementAdhesionMapper paiementAdhesionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementAdhesionMockMvc;

    private PaiementAdhesion paiementAdhesion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementAdhesion createEntity(EntityManager em) {
        PaiementAdhesion paiementAdhesion = new PaiementAdhesion().referencePaiement(DEFAULT_REFERENCE_PAIEMENT);
        return paiementAdhesion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementAdhesion createUpdatedEntity(EntityManager em) {
        PaiementAdhesion paiementAdhesion = new PaiementAdhesion().referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        return paiementAdhesion;
    }

    @BeforeEach
    public void initTest() {
        paiementAdhesion = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementAdhesion() throws Exception {
        int databaseSizeBeforeCreate = paiementAdhesionRepository.findAll().size();
        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);
        restPaiementAdhesionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementAdhesion testPaiementAdhesion = paiementAdhesionList.get(paiementAdhesionList.size() - 1);
        assertThat(testPaiementAdhesion.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void createPaiementAdhesionWithExistingId() throws Exception {
        // Create the PaiementAdhesion with an existing ID
        paiementAdhesion.setId(1L);
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        int databaseSizeBeforeCreate = paiementAdhesionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementAdhesionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferencePaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementAdhesionRepository.findAll().size();
        // set the field null
        paiementAdhesion.setReferencePaiement(null);

        // Create the PaiementAdhesion, which fails.
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        restPaiementAdhesionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesions() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementAdhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));
    }

    @Test
    @Transactional
    void getPaiementAdhesion() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get the paiementAdhesion
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementAdhesion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementAdhesion.getId().intValue()))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT));
    }

    @Test
    @Transactional
    void getPaiementAdhesionsByIdFiltering() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        Long id = paiementAdhesion.getId();

        defaultPaiementAdhesionShouldBeFound("id.equals=" + id);
        defaultPaiementAdhesionShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementAdhesionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementAdhesionShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementAdhesionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementAdhesionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByReferencePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList where referencePaiement equals to DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldBeFound("referencePaiement.equals=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementAdhesionList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldNotBeFound("referencePaiement.equals=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByReferencePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList where referencePaiement in DEFAULT_REFERENCE_PAIEMENT or UPDATED_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldBeFound("referencePaiement.in=" + DEFAULT_REFERENCE_PAIEMENT + "," + UPDATED_REFERENCE_PAIEMENT);

        // Get all the paiementAdhesionList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldNotBeFound("referencePaiement.in=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByReferencePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList where referencePaiement is not null
        defaultPaiementAdhesionShouldBeFound("referencePaiement.specified=true");

        // Get all the paiementAdhesionList where referencePaiement is null
        defaultPaiementAdhesionShouldNotBeFound("referencePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByReferencePaiementContainsSomething() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList where referencePaiement contains DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldBeFound("referencePaiement.contains=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementAdhesionList where referencePaiement contains UPDATED_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldNotBeFound("referencePaiement.contains=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByReferencePaiementNotContainsSomething() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        // Get all the paiementAdhesionList where referencePaiement does not contain DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldNotBeFound("referencePaiement.doesNotContain=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementAdhesionList where referencePaiement does not contain UPDATED_REFERENCE_PAIEMENT
        defaultPaiementAdhesionShouldBeFound("referencePaiement.doesNotContain=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementAdhesionsByAdhesionIsEqualToSomething() throws Exception {
        Adhesion adhesion;
        if (TestUtil.findAll(em, Adhesion.class).isEmpty()) {
            paiementAdhesionRepository.saveAndFlush(paiementAdhesion);
            adhesion = AdhesionResourceIT.createEntity(em);
        } else {
            adhesion = TestUtil.findAll(em, Adhesion.class).get(0);
        }
        em.persist(adhesion);
        em.flush();
        paiementAdhesion.setAdhesion(adhesion);
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);
        Long adhesionId = adhesion.getId();

        // Get all the paiementAdhesionList where adhesion equals to adhesionId
        defaultPaiementAdhesionShouldBeFound("adhesionId.equals=" + adhesionId);

        // Get all the paiementAdhesionList where adhesion equals to (adhesionId + 1)
        defaultPaiementAdhesionShouldNotBeFound("adhesionId.equals=" + (adhesionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementAdhesionShouldBeFound(String filter) throws Exception {
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementAdhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));

        // Check, that the count call also returns 1
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementAdhesionShouldNotBeFound(String filter) throws Exception {
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiementAdhesion() throws Exception {
        // Get the paiementAdhesion
        restPaiementAdhesionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementAdhesion() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();

        // Update the paiementAdhesion
        PaiementAdhesion updatedPaiementAdhesion = paiementAdhesionRepository.findById(paiementAdhesion.getId()).get();
        // Disconnect from session so that the updates on updatedPaiementAdhesion are not directly saved in db
        em.detach(updatedPaiementAdhesion);
        updatedPaiementAdhesion.referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(updatedPaiementAdhesion);

        restPaiementAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementAdhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
        PaiementAdhesion testPaiementAdhesion = paiementAdhesionList.get(paiementAdhesionList.size() - 1);
        assertThat(testPaiementAdhesion.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void putNonExistingPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementAdhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementAdhesionWithPatch() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();

        // Update the paiementAdhesion using partial update
        PaiementAdhesion partialUpdatedPaiementAdhesion = new PaiementAdhesion();
        partialUpdatedPaiementAdhesion.setId(paiementAdhesion.getId());

        partialUpdatedPaiementAdhesion.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
        PaiementAdhesion testPaiementAdhesion = paiementAdhesionList.get(paiementAdhesionList.size() - 1);
        assertThat(testPaiementAdhesion.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementAdhesionWithPatch() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();

        // Update the paiementAdhesion using partial update
        PaiementAdhesion partialUpdatedPaiementAdhesion = new PaiementAdhesion();
        partialUpdatedPaiementAdhesion.setId(paiementAdhesion.getId());

        partialUpdatedPaiementAdhesion.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
        PaiementAdhesion testPaiementAdhesion = paiementAdhesionList.get(paiementAdhesionList.size() - 1);
        assertThat(testPaiementAdhesion.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementAdhesionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = paiementAdhesionRepository.findAll().size();
        paiementAdhesion.setId(count.incrementAndGet());

        // Create the PaiementAdhesion
        PaiementAdhesionDTO paiementAdhesionDTO = paiementAdhesionMapper.toDto(paiementAdhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementAdhesionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementAdhesion in the database
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementAdhesion() throws Exception {
        // Initialize the database
        paiementAdhesionRepository.saveAndFlush(paiementAdhesion);

        int databaseSizeBeforeDelete = paiementAdhesionRepository.findAll().size();

        // Delete the paiementAdhesion
        restPaiementAdhesionMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementAdhesion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementAdhesion> paiementAdhesionList = paiementAdhesionRepository.findAll();
        assertThat(paiementAdhesionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
