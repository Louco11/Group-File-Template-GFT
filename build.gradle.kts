plugins {
    id ("org.jetbrains.intellij") version "1.17.2"
    java
    kotlin("jvm") version "1.9.23"
}

group = "com.arch"
version = "5.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

}

intellij {
    version.value("IC-2023.3.5")
//    version.set("232.10300.40.2321.11567975")
//    type.set("AI")
//    plugins.set(listOf("android"))
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {}