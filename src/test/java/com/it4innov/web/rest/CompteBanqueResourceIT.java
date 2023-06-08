package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Banque;
import com.it4innov.domain.CompteBanque;
import com.it4innov.domain.CotisationBanque;
import com.it4innov.domain.DecaisementBanque;
import com.it4innov.repository.CompteBanqueRepository;
import com.it4innov.service.criteria.CompteBanqueCriteria;
import com.it4innov.service.dto.CompteBanqueDTO;
import com.it4innov.service.mapper.CompteBanqueMapper;
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
 * Integration tests for the {@link CompteBanqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompteBanqueResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_ADHERANT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ADHERANT = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTANT_DISPONNIBLE = 1D;
    private static final Double UPDATED_MONTANT_DISPONNIBLE = 2D;
    private static final Double SMALLER_MONTANT_DISPONNIBLE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/compte-banques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompteBanqueRepository compteBanqueRepository;

    @Autowired
    private CompteBanqueMapper compteBanqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompteBanqueMockMvc;

    private CompteBanque compteBanque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteBanque createEntity(EntityManager em) {
        CompteBanque compteBanque = new CompteBanque()
            .libelle(DEFAULT_LIBELLE)
            .description(DEFAULT_DESCRIPTION)
            .matriculeAdherant(DEFAULT_MATRICULE_ADHERANT)
            .montantDisponnible(DEFAULT_MONTANT_DISPONNIBLE);
        return compteBanque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompteBanque createUpdatedEntity(EntityManager em) {
        CompteBanque compteBanque = new CompteBanque()
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .matriculeAdherant(UPDATED_MATRICULE_ADHERANT)
            .montantDisponnible(UPDATED_MONTANT_DISPONNIBLE);
        return compteBanque;
    }

    @BeforeEach
    public void initTest() {
        compteBanque = createEntity(em);
    }

    @Test
    @Transactional
    void createCompteBanque() throws Exception {
        int databaseSizeBeforeCreate = compteBanqueRepository.findAll().size();
        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);
        restCompteBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeCreate + 1);
        CompteBanque testCompteBanque = compteBanqueList.get(compteBanqueList.size() - 1);
        assertThat(testCompteBanque.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testCompteBanque.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompteBanque.getMatriculeAdherant()).isEqualTo(DEFAULT_MATRICULE_ADHERANT);
        assertThat(testCompteBanque.getMontantDisponnible()).isEqualTo(DEFAULT_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void createCompteBanqueWithExistingId() throws Exception {
        // Create the CompteBanque with an existing ID
        compteBanque.setId(1L);
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        int databaseSizeBeforeCreate = compteBanqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompteBanqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompteBanques() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].matriculeAdherant").value(hasItem(DEFAULT_MATRICULE_ADHERANT)))
            .andExpect(jsonPath("$.[*].montantDisponnible").value(hasItem(DEFAULT_MONTANT_DISPONNIBLE.doubleValue())));
    }

    @Test
    @Transactional
    void getCompteBanque() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get the compteBanque
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL_ID, compteBanque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compteBanque.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.matriculeAdherant").value(DEFAULT_MATRICULE_ADHERANT))
            .andExpect(jsonPath("$.montantDisponnible").value(DEFAULT_MONTANT_DISPONNIBLE.doubleValue()));
    }

    @Test
    @Transactional
    void getCompteBanquesByIdFiltering() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        Long id = compteBanque.getId();

        defaultCompteBanqueShouldBeFound("id.equals=" + id);
        defaultCompteBanqueShouldNotBeFound("id.notEquals=" + id);

        defaultCompteBanqueShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompteBanqueShouldNotBeFound("id.greaterThan=" + id);

        defaultCompteBanqueShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompteBanqueShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where libelle equals to DEFAULT_LIBELLE
        defaultCompteBanqueShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the compteBanqueList where libelle equals to UPDATED_LIBELLE
        defaultCompteBanqueShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultCompteBanqueShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the compteBanqueList where libelle equals to UPDATED_LIBELLE
        defaultCompteBanqueShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where libelle is not null
        defaultCompteBanqueShouldBeFound("libelle.specified=true");

        // Get all the compteBanqueList where libelle is null
        defaultCompteBanqueShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteBanquesByLibelleContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where libelle contains DEFAULT_LIBELLE
        defaultCompteBanqueShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the compteBanqueList where libelle contains UPDATED_LIBELLE
        defaultCompteBanqueShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where libelle does not contain DEFAULT_LIBELLE
        defaultCompteBanqueShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the compteBanqueList where libelle does not contain UPDATED_LIBELLE
        defaultCompteBanqueShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where description equals to DEFAULT_DESCRIPTION
        defaultCompteBanqueShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the compteBanqueList where description equals to UPDATED_DESCRIPTION
        defaultCompteBanqueShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCompteBanqueShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the compteBanqueList where description equals to UPDATED_DESCRIPTION
        defaultCompteBanqueShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where description is not null
        defaultCompteBanqueShouldBeFound("description.specified=true");

        // Get all the compteBanqueList where description is null
        defaultCompteBanqueShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where description contains DEFAULT_DESCRIPTION
        defaultCompteBanqueShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the compteBanqueList where description contains UPDATED_DESCRIPTION
        defaultCompteBanqueShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where description does not contain DEFAULT_DESCRIPTION
        defaultCompteBanqueShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the compteBanqueList where description does not contain UPDATED_DESCRIPTION
        defaultCompteBanqueShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMatriculeAdherantIsEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where matriculeAdherant equals to DEFAULT_MATRICULE_ADHERANT
        defaultCompteBanqueShouldBeFound("matriculeAdherant.equals=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the compteBanqueList where matriculeAdherant equals to UPDATED_MATRICULE_ADHERANT
        defaultCompteBanqueShouldNotBeFound("matriculeAdherant.equals=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMatriculeAdherantIsInShouldWork() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where matriculeAdherant in DEFAULT_MATRICULE_ADHERANT or UPDATED_MATRICULE_ADHERANT
        defaultCompteBanqueShouldBeFound("matriculeAdherant.in=" + DEFAULT_MATRICULE_ADHERANT + "," + UPDATED_MATRICULE_ADHERANT);

        // Get all the compteBanqueList where matriculeAdherant equals to UPDATED_MATRICULE_ADHERANT
        defaultCompteBanqueShouldNotBeFound("matriculeAdherant.in=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMatriculeAdherantIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where matriculeAdherant is not null
        defaultCompteBanqueShouldBeFound("matriculeAdherant.specified=true");

        // Get all the compteBanqueList where matriculeAdherant is null
        defaultCompteBanqueShouldNotBeFound("matriculeAdherant.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMatriculeAdherantContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where matriculeAdherant contains DEFAULT_MATRICULE_ADHERANT
        defaultCompteBanqueShouldBeFound("matriculeAdherant.contains=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the compteBanqueList where matriculeAdherant contains UPDATED_MATRICULE_ADHERANT
        defaultCompteBanqueShouldNotBeFound("matriculeAdherant.contains=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMatriculeAdherantNotContainsSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where matriculeAdherant does not contain DEFAULT_MATRICULE_ADHERANT
        defaultCompteBanqueShouldNotBeFound("matriculeAdherant.doesNotContain=" + DEFAULT_MATRICULE_ADHERANT);

        // Get all the compteBanqueList where matriculeAdherant does not contain UPDATED_MATRICULE_ADHERANT
        defaultCompteBanqueShouldBeFound("matriculeAdherant.doesNotContain=" + UPDATED_MATRICULE_ADHERANT);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible equals to DEFAULT_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.equals=" + DEFAULT_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible equals to UPDATED_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.equals=" + UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsInShouldWork() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible in DEFAULT_MONTANT_DISPONNIBLE or UPDATED_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.in=" + DEFAULT_MONTANT_DISPONNIBLE + "," + UPDATED_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible equals to UPDATED_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.in=" + UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible is not null
        defaultCompteBanqueShouldBeFound("montantDisponnible.specified=true");

        // Get all the compteBanqueList where montantDisponnible is null
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.specified=false");
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible is greater than or equal to DEFAULT_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.greaterThanOrEqual=" + DEFAULT_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible is greater than or equal to UPDATED_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.greaterThanOrEqual=" + UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible is less than or equal to DEFAULT_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.lessThanOrEqual=" + DEFAULT_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible is less than or equal to SMALLER_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.lessThanOrEqual=" + SMALLER_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsLessThanSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible is less than DEFAULT_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.lessThan=" + DEFAULT_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible is less than UPDATED_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.lessThan=" + UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByMontantDisponnibleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        // Get all the compteBanqueList where montantDisponnible is greater than DEFAULT_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldNotBeFound("montantDisponnible.greaterThan=" + DEFAULT_MONTANT_DISPONNIBLE);

        // Get all the compteBanqueList where montantDisponnible is greater than SMALLER_MONTANT_DISPONNIBLE
        defaultCompteBanqueShouldBeFound("montantDisponnible.greaterThan=" + SMALLER_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void getAllCompteBanquesByCotisationBanqueIsEqualToSomething() throws Exception {
        CotisationBanque cotisationBanque;
        if (TestUtil.findAll(em, CotisationBanque.class).isEmpty()) {
            compteBanqueRepository.saveAndFlush(compteBanque);
            cotisationBanque = CotisationBanqueResourceIT.createEntity(em);
        } else {
            cotisationBanque = TestUtil.findAll(em, CotisationBanque.class).get(0);
        }
        em.persist(cotisationBanque);
        em.flush();
        compteBanque.addCotisationBanque(cotisationBanque);
        compteBanqueRepository.saveAndFlush(compteBanque);
        Long cotisationBanqueId = cotisationBanque.getId();

        // Get all the compteBanqueList where cotisationBanque equals to cotisationBanqueId
        defaultCompteBanqueShouldBeFound("cotisationBanqueId.equals=" + cotisationBanqueId);

        // Get all the compteBanqueList where cotisationBanque equals to (cotisationBanqueId + 1)
        defaultCompteBanqueShouldNotBeFound("cotisationBanqueId.equals=" + (cotisationBanqueId + 1));
    }

    @Test
    @Transactional
    void getAllCompteBanquesByDecaisementBanqueIsEqualToSomething() throws Exception {
        DecaisementBanque decaisementBanque;
        if (TestUtil.findAll(em, DecaisementBanque.class).isEmpty()) {
            compteBanqueRepository.saveAndFlush(compteBanque);
            decaisementBanque = DecaisementBanqueResourceIT.createEntity(em);
        } else {
            decaisementBanque = TestUtil.findAll(em, DecaisementBanque.class).get(0);
        }
        em.persist(decaisementBanque);
        em.flush();
        compteBanque.addDecaisementBanque(decaisementBanque);
        compteBanqueRepository.saveAndFlush(compteBanque);
        Long decaisementBanqueId = decaisementBanque.getId();

        // Get all the compteBanqueList where decaisementBanque equals to decaisementBanqueId
        defaultCompteBanqueShouldBeFound("decaisementBanqueId.equals=" + decaisementBanqueId);

        // Get all the compteBanqueList where decaisementBanque equals to (decaisementBanqueId + 1)
        defaultCompteBanqueShouldNotBeFound("decaisementBanqueId.equals=" + (decaisementBanqueId + 1));
    }

    @Test
    @Transactional
    void getAllCompteBanquesByBanqueIsEqualToSomething() throws Exception {
        Banque banque;
        if (TestUtil.findAll(em, Banque.class).isEmpty()) {
            compteBanqueRepository.saveAndFlush(compteBanque);
            banque = BanqueResourceIT.createEntity(em);
        } else {
            banque = TestUtil.findAll(em, Banque.class).get(0);
        }
        em.persist(banque);
        em.flush();
        compteBanque.setBanque(banque);
        compteBanqueRepository.saveAndFlush(compteBanque);
        Long banqueId = banque.getId();

        // Get all the compteBanqueList where banque equals to banqueId
        defaultCompteBanqueShouldBeFound("banqueId.equals=" + banqueId);

        // Get all the compteBanqueList where banque equals to (banqueId + 1)
        defaultCompteBanqueShouldNotBeFound("banqueId.equals=" + (banqueId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompteBanqueShouldBeFound(String filter) throws Exception {
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compteBanque.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].matriculeAdherant").value(hasItem(DEFAULT_MATRICULE_ADHERANT)))
            .andExpect(jsonPath("$.[*].montantDisponnible").value(hasItem(DEFAULT_MONTANT_DISPONNIBLE.doubleValue())));

        // Check, that the count call also returns 1
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompteBanqueShouldNotBeFound(String filter) throws Exception {
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompteBanqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompteBanque() throws Exception {
        // Get the compteBanque
        restCompteBanqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompteBanque() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();

        // Update the compteBanque
        CompteBanque updatedCompteBanque = compteBanqueRepository.findById(compteBanque.getId()).get();
        // Disconnect from session so that the updates on updatedCompteBanque are not directly saved in db
        em.detach(updatedCompteBanque);
        updatedCompteBanque
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .matriculeAdherant(UPDATED_MATRICULE_ADHERANT)
            .montantDisponnible(UPDATED_MONTANT_DISPONNIBLE);
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(updatedCompteBanque);

        restCompteBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
        CompteBanque testCompteBanque = compteBanqueList.get(compteBanqueList.size() - 1);
        assertThat(testCompteBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCompteBanque.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompteBanque.getMatriculeAdherant()).isEqualTo(UPDATED_MATRICULE_ADHERANT);
        assertThat(testCompteBanque.getMontantDisponnible()).isEqualTo(UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void putNonExistingCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compteBanqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompteBanqueWithPatch() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();

        // Update the compteBanque using partial update
        CompteBanque partialUpdatedCompteBanque = new CompteBanque();
        partialUpdatedCompteBanque.setId(compteBanque.getId());

        partialUpdatedCompteBanque.libelle(UPDATED_LIBELLE);

        restCompteBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteBanque))
            )
            .andExpect(status().isOk());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
        CompteBanque testCompteBanque = compteBanqueList.get(compteBanqueList.size() - 1);
        assertThat(testCompteBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCompteBanque.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCompteBanque.getMatriculeAdherant()).isEqualTo(DEFAULT_MATRICULE_ADHERANT);
        assertThat(testCompteBanque.getMontantDisponnible()).isEqualTo(DEFAULT_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void fullUpdateCompteBanqueWithPatch() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();

        // Update the compteBanque using partial update
        CompteBanque partialUpdatedCompteBanque = new CompteBanque();
        partialUpdatedCompteBanque.setId(compteBanque.getId());

        partialUpdatedCompteBanque
            .libelle(UPDATED_LIBELLE)
            .description(UPDATED_DESCRIPTION)
            .matriculeAdherant(UPDATED_MATRICULE_ADHERANT)
            .montantDisponnible(UPDATED_MONTANT_DISPONNIBLE);

        restCompteBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompteBanque.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompteBanque))
            )
            .andExpect(status().isOk());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
        CompteBanque testCompteBanque = compteBanqueList.get(compteBanqueList.size() - 1);
        assertThat(testCompteBanque.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testCompteBanque.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCompteBanque.getMatriculeAdherant()).isEqualTo(UPDATED_MATRICULE_ADHERANT);
        assertThat(testCompteBanque.getMontantDisponnible()).isEqualTo(UPDATED_MONTANT_DISPONNIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compteBanqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompteBanque() throws Exception {
        int databaseSizeBeforeUpdate = compteBanqueRepository.findAll().size();
        compteBanque.setId(count.incrementAndGet());

        // Create the CompteBanque
        CompteBanqueDTO compteBanqueDTO = compteBanqueMapper.toDto(compteBanque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompteBanqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compteBanqueDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompteBanque in the database
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompteBanque() throws Exception {
        // Initialize the database
        compteBanqueRepository.saveAndFlush(compteBanque);

        int databaseSizeBeforeDelete = compteBanqueRepository.findAll().size();

        // Delete the compteBanque
        restCompteBanqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, compteBanque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompteBanque> compteBanqueList = compteBanqueRepository.findAll();
        assertThat(compteBanqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
