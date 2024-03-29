pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven (url="https://jitpack.io" )
    }
}

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("gradle/dependencies.toml"))
        }
    }
}

rootProject.name = "Olive"
include(":app")
include(":feature-cash-book")
include(":feature-statistics")
include(":network")
include(":data")
include(":feature-post")
include(":feature-settings")
include(":navigator")
