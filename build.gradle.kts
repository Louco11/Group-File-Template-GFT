plugins {
    id("org.jetbrains.intellij.platform") version "2.10.5"
    java
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IU", "2025.3")
    }
}

intellijPlatform {
    pluginConfiguration {
        group = "com.arch"
        version = "5.6"

        ideaVersion {
            sinceBuild = "231"
            untilBuild = "253.*"
        }
    }
}
