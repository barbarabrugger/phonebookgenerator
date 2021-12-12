package ch.qfs.phonebook.generator.web.rest;

import ch.qfs.phonebook.generator.domain.PhoneNumber;
import ch.qfs.phonebook.generator.repository.PhoneNumberRepository;
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
 * REST controller for managing {@link ch.qfs.phonebook.generator.domain.PhoneNumber}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PhoneNumberResource {

    private final Logger log = LoggerFactory.getLogger(PhoneNumberResource.class);

    private static final String ENTITY_NAME = "phoneNumber";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneNumberRepository phoneNumberRepository;

    public PhoneNumberResource(PhoneNumberRepository phoneNumberRepository) {
        this.phoneNumberRepository = phoneNumberRepository;
    }

    /**
     * {@code POST  /phone-numbers} : Create a new phoneNumber.
     *
     * @param phoneNumber the phoneNumber to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phoneNumber, or with status {@code 400 (Bad Request)} if the phoneNumber has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/phone-numbers")
    public ResponseEntity<PhoneNumber> createPhoneNumber(@Valid @RequestBody PhoneNumber phoneNumber) throws URISyntaxException {
        log.debug("REST request to save PhoneNumber : {}", phoneNumber);
        if (phoneNumber.getId() != null) {
            throw new BadRequestAlertException("A new phoneNumber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhoneNumber result = phoneNumberRepository.save(phoneNumber);
        return ResponseEntity
            .created(new URI("/api/phone-numbers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phone-numbers/:id} : Updates an existing phoneNumber.
     *
     * @param id the id of the phoneNumber to save.
     * @param phoneNumber the phoneNumber to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneNumber,
     * or with status {@code 400 (Bad Request)} if the phoneNumber is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phoneNumber couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/phone-numbers/{id}")
    public ResponseEntity<PhoneNumber> updatePhoneNumber(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhoneNumber phoneNumber
    ) throws URISyntaxException {
        log.debug("REST request to update PhoneNumber : {}, {}", id, phoneNumber);
        if (phoneNumber.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneNumber.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneNumberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhoneNumber result = phoneNumberRepository.save(phoneNumber);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneNumber.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phone-numbers/:id} : Partial updates given fields of an existing phoneNumber, field will ignore if it is null
     *
     * @param id the id of the phoneNumber to save.
     * @param phoneNumber the phoneNumber to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneNumber,
     * or with status {@code 400 (Bad Request)} if the phoneNumber is not valid,
     * or with status {@code 404 (Not Found)} if the phoneNumber is not found,
     * or with status {@code 500 (Internal Server Error)} if the phoneNumber couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/phone-numbers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhoneNumber> partialUpdatePhoneNumber(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhoneNumber phoneNumber
    ) throws URISyntaxException {
        log.debug("REST request to partial update PhoneNumber partially : {}, {}", id, phoneNumber);
        if (phoneNumber.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneNumber.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneNumberRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhoneNumber> result = phoneNumberRepository
            .findById(phoneNumber.getId())
            .map(existingPhoneNumber -> {
                if (phoneNumber.getNumber() != null) {
                    existingPhoneNumber.setNumber(phoneNumber.getNumber());
                }

                return existingPhoneNumber;
            })
            .map(phoneNumberRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phoneNumber.getId().toString())
        );
    }

    /**
     * {@code GET  /phone-numbers} : get all the phoneNumbers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phoneNumbers in body.
     */
    @GetMapping("/phone-numbers")
    public List<PhoneNumber> getAllPhoneNumbers() {
        log.debug("REST request to get all PhoneNumbers");
        return phoneNumberRepository.findAll();
    }

    /**
     * {@code GET  /phone-numbers/:id} : get the "id" phoneNumber.
     *
     * @param id the id of the phoneNumber to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phoneNumber, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/phone-numbers/{id}")
    public ResponseEntity<PhoneNumber> getPhoneNumber(@PathVariable Long id) {
        log.debug("REST request to get PhoneNumber : {}", id);
        Optional<PhoneNumber> phoneNumber = phoneNumberRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(phoneNumber);
    }

    /**
     * {@code DELETE  /phone-numbers/:id} : delete the "id" phoneNumber.
     *
     * @param id the id of the phoneNumber to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/phone-numbers/{id}")
    public ResponseEntity<Void> deletePhoneNumber(@PathVariable Long id) {
        log.debug("REST request to delete PhoneNumber : {}", id);
        phoneNumberRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
