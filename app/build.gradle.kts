plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.translator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.translator"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:home"))
    implementation(project(":feature:favored"))

    implementation(libs.core.ktx)
    implementation(libs.android.appcompat)
    implementation(libs.android.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Detekt
    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.test.android)
}
