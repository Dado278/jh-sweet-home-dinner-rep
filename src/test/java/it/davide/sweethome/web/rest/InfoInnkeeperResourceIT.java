package it.davide.sweethome.web.rest;

import it.davide.sweethome.JhSweetHomeDinnerApplicationApp;
import it.davide.sweethome.domain.InfoInnkeeper;
import it.davide.sweethome.repository.InfoInnkeeperRepository;
import it.davide.sweethome.repository.search.InfoInnkeeperSearchRepository;
import it.davide.sweethome.service.InfoInnkeeperService;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InfoInnkeeperResource} REST controller.
 */
@SpringBootTest(classes = JhSweetHomeDinnerApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class InfoInnkeeperResourceIT {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

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

    private static final String DEFAULT_SERVICES = "AAAAAAAAAA";
    private static final String UPDATED_SERVICES = "BBBBBBBBBB";

    @Autowired
    private InfoInnkeeperRepository infoInnkeeperRepository;

    @Autowired
    private InfoInnkeeperService infoInnkeeperService;

    /**
     * This repository is mocked in the it.davide.sweethome.repository.search test package.
     *
     * @see it.davide.sweethome.repository.search.InfoInnkeeperSearchRepositoryMockConfiguration
     */
    @Autowired
    private InfoInnkeeperSearchRepository mockInfoInnkeeperSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInfoInnkeeperMockMvc;

    private InfoInnkeeper infoInnkeeper;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoInnkeeper createEntity(EntityManager em) {
        InfoInnkeeper infoInnkeeper = new InfoInnkeeper()
            .nickname(DEFAULT_NICKNAME)
            .slogan(DEFAULT_SLOGAN)
            .description(DEFAULT_DESCRIPTION)
            .homePage(DEFAULT_HOME_PAGE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .address(DEFAULT_ADDRESS)
            .services(DEFAULT_SERVICES);
        return infoInnkeeper;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InfoInnkeeper createUpdatedEntity(EntityManager em) {
        InfoInnkeeper infoInnkeeper = new InfoInnkeeper()
            .nickname(UPDATED_NICKNAME)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .services(UPDATED_SERVICES);
        return infoInnkeeper;
    }

    @BeforeEach
    public void initTest() {
        infoInnkeeper = createEntity(em);
    }

    @Test
    @Transactional
    public void createInfoInnkeeper() throws Exception {
        int databaseSizeBeforeCreate = infoInnkeeperRepository.findAll().size();
        // Create the InfoInnkeeper
        restInfoInnkeeperMockMvc.perform(post("/api/info-innkeepers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infoInnkeeper)))
            .andExpect(status().isCreated());

        // Validate the InfoInnkeeper in the database
        List<InfoInnkeeper> infoInnkeeperList = infoInnkeeperRepository.findAll();
        assertThat(infoInnkeeperList).hasSize(databaseSizeBeforeCreate + 1);
        InfoInnkeeper testInfoInnkeeper = infoInnkeeperList.get(infoInnkeeperList.size() - 1);
        assertThat(testInfoInnkeeper.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testInfoInnkeeper.getSlogan()).isEqualTo(DEFAULT_SLOGAN);
        assertThat(testInfoInnkeeper.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInfoInnkeeper.getHomePage()).isEqualTo(DEFAULT_HOME_PAGE);
        assertThat(testInfoInnkeeper.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testInfoInnkeeper.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testInfoInnkeeper.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testInfoInnkeeper.getServices()).isEqualTo(DEFAULT_SERVICES);

        // Validate the InfoInnkeeper in Elasticsearch
        verify(mockInfoInnkeeperSearchRepository, times(1)).save(testInfoInnkeeper);
    }

    @Test
    @Transactional
    public void createInfoInnkeeperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = infoInnkeeperRepository.findAll().size();

        // Create the InfoInnkeeper with an existing ID
        infoInnkeeper.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInfoInnkeeperMockMvc.perform(post("/api/info-innkeepers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infoInnkeeper)))
            .andExpect(status().isBadRequest());

        // Validate the InfoInnkeeper in the database
        List<InfoInnkeeper> infoInnkeeperList = infoInnkeeperRepository.findAll();
        assertThat(infoInnkeeperList).hasSize(databaseSizeBeforeCreate);

        // Validate the InfoInnkeeper in Elasticsearch
        verify(mockInfoInnkeeperSearchRepository, times(0)).save(infoInnkeeper);
    }


    @Test
    @Transactional
    public void getAllInfoInnkeepers() throws Exception {
        // Initialize the database
        infoInnkeeperRepository.saveAndFlush(infoInnkeeper);

        // Get all the infoInnkeeperList
        restInfoInnkeeperMockMvc.perform(get("/api/info-innkeepers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infoInnkeeper.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)));
    }
    
    @Test
    @Transactional
    public void getInfoInnkeeper() throws Exception {
        // Initialize the database
        infoInnkeeperRepository.saveAndFlush(infoInnkeeper);

        // Get the infoInnkeeper
        restInfoInnkeeperMockMvc.perform(get("/api/info-innkeepers/{id}", infoInnkeeper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(infoInnkeeper.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.slogan").value(DEFAULT_SLOGAN))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.homePage").value(DEFAULT_HOME_PAGE))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.services").value(DEFAULT_SERVICES));
    }
    @Test
    @Transactional
    public void getNonExistingInfoInnkeeper() throws Exception {
        // Get the infoInnkeeper
        restInfoInnkeeperMockMvc.perform(get("/api/info-innkeepers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInfoInnkeeper() throws Exception {
        // Initialize the database
        infoInnkeeperService.save(infoInnkeeper);

        int databaseSizeBeforeUpdate = infoInnkeeperRepository.findAll().size();

        // Update the infoInnkeeper
        InfoInnkeeper updatedInfoInnkeeper = infoInnkeeperRepository.findById(infoInnkeeper.getId()).get();
        // Disconnect from session so that the updates on updatedInfoInnkeeper are not directly saved in db
        em.detach(updatedInfoInnkeeper);
        updatedInfoInnkeeper
            .nickname(UPDATED_NICKNAME)
            .slogan(UPDATED_SLOGAN)
            .description(UPDATED_DESCRIPTION)
            .homePage(UPDATED_HOME_PAGE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .address(UPDATED_ADDRESS)
            .services(UPDATED_SERVICES);

        restInfoInnkeeperMockMvc.perform(put("/api/info-innkeepers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedInfoInnkeeper)))
            .andExpect(status().isOk());

        // Validate the InfoInnkeeper in the database
        List<InfoInnkeeper> infoInnkeeperList = infoInnkeeperRepository.findAll();
        assertThat(infoInnkeeperList).hasSize(databaseSizeBeforeUpdate);
        InfoInnkeeper testInfoInnkeeper = infoInnkeeperList.get(infoInnkeeperList.size() - 1);
        assertThat(testInfoInnkeeper.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testInfoInnkeeper.getSlogan()).isEqualTo(UPDATED_SLOGAN);
        assertThat(testInfoInnkeeper.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInfoInnkeeper.getHomePage()).isEqualTo(UPDATED_HOME_PAGE);
        assertThat(testInfoInnkeeper.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testInfoInnkeeper.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testInfoInnkeeper.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testInfoInnkeeper.getServices()).isEqualTo(UPDATED_SERVICES);

        // Validate the InfoInnkeeper in Elasticsearch
        verify(mockInfoInnkeeperSearchRepository, times(2)).save(testInfoInnkeeper);
    }

    @Test
    @Transactional
    public void updateNonExistingInfoInnkeeper() throws Exception {
        int databaseSizeBeforeUpdate = infoInnkeeperRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInfoInnkeeperMockMvc.perform(put("/api/info-innkeepers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(infoInnkeeper)))
            .andExpect(status().isBadRequest());

        // Validate the InfoInnkeeper in the database
        List<InfoInnkeeper> infoInnkeeperList = infoInnkeeperRepository.findAll();
        assertThat(infoInnkeeperList).hasSize(databaseSizeBeforeUpdate);

        // Validate the InfoInnkeeper in Elasticsearch
        verify(mockInfoInnkeeperSearchRepository, times(0)).save(infoInnkeeper);
    }

    @Test
    @Transactional
    public void deleteInfoInnkeeper() throws Exception {
        // Initialize the database
        infoInnkeeperService.save(infoInnkeeper);

        int databaseSizeBeforeDelete = infoInnkeeperRepository.findAll().size();

        // Delete the infoInnkeeper
        restInfoInnkeeperMockMvc.perform(delete("/api/info-innkeepers/{id}", infoInnkeeper.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InfoInnkeeper> infoInnkeeperList = infoInnkeeperRepository.findAll();
        assertThat(infoInnkeeperList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the InfoInnkeeper in Elasticsearch
        verify(mockInfoInnkeeperSearchRepository, times(1)).deleteById(infoInnkeeper.getId());
    }

    @Test
    @Transactional
    public void searchInfoInnkeeper() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        infoInnkeeperService.save(infoInnkeeper);
        when(mockInfoInnkeeperSearchRepository.search(queryStringQuery("id:" + infoInnkeeper.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(infoInnkeeper), PageRequest.of(0, 1), 1));

        // Search the infoInnkeeper
        restInfoInnkeeperMockMvc.perform(get("/api/_search/info-innkeepers?query=id:" + infoInnkeeper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(infoInnkeeper.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].slogan").value(hasItem(DEFAULT_SLOGAN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].homePage").value(hasItem(DEFAULT_HOME_PAGE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].services").value(hasItem(DEFAULT_SERVICES)));
    }
}
