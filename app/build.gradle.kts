import com.farmer.olive.Configuration

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = Configuration.applicationName
    compileSdk = Configuration.compileSdk

    defaultConfig {
        applicationId = Configuration.applicationName
        minSdk = Configuration.minSdk
        targetSdk = Configuration.targetSdk
        versionCode = Configuration.versionCode
        versionName = Configuration.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    // Charts
    //implementation ("com.diogobernardino:williamchart:3.10.1")

    // Tooltips
    // implementation ("com.diogobernardino.williamchart:tooltip-slider:3.10.1")
//    implementation ("com.diogobernardino.williamchart:tooltip-points:3.10.1")

    implementation ("androidx.core:core-splashscreen:1.0.0-alpha01")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0") // 그래프 라이브러리 추가
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation("androidx.compose.material:material-icons-extended-android:1.5.4")
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

    // base
    implementation(libs.bundles.compose)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle)
    implementation(libs.lifecycle.viewmodel)

    // hilt
    implementation(libs.hilt.android)
    implementation(project(mapOf("path" to ":data")))
    kapt(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)

    implementation(libs.kotlin.serialization)
    implementation(libs.retrofit)
    implementation(libs.kotlin.serialization.converter)

    // other modules
    implementation(project(":feature-cash-book"))
    implementation(project(":feature-statistics"))
    implementation(project(":feature-post"))
    implementation(project(":feature-settings"))
    implementation(project(":navigator"))
    implementation(project(":data"))
}
