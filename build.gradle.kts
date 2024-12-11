import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath("com.gradleup.shadow:shadow-gradle-plugin:9.0.0-beta4")
    }
}

plugins {
    id("application")
    id("com.gradleup.shadow") version("9.0.0-beta4")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.AMAZON
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    version = "0.0.1"
}

application {
    mainClass = "io.confluent.developer.KafkaProducerApplication"
}

repositories {
    mavenCentral()
    maven {
        setUrl("https://packages.confluent.io/maven")
    }
}

dependencies {
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("org.apache.kafka:kafka-clients:3.9.0")
    testImplementation("org.apache.kafka:kafka-streams-test-utils:3.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    testImplementation("org.hamcrest:hamcrest:3.0")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        outputs.upToDateWhen { false }
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Class-Path"] = configurations.compileClasspath.get().joinToString(" ") { it.name }
        attributes["Main-Class"] = "io.confluent.developer.KafkaProducerApplication"
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveBaseName = "kafka-producer-application-standalone"
    archiveClassifier = ""
}
