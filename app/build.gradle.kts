plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")

    kotlin("kapt")
    id ("kotlin-android")
    id("com.google.dagger.hilt.android")


}

android {
    namespace = "com.keru.pdfcreator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.keru.pdfcreator"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/*.kotlin_module"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.play.services.mlkit.document.scanner)
    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)

    val hiltVersion = "2.51"
    //noinspection UseTomlInstead
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    //noinspection UseTomlInstead
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    val roomVersion = "2.6.1"
    //noinspection UseTomlInstead
    implementation ("androidx.room:room-ktx:$roomVersion")
    //noinspection UseTomlInstead,KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$roomVersion")
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}