import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


dependencies {
    implementation("ch.qos.logback:logback-classic")
}




plugins {
    id("com.github.johnrengelman.shadow")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("gc")
        archiveVersion.set("0.1")
        archiveClassifier.set("")
        manifest {
            attributes(mapOf("Main-Class" to "ru.calculator.CalcDemo"))
        }
    }

    build {
        dependsOn(shadowJar)
    }
}