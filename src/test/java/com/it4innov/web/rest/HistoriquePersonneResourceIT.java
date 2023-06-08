package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.HistoriquePersonne;
import com.it4innov.domain.Personne;
import com.it4innov.repository.HistoriquePersonneRepository;
import com.it4innov.service.criteria.HistoriquePersonneCriteria;
import com.it4innov.service.dto.HistoriquePersonneDTO;
import com.it4innov.service.mapper.HistoriquePersonneMapper;
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
 * Integration tests for the {@link HistoriquePersonneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriquePersonneResourceIT {

    private static final Instant DEFAULT_DATE_ACTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ACTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MATRICULE_PERSONNE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_PERSONNE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historique-personnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HistoriquePersonneRepository historiquePersonneRepository;

    @Autowired
    private HistoriquePersonneMapper historiquePersonneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriquePersonneMockMvc;

    private HistoriquePersonne historiquePersonne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriquePersonne createEntity(EntityManager em) {
        HistoriquePersonne historiquePersonne = new HistoriquePersonne()
            .dateAction(DEFAULT_DATE_ACTION)
            .matriculePersonne(DEFAULT_MATRICULE_PERSONNE)
            .action(DEFAULT_ACTION)
            .result(DEFAULT_RESULT)
            .description(DEFAULT_DESCRIPTION);
        return historiquePersonne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriquePersonne createUpdatedEntity(EntityManager em) {
        HistoriquePersonne historiquePersonne = new HistoriquePersonne()
            .dateAction(UPDATED_DATE_ACTION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .action(UPDATED_ACTION)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION);
        return historiquePersonne;
    }

    @BeforeEach
    public void initTest() {
        historiquePersonne = createEntity(em);
    }

    @Test
    @Transactional
    void createHistoriquePersonne() throws Exception {
        int databaseSizeBeforeCreate = historiquePersonneRepository.findAll().size();
        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);
        restHistoriquePersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isCreated());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeCreate + 1);
        HistoriquePersonne testHistoriquePersonne = historiquePersonneList.get(historiquePersonneList.size() - 1);
        assertThat(testHistoriquePersonne.getDateAction()).isEqualTo(DEFAULT_DATE_ACTION);
        assertThat(testHistoriquePersonne.getMatriculePersonne()).isEqualTo(DEFAULT_MATRICULE_PERSONNE);
        assertThat(testHistoriquePersonne.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testHistoriquePersonne.getResult()).isEqualTo(DEFAULT_RESULT);
        assertThat(testHistoriquePersonne.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createHistoriquePersonneWithExistingId() throws Exception {
        // Create the HistoriquePersonne with an existing ID
        historiquePersonne.setId(1L);
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        int databaseSizeBeforeCreate = historiquePersonneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriquePersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculePersonneIsRequired() throws Exception {
        int databaseSizeBeforeTest = historiquePersonneRepository.findAll().size();
        // set the field null
        historiquePersonne.setMatriculePersonne(null);

        // Create the HistoriquePersonne, which fails.
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        restHistoriquePersonneMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnes() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiquePersonne.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(DEFAULT_DATE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].matriculePersonne").value(hasItem(DEFAULT_MATRICULE_PERSONNE)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getHistoriquePersonne() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get the historiquePersonne
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL_ID, historiquePersonne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiquePersonne.getId().intValue()))
            .andExpect(jsonPath("$.dateAction").value(DEFAULT_DATE_ACTION.toString()))
            .andExpect(jsonPath("$.matriculePersonne").value(DEFAULT_MATRICULE_PERSONNE))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getHistoriquePersonnesByIdFiltering() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        Long id = historiquePersonne.getId();

        defaultHistoriquePersonneShouldBeFound("id.equals=" + id);
        defaultHistoriquePersonneShouldNotBeFound("id.notEquals=" + id);

        defaultHistoriquePersonneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHistoriquePersonneShouldNotBeFound("id.greaterThan=" + id);

        defaultHistoriquePersonneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHistoriquePersonneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDateActionIsEqualToSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where dateAction equals to DEFAULT_DATE_ACTION
        defaultHistoriquePersonneShouldBeFound("dateAction.equals=" + DEFAULT_DATE_ACTION);

        // Get all the historiquePersonneList where dateAction equals to UPDATED_DATE_ACTION
        defaultHistoriquePersonneShouldNotBeFound("dateAction.equals=" + UPDATED_DATE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDateActionIsInShouldWork() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where dateAction in DEFAULT_DATE_ACTION or UPDATED_DATE_ACTION
        defaultHistoriquePersonneShouldBeFound("dateAction.in=" + DEFAULT_DATE_ACTION + "," + UPDATED_DATE_ACTION);

        // Get all the historiquePersonneList where dateAction equals to UPDATED_DATE_ACTION
        defaultHistoriquePersonneShouldNotBeFound("dateAction.in=" + UPDATED_DATE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDateActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where dateAction is not null
        defaultHistoriquePersonneShouldBeFound("dateAction.specified=true");

        // Get all the historiquePersonneList where dateAction is null
        defaultHistoriquePersonneShouldNotBeFound("dateAction.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByMatriculePersonneIsEqualToSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where matriculePersonne equals to DEFAULT_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldBeFound("matriculePersonne.equals=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the historiquePersonneList where matriculePersonne equals to UPDATED_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldNotBeFound("matriculePersonne.equals=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByMatriculePersonneIsInShouldWork() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where matriculePersonne in DEFAULT_MATRICULE_PERSONNE or UPDATED_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldBeFound("matriculePersonne.in=" + DEFAULT_MATRICULE_PERSONNE + "," + UPDATED_MATRICULE_PERSONNE);

        // Get all the historiquePersonneList where matriculePersonne equals to UPDATED_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldNotBeFound("matriculePersonne.in=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByMatriculePersonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where matriculePersonne is not null
        defaultHistoriquePersonneShouldBeFound("matriculePersonne.specified=true");

        // Get all the historiquePersonneList where matriculePersonne is null
        defaultHistoriquePersonneShouldNotBeFound("matriculePersonne.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByMatriculePersonneContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where matriculePersonne contains DEFAULT_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldBeFound("matriculePersonne.contains=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the historiquePersonneList where matriculePersonne contains UPDATED_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldNotBeFound("matriculePersonne.contains=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByMatriculePersonneNotContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where matriculePersonne does not contain DEFAULT_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldNotBeFound("matriculePersonne.doesNotContain=" + DEFAULT_MATRICULE_PERSONNE);

        // Get all the historiquePersonneList where matriculePersonne does not contain UPDATED_MATRICULE_PERSONNE
        defaultHistoriquePersonneShouldBeFound("matriculePersonne.doesNotContain=" + UPDATED_MATRICULE_PERSONNE);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where action equals to DEFAULT_ACTION
        defaultHistoriquePersonneShouldBeFound("action.equals=" + DEFAULT_ACTION);

        // Get all the historiquePersonneList where action equals to UPDATED_ACTION
        defaultHistoriquePersonneShouldNotBeFound("action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByActionIsInShouldWork() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where action in DEFAULT_ACTION or UPDATED_ACTION
        defaultHistoriquePersonneShouldBeFound("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION);

        // Get all the historiquePersonneList where action equals to UPDATED_ACTION
        defaultHistoriquePersonneShouldNotBeFound("action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where action is not null
        defaultHistoriquePersonneShouldBeFound("action.specified=true");

        // Get all the historiquePersonneList where action is null
        defaultHistoriquePersonneShouldNotBeFound("action.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByActionContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where action contains DEFAULT_ACTION
        defaultHistoriquePersonneShouldBeFound("action.contains=" + DEFAULT_ACTION);

        // Get all the historiquePersonneList where action contains UPDATED_ACTION
        defaultHistoriquePersonneShouldNotBeFound("action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByActionNotContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where action does not contain DEFAULT_ACTION
        defaultHistoriquePersonneShouldNotBeFound("action.doesNotContain=" + DEFAULT_ACTION);

        // Get all the historiquePersonneList where action does not contain UPDATED_ACTION
        defaultHistoriquePersonneShouldBeFound("action.doesNotContain=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByResultIsEqualToSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where result equals to DEFAULT_RESULT
        defaultHistoriquePersonneShouldBeFound("result.equals=" + DEFAULT_RESULT);

        // Get all the historiquePersonneList where result equals to UPDATED_RESULT
        defaultHistoriquePersonneShouldNotBeFound("result.equals=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByResultIsInShouldWork() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where result in DEFAULT_RESULT or UPDATED_RESULT
        defaultHistoriquePersonneShouldBeFound("result.in=" + DEFAULT_RESULT + "," + UPDATED_RESULT);

        // Get all the historiquePersonneList where result equals to UPDATED_RESULT
        defaultHistoriquePersonneShouldNotBeFound("result.in=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByResultIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where result is not null
        defaultHistoriquePersonneShouldBeFound("result.specified=true");

        // Get all the historiquePersonneList where result is null
        defaultHistoriquePersonneShouldNotBeFound("result.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByResultContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where result contains DEFAULT_RESULT
        defaultHistoriquePersonneShouldBeFound("result.contains=" + DEFAULT_RESULT);

        // Get all the historiquePersonneList where result contains UPDATED_RESULT
        defaultHistoriquePersonneShouldNotBeFound("result.contains=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByResultNotContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where result does not contain DEFAULT_RESULT
        defaultHistoriquePersonneShouldNotBeFound("result.doesNotContain=" + DEFAULT_RESULT);

        // Get all the historiquePersonneList where result does not contain UPDATED_RESULT
        defaultHistoriquePersonneShouldBeFound("result.doesNotContain=" + UPDATED_RESULT);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where description equals to DEFAULT_DESCRIPTION
        defaultHistoriquePersonneShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the historiquePersonneList where description equals to UPDATED_DESCRIPTION
        defaultHistoriquePersonneShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultHistoriquePersonneShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the historiquePersonneList where description equals to UPDATED_DESCRIPTION
        defaultHistoriquePersonneShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where description is not null
        defaultHistoriquePersonneShouldBeFound("description.specified=true");

        // Get all the historiquePersonneList where description is null
        defaultHistoriquePersonneShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where description contains DEFAULT_DESCRIPTION
        defaultHistoriquePersonneShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the historiquePersonneList where description contains UPDATED_DESCRIPTION
        defaultHistoriquePersonneShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        // Get all the historiquePersonneList where description does not contain DEFAULT_DESCRIPTION
        defaultHistoriquePersonneShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the historiquePersonneList where description does not contain UPDATED_DESCRIPTION
        defaultHistoriquePersonneShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHistoriquePersonnesByPersonneIsEqualToSomething() throws Exception {
        Personne personne;
        if (TestUtil.findAll(em, Personne.class).isEmpty()) {
            historiquePersonneRepository.saveAndFlush(historiquePersonne);
            personne = PersonneResourceIT.createEntity(em);
        } else {
            personne = TestUtil.findAll(em, Personne.class).get(0);
        }
        em.persist(personne);
        em.flush();
        historiquePersonne.setPersonne(personne);
        historiquePersonneRepository.saveAndFlush(historiquePersonne);
        Long personneId = personne.getId();

        // Get all the historiquePersonneList where personne equals to personneId
        defaultHistoriquePersonneShouldBeFound("personneId.equals=" + personneId);

        // Get all the historiquePersonneList where personne equals to (personneId + 1)
        defaultHistoriquePersonneShouldNotBeFound("personneId.equals=" + (personneId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriquePersonneShouldBeFound(String filter) throws Exception {
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiquePersonne.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(DEFAULT_DATE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].matriculePersonne").value(hasItem(DEFAULT_MATRICULE_PERSONNE)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriquePersonneShouldNotBeFound(String filter) throws Exception {
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriquePersonneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriquePersonne() throws Exception {
        // Get the historiquePersonne
        restHistoriquePersonneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriquePersonne() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();

        // Update the historiquePersonne
        HistoriquePersonne updatedHistoriquePersonne = historiquePersonneRepository.findById(historiquePersonne.getId()).get();
        // Disconnect from session so that the updates on updatedHistoriquePersonne are not directly saved in db
        em.detach(updatedHistoriquePersonne);
        updatedHistoriquePersonne
            .dateAction(UPDATED_DATE_ACTION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .action(UPDATED_ACTION)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION);
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(updatedHistoriquePersonne);

        restHistoriquePersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiquePersonneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
        HistoriquePersonne testHistoriquePersonne = historiquePersonneList.get(historiquePersonneList.size() - 1);
        assertThat(testHistoriquePersonne.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testHistoriquePersonne.getMatriculePersonne()).isEqualTo(UPDATED_MATRICULE_PERSONNE);
        assertThat(testHistoriquePersonne.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testHistoriquePersonne.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testHistoriquePersonne.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiquePersonneDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriquePersonneWithPatch() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();

        // Update the historiquePersonne using partial update
        HistoriquePersonne partialUpdatedHistoriquePersonne = new HistoriquePersonne();
        partialUpdatedHistoriquePersonne.setId(historiquePersonne.getId());

        partialUpdatedHistoriquePersonne
            .dateAction(UPDATED_DATE_ACTION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .action(UPDATED_ACTION)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION);

        restHistoriquePersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriquePersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriquePersonne))
            )
            .andExpect(status().isOk());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
        HistoriquePersonne testHistoriquePersonne = historiquePersonneList.get(historiquePersonneList.size() - 1);
        assertThat(testHistoriquePersonne.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testHistoriquePersonne.getMatriculePersonne()).isEqualTo(UPDATED_MATRICULE_PERSONNE);
        assertThat(testHistoriquePersonne.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testHistoriquePersonne.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testHistoriquePersonne.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateHistoriquePersonneWithPatch() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();

        // Update the historiquePersonne using partial update
        HistoriquePersonne partialUpdatedHistoriquePersonne = new HistoriquePersonne();
        partialUpdatedHistoriquePersonne.setId(historiquePersonne.getId());

        partialUpdatedHistoriquePersonne
            .dateAction(UPDATED_DATE_ACTION)
            .matriculePersonne(UPDATED_MATRICULE_PERSONNE)
            .action(UPDATED_ACTION)
            .result(UPDATED_RESULT)
            .description(UPDATED_DESCRIPTION);

        restHistoriquePersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriquePersonne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHistoriquePersonne))
            )
            .andExpect(status().isOk());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
        HistoriquePersonne testHistoriquePersonne = historiquePersonneList.get(historiquePersonneList.size() - 1);
        assertThat(testHistoriquePersonne.getDateAction()).isEqualTo(UPDATED_DATE_ACTION);
        assertThat(testHistoriquePersonne.getMatriculePersonne()).isEqualTo(UPDATED_MATRICULE_PERSONNE);
        assertThat(testHistoriquePersonne.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testHistoriquePersonne.getResult()).isEqualTo(UPDATED_RESULT);
        assertThat(testHistoriquePersonne.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiquePersonneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriquePersonne() throws Exception {
        int databaseSizeBeforeUpdate = historiquePersonneRepository.findAll().size();
        historiquePersonne.setId(count.incrementAndGet());

        // Create the HistoriquePersonne
        HistoriquePersonneDTO historiquePersonneDTO = historiquePersonneMapper.toDto(historiquePersonne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriquePersonneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(historiquePersonneDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriquePersonne in the database
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriquePersonne() throws Exception {
        // Initialize the database
        historiquePersonneRepository.saveAndFlush(historiquePersonne);

        int databaseSizeBeforeDelete = historiquePersonneRepository.findAll().size();

        // Delete the historiquePersonne
        restHistoriquePersonneMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiquePersonne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HistoriquePersonne> historiquePersonneList = historiquePersonneRepository.findAll();
        assertThat(historiquePersonneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
