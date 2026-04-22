plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.farmgate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.farmgate"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.20.102.30:5097/\"")
        }

        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"http://10.20.102.30:5097/\"")
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

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Lifecycle Compose dependency
    implementation(libs.androidx.lifecycle.runtime.compose)
    // ViewModel Compose dependency
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Compose foundation dependency
    implementation(libs.androidx.compose.foundation)
    // Extended icons dependency
    implementation(libs.androidx.compose.material.icons.extended)
    // Navigation Compose dependency
    implementation(libs.androidx.navigation.compose)
    // DataStore dependency
    implementation(libs.androidx.datastore.preferences)
    // Splash screen dependency
    implementation(libs.androidx.core.splashscreen)
    // Coroutines Android dependency
    implementation(libs.kotlinx.coroutines.android)
    // Retrofit dependency
    implementation(libs.retrofit.core)
    // Retrofit Gson converter dependency
    implementation(libs.retrofit.gson)
    // OkHttp dependency
    implementation(libs.okhttp.core)
    // OkHttp logging dependency
    implementation(libs.okhttp.logging)
    // Gson dependency
    implementation(libs.gson.core)
    // Coil dependency
    implementation(libs.coil.compose)

}