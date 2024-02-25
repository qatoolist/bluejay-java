// Define versions as variables
val seleniumVersion = "4.17.0"
val webdriverManagerVersion = "5.6.3"
val testngVersion = "7.9.0"
val extentReportsVersion = "5.1.1"
val poiVersion = "5.2.5"
val log4jVersion = "2.22.1"
val mockitoVersion = "5.10.0"
val mockitoTestngVersion = "0.5.2"
val commonsIoVersion = "2.15.1"
val guavaVersion = "33.0.0-jre"
val sonarVersion = "10.6.0.2114"

plugins {
    id("java")
    id("org.sonarqube") version "4.4.1.3373"
    kotlin("jvm") version "1.8.0"
}

group = "com.qatoolist"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.sonarsource.api.plugin:sonar-plugin-api:$sonarVersion")

    // Core Selenium & WebDriver
    implementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
    implementation("io.github.bonigarcia:webdrivermanager:$webdriverManagerVersion")

    // TestNG
    implementation("org.testng:testng:$testngVersion")

    // Reporting (ExtentReports)
    implementation("com.aventstack:extentreports:$extentReportsVersion")

    // Data Handling (Apache POI for Excel)
    implementation("org.apache.poi:poi:$poiVersion")
    implementation("org.apache.poi:poi-ooxml:$poiVersion")

    // Logging (Log4j2)
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")

    // Other potential dependencies
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("com.google.guava:guava:$guavaVersion")

    // Mocking (Mockito)
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-testng:$mockitoTestngVersion")

}

sonar {
    properties {
        property("sonar.projectKey", "bluejay")
        property("sonar.projectName", "bluejay")
        property("sonar.host.url", "http://localhost")
    }
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"

    val javaSrc = sourceSets.main.get().allJava
    source = javaSrc

    (options as StandardJavadocDocletOptions).links("https://docs.oracle.com/javase/21/docs/api/index.html")
}

tasks.test {
    useTestNG()

    // Example: Parameterizing your tests for different browsers
    systemProperty("browser", "chrome")
}
