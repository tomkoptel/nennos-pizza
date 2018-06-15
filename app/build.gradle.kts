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
           // used by Room, to test migrations
           (assets.srcDirs + files("$projectDir/schemas")).let { assets.setSrcDirs(it) }
       }
    }

    // Allow Room database to save the schem files
    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(Deps.kotlinStdlibJdk7)

    implementation(Deps.appCompat)
    implementation(Deps.material)
    implementation(Deps.recyclerView)
    implementation(Deps.constraintLayout)
    implementation(Deps.androidXcore)
    implementation(Deps.lifecycleLivedata)

    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshiConverter)
    implementation(Deps.retrofitRxAdapter)
    debugImplementation(Deps.okReplay)
    releaseImplementation(Deps.okReplayNoop)

    implementation(Deps.moshi)
    kapt(Deps.moshiCodeGen)

    implementation(Deps.roomCommon)
    implementation(Deps.roomMigration)
    implementation(Deps.roomRuntime)
    implementation(Deps.roomRxJava)
    kapt(Deps.roomCompiler)

    implementation(Deps.timber)

    implementation(Deps.kodeinJvm)
    implementation(Deps.kodeinAndroid)

    implementation(Deps.rxJava)
    implementation(Deps.rxAndroid)
    implementation(Deps.rxKotlin)

    testImplementation(Deps.junit4)
    testImplementation(Deps.kluent)
    testImplementation(Deps.mockk)
    testImplementation(Deps.okReplayTest)

    androidTestImplementation(Deps.espressoContrib)
    androidTestImplementation(Deps.androidTestRules)
    androidTestImplementation(Deps.kluentAndroid)
    androidTestImplementation(Deps.roomTesting)
}