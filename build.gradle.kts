/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 */



plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.4.10" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.5.10"
    // Apply the application plugin to add support for building a CLI application.
    application
    jacoco
    maven


}


repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

val kotlinVersion="1.4.10"
val ktorVersion = "1.3.2"
val koinVersion = "2.1.6"
val logbackVersion = "1.2.3"
val exposedVersion = "0.24.1"
val rabbitMqClientVersion = "5.9.0"

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION)) // or "stdlib-jdk8"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2") // JVM dependency


    // Use coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    //Asynchronous Web Framework
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-json:${ktorVersion}")
    implementation("io.ktor:ktor-client-jackson:${ktorVersion}")
    implementation("io.ktor:ktor-server-netty:${ktorVersion}")
    implementation("io.ktor:ktor-jackson:${ktorVersion}")
    //implementation("io.ktor:ktor-gson:${ktorVersion}")

    //ORM
    implementation("org.jetbrains.exposed", "exposed-core", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-dao", exposedVersion)
    implementation("org.jetbrains.exposed", "exposed-jdbc", exposedVersion)


    //CQS
    implementation(group = "org.reflections",name ="reflections",version = "0.9.12" )

    //DI
    implementation("org.koin:koin-ktor:${koinVersion}")
    implementation("org.koin:koin-core:${koinVersion}")

    //Log
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    //DI test
    testImplementation("org.koin:koin-test:${koinVersion}")

    //Asynchronous Web Framework
    testImplementation("io.ktor:ktor-server-test-host:${ktorVersion}")
    implementation(kotlin("script-runtime"))

    //RabbitMQ
    implementation("com.rabbitmq:amqp-client:${rabbitMqClientVersion}")



}

application {
    // Define the main class for the application.
    applicationDefaultJvmArgs = listOf("-ea")
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

task("writeNewPom") {
    doLast {
        maven.pom {
            withGroovyBuilder {
                "project" {
                    setProperty("groupId", "com.abaddon83")
                    setProperty("inceptionYear", "2020")
                    setProperty("version","0.0.1")
                    "licenses" {
                        "license" {
                            setProperty("name", "The Apache Software License, Version 2.0")
                            setProperty("url", "http://www.apache.org/licenses/LICENSE-2.0.txt")
                            setProperty("distribution", "repo")
                        }
                    }
                }
            }
        //}.writeTo("$buildDir/pom.xml")
        }.writeTo("./pom.xml")
    }
}

