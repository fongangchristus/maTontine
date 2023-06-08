package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Pot;
import com.it4innov.domain.TypePot;
import com.it4innov.repository.TypePotRepository;
import com.it4innov.service.criteria.TypePotCriteria;
import com.it4innov.service.dto.TypePotDTO;
import com.it4innov.service.mapper.TypePotMapper;
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
 * Integration tests for the {@link TypePotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypePotResourceIT {

    private static final String DEFAULT_LIBELE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-pots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypePotRepository typePotRepository;

    @Autowired
    private TypePotMapper typePotMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypePotMockMvc;

    private TypePot typePot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePot createEntity(EntityManager em) {
        TypePot typePot = new TypePot().libele(DEFAULT_LIBELE).descrption(DEFAULT_DESCRPTION);
        return typePot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypePot createUpdatedEntity(EntityManager em) {
        TypePot typePot = new TypePot().libele(UPDATED_LIBELE).descrption(UPDATED_DESCRPTION);
        return typePot;
    }

    @BeforeEach
    public void initTest() {
        typePot = createEntity(em);
    }

    @Test
    @Transactional
    void createTypePot() throws Exception {
        int databaseSizeBeforeCreate = typePotRepository.findAll().size();
        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);
        restTypePotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePotDTO)))
            .andExpect(status().isCreated());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeCreate + 1);
        TypePot testTypePot = typePotList.get(typePotList.size() - 1);
        assertThat(testTypePot.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testTypePot.getDescrption()).isEqualTo(DEFAULT_DESCRPTION);
    }

    @Test
    @Transactional
    void createTypePotWithExistingId() throws Exception {
        // Create the TypePot with an existing ID
        typePot.setId(1L);
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        int databaseSizeBeforeCreate = typePotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypePotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typePotRepository.findAll().size();
        // set the field null
        typePot.setLibele(null);

        // Create the TypePot, which fails.
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        restTypePotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePotDTO)))
            .andExpect(status().isBadRequest());

        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypePots() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].descrption").value(hasItem(DEFAULT_DESCRPTION)));
    }

    @Test
    @Transactional
    void getTypePot() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get the typePot
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL_ID, typePot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typePot.getId().intValue()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE))
            .andExpect(jsonPath("$.descrption").value(DEFAULT_DESCRPTION));
    }

    @Test
    @Transactional
    void getTypePotsByIdFiltering() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        Long id = typePot.getId();

        defaultTypePotShouldBeFound("id.equals=" + id);
        defaultTypePotShouldNotBeFound("id.notEquals=" + id);

        defaultTypePotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTypePotShouldNotBeFound("id.greaterThan=" + id);

        defaultTypePotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTypePotShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypePotsByLibeleIsEqualToSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where libele equals to DEFAULT_LIBELE
        defaultTypePotShouldBeFound("libele.equals=" + DEFAULT_LIBELE);

        // Get all the typePotList where libele equals to UPDATED_LIBELE
        defaultTypePotShouldNotBeFound("libele.equals=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypePotsByLibeleIsInShouldWork() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where libele in DEFAULT_LIBELE or UPDATED_LIBELE
        defaultTypePotShouldBeFound("libele.in=" + DEFAULT_LIBELE + "," + UPDATED_LIBELE);

        // Get all the typePotList where libele equals to UPDATED_LIBELE
        defaultTypePotShouldNotBeFound("libele.in=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypePotsByLibeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where libele is not null
        defaultTypePotShouldBeFound("libele.specified=true");

        // Get all the typePotList where libele is null
        defaultTypePotShouldNotBeFound("libele.specified=false");
    }

    @Test
    @Transactional
    void getAllTypePotsByLibeleContainsSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where libele contains DEFAULT_LIBELE
        defaultTypePotShouldBeFound("libele.contains=" + DEFAULT_LIBELE);

        // Get all the typePotList where libele contains UPDATED_LIBELE
        defaultTypePotShouldNotBeFound("libele.contains=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypePotsByLibeleNotContainsSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where libele does not contain DEFAULT_LIBELE
        defaultTypePotShouldNotBeFound("libele.doesNotContain=" + DEFAULT_LIBELE);

        // Get all the typePotList where libele does not contain UPDATED_LIBELE
        defaultTypePotShouldBeFound("libele.doesNotContain=" + UPDATED_LIBELE);
    }

    @Test
    @Transactional
    void getAllTypePotsByDescrptionIsEqualToSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where descrption equals to DEFAULT_DESCRPTION
        defaultTypePotShouldBeFound("descrption.equals=" + DEFAULT_DESCRPTION);

        // Get all the typePotList where descrption equals to UPDATED_DESCRPTION
        defaultTypePotShouldNotBeFound("descrption.equals=" + UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void getAllTypePotsByDescrptionIsInShouldWork() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where descrption in DEFAULT_DESCRPTION or UPDATED_DESCRPTION
        defaultTypePotShouldBeFound("descrption.in=" + DEFAULT_DESCRPTION + "," + UPDATED_DESCRPTION);

        // Get all the typePotList where descrption equals to UPDATED_DESCRPTION
        defaultTypePotShouldNotBeFound("descrption.in=" + UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void getAllTypePotsByDescrptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where descrption is not null
        defaultTypePotShouldBeFound("descrption.specified=true");

        // Get all the typePotList where descrption is null
        defaultTypePotShouldNotBeFound("descrption.specified=false");
    }

    @Test
    @Transactional
    void getAllTypePotsByDescrptionContainsSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where descrption contains DEFAULT_DESCRPTION
        defaultTypePotShouldBeFound("descrption.contains=" + DEFAULT_DESCRPTION);

        // Get all the typePotList where descrption contains UPDATED_DESCRPTION
        defaultTypePotShouldNotBeFound("descrption.contains=" + UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void getAllTypePotsByDescrptionNotContainsSomething() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        // Get all the typePotList where descrption does not contain DEFAULT_DESCRPTION
        defaultTypePotShouldNotBeFound("descrption.doesNotContain=" + DEFAULT_DESCRPTION);

        // Get all the typePotList where descrption does not contain UPDATED_DESCRPTION
        defaultTypePotShouldBeFound("descrption.doesNotContain=" + UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void getAllTypePotsByPotIsEqualToSomething() throws Exception {
        Pot pot;
        if (TestUtil.findAll(em, Pot.class).isEmpty()) {
            typePotRepository.saveAndFlush(typePot);
            pot = PotResourceIT.createEntity(em);
        } else {
            pot = TestUtil.findAll(em, Pot.class).get(0);
        }
        em.persist(pot);
        em.flush();
        typePot.addPot(pot);
        typePotRepository.saveAndFlush(typePot);
        Long potId = pot.getId();

        // Get all the typePotList where pot equals to potId
        defaultTypePotShouldBeFound("potId.equals=" + potId);

        // Get all the typePotList where pot equals to (potId + 1)
        defaultTypePotShouldNotBeFound("potId.equals=" + (potId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypePotShouldBeFound(String filter) throws Exception {
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typePot.getId().intValue())))
            .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE)))
            .andExpect(jsonPath("$.[*].descrption").value(hasItem(DEFAULT_DESCRPTION)));

        // Check, that the count call also returns 1
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypePotShouldNotBeFound(String filter) throws Exception {
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypePotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypePot() throws Exception {
        // Get the typePot
        restTypePotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypePot() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();

        // Update the typePot
        TypePot updatedTypePot = typePotRepository.findById(typePot.getId()).get();
        // Disconnect from session so that the updates on updatedTypePot are not directly saved in db
        em.detach(updatedTypePot);
        updatedTypePot.libele(UPDATED_LIBELE).descrption(UPDATED_DESCRPTION);
        TypePotDTO typePotDTO = typePotMapper.toDto(updatedTypePot);

        restTypePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
        TypePot testTypePot = typePotList.get(typePotList.size() - 1);
        assertThat(testTypePot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypePot.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void putNonExistingTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typePotDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typePotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypePotWithPatch() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();

        // Update the typePot using partial update
        TypePot partialUpdatedTypePot = new TypePot();
        partialUpdatedTypePot.setId(typePot.getId());

        partialUpdatedTypePot.libele(UPDATED_LIBELE);

        restTypePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePot))
            )
            .andExpect(status().isOk());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
        TypePot testTypePot = typePotList.get(typePotList.size() - 1);
        assertThat(testTypePot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypePot.getDescrption()).isEqualTo(DEFAULT_DESCRPTION);
    }

    @Test
    @Transactional
    void fullUpdateTypePotWithPatch() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();

        // Update the typePot using partial update
        TypePot partialUpdatedTypePot = new TypePot();
        partialUpdatedTypePot.setId(typePot.getId());

        partialUpdatedTypePot.libele(UPDATED_LIBELE).descrption(UPDATED_DESCRPTION);

        restTypePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypePot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypePot))
            )
            .andExpect(status().isOk());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
        TypePot testTypePot = typePotList.get(typePotList.size() - 1);
        assertThat(testTypePot.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTypePot.getDescrption()).isEqualTo(UPDATED_DESCRPTION);
    }

    @Test
    @Transactional
    void patchNonExistingTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typePotDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypePot() throws Exception {
        int databaseSizeBeforeUpdate = typePotRepository.findAll().size();
        typePot.setId(count.incrementAndGet());

        // Create the TypePot
        TypePotDTO typePotDTO = typePotMapper.toDto(typePot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypePotMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typePotDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypePot in the database
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypePot() throws Exception {
        // Initialize the database
        typePotRepository.saveAndFlush(typePot);

        int databaseSizeBeforeDelete = typePotRepository.findAll().size();

        // Delete the typePot
        restTypePotMockMvc
            .perform(delete(ENTITY_API_URL_ID, typePot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypePot> typePotList = typePotRepository.findAll();
        assertThat(typePotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
