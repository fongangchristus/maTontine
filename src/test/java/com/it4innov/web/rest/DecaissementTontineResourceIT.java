package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CompteTontine;
import com.it4innov.domain.DecaissementTontine;
import com.it4innov.domain.PaiementTontine;
import com.it4innov.domain.SessionTontine;
import com.it4innov.repository.DecaissementTontineRepository;
import com.it4innov.service.criteria.DecaissementTontineCriteria;
import com.it4innov.service.dto.DecaissementTontineDTO;
import com.it4innov.service.mapper.DecaissementTontineMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DecaissementTontineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecaissementTontineResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DECAISSEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DECAISSEMENT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DECAISSEMENT = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_MONTANT_DECAISSE = 1D;
    private static final Double UPDATED_MONTANT_DECAISSE = 2D;
    private static final Double SMALLER_MONTANT_DECAISSE = 1D - 1D;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/decaissement-tontines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DecaissementTontineRepository decaissementTontineRepository;

    @Autowired
    private DecaissementTontineMapper decaissementTontineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecaissementTontineMockMvc;

    private DecaissementTontine decaissementTontine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecaissementTontine createEntity(EntityManager em) {
        DecaissementTontine decaissementTontine = new DecaissementTontine()
            .libele(DEFAULT_LIBELE)
            .dateDecaissement(DEFAULT_DATE_DECAISSEMENT)
            .montantDecaisse(DEFAULT_MONTANT_DECAISSE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return decaissementTontine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecaissementTontine createUpdatedEntity(EntityManager em) {
        DecaissementTontine decaissementTontine = new DecaissementTontine()
            .libele(UPDATED_LIBELE)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);
        return decaissementTontine;
    }

    @BeforeEach
    public void initTest() {
        decaissementTontine = createEntity(em);
    }

    @Test
    @Transactional
    void createDecaissementTontine() throws Exception {
        int databaseSizeBeforeCreate = decaissementTontineRepository.findAll().size();
        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);
        restDecaissementTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeCreate + 1);
        DecaissementTontine testDecaissementTontine = decaissementTontineList.get(decaissementTontineList.size() - 1);
        assertThat(testDecaissementTontine.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testDecaissementTontine.getDateDecaissement()).isEqualTo(DEFAULT_DATE_DECAISSEMENT);
        assertThat(testDecaissementTontine.getMontantDecaisse()).isEqualTo(DEFAULT_MONTANT_DECAISSE);
        assertThat(testDecaissementTontine.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createDecaissementTontineWithExistingId() throws Exception {
        // Create the DecaissementTontine with an existing ID
        decaissementTontine.setId(1L);
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        int databaseSizeBeforeCreate = decaissementTontineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecaissementTontineMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDecaissementTontines() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decaissementTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].dateDecaissement").value(hasItem(DEFAULT_DATE_DECAISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].montantDecaisse").value(hasItem(DEFAULT_MONTANT_DECAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getDecaissementTontine() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get the decaissementTontine
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL_ID, decaissementTontine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decaissementTontine.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.dateDecaissement").value(DEFAULT_DATE_DECAISSEMENT.toString()))
            .andExpect(jsonPath("$.montantDecaisse").value(DEFAULT_MONTANT_DECAISSE.doubleValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getDecaissementTontinesByIdFiltering() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        Long id = decaissementTontine.getId();

        defaultDecaissementTontineShouldBeFound("id.equals=" + id);
        defaultDecaissementTontineShouldNotBeFound("id.notEquals=" + id);

        defaultDecaissementTontineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDecaissementTontineShouldNotBeFound("id.greaterThan=" + id);

        defaultDecaissementTontineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDecaissementTontineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where libele equals to DEFAULT_LIBELE
        defaultDecaissementTontineShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the decaissementTontineList where libele equals to UPDATED_LIBELE
        defaultDecaissementTontineShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultDecaissementTontineShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the decaissementTontineList where libele equals to UPDATED_LIBELE
        defaultDecaissementTontineShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where libele is not null
        defaultDecaissementTontineShouldBeFound("libele.specified=true");

        // Get all the decaissementTontineList where libele is null
        defaultDecaissementTontineShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where libele contains DEFAULT_LIBELE
        defaultDecaissementTontineShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the decaissementTontineList where libele contains UPDATED_LIBELE
        defaultDecaissementTontineShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where libele does not contain DEFAULT_LIBELE
        defaultDecaissementTontineShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the decaissementTontineList where libele does not contain UPDATED_LIBELE
        defaultDecaissementTontineShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement equals to DEFAULT_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.equals=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement equals to UPDATED_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.equals=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsInShouldWork() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement in DEFAULT_DATE_DECAISSEMENT or UPDATED_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.in=" + DEFAULT_DATE_DECAISSEMENT + "," + UPDATED_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement equals to UPDATED_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.in=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement is not null
        defaultDecaissementTontineShouldBeFound("dateDecaissement.specified=true");

        // Get all the decaissementTontineList where dateDecaissement is null
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement is greater than or equal to DEFAULT_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.greaterThanOrEqual=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement is greater than or equal to UPDATED_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.greaterThanOrEqual=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement is less than or equal to DEFAULT_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.lessThanOrEqual=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement is less than or equal to SMALLER_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.lessThanOrEqual=" + SMALLER_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsLessThanSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement is less than DEFAULT_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.lessThan=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement is less than UPDATED_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.lessThan=" + UPDATED_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByDateDecaissementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where dateDecaissement is greater than DEFAULT_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldNotBeFound("dateDecaissement.greaterThan=" + DEFAULT_DATE_DECAISSEMENT);

        // Get all the decaissementTontineList where dateDecaissement is greater than SMALLER_DATE_DECAISSEMENT
        defaultDecaissementTontineShouldBeFound("dateDecaissement.greaterThan=" + SMALLER_DATE_DECAISSEMENT);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse equals to DEFAULT_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.equals=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse equals to UPDATED_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.equals=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsInShouldWork() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse in DEFAULT_MONTANT_DECAISSE or UPDATED_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.in=" + DEFAULT_MONTANT_DECAISSE + "," + UPDATED_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse equals to UPDATED_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.in=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse is not null
        defaultDecaissementTontineShouldBeFound("montantDecaisse.specified=true");

        // Get all the decaissementTontineList where montantDecaisse is null
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse is greater than or equal to DEFAULT_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.greaterThanOrEqual=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse is greater than or equal to UPDATED_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.greaterThanOrEqual=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse is less than or equal to DEFAULT_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.lessThanOrEqual=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse is less than or equal to SMALLER_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.lessThanOrEqual=" + SMALLER_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsLessThanSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse is less than DEFAULT_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.lessThan=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse is less than UPDATED_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.lessThan=" + UPDATED_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByMontantDecaisseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where montantDecaisse is greater than DEFAULT_MONTANT_DECAISSE
        defaultDecaissementTontineShouldNotBeFound("montantDecaisse.greaterThan=" + DEFAULT_MONTANT_DECAISSE);

        // Get all the decaissementTontineList where montantDecaisse is greater than SMALLER_MONTANT_DECAISSE
        defaultDecaissementTontineShouldBeFound("montantDecaisse.greaterThan=" + SMALLER_MONTANT_DECAISSE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultDecaissementTontineShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the decaissementTontineList where commentaire equals to UPDATED_COMMENTAIRE
        defaultDecaissementTontineShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultDecaissementTontineShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the decaissementTontineList where commentaire equals to UPDATED_COMMENTAIRE
        defaultDecaissementTontineShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where commentaire is not null
        defaultDecaissementTontineShouldBeFound("commentaire.specified=true");

        // Get all the decaissementTontineList where commentaire is null
        defaultDecaissementTontineShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where commentaire contains DEFAULT_COMMENTAIRE
        defaultDecaissementTontineShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the decaissementTontineList where commentaire contains UPDATED_COMMENTAIRE
        defaultDecaissementTontineShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        // Get all the decaissementTontineList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultDecaissementTontineShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the decaissementTontineList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultDecaissementTontineShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByPaiementTontineIsEqualToSomething() throws Exception {
        PaiementTontine paiementTontine;
        if (TestUtil.findAll(em, PaiementTontine.class).isEmpty()) {
            decaissementTontineRepository.saveAndFlush(decaissementTontine);
            paiementTontine = PaiementTontineResourceIT.createEntity(em);
        } else {
            paiementTontine = TestUtil.findAll(em, PaiementTontine.class).get(0);
        }
        em.persist(paiementTontine);
        em.flush();
        decaissementTontine.addPaiementTontine(paiementTontine);
        decaissementTontineRepository.saveAndFlush(decaissementTontine);
        Long paiementTontineId = paiementTontine.getId();

        // Get all the decaissementTontineList where paiementTontine equals to paiementTontineId
        defaultDecaissementTontineShouldBeFound("paiementTontineId.equals=" + paiementTontineId);

        // Get all the decaissementTontineList where paiementTontine equals to (paiementTontineId + 1)
        defaultDecaissementTontineShouldNotBeFound("paiementTontineId.equals=" + (paiementTontineId + 1));
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByTontineIsEqualToSomething() throws Exception {
        SessionTontine tontine;
        if (TestUtil.findAll(em, SessionTontine.class).isEmpty()) {
            decaissementTontineRepository.saveAndFlush(decaissementTontine);
            tontine = SessionTontineResourceIT.createEntity(em);
        } else {
            tontine = TestUtil.findAll(em, SessionTontine.class).get(0);
        }
        em.persist(tontine);
        em.flush();
        decaissementTontine.setTontine(tontine);
        decaissementTontineRepository.saveAndFlush(decaissementTontine);
        Long tontineId = tontine.getId();

        // Get all the decaissementTontineList where tontine equals to tontineId
        defaultDecaissementTontineShouldBeFound("tontineId.equals=" + tontineId);

        // Get all the decaissementTontineList where tontine equals to (tontineId + 1)
        defaultDecaissementTontineShouldNotBeFound("tontineId.equals=" + (tontineId + 1));
    }

    @Test
    @Transactional
    void getAllDecaissementTontinesByCompteTontineIsEqualToSomething() throws Exception {
        CompteTontine compteTontine;
        if (TestUtil.findAll(em, CompteTontine.class).isEmpty()) {
            decaissementTontineRepository.saveAndFlush(decaissementTontine);
            compteTontine = CompteTontineResourceIT.createEntity(em);
        } else {
            compteTontine = TestUtil.findAll(em, CompteTontine.class).get(0);
        }
        em.persist(compteTontine);
        em.flush();
        decaissementTontine.setCompteTontine(compteTontine);
        decaissementTontineRepository.saveAndFlush(decaissementTontine);
        Long compteTontineId = compteTontine.getId();

        // Get all the decaissementTontineList where compteTontine equals to compteTontineId
        defaultDecaissementTontineShouldBeFound("compteTontineId.equals=" + compteTontineId);

        // Get all the decaissementTontineList where compteTontine equals to (compteTontineId + 1)
        defaultDecaissementTontineShouldNotBeFound("compteTontineId.equals=" + (compteTontineId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecaissementTontineShouldBeFound(String filter) throws Exception {
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decaissementTontine.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].dateDecaissement").value(hasItem(DEFAULT_DATE_DECAISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].montantDecaisse").value(hasItem(DEFAULT_MONTANT_DECAISSE.doubleValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecaissementTontineShouldNotBeFound(String filter) throws Exception {
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecaissementTontineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecaissementTontine() throws Exception {
        // Get the decaissementTontine
        restDecaissementTontineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDecaissementTontine() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();

        // Update the decaissementTontine
        DecaissementTontine updatedDecaissementTontine = decaissementTontineRepository.findById(decaissementTontine.getId()).get();
        // Disconnect from session so that the updates on updatedDecaissementTontine are not directly saved in db
        em.detach(updatedDecaissementTontine);
        updatedDecaissementTontine
            .libele(UPDATED_LIBELE)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(updatedDecaissementTontine);

        restDecaissementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decaissementTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isOk());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
        DecaissementTontine testDecaissementTontine = decaissementTontineList.get(decaissementTontineList.size() - 1);
        assertThat(testDecaissementTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDecaissementTontine.getDateDecaissement()).isEqualTo(UPDATED_DATE_DECAISSEMENT);
        assertThat(testDecaissementTontine.getMontantDecaisse()).isEqualTo(UPDATED_MONTANT_DECAISSE);
        assertThat(testDecaissementTontine.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decaissementTontineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecaissementTontineWithPatch() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();

        // Update the decaissementTontine using partial update
        DecaissementTontine partialUpdatedDecaissementTontine = new DecaissementTontine();
        partialUpdatedDecaissementTontine.setId(decaissementTontine.getId());

        partialUpdatedDecaissementTontine.libele(UPDATED_LIBELE).dateDecaissement(UPDATED_DATE_DECAISSEMENT);

        restDecaissementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecaissementTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecaissementTontine))
            )
            .andExpect(status().isOk());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
        DecaissementTontine testDecaissementTontine = decaissementTontineList.get(decaissementTontineList.size() - 1);
        assertThat(testDecaissementTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDecaissementTontine.getDateDecaissement()).isEqualTo(UPDATED_DATE_DECAISSEMENT);
        assertThat(testDecaissementTontine.getMontantDecaisse()).isEqualTo(DEFAULT_MONTANT_DECAISSE);
        assertThat(testDecaissementTontine.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateDecaissementTontineWithPatch() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();

        // Update the decaissementTontine using partial update
        DecaissementTontine partialUpdatedDecaissementTontine = new DecaissementTontine();
        partialUpdatedDecaissementTontine.setId(decaissementTontine.getId());

        partialUpdatedDecaissementTontine
            .libele(UPDATED_LIBELE)
            .dateDecaissement(UPDATED_DATE_DECAISSEMENT)
            .montantDecaisse(UPDATED_MONTANT_DECAISSE)
            .commentaire(UPDATED_COMMENTAIRE);

        restDecaissementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecaissementTontine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDecaissementTontine))
            )
            .andExpect(status().isOk());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
        DecaissementTontine testDecaissementTontine = decaissementTontineList.get(decaissementTontineList.size() - 1);
        assertThat(testDecaissementTontine.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDecaissementTontine.getDateDecaissement()).isEqualTo(UPDATED_DATE_DECAISSEMENT);
        assertThat(testDecaissementTontine.getMontantDecaisse()).isEqualTo(UPDATED_MONTANT_DECAISSE);
        assertThat(testDecaissementTontine.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decaissementTontineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecaissementTontine() throws Exception {
        int databaseSizeBeforeUpdate = decaissementTontineRepository.findAll().size();
        decaissementTontine.setId(count.incrementAndGet());

        // Create the DecaissementTontine
        DecaissementTontineDTO decaissementTontineDTO = decaissementTontineMapper.toDto(decaissementTontine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecaissementTontineMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(decaissementTontineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecaissementTontine in the database
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecaissementTontine() throws Exception {
        // Initialize the database
        decaissementTontineRepository.saveAndFlush(decaissementTontine);

        int databaseSizeBeforeDelete = decaissementTontineRepository.findAll().size();

        // Delete the decaissementTontine
        restDecaissementTontineMockMvc
            .perform(delete(ENTITY_API_URL_ID, decaissementTontine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DecaissementTontine> decaissementTontineList = decaissementTontineRepository.findAll();
        assertThat(decaissementTontineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
