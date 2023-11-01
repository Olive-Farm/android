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

    // For Jetpack Compose.
    implementation("com.patrykandpatrick.vico:compose:1.12.0")

    // For `compose`. Creates a `ChartStyle` based on an M2 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m2:1.12.0")

    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation("com.patrykandpatrick.vico:compose-m3:1.12.0")

    // Houses the core logic for charts and other elements. Included in all other modules.
    implementation("com.patrykandpatrick.vico:core:1.12.0")

    // For the view system.
    implementation("com.patrykandpatrick.vico:views:1.12.0")

    // hilt
    implementation(libs.hilt.android)
    implementation(project(":data"))
    implementation(libs.foundation.layout.android)
    kapt(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation(libs.tehras.charts)
    implementation(libs.kotlin.datetime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
}