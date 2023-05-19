import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val springBootVersion = "3.1.0"
val springDocVersion = "2.1.0"
val mysqlVersion = "8.0.33"
val jacksonKotlinVersion = "2.15.1"
val jacksonCoreVersion = "2.15.1"
val snakeYamlVersion = "2.0"
val guavaVersion = "31.1-jre"
val kotlinCoroutineVersion = "1.7.1"
val slf4jVersion = "2.0.7"

val archivesBaseName = "skc-api"
group = "com.rtomyj.skc"
version = "2.1.10"
java.sourceCompatibility = JavaVersion.VERSION_17

plugins {
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("info.solidsoft.pitest") version "1.9.11"
    id("com.adarshr.test-logger") version "3.2.0"    // printing for JUnits

    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"

    jacoco
    java
    scala
}

repositories {
    mavenCentral()
}

sourceSets {
    create("integTest")

    create("perfTest") {
        scala.srcDir("src/perfTest/scala")
    }
}

apply(from = "gradle/unitTest.gradle.kts")
apply(from = "gradle/integTest.gradle.kts")
apply(from = "gradle/perfTest.gradle.kts")

configurations {
    all {
        resolutionStrategy.eachDependency {
            if (this.requested.group == ("com.fasterxml.jackson.core")) {
                this.useVersion(jacksonCoreVersion)
            }
        }
    }

    implementation {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(module = "spring-boot-starter-tomcat")
        exclude(group = "org.apache.tomcat")
        exclude(group = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    testImplementation {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")    // needed for @Validated to work
    runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")

    implementation("org.springframework.boot:spring-boot-starter-jetty:$springBootVersion")
    runtimeOnly("org.eclipse.jetty:jetty-alpn-java-server")
    runtimeOnly("org.eclipse.jetty.http2:http2-server")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonCoreVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")
    implementation("org.yaml:snakeyaml:$snakeYamlVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutineVersion")

    implementation("com.google.guava:guava:$guavaVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

    withType<BootJar> {
        group = "Build"
        description = "Creates a JAR file that can be executed to launch YGO service"

        manifest.attributes.apply {
            put("Implementation-Title", archivesBaseName)
            put("Implementation-Version", project.version)
        }
    }

    withType<Javadoc> {
        options.memberLevel = JavadocMemberLevel.PRIVATE
        source = sourceSets["main"].allJava
    }

    getByName<Jar>("jar") {
        enabled = false    // Spring Boot > 2.5.x will create two JARs (one which is useless) unless this is disabled
    }

    register("bootJarPath") {
        description = "Prints the relative path where boot JAR resides"
        group = "Util"

        doFirst {
            println("${buildDir}/libs/${archivesBaseName}-${project.version}.jar")
        }
    }

    register("createDockerJar", Copy::class) {
        description = "Renames JAR (removes version number) which makes it easier to deploy via Docker"
        group = "Util"

        dependsOn(bootJar)

        from("${buildDir}/libs/${archivesBaseName}-${project.version}.jar")
        into("${buildDir}/libs")

        rename("${archivesBaseName}-${project.version}.jar", "${archivesBaseName}.jar")
    }

    register("integTest", JavaExec::class) {
        description = "Integration test executed using Cucumber"
        group = "Verification"

        // This task needs to be of type JavaExec in order for all subtasks to run
        // Especially important is the processIntegTestResources task which will correctly configure/copy the cucumber.properties file in resources folder
        classpath = sourceSets["integTest"].runtimeClasspath
        mainClass.set("io.cucumber.core.cli.Main")
    }

    register("perfTest", JavaExec::class) {
        description = "Performance test executed using Gatling"
        group = "Verification"

        classpath = sourceSets["perfTest"].runtimeClasspath

        mainClass.set("io.gatling.app.Gatling")
        args = listOf(
            "-s", "com.rtomyj.skc.simulations.BrowseSimulation",
            "-rf", "${buildDir}/gatling-results",
        )
    }
}

pitest {
    targetClasses.set(listOf("com.rtomyj.skc.*"))
    excludedClasses.set(
        listOf(
            "com.rtomyj.skc.model.*",
            "com.rtomyj.skc.config.*",
            "com.rtomyj.skc.exception.*",
            "com.rtomyj.skc.util.constant.*",
            "com.rtomyj.skc.util.enumeration.*"
        )
    )

    threads.set(Runtime.getRuntime().availableProcessors() - 2)
    outputFormats.set(listOf("XML", "HTML"))
    timestampedReports.set(false)
    junit5PluginVersion.set("1.0.0")
    pitestVersion.set("1.9.4")

    mutators.set(listOf("STRONGER"))

    avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}

jacoco {
    toolVersion = "0.8.10"
}