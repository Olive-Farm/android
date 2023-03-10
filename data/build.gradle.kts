plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.farmer.data"
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

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // kotlinx
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serialization.converter)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
}