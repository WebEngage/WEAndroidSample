plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    namespace = AppConfig.nameSpace
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = "com.webengage.sample"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //app libs
    implementation(platform(Dependencies.FirebaseBOM))
    implementation(Dependencies.appLibraries)
    //test libs
    testImplementation(Dependencies.testLibraries)
    androidTestImplementation(Dependencies.androidTestLibraries)
}