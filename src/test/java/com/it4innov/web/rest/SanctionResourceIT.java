package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.PaiementSanction;
import com.it4innov.domain.Sanction;
import com.it4innov.domain.SanctionConfiguration;
import com.it4innov.repository.SanctionRepository;
import com.it4innov.service.criteria.SanctionCriteria;
import com.it4innov.service.dto.SanctionDTO;
import com.it4innov.service.mapper.SanctionMapper;
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
 * Integration tests for the {@link SanctionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanctionResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE_ADHERENT = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE_ADHERENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_SANCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SANCTION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_SANCTION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_MOTIF_SANCTION = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF_SANCTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_ACTIVITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sanctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SanctionRepository sanctionRepository;

    @Autowired
    private SanctionMapper sanctionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSanctionMockMvc;

    private Sanction sanction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanction createEntity(EntityManager em) {
        Sanction sanction = new Sanction()
            .libelle(DEFAULT_LIBELLE)
            .matriculeAdherent(DEFAULT_MATRICULE_ADHERENT)
            .dateSanction(DEFAULT_DATE_SANCTION)
            .motifSanction(DEFAULT_MOTIF_SANCTION)
            .description(DEFAULT_DESCRIPTION)
            .codeActivite(DEFAULT_CODE_ACTIVITE);
        return sanction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sanction createUpdatedEntity(EntityManager em) {
        Sanction sanction = new Sanction()
            .libelle(UPDATED_LIBELLE)
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .dateSanction(UPDATED_DATE_SANCTION)
            .motifSanction(UPDATED_MOTIF_SANCTION)
            .description(UPDATED_DESCRIPTION)
            .codeActivite(UPDATED_CODE_ACTIVITE);
        return sanction;
    }

    @BeforeEach
    public void initTest() {
        sanction = createEntity(em);
    }

    @Test
    @Transactional
    void createSanction() throws Exception {
        int databaseSizeBeforeCreate = sanctionRepository.findAll().size();
        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);
        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionDTO)))
            .andExpect(status().isCreated());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeCreate + 1);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testSanction.getMatriculeAdherent()).isEqualTo(DEFAULT_MATRICULE_ADHERENT);
        assertThat(testSanction.getDateSanction()).isEqualTo(DEFAULT_DATE_SANCTION);
        assertThat(testSanction.getMotifSanction()).isEqualTo(DEFAULT_MOTIF_SANCTION);
        assertThat(testSanction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSanction.getCodeActivite()).isEqualTo(DEFAULT_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void createSanctionWithExistingId() throws Exception {
        // Create the Sanction with an existing ID
        sanction.setId(1L);
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        int databaseSizeBeforeCreate = sanctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMatriculeAdherentIsRequired() throws Exception {
        int databaseSizeBeforeTest = sanctionRepository.findAll().size();
        // set the field null
        sanction.setMatriculeAdherent(null);

        // Create the Sanction, which fails.
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        restSanctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionDTO)))
            .andExpect(status().isBadRequest());

        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSanctions() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].dateSanction").value(hasItem(DEFAULT_DATE_SANCTION.toString())))
            .andExpect(jsonPath("$.[*].motifSanction").value(hasItem(DEFAULT_MOTIF_SANCTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].codeActivite").value(hasItem(DEFAULT_CODE_ACTIVITE)));
    }

    @Test
    @Transactional
    void getSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get the sanction
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL_ID, sanction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sanction.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.matriculeAdherent").value(DEFAULT_MATRICULE_ADHERENT))
            .andExpect(jsonPath("$.dateSanction").value(DEFAULT_DATE_SANCTION.toString()))
            .andExpect(jsonPath("$.motifSanction").value(DEFAULT_MOTIF_SANCTION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.codeActivite").value(DEFAULT_CODE_ACTIVITE));
    }

    @Test
    @Transactional
    void getSanctionsByIdFiltering() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        Long id = sanction.getId();

        defaultSanctionShouldBeFound("id.equals=" + id);
        defaultSanctionShouldNotBeFound("id.notEquals=" + id);

        defaultSanctionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSanctionShouldNotBeFound("id.greaterThan=" + id);

        defaultSanctionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSanctionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle equals to DEFAULT_LIBELLE
        defaultSanctionShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle equals to UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultSanctionShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the sanctionList where libelle equals to UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle is not null
        defaultSanctionShouldBeFound("libelle.specified=true");

        // Get all the sanctionList where libelle is null
        defaultSanctionShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle contains DEFAULT_LIBELLE
        defaultSanctionShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle contains UPDATED_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where libelle does not contain DEFAULT_LIBELLE
        defaultSanctionShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the sanctionList where libelle does not contain UPDATED_LIBELLE
        defaultSanctionShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllSanctionsByMatriculeAdherentIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where matriculeAdherent equals to DEFAULT_MATRICULE_ADHERENT
        defaultSanctionShouldBeFound("matriculeAdherent.equals=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the sanctionList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultSanctionShouldNotBeFound("matriculeAdherent.equals=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllSanctionsByMatriculeAdherentIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where matriculeAdherent in DEFAULT_MATRICULE_ADHERENT or UPDATED_MATRICULE_ADHERENT
        defaultSanctionShouldBeFound("matriculeAdherent.in=" + DEFAULT_MATRICULE_ADHERENT + "," + UPDATED_MATRICULE_ADHERENT);

        // Get all the sanctionList where matriculeAdherent equals to UPDATED_MATRICULE_ADHERENT
        defaultSanctionShouldNotBeFound("matriculeAdherent.in=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllSanctionsByMatriculeAdherentIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where matriculeAdherent is not null
        defaultSanctionShouldBeFound("matriculeAdherent.specified=true");

        // Get all the sanctionList where matriculeAdherent is null
        defaultSanctionShouldNotBeFound("matriculeAdherent.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByMatriculeAdherentContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where matriculeAdherent contains DEFAULT_MATRICULE_ADHERENT
        defaultSanctionShouldBeFound("matriculeAdherent.contains=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the sanctionList where matriculeAdherent contains UPDATED_MATRICULE_ADHERENT
        defaultSanctionShouldNotBeFound("matriculeAdherent.contains=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllSanctionsByMatriculeAdherentNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where matriculeAdherent does not contain DEFAULT_MATRICULE_ADHERENT
        defaultSanctionShouldNotBeFound("matriculeAdherent.doesNotContain=" + DEFAULT_MATRICULE_ADHERENT);

        // Get all the sanctionList where matriculeAdherent does not contain UPDATED_MATRICULE_ADHERENT
        defaultSanctionShouldBeFound("matriculeAdherent.doesNotContain=" + UPDATED_MATRICULE_ADHERENT);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction equals to DEFAULT_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.equals=" + DEFAULT_DATE_SANCTION);

        // Get all the sanctionList where dateSanction equals to UPDATED_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.equals=" + UPDATED_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction in DEFAULT_DATE_SANCTION or UPDATED_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.in=" + DEFAULT_DATE_SANCTION + "," + UPDATED_DATE_SANCTION);

        // Get all the sanctionList where dateSanction equals to UPDATED_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.in=" + UPDATED_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction is not null
        defaultSanctionShouldBeFound("dateSanction.specified=true");

        // Get all the sanctionList where dateSanction is null
        defaultSanctionShouldNotBeFound("dateSanction.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction is greater than or equal to DEFAULT_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.greaterThanOrEqual=" + DEFAULT_DATE_SANCTION);

        // Get all the sanctionList where dateSanction is greater than or equal to UPDATED_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.greaterThanOrEqual=" + UPDATED_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction is less than or equal to DEFAULT_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.lessThanOrEqual=" + DEFAULT_DATE_SANCTION);

        // Get all the sanctionList where dateSanction is less than or equal to SMALLER_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.lessThanOrEqual=" + SMALLER_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsLessThanSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction is less than DEFAULT_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.lessThan=" + DEFAULT_DATE_SANCTION);

        // Get all the sanctionList where dateSanction is less than UPDATED_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.lessThan=" + UPDATED_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDateSanctionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where dateSanction is greater than DEFAULT_DATE_SANCTION
        defaultSanctionShouldNotBeFound("dateSanction.greaterThan=" + DEFAULT_DATE_SANCTION);

        // Get all the sanctionList where dateSanction is greater than SMALLER_DATE_SANCTION
        defaultSanctionShouldBeFound("dateSanction.greaterThan=" + SMALLER_DATE_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByMotifSanctionIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where motifSanction equals to DEFAULT_MOTIF_SANCTION
        defaultSanctionShouldBeFound("motifSanction.equals=" + DEFAULT_MOTIF_SANCTION);

        // Get all the sanctionList where motifSanction equals to UPDATED_MOTIF_SANCTION
        defaultSanctionShouldNotBeFound("motifSanction.equals=" + UPDATED_MOTIF_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByMotifSanctionIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where motifSanction in DEFAULT_MOTIF_SANCTION or UPDATED_MOTIF_SANCTION
        defaultSanctionShouldBeFound("motifSanction.in=" + DEFAULT_MOTIF_SANCTION + "," + UPDATED_MOTIF_SANCTION);

        // Get all the sanctionList where motifSanction equals to UPDATED_MOTIF_SANCTION
        defaultSanctionShouldNotBeFound("motifSanction.in=" + UPDATED_MOTIF_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByMotifSanctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where motifSanction is not null
        defaultSanctionShouldBeFound("motifSanction.specified=true");

        // Get all the sanctionList where motifSanction is null
        defaultSanctionShouldNotBeFound("motifSanction.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByMotifSanctionContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where motifSanction contains DEFAULT_MOTIF_SANCTION
        defaultSanctionShouldBeFound("motifSanction.contains=" + DEFAULT_MOTIF_SANCTION);

        // Get all the sanctionList where motifSanction contains UPDATED_MOTIF_SANCTION
        defaultSanctionShouldNotBeFound("motifSanction.contains=" + UPDATED_MOTIF_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByMotifSanctionNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where motifSanction does not contain DEFAULT_MOTIF_SANCTION
        defaultSanctionShouldNotBeFound("motifSanction.doesNotContain=" + DEFAULT_MOTIF_SANCTION);

        // Get all the sanctionList where motifSanction does not contain UPDATED_MOTIF_SANCTION
        defaultSanctionShouldBeFound("motifSanction.doesNotContain=" + UPDATED_MOTIF_SANCTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where description equals to DEFAULT_DESCRIPTION
        defaultSanctionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the sanctionList where description equals to UPDATED_DESCRIPTION
        defaultSanctionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultSanctionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the sanctionList where description equals to UPDATED_DESCRIPTION
        defaultSanctionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where description is not null
        defaultSanctionShouldBeFound("description.specified=true");

        // Get all the sanctionList where description is null
        defaultSanctionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where description contains DEFAULT_DESCRIPTION
        defaultSanctionShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the sanctionList where description contains UPDATED_DESCRIPTION
        defaultSanctionShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where description does not contain DEFAULT_DESCRIPTION
        defaultSanctionShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the sanctionList where description does not contain UPDATED_DESCRIPTION
        defaultSanctionShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where codeActivite equals to DEFAULT_CODE_ACTIVITE
        defaultSanctionShouldBeFound("codeActivite.equals=" + DEFAULT_CODE_ACTIVITE);

        // Get all the sanctionList where codeActivite equals to UPDATED_CODE_ACTIVITE
        defaultSanctionShouldNotBeFound("codeActivite.equals=" + UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeActiviteIsInShouldWork() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where codeActivite in DEFAULT_CODE_ACTIVITE or UPDATED_CODE_ACTIVITE
        defaultSanctionShouldBeFound("codeActivite.in=" + DEFAULT_CODE_ACTIVITE + "," + UPDATED_CODE_ACTIVITE);

        // Get all the sanctionList where codeActivite equals to UPDATED_CODE_ACTIVITE
        defaultSanctionShouldNotBeFound("codeActivite.in=" + UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeActiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where codeActivite is not null
        defaultSanctionShouldBeFound("codeActivite.specified=true");

        // Get all the sanctionList where codeActivite is null
        defaultSanctionShouldNotBeFound("codeActivite.specified=false");
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeActiviteContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where codeActivite contains DEFAULT_CODE_ACTIVITE
        defaultSanctionShouldBeFound("codeActivite.contains=" + DEFAULT_CODE_ACTIVITE);

        // Get all the sanctionList where codeActivite contains UPDATED_CODE_ACTIVITE
        defaultSanctionShouldNotBeFound("codeActivite.contains=" + UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllSanctionsByCodeActiviteNotContainsSomething() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        // Get all the sanctionList where codeActivite does not contain DEFAULT_CODE_ACTIVITE
        defaultSanctionShouldNotBeFound("codeActivite.doesNotContain=" + DEFAULT_CODE_ACTIVITE);

        // Get all the sanctionList where codeActivite does not contain UPDATED_CODE_ACTIVITE
        defaultSanctionShouldBeFound("codeActivite.doesNotContain=" + UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllSanctionsByPaiementSanctionIsEqualToSomething() throws Exception {
        PaiementSanction paiementSanction;
        if (TestUtil.findAll(em, PaiementSanction.class).isEmpty()) {
            sanctionRepository.saveAndFlush(sanction);
            paiementSanction = PaiementSanctionResourceIT.createEntity(em);
        } else {
            paiementSanction = TestUtil.findAll(em, PaiementSanction.class).get(0);
        }
        em.persist(paiementSanction);
        em.flush();
        sanction.addPaiementSanction(paiementSanction);
        sanctionRepository.saveAndFlush(sanction);
        Long paiementSanctionId = paiementSanction.getId();

        // Get all the sanctionList where paiementSanction equals to paiementSanctionId
        defaultSanctionShouldBeFound("paiementSanctionId.equals=" + paiementSanctionId);

        // Get all the sanctionList where paiementSanction equals to (paiementSanctionId + 1)
        defaultSanctionShouldNotBeFound("paiementSanctionId.equals=" + (paiementSanctionId + 1));
    }

    @Test
    @Transactional
    void getAllSanctionsBySanctionConfigIsEqualToSomething() throws Exception {
        SanctionConfiguration sanctionConfig;
        if (TestUtil.findAll(em, SanctionConfiguration.class).isEmpty()) {
            sanctionRepository.saveAndFlush(sanction);
            sanctionConfig = SanctionConfigurationResourceIT.createEntity(em);
        } else {
            sanctionConfig = TestUtil.findAll(em, SanctionConfiguration.class).get(0);
        }
        em.persist(sanctionConfig);
        em.flush();
        sanction.setSanctionConfig(sanctionConfig);
        sanctionRepository.saveAndFlush(sanction);
        Long sanctionConfigId = sanctionConfig.getId();

        // Get all the sanctionList where sanctionConfig equals to sanctionConfigId
        defaultSanctionShouldBeFound("sanctionConfigId.equals=" + sanctionConfigId);

        // Get all the sanctionList where sanctionConfig equals to (sanctionConfigId + 1)
        defaultSanctionShouldNotBeFound("sanctionConfigId.equals=" + (sanctionConfigId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSanctionShouldBeFound(String filter) throws Exception {
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sanction.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].matriculeAdherent").value(hasItem(DEFAULT_MATRICULE_ADHERENT)))
            .andExpect(jsonPath("$.[*].dateSanction").value(hasItem(DEFAULT_DATE_SANCTION.toString())))
            .andExpect(jsonPath("$.[*].motifSanction").value(hasItem(DEFAULT_MOTIF_SANCTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].codeActivite").value(hasItem(DEFAULT_CODE_ACTIVITE)));

        // Check, that the count call also returns 1
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSanctionShouldNotBeFound(String filter) throws Exception {
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSanctionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSanction() throws Exception {
        // Get the sanction
        restSanctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction
        Sanction updatedSanction = sanctionRepository.findById(sanction.getId()).get();
        // Disconnect from session so that the updates on updatedSanction are not directly saved in db
        em.detach(updatedSanction);
        updatedSanction
            .libelle(UPDATED_LIBELLE)
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .dateSanction(UPDATED_DATE_SANCTION)
            .motifSanction(UPDATED_MOTIF_SANCTION)
            .description(UPDATED_DESCRIPTION)
            .codeActivite(UPDATED_CODE_ACTIVITE);
        SanctionDTO sanctionDTO = sanctionMapper.toDto(updatedSanction);

        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSanction.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testSanction.getDateSanction()).isEqualTo(UPDATED_DATE_SANCTION);
        assertThat(testSanction.getMotifSanction()).isEqualTo(UPDATED_MOTIF_SANCTION);
        assertThat(testSanction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSanction.getCodeActivite()).isEqualTo(UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void putNonExistingSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sanctionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sanctionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSanctionWithPatch() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction using partial update
        Sanction partialUpdatedSanction = new Sanction();
        partialUpdatedSanction.setId(sanction.getId());

        partialUpdatedSanction.libelle(UPDATED_LIBELLE).dateSanction(UPDATED_DATE_SANCTION).description(UPDATED_DESCRIPTION);

        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanction))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSanction.getMatriculeAdherent()).isEqualTo(DEFAULT_MATRICULE_ADHERENT);
        assertThat(testSanction.getDateSanction()).isEqualTo(UPDATED_DATE_SANCTION);
        assertThat(testSanction.getMotifSanction()).isEqualTo(DEFAULT_MOTIF_SANCTION);
        assertThat(testSanction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSanction.getCodeActivite()).isEqualTo(DEFAULT_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void fullUpdateSanctionWithPatch() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();

        // Update the sanction using partial update
        Sanction partialUpdatedSanction = new Sanction();
        partialUpdatedSanction.setId(sanction.getId());

        partialUpdatedSanction
            .libelle(UPDATED_LIBELLE)
            .matriculeAdherent(UPDATED_MATRICULE_ADHERENT)
            .dateSanction(UPDATED_DATE_SANCTION)
            .motifSanction(UPDATED_MOTIF_SANCTION)
            .description(UPDATED_DESCRIPTION)
            .codeActivite(UPDATED_CODE_ACTIVITE);

        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSanction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSanction))
            )
            .andExpect(status().isOk());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
        Sanction testSanction = sanctionList.get(sanctionList.size() - 1);
        assertThat(testSanction.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testSanction.getMatriculeAdherent()).isEqualTo(UPDATED_MATRICULE_ADHERENT);
        assertThat(testSanction.getDateSanction()).isEqualTo(UPDATED_DATE_SANCTION);
        assertThat(testSanction.getMotifSanction()).isEqualTo(UPDATED_MOTIF_SANCTION);
        assertThat(testSanction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSanction.getCodeActivite()).isEqualTo(UPDATED_CODE_ACTIVITE);
    }

    @Test
    @Transactional
    void patchNonExistingSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sanctionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSanction() throws Exception {
        int databaseSizeBeforeUpdate = sanctionRepository.findAll().size();
        sanction.setId(count.incrementAndGet());

        // Create the Sanction
        SanctionDTO sanctionDTO = sanctionMapper.toDto(sanction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanctionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sanctionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sanction in the database
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSanction() throws Exception {
        // Initialize the database
        sanctionRepository.saveAndFlush(sanction);

        int databaseSizeBeforeDelete = sanctionRepository.findAll().size();

        // Delete the sanction
        restSanctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, sanction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sanction> sanctionList = sanctionRepository.findAll();
        assertThat(sanctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
