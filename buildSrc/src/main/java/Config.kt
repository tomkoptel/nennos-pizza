object Versions {
    const val kotlinVersion = "1.2.41"
    const val compileSdkVersion = "android-P"
    const val minSdkVersion = 15
    const val targetSdkVersion = "P"
    const val versionCode = 1
    const val versionName = "1.0"
}

object Deps {
    private const val androidxViewVersion = "1.0.0-alpha3"

    // Android Jetpack
    const val androidXcore = "androidx.core:core:$androidxViewVersion"
    const val appCompat = "androidx.appcompat:appcompat:$androidxViewVersion"
    const val material = "com.google.android.material:material:$androidxViewVersion"
    const val recyclerView = "androidx.recyclerview:recyclerview:$androidxViewVersion"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.0"
    const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata:2.0.0-alpha1"

    // Kotlin
    const val kotlinStdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val kotlinCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5"

    // Network
    const val retrofit = "com.squareup.retrofit2:retrofit:2.4.0"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:2.4.0"
    const val retrofitCoroutinesAdapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-experimental-adapter:1.0.0"
    const val moshi = "com.squareup.moshi:moshi:1.6.0"
    const val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:1.6.0"

    // Logging
    const val timber = "com.jakewharton.timber:timber:4.7.0"

    // DI
    const val kodein = "org.kodein.di:kodein-di-generic-jvm:5.0.0"

    // Unit testing
    const val kluent = "org.amshove.kluent:kluent:1.38"
    const val mockk = "io.mockk:mockk:1.8.3"
    const val junit4 = "junit:junit:4.12"

    // UI testing
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.1.0-alpha3"
    const val androidTestRules = "androidx.test:rules:1.1.0-alpha3"
}

object Plugins {
    const val androidTools = "com.android.tools.build:gradle:3.2.0-alpha18"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
}