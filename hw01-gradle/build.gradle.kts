plugins {
    id("java")
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ru.otus"

repositories {
    mavenCentral()
}



dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(libs.guava)

}

tasks.jar {
    manifest.attributes["Main-Class"] = "ru.otus.HelloOtus"
}



//tasks.test {
//    useJUnitPlatform()
//}