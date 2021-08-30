val springVersion = "2.5.4"

dependencies {

    "testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
    "testRuntimeOnly"("com.h2database:h2")

    "testImplementation"("org.junit.jupiter:junit-jupiter-api")
    "testImplementation"("org.junit.jupiter:junit-jupiter-engine")

}


tasks.withType<Test> {
    useJUnitPlatform()

    minHeapSize = "256m"
    maxHeapSize = "512m"
    maxParallelForks = Runtime.getRuntime().availableProcessors() / 3 ?: 1
}