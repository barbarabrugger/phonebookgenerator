package ch.qfs.phonebook.generator.web.rest;

import ch.qfs.phonebook.generator.domain.PhonebookEntry;
import ch.qfs.phonebook.generator.repository.PhonebookEntryRepository;
import ch.qfs.phonebook.generator.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ch.qfs.phonebook.generator.domain.PhonebookEntry}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PhonebookEntryResource {

    private final Logger log = LoggerFactory.getLogger(PhonebookEntryResource.class);

    private static final String ENTITY_NAME = "phonebookEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhonebookEntryRepository phonebookEntryRepository;

    public PhonebookEntryResource(PhonebookEntryRepository phonebookEntryRepository) {
        this.phonebookEntryRepository = phonebookEntryRepository;
    }

    /**
     * {@code POST  /phonebook-entries} : Create a new phonebookEntry.
     *
     * @param phonebookEntry the phonebookEntry to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phonebookEntry, or with status {@code 400 (Bad Request)} if the phonebookEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phonebook-entries")
    public ResponseEntity<PhonebookEntry> createPhonebookEntry(@Valid @RequestBody PhonebookEntry phonebookEntry)
        throws URISyntaxException {
        log.debug("REST request to save PhonebookEntry : {}", phonebookEntry);
        if (phonebookEntry.getId() != null) {
            throw new BadRequestAlertException("A new phonebookEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhonebookEntry result = phonebookEntryRepository.save(phonebookEntry);
        return ResponseEntity
            .created(new URI("/api/phonebook-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phonebook-entries/:id} : Updates an existing phonebookEntry.
     *
     * @param id the id of the phonebookEntry to save.
     * @param phonebookEntry the phonebookEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonebookEntry,
     * or with status {@code 400 (Bad Request)} if the phonebookEntry is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phonebookEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phonebook-entries/{id}")
    public ResponseEntity<PhonebookEntry> updatePhonebookEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhonebookEntry phonebookEntry
    ) throws URISyntaxException {
        log.debug("REST request to update PhonebookEntry : {}, {}", id, phonebookEntry);
        if (phonebookEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonebookEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phonebookEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhonebookEntry result = phonebookEntryRepository.save(phonebookEntry);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phonebookEntry.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phonebook-entries/:id} : Partial updates given fields of an existing phonebookEntry, field will ignore if it is null
     *
     * @param id the id of the phonebookEntry to save.
     * @param phonebookEntry the phonebookEntry to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonebookEntry,
     * or with status {@code 400 (Bad Request)} if the phonebookEntry is not valid,
     * or with status {@code 404 (Not Found)} if the phonebookEntry is not found,
     * or with status {@code 500 (Internal Server Error)} if the phonebookEntry couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phonebook-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhonebookEntry> partialUpdatePhonebookEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhonebookEntry phonebookEntry
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhonebookEntry partially : {}, {}", id, phonebookEntry);
        if (phonebookEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonebookEntry.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phonebookEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhonebookEntry> result = phonebookEntryRepository
            .findById(phonebookEntry.getId())
            .map(existingPhonebookEntry -> {
                if (phonebookEntry.getDescription() != null) {
                    existingPhonebookEntry.setDescription(phonebookEntry.getDescription());
                }

                return existingPhonebookEntry;
            })
            .map(phonebookEntryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phonebookEntry.getId().toString())
        );
    }

    /**
     * {@code GET  /phonebook-entries} : get all the phonebookEntries.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phonebookEntries in body.
     */
    @GetMapping("/phonebook-entries")
    public List<PhonebookEntry> getAllPhonebookEntries() {
        log.debug("REST request to get all PhonebookEntries");
        return phonebookEntryRepository.findAll();
    }

    /**
     * {@code GET  /phonebook-entries/:id} : get the "id" phonebookEntry.
     *
     * @param id the id of the phonebookEntry to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phonebookEntry, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phonebook-entries/{id}")
    public ResponseEntity<PhonebookEntry> getPhonebookEntry(@PathVariable Long id) {
        log.debug("REST request to get PhonebookEntry : {}", id);
        Optional<PhonebookEntry> phonebookEntry = phonebookEntryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phonebookEntry);
    }

    /**
     * {@code DELETE  /phonebook-entries/:id} : delete the "id" phonebookEntry.
     *
     * @param id the id of the phonebookEntry to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phonebook-entries/{id}")
    public ResponseEntity<Void> deletePhonebookEntry(@PathVariable Long id) {
        log.debug("REST request to delete PhonebookEntry : {}", id);
        phonebookEntryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
