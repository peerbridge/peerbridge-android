plugins {
    id("com.android.library")
    id("kotlin-android")
    id("org.jetbrains.dokka") version "1.4.32"
    `maven-publish`
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 23
        targetSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    kotlinOptions { jvmTarget = "1.8" }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    externalNativeBuild {
        cmake {
            path("src/main/cpp/CMakeLists.txt")
            version = "3.18.1"
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("default") {
                from(components["release"])
            }
        }
        repositories {
            maven {
                url = uri("$buildDir/repository")
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.3.0")

    // JUnit - https://github.com/junit-team/junit4
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
