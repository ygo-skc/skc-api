val cucumberVersion = "7.20.1"
val restAssuredVersion = "5.5.0"
val groovyVersion = "4.0.23"
val commonsLoggingVersion = "1.3.4"

dependencies {
    "integTestImplementation"("commons-logging:commons-logging:$commonsLoggingVersion")

    "integTestImplementation"("io.cucumber:cucumber-java:${cucumberVersion}")

    "integTestImplementation"("io.rest-assured:rest-assured:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:json-path:${restAssuredVersion}")
    "integTestImplementation"("io.rest-assured:xml-path:${restAssuredVersion}")

    "integTestImplementation"("org.apache.groovy:groovy:${groovyVersion}")   // Need to specify groovy version >= 3 to be able to use rest assured version >= 4.3
    "integTestImplementation"("org.apache.groovy:groovy-xml:${groovyVersion}")   // Need to specify groovy version >= 3 to be able to use rest assured version >= 4.3
}