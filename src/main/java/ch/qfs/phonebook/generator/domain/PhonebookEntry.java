package ch.qfs.phonebook.generator.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PhonebookEntry.
 */
@Entity
@Table(name = "phonebook_entry")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PhonebookEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "phonebookEntry")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "phonebookEntry" }, allowSetters = true)
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PhonebookEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public PhonebookEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PhoneNumber> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumber> phoneNumbers) {
        if (this.phoneNumbers != null) {
            this.phoneNumbers.forEach(i -> i.setPhonebookEntry(null));
        }
        if (phoneNumbers != null) {
            phoneNumbers.forEach(i -> i.setPhonebookEntry(this));
        }
        this.phoneNumbers = phoneNumbers;
    }

    public PhonebookEntry phoneNumbers(Set<PhoneNumber> phoneNumbers) {
        this.setPhoneNumbers(phoneNumbers);
        return this;
    }

    public PhonebookEntry addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
        phoneNumber.setPhonebookEntry(this);
        return this;
    }

    public PhonebookEntry removePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.remove(phoneNumber);
        phoneNumber.setPhonebookEntry(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhonebookEntry)) {
            return false;
        }
        return id != null && id.equals(((PhonebookEntry) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhonebookEntry{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
