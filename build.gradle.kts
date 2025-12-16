import io.micronaut.gradle.docker.MicronautDockerfile

//***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME *****
//
//- This build.gradle.kts file has been configured to include all known mechanisms for configuring a Micronaut mainClass.
//- There are currently 5 known common mechanisms.
//- Identify the mechanisms below by locating comments with "Micronaut mainClass Mechanism X"
//- Each mechanism should identify a different mainclass value, and an associated class file should exist.
//
//***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME ***** READ ME *****

plugins {
    id("io.micronaut.application") version "4.6.1"
    id("com.gradleup.shadow") version "9.2.2"
}

version = "0.1"
group = "micronaut.documentation.search"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut.opensearch:micronaut-opensearch-httpclient5")

    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    implementation("io.micronaut.views:micronaut-views-thymeleaf")
    implementation("io.micronaut.opensearch:micronaut-opensearch")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    // HTML -> Markdown converter
    implementation("com.vladsch.flexmark:flexmark-html2md-converter:0.64.8")
    implementation("io.micronaut.mcp:micronaut-mcp-server-java-sdk")
    testImplementation("io.micronaut.mcp:micronaut-mcp-client-java-sdk")
    annotationProcessor("io.micronaut.jsonschema:micronaut-json-schema-processor")
    implementation("io.micronaut.jsonschema:micronaut-json-schema-annotations")
    implementation("io.micronaut.cache:micronaut-cache-caffeine")
    compileOnly("io.micronaut:micronaut-http-client")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.micronaut:micronaut-http-client")
}
tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.compilerArgs.add("-Amicronaut.jsonschema.baseUri=https://micronaut.fun/schemas")
}
java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}


graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("micronaut.documentation.search.*")
    }
}

// ----------------------------------------------------------------------
// Micronaut mainClass Mechanism 1: Application plugin - modern property `mainClass`
// ----------------------------------------------------------------------
// This is the “new” API Gradle documents for the Application plugin.
application {
    mainClass.set("fun.micronaut.GradleKotlin1")

    // ------------------------------------------------------------------
    // Micronaut mainClass Mechanism 2: Application plugin - legacy alias `mainClassName`
    // ------------------------------------------------------------------
    @Suppress("DEPRECATION")
    mainClassName = "fun.micronaut.GradleKotlin2"
}

// ----------------------------------------------------------------------
// Micronaut mainClass Mechanism 3: Override the `run` task's main class (JavaExec)
// ----------------------------------------------------------------------
// This affects only `./gradlew run` (not the JARs).
tasks.named<JavaExec>("run") {
    mainClass.set("fun.micronaut.GradleKotlin3")
}

// ----------------------------------------------------------------------
// Micronaut mainClass Mechanism 4: Standard JAR manifest `Main-Class`
// ----------------------------------------------------------------------
// This affects `java -jar build/libs/<name>.jar` (the *plain* jar).
tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "fun.micronaut.GradleKotlin4"
    }
}

// ----------------------------------------------------------------------
// Micronaut mainClass Mechanism 5: Shadow JAR manifest `Main-Class`
// ----------------------------------------------------------------------
// This affects `java -jar build/libs/<name>-all.jar` (the fat JAR).
tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("all")

    manifest {
        attributes["Main-Class"] = "fun.micronaut.GradleKotlin5"
    }
}

tasks.named<MicronautDockerfile>("dockerfile") {
    baseImage.set("eclipse-temurin:25-jre")
}
tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}
