import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.android.extensions")
}

apply {
    from(rootProject.file("gradle/config-kotlin-sources.gradle"))
}

android {
    compileSdkVersion(Versions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)

        applicationId = "com.sample.nennos"
        versionCode  = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
       getByName("main") {
           (java.srcDirs + "src/main/kotlin").let { java.setSrcDirs(it) }
       }
       getByName("test") {
           (java.srcDirs + "src/test/kotlin").let { java.setSrcDirs(it) }
       }
       getByName("androidTest") {
           (java.srcDirs + "src/androidTest/kotlin").let { java.setSrcDirs(it) }
       }
    }
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

dependencies {
    implementation(Deps.kotlinStdlibJdk7)
    implementation(Deps.kotlinCoroutines)

    implementation(Deps.appCompat)
    implementation(Deps.material)
    implementation(Deps.recyclerView)
    implementation(Deps.constraintLayout)
    implementation(Deps.androidXcore)
    implementation(Deps.lifecycleLivedata)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshiConverter)
    implementation(Deps.retrofitCoroutinesAdapter)

    implementation(Deps.moshi)
    kapt(Deps.moshiCodeGen)

    implementation(Deps.timber)

    implementation(Deps.kodein)

    testImplementation(Deps.junit4)
    testImplementation(Deps.kluent)
    testImplementation(Deps.mockk)

    androidTestImplementation(Deps.espressoContrib)
    androidTestImplementation(Deps.androidTestRules)
}