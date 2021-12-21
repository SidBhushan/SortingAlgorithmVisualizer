plugins {
    kotlin("js") version "1.6.0"
}

group = "me.sidbhushan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-RC3")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            distribution {
                directory = File("$projectDir/docs")
            }
        }
    }
}