buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.agp)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.hilt.plugin)
    }
}

allprojects {
    afterEvaluate {
        apply("${rootDir}/gradle/common.gradle")
    }
}