package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteRIB;
import com.it4innov.domain.Personne;
import com.it4innov.repository.CompteRIBRepository;
import com.it4innov.service.criteria.CompteRIBCriteria;
import com.it4innov.service.dto.CompteRIBDTO;
import com.it4innov.service.mapper.CompteRIBMapper;
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
 * Integration tests for the {@link CompteRIBResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteRIBResourceIT {

    private static final String DEFAULT_IBAN = "AAAAAAAAAA";
    private static final String UPDATED_IBAN = "BBBBBBBBBB";

    private static final String DEFAULT_TITULAIRE_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_TITULAIRE_COMPTE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VERIFIER = false;
    private static final Boolean UPDATED_VERIFIER = true;

    private static final String ENTITY_API_URL = "/api/compte-ribs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteRIBRepository compteRIBRepository;

    @Autowired
    private CompteRIBMapper compteRIBMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteRIBMockMvc;

    private CompteRIB compteRIB;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteRIB createEntity(EntityManager em) {
        CompteRIB compteRIB = new CompteRIB().iban(DEFAULT_IBAN).titulaireCompte(DEFAULT_TITULAIRE_COMPTE).verifier(DEFAULT_VERIFIER);
        return compteRIB;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteRIB createUpdatedEntity(EntityManager em) {
        CompteRIB compteRIB = new CompteRIB().iban(UPDATED_IBAN).titulaireCompte(UPDATED_TITULAIRE_COMPTE).verifier(UPDATED_VERIFIER);
        return compteRIB;
    }

    @BeforeEach
    public void initTest() {
        compteRIB = createEntity(em);
    }

    @Test
    @Transactional
    void createCompteRIB() throws Exception {
        int databaseSizeBeforeCreate = compteRIBRepository.findAll().size();
        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);
        restCompteRIBMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteRIBDTO)))
            .andExpect(status().isCreated());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeCreate + 1);
        CompteRIB testCompteRIB = compteRIBList.get(compteRIBList.size() - 1);
        assertThat(testCompteRIB.getIban()).isEqualTo(DEFAULT_IBAN);
        assertThat(testCompteRIB.getTitulaireCompte()).isEqualTo(DEFAULT_TITULAIRE_COMPTE);
        assertThat(testCompteRIB.getVerifier()).isEqualTo(DEFAULT_VERIFIER);
    }

    @Test
    @Transactional
    void createCompteRIBWithExistingId() throws Exception {
        // Create the CompteRIB with an existing ID
        compteRIB.setId(1L);
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        int databaseSizeBeforeCreate = compteRIBRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteRIBMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteRIBDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompteRIBS() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteRIB.getId().intValue())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].titulaireCompte").value(hasItem(DEFAULT_TITULAIRE_COMPTE)))
            .andExpect(jsonPath("$.[*].verifier").value(hasItem(DEFAULT_VERIFIER.booleanValue())));
    }

    @Test
    @Transactional
    void getCompteRIB() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get the compteRIB
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL_ID, compteRIB.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteRIB.getId().intValue()))
            .andExpect(jsonPath("$.iban").value(DEFAULT_IBAN))
            .andExpect(jsonPath("$.titulaireCompte").value(DEFAULT_TITULAIRE_COMPTE))
            .andExpect(jsonPath("$.verifier").value(DEFAULT_VERIFIER.booleanValue()));
    }

    @Test
    @Transactional
    void getCompteRIBSByIdFiltering() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        Long id = compteRIB.getId();

        defaultCompteRIBShouldBeFound("id.equals=" + id);
        defaultCompteRIBShouldNotBeFound("id.notEquals=" + id);

        defaultCompteRIBShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompteRIBShouldNotBeFound("id.greaterThan=" + id);

        defaultCompteRIBShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompteRIBShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByIbanIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where iban equals to DEFAULT_IBAN
        defaultCompteRIBShouldBeFound("iban.equals=" + DEFAULT_IBAN);

        // Get all the compteRIBList where iban equals to UPDATED_IBAN
        defaultCompteRIBShouldNotBeFound("iban.equals=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByIbanIsInShouldWork() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where iban in DEFAULT_IBAN or UPDATED_IBAN
        defaultCompteRIBShouldBeFound("iban.in=" + DEFAULT_IBAN + "," + UPDATED_IBAN);

        // Get all the compteRIBList where iban equals to UPDATED_IBAN
        defaultCompteRIBShouldNotBeFound("iban.in=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByIbanIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where iban is not null
        defaultCompteRIBShouldBeFound("iban.specified=true");

        // Get all the compteRIBList where iban is null
        defaultCompteRIBShouldNotBeFound("iban.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteRIBSByIbanContainsSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where iban contains DEFAULT_IBAN
        defaultCompteRIBShouldBeFound("iban.contains=" + DEFAULT_IBAN);

        // Get all the compteRIBList where iban contains UPDATED_IBAN
        defaultCompteRIBShouldNotBeFound("iban.contains=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByIbanNotContainsSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where iban does not contain DEFAULT_IBAN
        defaultCompteRIBShouldNotBeFound("iban.doesNotContain=" + DEFAULT_IBAN);

        // Get all the compteRIBList where iban does not contain UPDATED_IBAN
        defaultCompteRIBShouldBeFound("iban.doesNotContain=" + UPDATED_IBAN);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByTitulaireCompteIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where titulaireCompte equals to DEFAULT_TITULAIRE_COMPTE
        defaultCompteRIBShouldBeFound("titulaireCompte.equals=" + DEFAULT_TITULAIRE_COMPTE);

        // Get all the compteRIBList where titulaireCompte equals to UPDATED_TITULAIRE_COMPTE
        defaultCompteRIBShouldNotBeFound("titulaireCompte.equals=" + UPDATED_TITULAIRE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByTitulaireCompteIsInShouldWork() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where titulaireCompte in DEFAULT_TITULAIRE_COMPTE or UPDATED_TITULAIRE_COMPTE
        defaultCompteRIBShouldBeFound("titulaireCompte.in=" + DEFAULT_TITULAIRE_COMPTE + "," + UPDATED_TITULAIRE_COMPTE);

        // Get all the compteRIBList where titulaireCompte equals to UPDATED_TITULAIRE_COMPTE
        defaultCompteRIBShouldNotBeFound("titulaireCompte.in=" + UPDATED_TITULAIRE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByTitulaireCompteIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where titulaireCompte is not null
        defaultCompteRIBShouldBeFound("titulaireCompte.specified=true");

        // Get all the compteRIBList where titulaireCompte is null
        defaultCompteRIBShouldNotBeFound("titulaireCompte.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteRIBSByTitulaireCompteContainsSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where titulaireCompte contains DEFAULT_TITULAIRE_COMPTE
        defaultCompteRIBShouldBeFound("titulaireCompte.contains=" + DEFAULT_TITULAIRE_COMPTE);

        // Get all the compteRIBList where titulaireCompte contains UPDATED_TITULAIRE_COMPTE
        defaultCompteRIBShouldNotBeFound("titulaireCompte.contains=" + UPDATED_TITULAIRE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByTitulaireCompteNotContainsSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where titulaireCompte does not contain DEFAULT_TITULAIRE_COMPTE
        defaultCompteRIBShouldNotBeFound("titulaireCompte.doesNotContain=" + DEFAULT_TITULAIRE_COMPTE);

        // Get all the compteRIBList where titulaireCompte does not contain UPDATED_TITULAIRE_COMPTE
        defaultCompteRIBShouldBeFound("titulaireCompte.doesNotContain=" + UPDATED_TITULAIRE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByVerifierIsEqualToSomething() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where verifier equals to DEFAULT_VERIFIER
        defaultCompteRIBShouldBeFound("verifier.equals=" + DEFAULT_VERIFIER);

        // Get all the compteRIBList where verifier equals to UPDATED_VERIFIER
        defaultCompteRIBShouldNotBeFound("verifier.equals=" + UPDATED_VERIFIER);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByVerifierIsInShouldWork() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where verifier in DEFAULT_VERIFIER or UPDATED_VERIFIER
        defaultCompteRIBShouldBeFound("verifier.in=" + DEFAULT_VERIFIER + "," + UPDATED_VERIFIER);

        // Get all the compteRIBList where verifier equals to UPDATED_VERIFIER
        defaultCompteRIBShouldNotBeFound("verifier.in=" + UPDATED_VERIFIER);
    }

    @Test
    @Transactional
    void getAllCompteRIBSByVerifierIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        // Get all the compteRIBList where verifier is not null
        defaultCompteRIBShouldBeFound("verifier.specified=true");

        // Get all the compteRIBList where verifier is null
        defaultCompteRIBShouldNotBeFound("verifier.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteRIBSByAdherentIsEqualToSomething() throws Exception {
        Personne adherent;
        if (TestUtil.findAll(em, Personne.class).isEmpty()) {
            compteRIBRepository.saveAndFlush(compteRIB);
            adherent = PersonneResourceIT.createEntity(em);
        } else {
            adherent = TestUtil.findAll(em, Personne.class).get(0);
        }
        em.persist(adherent);
        em.flush();
        compteRIB.setAdherent(adherent);
        compteRIBRepository.saveAndFlush(compteRIB);
        Long adherentId = adherent.getId();

        // Get all the compteRIBList where adherent equals to adherentId
        defaultCompteRIBShouldBeFound("adherentId.equals=" + adherentId);

        // Get all the compteRIBList where adherent equals to (adherentId + 1)
        defaultCompteRIBShouldNotBeFound("adherentId.equals=" + (adherentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompteRIBShouldBeFound(String filter) throws Exception {
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteRIB.getId().intValue())))
            .andExpect(jsonPath("$.[*].iban").value(hasItem(DEFAULT_IBAN)))
            .andExpect(jsonPath("$.[*].titulaireCompte").value(hasItem(DEFAULT_TITULAIRE_COMPTE)))
            .andExpect(jsonPath("$.[*].verifier").value(hasItem(DEFAULT_VERIFIER.booleanValue())));

        // Check, that the count call also returns 1
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompteRIBShouldNotBeFound(String filter) throws Exception {
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompteRIBMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompteRIB() throws Exception {
        // Get the compteRIB
        restCompteRIBMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompteRIB() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();

        // Update the compteRIB
        CompteRIB updatedCompteRIB = compteRIBRepository.findById(compteRIB.getId()).get();
        // Disconnect from session so that the updates on updatedCompteRIB are not directly saved in db
        em.detach(updatedCompteRIB);
        updatedCompteRIB.iban(UPDATED_IBAN).titulaireCompte(UPDATED_TITULAIRE_COMPTE).verifier(UPDATED_VERIFIER);
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(updatedCompteRIB);

        restCompteRIBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteRIBDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
        CompteRIB testCompteRIB = compteRIBList.get(compteRIBList.size() - 1);
        assertThat(testCompteRIB.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCompteRIB.getTitulaireCompte()).isEqualTo(UPDATED_TITULAIRE_COMPTE);
        assertThat(testCompteRIB.getVerifier()).isEqualTo(UPDATED_VERIFIER);
    }

    @Test
    @Transactional
    void putNonExistingCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteRIBDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteRIBDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteRIBWithPatch() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();

        // Update the compteRIB using partial update
        CompteRIB partialUpdatedCompteRIB = new CompteRIB();
        partialUpdatedCompteRIB.setId(compteRIB.getId());

        partialUpdatedCompteRIB.iban(UPDATED_IBAN);

        restCompteRIBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteRIB.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteRIB))
            )
            .andExpect(status().isOk());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
        CompteRIB testCompteRIB = compteRIBList.get(compteRIBList.size() - 1);
        assertThat(testCompteRIB.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCompteRIB.getTitulaireCompte()).isEqualTo(DEFAULT_TITULAIRE_COMPTE);
        assertThat(testCompteRIB.getVerifier()).isEqualTo(DEFAULT_VERIFIER);
    }

    @Test
    @Transactional
    void fullUpdateCompteRIBWithPatch() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();

        // Update the compteRIB using partial update
        CompteRIB partialUpdatedCompteRIB = new CompteRIB();
        partialUpdatedCompteRIB.setId(compteRIB.getId());

        partialUpdatedCompteRIB.iban(UPDATED_IBAN).titulaireCompte(UPDATED_TITULAIRE_COMPTE).verifier(UPDATED_VERIFIER);

        restCompteRIBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteRIB.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteRIB))
            )
            .andExpect(status().isOk());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
        CompteRIB testCompteRIB = compteRIBList.get(compteRIBList.size() - 1);
        assertThat(testCompteRIB.getIban()).isEqualTo(UPDATED_IBAN);
        assertThat(testCompteRIB.getTitulaireCompte()).isEqualTo(UPDATED_TITULAIRE_COMPTE);
        assertThat(testCompteRIB.getVerifier()).isEqualTo(UPDATED_VERIFIER);
    }

    @Test
    @Transactional
    void patchNonExistingCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteRIBDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteRIB() throws Exception {
        int databaseSizeBeforeUpdate = compteRIBRepository.findAll().size();
        compteRIB.setId(count.incrementAndGet());

        // Create the CompteRIB
        CompteRIBDTO compteRIBDTO = compteRIBMapper.toDto(compteRIB);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteRIBMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compteRIBDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteRIB in the database
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteRIB() throws Exception {
        // Initialize the database
        compteRIBRepository.saveAndFlush(compteRIB);

        int databaseSizeBeforeDelete = compteRIBRepository.findAll().size();

        // Delete the compteRIB
        restCompteRIBMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteRIB.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompteRIB> compteRIBList = compteRIBRepository.findAll();
        assertThat(compteRIBList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
