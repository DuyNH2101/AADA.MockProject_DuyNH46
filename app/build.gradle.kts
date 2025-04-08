plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.aadamockproject_duynh46"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aadamockproject_duynh46"
        minSdk = 26
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
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.preference)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.paging.common.android)
    implementation(libs.paging.rxjava2)
    implementation(libs.paging.runtime)
    implementation(libs.firebase.database)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.picasso)
    implementation (libs.rxjava)
    implementation (libs.rxandroid)
    implementation (libs.retrofit2.rxjava2.adapter)
    implementation (libs.logging.interceptor)
    implementation (libs.dagger)
    implementation (libs.room.runtime)
    implementation (libs.annotation)
    implementation(libs.hilt.android.v2511)
    implementation(platform(libs.firebase.bom))
    implementation (libs.paging.runtime)
    implementation (libs.paging.rxjava3)
    implementation(libs.room.paging)
    implementation (libs.hilt.work)
    implementation (libs.work.runtime.v290)
    implementation(libs.firebase.analytics)

    annotationProcessor (libs.room.compiler)
    annotationProcessor (libs.dagger.compiler)
    annotationProcessor(libs.hilt.android.compiler.v2511)
    annotationProcessor(libs.hilt.compiler)
}