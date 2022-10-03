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
//        classpath(deps.plugins.hilt.plugin) // todo need to add hilt
    }
}
