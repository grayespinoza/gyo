plugins {
  `maven-publish`
  id("fabric-loom")
}

version = "${property("mod.version")}+${stonecutter.current.version}"

group = property("maven.group") as String

base.archivesName = property("mod.id") as String

repositories {
  fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
    forRepository { maven(url) { name = alias } }
    filter { groups.forEach(::includeGroup) }
  }
  maven { url = uri("https://maven.terraformersmc.com/releases/") }
}

dependencies {
  fun fapi(vararg modules: String) {
    for (it in modules) modImplementation(
      fabricApi.module(it, property("fabric.version") as String)
    )
  }
  minecraft("com.mojang:minecraft:${stonecutter.current.version}")
  mappings("net.fabricmc:yarn:${property("yarn.mappings")}:v2")
  modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric.version")}")
  modImplementation("net.fabricmc:fabric-loader:${property("fabric.loader.version")}")
  modApi("com.terraformersmc:modmenu:${property("mod.menu.version")}")
  fapi("fabric-lifecycle-events-v1")
}

loom {
  decompilerOptions.named("vineflower") { options.put("mark-corresponding-synthetics", "1") }

  runConfigs.all {
    ideConfigGenerated(true)
    vmArgs("-Dmixin.debug.export=true")
    runDir = "../../run"
  }
}

java {
  withSourcesJar()
  val requiresJava21: Boolean = stonecutter.eval(stonecutter.current.version, ">=1.20.6")
  val javaVersion: JavaVersion =
    if (requiresJava21) JavaVersion.VERSION_21 else JavaVersion.VERSION_17
  targetCompatibility = javaVersion
  sourceCompatibility = javaVersion
}

tasks {
  processResources {
    inputs.property("java_version", project.property("java.version"))
    inputs.property("license", project.property("license"))
    inputs.property("mod_description", project.property("mod.description"))
    inputs.property("mod_version", project.property("mod.version"))
    inputs.property("minecraft_version", project.property("minecraft.version"))
    inputs.property("fabric_loader_version", project.property("fabric.loader.version"))

    val props =
      mapOf(
        "java_version" to project.property("java.version"),
        "license" to project.property("license"),
        "mod_description" to project.property("mod.description"),
        "mod_version" to project.property("mod.version"),
        "minecraft_version" to project.property("minecraft.version"),
        "fabric_loader_version" to project.property("fabric.loader.version"),
      )

    filesMatching("fabric.mod.json") { expand(props) }
  }

  register<Copy>("buildAndCollect") {
    group = "build"
    from(remapJar.map { it.archiveFile }, remapSourcesJar.map { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    dependsOn("build")
  }
}
