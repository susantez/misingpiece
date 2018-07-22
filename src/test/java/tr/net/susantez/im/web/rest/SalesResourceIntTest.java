package tr.net.susantez.im.web.rest;

import tr.net.susantez.im.MisingpieceApp;

import tr.net.susantez.im.domain.Sales;
import tr.net.susantez.im.repository.SalesRepository;
import tr.net.susantez.im.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;


import static tr.net.susantez.im.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tr.net.susantez.im.domain.enumeration.SalesType;
import tr.net.susantez.im.domain.enumeration.Status;
/**
 * Test class for the SalesResource REST controller.
 *
 * @see SalesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MisingpieceApp.class)
public class SalesResourceIntTest {

    private static final SalesType DEFAULT_TYPE = SalesType.ONLINE;
    private static final SalesType UPDATED_TYPE = SalesType.EVENT;

    private static final Float DEFAULT_UNIT_PRICE = 1F;
    private static final Float UPDATED_UNIT_PRICE = 2F;

    private static final Float DEFAULT_ITEM_COUNT = 1F;
    private static final Float UPDATED_ITEM_COUNT = 2F;

    private static final Float DEFAULT_TOTAL_PRICE = 1F;
    private static final Float UPDATED_TOTAL_PRICE = 2F;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.HISTORY;

    @Autowired
    private SalesRepository salesRepository;
    @Mock
    private SalesRepository salesRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesMockMvc;

    private Sales sales;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesResource salesResource = new SalesResource(salesRepository);
        this.restSalesMockMvc = MockMvcBuilders.standaloneSetup(salesResource)
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
    public static Sales createEntity(EntityManager em) {
        Sales sales = new Sales()
            .type(DEFAULT_TYPE)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .itemCount(DEFAULT_ITEM_COUNT)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .creationDate(DEFAULT_CREATION_DATE)
            .updateDate(DEFAULT_UPDATE_DATE)
            .status(DEFAULT_STATUS);
        return sales;
    }

    @Before
    public void initTest() {
        sales = createEntity(em);
    }

    @Test
    @Transactional
    public void createSales() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isCreated());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate + 1);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSales.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testSales.getItemCount()).isEqualTo(DEFAULT_ITEM_COUNT);
        assertThat(testSales.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testSales.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testSales.getUpdateDate()).isEqualTo(DEFAULT_UPDATE_DATE);
        assertThat(testSales.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createSalesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales with an existing ID
        sales.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesRepository.findAll().size();
        // set the field null
        sales.setUnitPrice(null);

        // Create the Sales, which fails.

        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesRepository.findAll().size();
        // set the field null
        sales.setItemCount(null);

        // Create the Sales, which fails.

        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesRepository.findAll().size();
        // set the field null
        sales.setTotalPrice(null);

        // Create the Sales, which fails.

        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList
        restSalesMockMvc.perform(get("/api/sales?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sales.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].itemCount").value(hasItem(DEFAULT_ITEM_COUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].updateDate").value(hasItem(DEFAULT_UPDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    public void getAllSalesWithEagerRelationshipsIsEnabled() throws Exception {
        SalesResource salesResource = new SalesResource(salesRepositoryMock);
        when(salesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSalesMockMvc = MockMvcBuilders.standaloneSetup(salesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSalesMockMvc.perform(get("/api/sales?eagerload=true"))
        .andExpect(status().isOk());

        verify(salesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllSalesWithEagerRelationshipsIsNotEnabled() throws Exception {
        SalesResource salesResource = new SalesResource(salesRepositoryMock);
            when(salesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restSalesMockMvc = MockMvcBuilders.standaloneSetup(salesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSalesMockMvc.perform(get("/api/sales?eagerload=true"))
        .andExpect(status().isOk());

            verify(salesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", sales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sales.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.itemCount").value(DEFAULT_ITEM_COUNT.doubleValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.updateDate").value(DEFAULT_UPDATE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingSales() throws Exception {
        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Update the sales
        Sales updatedSales = salesRepository.findById(sales.getId()).get();
        // Disconnect from session so that the updates on updatedSales are not directly saved in db
        em.detach(updatedSales);
        updatedSales
            .type(UPDATED_TYPE)
            .unitPrice(UPDATED_UNIT_PRICE)
            .itemCount(UPDATED_ITEM_COUNT)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .creationDate(UPDATED_CREATION_DATE)
            .updateDate(UPDATED_UPDATE_DATE)
            .status(UPDATED_STATUS);

        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSales)))
            .andExpect(status().isOk());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSales.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testSales.getItemCount()).isEqualTo(UPDATED_ITEM_COUNT);
        assertThat(testSales.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testSales.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testSales.getUpdateDate()).isEqualTo(UPDATED_UPDATE_DATE);
        assertThat(testSales.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingSales() throws Exception {
        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Create the Sales

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sales)))
            .andExpect(status().isBadRequest());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        int databaseSizeBeforeDelete = salesRepository.findAll().size();

        // Get the sales
        restSalesMockMvc.perform(delete("/api/sales/{id}", sales.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sales.class);
        Sales sales1 = new Sales();
        sales1.setId(1L);
        Sales sales2 = new Sales();
        sales2.setId(sales1.getId());
        assertThat(sales1).isEqualTo(sales2);
        sales2.setId(2L);
        assertThat(sales1).isNotEqualTo(sales2);
        sales1.setId(null);
        assertThat(sales1).isNotEqualTo(sales2);
    }
}
