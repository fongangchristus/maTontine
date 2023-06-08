package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Association;
import com.it4innov.domain.DocumentAssociation;
import com.it4innov.repository.DocumentAssociationRepository;
import com.it4innov.service.criteria.DocumentAssociationCriteria;
import com.it4innov.service.dto.DocumentAssociationDTO;
import com.it4innov.service.mapper.DocumentAssociationMapper;
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
 * Integration tests for the {@link DocumentAssociationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentAssociationResourceIT {

    private static final String DEFAULT_CODE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_CODE_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ENREGISTREMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ENREGISTREMENT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ENREGISTREMENT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_ARCHIVAGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ARCHIVAGE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ARCHIVAGE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-associations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentAssociationRepository documentAssociationRepository;

    @Autowired
    private DocumentAssociationMapper documentAssociationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentAssociationMockMvc;

    private DocumentAssociation documentAssociation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentAssociation createEntity(EntityManager em) {
        DocumentAssociation documentAssociation = new DocumentAssociation()
            .codeDocument(DEFAULT_CODE_DOCUMENT)
            .libele(DEFAULT_LIBELE)
            .description(DEFAULT_DESCRIPTION)
            .dateEnregistrement(DEFAULT_DATE_ENREGISTREMENT)
            .dateArchivage(DEFAULT_DATE_ARCHIVAGE)
            .version(DEFAULT_VERSION);
        return documentAssociation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentAssociation createUpdatedEntity(EntityManager em) {
        DocumentAssociation documentAssociation = new DocumentAssociation()
            .codeDocument(UPDATED_CODE_DOCUMENT)
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .dateEnregistrement(UPDATED_DATE_ENREGISTREMENT)
            .dateArchivage(UPDATED_DATE_ARCHIVAGE)
            .version(UPDATED_VERSION);
        return documentAssociation;
    }

    @BeforeEach
    public void initTest() {
        documentAssociation = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentAssociation() throws Exception {
        int databaseSizeBeforeCreate = documentAssociationRepository.findAll().size();
        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);
        restDocumentAssociationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentAssociation testDocumentAssociation = documentAssociationList.get(documentAssociationList.size() - 1);
        assertThat(testDocumentAssociation.getCodeDocument()).isEqualTo(DEFAULT_CODE_DOCUMENT);
        assertThat(testDocumentAssociation.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testDocumentAssociation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentAssociation.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
        assertThat(testDocumentAssociation.getDateArchivage()).isEqualTo(DEFAULT_DATE_ARCHIVAGE);
        assertThat(testDocumentAssociation.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void createDocumentAssociationWithExistingId() throws Exception {
        // Create the DocumentAssociation with an existing ID
        documentAssociation.setId(1L);
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        int databaseSizeBeforeCreate = documentAssociationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentAssociationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocumentAssociations() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentAssociation.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeDocument").value(hasItem(DEFAULT_CODE_DOCUMENT)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateEnregistrement").value(hasItem(DEFAULT_DATE_ENREGISTREMENT.toString())))
            .andExpect(jsonPath("$.[*].dateArchivage").value(hasItem(DEFAULT_DATE_ARCHIVAGE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    void getDocumentAssociation() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get the documentAssociation
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL_ID, documentAssociation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentAssociation.getId().intValue()))
            .andExpect(jsonPath("$.codeDocument").value(DEFAULT_CODE_DOCUMENT))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateEnregistrement").value(DEFAULT_DATE_ENREGISTREMENT.toString()))
            .andExpect(jsonPath("$.dateArchivage").value(DEFAULT_DATE_ARCHIVAGE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    void getDocumentAssociationsByIdFiltering() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        Long id = documentAssociation.getId();

        defaultDocumentAssociationShouldBeFound("id.equals=" + id);
        defaultDocumentAssociationShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentAssociationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentAssociationShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentAssociationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentAssociationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByCodeDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where codeDocument equals to DEFAULT_CODE_DOCUMENT
        defaultDocumentAssociationShouldBeFound("codeDocument.equals=" + DEFAULT_CODE_DOCUMENT);

        // Get all the documentAssociationList where codeDocument equals to UPDATED_CODE_DOCUMENT
        defaultDocumentAssociationShouldNotBeFound("codeDocument.equals=" + UPDATED_CODE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByCodeDocumentIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where codeDocument in DEFAULT_CODE_DOCUMENT or UPDATED_CODE_DOCUMENT
        defaultDocumentAssociationShouldBeFound("codeDocument.in=" + DEFAULT_CODE_DOCUMENT + "," + UPDATED_CODE_DOCUMENT);

        // Get all the documentAssociationList where codeDocument equals to UPDATED_CODE_DOCUMENT
        defaultDocumentAssociationShouldNotBeFound("codeDocument.in=" + UPDATED_CODE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByCodeDocumentIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where codeDocument is not null
        defaultDocumentAssociationShouldBeFound("codeDocument.specified=true");

        // Get all the documentAssociationList where codeDocument is null
        defaultDocumentAssociationShouldNotBeFound("codeDocument.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByCodeDocumentContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where codeDocument contains DEFAULT_CODE_DOCUMENT
        defaultDocumentAssociationShouldBeFound("codeDocument.contains=" + DEFAULT_CODE_DOCUMENT);

        // Get all the documentAssociationList where codeDocument contains UPDATED_CODE_DOCUMENT
        defaultDocumentAssociationShouldNotBeFound("codeDocument.contains=" + UPDATED_CODE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByCodeDocumentNotContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where codeDocument does not contain DEFAULT_CODE_DOCUMENT
        defaultDocumentAssociationShouldNotBeFound("codeDocument.doesNotContain=" + DEFAULT_CODE_DOCUMENT);

        // Get all the documentAssociationList where codeDocument does not contain UPDATED_CODE_DOCUMENT
        defaultDocumentAssociationShouldBeFound("codeDocument.doesNotContain=" + UPDATED_CODE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where libele equals to DEFAULT_LIBELE
        defaultDocumentAssociationShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the documentAssociationList where libele equals to UPDATED_LIBELE
        defaultDocumentAssociationShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultDocumentAssociationShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the documentAssociationList where libele equals to UPDATED_LIBELE
        defaultDocumentAssociationShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where libele is not null
        defaultDocumentAssociationShouldBeFound("libele.specified=true");

        // Get all the documentAssociationList where libele is null
        defaultDocumentAssociationShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where libele contains DEFAULT_LIBELE
        defaultDocumentAssociationShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the documentAssociationList where libele contains UPDATED_LIBELE
        defaultDocumentAssociationShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where libele does not contain DEFAULT_LIBELE
        defaultDocumentAssociationShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the documentAssociationList where libele does not contain UPDATED_LIBELE
        defaultDocumentAssociationShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where description equals to DEFAULT_DESCRIPTION
        defaultDocumentAssociationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the documentAssociationList where description equals to UPDATED_DESCRIPTION
        defaultDocumentAssociationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDocumentAssociationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the documentAssociationList where description equals to UPDATED_DESCRIPTION
        defaultDocumentAssociationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where description is not null
        defaultDocumentAssociationShouldBeFound("description.specified=true");

        // Get all the documentAssociationList where description is null
        defaultDocumentAssociationShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where description contains DEFAULT_DESCRIPTION
        defaultDocumentAssociationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the documentAssociationList where description contains UPDATED_DESCRIPTION
        defaultDocumentAssociationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where description does not contain DEFAULT_DESCRIPTION
        defaultDocumentAssociationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the documentAssociationList where description does not contain UPDATED_DESCRIPTION
        defaultDocumentAssociationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement equals to DEFAULT_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.equals=" + DEFAULT_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement equals to UPDATED_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.equals=" + UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement in DEFAULT_DATE_ENREGISTREMENT or UPDATED_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.in=" + DEFAULT_DATE_ENREGISTREMENT + "," + UPDATED_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement equals to UPDATED_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.in=" + UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement is not null
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.specified=true");

        // Get all the documentAssociationList where dateEnregistrement is null
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement is greater than or equal to DEFAULT_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.greaterThanOrEqual=" + DEFAULT_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement is greater than or equal to UPDATED_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.greaterThanOrEqual=" + UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement is less than or equal to DEFAULT_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.lessThanOrEqual=" + DEFAULT_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement is less than or equal to SMALLER_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.lessThanOrEqual=" + SMALLER_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsLessThanSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement is less than DEFAULT_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.lessThan=" + DEFAULT_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement is less than UPDATED_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.lessThan=" + UPDATED_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateEnregistrementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateEnregistrement is greater than DEFAULT_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldNotBeFound("dateEnregistrement.greaterThan=" + DEFAULT_DATE_ENREGISTREMENT);

        // Get all the documentAssociationList where dateEnregistrement is greater than SMALLER_DATE_ENREGISTREMENT
        defaultDocumentAssociationShouldBeFound("dateEnregistrement.greaterThan=" + SMALLER_DATE_ENREGISTREMENT);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage equals to DEFAULT_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.equals=" + DEFAULT_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage equals to UPDATED_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.equals=" + UPDATED_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage in DEFAULT_DATE_ARCHIVAGE or UPDATED_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.in=" + DEFAULT_DATE_ARCHIVAGE + "," + UPDATED_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage equals to UPDATED_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.in=" + UPDATED_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage is not null
        defaultDocumentAssociationShouldBeFound("dateArchivage.specified=true");

        // Get all the documentAssociationList where dateArchivage is null
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage is greater than or equal to DEFAULT_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.greaterThanOrEqual=" + DEFAULT_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage is greater than or equal to UPDATED_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.greaterThanOrEqual=" + UPDATED_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage is less than or equal to DEFAULT_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.lessThanOrEqual=" + DEFAULT_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage is less than or equal to SMALLER_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.lessThanOrEqual=" + SMALLER_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsLessThanSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage is less than DEFAULT_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.lessThan=" + DEFAULT_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage is less than UPDATED_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.lessThan=" + UPDATED_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByDateArchivageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where dateArchivage is greater than DEFAULT_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldNotBeFound("dateArchivage.greaterThan=" + DEFAULT_DATE_ARCHIVAGE);

        // Get all the documentAssociationList where dateArchivage is greater than SMALLER_DATE_ARCHIVAGE
        defaultDocumentAssociationShouldBeFound("dateArchivage.greaterThan=" + SMALLER_DATE_ARCHIVAGE);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where version equals to DEFAULT_VERSION
        defaultDocumentAssociationShouldBeFound("version.equals=" + DEFAULT_VERSION);

        // Get all the documentAssociationList where version equals to UPDATED_VERSION
        defaultDocumentAssociationShouldNotBeFound("version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where version in DEFAULT_VERSION or UPDATED_VERSION
        defaultDocumentAssociationShouldBeFound("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION);

        // Get all the documentAssociationList where version equals to UPDATED_VERSION
        defaultDocumentAssociationShouldNotBeFound("version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where version is not null
        defaultDocumentAssociationShouldBeFound("version.specified=true");

        // Get all the documentAssociationList where version is null
        defaultDocumentAssociationShouldNotBeFound("version.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByVersionContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where version contains DEFAULT_VERSION
        defaultDocumentAssociationShouldBeFound("version.contains=" + DEFAULT_VERSION);

        // Get all the documentAssociationList where version contains UPDATED_VERSION
        defaultDocumentAssociationShouldNotBeFound("version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        // Get all the documentAssociationList where version does not contain DEFAULT_VERSION
        defaultDocumentAssociationShouldNotBeFound("version.doesNotContain=" + DEFAULT_VERSION);

        // Get all the documentAssociationList where version does not contain UPDATED_VERSION
        defaultDocumentAssociationShouldBeFound("version.doesNotContain=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllDocumentAssociationsByAssociationIsEqualToSomething() throws Exception {
        Association association;
        if (TestUtil.findAll(em, Association.class).isEmpty()) {
            documentAssociationRepository.saveAndFlush(documentAssociation);
            association = AssociationResourceIT.createEntity(em);
        } else {
            association = TestUtil.findAll(em, Association.class).get(0);
        }
        em.persist(association);
        em.flush();
        documentAssociation.setAssociation(association);
        documentAssociationRepository.saveAndFlush(documentAssociation);
        Long associationId = association.getId();

        // Get all the documentAssociationList where association equals to associationId
        defaultDocumentAssociationShouldBeFound("associationId.equals=" + associationId);

        // Get all the documentAssociationList where association equals to (associationId + 1)
        defaultDocumentAssociationShouldNotBeFound("associationId.equals=" + (associationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentAssociationShouldBeFound(String filter) throws Exception {
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentAssociation.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeDocument").value(hasItem(DEFAULT_CODE_DOCUMENT)))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateEnregistrement").value(hasItem(DEFAULT_DATE_ENREGISTREMENT.toString())))
            .andExpect(jsonPath("$.[*].dateArchivage").value(hasItem(DEFAULT_DATE_ARCHIVAGE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));

        // Check, that the count call also returns 1
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentAssociationShouldNotBeFound(String filter) throws Exception {
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentAssociationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentAssociation() throws Exception {
        // Get the documentAssociation
        restDocumentAssociationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentAssociation() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();

        // Update the documentAssociation
        DocumentAssociation updatedDocumentAssociation = documentAssociationRepository.findById(documentAssociation.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentAssociation are not directly saved in db
        em.detach(updatedDocumentAssociation);
        updatedDocumentAssociation
            .codeDocument(UPDATED_CODE_DOCUMENT)
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .dateEnregistrement(UPDATED_DATE_ENREGISTREMENT)
            .dateArchivage(UPDATED_DATE_ARCHIVAGE)
            .version(UPDATED_VERSION);
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(updatedDocumentAssociation);

        restDocumentAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
        DocumentAssociation testDocumentAssociation = documentAssociationList.get(documentAssociationList.size() - 1);
        assertThat(testDocumentAssociation.getCodeDocument()).isEqualTo(UPDATED_CODE_DOCUMENT);
        assertThat(testDocumentAssociation.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDocumentAssociation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentAssociation.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
        assertThat(testDocumentAssociation.getDateArchivage()).isEqualTo(UPDATED_DATE_ARCHIVAGE);
        assertThat(testDocumentAssociation.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void putNonExistingDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentAssociationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentAssociationWithPatch() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();

        // Update the documentAssociation using partial update
        DocumentAssociation partialUpdatedDocumentAssociation = new DocumentAssociation();
        partialUpdatedDocumentAssociation.setId(documentAssociation.getId());

        partialUpdatedDocumentAssociation.libele(UPDATED_LIBELE).description(UPDATED_DESCRIPTION).dateArchivage(UPDATED_DATE_ARCHIVAGE);

        restDocumentAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentAssociation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
        DocumentAssociation testDocumentAssociation = documentAssociationList.get(documentAssociationList.size() - 1);
        assertThat(testDocumentAssociation.getCodeDocument()).isEqualTo(DEFAULT_CODE_DOCUMENT);
        assertThat(testDocumentAssociation.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDocumentAssociation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentAssociation.getDateEnregistrement()).isEqualTo(DEFAULT_DATE_ENREGISTREMENT);
        assertThat(testDocumentAssociation.getDateArchivage()).isEqualTo(UPDATED_DATE_ARCHIVAGE);
        assertThat(testDocumentAssociation.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void fullUpdateDocumentAssociationWithPatch() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();

        // Update the documentAssociation using partial update
        DocumentAssociation partialUpdatedDocumentAssociation = new DocumentAssociation();
        partialUpdatedDocumentAssociation.setId(documentAssociation.getId());

        partialUpdatedDocumentAssociation
            .codeDocument(UPDATED_CODE_DOCUMENT)
            .libele(UPDATED_LIBELE)
            .description(UPDATED_DESCRIPTION)
            .dateEnregistrement(UPDATED_DATE_ENREGISTREMENT)
            .dateArchivage(UPDATED_DATE_ARCHIVAGE)
            .version(UPDATED_VERSION);

        restDocumentAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentAssociation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentAssociation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
        DocumentAssociation testDocumentAssociation = documentAssociationList.get(documentAssociationList.size() - 1);
        assertThat(testDocumentAssociation.getCodeDocument()).isEqualTo(UPDATED_CODE_DOCUMENT);
        assertThat(testDocumentAssociation.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testDocumentAssociation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentAssociation.getDateEnregistrement()).isEqualTo(UPDATED_DATE_ENREGISTREMENT);
        assertThat(testDocumentAssociation.getDateArchivage()).isEqualTo(UPDATED_DATE_ARCHIVAGE);
        assertThat(testDocumentAssociation.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentAssociationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentAssociation() throws Exception {
        int databaseSizeBeforeUpdate = documentAssociationRepository.findAll().size();
        documentAssociation.setId(count.incrementAndGet());

        // Create the DocumentAssociation
        DocumentAssociationDTO documentAssociationDTO = documentAssociationMapper.toDto(documentAssociation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAssociationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentAssociationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentAssociation in the database
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentAssociation() throws Exception {
        // Initialize the database
        documentAssociationRepository.saveAndFlush(documentAssociation);

        int databaseSizeBeforeDelete = documentAssociationRepository.findAll().size();

        // Delete the documentAssociation
        restDocumentAssociationMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentAssociation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentAssociation> documentAssociationList = documentAssociationRepository.findAll();
        assertThat(documentAssociationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
