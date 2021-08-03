plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.4.32"
    id("org.jetbrains.dokka") version "1.4.32"
    id("com.google.gms.google-services")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.peerbridge.android"
        minSdk = 24
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
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xinline-classes",
            "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes",
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xuse-experimental=kotlinx.serialization.ExperimentalSerializationApi",
        )
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
        kotlinCompilerVersion = "1.4.32"
        kotlinCompilerExtensionVersion = "1.0.0-beta07"
    }

    packagingOptions {
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }

    sourceSets.all {
        java.srcDir("src/$name/kotlin")
    }
}

dependencies {
    implementation(project(":secp256k1"))

    // Core - https://developer.android.com/kotlin/ktx#core
    implementation("androidx.core:core-ktx:1.5.0")

    // AppCombat
    implementation("androidx.appcompat:appcompat:1.3.0")

    // Lifecycle - https://developer.android.com/kotlin/ktx#lifecycle
    // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")

    // Compose
    implementation("androidx.compose.ui:ui:1.0.0-beta07")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta07")
    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha01")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha07")

    // Material Design
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.compose.material:material:1.0.0-beta07")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:27.1.0"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // Crypto - https://developer.android.com/topic/security/data -
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("androidx.security:security-identity-credential:1.0.0-alpha02")

    // ZXing Android Embedded - https://github.com/journeyapps/zxing-android-embedded
    implementation("com.journeyapps:zxing-android-embedded:4.2.0")

    // Android Room - https://developer.android.com/training/data-storage/room
    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

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

    // OKHTTP3 - https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // JUnit - https://github.com/junit-team/junit4
    testImplementation("junit:junit:4.13.2")

    // kotlinx-coroutines-test - https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-beta07")

    // Android Test
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
