plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.TravelPlanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.TravelPlanner"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "0.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        room {
            schemaDirectory("$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.firebase.auth)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.room.runtime.android)
    // Room dependencies
    val room_version = "2.6.1"
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:$room_version")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")

    // Coil para carga de imágenes en Compose
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Other dependencies
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.compiler)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)

    // Material Design & Navigation
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.preference.ktx)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Other dependencies
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.room.common)
}

kapt{
    correctErrorTypes = true
}
