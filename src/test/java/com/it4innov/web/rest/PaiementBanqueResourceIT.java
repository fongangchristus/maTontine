package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.PaiementBanque;
import com.it4innov.repository.PaiementBanqueRepository;
import com.it4innov.service.criteria.PaiementBanqueCriteria;
import com.it4innov.service.dto.PaiementBanqueDTO;
import com.it4innov.service.mapper.PaiementBanqueMapper;
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
 * Integration tests for the {@link PaiementBanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementBanqueResourceIT {

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/paiement-banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementBanqueRepository paiementBanqueRepository;

    @Autowired
    private PaiementBanqueMapper paiementBanqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementBanqueMockMvc;

    private PaiementBanque paiementBanque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementBanque createEntity(EntityManager em) {
        PaiementBanque paiementBanque = new PaiementBanque().referencePaiement(DEFAULT_REFERENCE_PAIEMENT);
        return paiementBanque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PaiementBanque createUpdatedEntity(EntityManager em) {
        PaiementBanque paiementBanque = new PaiementBanque().referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        return paiementBanque;
    }

    @BeforeEach
    public void initTest() {
        paiementBanque = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiementBanque() throws Exception {
        int databaseSizeBeforeCreate = paiementBanqueRepository.findAll().size();
        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);
        restPaiementBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeCreate + 1);
        PaiementBanque testPaiementBanque = paiementBanqueList.get(paiementBanqueList.size() - 1);
        assertThat(testPaiementBanque.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void createPaiementBanqueWithExistingId() throws Exception {
        // Create the PaiementBanque with an existing ID
        paiementBanque.setId(1L);
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        int databaseSizeBeforeCreate = paiementBanqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkReferencePaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementBanqueRepository.findAll().size();
        // set the field null
        paiementBanque.setReferencePaiement(null);

        // Create the PaiementBanque, which fails.
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        restPaiementBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiementBanques() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));
    }

    @Test
    @Transactional
    void getPaiementBanque() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get the paiementBanque
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, paiementBanque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiementBanque.getId().intValue()))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT));
    }

    @Test
    @Transactional
    void getPaiementBanquesByIdFiltering() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        Long id = paiementBanque.getId();

        defaultPaiementBanqueShouldBeFound("id.equals=" + id);
        defaultPaiementBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementBanquesByReferencePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList where referencePaiement equals to DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldBeFound("referencePaiement.equals=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementBanqueList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldNotBeFound("referencePaiement.equals=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementBanquesByReferencePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList where referencePaiement in DEFAULT_REFERENCE_PAIEMENT or UPDATED_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldBeFound("referencePaiement.in=" + DEFAULT_REFERENCE_PAIEMENT + "," + UPDATED_REFERENCE_PAIEMENT);

        // Get all the paiementBanqueList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldNotBeFound("referencePaiement.in=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementBanquesByReferencePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList where referencePaiement is not null
        defaultPaiementBanqueShouldBeFound("referencePaiement.specified=true");

        // Get all the paiementBanqueList where referencePaiement is null
        defaultPaiementBanqueShouldNotBeFound("referencePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementBanquesByReferencePaiementContainsSomething() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList where referencePaiement contains DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldBeFound("referencePaiement.contains=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementBanqueList where referencePaiement contains UPDATED_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldNotBeFound("referencePaiement.contains=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementBanquesByReferencePaiementNotContainsSomething() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        // Get all the paiementBanqueList where referencePaiement does not contain DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldNotBeFound("referencePaiement.doesNotContain=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementBanqueList where referencePaiement does not contain UPDATED_REFERENCE_PAIEMENT
        defaultPaiementBanqueShouldBeFound("referencePaiement.doesNotContain=" + UPDATED_REFERENCE_PAIEMENT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementBanqueShouldBeFound(String filter) throws Exception {
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiementBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)));

        // Check, that the count call also returns 1
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementBanqueShouldNotBeFound(String filter) throws Exception {
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiementBanque() throws Exception {
        // Get the paiementBanque
        restPaiementBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiementBanque() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();

        // Update the paiementBanque
        PaiementBanque updatedPaiementBanque = paiementBanqueRepository.findById(paiementBanque.getId()).get();
        // Disconnect from session so that the updates on updatedPaiementBanque are not directly saved in db
        em.detach(updatedPaiementBanque);
        updatedPaiementBanque.referencePaiement(UPDATED_REFERENCE_PAIEMENT);
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(updatedPaiementBanque);

        restPaiementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
        PaiementBanque testPaiementBanque = paiementBanqueList.get(paiementBanqueList.size() - 1);
        assertThat(testPaiementBanque.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void putNonExistingPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementBanqueWithPatch() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();

        // Update the paiementBanque using partial update
        PaiementBanque partialUpdatedPaiementBanque = new PaiementBanque();
        partialUpdatedPaiementBanque.setId(paiementBanque.getId());

        restPaiementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementBanque))
            )
            .andExpect(status().isOk());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
        PaiementBanque testPaiementBanque = paiementBanqueList.get(paiementBanqueList.size() - 1);
        assertThat(testPaiementBanque.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementBanqueWithPatch() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();

        // Update the paiementBanque using partial update
        PaiementBanque partialUpdatedPaiementBanque = new PaiementBanque();
        partialUpdatedPaiementBanque.setId(paiementBanque.getId());

        partialUpdatedPaiementBanque.referencePaiement(UPDATED_REFERENCE_PAIEMENT);

        restPaiementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiementBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiementBanque))
            )
            .andExpect(status().isOk());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
        PaiementBanque testPaiementBanque = paiementBanqueList.get(paiementBanqueList.size() - 1);
        assertThat(testPaiementBanque.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementBanqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiementBanque() throws Exception {
        int databaseSizeBeforeUpdate = paiementBanqueRepository.findAll().size();
        paiementBanque.setId(count.incrementAndGet());

        // Create the PaiementBanque
        PaiementBanqueDTO paiementBanqueDTO = paiementBanqueMapper.toDto(paiementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PaiementBanque in the database
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiementBanque() throws Exception {
        // Initialize the database
        paiementBanqueRepository.saveAndFlush(paiementBanque);

        int databaseSizeBeforeDelete = paiementBanqueRepository.findAll().size();

        // Delete the paiementBanque
        restPaiementBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiementBanque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PaiementBanque> paiementBanqueList = paiementBanqueRepository.findAll();
        assertThat(paiementBanqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
