package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Banque;
import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.GestionnaireBanque;
import com.it4innov.repository.BanqueRepository;
import com.it4innov.service.criteria.BanqueCriteria;
import com.it4innov.service.dto.BanqueDTO;
import com.it4innov.service.mapper.BanqueMapper;
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
 * Integration tests for the {@link BanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BanqueResourceIT {

    private static final String DEFAULT_CODE_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OUVERTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OUVERTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_CLOTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CLOTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PENALITE_RETARD_RNBRSMNT = 1D;
    private static final Double UPDATED_PENALITE_RETARD_RNBRSMNT = 2D;
    private static final Double SMALLER_PENALITE_RETARD_RNBRSMNT = 1D - 1D;

    private static final Double DEFAULT_TAUX_INTERET_PRET = 1D;
    private static final Double UPDATED_TAUX_INTERET_PRET = 2D;
    private static final Double SMALLER_TAUX_INTERET_PRET = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BanqueRepository banqueRepository;

    @Autowired
    private BanqueMapper banqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBanqueMockMvc;

    private Banque banque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banque createEntity(EntityManager em) {
        Banque banque = new Banque()
            .codeAssociation(DEFAULT_CODE_ASSOCIATION)
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .dateOuverture(DEFAULT_DATE_OUVERTURE)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .penaliteRetardRnbrsmnt(DEFAULT_PENALITE_RETARD_RNBRSMNT)
            .tauxInteretPret(DEFAULT_TAUX_INTERET_PRET);
        return banque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Banque createUpdatedEntity(EntityManager em) {
        Banque banque = new Banque()
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .penaliteRetardRnbrsmnt(UPDATED_PENALITE_RETARD_RNBRSMNT)
            .tauxInteretPret(UPDATED_TAUX_INTERET_PRET);
        return banque;
    }

    @BeforeEach
    public void initTest() {
        banque = createEntity(em);
    }

    @Test
    @Transactional
    void createBanque() throws Exception {
        int databaseSizeBeforeCreate = banqueRepository.findAll().size();
        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);
        restBanqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banqueDTO)))
            .andExpect(status().isCreated());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeCreate + 1);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getCodeAssociation()).isEqualTo(DEFAULT_CODE_ASSOCIATION);
        assertThat(testBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testBanque.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBanque.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testBanque.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testBanque.getPenaliteRetardRnbrsmnt()).isEqualTo(DEFAULT_PENALITE_RETARD_RNBRSMNT);
        assertThat(testBanque.getTauxInteretPret()).isEqualTo(DEFAULT_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void createBanqueWithExistingId() throws Exception {
        // Create the Banque with an existing ID
        banque.setId(1L);
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        int databaseSizeBeforeCreate = banqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBanqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeAssociationIsRequired() throws Exception {
        int databaseSizeBeforeTest = banqueRepository.findAll().size();
        // set the field null
        banque.setCodeAssociation(null);

        // Create the Banque, which fails.
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        restBanqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banqueDTO)))
            .andExpect(status().isBadRequest());

        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBanques() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banque.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].penaliteRetardRnbrsmnt").value(hasItem(DEFAULT_PENALITE_RETARD_RNBRSMNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxInteretPret").value(hasItem(DEFAULT_TAUX_INTERET_PRET.doubleValue())));
    }

    @Test
    @Transactional
    void getBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get the banque
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, banque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(banque.getId().intValue()))
            .andExpect(jsonPath("$.codeAssociation").value(DEFAULT_CODE_ASSOCIATION))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateOuverture").value(DEFAULT_DATE_OUVERTURE.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.penaliteRetardRnbrsmnt").value(DEFAULT_PENALITE_RETARD_RNBRSMNT.doubleValue()))
            .andExpect(jsonPath("$.tauxInteretPret").value(DEFAULT_TAUX_INTERET_PRET.doubleValue()));
    }

    @Test
    @Transactional
    void getBanquesByIdFiltering() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        Long id = banque.getId();

        defaultBanqueShouldBeFound("id.equals=" + id);
        defaultBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBanquesByCodeAssociationIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where codeAssociation equals to DEFAULT_CODE_ASSOCIATION
        defaultBanqueShouldBeFound("codeAssociation.equals=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the banqueList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultBanqueShouldNotBeFound("codeAssociation.equals=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllBanquesByCodeAssociationIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where codeAssociation in DEFAULT_CODE_ASSOCIATION or UPDATED_CODE_ASSOCIATION
        defaultBanqueShouldBeFound("codeAssociation.in=" + DEFAULT_CODE_ASSOCIATION + "," + UPDATED_CODE_ASSOCIATION);

        // Get all the banqueList where codeAssociation equals to UPDATED_CODE_ASSOCIATION
        defaultBanqueShouldNotBeFound("codeAssociation.in=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllBanquesByCodeAssociationIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where codeAssociation is not null
        defaultBanqueShouldBeFound("codeAssociation.specified=true");

        // Get all the banqueList where codeAssociation is null
        defaultBanqueShouldNotBeFound("codeAssociation.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByCodeAssociationContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where codeAssociation contains DEFAULT_CODE_ASSOCIATION
        defaultBanqueShouldBeFound("codeAssociation.contains=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the banqueList where codeAssociation contains UPDATED_CODE_ASSOCIATION
        defaultBanqueShouldNotBeFound("codeAssociation.contains=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllBanquesByCodeAssociationNotContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where codeAssociation does not contain DEFAULT_CODE_ASSOCIATION
        defaultBanqueShouldNotBeFound("codeAssociation.doesNotContain=" + DEFAULT_CODE_ASSOCIATION);

        // Get all the banqueList where codeAssociation does not contain UPDATED_CODE_ASSOCIATION
        defaultBanqueShouldBeFound("codeAssociation.doesNotContain=" + UPDATED_CODE_ASSOCIATION);
    }

    @Test
    @Transactional
    void getAllBanquesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where libelle equals to DEFAULT_LIBELLE
        defaultBanqueShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the banqueList where libelle equals to UPDATED_LIBELLE
        defaultBanqueShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllBanquesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultBanqueShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the banqueList where libelle equals to UPDATED_LIBELLE
        defaultBanqueShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllBanquesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where libelle is not null
        defaultBanqueShouldBeFound("libelle.specified=true");

        // Get all the banqueList where libelle is null
        defaultBanqueShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where libelle contains DEFAULT_LIBELLE
        defaultBanqueShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the banqueList where libelle contains UPDATED_LIBELLE
        defaultBanqueShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllBanquesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where libelle does not contain DEFAULT_LIBELLE
        defaultBanqueShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the banqueList where libelle does not contain UPDATED_LIBELLE
        defaultBanqueShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllBanquesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where description equals to DEFAULT_DESCRIPTION
        defaultBanqueShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the banqueList where description equals to UPDATED_DESCRIPTION
        defaultBanqueShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBanquesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBanqueShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the banqueList where description equals to UPDATED_DESCRIPTION
        defaultBanqueShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBanquesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where description is not null
        defaultBanqueShouldBeFound("description.specified=true");

        // Get all the banqueList where description is null
        defaultBanqueShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where description contains DEFAULT_DESCRIPTION
        defaultBanqueShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the banqueList where description contains UPDATED_DESCRIPTION
        defaultBanqueShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBanquesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where description does not contain DEFAULT_DESCRIPTION
        defaultBanqueShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the banqueList where description does not contain UPDATED_DESCRIPTION
        defaultBanqueShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllBanquesByDateOuvertureIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateOuverture equals to DEFAULT_DATE_OUVERTURE
        defaultBanqueShouldBeFound("dateOuverture.equals=" + DEFAULT_DATE_OUVERTURE);

        // Get all the banqueList where dateOuverture equals to UPDATED_DATE_OUVERTURE
        defaultBanqueShouldNotBeFound("dateOuverture.equals=" + UPDATED_DATE_OUVERTURE);
    }

    @Test
    @Transactional
    void getAllBanquesByDateOuvertureIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateOuverture in DEFAULT_DATE_OUVERTURE or UPDATED_DATE_OUVERTURE
        defaultBanqueShouldBeFound("dateOuverture.in=" + DEFAULT_DATE_OUVERTURE + "," + UPDATED_DATE_OUVERTURE);

        // Get all the banqueList where dateOuverture equals to UPDATED_DATE_OUVERTURE
        defaultBanqueShouldNotBeFound("dateOuverture.in=" + UPDATED_DATE_OUVERTURE);
    }

    @Test
    @Transactional
    void getAllBanquesByDateOuvertureIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateOuverture is not null
        defaultBanqueShouldBeFound("dateOuverture.specified=true");

        // Get all the banqueList where dateOuverture is null
        defaultBanqueShouldNotBeFound("dateOuverture.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByDateClotureIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateCloture equals to DEFAULT_DATE_CLOTURE
        defaultBanqueShouldBeFound("dateCloture.equals=" + DEFAULT_DATE_CLOTURE);

        // Get all the banqueList where dateCloture equals to UPDATED_DATE_CLOTURE
        defaultBanqueShouldNotBeFound("dateCloture.equals=" + UPDATED_DATE_CLOTURE);
    }

    @Test
    @Transactional
    void getAllBanquesByDateClotureIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateCloture in DEFAULT_DATE_CLOTURE or UPDATED_DATE_CLOTURE
        defaultBanqueShouldBeFound("dateCloture.in=" + DEFAULT_DATE_CLOTURE + "," + UPDATED_DATE_CLOTURE);

        // Get all the banqueList where dateCloture equals to UPDATED_DATE_CLOTURE
        defaultBanqueShouldNotBeFound("dateCloture.in=" + UPDATED_DATE_CLOTURE);
    }

    @Test
    @Transactional
    void getAllBanquesByDateClotureIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where dateCloture is not null
        defaultBanqueShouldBeFound("dateCloture.specified=true");

        // Get all the banqueList where dateCloture is null
        defaultBanqueShouldNotBeFound("dateCloture.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt equals to DEFAULT_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.equals=" + DEFAULT_PENALITE_RETARD_RNBRSMNT);

        // Get all the banqueList where penaliteRetardRnbrsmnt equals to UPDATED_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.equals=" + UPDATED_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt in DEFAULT_PENALITE_RETARD_RNBRSMNT or UPDATED_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound(
            "penaliteRetardRnbrsmnt.in=" + DEFAULT_PENALITE_RETARD_RNBRSMNT + "," + UPDATED_PENALITE_RETARD_RNBRSMNT
        );

        // Get all the banqueList where penaliteRetardRnbrsmnt equals to UPDATED_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.in=" + UPDATED_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt is not null
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.specified=true");

        // Get all the banqueList where penaliteRetardRnbrsmnt is null
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt is greater than or equal to DEFAULT_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.greaterThanOrEqual=" + DEFAULT_PENALITE_RETARD_RNBRSMNT);

        // Get all the banqueList where penaliteRetardRnbrsmnt is greater than or equal to UPDATED_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.greaterThanOrEqual=" + UPDATED_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt is less than or equal to DEFAULT_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.lessThanOrEqual=" + DEFAULT_PENALITE_RETARD_RNBRSMNT);

        // Get all the banqueList where penaliteRetardRnbrsmnt is less than or equal to SMALLER_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.lessThanOrEqual=" + SMALLER_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsLessThanSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt is less than DEFAULT_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.lessThan=" + DEFAULT_PENALITE_RETARD_RNBRSMNT);

        // Get all the banqueList where penaliteRetardRnbrsmnt is less than UPDATED_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.lessThan=" + UPDATED_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByPenaliteRetardRnbrsmntIsGreaterThanSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where penaliteRetardRnbrsmnt is greater than DEFAULT_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldNotBeFound("penaliteRetardRnbrsmnt.greaterThan=" + DEFAULT_PENALITE_RETARD_RNBRSMNT);

        // Get all the banqueList where penaliteRetardRnbrsmnt is greater than SMALLER_PENALITE_RETARD_RNBRSMNT
        defaultBanqueShouldBeFound("penaliteRetardRnbrsmnt.greaterThan=" + SMALLER_PENALITE_RETARD_RNBRSMNT);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret equals to DEFAULT_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.equals=" + DEFAULT_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret equals to UPDATED_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.equals=" + UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsInShouldWork() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret in DEFAULT_TAUX_INTERET_PRET or UPDATED_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.in=" + DEFAULT_TAUX_INTERET_PRET + "," + UPDATED_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret equals to UPDATED_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.in=" + UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsNullOrNotNull() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret is not null
        defaultBanqueShouldBeFound("tauxInteretPret.specified=true");

        // Get all the banqueList where tauxInteretPret is null
        defaultBanqueShouldNotBeFound("tauxInteretPret.specified=false");
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret is greater than or equal to DEFAULT_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.greaterThanOrEqual=" + DEFAULT_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret is greater than or equal to UPDATED_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.greaterThanOrEqual=" + UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret is less than or equal to DEFAULT_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.lessThanOrEqual=" + DEFAULT_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret is less than or equal to SMALLER_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.lessThanOrEqual=" + SMALLER_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsLessThanSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret is less than DEFAULT_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.lessThan=" + DEFAULT_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret is less than UPDATED_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.lessThan=" + UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByTauxInteretPretIsGreaterThanSomething() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        // Get all the banqueList where tauxInteretPret is greater than DEFAULT_TAUX_INTERET_PRET
        defaultBanqueShouldNotBeFound("tauxInteretPret.greaterThan=" + DEFAULT_TAUX_INTERET_PRET);

        // Get all the banqueList where tauxInteretPret is greater than SMALLER_TAUX_INTERET_PRET
        defaultBanqueShouldBeFound("tauxInteretPret.greaterThan=" + SMALLER_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void getAllBanquesByCompteBanqueIsEqualToSomething() throws Exception {
        CompteBanque compteBanque;
        if (TestUtil.findAll(em, CompteBanque.class).isEmpty()) {
            banqueRepository.saveAndFlush(banque);
            compteBanque = CompteBanqueResourceIT.createEntity(em);
        } else {
            compteBanque = TestUtil.findAll(em, CompteBanque.class).get(0);
        }
        em.persist(compteBanque);
        em.flush();
        banque.addCompteBanque(compteBanque);
        banqueRepository.saveAndFlush(banque);
        Long compteBanqueId = compteBanque.getId();

        // Get all the banqueList where compteBanque equals to compteBanqueId
        defaultBanqueShouldBeFound("compteBanqueId.equals=" + compteBanqueId);

        // Get all the banqueList where compteBanque equals to (compteBanqueId + 1)
        defaultBanqueShouldNotBeFound("compteBanqueId.equals=" + (compteBanqueId + 1));
    }

    @Test
    @Transactional
    void getAllBanquesByGestionnaireBanqueIsEqualToSomething() throws Exception {
        GestionnaireBanque gestionnaireBanque;
        if (TestUtil.findAll(em, GestionnaireBanque.class).isEmpty()) {
            banqueRepository.saveAndFlush(banque);
            gestionnaireBanque = GestionnaireBanqueResourceIT.createEntity(em);
        } else {
            gestionnaireBanque = TestUtil.findAll(em, GestionnaireBanque.class).get(0);
        }
        em.persist(gestionnaireBanque);
        em.flush();
        banque.addGestionnaireBanque(gestionnaireBanque);
        banqueRepository.saveAndFlush(banque);
        Long gestionnaireBanqueId = gestionnaireBanque.getId();

        // Get all the banqueList where gestionnaireBanque equals to gestionnaireBanqueId
        defaultBanqueShouldBeFound("gestionnaireBanqueId.equals=" + gestionnaireBanqueId);

        // Get all the banqueList where gestionnaireBanque equals to (gestionnaireBanqueId + 1)
        defaultBanqueShouldNotBeFound("gestionnaireBanqueId.equals=" + (gestionnaireBanqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBanqueShouldBeFound(String filter) throws Exception {
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banque.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeAssociation").value(hasItem(DEFAULT_CODE_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateOuverture").value(hasItem(DEFAULT_DATE_OUVERTURE.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].penaliteRetardRnbrsmnt").value(hasItem(DEFAULT_PENALITE_RETARD_RNBRSMNT.doubleValue())))
            .andExpect(jsonPath("$.[*].tauxInteretPret").value(hasItem(DEFAULT_TAUX_INTERET_PRET.doubleValue())));

        // Check, that the count call also returns 1
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBanqueShouldNotBeFound(String filter) throws Exception {
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBanque() throws Exception {
        // Get the banque
        restBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

        // Update the banque
        Banque updatedBanque = banqueRepository.findById(banque.getId()).get();
        // Disconnect from session so that the updates on updatedBanque are not directly saved in db
        em.detach(updatedBanque);
        updatedBanque
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .penaliteRetardRnbrsmnt(UPDATED_PENALITE_RETARD_RNBRSMNT)
            .tauxInteretPret(UPDATED_TAUX_INTERET_PRET);
        BanqueDTO banqueDTO = banqueMapper.toDto(updatedBanque);

        restBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, banqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testBanque.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanque.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testBanque.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testBanque.getPenaliteRetardRnbrsmnt()).isEqualTo(UPDATED_PENALITE_RETARD_RNBRSMNT);
        assertThat(testBanque.getTauxInteretPret()).isEqualTo(UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void putNonExistingBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, banqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(banqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBanqueWithPatch() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

        // Update the banque using partial update
        Banque partialUpdatedBanque = new Banque();
        partialUpdatedBanque.setId(banque.getId());

        partialUpdatedBanque
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .description(UPDATED_DESCRIPTION)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .tauxInteretPret(UPDATED_TAUX_INTERET_PRET);

        restBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanque))
            )
            .andExpect(status().isOk());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testBanque.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanque.getDateOuverture()).isEqualTo(DEFAULT_DATE_OUVERTURE);
        assertThat(testBanque.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testBanque.getPenaliteRetardRnbrsmnt()).isEqualTo(DEFAULT_PENALITE_RETARD_RNBRSMNT);
        assertThat(testBanque.getTauxInteretPret()).isEqualTo(UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void fullUpdateBanqueWithPatch() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();

        // Update the banque using partial update
        Banque partialUpdatedBanque = new Banque();
        partialUpdatedBanque.setId(banque.getId());

        partialUpdatedBanque
            .codeAssociation(UPDATED_CODE_ASSOCIATION)
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .dateOuverture(UPDATED_DATE_OUVERTURE)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .penaliteRetardRnbrsmnt(UPDATED_PENALITE_RETARD_RNBRSMNT)
            .tauxInteretPret(UPDATED_TAUX_INTERET_PRET);

        restBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBanque))
            )
            .andExpect(status().isOk());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
        Banque testBanque = banqueList.get(banqueList.size() - 1);
        assertThat(testBanque.getCodeAssociation()).isEqualTo(UPDATED_CODE_ASSOCIATION);
        assertThat(testBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testBanque.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanque.getDateOuverture()).isEqualTo(UPDATED_DATE_OUVERTURE);
        assertThat(testBanque.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testBanque.getPenaliteRetardRnbrsmnt()).isEqualTo(UPDATED_PENALITE_RETARD_RNBRSMNT);
        assertThat(testBanque.getTauxInteretPret()).isEqualTo(UPDATED_TAUX_INTERET_PRET);
    }

    @Test
    @Transactional
    void patchNonExistingBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, banqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBanque() throws Exception {
        int databaseSizeBeforeUpdate = banqueRepository.findAll().size();
        banque.setId(count.incrementAndGet());

        // Create the Banque
        BanqueDTO banqueDTO = banqueMapper.toDto(banque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(banqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Banque in the database
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBanque() throws Exception {
        // Initialize the database
        banqueRepository.saveAndFlush(banque);

        int databaseSizeBeforeDelete = banqueRepository.findAll().size();

        // Delete the banque
        restBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, banque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Banque> banqueList = banqueRepository.findAll();
        assertThat(banqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
