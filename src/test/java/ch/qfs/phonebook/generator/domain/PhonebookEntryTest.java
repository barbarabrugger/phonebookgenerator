package ch.qfs.phonebook.generator.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qfs.phonebook.generator.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhonebookEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhonebookEntry.class);
        PhonebookEntry phonebookEntry1 = new PhonebookEntry();
        phonebookEntry1.setId(1L);
        PhonebookEntry phonebookEntry2 = new PhonebookEntry();
        phonebookEntry2.setId(phonebookEntry1.getId());
        assertThat(phonebookEntry1).isEqualTo(phonebookEntry2);
        phonebookEntry2.setId(2L);
        assertThat(phonebookEntry1).isNotEqualTo(phonebookEntry2);
        phonebookEntry1.setId(null);
        assertThat(phonebookEntry1).isNotEqualTo(phonebookEntry2);
    }
}
