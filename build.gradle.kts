import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val springBootVersion = "3.3.4"
val springDocVersion = "2.6.0"
val mysqlVersion = "3.4.1"
val jacksonKotlinVersion = "2.18.0"
val jacksonCoreVersion = "2.18.0"
val snakeYamlVersion = "2.3"
val guavaVersion = "33.3.1-jre"
val kotlinCoroutineVersion = "1.9.0"
val slf4jVersion = "2.0.16"
val jakartaServletApiVersion = "6.1.0"

val archivesBaseName = "skc-api"
group = "com.rtomyj.skc"
version = "3.0.1"
java.sourceCompatibility = JavaVersion.VERSION_21

plugins {
  id("org.springframework.boot") version "3.3.4"
  id("io.spring.dependency-management") version "1.1.6"
  id("info.solidsoft.pitest") version "1.15.0"
  id("com.adarshr.test-logger") version "4.0.0"    // printing for JUnits

  kotlin("jvm") version "2.0.20"
  kotlin("plugin.spring") version "2.0.20"

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
  implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
  implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")    // needed for @Validated to work

  implementation("jakarta.servlet:jakarta.servlet-api:${jakartaServletApiVersion}")

  implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springDocVersion")

  implementation("org.slf4j:slf4j-api:$slf4jVersion")

  implementation("com.fasterxml.jackson.core:jackson-core:$jacksonCoreVersion")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")
  implementation("org.yaml:snakeyaml:$snakeYamlVersion")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutineVersion")

  implementation("com.google.guava:guava:$guavaVersion")

  runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")
  runtimeOnly("org.mariadb.jdbc:mariadb-java-client:$mysqlVersion")
}

tasks {
  withType<KotlinCompile> {
    compilerOptions {
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
      jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
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
      println("${layout.buildDirectory.get()}/libs/${archivesBaseName}-${project.version}.jar")
    }
  }

  register("createDockerJar", Copy::class) {
    description = "Renames JAR (removes version number) which makes it easier to deploy via Docker"
    group = "Util"

    dependsOn(bootJar)

    from("${layout.buildDirectory.get()}/libs/${archivesBaseName}-${project.version}.jar")
    into("${layout.buildDirectory.get()}/libs")

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
      "-rf", "${layout.buildDirectory.get()}/gatling-results",
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

  threads.set(
    Runtime
        .getRuntime()
        .availableProcessors() - 2
  )
  outputFormats.set(listOf("XML", "HTML"))
  timestampedReports.set(false)
  junit5PluginVersion.set("1.1.2")

  mutators.set(listOf("STRONGER"))

  avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}

jacoco {
  toolVersion = "0.8.12"
}