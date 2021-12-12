package ch.qfs.phonebook.generator;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ch.qfs.phonebook.generator");

        noClasses()
            .that()
            .resideInAnyPackage("ch.qfs.phonebook.generator.service..")
            .or()
            .resideInAnyPackage("ch.qfs.phonebook.generator.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ch.qfs.phonebook.generator.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
