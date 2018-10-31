buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Plugins.androidTools)
        classpath(Plugins.kotlin)
        classpath(Plugins.navPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}