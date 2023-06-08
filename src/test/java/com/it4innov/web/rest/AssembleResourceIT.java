package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Assemble;
import com.it4innov.domain.enumeration.NatureAssemble;
import com.it4innov.repository.AssembleRepository;
import com.it4innov.service.criteria.AssembleCriteria;
import com.it4innov.service.dto.AssembleDTO;
import com.it4innov.service.mapper.AssembleMapper;
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
 * Integration tests for the {@link AssembleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssembleResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EN_LIGNE = false;
    private static final Boolean UPDATED_EN_LIGNE = true;

    private static final String DEFAULT_DATE_SEANCE = "AAAAAAAAAA";
    private static final String UPDATED_DATE_SEANCE = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_SEANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_SEANCE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_MEMBRE_RECOIT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_MEMBRE_RECOIT = "BBBBBBBBBB";

    private static final NatureAssemble DEFAULT_NATURE = NatureAssemble.ORDINAIRE;
    private static final NatureAssemble UPDATED_NATURE = NatureAssemble.EXTRAORDINAIRE;

    private static final String DEFAULT_COMPTE_RENDU = "AAAAAAAAAA";
    private static final String UPDATED_COMPTE_RENDU = "BBBBBBBBBB";

    private static final String DEFAULT_RESUME_ASSEMBLE = "AAAAAAAAAA";
    private static final String UPDATED_RESUME_ASSEMBLE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_CR_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_CR_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/assembles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssembleRepository assembleRepository;

    @Autowired
    private AssembleMapper assembleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssembleMockMvc;

    private Assemble assemble;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assemble createEntity(EntityManager em) {
        Assemble assemble = new Assemble()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .libele(DEFAULT_LIBELE)
            .enLigne(DEFAULT_EN_LIGNE)
            .dateSeance(DEFAULT_DATE_SEANCE)
            .lieuSeance(DEFAULT_LIEU_SEANCE)
            .matriculeMembreRecoit(DEFAULT_MATRICULE_MEMBRE_RECOIT)
            .nature(DEFAULT_NATURE)
            .compteRendu(DEFAULT_COMPTE_RENDU)
            .resumeAssemble(DEFAULT_RESUME_ASSEMBLE)
            .documentCRPath(DEFAULT_DOCUMENT_CR_PATH);
        return assemble;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assemble createUpdatedEntity(EntityManager em) {
        Assemble assemble = new Assemble()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .enLigne(UPDATED_EN_LIGNE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .lieuSeance(UPDATED_LIEU_SEANCE)
            .matriculeMembreRecoit(UPDATED_MATRICULE_MEMBRE_RECOIT)
            .nature(UPDATED_NATURE)
            .compteRendu(UPDATED_COMPTE_RENDU)
            .resumeAssemble(UPDATED_RESUME_ASSEMBLE)
            .documentCRPath(UPDATED_DOCUMENT_CR_PATH);
        return assemble;
    }

    @BeforeEach
    public void initTest() {
        assemble = createEntity(em);
    }

    @Test
    @Transactional
    void createAssemble() throws Exception {
        int databaseSizeBeforeCreate = assembleRepository.findAll().size();
        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);
        restAssembleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assembleDTO)))
            .andExpect(status().isCreated());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeCreate + 1);
        Assemble testAssemble = assembleList.get(assembleList.size() - 1);
        assertThat(testAssemble.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testAssemble.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testAssemble.getEnLigne()).isEqualTo(DEFAULT_EN_LIGNE);
        assertThat(testAssemble.getDateSeance()).isEqualTo(DEFAULT_DATE_SEANCE);
        assertThat(testAssemble.getLieuSeance()).isEqualTo(DEFAULT_LIEU_SEANCE);
        assertThat(testAssemble.getMatriculeMembreRecoit()).isEqualTo(DEFAULT_MATRICULE_MEMBRE_RECOIT);
        assertThat(testAssemble.getNature()).isEqualTo(DEFAULT_NATURE);
        assertThat(testAssemble.getCompteRendu()).isEqualTo(DEFAULT_COMPTE_RENDU);
        assertThat(testAssemble.getResumeAssemble()).isEqualTo(DEFAULT_RESUME_ASSEMBLE);
        assertThat(testAssemble.getDocumentCRPath()).isEqualTo(DEFAULT_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void createAssembleWithExistingId() throws Exception {
        // Create the Assemble with an existing ID
        assemble.setId(1L);
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        int databaseSizeBeforeCreate = assembleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssembleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assembleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = assembleRepository.findAll().size();
        // set the field null
        assemble.setCodeAssociation(null);

        // Create the Assemble, which fails.
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        restAssembleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assembleDTO)))
            .andExpect(status().isBadRequest());

        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssembles() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assemble.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].enLigne").value(hasItem(DEFAULT_EN_LIGNE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateSeance").value(hasItem(DEFAULT_DATE_SEANCE)))
            .andExpect(jsonPath("$.[*].lieuSeance").value(hasItem(DEFAULT_LIEU_SEANCE)))
            .andExpect(jsonPath("$.[*].matriculeMembreRecoit").value(hasItem(DEFAULT_MATRICULE_MEMBRE_RECOIT)))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
            .andExpect(jsonPath("$.[*].compteRendu").value(hasItem(DEFAULT_COMPTE_RENDU)))
            .andExpect(jsonPath("$.[*].resumeAssemble").value(hasItem(DEFAULT_RESUME_ASSEMBLE)))
            .andExpect(jsonPath("$.[*].documentCRPath").value(hasItem(DEFAULT_DOCUMENT_CR_PATH)));
    }

    @Test
    @Transactional
    void getAssemble() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get the assemble
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL_ID, assemble.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assemble.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.enLigne").value(DEFAULT_EN_LIGNE.booleanValue()))
            .andExpect(jsonPath("$.dateSeance").value(DEFAULT_DATE_SEANCE))
            .andExpect(jsonPath("$.lieuSeance").value(DEFAULT_LIEU_SEANCE))
            .andExpect(jsonPath("$.matriculeMembreRecoit").value(DEFAULT_MATRICULE_MEMBRE_RECOIT))
            .andExpect(jsonPath("$.nature").value(DEFAULT_NATURE.toString()))
            .andExpect(jsonPath("$.compteRendu").value(DEFAULT_COMPTE_RENDU))
            .andExpect(jsonPath("$.resumeAssemble").value(DEFAULT_RESUME_ASSEMBLE))
            .andExpect(jsonPath("$.documentCRPath").value(DEFAULT_DOCUMENT_CR_PATH));
    }

    @Test
    @Transactional
    void getAssemblesByIdFiltering() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        Long id = assemble.getId();

        defaultAssembleShouldBeFound("id.equals=" + id);
        defaultAssembleShouldNotBeFound("id.notEquals=" + id);

        defaultAssembleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAssembleShouldNotBeFound("id.greaterThan=" + id);

        defaultAssembleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAssembleShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAssemblesByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultAssembleShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the assembleList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultAssembleShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssemblesByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultAssembleShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the assembleList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultAssembleShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssemblesByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where codeAssociation is not null
        defaultAssembleShouldBeFound("codeAssociation.specified=true");

        // Get all the assembleList where codeAssociation is null
        defaultAssembleShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultAssembleShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the assembleList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultAssembleShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssemblesByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultAssembleShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the assembleList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultAssembleShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllAssemblesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where libele equals to DEFAULT_LIBELE
        defaultAssembleShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the assembleList where libele equals to UPDATED_LIBELE
        defaultAssembleShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultAssembleShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the assembleList where libele equals to UPDATED_LIBELE
        defaultAssembleShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where libele is not null
        defaultAssembleShouldBeFound("libele.specified=true");

        // Get all the assembleList where libele is null
        defaultAssembleShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where libele contains DEFAULT_LIBELE
        defaultAssembleShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the assembleList where libele contains UPDATED_LIBELE
        defaultAssembleShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where libele does not contain DEFAULT_LIBELE
        defaultAssembleShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the assembleList where libele does not contain UPDATED_LIBELE
        defaultAssembleShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllAssemblesByEnLigneIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where enLigne equals to DEFAULT_EN_LIGNE
        defaultAssembleShouldBeFound("enLigne.equals=" + DEFAULT_EN_LIGNE);

        // Get all the assembleList where enLigne equals to UPDATED_EN_LIGNE
        defaultAssembleShouldNotBeFound("enLigne.equals=" + UPDATED_EN_LIGNE);
    }

    @Test
    @Transactional
    void getAllAssemblesByEnLigneIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where enLigne in DEFAULT_EN_LIGNE or UPDATED_EN_LIGNE
        defaultAssembleShouldBeFound("enLigne.in=" + DEFAULT_EN_LIGNE + "," + UPDATED_EN_LIGNE);

        // Get all the assembleList where enLigne equals to UPDATED_EN_LIGNE
        defaultAssembleShouldNotBeFound("enLigne.in=" + UPDATED_EN_LIGNE);
    }

    @Test
    @Transactional
    void getAllAssemblesByEnLigneIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where enLigne is not null
        defaultAssembleShouldBeFound("enLigne.specified=true");

        // Get all the assembleList where enLigne is null
        defaultAssembleShouldNotBeFound("enLigne.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByDateSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where dateSeance equals to DEFAULT_DATE_SEANCE
        defaultAssembleShouldBeFound("dateSeance.equals=" + DEFAULT_DATE_SEANCE);

        // Get all the assembleList where dateSeance equals to UPDATED_DATE_SEANCE
        defaultAssembleShouldNotBeFound("dateSeance.equals=" + UPDATED_DATE_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByDateSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where dateSeance in DEFAULT_DATE_SEANCE or UPDATED_DATE_SEANCE
        defaultAssembleShouldBeFound("dateSeance.in=" + DEFAULT_DATE_SEANCE + "," + UPDATED_DATE_SEANCE);

        // Get all the assembleList where dateSeance equals to UPDATED_DATE_SEANCE
        defaultAssembleShouldNotBeFound("dateSeance.in=" + UPDATED_DATE_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByDateSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where dateSeance is not null
        defaultAssembleShouldBeFound("dateSeance.specified=true");

        // Get all the assembleList where dateSeance is null
        defaultAssembleShouldNotBeFound("dateSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByDateSeanceContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where dateSeance contains DEFAULT_DATE_SEANCE
        defaultAssembleShouldBeFound("dateSeance.contains=" + DEFAULT_DATE_SEANCE);

        // Get all the assembleList where dateSeance contains UPDATED_DATE_SEANCE
        defaultAssembleShouldNotBeFound("dateSeance.contains=" + UPDATED_DATE_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByDateSeanceNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where dateSeance does not contain DEFAULT_DATE_SEANCE
        defaultAssembleShouldNotBeFound("dateSeance.doesNotContain=" + DEFAULT_DATE_SEANCE);

        // Get all the assembleList where dateSeance does not contain UPDATED_DATE_SEANCE
        defaultAssembleShouldBeFound("dateSeance.doesNotContain=" + UPDATED_DATE_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLieuSeanceIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where lieuSeance equals to DEFAULT_LIEU_SEANCE
        defaultAssembleShouldBeFound("lieuSeance.equals=" + DEFAULT_LIEU_SEANCE);

        // Get all the assembleList where lieuSeance equals to UPDATED_LIEU_SEANCE
        defaultAssembleShouldNotBeFound("lieuSeance.equals=" + UPDATED_LIEU_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLieuSeanceIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where lieuSeance in DEFAULT_LIEU_SEANCE or UPDATED_LIEU_SEANCE
        defaultAssembleShouldBeFound("lieuSeance.in=" + DEFAULT_LIEU_SEANCE + "," + UPDATED_LIEU_SEANCE);

        // Get all the assembleList where lieuSeance equals to UPDATED_LIEU_SEANCE
        defaultAssembleShouldNotBeFound("lieuSeance.in=" + UPDATED_LIEU_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLieuSeanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where lieuSeance is not null
        defaultAssembleShouldBeFound("lieuSeance.specified=true");

        // Get all the assembleList where lieuSeance is null
        defaultAssembleShouldNotBeFound("lieuSeance.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByLieuSeanceContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where lieuSeance contains DEFAULT_LIEU_SEANCE
        defaultAssembleShouldBeFound("lieuSeance.contains=" + DEFAULT_LIEU_SEANCE);

        // Get all the assembleList where lieuSeance contains UPDATED_LIEU_SEANCE
        defaultAssembleShouldNotBeFound("lieuSeance.contains=" + UPDATED_LIEU_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByLieuSeanceNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where lieuSeance does not contain DEFAULT_LIEU_SEANCE
        defaultAssembleShouldNotBeFound("lieuSeance.doesNotContain=" + DEFAULT_LIEU_SEANCE);

        // Get all the assembleList where lieuSeance does not contain UPDATED_LIEU_SEANCE
        defaultAssembleShouldBeFound("lieuSeance.doesNotContain=" + UPDATED_LIEU_SEANCE);
    }

    @Test
    @Transactional
    void getAllAssemblesByMatriculeMembreRecoitIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where matriculeMembreRecoit equals to DEFAULT_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldBeFound("matriculeMembreRecoit.equals=" + DEFAULT_MATRICULE_MEMBRE_RECOIT);

        // Get all the assembleList where matriculeMembreRecoit equals to UPDATED_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldNotBeFound("matriculeMembreRecoit.equals=" + UPDATED_MATRICULE_MEMBRE_RECOIT);
    }

    @Test
    @Transactional
    void getAllAssemblesByMatriculeMembreRecoitIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where matriculeMembreRecoit in DEFAULT_MATRICULE_MEMBRE_RECOIT or UPDATED_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldBeFound("matriculeMembreRecoit.in=" + DEFAULT_MATRICULE_MEMBRE_RECOIT + "," + UPDATED_MATRICULE_MEMBRE_RECOIT);

        // Get all the assembleList where matriculeMembreRecoit equals to UPDATED_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldNotBeFound("matriculeMembreRecoit.in=" + UPDATED_MATRICULE_MEMBRE_RECOIT);
    }

    @Test
    @Transactional
    void getAllAssemblesByMatriculeMembreRecoitIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where matriculeMembreRecoit is not null
        defaultAssembleShouldBeFound("matriculeMembreRecoit.specified=true");

        // Get all the assembleList where matriculeMembreRecoit is null
        defaultAssembleShouldNotBeFound("matriculeMembreRecoit.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByMatriculeMembreRecoitContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where matriculeMembreRecoit contains DEFAULT_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldBeFound("matriculeMembreRecoit.contains=" + DEFAULT_MATRICULE_MEMBRE_RECOIT);

        // Get all the assembleList where matriculeMembreRecoit contains UPDATED_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldNotBeFound("matriculeMembreRecoit.contains=" + UPDATED_MATRICULE_MEMBRE_RECOIT);
    }

    @Test
    @Transactional
    void getAllAssemblesByMatriculeMembreRecoitNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where matriculeMembreRecoit does not contain DEFAULT_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldNotBeFound("matriculeMembreRecoit.doesNotContain=" + DEFAULT_MATRICULE_MEMBRE_RECOIT);

        // Get all the assembleList where matriculeMembreRecoit does not contain UPDATED_MATRICULE_MEMBRE_RECOIT
        defaultAssembleShouldBeFound("matriculeMembreRecoit.doesNotContain=" + UPDATED_MATRICULE_MEMBRE_RECOIT);
    }

    @Test
    @Transactional
    void getAllAssemblesByNatureIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where nature equals to DEFAULT_NATURE
        defaultAssembleShouldBeFound("nature.equals=" + DEFAULT_NATURE);

        // Get all the assembleList where nature equals to UPDATED_NATURE
        defaultAssembleShouldNotBeFound("nature.equals=" + UPDATED_NATURE);
    }

    @Test
    @Transactional
    void getAllAssemblesByNatureIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where nature in DEFAULT_NATURE or UPDATED_NATURE
        defaultAssembleShouldBeFound("nature.in=" + DEFAULT_NATURE + "," + UPDATED_NATURE);

        // Get all the assembleList where nature equals to UPDATED_NATURE
        defaultAssembleShouldNotBeFound("nature.in=" + UPDATED_NATURE);
    }

    @Test
    @Transactional
    void getAllAssemblesByNatureIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where nature is not null
        defaultAssembleShouldBeFound("nature.specified=true");

        // Get all the assembleList where nature is null
        defaultAssembleShouldNotBeFound("nature.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByCompteRenduIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where compteRendu equals to DEFAULT_COMPTE_RENDU
        defaultAssembleShouldBeFound("compteRendu.equals=" + DEFAULT_COMPTE_RENDU);

        // Get all the assembleList where compteRendu equals to UPDATED_COMPTE_RENDU
        defaultAssembleShouldNotBeFound("compteRendu.equals=" + UPDATED_COMPTE_RENDU);
    }

    @Test
    @Transactional
    void getAllAssemblesByCompteRenduIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where compteRendu in DEFAULT_COMPTE_RENDU or UPDATED_COMPTE_RENDU
        defaultAssembleShouldBeFound("compteRendu.in=" + DEFAULT_COMPTE_RENDU + "," + UPDATED_COMPTE_RENDU);

        // Get all the assembleList where compteRendu equals to UPDATED_COMPTE_RENDU
        defaultAssembleShouldNotBeFound("compteRendu.in=" + UPDATED_COMPTE_RENDU);
    }

    @Test
    @Transactional
    void getAllAssemblesByCompteRenduIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where compteRendu is not null
        defaultAssembleShouldBeFound("compteRendu.specified=true");

        // Get all the assembleList where compteRendu is null
        defaultAssembleShouldNotBeFound("compteRendu.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByCompteRenduContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where compteRendu contains DEFAULT_COMPTE_RENDU
        defaultAssembleShouldBeFound("compteRendu.contains=" + DEFAULT_COMPTE_RENDU);

        // Get all the assembleList where compteRendu contains UPDATED_COMPTE_RENDU
        defaultAssembleShouldNotBeFound("compteRendu.contains=" + UPDATED_COMPTE_RENDU);
    }

    @Test
    @Transactional
    void getAllAssemblesByCompteRenduNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where compteRendu does not contain DEFAULT_COMPTE_RENDU
        defaultAssembleShouldNotBeFound("compteRendu.doesNotContain=" + DEFAULT_COMPTE_RENDU);

        // Get all the assembleList where compteRendu does not contain UPDATED_COMPTE_RENDU
        defaultAssembleShouldBeFound("compteRendu.doesNotContain=" + UPDATED_COMPTE_RENDU);
    }

    @Test
    @Transactional
    void getAllAssemblesByResumeAssembleIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where resumeAssemble equals to DEFAULT_RESUME_ASSEMBLE
        defaultAssembleShouldBeFound("resumeAssemble.equals=" + DEFAULT_RESUME_ASSEMBLE);

        // Get all the assembleList where resumeAssemble equals to UPDATED_RESUME_ASSEMBLE
        defaultAssembleShouldNotBeFound("resumeAssemble.equals=" + UPDATED_RESUME_ASSEMBLE);
    }

    @Test
    @Transactional
    void getAllAssemblesByResumeAssembleIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where resumeAssemble in DEFAULT_RESUME_ASSEMBLE or UPDATED_RESUME_ASSEMBLE
        defaultAssembleShouldBeFound("resumeAssemble.in=" + DEFAULT_RESUME_ASSEMBLE + "," + UPDATED_RESUME_ASSEMBLE);

        // Get all the assembleList where resumeAssemble equals to UPDATED_RESUME_ASSEMBLE
        defaultAssembleShouldNotBeFound("resumeAssemble.in=" + UPDATED_RESUME_ASSEMBLE);
    }

    @Test
    @Transactional
    void getAllAssemblesByResumeAssembleIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where resumeAssemble is not null
        defaultAssembleShouldBeFound("resumeAssemble.specified=true");

        // Get all the assembleList where resumeAssemble is null
        defaultAssembleShouldNotBeFound("resumeAssemble.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByResumeAssembleContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where resumeAssemble contains DEFAULT_RESUME_ASSEMBLE
        defaultAssembleShouldBeFound("resumeAssemble.contains=" + DEFAULT_RESUME_ASSEMBLE);

        // Get all the assembleList where resumeAssemble contains UPDATED_RESUME_ASSEMBLE
        defaultAssembleShouldNotBeFound("resumeAssemble.contains=" + UPDATED_RESUME_ASSEMBLE);
    }

    @Test
    @Transactional
    void getAllAssemblesByResumeAssembleNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where resumeAssemble does not contain DEFAULT_RESUME_ASSEMBLE
        defaultAssembleShouldNotBeFound("resumeAssemble.doesNotContain=" + DEFAULT_RESUME_ASSEMBLE);

        // Get all the assembleList where resumeAssemble does not contain UPDATED_RESUME_ASSEMBLE
        defaultAssembleShouldBeFound("resumeAssemble.doesNotContain=" + UPDATED_RESUME_ASSEMBLE);
    }

    @Test
    @Transactional
    void getAllAssemblesByDocumentCRPathIsEqualToSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where documentCRPath equals to DEFAULT_DOCUMENT_CR_PATH
        defaultAssembleShouldBeFound("documentCRPath.equals=" + DEFAULT_DOCUMENT_CR_PATH);

        // Get all the assembleList where documentCRPath equals to UPDATED_DOCUMENT_CR_PATH
        defaultAssembleShouldNotBeFound("documentCRPath.equals=" + UPDATED_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void getAllAssemblesByDocumentCRPathIsInShouldWork() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where documentCRPath in DEFAULT_DOCUMENT_CR_PATH or UPDATED_DOCUMENT_CR_PATH
        defaultAssembleShouldBeFound("documentCRPath.in=" + DEFAULT_DOCUMENT_CR_PATH + "," + UPDATED_DOCUMENT_CR_PATH);

        // Get all the assembleList where documentCRPath equals to UPDATED_DOCUMENT_CR_PATH
        defaultAssembleShouldNotBeFound("documentCRPath.in=" + UPDATED_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void getAllAssemblesByDocumentCRPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where documentCRPath is not null
        defaultAssembleShouldBeFound("documentCRPath.specified=true");

        // Get all the assembleList where documentCRPath is null
        defaultAssembleShouldNotBeFound("documentCRPath.specified=false");
    }

    @Test
    @Transactional
    void getAllAssemblesByDocumentCRPathContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where documentCRPath contains DEFAULT_DOCUMENT_CR_PATH
        defaultAssembleShouldBeFound("documentCRPath.contains=" + DEFAULT_DOCUMENT_CR_PATH);

        // Get all the assembleList where documentCRPath contains UPDATED_DOCUMENT_CR_PATH
        defaultAssembleShouldNotBeFound("documentCRPath.contains=" + UPDATED_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void getAllAssemblesByDocumentCRPathNotContainsSomething() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        // Get all the assembleList where documentCRPath does not contain DEFAULT_DOCUMENT_CR_PATH
        defaultAssembleShouldNotBeFound("documentCRPath.doesNotContain=" + DEFAULT_DOCUMENT_CR_PATH);

        // Get all the assembleList where documentCRPath does not contain UPDATED_DOCUMENT_CR_PATH
        defaultAssembleShouldBeFound("documentCRPath.doesNotContain=" + UPDATED_DOCUMENT_CR_PATH);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAssembleShouldBeFound(String filter) throws Exception {
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assemble.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].enLigne").value(hasItem(DEFAULT_EN_LIGNE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateSeance").value(hasItem(DEFAULT_DATE_SEANCE)))
            .andExpect(jsonPath("$.[*].lieuSeance").value(hasItem(DEFAULT_LIEU_SEANCE)))
            .andExpect(jsonPath("$.[*].matriculeMembreRecoit").value(hasItem(DEFAULT_MATRICULE_MEMBRE_RECOIT)))
            .andExpect(jsonPath("$.[*].nature").value(hasItem(DEFAULT_NATURE.toString())))
            .andExpect(jsonPath("$.[*].compteRendu").value(hasItem(DEFAULT_COMPTE_RENDU)))
            .andExpect(jsonPath("$.[*].resumeAssemble").value(hasItem(DEFAULT_RESUME_ASSEMBLE)))
            .andExpect(jsonPath("$.[*].documentCRPath").value(hasItem(DEFAULT_DOCUMENT_CR_PATH)));

        // Check, that the count call also returns 1
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAssembleShouldNotBeFound(String filter) throws Exception {
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAssembleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAssemble() throws Exception {
        // Get the assemble
        restAssembleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssemble() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();

        // Update the assemble
        Assemble updatedAssemble = assembleRepository.findById(assemble.getId()).get();
        // Disconnect from session so that the updates on updatedAssemble are not directly saved in db
        em.detach(updatedAssemble);
        updatedAssemble
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .enLigne(UPDATED_EN_LIGNE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .lieuSeance(UPDATED_LIEU_SEANCE)
            .matriculeMembreRecoit(UPDATED_MATRICULE_MEMBRE_RECOIT)
            .nature(UPDATED_NATURE)
            .compteRendu(UPDATED_COMPTE_RENDU)
            .resumeAssemble(UPDATED_RESUME_ASSEMBLE)
            .documentCRPath(UPDATED_DOCUMENT_CR_PATH);
        AssembleDTO assembleDTO = assembleMapper.toDto(updatedAssemble);

        restAssembleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assembleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
        Assemble testAssemble = assembleList.get(assembleList.size() - 1);
        assertThat(testAssemble.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testAssemble.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testAssemble.getEnLigne()).isEqualTo(UPDATED_EN_LIGNE);
        assertThat(testAssemble.getDateSeance()).isEqualTo(UPDATED_DATE_SEANCE);
        assertThat(testAssemble.getLieuSeance()).isEqualTo(UPDATED_LIEU_SEANCE);
        assertThat(testAssemble.getMatriculeMembreRecoit()).isEqualTo(UPDATED_MATRICULE_MEMBRE_RECOIT);
        assertThat(testAssemble.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testAssemble.getCompteRendu()).isEqualTo(UPDATED_COMPTE_RENDU);
        assertThat(testAssemble.getResumeAssemble()).isEqualTo(UPDATED_RESUME_ASSEMBLE);
        assertThat(testAssemble.getDocumentCRPath()).isEqualTo(UPDATED_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void putNonExistingAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assembleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assembleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssembleWithPatch() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();

        // Update the assemble using partial update
        Assemble partialUpdatedAssemble = new Assemble();
        partialUpdatedAssemble.setId(assemble.getId());

        partialUpdatedAssemble.lieuSeance(UPDATED_LIEU_SEANCE).nature(UPDATED_NATURE).resumeAssemble(UPDATED_RESUME_ASSEMBLE);

        restAssembleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssemble.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssemble))
            )
            .andExpect(status().isOk());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
        Assemble testAssemble = assembleList.get(assembleList.size() - 1);
        assertThat(testAssemble.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testAssemble.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testAssemble.getEnLigne()).isEqualTo(DEFAULT_EN_LIGNE);
        assertThat(testAssemble.getDateSeance()).isEqualTo(DEFAULT_DATE_SEANCE);
        assertThat(testAssemble.getLieuSeance()).isEqualTo(UPDATED_LIEU_SEANCE);
        assertThat(testAssemble.getMatriculeMembreRecoit()).isEqualTo(DEFAULT_MATRICULE_MEMBRE_RECOIT);
        assertThat(testAssemble.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testAssemble.getCompteRendu()).isEqualTo(DEFAULT_COMPTE_RENDU);
        assertThat(testAssemble.getResumeAssemble()).isEqualTo(UPDATED_RESUME_ASSEMBLE);
        assertThat(testAssemble.getDocumentCRPath()).isEqualTo(DEFAULT_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void fullUpdateAssembleWithPatch() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();

        // Update the assemble using partial update
        Assemble partialUpdatedAssemble = new Assemble();
        partialUpdatedAssemble.setId(assemble.getId());

        partialUpdatedAssemble
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libele(UPDATED_LIBELE)
            .enLigne(UPDATED_EN_LIGNE)
            .dateSeance(UPDATED_DATE_SEANCE)
            .lieuSeance(UPDATED_LIEU_SEANCE)
            .matriculeMembreRecoit(UPDATED_MATRICULE_MEMBRE_RECOIT)
            .nature(UPDATED_NATURE)
            .compteRendu(UPDATED_COMPTE_RENDU)
            .resumeAssemble(UPDATED_RESUME_ASSEMBLE)
            .documentCRPath(UPDATED_DOCUMENT_CR_PATH);

        restAssembleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssemble.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssemble))
            )
            .andExpect(status().isOk());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
        Assemble testAssemble = assembleList.get(assembleList.size() - 1);
        assertThat(testAssemble.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testAssemble.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testAssemble.getEnLigne()).isEqualTo(UPDATED_EN_LIGNE);
        assertThat(testAssemble.getDateSeance()).isEqualTo(UPDATED_DATE_SEANCE);
        assertThat(testAssemble.getLieuSeance()).isEqualTo(UPDATED_LIEU_SEANCE);
        assertThat(testAssemble.getMatriculeMembreRecoit()).isEqualTo(UPDATED_MATRICULE_MEMBRE_RECOIT);
        assertThat(testAssemble.getNature()).isEqualTo(UPDATED_NATURE);
        assertThat(testAssemble.getCompteRendu()).isEqualTo(UPDATED_COMPTE_RENDU);
        assertThat(testAssemble.getResumeAssemble()).isEqualTo(UPDATED_RESUME_ASSEMBLE);
        assertThat(testAssemble.getDocumentCRPath()).isEqualTo(UPDATED_DOCUMENT_CR_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assembleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssemble() throws Exception {
        int databaseSizeBeforeUpdate = assembleRepository.findAll().size();
        assemble.setId(count.incrementAndGet());

        // Create the Assemble
        AssembleDTO assembleDTO = assembleMapper.toDto(assemble);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssembleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assembleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assemble in the database
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssemble() throws Exception {
        // Initialize the database
        assembleRepository.saveAndFlush(assemble);

        int databaseSizeBeforeDelete = assembleRepository.findAll().size();

        // Delete the assemble
        restAssembleMockMvc
            .perform(delete(ENTITY_API_URL_ID, assemble.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assemble> assembleList = assembleRepository.findAll();
        assertThat(assembleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
