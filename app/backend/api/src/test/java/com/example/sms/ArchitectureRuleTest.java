package com.example.sms;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AnalyzeClasses(packages = "com.example.sms")
@DisplayName("アーキテクチャルール")
public class ArchitectureRuleTest {

    @Test
    @DisplayName("プレゼンテーション層はサービス層とドメイン層にアクセスできる")
    public void presentationLayerShouldOnlyAccessServiceLayerAndDomainLayer() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..presentation..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..infrastructure..")
                .check(importedClasses);
    }

    @Test
    @DisplayName("サービス層はドメイン層とインフラストラクチャ層のみにアクセスできる")
    public void serviceLayerShouldOnlyAccessDomainAndInfrastructureLayers() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .check(importedClasses);
    }

    @Test
    @DisplayName("ドメイン層は他の層にアクセスできない")
    public void domainLayerShouldNotAccessOtherLayers() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .check(importedClasses);

        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..service..")
                .check(importedClasses);

        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..infrastructure..")
                .check(importedClasses);
    }

    @Test
    @DisplayName("インフラストラクチャ層はドメイン層とサービス層以外にアクセスできない")
    public void infrastructureLayerShouldNotAccessNonDomainLayers() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.sms");
        ArchRuleDefinition.noClasses()
                .that()
                .resideInAPackage("..infrastructure..")
                .should()
                .accessClassesThat()
                .resideInAPackage("..presentation..")
                .check(importedClasses);
    }
}
