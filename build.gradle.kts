// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0-alpha01")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")

        // Google Services plugin
        classpath("com.google.gms:google-services:4.3.8")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            // Set JVM target to 1.8
            jvmTarget = "1.8"
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
