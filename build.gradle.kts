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

plugins {
    id("java")
    kotlin("jvm") version "1.8.0"
}

group = "com.qatoolist"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

    // Mocking (Mockito)
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-testng:$mockitoTestngVersion")

    // Other potential dependencies
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("com.google.guava:guava:$guavaVersion")
}

tasks.test {
    useTestNG()

    // Example: Parameterizing your tests for different browsers
    systemProperty("browser", "chrome")
}
