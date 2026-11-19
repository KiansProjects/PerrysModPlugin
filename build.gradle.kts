plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "space.perrys"
version = "1.0.0"

repositories {
    maven("https://maven.fabricmc.net/") {
        name = "Fabric"
    }
    gradlePluginPortal()
}

dependencies {
    // The plugins the mods use; their versions are managed here for all mods.
    implementation("net.fabricmc.fabric-loom:net.fabricmc.fabric-loom.gradle.plugin:1.19.4")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:8.11.0")
}

publishing {
    repositories {
        // Maven repo for GitHub Pages, see .github/workflows/publish.yml.
        maven(layout.buildDirectory.dir("repo")) {
            name = "pages"
        }
    }
}
