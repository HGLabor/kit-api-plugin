val minecraftVersion = "1.19.2"

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.3.8"
    id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2" // Generates plugin.yml
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.hglabor"
version = "1.0.0"
description = "basic kit-api implementation"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val kitApi = "de.hglabor:kit-api:1.0.0"

dependencies {
    paperDevBundle("${minecraftVersion}-R0.1-SNAPSHOT")
    implementation(kitApi)
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
    shadowJar {
    }
}

bukkit {
    main = "de.hglabor.kitapi.plugin.KitApiPlugin"
    apiVersion = "1.19"
    authors = listOf("copyandexecute")
}
