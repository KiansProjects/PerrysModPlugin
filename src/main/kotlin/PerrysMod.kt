/**
 * Versions and settings shared by all mods. A mod can override a version in its `gradle.properties` (e.g.
 * `minecraftVersion=...`) while it is not updated yet.
 */
object PerrysMod {
    const val GROUP = "space.perrys"
    const val JAVA_VERSION = 25

    // Check these on https://fabricmc.net/develop
    const val MINECRAFT_VERSION = "26.3"
    const val LOADER_VERSION = "0.20.1"
    const val FABRIC_VERSION = "0.166.2+26.3"

    /** Text of a file in `src/main/resources/space/perrys/mod`. */
    fun resource(name: String): String =
        PerrysMod::class.java.getResource("/space/perrys/mod/$name")?.readText()
            ?: error("Missing resource $name")

    /** The import order from an Eclipse `.importorder` export (`0=java`, `1=javax`, ...). */
    fun importOrder(): Array<String> = resource("eclipse.importorder").lines()
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .map { it.substringBefore('=').trim().toInt() to it.substringAfter('=') }
        .sortedBy { it.first }
        .map { it.second }
        .toTypedArray()
}
