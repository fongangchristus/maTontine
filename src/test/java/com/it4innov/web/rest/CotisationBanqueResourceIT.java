package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.CotisationBanque;
import com.it4innov.repository.CotisationBanqueRepository;
import com.it4innov.service.criteria.CotisationBanqueCriteria;
import com.it4innov.service.dto.CotisationBanqueDTO;
import com.it4innov.service.mapper.CotisationBanqueMapper;
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
 * Integration tests for the {@link CotisationBanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CotisationBanqueResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;
    private static final Double SMALLER_MONTANT = 1D - 1D;

    private static final Instant DEFAULT_DATE_COTISATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COTISATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_MONTANT_COTISE = 1D;
    private static final Double UPDATED_MONTANT_COTISE = 2D;
    private static final Double SMALLER_MONTANT_COTISE = 1D - 1D;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cotisation-banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CotisationBanqueRepository cotisationBanqueRepository;

    @Autowired
    private CotisationBanqueMapper cotisationBanqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCotisationBanqueMockMvc;

    private CotisationBanque cotisationBanque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CotisationBanque createEntity(EntityManager em) {
        CotisationBanque cotisationBanque = new CotisationBanque()
            .libelle(DEFAULT_LIBELLE)
            .montant(DEFAULT_MONTANT)
            .dateCotisation(DEFAULT_DATE_COTISATION)
            .montantCotise(DEFAULT_MONTANT_COTISE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return cotisationBanque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CotisationBanque createUpdatedEntity(EntityManager em) {
        CotisationBanque cotisationBanque = new CotisationBanque()
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .montantCotise(UPDATED_MONTANT_COTISE)
            .commentaire(UPDATED_COMMENTAIRE);
        return cotisationBanque;
    }

    @BeforeEach
    public void initTest() {
        cotisationBanque = createEntity(em);
    }

    @Test
    @Transactional
    void createCotisationBanque() throws Exception {
        int databaseSizeBeforeCreate = cotisationBanqueRepository.findAll().size();
        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);
        restCotisationBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeCreate + 1);
        CotisationBanque testCotisationBanque = cotisationBanqueList.get(cotisationBanqueList.size() - 1);
        assertThat(testCotisationBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCotisationBanque.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testCotisationBanque.getDateCotisation()).isEqualTo(DEFAULT_DATE_COTISATION);
        assertThat(testCotisationBanque.getMontantCotise()).isEqualTo(DEFAULT_MONTANT_COTISE);
        assertThat(testCotisationBanque.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createCotisationBanqueWithExistingId() throws Exception {
        // Create the CotisationBanque with an existing ID
        cotisationBanque.setId(1L);
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        int databaseSizeBeforeCreate = cotisationBanqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCotisationBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCotisationBanques() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cotisationBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCotisation").value(hasItem(DEFAULT_DATE_COTISATION.toString())))
            .andExpect(jsonPath("$.[*].montantCotise").value(hasItem(DEFAULT_MONTANT_COTISE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getCotisationBanque() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get the cotisationBanque
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, cotisationBanque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cotisationBanque.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.dateCotisation").value(DEFAULT_DATE_COTISATION.toString()))
            .andExpect(jsonPath("$.montantCotise").value(DEFAULT_MONTANT_COTISE.doubleValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getCotisationBanquesByIdFiltering() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        Long id = cotisationBanque.getId();

        defaultCotisationBanqueShouldBeFound("id.equals=" + id);
        defaultCotisationBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultCotisationBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCotisationBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultCotisationBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCotisationBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where libelle equals to DEFAULT_LIBELLE
        defaultCotisationBanqueShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the cotisationBanqueList where libelle equals to UPDATED_LIBELLE
        defaultCotisationBanqueShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultCotisationBanqueShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the cotisationBanqueList where libelle equals to UPDATED_LIBELLE
        defaultCotisationBanqueShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where libelle is not null
        defaultCotisationBanqueShouldBeFound("libelle.specified=true");

        // Get all the cotisationBanqueList where libelle is null
        defaultCotisationBanqueShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where libelle contains DEFAULT_LIBELLE
        defaultCotisationBanqueShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the cotisationBanqueList where libelle contains UPDATED_LIBELLE
        defaultCotisationBanqueShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where libelle does not contain DEFAULT_LIBELLE
        defaultCotisationBanqueShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the cotisationBanqueList where libelle does not contain UPDATED_LIBELLE
        defaultCotisationBanqueShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant equals to DEFAULT_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the cotisationBanqueList where montant equals to UPDATED_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the cotisationBanqueList where montant equals to UPDATED_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant is not null
        defaultCotisationBanqueShouldBeFound("montant.specified=true");

        // Get all the cotisationBanqueList where montant is null
        defaultCotisationBanqueShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant is greater than or equal to DEFAULT_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the cotisationBanqueList where montant is greater than or equal to UPDATED_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant is less than or equal to DEFAULT_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the cotisationBanqueList where montant is less than or equal to SMALLER_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant is less than DEFAULT_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the cotisationBanqueList where montant is less than UPDATED_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montant is greater than DEFAULT_MONTANT
        defaultCotisationBanqueShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the cotisationBanqueList where montant is greater than SMALLER_MONTANT
        defaultCotisationBanqueShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByDateCotisationIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where dateCotisation equals to DEFAULT_DATE_COTISATION
        defaultCotisationBanqueShouldBeFound("dateCotisation.equals=" + DEFAULT_DATE_COTISATION);

        // Get all the cotisationBanqueList where dateCotisation equals to UPDATED_DATE_COTISATION
        defaultCotisationBanqueShouldNotBeFound("dateCotisation.equals=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByDateCotisationIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where dateCotisation in DEFAULT_DATE_COTISATION or UPDATED_DATE_COTISATION
        defaultCotisationBanqueShouldBeFound("dateCotisation.in=" + DEFAULT_DATE_COTISATION + "," + UPDATED_DATE_COTISATION);

        // Get all the cotisationBanqueList where dateCotisation equals to UPDATED_DATE_COTISATION
        defaultCotisationBanqueShouldNotBeFound("dateCotisation.in=" + UPDATED_DATE_COTISATION);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByDateCotisationIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where dateCotisation is not null
        defaultCotisationBanqueShouldBeFound("dateCotisation.specified=true");

        // Get all the cotisationBanqueList where dateCotisation is null
        defaultCotisationBanqueShouldNotBeFound("dateCotisation.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise equals to DEFAULT_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.equals=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise equals to UPDATED_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.equals=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise in DEFAULT_MONTANT_COTISE or UPDATED_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.in=" + DEFAULT_MONTANT_COTISE + "," + UPDATED_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise equals to UPDATED_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.in=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise is not null
        defaultCotisationBanqueShouldBeFound("montantCotise.specified=true");

        // Get all the cotisationBanqueList where montantCotise is null
        defaultCotisationBanqueShouldNotBeFound("montantCotise.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise is greater than or equal to DEFAULT_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.greaterThanOrEqual=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise is greater than or equal to UPDATED_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.greaterThanOrEqual=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise is less than or equal to DEFAULT_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.lessThanOrEqual=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise is less than or equal to SMALLER_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.lessThanOrEqual=" + SMALLER_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsLessThanSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise is less than DEFAULT_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.lessThan=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise is less than UPDATED_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.lessThan=" + UPDATED_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByMontantCotiseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where montantCotise is greater than DEFAULT_MONTANT_COTISE
        defaultCotisationBanqueShouldNotBeFound("montantCotise.greaterThan=" + DEFAULT_MONTANT_COTISE);

        // Get all the cotisationBanqueList where montantCotise is greater than SMALLER_MONTANT_COTISE
        defaultCotisationBanqueShouldBeFound("montantCotise.greaterThan=" + SMALLER_MONTANT_COTISE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultCotisationBanqueShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationBanqueList where commentaire equals to UPDATED_COMMENTAIRE
        defaultCotisationBanqueShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultCotisationBanqueShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the cotisationBanqueList where commentaire equals to UPDATED_COMMENTAIRE
        defaultCotisationBanqueShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where commentaire is not null
        defaultCotisationBanqueShouldBeFound("commentaire.specified=true");

        // Get all the cotisationBanqueList where commentaire is null
        defaultCotisationBanqueShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where commentaire contains DEFAULT_COMMENTAIRE
        defaultCotisationBanqueShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationBanqueList where commentaire contains UPDATED_COMMENTAIRE
        defaultCotisationBanqueShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        // Get all the cotisationBanqueList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultCotisationBanqueShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the cotisationBanqueList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultCotisationBanqueShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCotisationBanquesByCompteBanqueIsEqualToSomething() throws Exception {
        CompteBanque compteBanque;
        if (TestUtil.findAll(em, CompteBanque.class).isEmpty()) {
            cotisationBanqueRepository.saveAndFlush(cotisationBanque);
            compteBanque = CompteBanqueResourceIT.createEntity(em);
        } else {
            compteBanque = TestUtil.findAll(em, CompteBanque.class).get(0);
        }
        em.persist(compteBanque);
        em.flush();
        cotisationBanque.setCompteBanque(compteBanque);
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);
        Long compteBanqueId = compteBanque.getId();

        // Get all the cotisationBanqueList where compteBanque equals to compteBanqueId
        defaultCotisationBanqueShouldBeFound("compteBanqueId.equals=" + compteBanqueId);

        // Get all the cotisationBanqueList where compteBanque equals to (compteBanqueId + 1)
        defaultCotisationBanqueShouldNotBeFound("compteBanqueId.equals=" + (compteBanqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCotisationBanqueShouldBeFound(String filter) throws Exception {
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cotisationBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCotisation").value(hasItem(DEFAULT_DATE_COTISATION.toString())))
            .andExpect(jsonPath("$.[*].montantCotise").value(hasItem(DEFAULT_MONTANT_COTISE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCotisationBanqueShouldNotBeFound(String filter) throws Exception {
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCotisationBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCotisationBanque() throws Exception {
        // Get the cotisationBanque
        restCotisationBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCotisationBanque() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();

        // Update the cotisationBanque
        CotisationBanque updatedCotisationBanque = cotisationBanqueRepository.findById(cotisationBanque.getId()).get();
        // Disconnect from session so that the updates on updatedCotisationBanque are not directly saved in db
        em.detach(updatedCotisationBanque);
        updatedCotisationBanque
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .montantCotise(UPDATED_MONTANT_COTISE)
            .commentaire(UPDATED_COMMENTAIRE);
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(updatedCotisationBanque);

        restCotisationBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cotisationBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
        CotisationBanque testCotisationBanque = cotisationBanqueList.get(cotisationBanqueList.size() - 1);
        assertThat(testCotisationBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCotisationBanque.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCotisationBanque.getDateCotisation()).isEqualTo(UPDATED_DATE_COTISATION);
        assertThat(testCotisationBanque.getMontantCotise()).isEqualTo(UPDATED_MONTANT_COTISE);
        assertThat(testCotisationBanque.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cotisationBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCotisationBanqueWithPatch() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();

        // Update the cotisationBanque using partial update
        CotisationBanque partialUpdatedCotisationBanque = new CotisationBanque();
        partialUpdatedCotisationBanque.setId(cotisationBanque.getId());

        partialUpdatedCotisationBanque.montant(UPDATED_MONTANT).dateCotisation(UPDATED_DATE_COTISATION);

        restCotisationBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisationBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisationBanque))
            )
            .andExpect(status().isOk());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
        CotisationBanque testCotisationBanque = cotisationBanqueList.get(cotisationBanqueList.size() - 1);
        assertThat(testCotisationBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCotisationBanque.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCotisationBanque.getDateCotisation()).isEqualTo(UPDATED_DATE_COTISATION);
        assertThat(testCotisationBanque.getMontantCotise()).isEqualTo(DEFAULT_MONTANT_COTISE);
        assertThat(testCotisationBanque.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateCotisationBanqueWithPatch() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();

        // Update the cotisationBanque using partial update
        CotisationBanque partialUpdatedCotisationBanque = new CotisationBanque();
        partialUpdatedCotisationBanque.setId(cotisationBanque.getId());

        partialUpdatedCotisationBanque
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateCotisation(UPDATED_DATE_COTISATION)
            .montantCotise(UPDATED_MONTANT_COTISE)
            .commentaire(UPDATED_COMMENTAIRE);

        restCotisationBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCotisationBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCotisationBanque))
            )
            .andExpect(status().isOk());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
        CotisationBanque testCotisationBanque = cotisationBanqueList.get(cotisationBanqueList.size() - 1);
        assertThat(testCotisationBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCotisationBanque.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testCotisationBanque.getDateCotisation()).isEqualTo(UPDATED_DATE_COTISATION);
        assertThat(testCotisationBanque.getMontantCotise()).isEqualTo(UPDATED_MONTANT_COTISE);
        assertThat(testCotisationBanque.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cotisationBanqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCotisationBanque() throws Exception {
        int databaseSizeBeforeUpdate = cotisationBanqueRepository.findAll().size();
        cotisationBanque.setId(count.incrementAndGet());

        // Create the CotisationBanque
        CotisationBanqueDTO cotisationBanqueDTO = cotisationBanqueMapper.toDto(cotisationBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCotisationBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cotisationBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CotisationBanque in the database
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCotisationBanque() throws Exception {
        // Initialize the database
        cotisationBanqueRepository.saveAndFlush(cotisationBanque);

        int databaseSizeBeforeDelete = cotisationBanqueRepository.findAll().size();

        // Delete the cotisationBanque
        restCotisationBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, cotisationBanque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CotisationBanque> cotisationBanqueList = cotisationBanqueRepository.findAll();
        assertThat(cotisationBanqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
