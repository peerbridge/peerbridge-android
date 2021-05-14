plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.4.30"
    id("org.jetbrains.dokka") version "1.4.30"
    id("com.google.gms.google-services")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.peerbridge.android"
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
        freeCompilerArgs = freeCompilerArgs + listOf("-Xuse-experimental=kotlin.ExperimentalUnsignedTypes", "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi", "-Xinline-classes")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
        // Determines whether to generate a BuildConfig class.
        buildConfig = true
        // Determines whether to support View Binding.
        // Note that the viewBinding.enabled property is now deprecated.
        viewBinding = false
        // Determines whether to support Data Binding.
        // Note that the dataBinding.enabled property is now deprecated.
        dataBinding = false
        // Determines whether to generate binder classes for your AIDL files.
        aidl = false
        // Determines whether to support RenderScript.
        renderScript = false
        // Determines whether to support injecting custom variables into the module’s R class.
        resValues = false
        // Determines whether to support shader AOT compilation.
        shaders = false
    }

    composeOptions {
        kotlinCompilerVersion = "1.4.30"
        kotlinCompilerExtensionVersion = "1.0.0-beta01"
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }
}

dependencies {
    implementation(project(":secp256k1"))

    // Core - https://developer.android.com/kotlin/ktx#core
    implementation("androidx.core:core-ktx:1.3.2")

    // AppCombat
    implementation("androidx.appcompat:appcompat:1.2.0")

    // Lifecycle - https://developer.android.com/kotlin/ktx#lifecycle
    // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    // Compose
    implementation("androidx.compose.ui:ui:1.0.0-beta06")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta06")
    implementation("androidx.activity:activity-compose:1.3.0-alpha07")
    implementation("androidx.navigation:navigation-compose:1.0.0-alpha10")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha06")

    // Material Design
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.compose.material:material:1.0.0-beta06")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:27.1.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // ViewModel - https://developer.android.com/kotlin/ktx#viewmodel
    // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    // LiveData - https://developer.android.com/kotlin/ktx#livedata
    // implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")

    // WorkManager - https://developer.android.com/kotlin/ktx#workmanager
    // implementation("androidx.work:work-runtime-ktx:2.6.0-alpha02")

    // Kotlin Coroutines - https://developer.android.com/kotlin/coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    // Kotlinx-Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // JUnit - https://github.com/junit-team/junit4
    testImplementation("junit:junit:4.13.2")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-beta06")

    // Android Test
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
