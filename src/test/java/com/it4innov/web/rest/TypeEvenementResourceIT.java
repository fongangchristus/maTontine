package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Evenement;
import com.it4innov.domain.TypeEvenement;
import com.it4innov.repository.TypeEvenementRepository;
import com.it4innov.service.criteria.TypeEvenementCriteria;
import com.it4innov.service.dto.TypeEvenementDTO;
import com.it4innov.service.mapper.TypeEvenementMapper;
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
 * Integration tests for the {@link TypeEvenementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeEvenementResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-evenements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeEvenementRepository typeEvenementRepository;

    @Autowired
    private TypeEvenementMapper typeEvenementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeEvenementMockMvc;

    private TypeEvenement typeEvenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeEvenement createEntity(EntityManager em) {
        TypeEvenement typeEvenement = new TypeEvenement().libele(DEFAULT_LIBELE).observation(DEFAULT_OBSERVATION);
        return typeEvenement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeEvenement createUpdatedEntity(EntityManager em) {
        TypeEvenement typeEvenement = new TypeEvenement().libele(UPDATED_LIBELE).observation(UPDATED_OBSERVATION);
        return typeEvenement;
    }

    @BeforeEach
    public void initTest() {
        typeEvenement = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeEvenement() throws Exception {
        int databaseSizeBeforeCreate = typeEvenementRepository.findAll().size();
        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);
        restTypeEvenementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeCreate + 1);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testTypeEvenement.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createTypeEvenementWithExistingId() throws Exception {
        // Create the TypeEvenement with an existing ID
        typeEvenement.setId(1L);
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        int databaseSizeBeforeCreate = typeEvenementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeEvenementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeEvenements() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get the typeEvenement
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL_ID, typeEvenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeEvenement.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getTypeEvenementsByIdFiltering() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        Long id = typeEvenement.getId();

        defaultTypeEvenementShouldBeFound("id.equals=" + id);
        defaultTypeEvenementShouldNotBeFound("id.notEquals=" + id);

        defaultTypeEvenementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypeEvenementShouldNotBeFound("id.greaterThan=" + id);

        defaultTypeEvenementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypeEvenementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where libele equals to DEFAULT_LIBELE
        defaultTypeEvenementShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the typeEvenementList where libele equals to UPDATED_LIBELE
        defaultTypeEvenementShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultTypeEvenementShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the typeEvenementList where libele equals to UPDATED_LIBELE
        defaultTypeEvenementShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where libele is not null
        defaultTypeEvenementShouldBeFound("libele.specified=true");

        // Get all the typeEvenementList where libele is null
        defaultTypeEvenementShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where libele contains DEFAULT_LIBELE
        defaultTypeEvenementShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the typeEvenementList where libele contains UPDATED_LIBELE
        defaultTypeEvenementShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where libele does not contain DEFAULT_LIBELE
        defaultTypeEvenementShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the typeEvenementList where libele does not contain UPDATED_LIBELE
        defaultTypeEvenementShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where observation equals to DEFAULT_OBSERVATION
        defaultTypeEvenementShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the typeEvenementList where observation equals to UPDATED_OBSERVATION
        defaultTypeEvenementShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultTypeEvenementShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the typeEvenementList where observation equals to UPDATED_OBSERVATION
        defaultTypeEvenementShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where observation is not null
        defaultTypeEvenementShouldBeFound("observation.specified=true");

        // Get all the typeEvenementList where observation is null
        defaultTypeEvenementShouldNotBeFound("observation.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByObservationContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where observation contains DEFAULT_OBSERVATION
        defaultTypeEvenementShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the typeEvenementList where observation contains UPDATED_OBSERVATION
        defaultTypeEvenementShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        // Get all the typeEvenementList where observation does not contain DEFAULT_OBSERVATION
        defaultTypeEvenementShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the typeEvenementList where observation does not contain UPDATED_OBSERVATION
        defaultTypeEvenementShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTypeEvenementsByEvenementIsEqualToSomething() throws Exception {
        Evenement evenement;
        if (TestUtil.findAll(em, Evenement.class).isEmpty()) {
            typeEvenementRepository.saveAndFlush(typeEvenement);
            evenement = EvenementResourceIT.createEntity(em);
        } else {
            evenement = TestUtil.findAll(em, Evenement.class).get(0);
        }
        em.persist(evenement);
        em.flush();
        typeEvenement.addEvenement(evenement);
        typeEvenementRepository.saveAndFlush(typeEvenement);
        Long evenementId = evenement.getId();

        // Get all the typeEvenementList where evenement equals to evenementId
        defaultTypeEvenementShouldBeFound("evenementId.equals=" + evenementId);

        // Get all the typeEvenementList where evenement equals to (evenementId + 1)
        defaultTypeEvenementShouldNotBeFound("evenementId.equals=" + (evenementId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeEvenementShouldBeFound(String filter) throws Exception {
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeEvenementShouldNotBeFound(String filter) throws Exception {
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeEvenementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeEvenement() throws Exception {
        // Get the typeEvenement
        restTypeEvenementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement
        TypeEvenement updatedTypeEvenement = typeEvenementRepository.findById(typeEvenement.getId()).get();
        // Disconnect from session so that the updates on updatedTypeEvenement are not directly saved in db
        em.detach(updatedTypeEvenement);
        updatedTypeEvenement.libele(UPDATED_LIBELE).observation(UPDATED_OBSERVATION);
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(updatedTypeEvenement);

        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeEvenementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypeEvenement.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeEvenementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeEvenementWithPatch() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement using partial update
        TypeEvenement partialUpdatedTypeEvenement = new TypeEvenement();
        partialUpdatedTypeEvenement.setId(typeEvenement.getId());

        partialUpdatedTypeEvenement.libele(UPDATED_LIBELE);

        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeEvenement))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypeEvenement.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateTypeEvenementWithPatch() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();

        // Update the typeEvenement using partial update
        TypeEvenement partialUpdatedTypeEvenement = new TypeEvenement();
        partialUpdatedTypeEvenement.setId(typeEvenement.getId());

        partialUpdatedTypeEvenement.libele(UPDATED_LIBELE).observation(UPDATED_OBSERVATION);

        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeEvenement))
            )
            .andExpect(status().isOk());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
        TypeEvenement testTypeEvenement = typeEvenementList.get(typeEvenementList.size() - 1);
        assertThat(testTypeEvenement.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypeEvenement.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeEvenementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeEvenement() throws Exception {
        int databaseSizeBeforeUpdate = typeEvenementRepository.findAll().size();
        typeEvenement.setId(count.incrementAndGet());

        // Create the TypeEvenement
        TypeEvenementDTO typeEvenementDTO = typeEvenementMapper.toDto(typeEvenement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeEvenementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeEvenement in the database
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeEvenement() throws Exception {
        // Initialize the database
        typeEvenementRepository.saveAndFlush(typeEvenement);

        int databaseSizeBeforeDelete = typeEvenementRepository.findAll().size();

        // Delete the typeEvenement
        restTypeEvenementMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeEvenement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeEvenement> typeEvenementList = typeEvenementRepository.findAll();
        assertThat(typeEvenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
