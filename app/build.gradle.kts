import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.detekt)

}

android {
    namespace = "ru.tbank.petcare"
    compileSdk {
        version = release(36)
    }

    val localProperties = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { inputStream ->
            localProperties.load(inputStream)
        }
    }

    val baseUrlAnimals = localProperties.getProperty("BASE_URL_ANIMALS") ?: ""
    val apiKeyAnimals = localProperties.getProperty("API_KEY_ANIMALS") ?: ""
    val baseUrlCloudinary = localProperties.getProperty("BASE_URL_CLOUDINARY") ?: ""
    val cloudinaryName = localProperties.getProperty("CLOUDINARY_CLOUD_NAME") ?: ""
    val presetName = localProperties.getProperty("CLOUDINARY_PRESET_NAME") ?: ""
    val petsFolder = localProperties.getProperty("CLOUDINARY_PETS_FOLDER") ?: ""
    val usersFolder = localProperties.getProperty("CLOUDINARY_USERS_FOLDER") ?: ""

    defaultConfig {
        applicationId = "ru.tbank.petcare"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL_ANIMALS", "\"$baseUrlAnimals\"")
        buildConfigField("String", "API_KEY_ANIMALS", "\"$apiKeyAnimals\"")
        buildConfigField("String", "BASE_URL_CLOUDINARY", "\"$baseUrlCloudinary\"")
        buildConfigField("String", "CLOUDINARY_CLOUD_NAME", "\"$cloudinaryName\"")
        buildConfigField("String", "CLOUDINARY_PRESET_NAME", "\"$presetName\"")
        buildConfigField("String", "CLOUDINARY_PETS_FOLDER", "\"$petsFolder\"")
        buildConfigField("String", "CLOUDINARY_USERS_FOLDER", "\"$usersFolder\"")
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
        buildConfig = true
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))

    baseline = file("$rootDir/config/detekt/baseline.xml")
}

dependencies {
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.googleid)

    // Hilt
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)

    // Formating
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt)

    // Network
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.compose.shimmer)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}