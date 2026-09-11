import io.gatling.gradle.GatlingRunTask

plugins {
    scala
    id("io.gatling.gradle")
}

val gatlingVersion = "3.15.1"

configurations {
    gatlingImplementation {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
}

dependencies {
    gatlingImplementation("io.gatling.highcharts:gatling-charts-highcharts:$gatlingVersion")
    gatlingImplementation("io.gatling:gatling-core:$gatlingVersion")
}

gatling {
    includeMainOutput = false
    includeTestOutput = false
}

tasks.register<GatlingRunTask>("skcAPIPerf") {
    dependsOn("gatlingClasses")
    description = "Performance test executed using Gatling for SKC API"
    group = "Verification"
    simulationClassName = "com.rtomyj.skc.simulations.BrowseSimulation"
}
