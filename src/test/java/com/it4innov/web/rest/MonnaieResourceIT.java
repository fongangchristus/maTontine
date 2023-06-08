package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Association;
import com.it4innov.domain.Monnaie;
import com.it4innov.repository.MonnaieRepository;
import com.it4innov.service.criteria.MonnaieCriteria;
import com.it4innov.service.dto.MonnaieDTO;
import com.it4innov.service.mapper.MonnaieMapper;
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
 * Integration tests for the {@link MonnaieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonnaieResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/monnaies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonnaieRepository monnaieRepository;

    @Autowired
    private MonnaieMapper monnaieMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonnaieMockMvc;

    private Monnaie monnaie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monnaie createEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie().libele(DEFAULT_LIBELE);
        return monnaie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monnaie createUpdatedEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie().libele(UPDATED_LIBELE);
        return monnaie;
    }

    @BeforeEach
    public void initTest() {
        monnaie = createEntity(em);
    }

    @Test
    @Transactional
    void createMonnaie() throws Exception {
        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();
        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);
        restMonnaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaieDTO)))
            .andExpect(status().isCreated());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate + 1);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getLibele()).isEqualTo(DEFAULT_LIBELE);
    }

    @Test
    @Transactional
    void createMonnaieWithExistingId() throws Exception {
        // Create the Monnaie with an existing ID
        monnaie.setId(1L);
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonnaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMonnaies() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)));
    }

    @Test
    @Transactional
    void getMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get the monnaie
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL_ID, monnaie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monnaie.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE));
    }

    @Test
    @Transactional
    void getMonnaiesByIdFiltering() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        Long id = monnaie.getId();

        defaultMonnaieShouldBeFound("id.equals=" + id);
        defaultMonnaieShouldNotBeFound("id.notEquals=" + id);

        defaultMonnaieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonnaieShouldNotBeFound("id.greaterThan=" + id);

        defaultMonnaieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonnaieShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonnaiesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where libele equals to DEFAULT_LIBELE
        defaultMonnaieShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the monnaieList where libele equals to UPDATED_LIBELE
        defaultMonnaieShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultMonnaieShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the monnaieList where libele equals to UPDATED_LIBELE
        defaultMonnaieShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where libele is not null
        defaultMonnaieShouldBeFound("libele.specified=true");

        // Get all the monnaieList where libele is null
        defaultMonnaieShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllMonnaiesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where libele contains DEFAULT_LIBELE
        defaultMonnaieShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the monnaieList where libele contains UPDATED_LIBELE
        defaultMonnaieShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where libele does not contain DEFAULT_LIBELE
        defaultMonnaieShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the monnaieList where libele does not contain UPDATED_LIBELE
        defaultMonnaieShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByAssociationIsEqualToSomething() throws Exception {
        Association association;
        if (TestUtil.findAll(em, Association.class).isEmpty()) {
            monnaieRepository.saveAndFlush(monnaie);
            association = AssociationResourceIT.createEntity(em);
        } else {
            association = TestUtil.findAll(em, Association.class).get(0);
        }
        em.persist(association);
        em.flush();
        monnaie.addAssociation(association);
        monnaieRepository.saveAndFlush(monnaie);
        Long associationId = association.getId();

        // Get all the monnaieList where association equals to associationId
        defaultMonnaieShouldBeFound("associationId.equals=" + associationId);

        // Get all the monnaieList where association equals to (associationId + 1)
        defaultMonnaieShouldNotBeFound("associationId.equals=" + (associationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonnaieShouldBeFound(String filter) throws Exception {
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)));

        // Check, that the count call also returns 1
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonnaieShouldNotBeFound(String filter) throws Exception {
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonnaie() throws Exception {
        // Get the monnaie
        restMonnaieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie
        Monnaie updatedMonnaie = monnaieRepository.findById(monnaie.getId()).get();
        // Disconnect from session so that the updates on updatedMonnaie are not directly saved in db
        em.detach(updatedMonnaie);
        updatedMonnaie.libele(UPDATED_LIBELE);
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(updatedMonnaie);

        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monnaieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getLibele()).isEqualTo(UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void putNonExistingMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monnaieDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonnaieWithPatch() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie using partial update
        Monnaie partialUpdatedMonnaie = new Monnaie();
        partialUpdatedMonnaie.setId(monnaie.getId());

        partialUpdatedMonnaie.libele(UPDATED_LIBELE);

        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonnaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonnaie))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getLibele()).isEqualTo(UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void fullUpdateMonnaieWithPatch() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie using partial update
        Monnaie partialUpdatedMonnaie = new Monnaie();
        partialUpdatedMonnaie.setId(monnaie.getId());

        partialUpdatedMonnaie.libele(UPDATED_LIBELE);

        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonnaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonnaie))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getLibele()).isEqualTo(UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void patchNonExistingMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monnaieDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // Create the Monnaie
        MonnaieDTO monnaieDTO = monnaieMapper.toDto(monnaie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(monnaieDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeDelete = monnaieRepository.findAll().size();

        // Delete the monnaie
        restMonnaieMockMvc
            .perform(delete(ENTITY_API_URL_ID, monnaie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
