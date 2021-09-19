val springVersion = "2.5.4"
val junitVersion = "5.8.0"
val h2Version = "1.4.200"

dependencies {
    "testImplementation"(kotlin("test"))

    "testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
    "testRuntimeOnly"("com.h2database:h2:$h2Version")

    "testImplementation"("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    "testImplementation"("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}


tasks.withType<Test> {
    useJUnitPlatform()

    minHeapSize = "256m"
    maxHeapSize = "512m"
    maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 ?: 1
}