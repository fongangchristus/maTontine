package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Paiement;
import com.it4innov.domain.enumeration.ModePaiement;
import com.it4innov.domain.enumeration.StatutPaiement;
import com.it4innov.repository.PaiementRepository;
import com.it4innov.service.criteria.PaiementCriteria;
import com.it4innov.service.dto.PaiementDTO;
import com.it4innov.service.mapper.PaiementMapper;
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
 * Integration tests for the {@link PaiementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaiementResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCE_PAIEMENT = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_PAIEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULECMPT_EMET = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULECMPT_EMET = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULECMPT_DEST = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULECMPT_DEST = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_PAIEMENT = 1D;
    private static final Double UPDATED_MONTANT_PAIEMENT = 2D;
    private static final Double SMALLER_MONTANT_PAIEMENT = 1D - 1D;

    private static final Instant DEFAULT_DATE_PAIEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAIEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ModePaiement DEFAULT_MODE_PAIEMENT = ModePaiement.ESPECE;
    private static final ModePaiement UPDATED_MODE_PAIEMENT = ModePaiement.CHEQUE;

    private static final StatutPaiement DEFAULT_STATUT_PAIEMENT = StatutPaiement.ENCOURS;
    private static final StatutPaiement UPDATED_STATUT_PAIEMENT = StatutPaiement.PAYER;

    private static final String ENTITY_API_URL = "/api/paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private PaiementMapper paiementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createEntity(EntityManager em) {
        Paiement paiement = new Paiement()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .referencePaiement(DEFAULT_REFERENCE_PAIEMENT)
            .matriculecmptEmet(DEFAULT_MATRICULECMPT_EMET)
            .matriculecmptDest(DEFAULT_MATRICULECMPT_DEST)
            .montantPaiement(DEFAULT_MONTANT_PAIEMENT)
            .datePaiement(DEFAULT_DATE_PAIEMENT)
            .modePaiement(DEFAULT_MODE_PAIEMENT)
            .statutPaiement(DEFAULT_STATUT_PAIEMENT);
        return paiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paiement createUpdatedEntity(EntityManager em) {
        Paiement paiement = new Paiement()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .matriculecmptEmet(UPDATED_MATRICULECMPT_EMET)
            .matriculecmptDest(UPDATED_MATRICULECMPT_DEST)
            .montantPaiement(UPDATED_MONTANT_PAIEMENT)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT);
        return paiement;
    }

    @BeforeEach
    public void initTest() {
        paiement = createEntity(em);
    }

    @Test
    @Transactional
    void createPaiement() throws Exception {
        int databaseSizeBeforeCreate = paiementRepository.findAll().size();
        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isCreated());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate + 1);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testPaiement.getReferencePaiement()).isEqualTo(DEFAULT_REFERENCE_PAIEMENT);
        assertThat(testPaiement.getMatriculecmptEmet()).isEqualTo(DEFAULT_MATRICULECMPT_EMET);
        assertThat(testPaiement.getMatriculecmptDest()).isEqualTo(DEFAULT_MATRICULECMPT_DEST);
        assertThat(testPaiement.getMontantPaiement()).isEqualTo(DEFAULT_MONTANT_PAIEMENT);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiement.getModePaiement()).isEqualTo(DEFAULT_MODE_PAIEMENT);
        assertThat(testPaiement.getStatutPaiement()).isEqualTo(DEFAULT_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void createPaiementWithExistingId() throws Exception {
        // Create the Paiement with an existing ID
        paiement.setId(1L);
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        int databaseSizeBeforeCreate = paiementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setCodeAssociation(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculecmptEmetIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setMatriculecmptEmet(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMatriculecmptDestIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setMatriculecmptDest(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontantPaiementIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setMontantPaiement(null);

        // Create the Paiement, which fails.
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        restPaiementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isBadRequest());

        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPaiements() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)))
            .andExpect(jsonPath("$.[*].matriculecmptEmet").value(hasItem(DEFAULT_MATRICULECMPT_EMET)))
            .andExpect(jsonPath("$.[*].matriculecmptDest").value(hasItem(DEFAULT_MATRICULECMPT_DEST)))
            .andExpect(jsonPath("$.[*].montantPaiement").value(hasItem(DEFAULT_MONTANT_PAIEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].modePaiement").value(hasItem(DEFAULT_MODE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].statutPaiement").value(hasItem(DEFAULT_STATUT_PAIEMENT.toString())));
    }

    @Test
    @Transactional
    void getPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL_ID, paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.referencePaiement").value(DEFAULT_REFERENCE_PAIEMENT))
            .andExpect(jsonPath("$.matriculecmptEmet").value(DEFAULT_MATRICULECMPT_EMET))
            .andExpect(jsonPath("$.matriculecmptDest").value(DEFAULT_MATRICULECMPT_DEST))
            .andExpect(jsonPath("$.montantPaiement").value(DEFAULT_MONTANT_PAIEMENT.doubleValue()))
            .andExpect(jsonPath("$.datePaiement").value(DEFAULT_DATE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.modePaiement").value(DEFAULT_MODE_PAIEMENT.toString()))
            .andExpect(jsonPath("$.statutPaiement").value(DEFAULT_STATUT_PAIEMENT.toString()));
    }

    @Test
    @Transactional
    void getPaiementsByIdFiltering() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        Long id = paiement.getId();

        defaultPaiementShouldBeFound("id.equals=" + id);
        defaultPaiementShouldNotBeFound("id.notEquals=" + id);

        defaultPaiementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPaiementShouldNotBeFound("id.greaterThan=" + id);

        defaultPaiementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPaiementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaiementsByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultPaiementShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the paiementList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultPaiementShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultPaiementShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the paiementList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultPaiementShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where codeAssociation is not null
        defaultPaiementShouldBeFound("codeAssociation.specified=true");

        // Get all the paiementList where codeAssociation is null
        defaultPaiementShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultPaiementShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the paiementList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultPaiementShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultPaiementShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the paiementList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultPaiementShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllPaiementsByReferencePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where referencePaiement equals to DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementShouldBeFound("referencePaiement.equals=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementShouldNotBeFound("referencePaiement.equals=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByReferencePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where referencePaiement in DEFAULT_REFERENCE_PAIEMENT or UPDATED_REFERENCE_PAIEMENT
        defaultPaiementShouldBeFound("referencePaiement.in=" + DEFAULT_REFERENCE_PAIEMENT + "," + UPDATED_REFERENCE_PAIEMENT);

        // Get all the paiementList where referencePaiement equals to UPDATED_REFERENCE_PAIEMENT
        defaultPaiementShouldNotBeFound("referencePaiement.in=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByReferencePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where referencePaiement is not null
        defaultPaiementShouldBeFound("referencePaiement.specified=true");

        // Get all the paiementList where referencePaiement is null
        defaultPaiementShouldNotBeFound("referencePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByReferencePaiementContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where referencePaiement contains DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementShouldBeFound("referencePaiement.contains=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementList where referencePaiement contains UPDATED_REFERENCE_PAIEMENT
        defaultPaiementShouldNotBeFound("referencePaiement.contains=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByReferencePaiementNotContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where referencePaiement does not contain DEFAULT_REFERENCE_PAIEMENT
        defaultPaiementShouldNotBeFound("referencePaiement.doesNotContain=" + DEFAULT_REFERENCE_PAIEMENT);

        // Get all the paiementList where referencePaiement does not contain UPDATED_REFERENCE_PAIEMENT
        defaultPaiementShouldBeFound("referencePaiement.doesNotContain=" + UPDATED_REFERENCE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptEmetIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptEmet equals to DEFAULT_MATRICULECMPT_EMET
        defaultPaiementShouldBeFound("matriculecmptEmet.equals=" + DEFAULT_MATRICULECMPT_EMET);

        // Get all the paiementList where matriculecmptEmet equals to UPDATED_MATRICULECMPT_EMET
        defaultPaiementShouldNotBeFound("matriculecmptEmet.equals=" + UPDATED_MATRICULECMPT_EMET);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptEmetIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptEmet in DEFAULT_MATRICULECMPT_EMET or UPDATED_MATRICULECMPT_EMET
        defaultPaiementShouldBeFound("matriculecmptEmet.in=" + DEFAULT_MATRICULECMPT_EMET + "," + UPDATED_MATRICULECMPT_EMET);

        // Get all the paiementList where matriculecmptEmet equals to UPDATED_MATRICULECMPT_EMET
        defaultPaiementShouldNotBeFound("matriculecmptEmet.in=" + UPDATED_MATRICULECMPT_EMET);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptEmetIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptEmet is not null
        defaultPaiementShouldBeFound("matriculecmptEmet.specified=true");

        // Get all the paiementList where matriculecmptEmet is null
        defaultPaiementShouldNotBeFound("matriculecmptEmet.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptEmetContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptEmet contains DEFAULT_MATRICULECMPT_EMET
        defaultPaiementShouldBeFound("matriculecmptEmet.contains=" + DEFAULT_MATRICULECMPT_EMET);

        // Get all the paiementList where matriculecmptEmet contains UPDATED_MATRICULECMPT_EMET
        defaultPaiementShouldNotBeFound("matriculecmptEmet.contains=" + UPDATED_MATRICULECMPT_EMET);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptEmetNotContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptEmet does not contain DEFAULT_MATRICULECMPT_EMET
        defaultPaiementShouldNotBeFound("matriculecmptEmet.doesNotContain=" + DEFAULT_MATRICULECMPT_EMET);

        // Get all the paiementList where matriculecmptEmet does not contain UPDATED_MATRICULECMPT_EMET
        defaultPaiementShouldBeFound("matriculecmptEmet.doesNotContain=" + UPDATED_MATRICULECMPT_EMET);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptDestIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptDest equals to DEFAULT_MATRICULECMPT_DEST
        defaultPaiementShouldBeFound("matriculecmptDest.equals=" + DEFAULT_MATRICULECMPT_DEST);

        // Get all the paiementList where matriculecmptDest equals to UPDATED_MATRICULECMPT_DEST
        defaultPaiementShouldNotBeFound("matriculecmptDest.equals=" + UPDATED_MATRICULECMPT_DEST);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptDestIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptDest in DEFAULT_MATRICULECMPT_DEST or UPDATED_MATRICULECMPT_DEST
        defaultPaiementShouldBeFound("matriculecmptDest.in=" + DEFAULT_MATRICULECMPT_DEST + "," + UPDATED_MATRICULECMPT_DEST);

        // Get all the paiementList where matriculecmptDest equals to UPDATED_MATRICULECMPT_DEST
        defaultPaiementShouldNotBeFound("matriculecmptDest.in=" + UPDATED_MATRICULECMPT_DEST);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptDestIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptDest is not null
        defaultPaiementShouldBeFound("matriculecmptDest.specified=true");

        // Get all the paiementList where matriculecmptDest is null
        defaultPaiementShouldNotBeFound("matriculecmptDest.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptDestContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptDest contains DEFAULT_MATRICULECMPT_DEST
        defaultPaiementShouldBeFound("matriculecmptDest.contains=" + DEFAULT_MATRICULECMPT_DEST);

        // Get all the paiementList where matriculecmptDest contains UPDATED_MATRICULECMPT_DEST
        defaultPaiementShouldNotBeFound("matriculecmptDest.contains=" + UPDATED_MATRICULECMPT_DEST);
    }

    @Test
    @Transactional
    void getAllPaiementsByMatriculecmptDestNotContainsSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where matriculecmptDest does not contain DEFAULT_MATRICULECMPT_DEST
        defaultPaiementShouldNotBeFound("matriculecmptDest.doesNotContain=" + DEFAULT_MATRICULECMPT_DEST);

        // Get all the paiementList where matriculecmptDest does not contain UPDATED_MATRICULECMPT_DEST
        defaultPaiementShouldBeFound("matriculecmptDest.doesNotContain=" + UPDATED_MATRICULECMPT_DEST);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement equals to DEFAULT_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.equals=" + DEFAULT_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement equals to UPDATED_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.equals=" + UPDATED_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement in DEFAULT_MONTANT_PAIEMENT or UPDATED_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.in=" + DEFAULT_MONTANT_PAIEMENT + "," + UPDATED_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement equals to UPDATED_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.in=" + UPDATED_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement is not null
        defaultPaiementShouldBeFound("montantPaiement.specified=true");

        // Get all the paiementList where montantPaiement is null
        defaultPaiementShouldNotBeFound("montantPaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement is greater than or equal to DEFAULT_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.greaterThanOrEqual=" + DEFAULT_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement is greater than or equal to UPDATED_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.greaterThanOrEqual=" + UPDATED_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement is less than or equal to DEFAULT_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.lessThanOrEqual=" + DEFAULT_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement is less than or equal to SMALLER_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.lessThanOrEqual=" + SMALLER_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsLessThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement is less than DEFAULT_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.lessThan=" + DEFAULT_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement is less than UPDATED_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.lessThan=" + UPDATED_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByMontantPaiementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where montantPaiement is greater than DEFAULT_MONTANT_PAIEMENT
        defaultPaiementShouldNotBeFound("montantPaiement.greaterThan=" + DEFAULT_MONTANT_PAIEMENT);

        // Get all the paiementList where montantPaiement is greater than SMALLER_MONTANT_PAIEMENT
        defaultPaiementShouldBeFound("montantPaiement.greaterThan=" + SMALLER_MONTANT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByDatePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where datePaiement equals to DEFAULT_DATE_PAIEMENT
        defaultPaiementShouldBeFound("datePaiement.equals=" + DEFAULT_DATE_PAIEMENT);

        // Get all the paiementList where datePaiement equals to UPDATED_DATE_PAIEMENT
        defaultPaiementShouldNotBeFound("datePaiement.equals=" + UPDATED_DATE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByDatePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where datePaiement in DEFAULT_DATE_PAIEMENT or UPDATED_DATE_PAIEMENT
        defaultPaiementShouldBeFound("datePaiement.in=" + DEFAULT_DATE_PAIEMENT + "," + UPDATED_DATE_PAIEMENT);

        // Get all the paiementList where datePaiement equals to UPDATED_DATE_PAIEMENT
        defaultPaiementShouldNotBeFound("datePaiement.in=" + UPDATED_DATE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByDatePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where datePaiement is not null
        defaultPaiementShouldBeFound("datePaiement.specified=true");

        // Get all the paiementList where datePaiement is null
        defaultPaiementShouldNotBeFound("datePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByModePaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where modePaiement equals to DEFAULT_MODE_PAIEMENT
        defaultPaiementShouldBeFound("modePaiement.equals=" + DEFAULT_MODE_PAIEMENT);

        // Get all the paiementList where modePaiement equals to UPDATED_MODE_PAIEMENT
        defaultPaiementShouldNotBeFound("modePaiement.equals=" + UPDATED_MODE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByModePaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where modePaiement in DEFAULT_MODE_PAIEMENT or UPDATED_MODE_PAIEMENT
        defaultPaiementShouldBeFound("modePaiement.in=" + DEFAULT_MODE_PAIEMENT + "," + UPDATED_MODE_PAIEMENT);

        // Get all the paiementList where modePaiement equals to UPDATED_MODE_PAIEMENT
        defaultPaiementShouldNotBeFound("modePaiement.in=" + UPDATED_MODE_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByModePaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where modePaiement is not null
        defaultPaiementShouldBeFound("modePaiement.specified=true");

        // Get all the paiementList where modePaiement is null
        defaultPaiementShouldNotBeFound("modePaiement.specified=false");
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsEqualToSomething() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement equals to DEFAULT_STATUT_PAIEMENT
        defaultPaiementShouldBeFound("statutPaiement.equals=" + DEFAULT_STATUT_PAIEMENT);

        // Get all the paiementList where statutPaiement equals to UPDATED_STATUT_PAIEMENT
        defaultPaiementShouldNotBeFound("statutPaiement.equals=" + UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsInShouldWork() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement in DEFAULT_STATUT_PAIEMENT or UPDATED_STATUT_PAIEMENT
        defaultPaiementShouldBeFound("statutPaiement.in=" + DEFAULT_STATUT_PAIEMENT + "," + UPDATED_STATUT_PAIEMENT);

        // Get all the paiementList where statutPaiement equals to UPDATED_STATUT_PAIEMENT
        defaultPaiementShouldNotBeFound("statutPaiement.in=" + UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void getAllPaiementsByStatutPaiementIsNullOrNotNull() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiementList where statutPaiement is not null
        defaultPaiementShouldBeFound("statutPaiement.specified=true");

        // Get all the paiementList where statutPaiement is null
        defaultPaiementShouldNotBeFound("statutPaiement.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaiementShouldBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].referencePaiement").value(hasItem(DEFAULT_REFERENCE_PAIEMENT)))
            .andExpect(jsonPath("$.[*].matriculecmptEmet").value(hasItem(DEFAULT_MATRICULECMPT_EMET)))
            .andExpect(jsonPath("$.[*].matriculecmptDest").value(hasItem(DEFAULT_MATRICULECMPT_DEST)))
            .andExpect(jsonPath("$.[*].montantPaiement").value(hasItem(DEFAULT_MONTANT_PAIEMENT.doubleValue())))
            .andExpect(jsonPath("$.[*].datePaiement").value(hasItem(DEFAULT_DATE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].modePaiement").value(hasItem(DEFAULT_MODE_PAIEMENT.toString())))
            .andExpect(jsonPath("$.[*].statutPaiement").value(hasItem(DEFAULT_STATUT_PAIEMENT.toString())));

        // Check, that the count call also returns 1
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaiementShouldNotBeFound(String filter) throws Exception {
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaiementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement
        Paiement updatedPaiement = paiementRepository.findById(paiement.getId()).get();
        // Disconnect from session so that the updates on updatedPaiement are not directly saved in db
        em.detach(updatedPaiement);
        updatedPaiement
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .matriculecmptEmet(UPDATED_MATRICULECMPT_EMET)
            .matriculecmptDest(UPDATED_MATRICULECMPT_DEST)
            .montantPaiement(UPDATED_MONTANT_PAIEMENT)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT);
        PaiementDTO paiementDTO = paiementMapper.toDto(updatedPaiement);

        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testPaiement.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
        assertThat(testPaiement.getMatriculecmptEmet()).isEqualTo(UPDATED_MATRICULECMPT_EMET);
        assertThat(testPaiement.getMatriculecmptDest()).isEqualTo(UPDATED_MATRICULECMPT_DEST);
        assertThat(testPaiement.getMontantPaiement()).isEqualTo(UPDATED_MONTANT_PAIEMENT);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiement.getModePaiement()).isEqualTo(UPDATED_MODE_PAIEMENT);
        assertThat(testPaiement.getStatutPaiement()).isEqualTo(UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void putNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paiementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .matriculecmptEmet(UPDATED_MATRICULECMPT_EMET)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testPaiement.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
        assertThat(testPaiement.getMatriculecmptEmet()).isEqualTo(UPDATED_MATRICULECMPT_EMET);
        assertThat(testPaiement.getMatriculecmptDest()).isEqualTo(DEFAULT_MATRICULECMPT_DEST);
        assertThat(testPaiement.getMontantPaiement()).isEqualTo(DEFAULT_MONTANT_PAIEMENT);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(DEFAULT_DATE_PAIEMENT);
        assertThat(testPaiement.getModePaiement()).isEqualTo(UPDATED_MODE_PAIEMENT);
        assertThat(testPaiement.getStatutPaiement()).isEqualTo(UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void fullUpdatePaiementWithPatch() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement using partial update
        Paiement partialUpdatedPaiement = new Paiement();
        partialUpdatedPaiement.setId(paiement.getId());

        partialUpdatedPaiement
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .referencePaiement(UPDATED_REFERENCE_PAIEMENT)
            .matriculecmptEmet(UPDATED_MATRICULECMPT_EMET)
            .matriculecmptDest(UPDATED_MATRICULECMPT_DEST)
            .montantPaiement(UPDATED_MONTANT_PAIEMENT)
            .datePaiement(UPDATED_DATE_PAIEMENT)
            .modePaiement(UPDATED_MODE_PAIEMENT)
            .statutPaiement(UPDATED_STATUT_PAIEMENT);

        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPaiement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPaiement))
            )
            .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiementList.get(paiementList.size() - 1);
        assertThat(testPaiement.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testPaiement.getReferencePaiement()).isEqualTo(UPDATED_REFERENCE_PAIEMENT);
        assertThat(testPaiement.getMatriculecmptEmet()).isEqualTo(UPDATED_MATRICULECMPT_EMET);
        assertThat(testPaiement.getMatriculecmptDest()).isEqualTo(UPDATED_MATRICULECMPT_DEST);
        assertThat(testPaiement.getMontantPaiement()).isEqualTo(UPDATED_MONTANT_PAIEMENT);
        assertThat(testPaiement.getDatePaiement()).isEqualTo(UPDATED_DATE_PAIEMENT);
        assertThat(testPaiement.getModePaiement()).isEqualTo(UPDATED_MODE_PAIEMENT);
        assertThat(testPaiement.getStatutPaiement()).isEqualTo(UPDATED_STATUT_PAIEMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paiementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPaiement() throws Exception {
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();
        paiement.setId(count.incrementAndGet());

        // Create the Paiement
        PaiementDTO paiementDTO = paiementMapper.toDto(paiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaiementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paiementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Paiement in the database
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        int databaseSizeBeforeDelete = paiementRepository.findAll().size();

        // Delete the paiement
        restPaiementMockMvc
            .perform(delete(ENTITY_API_URL_ID, paiement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paiement> paiementList = paiementRepository.findAll();
        assertThat(paiementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
