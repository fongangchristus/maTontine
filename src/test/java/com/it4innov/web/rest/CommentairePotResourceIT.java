package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.CommentairePot;
import com.it4innov.domain.Pot;
import com.it4innov.repository.CommentairePotRepository;
import com.it4innov.service.criteria.CommentairePotCriteria;
import com.it4innov.service.dto.CommentairePotDTO;
import com.it4innov.service.mapper.CommentairePotMapper;
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
 * Integration tests for the {@link CommentairePotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommentairePotResourceIT {

    private static final String DEFAULT_MATRICULE_CONTRIBUTEUR = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_CONTRIBUTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFIANT_POT = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIANT_POT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_COMENTAIRE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_COMENTAIRE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/commentaire-pots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommentairePotRepository commentairePotRepository;

    @Autowired
    private CommentairePotMapper commentairePotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommentairePotMockMvc;

    private CommentairePot commentairePot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentairePot createEntity(EntityManager em) {
        CommentairePot commentairePot = new CommentairePot()
            .matriculeContributeur(DEFAULT_MATRICULE_CONTRIBUTEUR)
            .identifiantPot(DEFAULT_IDENTIFIANT_POT)
            .contenu(DEFAULT_CONTENU)
            .dateComentaire(DEFAULT_DATE_COMENTAIRE);
        return commentairePot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommentairePot createUpdatedEntity(EntityManager em) {
        CommentairePot commentairePot = new CommentairePot()
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .identifiantPot(UPDATED_IDENTIFIANT_POT)
            .contenu(UPDATED_CONTENU)
            .dateComentaire(UPDATED_DATE_COMENTAIRE);
        return commentairePot;
    }

    @BeforeEach
    public void initTest() {
        commentairePot = createEntity(em);
    }

    @Test
    @Transactional
    void createCommentairePot() throws Exception {
        int databaseSizeBeforeCreate = commentairePotRepository.findAll().size();
        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);
        restCommentairePotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeCreate + 1);
        CommentairePot testCommentairePot = commentairePotList.get(commentairePotList.size() - 1);
        assertThat(testCommentairePot.getMatriculeContributeur()).isEqualTo(DEFAULT_MATRICULE_CONTRIBUTEUR);
        assertThat(testCommentairePot.getIdentifiantPot()).isEqualTo(DEFAULT_IDENTIFIANT_POT);
        assertThat(testCommentairePot.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testCommentairePot.getDateComentaire()).isEqualTo(DEFAULT_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void createCommentairePotWithExistingId() throws Exception {
        // Create the CommentairePot with an existing ID
        commentairePot.setId(1L);
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        int databaseSizeBeforeCreate = commentairePotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentairePotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeContributeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentairePotRepository.findAll().size();
        // set the field null
        commentairePot.setMatriculeContributeur(null);

        // Create the CommentairePot, which fails.
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        restCommentairePotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentifiantPotIsRequired() throws Exception {
        int databaseSizeBeforeTest = commentairePotRepository.findAll().size();
        // set the field null
        commentairePot.setIdentifiantPot(null);

        // Create the CommentairePot, which fails.
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        restCommentairePotMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommentairePots() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentairePot.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeContributeur").value(hasItem(DEFAULT_MATRICULE_CONTRIBUTEUR)))
            .andExpect(jsonPath("$.[*].identifiantPot").value(hasItem(DEFAULT_IDENTIFIANT_POT)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].dateComentaire").value(hasItem(DEFAULT_DATE_COMENTAIRE.toString())));
    }

    @Test
    @Transactional
    void getCommentairePot() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get the commentairePot
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL_ID, commentairePot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commentairePot.getId().intValue()))
            .andExpect(jsonPath("$.matriculeContributeur").value(DEFAULT_MATRICULE_CONTRIBUTEUR))
            .andExpect(jsonPath("$.identifiantPot").value(DEFAULT_IDENTIFIANT_POT))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.dateComentaire").value(DEFAULT_DATE_COMENTAIRE.toString()));
    }

    @Test
    @Transactional
    void getCommentairePotsByIdFiltering() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        Long id = commentairePot.getId();

        defaultCommentairePotShouldBeFound("id.equals=" + id);
        defaultCommentairePotShouldNotBeFound("id.notEquals=" + id);

        defaultCommentairePotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCommentairePotShouldNotBeFound("id.greaterThan=" + id);

        defaultCommentairePotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCommentairePotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByMatriculeContributeurIsEqualToSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where matriculeContributeur equals to DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldBeFound("matriculeContributeur.equals=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the commentairePotList where matriculeContributeur equals to UPDATED_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldNotBeFound("matriculeContributeur.equals=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByMatriculeContributeurIsInShouldWork() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where matriculeContributeur in DEFAULT_MATRICULE_CONTRIBUTEUR or UPDATED_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldBeFound(
            "matriculeContributeur.in=" + DEFAULT_MATRICULE_CONTRIBUTEUR + "," + UPDATED_MATRICULE_CONTRIBUTEUR
        );

        // Get all the commentairePotList where matriculeContributeur equals to UPDATED_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldNotBeFound("matriculeContributeur.in=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByMatriculeContributeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where matriculeContributeur is not null
        defaultCommentairePotShouldBeFound("matriculeContributeur.specified=true");

        // Get all the commentairePotList where matriculeContributeur is null
        defaultCommentairePotShouldNotBeFound("matriculeContributeur.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairePotsByMatriculeContributeurContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where matriculeContributeur contains DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldBeFound("matriculeContributeur.contains=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the commentairePotList where matriculeContributeur contains UPDATED_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldNotBeFound("matriculeContributeur.contains=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByMatriculeContributeurNotContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where matriculeContributeur does not contain DEFAULT_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldNotBeFound("matriculeContributeur.doesNotContain=" + DEFAULT_MATRICULE_CONTRIBUTEUR);

        // Get all the commentairePotList where matriculeContributeur does not contain UPDATED_MATRICULE_CONTRIBUTEUR
        defaultCommentairePotShouldBeFound("matriculeContributeur.doesNotContain=" + UPDATED_MATRICULE_CONTRIBUTEUR);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByIdentifiantPotIsEqualToSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where identifiantPot equals to DEFAULT_IDENTIFIANT_POT
        defaultCommentairePotShouldBeFound("identifiantPot.equals=" + DEFAULT_IDENTIFIANT_POT);

        // Get all the commentairePotList where identifiantPot equals to UPDATED_IDENTIFIANT_POT
        defaultCommentairePotShouldNotBeFound("identifiantPot.equals=" + UPDATED_IDENTIFIANT_POT);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByIdentifiantPotIsInShouldWork() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where identifiantPot in DEFAULT_IDENTIFIANT_POT or UPDATED_IDENTIFIANT_POT
        defaultCommentairePotShouldBeFound("identifiantPot.in=" + DEFAULT_IDENTIFIANT_POT + "," + UPDATED_IDENTIFIANT_POT);

        // Get all the commentairePotList where identifiantPot equals to UPDATED_IDENTIFIANT_POT
        defaultCommentairePotShouldNotBeFound("identifiantPot.in=" + UPDATED_IDENTIFIANT_POT);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByIdentifiantPotIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where identifiantPot is not null
        defaultCommentairePotShouldBeFound("identifiantPot.specified=true");

        // Get all the commentairePotList where identifiantPot is null
        defaultCommentairePotShouldNotBeFound("identifiantPot.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairePotsByIdentifiantPotContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where identifiantPot contains DEFAULT_IDENTIFIANT_POT
        defaultCommentairePotShouldBeFound("identifiantPot.contains=" + DEFAULT_IDENTIFIANT_POT);

        // Get all the commentairePotList where identifiantPot contains UPDATED_IDENTIFIANT_POT
        defaultCommentairePotShouldNotBeFound("identifiantPot.contains=" + UPDATED_IDENTIFIANT_POT);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByIdentifiantPotNotContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where identifiantPot does not contain DEFAULT_IDENTIFIANT_POT
        defaultCommentairePotShouldNotBeFound("identifiantPot.doesNotContain=" + DEFAULT_IDENTIFIANT_POT);

        // Get all the commentairePotList where identifiantPot does not contain UPDATED_IDENTIFIANT_POT
        defaultCommentairePotShouldBeFound("identifiantPot.doesNotContain=" + UPDATED_IDENTIFIANT_POT);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByContenuIsEqualToSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where contenu equals to DEFAULT_CONTENU
        defaultCommentairePotShouldBeFound("contenu.equals=" + DEFAULT_CONTENU);

        // Get all the commentairePotList where contenu equals to UPDATED_CONTENU
        defaultCommentairePotShouldNotBeFound("contenu.equals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByContenuIsInShouldWork() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where contenu in DEFAULT_CONTENU or UPDATED_CONTENU
        defaultCommentairePotShouldBeFound("contenu.in=" + DEFAULT_CONTENU + "," + UPDATED_CONTENU);

        // Get all the commentairePotList where contenu equals to UPDATED_CONTENU
        defaultCommentairePotShouldNotBeFound("contenu.in=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByContenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where contenu is not null
        defaultCommentairePotShouldBeFound("contenu.specified=true");

        // Get all the commentairePotList where contenu is null
        defaultCommentairePotShouldNotBeFound("contenu.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairePotsByContenuContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where contenu contains DEFAULT_CONTENU
        defaultCommentairePotShouldBeFound("contenu.contains=" + DEFAULT_CONTENU);

        // Get all the commentairePotList where contenu contains UPDATED_CONTENU
        defaultCommentairePotShouldNotBeFound("contenu.contains=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByContenuNotContainsSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where contenu does not contain DEFAULT_CONTENU
        defaultCommentairePotShouldNotBeFound("contenu.doesNotContain=" + DEFAULT_CONTENU);

        // Get all the commentairePotList where contenu does not contain UPDATED_CONTENU
        defaultCommentairePotShouldBeFound("contenu.doesNotContain=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByDateComentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where dateComentaire equals to DEFAULT_DATE_COMENTAIRE
        defaultCommentairePotShouldBeFound("dateComentaire.equals=" + DEFAULT_DATE_COMENTAIRE);

        // Get all the commentairePotList where dateComentaire equals to UPDATED_DATE_COMENTAIRE
        defaultCommentairePotShouldNotBeFound("dateComentaire.equals=" + UPDATED_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByDateComentaireIsInShouldWork() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where dateComentaire in DEFAULT_DATE_COMENTAIRE or UPDATED_DATE_COMENTAIRE
        defaultCommentairePotShouldBeFound("dateComentaire.in=" + DEFAULT_DATE_COMENTAIRE + "," + UPDATED_DATE_COMENTAIRE);

        // Get all the commentairePotList where dateComentaire equals to UPDATED_DATE_COMENTAIRE
        defaultCommentairePotShouldNotBeFound("dateComentaire.in=" + UPDATED_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void getAllCommentairePotsByDateComentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        // Get all the commentairePotList where dateComentaire is not null
        defaultCommentairePotShouldBeFound("dateComentaire.specified=true");

        // Get all the commentairePotList where dateComentaire is null
        defaultCommentairePotShouldNotBeFound("dateComentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllCommentairePotsByPotIsEqualToSomething() throws Exception {
        Pot pot;
        if (TestUtil.findAll(em, Pot.class).isEmpty()) {
            commentairePotRepository.saveAndFlush(commentairePot);
            pot = PotResourceIT.createEntity(em);
        } else {
            pot = TestUtil.findAll(em, Pot.class).get(0);
        }
        em.persist(pot);
        em.flush();
        commentairePot.setPot(pot);
        commentairePotRepository.saveAndFlush(commentairePot);
        Long potId = pot.getId();

        // Get all the commentairePotList where pot equals to potId
        defaultCommentairePotShouldBeFound("potId.equals=" + potId);

        // Get all the commentairePotList where pot equals to (potId + 1)
        defaultCommentairePotShouldNotBeFound("potId.equals=" + (potId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCommentairePotShouldBeFound(String filter) throws Exception {
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentairePot.getId().intValue())))
            .andExpect(jsonPath("$.[*].matriculeContributeur").value(hasItem(DEFAULT_MATRICULE_CONTRIBUTEUR)))
            .andExpect(jsonPath("$.[*].identifiantPot").value(hasItem(DEFAULT_IDENTIFIANT_POT)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].dateComentaire").value(hasItem(DEFAULT_DATE_COMENTAIRE.toString())));

        // Check, that the count call also returns 1
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCommentairePotShouldNotBeFound(String filter) throws Exception {
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCommentairePotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCommentairePot() throws Exception {
        // Get the commentairePot
        restCommentairePotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommentairePot() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();

        // Update the commentairePot
        CommentairePot updatedCommentairePot = commentairePotRepository.findById(commentairePot.getId()).get();
        // Disconnect from session so that the updates on updatedCommentairePot are not directly saved in db
        em.detach(updatedCommentairePot);
        updatedCommentairePot
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .identifiantPot(UPDATED_IDENTIFIANT_POT)
            .contenu(UPDATED_CONTENU)
            .dateComentaire(UPDATED_DATE_COMENTAIRE);
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(updatedCommentairePot);

        restCommentairePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentairePotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isOk());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
        CommentairePot testCommentairePot = commentairePotList.get(commentairePotList.size() - 1);
        assertThat(testCommentairePot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testCommentairePot.getIdentifiantPot()).isEqualTo(UPDATED_IDENTIFIANT_POT);
        assertThat(testCommentairePot.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testCommentairePot.getDateComentaire()).isEqualTo(UPDATED_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commentairePotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommentairePotWithPatch() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();

        // Update the commentairePot using partial update
        CommentairePot partialUpdatedCommentairePot = new CommentairePot();
        partialUpdatedCommentairePot.setId(commentairePot.getId());

        partialUpdatedCommentairePot
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .identifiantPot(UPDATED_IDENTIFIANT_POT)
            .dateComentaire(UPDATED_DATE_COMENTAIRE);

        restCommentairePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentairePot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentairePot))
            )
            .andExpect(status().isOk());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
        CommentairePot testCommentairePot = commentairePotList.get(commentairePotList.size() - 1);
        assertThat(testCommentairePot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testCommentairePot.getIdentifiantPot()).isEqualTo(UPDATED_IDENTIFIANT_POT);
        assertThat(testCommentairePot.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testCommentairePot.getDateComentaire()).isEqualTo(UPDATED_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateCommentairePotWithPatch() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();

        // Update the commentairePot using partial update
        CommentairePot partialUpdatedCommentairePot = new CommentairePot();
        partialUpdatedCommentairePot.setId(commentairePot.getId());

        partialUpdatedCommentairePot
            .matriculeContributeur(UPDATED_MATRICULE_CONTRIBUTEUR)
            .identifiantPot(UPDATED_IDENTIFIANT_POT)
            .contenu(UPDATED_CONTENU)
            .dateComentaire(UPDATED_DATE_COMENTAIRE);

        restCommentairePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommentairePot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommentairePot))
            )
            .andExpect(status().isOk());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
        CommentairePot testCommentairePot = commentairePotList.get(commentairePotList.size() - 1);
        assertThat(testCommentairePot.getMatriculeContributeur()).isEqualTo(UPDATED_MATRICULE_CONTRIBUTEUR);
        assertThat(testCommentairePot.getIdentifiantPot()).isEqualTo(UPDATED_IDENTIFIANT_POT);
        assertThat(testCommentairePot.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testCommentairePot.getDateComentaire()).isEqualTo(UPDATED_DATE_COMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commentairePotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommentairePot() throws Exception {
        int databaseSizeBeforeUpdate = commentairePotRepository.findAll().size();
        commentairePot.setId(count.incrementAndGet());

        // Create the CommentairePot
        CommentairePotDTO commentairePotDTO = commentairePotMapper.toDto(commentairePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommentairePotMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commentairePotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommentairePot in the database
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommentairePot() throws Exception {
        // Initialize the database
        commentairePotRepository.saveAndFlush(commentairePot);

        int databaseSizeBeforeDelete = commentairePotRepository.findAll().size();

        // Delete the commentairePot
        restCommentairePotMockMvc
            .perform(delete(ENTITY_API_URL_ID, commentairePot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentairePot> commentairePotList = commentairePotRepository.findAll();
        assertThat(commentairePotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
