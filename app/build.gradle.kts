import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    application
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("checkstyle")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.25.3")

    implementation("io.javalin:javalin:6.1.2")
    implementation("io.javalin:javalin-bundle:6.1.2")
    implementation("org.slf4j:slf4j-simple:2.0.12")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.projectlombok:lombok:1.18.30")
    implementation("com.h2database:h2:2.2.224")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}
