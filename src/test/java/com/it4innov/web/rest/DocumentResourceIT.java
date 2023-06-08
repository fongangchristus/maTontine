package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Document;
import com.it4innov.repository.DocumentRepository;
import com.it4innov.service.criteria.DocumentCriteria;
import com.it4innov.service.dto.DocumentDTO;
import com.it4innov.service.mapper.DocumentMapper;
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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DOC_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_DOCUMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .libelle(DEFAULT_LIBELLE)
            .docKey(DEFAULT_DOC_KEY)
            .path(DEFAULT_PATH)
            .typeDocument(DEFAULT_TYPE_DOCUMENT);
        return document;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .libelle(UPDATED_LIBELLE)
            .docKey(UPDATED_DOC_KEY)
            .path(UPDATED_PATH)
            .typeDocument(UPDATED_TYPE_DOCUMENT);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testDocument.getDocKey()).isEqualTo(DEFAULT_DOC_KEY);
        assertThat(testDocument.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testDocument.getTypeDocument()).isEqualTo(DEFAULT_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].docKey").value(hasItem(DEFAULT_DOC_KEY)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].typeDocument").value(hasItem(DEFAULT_TYPE_DOCUMENT)));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.docKey").value(DEFAULT_DOC_KEY))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.typeDocument").value(DEFAULT_TYPE_DOCUMENT));
    }

    @Test
    @Transactional
    void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        Long id = document.getId();

        defaultDocumentShouldBeFound("id.equals=" + id);
        defaultDocumentShouldNotBeFound("id.notEquals=" + id);

        defaultDocumentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.greaterThan=" + id);

        defaultDocumentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDocumentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentsByLibelleIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where libelle equals to DEFAULT_LIBELLE
        defaultDocumentShouldBeFound("libelle.equals=" + DEFAULT_LIBELLE);

        // Get all the documentList where libelle equals to UPDATED_LIBELLE
        defaultDocumentShouldNotBeFound("libelle.equals=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByLibelleIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where libelle in DEFAULT_LIBELLE or UPDATED_LIBELLE
        defaultDocumentShouldBeFound("libelle.in=" + DEFAULT_LIBELLE + "," + UPDATED_LIBELLE);

        // Get all the documentList where libelle equals to UPDATED_LIBELLE
        defaultDocumentShouldNotBeFound("libelle.in=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByLibelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where libelle is not null
        defaultDocumentShouldBeFound("libelle.specified=true");

        // Get all the documentList where libelle is null
        defaultDocumentShouldNotBeFound("libelle.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByLibelleContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where libelle contains DEFAULT_LIBELLE
        defaultDocumentShouldBeFound("libelle.contains=" + DEFAULT_LIBELLE);

        // Get all the documentList where libelle contains UPDATED_LIBELLE
        defaultDocumentShouldNotBeFound("libelle.contains=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByLibelleNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where libelle does not contain DEFAULT_LIBELLE
        defaultDocumentShouldNotBeFound("libelle.doesNotContain=" + DEFAULT_LIBELLE);

        // Get all the documentList where libelle does not contain UPDATED_LIBELLE
        defaultDocumentShouldBeFound("libelle.doesNotContain=" + UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docKey equals to DEFAULT_DOC_KEY
        defaultDocumentShouldBeFound("docKey.equals=" + DEFAULT_DOC_KEY);

        // Get all the documentList where docKey equals to UPDATED_DOC_KEY
        defaultDocumentShouldNotBeFound("docKey.equals=" + UPDATED_DOC_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocKeyIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docKey in DEFAULT_DOC_KEY or UPDATED_DOC_KEY
        defaultDocumentShouldBeFound("docKey.in=" + DEFAULT_DOC_KEY + "," + UPDATED_DOC_KEY);

        // Get all the documentList where docKey equals to UPDATED_DOC_KEY
        defaultDocumentShouldNotBeFound("docKey.in=" + UPDATED_DOC_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docKey is not null
        defaultDocumentShouldBeFound("docKey.specified=true");

        // Get all the documentList where docKey is null
        defaultDocumentShouldNotBeFound("docKey.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByDocKeyContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docKey contains DEFAULT_DOC_KEY
        defaultDocumentShouldBeFound("docKey.contains=" + DEFAULT_DOC_KEY);

        // Get all the documentList where docKey contains UPDATED_DOC_KEY
        defaultDocumentShouldNotBeFound("docKey.contains=" + UPDATED_DOC_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocKeyNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where docKey does not contain DEFAULT_DOC_KEY
        defaultDocumentShouldNotBeFound("docKey.doesNotContain=" + DEFAULT_DOC_KEY);

        // Get all the documentList where docKey does not contain UPDATED_DOC_KEY
        defaultDocumentShouldBeFound("docKey.doesNotContain=" + UPDATED_DOC_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where path equals to DEFAULT_PATH
        defaultDocumentShouldBeFound("path.equals=" + DEFAULT_PATH);

        // Get all the documentList where path equals to UPDATED_PATH
        defaultDocumentShouldNotBeFound("path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByPathIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where path in DEFAULT_PATH or UPDATED_PATH
        defaultDocumentShouldBeFound("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH);

        // Get all the documentList where path equals to UPDATED_PATH
        defaultDocumentShouldNotBeFound("path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where path is not null
        defaultDocumentShouldBeFound("path.specified=true");

        // Get all the documentList where path is null
        defaultDocumentShouldNotBeFound("path.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByPathContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where path contains DEFAULT_PATH
        defaultDocumentShouldBeFound("path.contains=" + DEFAULT_PATH);

        // Get all the documentList where path contains UPDATED_PATH
        defaultDocumentShouldNotBeFound("path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByPathNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where path does not contain DEFAULT_PATH
        defaultDocumentShouldNotBeFound("path.doesNotContain=" + DEFAULT_PATH);

        // Get all the documentList where path does not contain UPDATED_PATH
        defaultDocumentShouldBeFound("path.doesNotContain=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where typeDocument equals to DEFAULT_TYPE_DOCUMENT
        defaultDocumentShouldBeFound("typeDocument.equals=" + DEFAULT_TYPE_DOCUMENT);

        // Get all the documentList where typeDocument equals to UPDATED_TYPE_DOCUMENT
        defaultDocumentShouldNotBeFound("typeDocument.equals=" + UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeDocumentIsInShouldWork() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where typeDocument in DEFAULT_TYPE_DOCUMENT or UPDATED_TYPE_DOCUMENT
        defaultDocumentShouldBeFound("typeDocument.in=" + DEFAULT_TYPE_DOCUMENT + "," + UPDATED_TYPE_DOCUMENT);

        // Get all the documentList where typeDocument equals to UPDATED_TYPE_DOCUMENT
        defaultDocumentShouldNotBeFound("typeDocument.in=" + UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeDocumentIsNullOrNotNull() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where typeDocument is not null
        defaultDocumentShouldBeFound("typeDocument.specified=true");

        // Get all the documentList where typeDocument is null
        defaultDocumentShouldNotBeFound("typeDocument.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeDocumentContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where typeDocument contains DEFAULT_TYPE_DOCUMENT
        defaultDocumentShouldBeFound("typeDocument.contains=" + DEFAULT_TYPE_DOCUMENT);

        // Get all the documentList where typeDocument contains UPDATED_TYPE_DOCUMENT
        defaultDocumentShouldNotBeFound("typeDocument.contains=" + UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeDocumentNotContainsSomething() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList where typeDocument does not contain DEFAULT_TYPE_DOCUMENT
        defaultDocumentShouldNotBeFound("typeDocument.doesNotContain=" + DEFAULT_TYPE_DOCUMENT);

        // Get all the documentList where typeDocument does not contain UPDATED_TYPE_DOCUMENT
        defaultDocumentShouldBeFound("typeDocument.doesNotContain=" + UPDATED_TYPE_DOCUMENT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].docKey").value(hasItem(DEFAULT_DOC_KEY)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].typeDocument").value(hasItem(DEFAULT_TYPE_DOCUMENT)));

        // Check, that the count call also returns 1
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument.libelle(UPDATED_LIBELLE).docKey(UPDATED_DOC_KEY).path(UPDATED_PATH).typeDocument(UPDATED_TYPE_DOCUMENT);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDocument.getDocKey()).isEqualTo(UPDATED_DOC_KEY);
        assertThat(testDocument.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testDocument.getTypeDocument()).isEqualTo(UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument.libelle(UPDATED_LIBELLE).docKey(UPDATED_DOC_KEY);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDocument.getDocKey()).isEqualTo(UPDATED_DOC_KEY);
        assertThat(testDocument.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testDocument.getTypeDocument()).isEqualTo(DEFAULT_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument.libelle(UPDATED_LIBELLE).docKey(UPDATED_DOC_KEY).path(UPDATED_PATH).typeDocument(UPDATED_TYPE_DOCUMENT);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testDocument.getDocKey()).isEqualTo(UPDATED_DOC_KEY);
        assertThat(testDocument.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testDocument.getTypeDocument()).isEqualTo(UPDATED_TYPE_DOCUMENT);
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, document.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
