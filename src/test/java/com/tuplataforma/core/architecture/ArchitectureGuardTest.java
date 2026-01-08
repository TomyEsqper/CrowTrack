package com.tuplataforma.core.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

public class ArchitectureGuardTest {
    private final JavaClasses classes = new ClassFileImporter().importPackages("com.tuplataforma.core");

    @Test
    void controllers_no_deben_acceder_a_dominio_ni_persistencia() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..infrastructure.web..")
                .and().doNotHaveSimpleName("HealthController")
                .should().onlyDependOnClassesThat().resideOutsideOfPackages("..infrastructure.persistence..");
        rule.check(classes);
    }

    @Test
    void servicios_fleet_deben_usar_guards() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..application.fleet..")
                .and().haveSimpleName("CreateVehicleService").or().haveSimpleName("AssignVehicleToFleetService")
                .should().dependOnClassesThat().resideInAnyPackage("..application.security..", "..application.governance..", "..application.subscription..");
        rule.check(classes);
    }
}
