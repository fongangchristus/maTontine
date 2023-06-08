package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CommentairePot;
import com.it4innov.domain.ContributionPot;
import com.it4innov.domain.Pot;
import com.it4innov.domain.TypePot;
import com.it4innov.domain.enumeration.StatutPot;
import com.it4innov.repository.PotRepository;
import com.it4innov.service.criteria.PotCriteria;
import com.it4innov.service.dto.PotDTO;
import com.it4innov.service.mapper.PotMapper;
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
 * Integration tests for the {@link PotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PotResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_CODEPOT = "AAAAAAAAAA";
    private static final String UPDATED_CODEPOT = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_CIBLE = 1D;
    private static final Double UPDATED_MONTANT_CIBLE = 2D;
    private static final Double SMALLER_MONTANT_CIBLE = 1D - 1D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT_COLLECTE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT_COLLECTE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT_COLLECTE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN_COLLECTE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN_COLLECTE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN_COLLECTE = LocalDate.ofEpochDay(-1L);

    private static final StatutPot DEFAULT_STATUT = StatutPot.OUVERTE;
    private static final StatutPot UPDATED_STATUT = StatutPot.FERMEE;

    private static final String ENTITY_API_URL = "/api/pots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PotRepository potRepository;

    @Autowired
    private PotMapper potMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPotMockMvc;

    private Pot pot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pot createEntity(EntityManager em) {
        Pot pot = new Pot()
            .libele(DEFAULT_LIBELE)
            .codepot(DEFAULT_CODEPOT)
            .montantCible(DEFAULT_MONTANT_CIBLE)
            .description(DEFAULT_DESCRIPTION)
            .dateDebutCollecte(DEFAULT_DATE_DEBUT_COLLECTE)
            .dateFinCollecte(DEFAULT_DATE_FIN_COLLECTE)
            .statut(DEFAULT_STATUT);
        return pot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pot createUpdatedEntity(EntityManager em) {
        Pot pot = new Pot()
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantCible(UPDATED_MONTANT_CIBLE)
            .description(UPDATED_DESCRIPTION)
            .dateDebutCollecte(UPDATED_DATE_DEBUT_COLLECTE)
            .dateFinCollecte(UPDATED_DATE_FIN_COLLECTE)
            .statut(UPDATED_STATUT);
        return pot;
    }

    @BeforeEach
    public void initTest() {
        pot = createEntity(em);
    }

    @Test
    @Transactional
    void createPot() throws Exception {
        int databaseSizeBeforeCreate = potRepository.findAll().size();
        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);
        restPotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potDTO)))
            .andExpect(status().isCreated());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeCreate + 1);
        Pot testPot = potList.get(potList.size() - 1);
        assertThat(testPot.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testPot.getCodepot()).isEqualTo(DEFAULT_CODEPOT);
        assertThat(testPot.getMontantCible()).isEqualTo(DEFAULT_MONTANT_CIBLE);
        assertThat(testPot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPot.getDateDebutCollecte()).isEqualTo(DEFAULT_DATE_DEBUT_COLLECTE);
        assertThat(testPot.getDateFinCollecte()).isEqualTo(DEFAULT_DATE_FIN_COLLECTE);
        assertThat(testPot.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createPotWithExistingId() throws Exception {
        // Create the Pot with an existing ID
        pot.setId(1L);
        PotDTO potDTO = potMapper.toDto(pot);

        int databaseSizeBeforeCreate = potRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodepotIsRequired() throws Exception {
        int databaseSizeBeforeTest = potRepository.findAll().size();
        // set the field null
        pot.setCodepot(null);

        // Create the Pot, which fails.
        PotDTO potDTO = potMapper.toDto(pot);

        restPotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potDTO)))
            .andExpect(status().isBadRequest());

        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPots() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList
        restPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].codepot").value(hasItem(DEFAULT_CODEPOT)))
            .andExpect(jsonPath("$.[*].montantCible").value(hasItem(DEFAULT_MONTANT_CIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDebutCollecte").value(hasItem(DEFAULT_DATE_DEBUT_COLLECTE.toString())))
            .andExpect(jsonPath("$.[*].dateFinCollecte").value(hasItem(DEFAULT_DATE_FIN_COLLECTE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getPot() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get the pot
        restPotMockMvc
            .perform(get(ENTITY_API_URL_ID, pot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pot.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.codepot").value(DEFAULT_CODEPOT))
            .andExpect(jsonPath("$.montantCible").value(DEFAULT_MONTANT_CIBLE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateDebutCollecte").value(DEFAULT_DATE_DEBUT_COLLECTE.toString()))
            .andExpect(jsonPath("$.dateFinCollecte").value(DEFAULT_DATE_FIN_COLLECTE.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getPotsByIdFiltering() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        Long id = pot.getId();

        defaultPotShouldBeFound("id.equals=" + id);
        defaultPotShouldNotBeFound("id.notEquals=" + id);

        defaultPotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPotShouldNotBeFound("id.greaterThan=" + id);

        defaultPotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPotsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where libele equals to DEFAULT_LIBELE
        defaultPotShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the potList where libele equals to UPDATED_LIBELE
        defaultPotShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllPotsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultPotShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the potList where libele equals to UPDATED_LIBELE
        defaultPotShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllPotsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where libele is not null
        defaultPotShouldBeFound("libele.specified=true");

        // Get all the potList where libele is null
        defaultPotShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where libele contains DEFAULT_LIBELE
        defaultPotShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the potList where libele contains UPDATED_LIBELE
        defaultPotShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllPotsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where libele does not contain DEFAULT_LIBELE
        defaultPotShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the potList where libele does not contain UPDATED_LIBELE
        defaultPotShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllPotsByCodepotIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where codepot equals to DEFAULT_CODEPOT
        defaultPotShouldBeFound("codepot.equals=" + DEFAULT_CODEPOT);

        // Get all the potList where codepot equals to UPDATED_CODEPOT
        defaultPotShouldNotBeFound("codepot.equals=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllPotsByCodepotIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where codepot in DEFAULT_CODEPOT or UPDATED_CODEPOT
        defaultPotShouldBeFound("codepot.in=" + DEFAULT_CODEPOT + "," + UPDATED_CODEPOT);

        // Get all the potList where codepot equals to UPDATED_CODEPOT
        defaultPotShouldNotBeFound("codepot.in=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllPotsByCodepotIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where codepot is not null
        defaultPotShouldBeFound("codepot.specified=true");

        // Get all the potList where codepot is null
        defaultPotShouldNotBeFound("codepot.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByCodepotContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where codepot contains DEFAULT_CODEPOT
        defaultPotShouldBeFound("codepot.contains=" + DEFAULT_CODEPOT);

        // Get all the potList where codepot contains UPDATED_CODEPOT
        defaultPotShouldNotBeFound("codepot.contains=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllPotsByCodepotNotContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where codepot does not contain DEFAULT_CODEPOT
        defaultPotShouldNotBeFound("codepot.doesNotContain=" + DEFAULT_CODEPOT);

        // Get all the potList where codepot does not contain UPDATED_CODEPOT
        defaultPotShouldBeFound("codepot.doesNotContain=" + UPDATED_CODEPOT);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible equals to DEFAULT_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.equals=" + DEFAULT_MONTANT_CIBLE);

        // Get all the potList where montantCible equals to UPDATED_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.equals=" + UPDATED_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible in DEFAULT_MONTANT_CIBLE or UPDATED_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.in=" + DEFAULT_MONTANT_CIBLE + "," + UPDATED_MONTANT_CIBLE);

        // Get all the potList where montantCible equals to UPDATED_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.in=" + UPDATED_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible is not null
        defaultPotShouldBeFound("montantCible.specified=true");

        // Get all the potList where montantCible is null
        defaultPotShouldNotBeFound("montantCible.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible is greater than or equal to DEFAULT_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.greaterThanOrEqual=" + DEFAULT_MONTANT_CIBLE);

        // Get all the potList where montantCible is greater than or equal to UPDATED_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.greaterThanOrEqual=" + UPDATED_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible is less than or equal to DEFAULT_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.lessThanOrEqual=" + DEFAULT_MONTANT_CIBLE);

        // Get all the potList where montantCible is less than or equal to SMALLER_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.lessThanOrEqual=" + SMALLER_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsLessThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible is less than DEFAULT_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.lessThan=" + DEFAULT_MONTANT_CIBLE);

        // Get all the potList where montantCible is less than UPDATED_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.lessThan=" + UPDATED_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByMontantCibleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where montantCible is greater than DEFAULT_MONTANT_CIBLE
        defaultPotShouldNotBeFound("montantCible.greaterThan=" + DEFAULT_MONTANT_CIBLE);

        // Get all the potList where montantCible is greater than SMALLER_MONTANT_CIBLE
        defaultPotShouldBeFound("montantCible.greaterThan=" + SMALLER_MONTANT_CIBLE);
    }

    @Test
    @Transactional
    void getAllPotsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where description equals to DEFAULT_DESCRIPTION
        defaultPotShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the potList where description equals to UPDATED_DESCRIPTION
        defaultPotShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPotsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPotShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the potList where description equals to UPDATED_DESCRIPTION
        defaultPotShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPotsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where description is not null
        defaultPotShouldBeFound("description.specified=true");

        // Get all the potList where description is null
        defaultPotShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where description contains DEFAULT_DESCRIPTION
        defaultPotShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the potList where description contains UPDATED_DESCRIPTION
        defaultPotShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPotsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where description does not contain DEFAULT_DESCRIPTION
        defaultPotShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the potList where description does not contain UPDATED_DESCRIPTION
        defaultPotShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte equals to DEFAULT_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.equals=" + DEFAULT_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte equals to UPDATED_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.equals=" + UPDATED_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte in DEFAULT_DATE_DEBUT_COLLECTE or UPDATED_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.in=" + DEFAULT_DATE_DEBUT_COLLECTE + "," + UPDATED_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte equals to UPDATED_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.in=" + UPDATED_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte is not null
        defaultPotShouldBeFound("dateDebutCollecte.specified=true");

        // Get all the potList where dateDebutCollecte is null
        defaultPotShouldNotBeFound("dateDebutCollecte.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte is greater than or equal to DEFAULT_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte is greater than or equal to UPDATED_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.greaterThanOrEqual=" + UPDATED_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte is less than or equal to DEFAULT_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.lessThanOrEqual=" + DEFAULT_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte is less than or equal to SMALLER_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.lessThanOrEqual=" + SMALLER_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsLessThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte is less than DEFAULT_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.lessThan=" + DEFAULT_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte is less than UPDATED_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.lessThan=" + UPDATED_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateDebutCollecteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateDebutCollecte is greater than DEFAULT_DATE_DEBUT_COLLECTE
        defaultPotShouldNotBeFound("dateDebutCollecte.greaterThan=" + DEFAULT_DATE_DEBUT_COLLECTE);

        // Get all the potList where dateDebutCollecte is greater than SMALLER_DATE_DEBUT_COLLECTE
        defaultPotShouldBeFound("dateDebutCollecte.greaterThan=" + SMALLER_DATE_DEBUT_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte equals to DEFAULT_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.equals=" + DEFAULT_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte equals to UPDATED_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.equals=" + UPDATED_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte in DEFAULT_DATE_FIN_COLLECTE or UPDATED_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.in=" + DEFAULT_DATE_FIN_COLLECTE + "," + UPDATED_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte equals to UPDATED_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.in=" + UPDATED_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte is not null
        defaultPotShouldBeFound("dateFinCollecte.specified=true");

        // Get all the potList where dateFinCollecte is null
        defaultPotShouldNotBeFound("dateFinCollecte.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte is greater than or equal to DEFAULT_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.greaterThanOrEqual=" + DEFAULT_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte is greater than or equal to UPDATED_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.greaterThanOrEqual=" + UPDATED_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte is less than or equal to DEFAULT_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.lessThanOrEqual=" + DEFAULT_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte is less than or equal to SMALLER_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.lessThanOrEqual=" + SMALLER_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsLessThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte is less than DEFAULT_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.lessThan=" + DEFAULT_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte is less than UPDATED_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.lessThan=" + UPDATED_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByDateFinCollecteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where dateFinCollecte is greater than DEFAULT_DATE_FIN_COLLECTE
        defaultPotShouldNotBeFound("dateFinCollecte.greaterThan=" + DEFAULT_DATE_FIN_COLLECTE);

        // Get all the potList where dateFinCollecte is greater than SMALLER_DATE_FIN_COLLECTE
        defaultPotShouldBeFound("dateFinCollecte.greaterThan=" + SMALLER_DATE_FIN_COLLECTE);
    }

    @Test
    @Transactional
    void getAllPotsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where statut equals to DEFAULT_STATUT
        defaultPotShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the potList where statut equals to UPDATED_STATUT
        defaultPotShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllPotsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultPotShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the potList where statut equals to UPDATED_STATUT
        defaultPotShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllPotsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        // Get all the potList where statut is not null
        defaultPotShouldBeFound("statut.specified=true");

        // Get all the potList where statut is null
        defaultPotShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    void getAllPotsByContributionPotIsEqualToSomething() throws Exception {
        ContributionPot contributionPot;
        if (TestUtil.findAll(em, ContributionPot.class).isEmpty()) {
            potRepository.saveAndFlush(pot);
            contributionPot = ContributionPotResourceIT.createEntity(em);
        } else {
            contributionPot = TestUtil.findAll(em, ContributionPot.class).get(0);
        }
        em.persist(contributionPot);
        em.flush();
        pot.addContributionPot(contributionPot);
        potRepository.saveAndFlush(pot);
        Long contributionPotId = contributionPot.getId();

        // Get all the potList where contributionPot equals to contributionPotId
        defaultPotShouldBeFound("contributionPotId.equals=" + contributionPotId);

        // Get all the potList where contributionPot equals to (contributionPotId + 1)
        defaultPotShouldNotBeFound("contributionPotId.equals=" + (contributionPotId + 1));
    }

    @Test
    @Transactional
    void getAllPotsByCommentairePotIsEqualToSomething() throws Exception {
        CommentairePot commentairePot;
        if (TestUtil.findAll(em, CommentairePot.class).isEmpty()) {
            potRepository.saveAndFlush(pot);
            commentairePot = CommentairePotResourceIT.createEntity(em);
        } else {
            commentairePot = TestUtil.findAll(em, CommentairePot.class).get(0);
        }
        em.persist(commentairePot);
        em.flush();
        pot.addCommentairePot(commentairePot);
        potRepository.saveAndFlush(pot);
        Long commentairePotId = commentairePot.getId();

        // Get all the potList where commentairePot equals to commentairePotId
        defaultPotShouldBeFound("commentairePotId.equals=" + commentairePotId);

        // Get all the potList where commentairePot equals to (commentairePotId + 1)
        defaultPotShouldNotBeFound("commentairePotId.equals=" + (commentairePotId + 1));
    }

    @Test
    @Transactional
    void getAllPotsByTypePotIsEqualToSomething() throws Exception {
        TypePot typePot;
        if (TestUtil.findAll(em, TypePot.class).isEmpty()) {
            potRepository.saveAndFlush(pot);
            typePot = TypePotResourceIT.createEntity(em);
        } else {
            typePot = TestUtil.findAll(em, TypePot.class).get(0);
        }
        em.persist(typePot);
        em.flush();
        pot.setTypePot(typePot);
        potRepository.saveAndFlush(pot);
        Long typePotId = typePot.getId();

        // Get all the potList where typePot equals to typePotId
        defaultPotShouldBeFound("typePotId.equals=" + typePotId);

        // Get all the potList where typePot equals to (typePotId + 1)
        defaultPotShouldNotBeFound("typePotId.equals=" + (typePotId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPotShouldBeFound(String filter) throws Exception {
        restPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].codepot").value(hasItem(DEFAULT_CODEPOT)))
            .andExpect(jsonPath("$.[*].montantCible").value(hasItem(DEFAULT_MONTANT_CIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDebutCollecte").value(hasItem(DEFAULT_DATE_DEBUT_COLLECTE.toString())))
            .andExpect(jsonPath("$.[*].dateFinCollecte").value(hasItem(DEFAULT_DATE_FIN_COLLECTE.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restPotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPotShouldNotBeFound(String filter) throws Exception {
        restPotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPot() throws Exception {
        // Get the pot
        restPotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPot() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        int databaseSizeBeforeUpdate = potRepository.findAll().size();

        // Update the pot
        Pot updatedPot = potRepository.findById(pot.getId()).get();
        // Disconnect from session so that the updates on updatedPot are not directly saved in db
        em.detach(updatedPot);
        updatedPot
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantCible(UPDATED_MONTANT_CIBLE)
            .description(UPDATED_DESCRIPTION)
            .dateDebutCollecte(UPDATED_DATE_DEBUT_COLLECTE)
            .dateFinCollecte(UPDATED_DATE_FIN_COLLECTE)
            .statut(UPDATED_STATUT);
        PotDTO potDTO = potMapper.toDto(updatedPot);

        restPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, potDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(potDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
        Pot testPot = potList.get(potList.size() - 1);
        assertThat(testPot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testPot.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testPot.getMontantCible()).isEqualTo(UPDATED_MONTANT_CIBLE);
        assertThat(testPot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPot.getDateDebutCollecte()).isEqualTo(UPDATED_DATE_DEBUT_COLLECTE);
        assertThat(testPot.getDateFinCollecte()).isEqualTo(UPDATED_DATE_FIN_COLLECTE);
        assertThat(testPot.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, potDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(potDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(potDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(potDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePotWithPatch() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        int databaseSizeBeforeUpdate = potRepository.findAll().size();

        // Update the pot using partial update
        Pot partialUpdatedPot = new Pot();
        partialUpdatedPot.setId(pot.getId());

        partialUpdatedPot.libele(UPDATED_LIBELE).codepot(UPDATED_CODEPOT).dateFinCollecte(UPDATED_DATE_FIN_COLLECTE);

        restPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPot))
            )
            .andExpect(status().isOk());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
        Pot testPot = potList.get(potList.size() - 1);
        assertThat(testPot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testPot.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testPot.getMontantCible()).isEqualTo(DEFAULT_MONTANT_CIBLE);
        assertThat(testPot.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPot.getDateDebutCollecte()).isEqualTo(DEFAULT_DATE_DEBUT_COLLECTE);
        assertThat(testPot.getDateFinCollecte()).isEqualTo(UPDATED_DATE_FIN_COLLECTE);
        assertThat(testPot.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void fullUpdatePotWithPatch() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        int databaseSizeBeforeUpdate = potRepository.findAll().size();

        // Update the pot using partial update
        Pot partialUpdatedPot = new Pot();
        partialUpdatedPot.setId(pot.getId());

        partialUpdatedPot
            .libele(UPDATED_LIBELE)
            .codepot(UPDATED_CODEPOT)
            .montantCible(UPDATED_MONTANT_CIBLE)
            .description(UPDATED_DESCRIPTION)
            .dateDebutCollecte(UPDATED_DATE_DEBUT_COLLECTE)
            .dateFinCollecte(UPDATED_DATE_FIN_COLLECTE)
            .statut(UPDATED_STATUT);

        restPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPot))
            )
            .andExpect(status().isOk());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
        Pot testPot = potList.get(potList.size() - 1);
        assertThat(testPot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testPot.getCodepot()).isEqualTo(UPDATED_CODEPOT);
        assertThat(testPot.getMontantCible()).isEqualTo(UPDATED_MONTANT_CIBLE);
        assertThat(testPot.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPot.getDateDebutCollecte()).isEqualTo(UPDATED_DATE_DEBUT_COLLECTE);
        assertThat(testPot.getDateFinCollecte()).isEqualTo(UPDATED_DATE_FIN_COLLECTE);
        assertThat(testPot.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, potDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(potDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(potDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPot() throws Exception {
        int databaseSizeBeforeUpdate = potRepository.findAll().size();
        pot.setId(count.incrementAndGet());

        // Create the Pot
        PotDTO potDTO = potMapper.toDto(pot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(potDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pot in the database
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePot() throws Exception {
        // Initialize the database
        potRepository.saveAndFlush(pot);

        int databaseSizeBeforeDelete = potRepository.findAll().size();

        // Delete the pot
        restPotMockMvc.perform(delete(ENTITY_API_URL_ID, pot.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pot> potList = potRepository.findAll();
        assertThat(potList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
