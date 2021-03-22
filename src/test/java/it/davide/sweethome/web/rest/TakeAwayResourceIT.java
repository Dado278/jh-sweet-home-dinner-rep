package it.davide.sweethome.web.rest;

import it.davide.sweethome.JhSweetHomeDinnerApplicationApp;
import it.davide.sweethome.domain.TakeAway;
import it.davide.sweethome.repository.TakeAwayRepository;
import it.davide.sweethome.repository.search.TakeAwaySearchRepository;
import it.davide.sweethome.service.TakeAwayService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TakeAwayResource} REST controller.
 */
@SpringBootTest(classes = JhSweetHomeDinnerApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TakeAwayResourceIT {

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DISH = "AAAAAAAAAA";
    private static final String UPDATED_DISH = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTS = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTS = "BBBBBBBBBB";

    private static final String DEFAULT_ALLERGENS = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGENS = "BBBBBBBBBB";

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

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    @Autowired
    private TakeAwayRepository takeAwayRepository;

    @Autowired
    private TakeAwayService takeAwayService;

    /**
     * This repository is mocked in the it.davide.sweethome.repository.search test package.
     *
     * @see it.davide.sweethome.repository.search.TakeAwaySearchRepositoryMockConfiguration
     */
    @Autowired
    private TakeAwaySearchRepository mockTakeAwaySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTakeAwayMockMvc;

    private TakeAway takeAway;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TakeAway createEntity(EntityManager em) {
        TakeAway takeAway = new TakeAway()
            .createDate(DEFAULT_CREATE_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .dish(DEFAULT_DISH)
            .description(DEFAULT_DESCRIPTION)
            .ingredients(DEFAULT_INGREDIENTS)
            .allergens(DEFAULT_ALLERGENS)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .costmin(DEFAULT_COSTMIN)
            .costmax(DEFAULT_COSTMAX)
            .tags(DEFAULT_TAGS);
        return takeAway;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TakeAway createUpdatedEntity(EntityManager em) {
        TakeAway takeAway = new TakeAway()
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .dish(UPDATED_DISH)
            .description(UPDATED_DESCRIPTION)
            .ingredients(UPDATED_INGREDIENTS)
            .allergens(UPDATED_ALLERGENS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX)
            .tags(UPDATED_TAGS);
        return takeAway;
    }

    @BeforeEach
    public void initTest() {
        takeAway = createEntity(em);
    }

    @Test
    @Transactional
    public void createTakeAway() throws Exception {
        int databaseSizeBeforeCreate = takeAwayRepository.findAll().size();
        // Create the TakeAway
        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isCreated());

        // Validate the TakeAway in the database
        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeCreate + 1);
        TakeAway testTakeAway = takeAwayList.get(takeAwayList.size() - 1);
        assertThat(testTakeAway.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTakeAway.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testTakeAway.getDish()).isEqualTo(DEFAULT_DISH);
        assertThat(testTakeAway.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTakeAway.getIngredients()).isEqualTo(DEFAULT_INGREDIENTS);
        assertThat(testTakeAway.getAllergens()).isEqualTo(DEFAULT_ALLERGENS);
        assertThat(testTakeAway.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testTakeAway.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testTakeAway.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTakeAway.getCostmin()).isEqualTo(DEFAULT_COSTMIN);
        assertThat(testTakeAway.getCostmax()).isEqualTo(DEFAULT_COSTMAX);
        assertThat(testTakeAway.getTags()).isEqualTo(DEFAULT_TAGS);

        // Validate the TakeAway in Elasticsearch
        verify(mockTakeAwaySearchRepository, times(1)).save(testTakeAway);
    }

    @Test
    @Transactional
    public void createTakeAwayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = takeAwayRepository.findAll().size();

        // Create the TakeAway with an existing ID
        takeAway.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        // Validate the TakeAway in the database
        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeCreate);

        // Validate the TakeAway in Elasticsearch
        verify(mockTakeAwaySearchRepository, times(0)).save(takeAway);
    }


    @Test
    @Transactional
    public void checkDishIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setDish(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setDescription(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIngredientsIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setIngredients(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAllergensIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setAllergens(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setAddress(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostminIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setCostmin(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCostmaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = takeAwayRepository.findAll().size();
        // set the field null
        takeAway.setCostmax(null);

        // Create the TakeAway, which fails.


        restTakeAwayMockMvc.perform(post("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTakeAways() throws Exception {
        // Initialize the database
        takeAwayRepository.saveAndFlush(takeAway);

        // Get all the takeAwayList
        restTakeAwayMockMvc.perform(get("/api/take-aways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(takeAway.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dish").value(hasItem(DEFAULT_DISH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS)))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }
    
    @Test
    @Transactional
    public void getTakeAway() throws Exception {
        // Initialize the database
        takeAwayRepository.saveAndFlush(takeAway);

        // Get the takeAway
        restTakeAwayMockMvc.perform(get("/api/take-aways/{id}", takeAway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(takeAway.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.dish").value(DEFAULT_DISH))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS))
            .andExpect(jsonPath("$.allergens").value(DEFAULT_ALLERGENS))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.costmin").value(DEFAULT_COSTMIN.doubleValue()))
            .andExpect(jsonPath("$.costmax").value(DEFAULT_COSTMAX.doubleValue()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS));
    }
    @Test
    @Transactional
    public void getNonExistingTakeAway() throws Exception {
        // Get the takeAway
        restTakeAwayMockMvc.perform(get("/api/take-aways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTakeAway() throws Exception {
        // Initialize the database
        takeAwayService.save(takeAway);

        int databaseSizeBeforeUpdate = takeAwayRepository.findAll().size();

        // Update the takeAway
        TakeAway updatedTakeAway = takeAwayRepository.findById(takeAway.getId()).get();
        // Disconnect from session so that the updates on updatedTakeAway are not directly saved in db
        em.detach(updatedTakeAway);
        updatedTakeAway
            .createDate(UPDATED_CREATE_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .dish(UPDATED_DISH)
            .description(UPDATED_DESCRIPTION)
            .ingredients(UPDATED_INGREDIENTS)
            .allergens(UPDATED_ALLERGENS)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .costmin(UPDATED_COSTMIN)
            .costmax(UPDATED_COSTMAX)
            .tags(UPDATED_TAGS);

        restTakeAwayMockMvc.perform(put("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTakeAway)))
            .andExpect(status().isOk());

        // Validate the TakeAway in the database
        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeUpdate);
        TakeAway testTakeAway = takeAwayList.get(takeAwayList.size() - 1);
        assertThat(testTakeAway.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTakeAway.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testTakeAway.getDish()).isEqualTo(UPDATED_DISH);
        assertThat(testTakeAway.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTakeAway.getIngredients()).isEqualTo(UPDATED_INGREDIENTS);
        assertThat(testTakeAway.getAllergens()).isEqualTo(UPDATED_ALLERGENS);
        assertThat(testTakeAway.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testTakeAway.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testTakeAway.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTakeAway.getCostmin()).isEqualTo(UPDATED_COSTMIN);
        assertThat(testTakeAway.getCostmax()).isEqualTo(UPDATED_COSTMAX);
        assertThat(testTakeAway.getTags()).isEqualTo(UPDATED_TAGS);

        // Validate the TakeAway in Elasticsearch
        verify(mockTakeAwaySearchRepository, times(2)).save(testTakeAway);
    }

    @Test
    @Transactional
    public void updateNonExistingTakeAway() throws Exception {
        int databaseSizeBeforeUpdate = takeAwayRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTakeAwayMockMvc.perform(put("/api/take-aways")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(takeAway)))
            .andExpect(status().isBadRequest());

        // Validate the TakeAway in the database
        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeUpdate);

        // Validate the TakeAway in Elasticsearch
        verify(mockTakeAwaySearchRepository, times(0)).save(takeAway);
    }

    @Test
    @Transactional
    public void deleteTakeAway() throws Exception {
        // Initialize the database
        takeAwayService.save(takeAway);

        int databaseSizeBeforeDelete = takeAwayRepository.findAll().size();

        // Delete the takeAway
        restTakeAwayMockMvc.perform(delete("/api/take-aways/{id}", takeAway.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TakeAway> takeAwayList = takeAwayRepository.findAll();
        assertThat(takeAwayList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the TakeAway in Elasticsearch
        verify(mockTakeAwaySearchRepository, times(1)).deleteById(takeAway.getId());
    }

    @Test
    @Transactional
    public void searchTakeAway() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        takeAwayService.save(takeAway);
        when(mockTakeAwaySearchRepository.search(queryStringQuery("id:" + takeAway.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(takeAway), PageRequest.of(0, 1), 1));

        // Search the takeAway
        restTakeAwayMockMvc.perform(get("/api/_search/take-aways?query=id:" + takeAway.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(takeAway.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].dish").value(hasItem(DEFAULT_DISH)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS)))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN.doubleValue())))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX.doubleValue())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)));
    }
}
