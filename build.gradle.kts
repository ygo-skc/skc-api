import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val springBootVersion = "2.5.4"
val swagger2Version = "3.0.0"
val javadocVersion = "3.1.1" // 3.3.0
val cache2kVersion = "2.2.1.Final"
val lombokVersion = "1.18.20"
val mysqlVersion = "8.0.26"
val jacksonVersion = "2.12.5"
val cucumberVersion = "6.7.0"
val gatlingVersion = "3.5.0"
val restAssuredVersion = "4.3.3"
val groovyVersion = "3.0.7"
val guavaVersion = "30.1.1-jre"

val archivesBaseName = "skc-api"


plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("info.solidsoft.pitest") version "1.6.0"
	kotlin("jvm") version "1.5.30"
	kotlin("plugin.spring") version "1.5.30"
	jacoco
	java
	scala
}


group = "com.rtomyj.skc"
version = "1.2.5"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
	mavenCentral()
}


sourceSets {
	create("integTest") {
		java.srcDir("src/integTest/java")
	}

	create("perfTest") {
		scala.srcDir("src/perfTest/scala")
		resources.srcDir("src/perfTest/resources")
	}
}


dependencies {
	implementation("javax.validation:validation-api:2.0.1.Final")

	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-log4j2:$springBootVersion")

	implementation("org.springframework.boot:spring-boot-starter-jetty:$springBootVersion")
	implementation("org.eclipse.jetty:jetty-alpn-conscrypt-server")
	implementation("org.eclipse.jetty.http2:http2-server")

	implementation("org.apache.maven.plugins:maven-javadoc-plugin:$javadocVersion")

	implementation("io.springfox:springfox-boot-starter:$swagger2Version")

	runtimeOnly("mysql:mysql-connector-java:$mysqlVersion")

	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")

	implementation("org.cache2k:cache2k-api:$cache2kVersion")
	implementation("org.cache2k:cache2k-core:$cache2kVersion")

	implementation("com.google.guava:guava:$guavaVersion")

	annotationProcessor("org.projectlombok:lombok:$lombokVersion")	// needed to compile via gradle CLI
	implementation("org.projectlombok:lombok:$lombokVersion")	// plug in required to work in VSCode, might be the same for other IDE"s
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
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_11.toString()
		}
	}

	getByName<Jar>("jar") {
		enabled = false	// Spring Boot > 2.5.x will create two JARs (one which is useless) unless this is disabled
	}

	withType<BootJar> {
		group = "Build"
		description = "Creates a JAR file that can be executed to launch YGO service"

		manifest.attributes.apply {
			put("Implementation-Title", archivesBaseName)
		}
	}

	register("bootJarPath") {
		group = "Util"

		doFirst {
			println("${buildDir}/libs/${archivesBaseName}-${project.version}.jar")
		}
	}

	register("createDockerJar", Copy::class) {
		from("${buildDir}/libs/${archivesBaseName}-${project.version}.jar")
		into("${buildDir}/libs")

		rename ("${archivesBaseName}-${project.version}.jar", "${archivesBaseName}.jar")
	}

	withType<Javadoc> {

		options.memberLevel = JavadocMemberLevel.PRIVATE
		source = sourceSets["main"].allJava

	}

	register("runIntegrationTests") {
		dependsOn(assemble, "compileIntegTestJava")
		doLast {
			javaexec {
				mainClass.set("io.cucumber.core.cli.Main")
				classpath = sourceSets["integTest"].runtimeClasspath
				args = listOf("-g", "com/rtomyj/skc/cucumber", "src/integTest/resources")
			}
		}
	}

	register("perfTest", JavaExec::class) {
		description = "Performance test executed using Gatling"
		group = "Test"
		classpath = sourceSets["perfTest"].runtimeClasspath

		mainClass.set("io.gatling.app.Gatling")
		args = listOf(
			"-s", "com.rtomyj.skc.simulations.BrowseSimulation",
			"-rf", "${buildDir}/gatling-results",
		)
	}
}