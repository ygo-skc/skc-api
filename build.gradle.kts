import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val scalaLibraryVersion = "2.13.10"
val springBootVersion = "2.7.6"
val springDocVersion = "1.6.13"
val mysqlVersion = "8.0.31"
val jacksonDatabindVersion = "2.14.1"
val jacksonKotlinVersion = "2.14.1"
val jacksonCoreVersion = "2.14.1"
val cucumberVersion = "6.7.0"
val gatlingVersion = "3.5.0"
val restAssuredVersion = "4.3.3"
val groovyVersion = "3.0.7"
val guavaVersion = "31.1-jre"
val validationAPIVersion = "2.0.1.Final"
val kotlinCoroutineVersion = "1.6.4"

val archivesBaseName = "skc-api"


plugins {
	id("org.springframework.boot") version "2.7.6"
	id("io.spring.dependency-management") version "1.1.0"
	id("info.solidsoft.pitest") version "1.9.0"
	id("com.adarshr.test-logger") version "3.2.0"	// printing for JUnits

	kotlin("jvm") version "1.7.21"
	kotlin("plugin.spring") version "1.7.21"
	kotlin("plugin.allopen") version "1.7.21"

	jacoco
	java
	scala
}

allOpen {
	annotation("com.rtomyj.skc.Open")
}


group = "com.rtomyj.skc"
version = "2.1.2"
java.sourceCompatibility = JavaVersion.VERSION_17


repositories {
	mavenCentral()
}


sourceSets {
	create("integTest")

	create("perfTest") {
		scala.srcDir("src/perfTest/scala")
	}
}


dependencies {
	compileOnly("org.scala-lang:scala-library:$scalaLibraryVersion")

	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")    // needed for @Validated to work
	runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")

	implementation("org.springframework.boot:spring-boot-starter-jetty:$springBootVersion")
	runtimeOnly("org.eclipse.jetty:jetty-alpn-java-server")
	runtimeOnly("org.eclipse.jetty.http2:http2-server")

	implementation("org.springdoc:springdoc-openapi-ui:$springDocVersion")

	runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")

	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonDatabindVersion")
	implementation("com.fasterxml.jackson.core:jackson-core:$jacksonCoreVersion")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonKotlinVersion")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$kotlinCoroutineVersion")

	implementation("com.google.guava:guava:$guavaVersion")
}


configurations {
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

apply(from = "gradle/unitTest.gradle.kts")
apply(from = "gradle/integTest.gradle.kts")
apply(from = "gradle/perfTest.gradle.kts")


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
		enabled = false	// Spring Boot > 2.5.x will create two JARs (one which is useless) unless this is disabled
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

		rename ("${archivesBaseName}-${project.version}.jar", "${archivesBaseName}.jar")
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
	excludedClasses.set(listOf("com.rtomyj.skc.*.model.*", "com.rtomyj.skc.config.*", "com.rtomyj.skc.constant.*"
		, "com.rtomyj.skc.exception.*", "com.rtomyj.skc.enums.*"))

	threads.set(Runtime.getRuntime().availableProcessors() - 2)
	outputFormats.set(listOf("XML", "HTML"))
	timestampedReports.set(false)
	junit5PluginVersion.set("1.0.0")
	pitestVersion.set("1.9.4")

	mutators.set(listOf("STRONGER"))

	avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}
