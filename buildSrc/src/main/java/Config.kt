object Versions {
    const val kotlinVersion = "1.3.50"
    const val compileSdkVersion = 29
    const val minSdkVersion = 19
    const val targetSdkVersion = 29
    const val versionCode = 1
    const val versionName = "1.0"
}

object Deps {
    // Android Jetpack
    private const val androidxViewVersion = "1.1.0"
    const val androidXcore = "androidx.core:core:$androidxViewVersion"
    const val appCompat = "androidx.appcompat:appcompat:$androidxViewVersion"
    const val material = "com.google.android.material:material:1.1.0-beta02"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-rc01"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"

    private const val navVersion = "2.1.0"
    const val navFragment = "androidx.navigation:navigation-fragment-ktx:$navVersion"
    const val navUi = "androidx.navigation:navigation-ui-ktx:$navVersion"
    const val navTesting =  "android.arch.navigation:navigation-testing:1.0.0-alpha08"

    private const val lifecycleVersion = "2.1.0"
    const val liveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
    const val liveDataReactive = "androidx.lifecycle:lifecycle-reactivestreams:$lifecycleVersion"

    // Kotlin
    const val kotlinStdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val kotlinAndroidKtx = "androidx.core:core-ktx:1.0.0-alpha1"

    // Network
    private const val retrofitVersion = "2.4.0"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    const val retrofitRxAdapter = "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"

    private const val moshiVersion = "1.6.0"
    const val moshi = "com.squareup.moshi:moshi:$moshiVersion"
    const val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"

    private const val okReplayVersion = "1.4.0"
    const val okReplay = "com.airbnb.okreplay:okreplay:$okReplayVersion"
    const val okReplayNoop = "com.airbnb.okreplay:noop:$okReplayVersion"
    const val okReplayTest = "com.airbnb.okreplay:junit:$okReplayVersion"

    //DB
    private const val roomVersion = "2.1.0"
    const val roomCommon = "androidx.room:room-common:$roomVersion"
    const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
    const val roomMigration = "androidx.room:room-migration:$roomVersion"
    const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
    const val roomRxJava = "androidx.room:room-rxjava2:$roomVersion"
    const val roomTesting = "androidx.room:room-testing:$roomVersion"

    // Utils
    const val rxJava = "io.reactivex.rxjava2:rxjava:2.1.14"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.2"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.2.0"
    const val picasso = "com.squareup.picasso:picasso:2.71828"
    const val java8time = "org.threeten:threetenbp:1.3.6"

    // Logging
    const val timber = "com.jakewharton.timber:timber:4.7.0"

    // DI
    private const val kodeinVersion = "5.1.0"
    const val kodeinJvm = "org.kodein.di:kodein-di-generic-jvm:$kodeinVersion"
    const val kodeinAndroid = "org.kodein.di:kodein-di-framework-android:$kodeinVersion"

    // Unit testing
    private const val kluentVersion = "1.38"
    const val kluent = "org.amshove.kluent:kluent:$kluentVersion"
    const val kluentAndroid = "org.amshove.kluent:kluent-android:$kluentVersion"
    const val junit4 = "junit:junit:4.12"

    private const val mockVersion = "1.9"
    const val mockk = "io.mockk:mockk:$mockVersion"
    const val mockkAndroid = "io.mockk:mockk-android:$mockVersion"

    // UI testing
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.2.0"
    const val androidTestRules = "androidx.test:rules:1.2.0"
}

object Plugins {
    const val androidTools = "com.android.tools.build:gradle:3.5.2"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val navPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0"
}
