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
    implementation("com.google.firebase:firebase-crashlytics-buildtools:2.8.1")
    kapt(libs.hilt.compiler)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // gson
    implementation("com.google.code.gson:gson:2.10.1")

    // kotlinx
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.serialization.converter)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)

    // chucker
    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")

    // okhttp3
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}