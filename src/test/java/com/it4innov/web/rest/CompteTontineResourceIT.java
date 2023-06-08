package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.CotisationTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.Tontine;
import com.it4innov.repository.CompteTontineRepository;
import com.it4innov.service.criteria.CompteTontineCriteria;
import com.it4innov.service.dto.CompteTontineDTO;
import com.it4innov.service.mapper.CompteTontineMapper;
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
 * Integration tests for the {@link CompteTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteTontineResourceIT {

    private static final Boolean DEFAULT_ETAT_DE_COMPTE = false;
    private static final Boolean UPDATED_ETAT_DE_COMPTE = true;

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ODRE_BENEFICIERE = 1;
    private static final Integer UPDATED_ODRE_BENEFICIERE = 2;
    private static final Integer SMALLER_ODRE_BENEFICIERE = 1 - 1;

    private static final String DEFAULT_MATRICULE_COMPTE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_COMPTE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_MENBRE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_MENBRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compte-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteTontineRepository compteTontineRepository;

    @Autowired
    private CompteTontineMapper compteTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteTontineMockMvc;

    private CompteTontine compteTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteTontine createEntity(EntityManager em) {
        CompteTontine compteTontine = new CompteTontine()
            .etatDeCompte(DEFAULT_ETAT_DE_COMPTE)
            .libele(DEFAULT_LIBELE)
            .odreBeneficiere(DEFAULT_ODRE_BENEFICIERE)
            .matriculeCompte(DEFAULT_MATRICULE_COMPTE)
            .matriculeMenbre(DEFAULT_MATRICULE_MENBRE);
        return compteTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteTontine createUpdatedEntity(EntityManager em) {
        CompteTontine compteTontine = new CompteTontine()
            .etatDeCompte(UPDATED_ETAT_DE_COMPTE)
            .libele(UPDATED_LIBELE)
            .odreBeneficiere(UPDATED_ODRE_BENEFICIERE)
            .matriculeCompte(UPDATED_MATRICULE_COMPTE)
            .matriculeMenbre(UPDATED_MATRICULE_MENBRE);
        return compteTontine;
    }

    @BeforeEach
    public void initTest() {
        compteTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createCompteTontine() throws Exception {
        int databaseSizeBeforeCreate = compteTontineRepository.findAll().size();
        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);
        restCompteTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeCreate + 1);
        CompteTontine testCompteTontine = compteTontineList.get(compteTontineList.size() - 1);
        assertThat(testCompteTontine.getEtatDeCompte()).isEqualTo(DEFAULT_ETAT_DE_COMPTE);
        assertThat(testCompteTontine.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testCompteTontine.getOdreBeneficiere()).isEqualTo(DEFAULT_ODRE_BENEFICIERE);
        assertThat(testCompteTontine.getMatriculeCompte()).isEqualTo(DEFAULT_MATRICULE_COMPTE);
        assertThat(testCompteTontine.getMatriculeMenbre()).isEqualTo(DEFAULT_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void createCompteTontineWithExistingId() throws Exception {
        // Create the CompteTontine with an existing ID
        compteTontine.setId(1L);
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        int databaseSizeBeforeCreate = compteTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeCompteIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteTontineRepository.findAll().size();
        // set the field null
        compteTontine.setMatriculeCompte(null);

        // Create the CompteTontine, which fails.
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        restCompteTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculeMenbreIsRequired() throws Exception {
        int databaseSizeBeforeTest = compteTontineRepository.findAll().size();
        // set the field null
        compteTontine.setMatriculeMenbre(null);

        // Create the CompteTontine, which fails.
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        restCompteTontineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompteTontines() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatDeCompte").value(hasItem(DEFAULT_ETAT_DE_COMPTE.booleanValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].odreBeneficiere").value(hasItem(DEFAULT_ODRE_BENEFICIERE)))
            .andExpect(jsonPath("$.[*].matriculeCompte").value(hasItem(DEFAULT_MATRICULE_COMPTE)))
            .andExpect(jsonPath("$.[*].matriculeMenbre").value(hasItem(DEFAULT_MATRICULE_MENBRE)));
    }

    @Test
    @Transactional
    void getCompteTontine() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get the compteTontine
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, compteTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteTontine.getId().intValue()))
            .andExpect(jsonPath("$.etatDeCompte").value(DEFAULT_ETAT_DE_COMPTE.booleanValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.odreBeneficiere").value(DEFAULT_ODRE_BENEFICIERE))
            .andExpect(jsonPath("$.matriculeCompte").value(DEFAULT_MATRICULE_COMPTE))
            .andExpect(jsonPath("$.matriculeMenbre").value(DEFAULT_MATRICULE_MENBRE));
    }

    @Test
    @Transactional
    void getCompteTontinesByIdFiltering() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        Long id = compteTontine.getId();

        defaultCompteTontineShouldBeFound("id.equals=" + id);
        defaultCompteTontineShouldNotBeFound("id.notEquals=" + id);

        defaultCompteTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompteTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultCompteTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompteTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByEtatDeCompteIsEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where etatDeCompte equals to DEFAULT_ETAT_DE_COMPTE
        defaultCompteTontineShouldBeFound("etatDeCompte.equals=" + DEFAULT_ETAT_DE_COMPTE);

        // Get all the compteTontineList where etatDeCompte equals to UPDATED_ETAT_DE_COMPTE
        defaultCompteTontineShouldNotBeFound("etatDeCompte.equals=" + UPDATED_ETAT_DE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByEtatDeCompteIsInShouldWork() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where etatDeCompte in DEFAULT_ETAT_DE_COMPTE or UPDATED_ETAT_DE_COMPTE
        defaultCompteTontineShouldBeFound("etatDeCompte.in=" + DEFAULT_ETAT_DE_COMPTE + "," + UPDATED_ETAT_DE_COMPTE);

        // Get all the compteTontineList where etatDeCompte equals to UPDATED_ETAT_DE_COMPTE
        defaultCompteTontineShouldNotBeFound("etatDeCompte.in=" + UPDATED_ETAT_DE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByEtatDeCompteIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where etatDeCompte is not null
        defaultCompteTontineShouldBeFound("etatDeCompte.specified=true");

        // Get all the compteTontineList where etatDeCompte is null
        defaultCompteTontineShouldNotBeFound("etatDeCompte.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteTontinesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where libele equals to DEFAULT_LIBELE
        defaultCompteTontineShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the compteTontineList where libele equals to UPDATED_LIBELE
        defaultCompteTontineShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultCompteTontineShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the compteTontineList where libele equals to UPDATED_LIBELE
        defaultCompteTontineShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where libele is not null
        defaultCompteTontineShouldBeFound("libele.specified=true");

        // Get all the compteTontineList where libele is null
        defaultCompteTontineShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteTontinesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where libele contains DEFAULT_LIBELE
        defaultCompteTontineShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the compteTontineList where libele contains UPDATED_LIBELE
        defaultCompteTontineShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where libele does not contain DEFAULT_LIBELE
        defaultCompteTontineShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the compteTontineList where libele does not contain UPDATED_LIBELE
        defaultCompteTontineShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere equals to DEFAULT_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.equals=" + DEFAULT_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere equals to UPDATED_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.equals=" + UPDATED_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsInShouldWork() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere in DEFAULT_ODRE_BENEFICIERE or UPDATED_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.in=" + DEFAULT_ODRE_BENEFICIERE + "," + UPDATED_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere equals to UPDATED_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.in=" + UPDATED_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere is not null
        defaultCompteTontineShouldBeFound("odreBeneficiere.specified=true");

        // Get all the compteTontineList where odreBeneficiere is null
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere is greater than or equal to DEFAULT_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.greaterThanOrEqual=" + DEFAULT_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere is greater than or equal to UPDATED_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.greaterThanOrEqual=" + UPDATED_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere is less than or equal to DEFAULT_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.lessThanOrEqual=" + DEFAULT_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere is less than or equal to SMALLER_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.lessThanOrEqual=" + SMALLER_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsLessThanSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere is less than DEFAULT_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.lessThan=" + DEFAULT_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere is less than UPDATED_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.lessThan=" + UPDATED_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByOdreBeneficiereIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where odreBeneficiere is greater than DEFAULT_ODRE_BENEFICIERE
        defaultCompteTontineShouldNotBeFound("odreBeneficiere.greaterThan=" + DEFAULT_ODRE_BENEFICIERE);

        // Get all the compteTontineList where odreBeneficiere is greater than SMALLER_ODRE_BENEFICIERE
        defaultCompteTontineShouldBeFound("odreBeneficiere.greaterThan=" + SMALLER_ODRE_BENEFICIERE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeCompteIsEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeCompte equals to DEFAULT_MATRICULE_COMPTE
        defaultCompteTontineShouldBeFound("matriculeCompte.equals=" + DEFAULT_MATRICULE_COMPTE);

        // Get all the compteTontineList where matriculeCompte equals to UPDATED_MATRICULE_COMPTE
        defaultCompteTontineShouldNotBeFound("matriculeCompte.equals=" + UPDATED_MATRICULE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeCompteIsInShouldWork() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeCompte in DEFAULT_MATRICULE_COMPTE or UPDATED_MATRICULE_COMPTE
        defaultCompteTontineShouldBeFound("matriculeCompte.in=" + DEFAULT_MATRICULE_COMPTE + "," + UPDATED_MATRICULE_COMPTE);

        // Get all the compteTontineList where matriculeCompte equals to UPDATED_MATRICULE_COMPTE
        defaultCompteTontineShouldNotBeFound("matriculeCompte.in=" + UPDATED_MATRICULE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeCompteIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeCompte is not null
        defaultCompteTontineShouldBeFound("matriculeCompte.specified=true");

        // Get all the compteTontineList where matriculeCompte is null
        defaultCompteTontineShouldNotBeFound("matriculeCompte.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeCompteContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeCompte contains DEFAULT_MATRICULE_COMPTE
        defaultCompteTontineShouldBeFound("matriculeCompte.contains=" + DEFAULT_MATRICULE_COMPTE);

        // Get all the compteTontineList where matriculeCompte contains UPDATED_MATRICULE_COMPTE
        defaultCompteTontineShouldNotBeFound("matriculeCompte.contains=" + UPDATED_MATRICULE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeCompteNotContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeCompte does not contain DEFAULT_MATRICULE_COMPTE
        defaultCompteTontineShouldNotBeFound("matriculeCompte.doesNotContain=" + DEFAULT_MATRICULE_COMPTE);

        // Get all the compteTontineList where matriculeCompte does not contain UPDATED_MATRICULE_COMPTE
        defaultCompteTontineShouldBeFound("matriculeCompte.doesNotContain=" + UPDATED_MATRICULE_COMPTE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeMenbreIsEqualToSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeMenbre equals to DEFAULT_MATRICULE_MENBRE
        defaultCompteTontineShouldBeFound("matriculeMenbre.equals=" + DEFAULT_MATRICULE_MENBRE);

        // Get all the compteTontineList where matriculeMenbre equals to UPDATED_MATRICULE_MENBRE
        defaultCompteTontineShouldNotBeFound("matriculeMenbre.equals=" + UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeMenbreIsInShouldWork() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeMenbre in DEFAULT_MATRICULE_MENBRE or UPDATED_MATRICULE_MENBRE
        defaultCompteTontineShouldBeFound("matriculeMenbre.in=" + DEFAULT_MATRICULE_MENBRE + "," + UPDATED_MATRICULE_MENBRE);

        // Get all the compteTontineList where matriculeMenbre equals to UPDATED_MATRICULE_MENBRE
        defaultCompteTontineShouldNotBeFound("matriculeMenbre.in=" + UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeMenbreIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeMenbre is not null
        defaultCompteTontineShouldBeFound("matriculeMenbre.specified=true");

        // Get all the compteTontineList where matriculeMenbre is null
        defaultCompteTontineShouldNotBeFound("matriculeMenbre.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeMenbreContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeMenbre contains DEFAULT_MATRICULE_MENBRE
        defaultCompteTontineShouldBeFound("matriculeMenbre.contains=" + DEFAULT_MATRICULE_MENBRE);

        // Get all the compteTontineList where matriculeMenbre contains UPDATED_MATRICULE_MENBRE
        defaultCompteTontineShouldNotBeFound("matriculeMenbre.contains=" + UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByMatriculeMenbreNotContainsSomething() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        // Get all the compteTontineList where matriculeMenbre does not contain DEFAULT_MATRICULE_MENBRE
        defaultCompteTontineShouldNotBeFound("matriculeMenbre.doesNotContain=" + DEFAULT_MATRICULE_MENBRE);

        // Get all the compteTontineList where matriculeMenbre does not contain UPDATED_MATRICULE_MENBRE
        defaultCompteTontineShouldBeFound("matriculeMenbre.doesNotContain=" + UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void getAllCompteTontinesByTontineIsEqualToSomething() throws Exception {
        Tontine tontine;
        if (TestUtil.findAll(em, Tontine.class).isEmpty()) {
            compteTontineRepository.saveAndFlush(compteTontine);
            tontine = TontineResourceIT.createEntity(em);
        } else {
            tontine = TestUtil.findAll(em, Tontine.class).get(0);
        }
        em.persist(tontine);
        em.flush();
        compteTontine.setTontine(tontine);
        compteTontineRepository.saveAndFlush(compteTontine);
        Long tontineId = tontine.getId();

        // Get all the compteTontineList where tontine equals to tontineId
        defaultCompteTontineShouldBeFound("tontineId.equals=" + tontineId);

        // Get all the compteTontineList where tontine equals to (tontineId + 1)
        defaultCompteTontineShouldNotBeFound("tontineId.equals=" + (tontineId + 1));
    }

    @Test
    @Transactional
    void getAllCompteTontinesByCotisationTontineIsEqualToSomething() throws Exception {
        CotisationTontine cotisationTontine;
        if (TestUtil.findAll(em, CotisationTontine.class).isEmpty()) {
            compteTontineRepository.saveAndFlush(compteTontine);
            cotisationTontine = CotisationTontineResourceIT.createEntity(em);
        } else {
            cotisationTontine = TestUtil.findAll(em, CotisationTontine.class).get(0);
        }
        em.persist(cotisationTontine);
        em.flush();
        compteTontine.addCotisationTontine(cotisationTontine);
        compteTontineRepository.saveAndFlush(compteTontine);
        Long cotisationTontineId = cotisationTontine.getId();

        // Get all the compteTontineList where cotisationTontine equals to cotisationTontineId
        defaultCompteTontineShouldBeFound("cotisationTontineId.equals=" + cotisationTontineId);

        // Get all the compteTontineList where cotisationTontine equals to (cotisationTontineId + 1)
        defaultCompteTontineShouldNotBeFound("cotisationTontineId.equals=" + (cotisationTontineId + 1));
    }

    @Test
    @Transactional
    void getAllCompteTontinesByDecaissementTontineIsEqualToSomething() throws Exception {
        DecaissementTontine decaissementTontine;
        if (TestUtil.findAll(em, DecaissementTontine.class).isEmpty()) {
            compteTontineRepository.saveAndFlush(compteTontine);
            decaissementTontine = DecaissementTontineResourceIT.createEntity(em);
        } else {
            decaissementTontine = TestUtil.findAll(em, DecaissementTontine.class).get(0);
        }
        em.persist(decaissementTontine);
        em.flush();
        compteTontine.addDecaissementTontine(decaissementTontine);
        compteTontineRepository.saveAndFlush(compteTontine);
        Long decaissementTontineId = decaissementTontine.getId();

        // Get all the compteTontineList where decaissementTontine equals to decaissementTontineId
        defaultCompteTontineShouldBeFound("decaissementTontineId.equals=" + decaissementTontineId);

        // Get all the compteTontineList where decaissementTontine equals to (decaissementTontineId + 1)
        defaultCompteTontineShouldNotBeFound("decaissementTontineId.equals=" + (decaissementTontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompteTontineShouldBeFound(String filter) throws Exception {
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatDeCompte").value(hasItem(DEFAULT_ETAT_DE_COMPTE.booleanValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].odreBeneficiere").value(hasItem(DEFAULT_ODRE_BENEFICIERE)))
            .andExpect(jsonPath("$.[*].matriculeCompte").value(hasItem(DEFAULT_MATRICULE_COMPTE)))
            .andExpect(jsonPath("$.[*].matriculeMenbre").value(hasItem(DEFAULT_MATRICULE_MENBRE)));

        // Check, that the count call also returns 1
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompteTontineShouldNotBeFound(String filter) throws Exception {
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompteTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompteTontine() throws Exception {
        // Get the compteTontine
        restCompteTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompteTontine() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();

        // Update the compteTontine
        CompteTontine updatedCompteTontine = compteTontineRepository.findById(compteTontine.getId()).get();
        // Disconnect from session so that the updates on updatedCompteTontine are not directly saved in db
        em.detach(updatedCompteTontine);
        updatedCompteTontine
            .etatDeCompte(UPDATED_ETAT_DE_COMPTE)
            .libele(UPDATED_LIBELE)
            .odreBeneficiere(UPDATED_ODRE_BENEFICIERE)
            .matriculeCompte(UPDATED_MATRICULE_COMPTE)
            .matriculeMenbre(UPDATED_MATRICULE_MENBRE);
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(updatedCompteTontine);

        restCompteTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
        CompteTontine testCompteTontine = compteTontineList.get(compteTontineList.size() - 1);
        assertThat(testCompteTontine.getEtatDeCompte()).isEqualTo(UPDATED_ETAT_DE_COMPTE);
        assertThat(testCompteTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testCompteTontine.getOdreBeneficiere()).isEqualTo(UPDATED_ODRE_BENEFICIERE);
        assertThat(testCompteTontine.getMatriculeCompte()).isEqualTo(UPDATED_MATRICULE_COMPTE);
        assertThat(testCompteTontine.getMatriculeMenbre()).isEqualTo(UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void putNonExistingCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteTontineWithPatch() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();

        // Update the compteTontine using partial update
        CompteTontine partialUpdatedCompteTontine = new CompteTontine();
        partialUpdatedCompteTontine.setId(compteTontine.getId());

        partialUpdatedCompteTontine.matriculeCompte(UPDATED_MATRICULE_COMPTE);

        restCompteTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteTontine))
            )
            .andExpect(status().isOk());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
        CompteTontine testCompteTontine = compteTontineList.get(compteTontineList.size() - 1);
        assertThat(testCompteTontine.getEtatDeCompte()).isEqualTo(DEFAULT_ETAT_DE_COMPTE);
        assertThat(testCompteTontine.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testCompteTontine.getOdreBeneficiere()).isEqualTo(DEFAULT_ODRE_BENEFICIERE);
        assertThat(testCompteTontine.getMatriculeCompte()).isEqualTo(UPDATED_MATRICULE_COMPTE);
        assertThat(testCompteTontine.getMatriculeMenbre()).isEqualTo(DEFAULT_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void fullUpdateCompteTontineWithPatch() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();

        // Update the compteTontine using partial update
        CompteTontine partialUpdatedCompteTontine = new CompteTontine();
        partialUpdatedCompteTontine.setId(compteTontine.getId());

        partialUpdatedCompteTontine
            .etatDeCompte(UPDATED_ETAT_DE_COMPTE)
            .libele(UPDATED_LIBELE)
            .odreBeneficiere(UPDATED_ODRE_BENEFICIERE)
            .matriculeCompte(UPDATED_MATRICULE_COMPTE)
            .matriculeMenbre(UPDATED_MATRICULE_MENBRE);

        restCompteTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteTontine))
            )
            .andExpect(status().isOk());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
        CompteTontine testCompteTontine = compteTontineList.get(compteTontineList.size() - 1);
        assertThat(testCompteTontine.getEtatDeCompte()).isEqualTo(UPDATED_ETAT_DE_COMPTE);
        assertThat(testCompteTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testCompteTontine.getOdreBeneficiere()).isEqualTo(UPDATED_ODRE_BENEFICIERE);
        assertThat(testCompteTontine.getMatriculeCompte()).isEqualTo(UPDATED_MATRICULE_COMPTE);
        assertThat(testCompteTontine.getMatriculeMenbre()).isEqualTo(UPDATED_MATRICULE_MENBRE);
    }

    @Test
    @Transactional
    void patchNonExistingCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteTontine() throws Exception {
        int databaseSizeBeforeUpdate = compteTontineRepository.findAll().size();
        compteTontine.setId(count.incrementAndGet());

        // Create the CompteTontine
        CompteTontineDTO compteTontineDTO = compteTontineMapper.toDto(compteTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteTontine in the database
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteTontine() throws Exception {
        // Initialize the database
        compteTontineRepository.saveAndFlush(compteTontine);

        int databaseSizeBeforeDelete = compteTontineRepository.findAll().size();

        // Delete the compteTontine
        restCompteTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompteTontine> compteTontineList = compteTontineRepository.findAll();
        assertThat(compteTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
