val cucumberVersion = "7.2.3"
val restAssuredVersion = "5.0.0"
val groovyVersion = "3.0.10"
val junitVersion = "5.8.2"
val commonsLoggingVersion = "1.2"

dependencies {
    "integTestImplementation"("commons-logging:commons-logging:$commonsLoggingVersion")

    "integTestImplementation"("io.cucumber:cucumber-java:${cucumberVersion}")

    "integTestImplementation"("io.rest-assured:rest-assured:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:json-path:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:xml-path:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:json-schema-validator:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:rest-assured-common:${restAssuredVersion}")

    "integTestImplementation"("org.codehaus.groovy:groovy:${groovyVersion}")   // Need to specify groovy version >= 3 to be able to use rest assured version >= 4.3
    "integTestImplementation"("org.codehaus.groovy:groovy-xml:${groovyVersion}")   // Need to specify groovy version >= 3 to be able to use rest assured version >= 4.3

    "integTestImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    "integTestImplementation"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}