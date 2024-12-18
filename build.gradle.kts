import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("application")
    alias(libs.plugins.shadowPlugin)
    id("org.example.greeting") version "0.0.1"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.AZUL
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
    implementation(libs.slf4j)
    implementation(libs.kafkaClients)
    implementation(libs.apacheHttp)
    testImplementation(libs.kafkaStreamTestUtils)
    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.hamcrest)
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

tasks.register("show-tasks") {
    doLast {
        gradle.taskGraph.allTasks.forEach {
            print(it.name)
        }
    }
}
