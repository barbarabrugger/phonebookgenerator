package ch.qfs.phonebook.generator.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.qfs.phonebook.generator.IntegrationTest;
import ch.qfs.phonebook.generator.domain.PhonebookEntry;
import ch.qfs.phonebook.generator.repository.PhonebookEntryRepository;
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
 * Integration tests for the {@link PhonebookEntryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhonebookEntryResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phonebook-entries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhonebookEntryRepository phonebookEntryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhonebookEntryMockMvc;

    private PhonebookEntry phonebookEntry;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhonebookEntry createEntity(EntityManager em) {
        PhonebookEntry phonebookEntry = new PhonebookEntry().description(DEFAULT_DESCRIPTION);
        return phonebookEntry;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PhonebookEntry createUpdatedEntity(EntityManager em) {
        PhonebookEntry phonebookEntry = new PhonebookEntry().description(UPDATED_DESCRIPTION);
        return phonebookEntry;
    }

    @BeforeEach
    public void initTest() {
        phonebookEntry = createEntity(em);
    }

    @Test
    @Transactional
    void createPhonebookEntry() throws Exception {
        int databaseSizeBeforeCreate = phonebookEntryRepository.findAll().size();
        // Create the PhonebookEntry
        restPhonebookEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isCreated());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeCreate + 1);
        PhonebookEntry testPhonebookEntry = phonebookEntryList.get(phonebookEntryList.size() - 1);
        assertThat(testPhonebookEntry.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPhonebookEntryWithExistingId() throws Exception {
        // Create the PhonebookEntry with an existing ID
        phonebookEntry.setId(1L);

        int databaseSizeBeforeCreate = phonebookEntryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhonebookEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = phonebookEntryRepository.findAll().size();
        // set the field null
        phonebookEntry.setDescription(null);

        // Create the PhonebookEntry, which fails.

        restPhonebookEntryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhonebookEntries() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        // Get all the phonebookEntryList
        restPhonebookEntryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phonebookEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPhonebookEntry() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        // Get the phonebookEntry
        restPhonebookEntryMockMvc
            .perform(get(ENTITY_API_URL_ID, phonebookEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phonebookEntry.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPhonebookEntry() throws Exception {
        // Get the phonebookEntry
        restPhonebookEntryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPhonebookEntry() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();

        // Update the phonebookEntry
        PhonebookEntry updatedPhonebookEntry = phonebookEntryRepository.findById(phonebookEntry.getId()).get();
        // Disconnect from session so that the updates on updatedPhonebookEntry are not directly saved in db
        em.detach(updatedPhonebookEntry);
        updatedPhonebookEntry.description(UPDATED_DESCRIPTION);

        restPhonebookEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhonebookEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPhonebookEntry))
            )
            .andExpect(status().isOk());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
        PhonebookEntry testPhonebookEntry = phonebookEntryList.get(phonebookEntryList.size() - 1);
        assertThat(testPhonebookEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phonebookEntry.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phonebookEntry)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhonebookEntryWithPatch() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();

        // Update the phonebookEntry using partial update
        PhonebookEntry partialUpdatedPhonebookEntry = new PhonebookEntry();
        partialUpdatedPhonebookEntry.setId(phonebookEntry.getId());

        partialUpdatedPhonebookEntry.description(UPDATED_DESCRIPTION);

        restPhonebookEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhonebookEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhonebookEntry))
            )
            .andExpect(status().isOk());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
        PhonebookEntry testPhonebookEntry = phonebookEntryList.get(phonebookEntryList.size() - 1);
        assertThat(testPhonebookEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePhonebookEntryWithPatch() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();

        // Update the phonebookEntry using partial update
        PhonebookEntry partialUpdatedPhonebookEntry = new PhonebookEntry();
        partialUpdatedPhonebookEntry.setId(phonebookEntry.getId());

        partialUpdatedPhonebookEntry.description(UPDATED_DESCRIPTION);

        restPhonebookEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhonebookEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhonebookEntry))
            )
            .andExpect(status().isOk());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
        PhonebookEntry testPhonebookEntry = phonebookEntryList.get(phonebookEntryList.size() - 1);
        assertThat(testPhonebookEntry.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phonebookEntry.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isBadRequest());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhonebookEntry() throws Exception {
        int databaseSizeBeforeUpdate = phonebookEntryRepository.findAll().size();
        phonebookEntry.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhonebookEntryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phonebookEntry))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PhonebookEntry in the database
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhonebookEntry() throws Exception {
        // Initialize the database
        phonebookEntryRepository.saveAndFlush(phonebookEntry);

        int databaseSizeBeforeDelete = phonebookEntryRepository.findAll().size();

        // Delete the phonebookEntry
        restPhonebookEntryMockMvc
            .perform(delete(ENTITY_API_URL_ID, phonebookEntry.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PhonebookEntry> phonebookEntryList = phonebookEntryRepository.findAll();
        assertThat(phonebookEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
