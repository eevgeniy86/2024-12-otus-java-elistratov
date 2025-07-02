dependencies {

    implementation("ch.qos.logback:logback-classic")
    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.zaxxer:HikariCP")

    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.assertj:assertj-core")
    testImplementation ("org.mockito:mockito-core")
    testImplementation ("org.mockito:mockito-junit-jupiter")

    testImplementation ("org.openjdk.jmh:jmh-core")
    testAnnotationProcessor ("org.openjdk.jmh:jmh-generator-annprocess")
}