import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
}

buildscript {
    val sqlDelightVersion = "1.5.5"
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}