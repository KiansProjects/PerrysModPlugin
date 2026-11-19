plugins {
    `kotlin-dsl`
}

repositories {
    maven("https://maven.fabricmc.net/") {
        name = "Fabric"
    }
    gradlePluginPortal()
}

dependencies {
    // The plugins the mods use; their versions are managed here for all mods.
    implementation("net.fabricmc.fabric-loom:net.fabricmc.fabric-loom.gradle.plugin:1.19-SNAPSHOT")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.11.0")
}
