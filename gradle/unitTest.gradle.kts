val springVersion = "2.6.0"
val h2Version = "2.0.202"

dependencies {
    "testImplementation"(kotlin("test"))

    "testImplementation"("org.springframework.boot:spring-boot-starter-test:$springVersion")
    "testRuntimeOnly"("com.h2database:h2:$h2Version")
}


tasks.withType<Test> {
    useJUnitPlatform()

    minHeapSize = "256m"
    maxHeapSize = "512m"
    maxParallelForks = Runtime.getRuntime().availableProcessors() / 2 ?: 1
}