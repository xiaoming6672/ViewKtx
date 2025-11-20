pluginManagement {
    repositories {
        /** 阿里云效 私有maven */
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/apache-snapshots")

        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        /** 阿里云效 私有maven */
        maven(url = "https://maven.aliyun.com/repository/public")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/apache-snapshots")

        maven {
            setUrl("https://packages.aliyun.com/maven/repository/2495277-release-LwpiHW")
            credentials {
                username = "66f6606383d62ab44fa5d54f"
                password = "s4BvVpAib[-I"
            }
        }

        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2/")
    }
    versionCatalogs {
        create("config") {
            from(files("gradle/config.versions.toml"))
        }
    }
}

rootProject.name = "ViewKtx"
include(":app")
include(":lib")
