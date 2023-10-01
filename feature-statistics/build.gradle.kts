plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.farmer.feature_statistics"
}

dependencies {

    // base
    implementation(libs.bundles.compose)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.viewmodel)

    // hilt
    implementation(libs.hilt.android)
    implementation(project(":data"))
    kapt(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation(libs.tehras.charts)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
}