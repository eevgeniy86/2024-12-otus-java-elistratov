import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import name.remal.gradle_plugins.sonarlint.SonarLintExtension


plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("com.diffplug.spotless") apply false
    id("fr.brouillard.oss.gradle.jgitver")
    id("name.remal.sonarlint") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(21)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

allprojects {
    group = "ru.otus"
    repositories {
        mavenLocal()
        mavenCentral()
    }

    val guava: String by project
    val testcontainersBom: String by project
    val protobufBom: String by project
    val jmh: String by project
    val jetty: String by project
    val reflections: String by project
    val freemarker: String by project
    val bootstrap: String by project
    val springDocOpenapiUi: String by project
    val jsr305: String by project


    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.openjdk.jmh:jmh-core:$jmh")
            dependency("org.openjdk.jmh:jmh-generator-annprocess:$jmh")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jetty")
            dependency("org.eclipse.jetty:jetty-server:$jetty")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-webapp:$jetty")
            dependency("org.eclipse.jetty:jetty-security:$jetty")
            dependency("org.eclipse.jetty:jetty-http:$jetty")
            dependency("org.eclipse.jetty:jetty-io:$jetty")
            dependency("org.eclipse.jetty:jetty-util:$jetty")
            dependency("org.freemarker:freemarker:$freemarker")

            dependency("org.reflections:reflections:$reflections")

            dependency("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocOpenapiUi")
            dependency("com.google.code.findbugs:jsr305:$jsr305")


        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
        }
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()

            force("javax.servlet:servlet-api:2.5")
            force("commons-logging:commons-logging:1.1.1")
            force("commons-lang:commons-lang:2.5")
            force("org.codehaus.jackson:jackson-core-asl:1.8.8")
            force("org.codehaus.jackson:jackson-mapper-asl:1.8.8")
            force("commons-io:commons-io:2.16.1")
            force("org.eclipse.jgit:org.eclipse.jgit:6.9.0.202403050737-r")
            force("org.apache.commons:commons-compress:1.26.1")
            force("com.google.errorprone:error_prone_annotations:2.36.0")
            force("org.jetbrains:annotations:19.0.0")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-parameters", "-Xlint:all,-serial,-processing"))

        dependsOn("spotlessApply")
    }

    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat("2.39.0")
        }
    }

    plugins.apply(fr.brouillard.oss.gradle.plugins.JGitverPlugin::class.java)
    extensions.configure<fr.brouillard.oss.gradle.plugins.JGitverPluginExtension> {
        strategy("PATTERN")
        nonQualifierBranches("main,master")
        tagVersionPattern("\${v}\${<meta.DIRTY_TEXT}")
        versionPattern(
            "\${v}\${<meta.COMMIT_DISTANCE}\${<meta.GIT_SHA1_8}" +
                    "\${<meta.QUALIFIED_BRANCH_NAME}\${<meta.DIRTY_TEXT}-SNAPSHOT"
        )
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    configure<SonarLintExtension> {
        nodeJs {
            detectNodeJs = false
            logNodeJsNotFound = false
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}

tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}