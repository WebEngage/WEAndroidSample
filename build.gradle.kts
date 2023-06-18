buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.Gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KotlinGradle}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}