package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Association;
import com.it4innov.domain.Exercise;
import com.it4innov.domain.enumeration.StatutExercice;
import com.it4innov.repository.ExerciseRepository;
import com.it4innov.service.criteria.ExerciseCriteria;
import com.it4innov.service.dto.ExerciseDTO;
import com.it4innov.service.mapper.ExerciseMapper;
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
 * Integration tests for the {@link ExerciseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExerciseResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final StatutExercice DEFAULT_STATUT = StatutExercice.OUVERT;
    private static final StatutExercice UPDATED_STATUT = StatutExercice.FERME;

    private static final String ENTITY_API_URL = "/api/exercises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExerciseMockMvc;

    private Exercise exercise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .libele(DEFAULT_LIBELE)
            .observation(DEFAULT_OBSERVATION)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .statut(DEFAULT_STATUT);
        return exercise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createUpdatedEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .libele(UPDATED_LIBELE)
            .observation(UPDATED_OBSERVATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statut(UPDATED_STATUT);
        return exercise;
    }

    @BeforeEach
    public void initTest() {
        exercise = createEntity(em);
    }

    @Test
    @Transactional
    void createExercise() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();
        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);
        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testExercise.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
        assertThat(testExercise.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testExercise.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testExercise.getStatut()).isEqualTo(DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void createExerciseWithExistingId() throws Exception {
        // Create the Exercise with an existing ID
        exercise.setId(1L);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExercises() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get the exercise
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL_ID, exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exercise.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getExercisesByIdFiltering() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        Long id = exercise.getId();

        defaultExerciseShouldBeFound("id.equals=" + id);
        defaultExerciseShouldNotBeFound("id.notEquals=" + id);

        defaultExerciseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultExerciseShouldNotBeFound("id.greaterThan=" + id);

        defaultExerciseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultExerciseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExercisesByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where libele equals to DEFAULT_LIBELE
        defaultExerciseShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the exerciseList where libele equals to UPDATED_LIBELE
        defaultExerciseShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllExercisesByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultExerciseShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the exerciseList where libele equals to UPDATED_LIBELE
        defaultExerciseShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllExercisesByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where libele is not null
        defaultExerciseShouldBeFound("libele.specified=true");

        // Get all the exerciseList where libele is null
        defaultExerciseShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByLibeleContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where libele contains DEFAULT_LIBELE
        defaultExerciseShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the exerciseList where libele contains UPDATED_LIBELE
        defaultExerciseShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllExercisesByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where libele does not contain DEFAULT_LIBELE
        defaultExerciseShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the exerciseList where libele does not contain UPDATED_LIBELE
        defaultExerciseShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllExercisesByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where observation equals to DEFAULT_OBSERVATION
        defaultExerciseShouldBeFound("observation.equals=" + DEFAULT_OBSERVATION);

        // Get all the exerciseList where observation equals to UPDATED_OBSERVATION
        defaultExerciseShouldNotBeFound("observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllExercisesByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where observation in DEFAULT_OBSERVATION or UPDATED_OBSERVATION
        defaultExerciseShouldBeFound("observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION);

        // Get all the exerciseList where observation equals to UPDATED_OBSERVATION
        defaultExerciseShouldNotBeFound("observation.in=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllExercisesByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where observation is not null
        defaultExerciseShouldBeFound("observation.specified=true");

        // Get all the exerciseList where observation is null
        defaultExerciseShouldNotBeFound("observation.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByObservationContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where observation contains DEFAULT_OBSERVATION
        defaultExerciseShouldBeFound("observation.contains=" + DEFAULT_OBSERVATION);

        // Get all the exerciseList where observation contains UPDATED_OBSERVATION
        defaultExerciseShouldNotBeFound("observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllExercisesByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where observation does not contain DEFAULT_OBSERVATION
        defaultExerciseShouldNotBeFound("observation.doesNotContain=" + DEFAULT_OBSERVATION);

        // Get all the exerciseList where observation does not contain UPDATED_OBSERVATION
        defaultExerciseShouldBeFound("observation.doesNotContain=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the exerciseList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the exerciseList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut is not null
        defaultExerciseShouldBeFound("dateDebut.specified=true");

        // Get all the exerciseList where dateDebut is null
        defaultExerciseShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the exerciseList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the exerciseList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the exerciseList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultExerciseShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the exerciseList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultExerciseShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin equals to DEFAULT_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the exerciseList where dateFin equals to UPDATED_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the exerciseList where dateFin equals to UPDATED_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin is not null
        defaultExerciseShouldBeFound("dateFin.specified=true");

        // Get all the exerciseList where dateFin is null
        defaultExerciseShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the exerciseList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the exerciseList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin is less than DEFAULT_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the exerciseList where dateFin is less than UPDATED_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where dateFin is greater than DEFAULT_DATE_FIN
        defaultExerciseShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the exerciseList where dateFin is greater than SMALLER_DATE_FIN
        defaultExerciseShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllExercisesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where statut equals to DEFAULT_STATUT
        defaultExerciseShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the exerciseList where statut equals to UPDATED_STATUT
        defaultExerciseShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllExercisesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultExerciseShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the exerciseList where statut equals to UPDATED_STATUT
        defaultExerciseShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllExercisesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where statut is not null
        defaultExerciseShouldBeFound("statut.specified=true");

        // Get all the exerciseList where statut is null
        defaultExerciseShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    void getAllExercisesByAssociationIsEqualToSomething() throws Exception {
        Association association;
        if (TestUtil.findAll(em, Association.class).isEmpty()) {
            exerciseRepository.saveAndFlush(exercise);
            association = AssociationResourceIT.createEntity(em);
        } else {
            association = TestUtil.findAll(em, Association.class).get(0);
        }
        em.persist(association);
        em.flush();
        exercise.setAssociation(association);
        exerciseRepository.saveAndFlush(exercise);
        Long associationId = association.getId();

        // Get all the exerciseList where association equals to associationId
        defaultExerciseShouldBeFound("associationId.equals=" + associationId);

        // Get all the exerciseList where association equals to (associationId + 1)
        defaultExerciseShouldNotBeFound("associationId.equals=" + (associationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseShouldBeFound(String filter) throws Exception {
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseShouldNotBeFound(String filter) throws Exception {
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExercise() throws Exception {
        // Get the exercise
        restExerciseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise
        Exercise updatedExercise = exerciseRepository.findById(exercise.getId()).get();
        // Disconnect from session so that the updates on updatedExercise are not directly saved in db
        em.detach(updatedExercise);
        updatedExercise
            .libele(UPDATED_LIBELE)
            .observation(UPDATED_OBSERVATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statut(UPDATED_STATUT);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(updatedExercise);

        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testExercise.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testExercise.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testExercise.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testExercise.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void putNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise.observation(UPDATED_OBSERVATION).statut(UPDATED_STATUT);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testExercise.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testExercise.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testExercise.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testExercise.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void fullUpdateExerciseWithPatch() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise using partial update
        Exercise partialUpdatedExercise = new Exercise();
        partialUpdatedExercise.setId(exercise.getId());

        partialUpdatedExercise
            .libele(UPDATED_LIBELE)
            .observation(UPDATED_OBSERVATION)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .statut(UPDATED_STATUT);

        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExercise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExercise))
            )
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testExercise.getObservation()).isEqualTo(UPDATED_OBSERVATION);
        assertThat(testExercise.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testExercise.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testExercise.getStatut()).isEqualTo(UPDATED_STATUT);
    }

    @Test
    @Transactional
    void patchNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exerciseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();
        exercise.setId(count.incrementAndGet());

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExerciseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(exerciseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeDelete = exerciseRepository.findAll().size();

        // Delete the exercise
        restExerciseMockMvc
            .perform(delete(ENTITY_API_URL_ID, exercise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
