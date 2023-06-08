package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.DecaisementBanque;
import com.it4innov.repository.DecaisementBanqueRepository;
import com.it4innov.service.criteria.DecaisementBanqueCriteria;
import com.it4innov.service.dto.DecaisementBanqueDTO;
import com.it4innov.service.mapper.DecaisementBanqueMapper;
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
 * Integration tests for the {@link DecaisementBanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecaisementBanqueResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;
    private static final Double SMALLER_MONTANT = 1D - 1D;

    private static final Instant DEFAULT_DATE_DECAISSEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DECAISSEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_MONTANT_DECAISSE = 1D;
    private static final Double UPDATED_MONTANT_DECAISSE = 2D;
    private static final Double SMALLER_MONTANT_DECAISSE = 1D - 1D;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/decaisement-banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DecaisementBanqueRepository decaisementBanqueRepository;

    @Autowired
    private DecaisementBanqueMapper decaisementBanqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecaisementBanqueMockMvc;

    private DecaisementBanque decaisementBanque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecaisementBanque createEntity(EntityManager em) {
        DecaisementBanque decaisementBanque = new DecaisementBanque()
            .libelle(DEFAULT_LIBELLE)
            .montant(DEFAULT_MONTANT)
            .dateDecaissement(DEFAULT_DATE_DECAISSEMENT)
            .montantDecaisse(DEFAULT_MONTANT_DECAISSE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return decaisementBanque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecaisementBanque createUpdatedEntity(EntityManager em) {
        DecaisementBanque decaisementBanque = new DecaisementBanque()
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);
        return decaisementBanque;
    }

    @BeforeEach
    public void initTest() {
        decaisementBanque = createEntity(em);
    }

    @Test
    @Transactional
    void createDecaisementBanque() throws Exception {
        int databaseSizeBeforeCreate = decaisementBanqueRepository.findAll().size();
        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);
        restDecaisementBanqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeCreate + 1);
        DecaisementBanque testDecaisementBanque = decaisementBanqueList.get(decaisementBanqueList.size() - 1);
        assertThat(testDecaisementBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDecaisementBanque.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDecaisementBanque.getDateDecaissement()).isEqualTo(DEFAULT_DATE_DECAISSEMENT);
        assertThat(testDecaisementBanque.getMontantDecaisse()).isEqualTo(DEFAULT_MONTANT_DECAISSE);
        assertThat(testDecaisementBanque.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createDecaisementBanqueWithExistingId() throws Exception {
        // Create the DecaisementBanque with an existing ID
        decaisementBanque.setId(1L);
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        int databaseSizeBeforeCreate = decaisementBanqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecaisementBanqueMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDecaisementBanques() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decaisementBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDecaissement").value(hasItem(DEFAULT_DATE_DECAISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].montantDecaisse").value(hasItem(DEFAULT_MONTANT_DECAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getDecaisementBanque() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get the decaisementBanque
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, decaisementBanque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decaisementBanque.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.dateDecaissement").value(DEFAULT_DATE_DECAISSEMENT.toString()))
            .andExpect(jsonPath("$.montantDecaisse").value(DEFAULT_MONTANT_DECAISSE.doubleValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getDecaisementBanquesByIdFiltering() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        Long id = decaisementBanque.getId();

        defaultDecaisementBanqueShouldBeFound("id.equals=" + id);
        defaultDecaisementBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultDecaisementBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDecaisementBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultDecaisementBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDecaisementBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where libelle equals to DEFAULT_LIBELLE
        defaultDecaisementBanqueShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the decaisementBanqueList where libelle equals to UPDATED_LIBELLE
        defaultDecaisementBanqueShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultDecaisementBanqueShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the decaisementBanqueList where libelle equals to UPDATED_LIBELLE
        defaultDecaisementBanqueShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where libelle is not null
        defaultDecaisementBanqueShouldBeFound("libelle.specified=true");

        // Get all the decaisementBanqueList where libelle is null
        defaultDecaisementBanqueShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where libelle contains DEFAULT_LIBELLE
        defaultDecaisementBanqueShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the decaisementBanqueList where libelle contains UPDATED_LIBELLE
        defaultDecaisementBanqueShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where libelle does not contain DEFAULT_LIBELLE
        defaultDecaisementBanqueShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the decaisementBanqueList where libelle does not contain UPDATED_LIBELLE
        defaultDecaisementBanqueShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant equals to DEFAULT_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the decaisementBanqueList where montant equals to UPDATED_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the decaisementBanqueList where montant equals to UPDATED_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant is not null
        defaultDecaisementBanqueShouldBeFound("montant.specified=true");

        // Get all the decaisementBanqueList where montant is null
        defaultDecaisementBanqueShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant is greater than or equal to DEFAULT_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the decaisementBanqueList where montant is greater than or equal to UPDATED_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant is less than or equal to DEFAULT_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the decaisementBanqueList where montant is less than or equal to SMALLER_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant is less than DEFAULT_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the decaisementBanqueList where montant is less than UPDATED_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montant is greater than DEFAULT_MONTANT
        defaultDecaisementBanqueShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the decaisementBanqueList where montant is greater than SMALLER_MONTANT
        defaultDecaisementBanqueShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByDateDecaissementIsEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where dateDecaissement equals to DEFAULT_DATE_DECAISSEMENT
        defaultDecaisementBanqueShouldBeFound("dateDecaissement.equals=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaisementBanqueList where dateDecaissement equals to UPDATED_DATE_DECAISSEMENT
        defaultDecaisementBanqueShouldNotBeFound("dateDecaissement.equals=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByDateDecaissementIsInShouldWork() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where dateDecaissement in DEFAULT_DATE_DECAISSEMENT or UPDATED_DATE_DECAISSEMENT
        defaultDecaisementBanqueShouldBeFound("dateDecaissement.in=" + DEFAULT_DATE_DECAISSEMENT + "," + UPDATED_DATE_DECAISSEMENT);

        // Get all the decaisementBanqueList where dateDecaissement equals to UPDATED_DATE_DECAISSEMENT
        defaultDecaisementBanqueShouldNotBeFound("dateDecaissement.in=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByDateDecaissementIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where dateDecaissement is not null
        defaultDecaisementBanqueShouldBeFound("dateDecaissement.specified=true");

        // Get all the decaisementBanqueList where dateDecaissement is null
        defaultDecaisementBanqueShouldNotBeFound("dateDecaissement.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse equals to DEFAULT_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.equals=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse equals to UPDATED_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.equals=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsInShouldWork() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse in DEFAULT_MONTANT_DECAISSE or UPDATED_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.in=" + DEFAULT_MONTANT_DECAISSE + "," + UPDATED_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse equals to UPDATED_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.in=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse is not null
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.specified=true");

        // Get all the decaisementBanqueList where montantDecaisse is null
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse is greater than or equal to DEFAULT_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.greaterThanOrEqual=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse is greater than or equal to UPDATED_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.greaterThanOrEqual=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse is less than or equal to DEFAULT_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.lessThanOrEqual=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse is less than or equal to SMALLER_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.lessThanOrEqual=" + SMALLER_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsLessThanSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse is less than DEFAULT_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.lessThan=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse is less than UPDATED_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.lessThan=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByMontantDecaisseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where montantDecaisse is greater than DEFAULT_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldNotBeFound("montantDecaisse.greaterThan=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaisementBanqueList where montantDecaisse is greater than SMALLER_MONTANT_DECAISSE
        defaultDecaisementBanqueShouldBeFound("montantDecaisse.greaterThan=" + SMALLER_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultDecaisementBanqueShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the decaisementBanqueList where commentaire equals to UPDATED_COMMENTAIRE
        defaultDecaisementBanqueShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultDecaisementBanqueShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the decaisementBanqueList where commentaire equals to UPDATED_COMMENTAIRE
        defaultDecaisementBanqueShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where commentaire is not null
        defaultDecaisementBanqueShouldBeFound("commentaire.specified=true");

        // Get all the decaisementBanqueList where commentaire is null
        defaultDecaisementBanqueShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where commentaire contains DEFAULT_COMMENTAIRE
        defaultDecaisementBanqueShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the decaisementBanqueList where commentaire contains UPDATED_COMMENTAIRE
        defaultDecaisementBanqueShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        // Get all the decaisementBanqueList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultDecaisementBanqueShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the decaisementBanqueList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultDecaisementBanqueShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaisementBanquesByCompteBanqueIsEqualToSomething() throws Exception {
        CompteBanque compteBanque;
        if (TestUtil.findAll(em, CompteBanque.class).isEmpty()) {
            decaisementBanqueRepository.saveAndFlush(decaisementBanque);
            compteBanque = CompteBanqueResourceIT.createEntity(em);
        } else {
            compteBanque = TestUtil.findAll(em, CompteBanque.class).get(0);
        }
        em.persist(compteBanque);
        em.flush();
        decaisementBanque.setCompteBanque(compteBanque);
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);
        Long compteBanqueId = compteBanque.getId();

        // Get all the decaisementBanqueList where compteBanque equals to compteBanqueId
        defaultDecaisementBanqueShouldBeFound("compteBanqueId.equals=" + compteBanqueId);

        // Get all the decaisementBanqueList where compteBanque equals to (compteBanqueId + 1)
        defaultDecaisementBanqueShouldNotBeFound("compteBanqueId.equals=" + (compteBanqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecaisementBanqueShouldBeFound(String filter) throws Exception {
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decaisementBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateDecaissement").value(hasItem(DEFAULT_DATE_DECAISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].montantDecaisse").value(hasItem(DEFAULT_MONTANT_DECAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecaisementBanqueShouldNotBeFound(String filter) throws Exception {
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecaisementBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecaisementBanque() throws Exception {
        // Get the decaisementBanque
        restDecaisementBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDecaisementBanque() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();

        // Update the decaisementBanque
        DecaisementBanque updatedDecaisementBanque = decaisementBanqueRepository.findById(decaisementBanque.getId()).get();
        // Disconnect from session so that the updates on updatedDecaisementBanque are not directly saved in db
        em.detach(updatedDecaisementBanque);
        updatedDecaisementBanque
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(updatedDecaisementBanque);

        restDecaisementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decaisementBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
        DecaisementBanque testDecaisementBanque = decaisementBanqueList.get(decaisementBanqueList.size() - 1);
        assertThat(testDecaisementBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDecaisementBanque.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDecaisementBanque.getDateDecaissement()).isEqualTo(UPDATED_DATE_DECAISSEMENT);
        assertThat(testDecaisementBanque.getMontantDecaisse()).isEqualTo(UPDATED_MONTANT_DECAISSE);
        assertThat(testDecaisementBanque.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decaisementBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecaisementBanqueWithPatch() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();

        // Update the decaisementBanque using partial update
        DecaisementBanque partialUpdatedDecaisementBanque = new DecaisementBanque();
        partialUpdatedDecaisementBanque.setId(decaisementBanque.getId());

        partialUpdatedDecaisementBanque.libelle(UPDATED_LIBELLE).commentaire(UPDATED_COMMENTAIRE);

        restDecaisementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecaisementBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecaisementBanque))
            )
            .andExpect(status().isOk());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
        DecaisementBanque testDecaisementBanque = decaisementBanqueList.get(decaisementBanqueList.size() - 1);
        assertThat(testDecaisementBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDecaisementBanque.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDecaisementBanque.getDateDecaissement()).isEqualTo(DEFAULT_DATE_DECAISSEMENT);
        assertThat(testDecaisementBanque.getMontantDecaisse()).isEqualTo(DEFAULT_MONTANT_DECAISSE);
        assertThat(testDecaisementBanque.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateDecaisementBanqueWithPatch() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();

        // Update the decaisementBanque using partial update
        DecaisementBanque partialUpdatedDecaisementBanque = new DecaisementBanque();
        partialUpdatedDecaisementBanque.setId(decaisementBanque.getId());

        partialUpdatedDecaisementBanque
            .libelle(UPDATED_LIBELLE)
            .montant(UPDATED_MONTANT)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);

        restDecaisementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecaisementBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecaisementBanque))
            )
            .andExpect(status().isOk());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
        DecaisementBanque testDecaisementBanque = decaisementBanqueList.get(decaisementBanqueList.size() - 1);
        assertThat(testDecaisementBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDecaisementBanque.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDecaisementBanque.getDateDecaissement()).isEqualTo(UPDATED_DATE_DECAISSEMENT);
        assertThat(testDecaisementBanque.getMontantDecaisse()).isEqualTo(UPDATED_MONTANT_DECAISSE);
        assertThat(testDecaisementBanque.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decaisementBanqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecaisementBanque() throws Exception {
        int databaseSizeBeforeUpdate = decaisementBanqueRepository.findAll().size();
        decaisementBanque.setId(count.incrementAndGet());

        // Create the DecaisementBanque
        DecaisementBanqueDTO decaisementBanqueDTO = decaisementBanqueMapper.toDto(decaisementBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaisementBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaisementBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecaisementBanque in the database
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecaisementBanque() throws Exception {
        // Initialize the database
        decaisementBanqueRepository.saveAndFlush(decaisementBanque);

        int databaseSizeBeforeDelete = decaisementBanqueRepository.findAll().size();

        // Delete the decaisementBanque
        restDecaisementBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, decaisementBanque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DecaisementBanque> decaisementBanqueList = decaisementBanqueRepository.findAll();
        assertThat(decaisementBanqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
