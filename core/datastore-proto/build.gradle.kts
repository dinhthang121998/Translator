import com.google.protobuf.gradle.id

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.protobuf") version "0.9.3"
}

android {
    namespace = "com.example.datastore_proto"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.1"
    }
    // Generates the java Protobuf-lite code for the Protobufs in this project. See
    // https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    // for more information.
    generateProtoTasks {
        // see https://github.com/google/protobuf-gradle-plugin/issues/518
        // see https://github.com/google/protobuf-gradle-plugin/issues/491
        // all() here because of android multi-variant
        all().forEach { task ->
            // this only works on version 3.8+ that has buildins for javalite / kotlin lite
            // with previous version the java build in is to be removed and a new plugin
            // need to be declared
            task.builtins {
                id("java") { // id is imported above
                    option("lite")
                }
                id("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation("com.google.protobuf:protobuf-javalite:3.25.5") // Ensure this is consistent
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.25.5")
}
