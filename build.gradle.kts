/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // Apply the application plugin to add support for building a CLI application.
    application
    jacoco
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

val kotlinVersion="1.3.7"
val ktorVersion = "1.3.2"
val koinVersion = "2.1.6"
val logbackVersion = "1.2.3"

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    //Asynchronous Web Framework
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-json:${ktorVersion}")
    implementation("io.ktor:ktor-client-jackson:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("io.ktor:ktor-jackson:${ktorVersion}")

    //CQS
    implementation(group = "org.reflections",name ="reflections",version = "0.9.12" )

    //DI
    implementation("org.koin:koin-ktor:${koinVersion}")
    implementation("org.koin:koin-core:${koinVersion}")

    //Log
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${kotlinVersion}")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    //DI test
    testImplementation("org.koin:koin-test:${koinVersion}")

    //Asynchronous Web Framework
    testImplementation("io.ktor:ktor-server-test-host:${ktorVersion}")
    implementation(kotlin("script-runtime"))

}

application {
    // Define the main class for the application.
    mainClassName = "com.abaddon83.AppKt"
}


jacoco {
    toolVersion = "0.8.5"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = true
        html.isEnabled = true
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)

}

