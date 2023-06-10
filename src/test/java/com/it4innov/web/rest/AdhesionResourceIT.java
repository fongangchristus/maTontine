package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Adhesion;
import com.it4innov.domain.FormuleAdhesion;
import com.it4innov.domain.PaiementAdhesion;
import com.it4innov.domain.enumeration.StatutAdhesion;
import com.it4innov.repository.AdhesionRepository;
import com.it4innov.service.criteria.AdhesionCriteria;
import com.it4innov.service.dto.AdhesionDTO;
import com.it4innov.service.mapper.AdhesionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AdhesionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdhesionResourceIT {

    private static final StatutAdhesion DEFAULT_STATUT_ADHESION = StatutAdhesion.ACTIVE;
    private static final StatutAdhesion UPDATED_STATUT_ADHESION = StatutAdhesion.DESACTIVE;

    private static final String DEFAULT_MATRICULE_PERSONNE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_PERSONNE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT_ADHESION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT_ADHESION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN_ADHESION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN_ADHESION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/adhesions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdhesionRepository adhesionRepository;

    @Autowired
    private AdhesionMapper adhesionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdhesionMockMvc;

    private Adhesion adhesion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adhesion createEntity(EntityManager em) {
        Adhesion adhesion = new Adhesion()
            .statutAdhesion(DEFAULT_STATUT_ADHESION)
            .matriculePersonne(DEFAULT_MATRICULE_PERSONNE)
            .dateDebutAdhesion(DEFAULT_DATE_DEBUT_ADHESION)
            .dateFinAdhesion(DEFAULT_DATE_FIN_ADHESION);
        return adhesion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adhesion createUpdatedEntity(EntityManager em) {
        Adhesion adhesion = new Adhesion()
            .statutAdhesion(UPDATED_STATUT_ADHESION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .dateDebutAdhesion(UPDATED_DATE_DEBUT_ADHESION)
            .dateFinAdhesion(UPDATED_DATE_FIN_ADHESION);
        return adhesion;
    }

    @BeforeEach
    public void initTest() {
        adhesion = createEntity(em);
    }

    @Test
    @Transactional
    void createAdhesion() throws Exception {
        int databaseSizeBeforeCreate = adhesionRepository.findAll().size();
        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);
        restAdhesionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adhesionDTO)))
            .andExpect(status().isCreated());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeCreate + 1);
        Adhesion testAdhesion = adhesionList.get(adhesionList.size() - 1);
        assertThat(testAdhesion.getStatutAdhesion()).isEqualTo(DEFAULT_STATUT_ADHESION);
        assertThat(testAdhesion.getMatriculePersonne()).isEqualTo(DEFAULT_MATRICULE_PERSONNE);
        assertThat(testAdhesion.getDateDebutAdhesion()).isEqualTo(DEFAULT_DATE_DEBUT_ADHESION);
        assertThat(testAdhesion.getDateFinAdhesion()).isEqualTo(DEFAULT_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void createAdhesionWithExistingId() throws Exception {
        // Create the Adhesion with an existing ID
        adhesion.setId(1L);
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        int databaseSizeBeforeCreate = adhesionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdhesionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adhesionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdhesions() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].statutAdhesion").value(hasItem(DEFAULT_STATUT_ADHESION.toString())))
            .andExpect(jsonPath("$.[*].matriculePersonne").value(hasItem(DEFAULT_MATRICULE_PERSONNE)))
            .andExpect(jsonPath("$.[*].dateDebutAdhesion").value(hasItem(DEFAULT_DATE_DEBUT_ADHESION.toString())))
            .andExpect(jsonPath("$.[*].dateFinAdhesion").value(hasItem(DEFAULT_DATE_FIN_ADHESION.toString())));
    }

    @Test
    @Transactional
    void getAdhesion() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get the adhesion
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL_ID, adhesion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adhesion.getId().intValue()))
            .andExpect(jsonPath("$.statutAdhesion").value(DEFAULT_STATUT_ADHESION.toString()))
            .andExpect(jsonPath("$.matriculePersonne").value(DEFAULT_MATRICULE_PERSONNE))
            .andExpect(jsonPath("$.dateDebutAdhesion").value(DEFAULT_DATE_DEBUT_ADHESION.toString()))
            .andExpect(jsonPath("$.dateFinAdhesion").value(DEFAULT_DATE_FIN_ADHESION.toString()));
    }

    @Test
    @Transactional
    void getAdhesionsByIdFiltering() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        Long id = adhesion.getId();

        defaultAdhesionShouldBeFound("id.equals=" + id);
        defaultAdhesionShouldNotBeFound("id.notEquals=" + id);

        defaultAdhesionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdhesionShouldNotBeFound("id.greaterThan=" + id);

        defaultAdhesionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdhesionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdhesionsByStatutAdhesionIsEqualToSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where statutAdhesion equals to DEFAULT_STATUT_ADHESION
        defaultAdhesionShouldBeFound("statutAdhesion.equals=" + DEFAULT_STATUT_ADHESION);

        // Get all the adhesionList where statutAdhesion equals to UPDATED_STATUT_ADHESION
        defaultAdhesionShouldNotBeFound("statutAdhesion.equals=" + UPDATED_STATUT_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByStatutAdhesionIsInShouldWork() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where statutAdhesion in DEFAULT_STATUT_ADHESION or UPDATED_STATUT_ADHESION
        defaultAdhesionShouldBeFound("statutAdhesion.in=" + DEFAULT_STATUT_ADHESION + "," + UPDATED_STATUT_ADHESION);

        // Get all the adhesionList where statutAdhesion equals to UPDATED_STATUT_ADHESION
        defaultAdhesionShouldNotBeFound("statutAdhesion.in=" + UPDATED_STATUT_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByStatutAdhesionIsNullOrNotNull() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where statutAdhesion is not null
        defaultAdhesionShouldBeFound("statutAdhesion.specified=true");

        // Get all the adhesionList where statutAdhesion is null
        defaultAdhesionShouldNotBeFound("statutAdhesion.specified=false");
    }

    @Test
    @Transactional
    void getAllAdhesionsByMatriculePersonneIsEqualToSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where matriculePersonne equals to DEFAULT_MATRICULE_PERSONNE
        defaultAdhesionShouldBeFound("matriculePersonne.equals=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the adhesionList where matriculePersonne equals to UPDATED_MATRICULE_PERSONNE
        defaultAdhesionShouldNotBeFound("matriculePersonne.equals=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllAdhesionsByMatriculePersonneIsInShouldWork() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where matriculePersonne in DEFAULT_MATRICULE_PERSONNE or UPDATED_MATRICULE_PERSONNE
        defaultAdhesionShouldBeFound("matriculePersonne.in=" + DEFAULT_MATRICULE_PERSONNE + "," + UPDATED_MATRICULE_PERSONNE);

        // Get all the adhesionList where matriculePersonne equals to UPDATED_MATRICULE_PERSONNE
        defaultAdhesionShouldNotBeFound("matriculePersonne.in=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllAdhesionsByMatriculePersonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where matriculePersonne is not null
        defaultAdhesionShouldBeFound("matriculePersonne.specified=true");

        // Get all the adhesionList where matriculePersonne is null
        defaultAdhesionShouldNotBeFound("matriculePersonne.specified=false");
    }

    @Test
    @Transactional
    void getAllAdhesionsByMatriculePersonneContainsSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where matriculePersonne contains DEFAULT_MATRICULE_PERSONNE
        defaultAdhesionShouldBeFound("matriculePersonne.contains=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the adhesionList where matriculePersonne contains UPDATED_MATRICULE_PERSONNE
        defaultAdhesionShouldNotBeFound("matriculePersonne.contains=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllAdhesionsByMatriculePersonneNotContainsSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where matriculePersonne does not contain DEFAULT_MATRICULE_PERSONNE
        defaultAdhesionShouldNotBeFound("matriculePersonne.doesNotContain=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the adhesionList where matriculePersonne does not contain UPDATED_MATRICULE_PERSONNE
        defaultAdhesionShouldBeFound("matriculePersonne.doesNotContain=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateDebutAdhesionIsEqualToSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateDebutAdhesion equals to DEFAULT_DATE_DEBUT_ADHESION
        defaultAdhesionShouldBeFound("dateDebutAdhesion.equals=" + DEFAULT_DATE_DEBUT_ADHESION);

        // Get all the adhesionList where dateDebutAdhesion equals to UPDATED_DATE_DEBUT_ADHESION
        defaultAdhesionShouldNotBeFound("dateDebutAdhesion.equals=" + UPDATED_DATE_DEBUT_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateDebutAdhesionIsInShouldWork() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateDebutAdhesion in DEFAULT_DATE_DEBUT_ADHESION or UPDATED_DATE_DEBUT_ADHESION
        defaultAdhesionShouldBeFound("dateDebutAdhesion.in=" + DEFAULT_DATE_DEBUT_ADHESION + "," + UPDATED_DATE_DEBUT_ADHESION);

        // Get all the adhesionList where dateDebutAdhesion equals to UPDATED_DATE_DEBUT_ADHESION
        defaultAdhesionShouldNotBeFound("dateDebutAdhesion.in=" + UPDATED_DATE_DEBUT_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateDebutAdhesionIsNullOrNotNull() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateDebutAdhesion is not null
        defaultAdhesionShouldBeFound("dateDebutAdhesion.specified=true");

        // Get all the adhesionList where dateDebutAdhesion is null
        defaultAdhesionShouldNotBeFound("dateDebutAdhesion.specified=false");
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateFinAdhesionIsEqualToSomething() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateFinAdhesion equals to DEFAULT_DATE_FIN_ADHESION
        defaultAdhesionShouldBeFound("dateFinAdhesion.equals=" + DEFAULT_DATE_FIN_ADHESION);

        // Get all the adhesionList where dateFinAdhesion equals to UPDATED_DATE_FIN_ADHESION
        defaultAdhesionShouldNotBeFound("dateFinAdhesion.equals=" + UPDATED_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateFinAdhesionIsInShouldWork() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateFinAdhesion in DEFAULT_DATE_FIN_ADHESION or UPDATED_DATE_FIN_ADHESION
        defaultAdhesionShouldBeFound("dateFinAdhesion.in=" + DEFAULT_DATE_FIN_ADHESION + "," + UPDATED_DATE_FIN_ADHESION);

        // Get all the adhesionList where dateFinAdhesion equals to UPDATED_DATE_FIN_ADHESION
        defaultAdhesionShouldNotBeFound("dateFinAdhesion.in=" + UPDATED_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void getAllAdhesionsByDateFinAdhesionIsNullOrNotNull() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        // Get all the adhesionList where dateFinAdhesion is not null
        defaultAdhesionShouldBeFound("dateFinAdhesion.specified=true");

        // Get all the adhesionList where dateFinAdhesion is null
        defaultAdhesionShouldNotBeFound("dateFinAdhesion.specified=false");
    }

    @Test
    @Transactional
    void getAllAdhesionsByFormuleAdhesionIsEqualToSomething() throws Exception {
        FormuleAdhesion formuleAdhesion;
        if (TestUtil.findAll(em, FormuleAdhesion.class).isEmpty()) {
            adhesionRepository.saveAndFlush(adhesion);
            formuleAdhesion = FormuleAdhesionResourceIT.createEntity(em);
        } else {
            formuleAdhesion = TestUtil.findAll(em, FormuleAdhesion.class).get(0);
        }
        em.persist(formuleAdhesion);
        em.flush();
        adhesion.addFormuleAdhesion(formuleAdhesion);
        adhesionRepository.saveAndFlush(adhesion);
        Long formuleAdhesionId = formuleAdhesion.getId();

        // Get all the adhesionList where formuleAdhesion equals to formuleAdhesionId
        defaultAdhesionShouldBeFound("formuleAdhesionId.equals=" + formuleAdhesionId);

        // Get all the adhesionList where formuleAdhesion equals to (formuleAdhesionId + 1)
        defaultAdhesionShouldNotBeFound("formuleAdhesionId.equals=" + (formuleAdhesionId + 1));
    }

    @Test
    @Transactional
    void getAllAdhesionsByPaiementAdhesionIsEqualToSomething() throws Exception {
        PaiementAdhesion paiementAdhesion;
        if (TestUtil.findAll(em, PaiementAdhesion.class).isEmpty()) {
            adhesionRepository.saveAndFlush(adhesion);
            paiementAdhesion = PaiementAdhesionResourceIT.createEntity(em);
        } else {
            paiementAdhesion = TestUtil.findAll(em, PaiementAdhesion.class).get(0);
        }
        em.persist(paiementAdhesion);
        em.flush();
        adhesion.addPaiementAdhesion(paiementAdhesion);
        adhesionRepository.saveAndFlush(adhesion);
        Long paiementAdhesionId = paiementAdhesion.getId();

        // Get all the adhesionList where paiementAdhesion equals to paiementAdhesionId
        defaultAdhesionShouldBeFound("paiementAdhesionId.equals=" + paiementAdhesionId);

        // Get all the adhesionList where paiementAdhesion equals to (paiementAdhesionId + 1)
        defaultAdhesionShouldNotBeFound("paiementAdhesionId.equals=" + (paiementAdhesionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdhesionShouldBeFound(String filter) throws Exception {
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adhesion.getId().intValue())))
            .andExpect(jsonPath("$.[*].statutAdhesion").value(hasItem(DEFAULT_STATUT_ADHESION.toString())))
            .andExpect(jsonPath("$.[*].matriculePersonne").value(hasItem(DEFAULT_MATRICULE_PERSONNE)))
            .andExpect(jsonPath("$.[*].dateDebutAdhesion").value(hasItem(DEFAULT_DATE_DEBUT_ADHESION.toString())))
            .andExpect(jsonPath("$.[*].dateFinAdhesion").value(hasItem(DEFAULT_DATE_FIN_ADHESION.toString())));

        // Check, that the count call also returns 1
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdhesionShouldNotBeFound(String filter) throws Exception {
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdhesionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdhesion() throws Exception {
        // Get the adhesion
        restAdhesionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdhesion() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();

        // Update the adhesion
        Adhesion updatedAdhesion = adhesionRepository.findById(adhesion.getId()).get();
        // Disconnect from session so that the updates on updatedAdhesion are not directly saved in db
        em.detach(updatedAdhesion);
        updatedAdhesion
            .statutAdhesion(UPDATED_STATUT_ADHESION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .dateDebutAdhesion(UPDATED_DATE_DEBUT_ADHESION)
            .dateFinAdhesion(UPDATED_DATE_FIN_ADHESION);
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(updatedAdhesion);

        restAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
        Adhesion testAdhesion = adhesionList.get(adhesionList.size() - 1);
        assertThat(testAdhesion.getStatutAdhesion()).isEqualTo(UPDATED_STATUT_ADHESION);
        assertThat(testAdhesion.getMatriculePersonne()).isEqualTo(UPDATED_MATRICULE_PERSONNE);
        assertThat(testAdhesion.getDateDebutAdhesion()).isEqualTo(UPDATED_DATE_DEBUT_ADHESION);
        assertThat(testAdhesion.getDateFinAdhesion()).isEqualTo(UPDATED_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void putNonExistingAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adhesionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adhesionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdhesionWithPatch() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();

        // Update the adhesion using partial update
        Adhesion partialUpdatedAdhesion = new Adhesion();
        partialUpdatedAdhesion.setId(adhesion.getId());

        partialUpdatedAdhesion.dateDebutAdhesion(UPDATED_DATE_DEBUT_ADHESION);

        restAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
        Adhesion testAdhesion = adhesionList.get(adhesionList.size() - 1);
        assertThat(testAdhesion.getStatutAdhesion()).isEqualTo(DEFAULT_STATUT_ADHESION);
        assertThat(testAdhesion.getMatriculePersonne()).isEqualTo(DEFAULT_MATRICULE_PERSONNE);
        assertThat(testAdhesion.getDateDebutAdhesion()).isEqualTo(UPDATED_DATE_DEBUT_ADHESION);
        assertThat(testAdhesion.getDateFinAdhesion()).isEqualTo(DEFAULT_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void fullUpdateAdhesionWithPatch() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();

        // Update the adhesion using partial update
        Adhesion partialUpdatedAdhesion = new Adhesion();
        partialUpdatedAdhesion.setId(adhesion.getId());

        partialUpdatedAdhesion
            .statutAdhesion(UPDATED_STATUT_ADHESION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .dateDebutAdhesion(UPDATED_DATE_DEBUT_ADHESION)
            .dateFinAdhesion(UPDATED_DATE_FIN_ADHESION);

        restAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdhesion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdhesion))
            )
            .andExpect(status().isOk());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
        Adhesion testAdhesion = adhesionList.get(adhesionList.size() - 1);
        assertThat(testAdhesion.getStatutAdhesion()).isEqualTo(UPDATED_STATUT_ADHESION);
        assertThat(testAdhesion.getMatriculePersonne()).isEqualTo(UPDATED_MATRICULE_PERSONNE);
        assertThat(testAdhesion.getDateDebutAdhesion()).isEqualTo(UPDATED_DATE_DEBUT_ADHESION);
        assertThat(testAdhesion.getDateFinAdhesion()).isEqualTo(UPDATED_DATE_FIN_ADHESION);
    }

    @Test
    @Transactional
    void patchNonExistingAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adhesionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdhesion() throws Exception {
        int databaseSizeBeforeUpdate = adhesionRepository.findAll().size();
        adhesion.setId(count.incrementAndGet());

        // Create the Adhesion
        AdhesionDTO adhesionDTO = adhesionMapper.toDto(adhesion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdhesionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adhesionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adhesion in the database
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdhesion() throws Exception {
        // Initialize the database
        adhesionRepository.saveAndFlush(adhesion);

        int databaseSizeBeforeDelete = adhesionRepository.findAll().size();

        // Delete the adhesion
        restAdhesionMockMvc
            .perform(delete(ENTITY_API_URL_ID, adhesion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adhesion> adhesionList = adhesionRepository.findAll();
        assertThat(adhesionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
