buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Plugins.androidTools)
        classpath(Plugins.kotlin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

createTask("clean", Delete::class) {
    delete(rootProject.buildDir)
}