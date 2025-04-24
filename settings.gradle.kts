pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Translator"
include(":app")
include(":core")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":feature:home")
include(":core:mlkit")
include(":core:voice")
include(":core:common")
include(":feature:translate-image")
include(":feature:translate-camerax")
include(":core:cipher")
include(":feature:favored")
include(":feature:settings")
include(":feature:feedback")
include(":feature:pronunciation-speed")
