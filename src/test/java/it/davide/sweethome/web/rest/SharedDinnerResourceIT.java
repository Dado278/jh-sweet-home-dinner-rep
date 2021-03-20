package it.davide.sweethome.web.rest;

import it.davide.sweethome.JhSweetHomeDinnerApplicationApp;
import it.davide.sweethome.domain.SharedDinner;
import it.davide.sweethome.repository.SharedDinnerRepository;
import it.davide.sweethome.repository.search.SharedDinnerSearchRepository;
import it.davide.sweethome.service.SharedDinnerService;

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
 * Integration tests for the {@link SharedDinnerResource} REST controller.
 */
@SpringBootTest(classes = JhSweetHomeDinnerApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SharedDinnerResourceIT {

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

    private static final String DEFAULT_HOME_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_HOME_PAGE = "BBBBBBBBBB";

    private static final String DEFAULT_LATITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LATITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_LONGITUDE = "AAAAAAAAAA";
    private static final String UPDATED_LONGITUDE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_COSTMIN = 1;
    private static final Integer UPDATED_COSTMIN = 2;

    private static final Integer DEFAULT_COSTMAX = 1;
    private static final Integer UPDATED_COSTMAX = 2;

    @Autowired
    private SharedDinnerRepository sharedDinnerRepository;

    @Autowired
    private SharedDinnerService sharedDinnerService;

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
    public void createSharedDinner() throws Exception {
        int databaseSizeBeforeCreate = sharedDinnerRepository.findAll().size();
        // Create the SharedDinner
        restSharedDinnerMockMvc.perform(post("/api/shared-dinners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
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
    public void createSharedDinnerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sharedDinnerRepository.findAll().size();

        // Create the SharedDinner with an existing ID
        sharedDinner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSharedDinnerMockMvc.perform(post("/api/shared-dinners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeCreate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }


    @Test
    @Transactional
    public void getAllSharedDinners() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        // Get all the sharedDinnerList
        restSharedDinnerMockMvc.perform(get("/api/shared-dinners?sort=id,desc"))
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
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN)))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX)));
    }
    
    @Test
    @Transactional
    public void getSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerRepository.saveAndFlush(sharedDinner);

        // Get the sharedDinner
        restSharedDinnerMockMvc.perform(get("/api/shared-dinners/{id}", sharedDinner.getId()))
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
            .andExpect(jsonPath("$.costmin").value(DEFAULT_COSTMIN))
            .andExpect(jsonPath("$.costmax").value(DEFAULT_COSTMAX));
    }
    @Test
    @Transactional
    public void getNonExistingSharedDinner() throws Exception {
        // Get the sharedDinner
        restSharedDinnerMockMvc.perform(get("/api/shared-dinners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerService.save(sharedDinner);

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

        restSharedDinnerMockMvc.perform(put("/api/shared-dinners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSharedDinner)))
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
        verify(mockSharedDinnerSearchRepository, times(2)).save(testSharedDinner);
    }

    @Test
    @Transactional
    public void updateNonExistingSharedDinner() throws Exception {
        int databaseSizeBeforeUpdate = sharedDinnerRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSharedDinnerMockMvc.perform(put("/api/shared-dinners")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sharedDinner)))
            .andExpect(status().isBadRequest());

        // Validate the SharedDinner in the database
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(0)).save(sharedDinner);
    }

    @Test
    @Transactional
    public void deleteSharedDinner() throws Exception {
        // Initialize the database
        sharedDinnerService.save(sharedDinner);

        int databaseSizeBeforeDelete = sharedDinnerRepository.findAll().size();

        // Delete the sharedDinner
        restSharedDinnerMockMvc.perform(delete("/api/shared-dinners/{id}", sharedDinner.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SharedDinner> sharedDinnerList = sharedDinnerRepository.findAll();
        assertThat(sharedDinnerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SharedDinner in Elasticsearch
        verify(mockSharedDinnerSearchRepository, times(1)).deleteById(sharedDinner.getId());
    }

    @Test
    @Transactional
    public void searchSharedDinner() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sharedDinnerService.save(sharedDinner);
        when(mockSharedDinnerSearchRepository.search(queryStringQuery("id:" + sharedDinner.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sharedDinner), PageRequest.of(0, 1), 1));

        // Search the sharedDinner
        restSharedDinnerMockMvc.perform(get("/api/_search/shared-dinners?query=id:" + sharedDinner.getId()))
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
            .andExpect(jsonPath("$.[*].costmin").value(hasItem(DEFAULT_COSTMIN)))
            .andExpect(jsonPath("$.[*].costmax").value(hasItem(DEFAULT_COSTMAX)));
    }
}
