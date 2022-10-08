//plugins {
//    alias(libs.plugins.android.application)
//    alias(deps.plugins.android.library)
//    alias(deps.plugins.kotlin.android)
//}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // todo change to libs(or deps)
//        classpath(libs.libraries.agp)
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
    }
}

allprojects {
    afterEvaluate {
        apply("${rootDir}/gradle/common.gradle")
    }
}