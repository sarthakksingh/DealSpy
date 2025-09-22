import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt.android)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}
android {
    namespace = "com.example.dealspy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.dealspy"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            }
        }

        buildConfigField(
            "String",
            "GOOGLE_WEB_CLIENT_ID",
            "\"${properties.getProperty("WEB_CLIENT_ID", "")}\""
        )
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY", "")}\"")
        buildConfigField("String","BASE_URL","\"${properties.getProperty("BASE_URL", "")}\"")
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
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
    implementation("com.airbnb.android:lottie-compose:6.3.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.gson)
    implementation(libs.coil.compose.v240)
    ksp(libs.google.dagger.hilt.compiler)
    implementation(libs.google.dagger.hilt.android)
    implementation(libs.androidx.material)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose.v275)
    implementation(libs.coil.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.3.1")

    // Optional: Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Optional: Firebase UI (for pre-built auth UI)
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-messaging")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}