package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Fonction;
import com.it4innov.domain.FonctionAdherent;
import com.it4innov.repository.FonctionRepository;
import com.it4innov.service.criteria.FonctionCriteria;
import com.it4innov.service.dto.FonctionDTO;
import com.it4innov.service.mapper.FonctionMapper;
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
 * Integration tests for the {@link FonctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FonctionResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fonctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FonctionRepository fonctionRepository;

    @Autowired
    private FonctionMapper fonctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFonctionMockMvc;

    private Fonction fonction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createEntity(EntityManager em) {
        Fonction fonction = new Fonction().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return fonction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fonction createUpdatedEntity(EntityManager em) {
        Fonction fonction = new Fonction().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return fonction;
    }

    @BeforeEach
    public void initTest() {
        fonction = createEntity(em);
    }

    @Test
    @Transactional
    void createFonction() throws Exception {
        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();
        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);
        restFonctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionDTO)))
            .andExpect(status().isCreated());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate + 1);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFonction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createFonctionWithExistingId() throws Exception {
        // Create the Fonction with an existing ID
        fonction.setId(1L);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFonctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFonctions() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get the fonction
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL_ID, fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getFonctionsByIdFiltering() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        Long id = fonction.getId();

        defaultFonctionShouldBeFound("id.equals=" + id);
        defaultFonctionShouldNotBeFound("id.notEquals=" + id);

        defaultFonctionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFonctionShouldNotBeFound("id.greaterThan=" + id);

        defaultFonctionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFonctionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFonctionsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where title equals to DEFAULT_TITLE
        defaultFonctionShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the fonctionList where title equals to UPDATED_TITLE
        defaultFonctionShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFonctionsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultFonctionShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the fonctionList where title equals to UPDATED_TITLE
        defaultFonctionShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFonctionsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where title is not null
        defaultFonctionShouldBeFound("title.specified=true");

        // Get all the fonctionList where title is null
        defaultFonctionShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionsByTitleContainsSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where title contains DEFAULT_TITLE
        defaultFonctionShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the fonctionList where title contains UPDATED_TITLE
        defaultFonctionShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFonctionsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where title does not contain DEFAULT_TITLE
        defaultFonctionShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the fonctionList where title does not contain UPDATED_TITLE
        defaultFonctionShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllFonctionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where description equals to DEFAULT_DESCRIPTION
        defaultFonctionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fonctionList where description equals to UPDATED_DESCRIPTION
        defaultFonctionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFonctionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFonctionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fonctionList where description equals to UPDATED_DESCRIPTION
        defaultFonctionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFonctionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where description is not null
        defaultFonctionShouldBeFound("description.specified=true");

        // Get all the fonctionList where description is null
        defaultFonctionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllFonctionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where description contains DEFAULT_DESCRIPTION
        defaultFonctionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fonctionList where description contains UPDATED_DESCRIPTION
        defaultFonctionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFonctionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctionList where description does not contain DEFAULT_DESCRIPTION
        defaultFonctionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fonctionList where description does not contain UPDATED_DESCRIPTION
        defaultFonctionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllFonctionsByFonctionAdherentIsEqualToSomething() throws Exception {
        FonctionAdherent fonctionAdherent;
        if (TestUtil.findAll(em, FonctionAdherent.class).isEmpty()) {
            fonctionRepository.saveAndFlush(fonction);
            fonctionAdherent = FonctionAdherentResourceIT.createEntity(em);
        } else {
            fonctionAdherent = TestUtil.findAll(em, FonctionAdherent.class).get(0);
        }
        em.persist(fonctionAdherent);
        em.flush();
        fonction.addFonctionAdherent(fonctionAdherent);
        fonctionRepository.saveAndFlush(fonction);
        Long fonctionAdherentId = fonctionAdherent.getId();

        // Get all the fonctionList where fonctionAdherent equals to fonctionAdherentId
        defaultFonctionShouldBeFound("fonctionAdherentId.equals=" + fonctionAdherentId);

        // Get all the fonctionList where fonctionAdherent equals to (fonctionAdherentId + 1)
        defaultFonctionShouldNotBeFound("fonctionAdherentId.equals=" + (fonctionAdherentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFonctionShouldBeFound(String filter) throws Exception {
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFonctionShouldNotBeFound(String filter) throws Exception {
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFonctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFonction() throws Exception {
        // Get the fonction
        restFonctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction
        Fonction updatedFonction = fonctionRepository.findById(fonction.getId()).get();
        // Disconnect from session so that the updates on updatedFonction are not directly saved in db
        em.detach(updatedFonction);
        updatedFonction.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        FonctionDTO fonctionDTO = fonctionMapper.toDto(updatedFonction);

        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFonction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fonctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testFonction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateFonctionWithPatch() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction using partial update
        Fonction partialUpdatedFonction = new Fonction();
        partialUpdatedFonction.setId(fonction.getId());

        partialUpdatedFonction.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFonction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFonction))
            )
            .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctionList.get(fonctionList.size() - 1);
        assertThat(testFonction.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testFonction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fonctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFonction() throws Exception {
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();
        fonction.setId(count.incrementAndGet());

        // Create the Fonction
        FonctionDTO fonctionDTO = fonctionMapper.toDto(fonction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFonctionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fonctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fonction in the database
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        int databaseSizeBeforeDelete = fonctionRepository.findAll().size();

        // Delete the fonction
        restFonctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, fonction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fonction> fonctionList = fonctionRepository.findAll();
        assertThat(fonctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
