plugins {
    id ("org.jetbrains.intellij") version "0.7.2"
    java
    kotlin("jvm") version "1.5.0"
}

group = "com.arch"
version = "alpha-3.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testCompile("junit", "junit", "4.12")
}

intellij {
//    version = "202.7660.26"
//    alternativeIdePath = "/Applications/Android Studio.app"
    version = "2020.3.3"
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}