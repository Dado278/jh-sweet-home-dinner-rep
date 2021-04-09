package it.davide.sweethome.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.davide.sweethome.IntegrationTest;
import it.davide.sweethome.domain.SharedDinner;
import it.davide.sweethome.repository.SharedDinnerRepository;
import it.davide.sweethome.repository.search.SharedDinnerSearchRepository;
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
 * Integration tests for the {@link SharedDinnerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SharedDinnerResourceIT {

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SLOGAN = "AAAAAAAAAA";
    private static final String UPDATED_SLOGAN = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_PAGE = "https://p8y2vb.kgigmn6e1";
    private static final String UPDATED_HOME_PAGE = "7i-i2...ijK.4W6IWau1ua1/";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Double DEFAULT_COSTMIN = 1D;
    private static final Double UPDATED_COSTMIN = 2D;

    private static final Double DEFAULT_COSTMAX = 1D;
    private static final Double UPDATED_COSTMAX = 2D;

    private static final String ENTITY_API_URL = "/api/shared-dinners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/shared-dinners";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SharedDinnerRepository sharedDinnerRepository;

    /**
     * This repository is mocked in the it.davide.sweethome.repository.search test package.
     *
     * @see it.davide.sweethome.repository.search.SharedDinnerSearchRepositoryMockConfiguration
     */
    @Autowired
    private SharedDinnerSearchRepository mockSharedDinnerSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSharedDinnerMockMvc;

    private SharedDinner sharedDinner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SharedDinner createEntity(EntityManager em) {
        SharedDinner sharedDinner = new SharedDinner()
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .title(DEFAULT_TITLE)
            .slogan(DEFAULT_SLOGAN)
            .description(DEFAULT_DESCRIPTION)
            .homePage(DEFAULT_HOME_PAGE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .costmin(DEFAULT_COSTMIN)
            .costmax(DEFAULT_COSTMAX);
        return sharedDinner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SharedDinner createUpdatedEntity(EntityManager em) {
        SharedDinner sharedDinner = new SharedDinner()
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .title(UPDATED_TITLE)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX);
        return sharedDinner;
    }

    @BeforeEach
    public void initTest() {
        sharedDinner = createEntity(em);
    }

    @Test
    @Transactional
    void createSharedDinner() throws Exception {
        int databaseSizeBeforeCreate = sharedDinnerRepository.findAll().size();
        // Create the SharedDinner
        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isCreated());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeCreate + 1);
        SharedDinner testSharedDinner = sharedDinnerList.get(sharedDinnerList.size() - 1);
        assertThat(testSharedDinner.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSharedDinner.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testSharedDinner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSharedDinner.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testSharedDinner.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSharedDinner.getHomePage()).isEqualTo(DEFAULT_HOME_PAGE);
        assertThat(testSharedDinner.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testSharedDinner.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testSharedDinner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSharedDinner.getCostmin()).isEqualTo(DEFAULT_COSTMIN);
        assertThat(testSharedDinner.getCostmax()).isEqualTo(DEFAULT_COSTMAX);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(1)).save(testSharedDinner);
    }

    @Test
    @Transactional
    void createSharedDinnerWithExistingId() throws Exception {
        // Create the SharedDinner with an existing ID
        sharedDinner.setId(1L);

        int databaseSizeBeforeCreate = sharedDinnerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeCreate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sharedDinnerRepository.findAll().size();
        // set the field null
        sharedDinner.setTitle(null);

        // Create the SharedDinner, which fails.

        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = sharedDinnerRepository.findAll().size();
        // set the field null
        sharedDinner.setDescription(null);

        // Create the SharedDinner, which fails.

        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = sharedDinnerRepository.findAll().size();
        // set the field null
        sharedDinner.setAddress(null);

        // Create the SharedDinner, which fails.

        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostminIsRequired() throws Exception {
        int databaseSizeBeforeTest = sharedDinnerRepository.findAll().size();
        // set the field null
        sharedDinner.setCostmin(null);

        // Create the SharedDinner, which fails.

        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostmaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = sharedDinnerRepository.findAll().size();
        // set the field null
        sharedDinner.setCostmax(null);

        // Create the SharedDinner, which fails.

        restSharedDinnerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSharedDinners() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        // Get all the sharedDinnerList
        restSharedDinnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sharedDinner.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX.doubleValue())));
    }

    @Test
    @Transactional
    void getSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        // Get the sharedDinner
        restSharedDinnerMockMvc
            .perform(get(ENTITY_API_URL_ID, sharedDinner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sharedDinner.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.homePage").value(DEFAULT_HOME_PAGE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.costmin").value(DEFAULT_COSTMIN.doubleValue()))
            .andExpect(jsonPath("$.costmax").value(DEFAULT_COSTMAX.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingSharedDinner() throws Exception {
        // Get the sharedDinner
        restSharedDinnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();

        // Update the sharedDinner
        SharedDinner updatedSharedDinner = sharedDinnerRepository.findById(sharedDinner.getId()).get();
        // Disconnect from session so that the updates on updatedSharedDinner are not directly saved in db
        em.detach(updatedSharedDinner);
        updatedSharedDinner
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .title(UPDATED_TITLE)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX);

        restSharedDinnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSharedDinner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSharedDinner))
            )
            .andExpect(status().isOk());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);
        SharedDinner testSharedDinner = sharedDinnerList.get(sharedDinnerList.size() - 1);
        assertThat(testSharedDinner.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSharedDinner.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testSharedDinner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSharedDinner.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testSharedDinner.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSharedDinner.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testSharedDinner.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testSharedDinner.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSharedDinner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSharedDinner.getCostmin()).isEqualTo(UPDATED_COSTMIN);
        assertThat(testSharedDinner.getCostmax()).isEqualTo(UPDATED_COSTMAX);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository).save(testSharedDinner);
    }

    @Test
    @Transactional
    void putNonExistingSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sharedDinner.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sharedDinner))
            )
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void putWithIdMismatchSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sharedDinner))
            )
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void partialUpdateSharedDinnerWithPatch() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();

        // Update the sharedDinner using partial update
        SharedDinner partialUpdatedSharedDinner = new SharedDinner();
        partialUpdatedSharedDinner.setId(sharedDinner.getId());

        partialUpdatedSharedDinner
            .createDate(UPDATED_CREATE_DATE)
            .homePage(UPDATED_HOME_PAGE)
            .longitude(UPDATED_LONGITUDE)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX);

        restSharedDinnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSharedDinner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSharedDinner))
            )
            .andExpect(status().isOk());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);
        SharedDinner testSharedDinner = sharedDinnerList.get(sharedDinnerList.size() - 1);
        assertThat(testSharedDinner.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSharedDinner.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testSharedDinner.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSharedDinner.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testSharedDinner.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSharedDinner.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testSharedDinner.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testSharedDinner.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSharedDinner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSharedDinner.getCostmin()).isEqualTo(UPDATED_COSTMIN);
        assertThat(testSharedDinner.getCostmax()).isEqualTo(UPDATED_COSTMAX);
    }

    @Test
    @Transactional
    void fullUpdateSharedDinnerWithPatch() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();

        // Update the sharedDinner using partial update
        SharedDinner partialUpdatedSharedDinner = new SharedDinner();
        partialUpdatedSharedDinner.setId(sharedDinner.getId());

        partialUpdatedSharedDinner
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .title(UPDATED_TITLE)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX);

        restSharedDinnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSharedDinner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSharedDinner))
            )
            .andExpect(status().isOk());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);
        SharedDinner testSharedDinner = sharedDinnerList.get(sharedDinnerList.size() - 1);
        assertThat(testSharedDinner.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSharedDinner.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testSharedDinner.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSharedDinner.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testSharedDinner.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSharedDinner.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testSharedDinner.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testSharedDinner.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testSharedDinner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSharedDinner.getCostmin()).isEqualTo(UPDATED_COSTMIN);
        assertThat(testSharedDinner.getCostmax()).isEqualTo(UPDATED_COSTMAX);
    }

    @Test
    @Transactional
    void patchNonExistingSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sharedDinner.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sharedDinner))
            )
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sharedDinner))
            )
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();
        sharedDinner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sharedDinner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    void deleteSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        int databaseSizeBeforeDelete = sharedDinnerRepository.findAll().size();

        // Delete the sharedDinner
        restSharedDinnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, sharedDinner.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(1)).deleteById(sharedDinner.getId());
    }

    @Test
    @Transactional
    void searchSharedDinner() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);
        when(mockSharedDinnerSearchRepository.search(queryStringQuery("id:" + sharedDinner.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sharedDinner), PageRequest.of(0, 1), 1));

        // Search the sharedDinner
        restSharedDinnerMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + sharedDinner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sharedDinner.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX.doubleValue())));
    }
}
