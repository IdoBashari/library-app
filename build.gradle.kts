plugins {
    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("maven-publish")
}

allprojects {
    group = "com.github.IdoBashari"
    version = "1.0.5"
}

tasks.register("cleanAll", Delete::class) {
    delete(rootProject.buildDir)
}