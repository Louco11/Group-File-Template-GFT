plugins {
    id("org.jetbrains.intellij") version "0.7.2"
    java
    kotlin("jvm") version "1.4.10"
}

group = "com.louco"
version = "alfe_test_1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.2")
    testCompile("junit", "junit", "4.12")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.3.3"
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes("""
      Add change notes here.<br>
      <em>most HTML tags may be used</em>""")
}