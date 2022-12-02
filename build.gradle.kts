buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.agp)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.plugin)
        classpath(libs.kotlin.serializationPlugin)
    }
}

allprojects {
    afterEvaluate {
        apply("${rootDir}/gradle/common.gradle")
    }
}