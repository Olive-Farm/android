plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.farmer.feature_settings"
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

    implementation(project(":network"))
    implementation(project(":navigator"))
    implementation(project(":data"))

    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
}