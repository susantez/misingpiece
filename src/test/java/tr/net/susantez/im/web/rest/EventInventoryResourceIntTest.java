package tr.net.susantez.im.web.rest;

import tr.net.susantez.im.MisingpieceApp;

import tr.net.susantez.im.domain.EventInventory;
import tr.net.susantez.im.repository.EventInventoryRepository;
import tr.net.susantez.im.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static tr.net.susantez.im.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EventInventoryResource REST controller.
 *
 * @see EventInventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisingpieceApp.class)
public class EventInventoryResourceIntTest {

    private static final Float DEFAULT_ITEM_COUNT = 1F;
    private static final Float UPDATED_ITEM_COUNT = 2F;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private EventInventoryRepository eventInventoryRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventInventoryMockMvc;

    private EventInventory eventInventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventInventoryResource eventInventoryResource = new EventInventoryResource(eventInventoryRepository);
        this.restEventInventoryMockMvc = MockMvcBuilders.standaloneSetup(eventInventoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventInventory createEntity(EntityManager em) {
        EventInventory eventInventory = new EventInventory()
            .itemCount(DEFAULT_ITEM_COUNT)
            .creationDate(DEFAULT_CREATION_DATE);
        return eventInventory;
    }

    @Before
    public void initTest() {
        eventInventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventInventory() throws Exception {
        int databaseSizeBeforeCreate = eventInventoryRepository.findAll().size();

        // Create the EventInventory
        restEventInventoryMockMvc.perform(post("/api/event-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventInventory)))
            .andExpect(status().isCreated());

        // Validate the EventInventory in the database
        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeCreate + 1);
        EventInventory testEventInventory = eventInventoryList.get(eventInventoryList.size() - 1);
        assertThat(testEventInventory.getItemCount()).isEqualTo(DEFAULT_ITEM_COUNT);
        assertThat(testEventInventory.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    public void createEventInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventInventoryRepository.findAll().size();

        // Create the EventInventory with an existing ID
        eventInventory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventInventoryMockMvc.perform(post("/api/event-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventInventory)))
            .andExpect(status().isBadRequest());

        // Validate the EventInventory in the database
        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkItemCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventInventoryRepository.findAll().size();
        // set the field null
        eventInventory.setItemCount(null);

        // Create the EventInventory, which fails.

        restEventInventoryMockMvc.perform(post("/api/event-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventInventory)))
            .andExpect(status().isBadRequest());

        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEventInventories() throws Exception {
        // Initialize the database
        eventInventoryRepository.saveAndFlush(eventInventory);

        // Get all the eventInventoryList
        restEventInventoryMockMvc.perform(get("/api/event-inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventInventory.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCount").value(hasItem(DEFAULT_ITEM_COUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getEventInventory() throws Exception {
        // Initialize the database
        eventInventoryRepository.saveAndFlush(eventInventory);

        // Get the eventInventory
        restEventInventoryMockMvc.perform(get("/api/event-inventories/{id}", eventInventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventInventory.getId().intValue()))
            .andExpect(jsonPath("$.itemCount").value(DEFAULT_ITEM_COUNT.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEventInventory() throws Exception {
        // Get the eventInventory
        restEventInventoryMockMvc.perform(get("/api/event-inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventInventory() throws Exception {
        // Initialize the database
        eventInventoryRepository.saveAndFlush(eventInventory);

        int databaseSizeBeforeUpdate = eventInventoryRepository.findAll().size();

        // Update the eventInventory
        EventInventory updatedEventInventory = eventInventoryRepository.findById(eventInventory.getId()).get();
        // Disconnect from session so that the updates on updatedEventInventory are not directly saved in db
        em.detach(updatedEventInventory);
        updatedEventInventory
            .itemCount(UPDATED_ITEM_COUNT)
            .creationDate(UPDATED_CREATION_DATE);

        restEventInventoryMockMvc.perform(put("/api/event-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventInventory)))
            .andExpect(status().isOk());

        // Validate the EventInventory in the database
        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeUpdate);
        EventInventory testEventInventory = eventInventoryList.get(eventInventoryList.size() - 1);
        assertThat(testEventInventory.getItemCount()).isEqualTo(UPDATED_ITEM_COUNT);
        assertThat(testEventInventory.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEventInventory() throws Exception {
        int databaseSizeBeforeUpdate = eventInventoryRepository.findAll().size();

        // Create the EventInventory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventInventoryMockMvc.perform(put("/api/event-inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventInventory)))
            .andExpect(status().isBadRequest());

        // Validate the EventInventory in the database
        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventInventory() throws Exception {
        // Initialize the database
        eventInventoryRepository.saveAndFlush(eventInventory);

        int databaseSizeBeforeDelete = eventInventoryRepository.findAll().size();

        // Get the eventInventory
        restEventInventoryMockMvc.perform(delete("/api/event-inventories/{id}", eventInventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EventInventory> eventInventoryList = eventInventoryRepository.findAll();
        assertThat(eventInventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventInventory.class);
        EventInventory eventInventory1 = new EventInventory();
        eventInventory1.setId(1L);
        EventInventory eventInventory2 = new EventInventory();
        eventInventory2.setId(eventInventory1.getId());
        assertThat(eventInventory1).isEqualTo(eventInventory2);
        eventInventory2.setId(2L);
        assertThat(eventInventory1).isNotEqualTo(eventInventory2);
        eventInventory1.setId(null);
        assertThat(eventInventory1).isNotEqualTo(eventInventory2);
    }
}
