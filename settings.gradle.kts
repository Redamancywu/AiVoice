pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven { url=
            uri("https://www.jitpack.io")}
        maven { url=uri("https://jitpack.io")  }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url=
            uri("https://www.jitpack.io")}
        maven { url=uri("https://jitpack.io")  }
        mavenCentral()
    }
}

rootProject.name = "AiVioceApp"
include(":app")
include(":lib_base")
include(":lib_network")
include(":lib_voice")
include(":module_app_manager")
include(":module_constellation")
include(":module_developer")
include(":module_joke")
include(":module_map")
include(":setting")
include(":module_voice_setting")
include(":module_weather")
rootProject.buildFileName = "build.gradle.kts"

