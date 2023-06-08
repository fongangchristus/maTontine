package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Banque;
import com.it4innov.domain.GestionnaireBanque;
import com.it4innov.repository.GestionnaireBanqueRepository;
import com.it4innov.service.criteria.GestionnaireBanqueCriteria;
import com.it4innov.service.dto.GestionnaireBanqueDTO;
import com.it4innov.service.mapper.GestionnaireBanqueMapper;
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
 * Integration tests for the {@link GestionnaireBanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GestionnaireBanqueResourceIT {

    private static final String DEFAULT_MATRICULE_MEMBRE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_MEMBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gestionnaire-banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GestionnaireBanqueRepository gestionnaireBanqueRepository;

    @Autowired
    private GestionnaireBanqueMapper gestionnaireBanqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGestionnaireBanqueMockMvc;

    private GestionnaireBanque gestionnaireBanque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GestionnaireBanque createEntity(EntityManager em) {
        GestionnaireBanque gestionnaireBanque = new GestionnaireBanque().matriculeMembre(DEFAULT_MATRICULE_MEMBRE);
        return gestionnaireBanque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GestionnaireBanque createUpdatedEntity(EntityManager em) {
        GestionnaireBanque gestionnaireBanque = new GestionnaireBanque().matriculeMembre(UPDATED_MATRICULE_MEMBRE);
        return gestionnaireBanque;
    }

    @BeforeEach
    public void initTest() {
        gestionnaireBanque = createEntity(em);
    }

    @Test
    @Transactional
    void createGestionnaireBanque() throws Exception {
        int databaseSizeBeforeCreate = gestionnaireBanqueRepository.findAll().size();
        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);
        restGestionnaireBanqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeCreate + 1);
        GestionnaireBanque testGestionnaireBanque = gestionnaireBanqueList.get(gestionnaireBanqueList.size() - 1);
        assertThat(testGestionnaireBanque.getMatriculeMembre()).isEqualTo(DEFAULT_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void createGestionnaireBanqueWithExistingId() throws Exception {
        // Create the GestionnaireBanque with an existing ID
        gestionnaireBanque.setId(1L);
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        int databaseSizeBeforeCreate = gestionnaireBanqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGestionnaireBanqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanques() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaireBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeMembre").value(hasItem(DEFAULT_MATRICULE_MEMBRE)));
    }

    @Test
    @Transactional
    void getGestionnaireBanque() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get the gestionnaireBanque
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, gestionnaireBanque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gestionnaireBanque.getId().intValue()))
            .andExpect(jsonPath("$.matriculeMembre").value(DEFAULT_MATRICULE_MEMBRE));
    }

    @Test
    @Transactional
    void getGestionnaireBanquesByIdFiltering() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        Long id = gestionnaireBanque.getId();

        defaultGestionnaireBanqueShouldBeFound("id.equals=" + id);
        defaultGestionnaireBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultGestionnaireBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGestionnaireBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultGestionnaireBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGestionnaireBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByMatriculeMembreIsEqualToSomething() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList where matriculeMembre equals to DEFAULT_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldBeFound("matriculeMembre.equals=" + DEFAULT_MATRICULE_MEMBRE);

        // Get all the gestionnaireBanqueList where matriculeMembre equals to UPDATED_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldNotBeFound("matriculeMembre.equals=" + UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByMatriculeMembreIsInShouldWork() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList where matriculeMembre in DEFAULT_MATRICULE_MEMBRE or UPDATED_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldBeFound("matriculeMembre.in=" + DEFAULT_MATRICULE_MEMBRE + "," + UPDATED_MATRICULE_MEMBRE);

        // Get all the gestionnaireBanqueList where matriculeMembre equals to UPDATED_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldNotBeFound("matriculeMembre.in=" + UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByMatriculeMembreIsNullOrNotNull() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList where matriculeMembre is not null
        defaultGestionnaireBanqueShouldBeFound("matriculeMembre.specified=true");

        // Get all the gestionnaireBanqueList where matriculeMembre is null
        defaultGestionnaireBanqueShouldNotBeFound("matriculeMembre.specified=false");
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByMatriculeMembreContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList where matriculeMembre contains DEFAULT_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldBeFound("matriculeMembre.contains=" + DEFAULT_MATRICULE_MEMBRE);

        // Get all the gestionnaireBanqueList where matriculeMembre contains UPDATED_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldNotBeFound("matriculeMembre.contains=" + UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByMatriculeMembreNotContainsSomething() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        // Get all the gestionnaireBanqueList where matriculeMembre does not contain DEFAULT_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldNotBeFound("matriculeMembre.doesNotContain=" + DEFAULT_MATRICULE_MEMBRE);

        // Get all the gestionnaireBanqueList where matriculeMembre does not contain UPDATED_MATRICULE_MEMBRE
        defaultGestionnaireBanqueShouldBeFound("matriculeMembre.doesNotContain=" + UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void getAllGestionnaireBanquesByBanqueIsEqualToSomething() throws Exception {
        Banque banque;
        if (TestUtil.findAll(em, Banque.class).isEmpty()) {
            gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);
            banque = BanqueResourceIT.createEntity(em);
        } else {
            banque = TestUtil.findAll(em, Banque.class).get(0);
        }
        em.persist(banque);
        em.flush();
        gestionnaireBanque.setBanque(banque);
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);
        Long banqueId = banque.getId();

        // Get all the gestionnaireBanqueList where banque equals to banqueId
        defaultGestionnaireBanqueShouldBeFound("banqueId.equals=" + banqueId);

        // Get all the gestionnaireBanqueList where banque equals to (banqueId + 1)
        defaultGestionnaireBanqueShouldNotBeFound("banqueId.equals=" + (banqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGestionnaireBanqueShouldBeFound(String filter) throws Exception {
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gestionnaireBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeMembre").value(hasItem(DEFAULT_MATRICULE_MEMBRE)));

        // Check, that the count call also returns 1
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGestionnaireBanqueShouldNotBeFound(String filter) throws Exception {
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGestionnaireBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGestionnaireBanque() throws Exception {
        // Get the gestionnaireBanque
        restGestionnaireBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGestionnaireBanque() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();

        // Update the gestionnaireBanque
        GestionnaireBanque updatedGestionnaireBanque = gestionnaireBanqueRepository.findById(gestionnaireBanque.getId()).get();
        // Disconnect from session so that the updates on updatedGestionnaireBanque are not directly saved in db
        em.detach(updatedGestionnaireBanque);
        updatedGestionnaireBanque.matriculeMembre(UPDATED_MATRICULE_MEMBRE);
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(updatedGestionnaireBanque);

        restGestionnaireBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionnaireBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireBanque testGestionnaireBanque = gestionnaireBanqueList.get(gestionnaireBanqueList.size() - 1);
        assertThat(testGestionnaireBanque.getMatriculeMembre()).isEqualTo(UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void putNonExistingGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gestionnaireBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGestionnaireBanqueWithPatch() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();

        // Update the gestionnaireBanque using partial update
        GestionnaireBanque partialUpdatedGestionnaireBanque = new GestionnaireBanque();
        partialUpdatedGestionnaireBanque.setId(gestionnaireBanque.getId());

        partialUpdatedGestionnaireBanque.matriculeMembre(UPDATED_MATRICULE_MEMBRE);

        restGestionnaireBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestionnaireBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestionnaireBanque))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireBanque testGestionnaireBanque = gestionnaireBanqueList.get(gestionnaireBanqueList.size() - 1);
        assertThat(testGestionnaireBanque.getMatriculeMembre()).isEqualTo(UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void fullUpdateGestionnaireBanqueWithPatch() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();

        // Update the gestionnaireBanque using partial update
        GestionnaireBanque partialUpdatedGestionnaireBanque = new GestionnaireBanque();
        partialUpdatedGestionnaireBanque.setId(gestionnaireBanque.getId());

        partialUpdatedGestionnaireBanque.matriculeMembre(UPDATED_MATRICULE_MEMBRE);

        restGestionnaireBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGestionnaireBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGestionnaireBanque))
            )
            .andExpect(status().isOk());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
        GestionnaireBanque testGestionnaireBanque = gestionnaireBanqueList.get(gestionnaireBanqueList.size() - 1);
        assertThat(testGestionnaireBanque.getMatriculeMembre()).isEqualTo(UPDATED_MATRICULE_MEMBRE);
    }

    @Test
    @Transactional
    void patchNonExistingGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gestionnaireBanqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGestionnaireBanque() throws Exception {
        int databaseSizeBeforeUpdate = gestionnaireBanqueRepository.findAll().size();
        gestionnaireBanque.setId(count.incrementAndGet());

        // Create the GestionnaireBanque
        GestionnaireBanqueDTO gestionnaireBanqueDTO = gestionnaireBanqueMapper.toDto(gestionnaireBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGestionnaireBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gestionnaireBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GestionnaireBanque in the database
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGestionnaireBanque() throws Exception {
        // Initialize the database
        gestionnaireBanqueRepository.saveAndFlush(gestionnaireBanque);

        int databaseSizeBeforeDelete = gestionnaireBanqueRepository.findAll().size();

        // Delete the gestionnaireBanque
        restGestionnaireBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, gestionnaireBanque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GestionnaireBanque> gestionnaireBanqueList = gestionnaireBanqueRepository.findAll();
        assertThat(gestionnaireBanqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
