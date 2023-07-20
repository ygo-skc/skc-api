val springVersion = "3.1.2"
val h2Version = "2.2.220"
val mockitKotlinVersion = "1.6.0"

dependencies {
    "testImplementation"(kotlin("test"))

    "testImplementation"("com.nhaarman:mockito-kotlin:$mockitKotlinVersion")    // provides helper functions needed for mockito to work in Kotlin
    "testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
    "testRuntimeOnly"("com.h2database:h2:$h2Version")
}

tasks.withType<Test> {
    useJUnitPlatform()

    minHeapSize = "256m"
    maxHeapSize = "896m"
    maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 ?: 1

    finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())

    reports {
        xml.required.set(false)
        csv.required.set(false)
    }

    afterEvaluate {
        classDirectories.setFrom(classDirectories.files.map {
            fileTree(it).matching {
                exclude(
                    "com/rtomyj/skc/model/**",
                    "com/rtomyj/skc/SKCApi.kt",
                    "com/rtomyj/skc/config/**",
                    "com/rtomyj/skc/util/constant/**",
                    "com/rtomyj/skc/util/enumeration/**"
                )
            }
        })
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