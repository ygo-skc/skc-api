plugins {
    java
    jacoco
    id("info.solidsoft.pitest")
}

val springVersion = "4.1.1"
val h2Version = "2.5.250"
val mockitKotlinVersion = "1.6.0"
val reactorTestVersion = "3.8.7"
val jacocoVersion = "0.8.15"

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("com.nhaarman:mockito-kotlin:$mockitKotlinVersion") // provides helper functions needed for mockito to work in Kotlin

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc-test:$springVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux-test:$springVersion")

    testImplementation("io.projectreactor:reactor-test:$reactorTestVersion")
    testRuntimeOnly("com.h2database:h2:$h2Version")
}

jacoco {
    toolVersion = jacocoVersion
}

tasks.withType<Test> {
    environment["LOG_LEVEL"] = "INFO"

    useJUnitPlatform()

    minHeapSize = "256m"
    maxHeapSize = "896m"
    maxParallelForks = Runtime
        .getRuntime()
        .availableProcessors() / 2

    finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())

    reports {
        xml.required.set(false)
        csv.required.set(false)
    }

    afterEvaluate {
        classDirectories.setFrom(
            classDirectories.files.map {
                fileTree(it).matching {
                    exclude(
                        "com/rtomyj/skc/model/**",
                        "com/rtomyj/skc/SKCApi.kt",
                        "com/rtomyj/skc/config/**",
                        "com/rtomyj/skc/util/constant/**",
                        "com/rtomyj/skc/util/enumeration/**",
                    )
                }
            },
        )
    }

    finalizedBy(tasks.withType<JacocoCoverageVerification>())
}

tasks.withType<JacocoCoverageVerification> {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.3".toBigDecimal()
            }
        }

        rule {
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.2".toBigDecimal()
            }
        }
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
            "com.rtomyj.skc.util.enumeration.*",
        ),
    )

    threads.set(
        Runtime
            .getRuntime()
            .availableProcessors() - 2,
    )
    outputFormats.set(listOf("XML", "HTML"))
    timestampedReports.set(false)
    junit5PluginVersion.set("1.2.3")

    mutators.set(listOf("STRONGER"))

    avoidCallsTo.set(setOf("kotlin.jvm.internal", "org.springframework.util.StopWatch", "org.slf4j.Logger"))
}
