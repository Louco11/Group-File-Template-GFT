plugins {
    id ("org.jetbrains.intellij") version "1.5.3"
    java
    kotlin("jvm") version "1.6.21"
}

group = "com.arch"
version = "alpha-3.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testCompile("junit", "junit", "4.12")
}

intellij {
    version.value("221.5080.210")

//    alternativeIdePath "/Applications/Android Studio.app"
//    version = "2020.3.2"
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {}