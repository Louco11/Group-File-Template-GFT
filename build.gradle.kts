plugins {
    id ("org.jetbrains.intellij") version "1.14.1"
    java
    kotlin("jvm") version "1.8.0"
}

group = "com.arch"
version = "4.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

intellij {
    version.value("IC-2022.2")
//    version.set("212.5712.43")
//    type.set("IC")
//    plugins.set(listOf("android"))
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {}