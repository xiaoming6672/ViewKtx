import com.android.build.gradle.internal.api.LibraryVariantOutputImpl

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-android")
    `maven-publish`
}


val libGropuId = config.versions.libGroupId.get()
val libArtifactId = config.versions.libArtifactId.get()
val libVersionCode = config.versions.libVersionCode.get()
val libVersionName = config.versions.libVersionName.get()


android {
    namespace = "com.zhang.view"
    compileSdk = config.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = config.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt") , "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    android.libraryVariants.all {
        outputs.all {
            if (this is LibraryVariantOutputImpl) {
                outputFileName = "${rootProject.name}-$name-$libVersionCode-$libVersionName.aar"
            }
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.library.utils)
    implementation(libs.library.adapter)
    implementation(libs.library.common)
    implementation(libs.lib.ktx)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = libGropuId
            artifactId = libArtifactId
            version = libVersionName

            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        maven {
            setUrl("https://packages.aliyun.com/maven/repository/2495277-release-LwpiHW")
            credentials {
                username = "66f6606383d62ab44fa5d54f"
                password = "s4BvVpAib[-I"
            }
        }
    }
}