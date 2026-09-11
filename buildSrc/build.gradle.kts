plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val gatlingPluginVersion = "3.15.1.3"
val pitestPluginVersion = "1.19.0"

dependencies {
    // needed so skc.gatling can apply the plugin and reference GatlingRunTask
    implementation("io.gatling.gradle:io.gatling.gradle.gradle.plugin:$gatlingPluginVersion")
    implementation("info.solidsoft.pitest:info.solidsoft.pitest.gradle.plugin:$pitestPluginVersion")
}
