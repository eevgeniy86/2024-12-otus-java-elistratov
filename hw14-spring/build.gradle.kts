plugins {
    id ("org.springframework.boot")
}

dependencies {
    implementation ("ch.qos.logback:logback-classic")

    implementation ("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    implementation ("org.flywaydb:flyway-core")

    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")

    implementation("com.google.code.gson:gson")
    implementation ("com.google.code.findbugs:jsr305")

    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui")

    //   implementation("org.springframework.boot:spring-boot-starter-test")

}