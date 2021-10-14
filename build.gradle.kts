plugins {
    id ("org.jetbrains.intellij") version "1.2.0"
    java
    kotlin("jvm") version "1.5.0"
}

group = "com.arch"
version = "alpha-2.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testCompile("junit", "junit", "4.12")
}

intellij {
    version = "201.8743.12"
    alternativeIdePath = "/Applications/Android Studio.app"
//    version = "2020.3.2"
}

tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}