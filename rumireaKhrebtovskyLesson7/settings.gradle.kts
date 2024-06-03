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

rootProject.name = "ru.mirea.Khrebtovsky.Lesson7"
include(":app")
include(":app:timeservice")
include(":app:httpurlconnection")
include(":app:firebaseauth")
