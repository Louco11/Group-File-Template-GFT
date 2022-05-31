plugins {
    id ("org.jetbrains.intellij") version "1.6.0"
    java
    kotlin("jvm") version "1.6.21"
}

group = "com.arch"
version = "alpha-3.6"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testCompile("junit", "junit", "4.12")
}

intellij {
    version.value("212.5712.43")
//    version.set("212.5712.43")
//    type.set("IC")
//    plugins.set(listOf("android"))
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {}