package com.it4innov.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.it4innov.IntegrationTest;
import com.it4innov.domain.Adresse;
import com.it4innov.repository.AdresseRepository;
import com.it4innov.service.criteria.AdresseCriteria;
import com.it4innov.service.dto.AdresseDTO;
import com.it4innov.service.mapper.AdresseMapper;
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
 * Integration tests for the {@link AdresseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdresseResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/adresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdresseRepository adresseRepository;

    @Autowired
    private AdresseMapper adresseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdresseMockMvc;

    private Adresse adresse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adresse createEntity(EntityManager em) {
        Adresse adresse = new Adresse()
            .departmentName(DEFAULT_DEPARTMENT_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .pays(DEFAULT_PAYS);
        return adresse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adresse createUpdatedEntity(EntityManager em) {
        Adresse adresse = new Adresse()
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .pays(UPDATED_PAYS);
        return adresse;
    }

    @BeforeEach
    public void initTest() {
        adresse = createEntity(em);
    }

    @Test
    @Transactional
    void createAdresse() throws Exception {
        int databaseSizeBeforeCreate = adresseRepository.findAll().size();
        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);
        restAdresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseDTO)))
            .andExpect(status().isCreated());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeCreate + 1);
        Adresse testAdresse = adresseList.get(adresseList.size() - 1);
        assertThat(testAdresse.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testAdresse.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testAdresse.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAdresse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAdresse.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testAdresse.getPays()).isEqualTo(DEFAULT_PAYS);
    }

    @Test
    @Transactional
    void createAdresseWithExistingId() throws Exception {
        // Create the Adresse with an existing ID
        adresse.setId(1L);
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        int databaseSizeBeforeCreate = adresseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDepartmentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresseRepository.findAll().size();
        // set the field null
        adresse.setDepartmentName(null);

        // Create the Adresse, which fails.
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        restAdresseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseDTO)))
            .andExpect(status().isBadRequest());

        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdresses() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresse.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)));
    }

    @Test
    @Transactional
    void getAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get the adresse
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL_ID, adresse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adresse.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS));
    }

    @Test
    @Transactional
    void getAdressesByIdFiltering() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        Long id = adresse.getId();

        defaultAdresseShouldBeFound("id.equals=" + id);
        defaultAdresseShouldNotBeFound("id.notEquals=" + id);

        defaultAdresseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdresseShouldNotBeFound("id.greaterThan=" + id);

        defaultAdresseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdresseShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdressesByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where departmentName equals to DEFAULT_DEPARTMENT_NAME
        defaultAdresseShouldBeFound("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the adresseList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultAdresseShouldNotBeFound("departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllAdressesByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where departmentName in DEFAULT_DEPARTMENT_NAME or UPDATED_DEPARTMENT_NAME
        defaultAdresseShouldBeFound("departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME);

        // Get all the adresseList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultAdresseShouldNotBeFound("departmentName.in=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllAdressesByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where departmentName is not null
        defaultAdresseShouldBeFound("departmentName.specified=true");

        // Get all the adresseList where departmentName is null
        defaultAdresseShouldNotBeFound("departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where departmentName contains DEFAULT_DEPARTMENT_NAME
        defaultAdresseShouldBeFound("departmentName.contains=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the adresseList where departmentName contains UPDATED_DEPARTMENT_NAME
        defaultAdresseShouldNotBeFound("departmentName.contains=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllAdressesByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where departmentName does not contain DEFAULT_DEPARTMENT_NAME
        defaultAdresseShouldNotBeFound("departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the adresseList where departmentName does not contain UPDATED_DEPARTMENT_NAME
        defaultAdresseShouldBeFound("departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllAdressesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultAdresseShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the adresseList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultAdresseShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAdressesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultAdresseShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the adresseList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultAdresseShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAdressesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where streetAddress is not null
        defaultAdresseShouldBeFound("streetAddress.specified=true");

        // Get all the adresseList where streetAddress is null
        defaultAdresseShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultAdresseShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the adresseList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultAdresseShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAdressesByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultAdresseShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the adresseList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultAdresseShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAdressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAdresseShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the adresseList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAdresseShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAdressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAdresseShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the adresseList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAdresseShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAdressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where postalCode is not null
        defaultAdresseShouldBeFound("postalCode.specified=true");

        // Get all the adresseList where postalCode is null
        defaultAdresseShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where postalCode contains DEFAULT_POSTAL_CODE
        defaultAdresseShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the adresseList where postalCode contains UPDATED_POSTAL_CODE
        defaultAdresseShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAdressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultAdresseShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the adresseList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultAdresseShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAdressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where city equals to DEFAULT_CITY
        defaultAdresseShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the adresseList where city equals to UPDATED_CITY
        defaultAdresseShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAdressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where city in DEFAULT_CITY or UPDATED_CITY
        defaultAdresseShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the adresseList where city equals to UPDATED_CITY
        defaultAdresseShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAdressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where city is not null
        defaultAdresseShouldBeFound("city.specified=true");

        // Get all the adresseList where city is null
        defaultAdresseShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByCityContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where city contains DEFAULT_CITY
        defaultAdresseShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the adresseList where city contains UPDATED_CITY
        defaultAdresseShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAdressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where city does not contain DEFAULT_CITY
        defaultAdresseShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the adresseList where city does not contain UPDATED_CITY
        defaultAdresseShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllAdressesByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where stateProvince equals to DEFAULT_STATE_PROVINCE
        defaultAdresseShouldBeFound("stateProvince.equals=" + DEFAULT_STATE_PROVINCE);

        // Get all the adresseList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultAdresseShouldNotBeFound("stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAdressesByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where stateProvince in DEFAULT_STATE_PROVINCE or UPDATED_STATE_PROVINCE
        defaultAdresseShouldBeFound("stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE);

        // Get all the adresseList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultAdresseShouldNotBeFound("stateProvince.in=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAdressesByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where stateProvince is not null
        defaultAdresseShouldBeFound("stateProvince.specified=true");

        // Get all the adresseList where stateProvince is null
        defaultAdresseShouldNotBeFound("stateProvince.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByStateProvinceContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where stateProvince contains DEFAULT_STATE_PROVINCE
        defaultAdresseShouldBeFound("stateProvince.contains=" + DEFAULT_STATE_PROVINCE);

        // Get all the adresseList where stateProvince contains UPDATED_STATE_PROVINCE
        defaultAdresseShouldNotBeFound("stateProvince.contains=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAdressesByStateProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where stateProvince does not contain DEFAULT_STATE_PROVINCE
        defaultAdresseShouldNotBeFound("stateProvince.doesNotContain=" + DEFAULT_STATE_PROVINCE);

        // Get all the adresseList where stateProvince does not contain UPDATED_STATE_PROVINCE
        defaultAdresseShouldBeFound("stateProvince.doesNotContain=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllAdressesByPaysIsEqualToSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where pays equals to DEFAULT_PAYS
        defaultAdresseShouldBeFound("pays.equals=" + DEFAULT_PAYS);

        // Get all the adresseList where pays equals to UPDATED_PAYS
        defaultAdresseShouldNotBeFound("pays.equals=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllAdressesByPaysIsInShouldWork() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where pays in DEFAULT_PAYS or UPDATED_PAYS
        defaultAdresseShouldBeFound("pays.in=" + DEFAULT_PAYS + "," + UPDATED_PAYS);

        // Get all the adresseList where pays equals to UPDATED_PAYS
        defaultAdresseShouldNotBeFound("pays.in=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllAdressesByPaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where pays is not null
        defaultAdresseShouldBeFound("pays.specified=true");

        // Get all the adresseList where pays is null
        defaultAdresseShouldNotBeFound("pays.specified=false");
    }

    @Test
    @Transactional
    void getAllAdressesByPaysContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where pays contains DEFAULT_PAYS
        defaultAdresseShouldBeFound("pays.contains=" + DEFAULT_PAYS);

        // Get all the adresseList where pays contains UPDATED_PAYS
        defaultAdresseShouldNotBeFound("pays.contains=" + UPDATED_PAYS);
    }

    @Test
    @Transactional
    void getAllAdressesByPaysNotContainsSomething() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresseList where pays does not contain DEFAULT_PAYS
        defaultAdresseShouldNotBeFound("pays.doesNotContain=" + DEFAULT_PAYS);

        // Get all the adresseList where pays does not contain UPDATED_PAYS
        defaultAdresseShouldBeFound("pays.doesNotContain=" + UPDATED_PAYS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdresseShouldBeFound(String filter) throws Exception {
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresse.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)));

        // Check, that the count call also returns 1
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdresseShouldNotBeFound(String filter) throws Exception {
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdresseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdresse() throws Exception {
        // Get the adresse
        restAdresseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();

        // Update the adresse
        Adresse updatedAdresse = adresseRepository.findById(adresse.getId()).get();
        // Disconnect from session so that the updates on updatedAdresse are not directly saved in db
        em.detach(updatedAdresse);
        updatedAdresse
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .pays(UPDATED_PAYS);
        AdresseDTO adresseDTO = adresseMapper.toDto(updatedAdresse);

        restAdresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adresseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
        Adresse testAdresse = adresseList.get(adresseList.size() - 1);
        assertThat(testAdresse.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testAdresse.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testAdresse.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdresse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAdresse.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testAdresse.getPays()).isEqualTo(UPDATED_PAYS);
    }

    @Test
    @Transactional
    void putNonExistingAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adresseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdresseWithPatch() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();

        // Update the adresse using partial update
        Adresse partialUpdatedAdresse = new Adresse();
        partialUpdatedAdresse.setId(adresse.getId());

        partialUpdatedAdresse
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .pays(UPDATED_PAYS);

        restAdresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresse))
            )
            .andExpect(status().isOk());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
        Adresse testAdresse = adresseList.get(adresseList.size() - 1);
        assertThat(testAdresse.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testAdresse.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testAdresse.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdresse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAdresse.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testAdresse.getPays()).isEqualTo(UPDATED_PAYS);
    }

    @Test
    @Transactional
    void fullUpdateAdresseWithPatch() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();

        // Update the adresse using partial update
        Adresse partialUpdatedAdresse = new Adresse();
        partialUpdatedAdresse.setId(adresse.getId());

        partialUpdatedAdresse
            .departmentName(UPDATED_DEPARTMENT_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .pays(UPDATED_PAYS);

        restAdresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresse))
            )
            .andExpect(status().isOk());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
        Adresse testAdresse = adresseList.get(adresseList.size() - 1);
        assertThat(testAdresse.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testAdresse.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testAdresse.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAdresse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAdresse.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testAdresse.getPays()).isEqualTo(UPDATED_PAYS);
    }

    @Test
    @Transactional
    void patchNonExistingAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adresseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdresse() throws Exception {
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();
        adresse.setId(count.incrementAndGet());

        // Create the Adresse
        AdresseDTO adresseDTO = adresseMapper.toDto(adresse);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adresseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Adresse in the database
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        int databaseSizeBeforeDelete = adresseRepository.findAll().size();

        // Delete the adresse
        restAdresseMockMvc
            .perform(delete(ENTITY_API_URL_ID, adresse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Adresse> adresseList = adresseRepository.findAll();
        assertThat(adresseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
