plugins {
    `java-library`
}

group = "com.sunmoon"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

// api() for anything that appears in the kernel's own public signatures —
// a service composing listeners and endpoints needs those types on its
// compile classpath without re-declaring them.
dependencies {
    api("io.netty:netty-codec-http:4.1.114.Final")
    api("io.netty:netty-handler:4.1.114.Final")
    api("io.netty:netty-transport:4.1.114.Final")
    api("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    api("org.mybatis:mybatis:3.5.16")
    // RequestValidation validates DTOs the caller declares, so the caller
    // needs the constraint annotations to declare them with.
    api("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    // A library picks the logging facade, never the binding — each service
    // declares its own logback (or whatever else) as runtimeOnly.
    api("org.slf4j:slf4j-api:2.0.16")

    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.flywaydb:flyway-core:10.20.1")
    implementation("org.flywaydb:flyway-database-postgresql:10.20.1")
    implementation("io.micrometer:micrometer-registry-prometheus:1.13.6")
    implementation("io.opentelemetry:opentelemetry-api:1.42.1")
    implementation("io.opentelemetry:opentelemetry-sdk:1.42.1")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.42.1")
    implementation("org.glassfish:jakarta.el:4.0.2")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("ch.qos.logback:logback-classic:1.5.8")
}

tasks.test {
    useJUnitPlatform()
}
