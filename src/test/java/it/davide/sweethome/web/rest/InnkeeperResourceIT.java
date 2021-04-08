package it.davide.sweethome.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.davide.sweethome.IntegrationTest;
import it.davide.sweethome.domain.Innkeeper;
import it.davide.sweethome.domain.enumeration.Gender;
import it.davide.sweethome.repository.InnkeeperRepository;
import it.davide.sweethome.repository.search.InnkeeperSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InnkeeperResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InnkeeperResourceIT {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_FRESHMAN = 1L;
    private static final Long UPDATED_FRESHMAN = 2L;

    private static final String DEFAULT_EMAIL = "jmhw-k@qnuo2.rxea.u";
    private static final String UPDATED_EMAIL = "sw3c@91.yr.ixtzse";

    private static final String DEFAULT_PHONE_NUMBER = "378463861";
    private static final String UPDATED_PHONE_NUMBER = "597804648186162";

    private static final Gender DEFAULT_GENDER = Gender.M;
    private static final Gender UPDATED_GENDER = Gender.F;

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_PAGE = "https://rpy2yg.np/";
    private static final String UPDATED_HOME_PAGE = "77.loeJeS4HV";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICES = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/innkeepers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/innkeepers";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InnkeeperRepository innkeeperRepository;

    /**
     * This repository is mocked in the it.davide.sweethome.repository.search test package.
     *
     * @see it.davide.sweethome.repository.search.InnkeeperSearchRepositoryMockConfiguration
     */
    @Autowired
    private InnkeeperSearchRepository mockInnkeeperSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInnkeeperMockMvc;

    private Innkeeper innkeeper;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Innkeeper createEntity(EntityManager em) {
        Innkeeper innkeeper = new Innkeeper()
            .nickname(DEFAULT_NICKNAME)
            .freshman(DEFAULT_FRESHMAN)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .gender(DEFAULT_GENDER)
            .slogan(DEFAULT_SLOGAN)
            .description(DEFAULT_DESCRIPTION)
            .homePage(DEFAULT_HOME_PAGE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .services(DEFAULT_SERVICES)
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE);
        return innkeeper;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Innkeeper createUpdatedEntity(EntityManager em) {
        Innkeeper innkeeper = new Innkeeper()
            .nickname(UPDATED_NICKNAME)
            .freshman(UPDATED_FRESHMAN)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .gender(UPDATED_GENDER)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .services(UPDATED_SERVICES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);
        return innkeeper;
    }

    @BeforeEach
    public void initTest() {
        innkeeper = createEntity(em);
    }

    @Test
    @Transactional
    void createInnkeeper() throws Exception {
        int databaseSizeBeforeCreate = innkeeperRepository.findAll().size();
        // Create the Innkeeper
        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isCreated());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeCreate + 1);
        Innkeeper testInnkeeper = innkeeperList.get(innkeeperList.size() - 1);
        assertThat(testInnkeeper.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testInnkeeper.getFreshman()).isEqualTo(DEFAULT_FRESHMAN);
        assertThat(testInnkeeper.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInnkeeper.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testInnkeeper.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testInnkeeper.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testInnkeeper.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInnkeeper.getHomePage()).isEqualTo(DEFAULT_HOME_PAGE);
        assertThat(testInnkeeper.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testInnkeeper.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testInnkeeper.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInnkeeper.getServices()).isEqualTo(DEFAULT_SERVICES);
        assertThat(testInnkeeper.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testInnkeeper.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(1)).save(testInnkeeper);
    }

    @Test
    @Transactional
    void createInnkeeperWithExistingId() throws Exception {
        // Create the Innkeeper with an existing ID
        innkeeper.setId(1L);

        int databaseSizeBeforeCreate = innkeeperRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeCreate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void checkNicknameIsRequired() throws Exception {
        int databaseSizeBeforeTest = innkeeperRepository.findAll().size();
        // set the field null
        innkeeper.setNickname(null);

        // Create the Innkeeper, which fails.

        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = innkeeperRepository.findAll().size();
        // set the field null
        innkeeper.setEmail(null);

        // Create the Innkeeper, which fails.

        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = innkeeperRepository.findAll().size();
        // set the field null
        innkeeper.setPhoneNumber(null);

        // Create the Innkeeper, which fails.

        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = innkeeperRepository.findAll().size();
        // set the field null
        innkeeper.setDescription(null);

        // Create the Innkeeper, which fails.

        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = innkeeperRepository.findAll().size();
        // set the field null
        innkeeper.setAddress(null);

        // Create the Innkeeper, which fails.

        restInnkeeperMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isBadRequest());

        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInnkeepers() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        // Get all the innkeeperList
        restInnkeeperMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(innkeeper.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].freshman").value(hasItem(DEFAULT_FRESHMAN.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getInnkeeper() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        // Get the innkeeper
        restInnkeeperMockMvc
            .perform(get(ENTITY_API_URL_ID, innkeeper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(innkeeper.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.freshman").value(DEFAULT_FRESHMAN.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.homePage").value(DEFAULT_HOME_PAGE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.services").value(DEFAULT_SERVICES))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInnkeeper() throws Exception {
        // Get the innkeeper
        restInnkeeperMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInnkeeper() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();

        // Update the innkeeper
        Innkeeper updatedInnkeeper = innkeeperRepository.findById(innkeeper.getId()).get();
        // Disconnect from session so that the updates on updatedInnkeeper are not directly saved in db
        em.detach(updatedInnkeeper);
        updatedInnkeeper
            .nickname(UPDATED_NICKNAME)
            .freshman(UPDATED_FRESHMAN)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .gender(UPDATED_GENDER)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .services(UPDATED_SERVICES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restInnkeeperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInnkeeper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInnkeeper))
            )
            .andExpect(status().isOk());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);
        Innkeeper testInnkeeper = innkeeperList.get(innkeeperList.size() - 1);
        assertThat(testInnkeeper.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testInnkeeper.getFreshman()).isEqualTo(UPDATED_FRESHMAN);
        assertThat(testInnkeeper.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInnkeeper.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testInnkeeper.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInnkeeper.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testInnkeeper.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInnkeeper.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testInnkeeper.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInnkeeper.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInnkeeper.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInnkeeper.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testInnkeeper.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInnkeeper.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository).save(testInnkeeper);
    }

    @Test
    @Transactional
    void putNonExistingInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, innkeeper.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(innkeeper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void putWithIdMismatchInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(innkeeper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(innkeeper)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void partialUpdateInnkeeperWithPatch() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();

        // Update the innkeeper using partial update
        Innkeeper partialUpdatedInnkeeper = new Innkeeper();
        partialUpdatedInnkeeper.setId(innkeeper.getId());

        partialUpdatedInnkeeper
            .nickname(UPDATED_NICKNAME)
            .freshman(UPDATED_FRESHMAN)
            .slogan(UPDATED_SLOGAN)
            .homePage(UPDATED_HOME_PAGE)
            .longitude(UPDATED_LONGITUDE)
            .services(UPDATED_SERVICES)
            .updateDate(UPDATED_UPDATE_DATE);

        restInnkeeperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInnkeeper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInnkeeper))
            )
            .andExpect(status().isOk());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);
        Innkeeper testInnkeeper = innkeeperList.get(innkeeperList.size() - 1);
        assertThat(testInnkeeper.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testInnkeeper.getFreshman()).isEqualTo(UPDATED_FRESHMAN);
        assertThat(testInnkeeper.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testInnkeeper.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testInnkeeper.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testInnkeeper.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testInnkeeper.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInnkeeper.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testInnkeeper.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testInnkeeper.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInnkeeper.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInnkeeper.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testInnkeeper.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testInnkeeper.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateInnkeeperWithPatch() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();

        // Update the innkeeper using partial update
        Innkeeper partialUpdatedInnkeeper = new Innkeeper();
        partialUpdatedInnkeeper.setId(innkeeper.getId());

        partialUpdatedInnkeeper
            .nickname(UPDATED_NICKNAME)
            .freshman(UPDATED_FRESHMAN)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .gender(UPDATED_GENDER)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .services(UPDATED_SERVICES)
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE);

        restInnkeeperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInnkeeper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInnkeeper))
            )
            .andExpect(status().isOk());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);
        Innkeeper testInnkeeper = innkeeperList.get(innkeeperList.size() - 1);
        assertThat(testInnkeeper.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testInnkeeper.getFreshman()).isEqualTo(UPDATED_FRESHMAN);
        assertThat(testInnkeeper.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testInnkeeper.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testInnkeeper.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testInnkeeper.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testInnkeeper.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInnkeeper.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testInnkeeper.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInnkeeper.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInnkeeper.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInnkeeper.getServices()).isEqualTo(UPDATED_SERVICES);
        assertThat(testInnkeeper.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testInnkeeper.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, innkeeper.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(innkeeper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(innkeeper))
            )
            .andExpect(status().isBadRequest());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = innkeeperRepository.findAll().size();
        innkeeper.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInnkeeperMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(innkeeper))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Innkeeper in the database
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(0)).save(innkeeper);
    }

    @Test
    @Transactional
    void deleteInnkeeper() throws Exception {
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);

        int databaseSizeBeforeDelete = innkeeperRepository.findAll().size();

        // Delete the innkeeper
        restInnkeeperMockMvc
            .perform(delete(ENTITY_API_URL_ID, innkeeper.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Innkeeper> innkeeperList = innkeeperRepository.findAll();
        assertThat(innkeeperList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Innkeeper in Elasticsearch
        verify(mockInnkeeperSearchRepository, times(1)).deleteById(innkeeper.getId());
    }

    @Test
    @Transactional
    void searchInnkeeper() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        innkeeperRepository.saveAndFlush(innkeeper);
        when(mockInnkeeperSearchRepository.search(queryStringQuery("id:" + innkeeper.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(innkeeper), PageRequest.of(0, 1), 1));

        // Search the innkeeper
        restInnkeeperMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + innkeeper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(innkeeper.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].freshman").value(hasItem(DEFAULT_FRESHMAN.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())));
    }
}
