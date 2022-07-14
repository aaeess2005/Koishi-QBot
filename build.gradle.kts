plugins {
    val kotlinVersion = "1.5.30"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

dependencies {
    //QQ Backend
    api("net.mamoe:mirai-core:2.11.1")

    //Logging
    api("org.slf4j:slf4j-api:1.7.36")
    api("org.slf4j:slf4j-simple:1.7.36")

    //HTTP
    api("org.apache.httpcomponents:httpclient:4.5.13")

    //JSON
    api("com.google.code.gson:gson:2.9.0")

    //QR Code
    api("com.google.zxing:core:3.5.0")

    //JOPT simple
    api("net.sf.jopt-simple:jopt-simple:5.0.4")
}

group = "io.github.aaeess2005"
version = "1.0"

repositories {
    maven("https://maven.aliyun.com/repository/public")
    mavenCentral()
}