plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.farmer.feature_home"
}

dependencies {
    // base
    implementation(libs.bundles.compose)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.viewmodel)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization)
    implementation(libs.retrofit)
    implementation(libs.kotlin.serialization.converter)
    implementation(libs.okhttp.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)

    implementation("androidx.compose.foundation:foundation:1.4.0-rc01")

    implementation(project(":network"))
    implementation(project(":data"))
}