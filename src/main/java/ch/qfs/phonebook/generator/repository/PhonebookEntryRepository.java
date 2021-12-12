package ch.qfs.phonebook.generator.repository;

import ch.qfs.phonebook.generator.domain.PhonebookEntry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PhonebookEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhonebookEntryRepository extends JpaRepository<PhonebookEntry, Long> {}
