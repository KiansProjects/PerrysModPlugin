// Everything a Perrys mod build has in common. The mod only sets `modId` and `modVersion` in gradle.properties.
plugins {
    id("net.fabricmc.fabric-loom")
    `maven-publish`
    id("com.diffplug.spotless")
}

val modId = providers.gradleProperty("modId").get()
val modVersion = providers.gradleProperty("modVersion").get()
val minecraftVersion = providers.gradleProperty("minecraftVersion").getOrElse(PerrysMod.MINECRAFT_VERSION)
val loaderVersion = providers.gradleProperty("loaderVersion").getOrElse(PerrysMod.LOADER_VERSION)
val fabricVersion = providers.gradleProperty("fabricVersion").getOrElse(PerrysMod.FABRIC_VERSION)

version = modVersion
group = PerrysMod.GROUP

base {
    archivesName = project.name
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

fabricApi {
    configureDataGeneration {
        client = true
    }
    configureTests {
        createSourceSet = true
        this.modId = "$modId-gametest"
        enableGameTests = false
        enableClientGameTests = true
        eula = true
        clearRunDirectory = false
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    implementation("net.fabricmc:fabric-loader:$loaderVersion")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")

    testImplementation("net.fabricmc:fabric-loader-junit:$loaderVersion")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(PerrysMod.JAVA_VERSION)
    withSourcesJar()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release = PerrysMod.JAVA_VERSION
}

tasks.processResources {
    val props = mapOf(
        "version" to project.version,
        "minecraft_version" to minecraftVersion,
        "loader_version" to loaderVersion,
    )
    inputs.properties(props)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

tasks.jar {
    val archivesName = base.archivesName.get()
    from("LICENSE.txt") {
        rename { "LICENSE_$archivesName.txt" }
    }
}

spotless {
    java {
        target("src/*/java/**/*.java")
        eclipse().configXml(PerrysMod.resource("formatter.xml"))
        importOrder(*PerrysMod.importOrder())
        // Eclipse has no option for a space inside empty method/constructor bodies: `void x() {}` -> `void x() { }`
        replaceRegex("Space in empty method body", """(?m)^([^"\n]*\)(?: throws [\w.,\s]+)?) \{}$""", "$1 { }")
        removeUnusedImports()
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }

    repositories {
        // Add repositories to publish to here.
    }
}
