import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

		plugins {
			id("org.springframework.boot") version "3.1.5"
			id("io.spring.dependency-management") version "1.1.3"
			kotlin("jvm") version "1.8.22"
			kotlin("plugin.spring") version "1.8.22"
			kotlin("plugin.jpa") version "1.8.22"
			kotlin("kapt") version "1.8.22" // Corrected version
		}

group = "com.hu.bme.aut"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.2")
	implementation("org.springframework.boot:spring-boot-starter-web:3.3.2")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:3.3.2")
	implementation("org.springframework.data:spring-data-rest-hal-explorer:4.3.2")
	runtimeOnly("com.h2database:h2:2.2.224")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.3.1")
	implementation("com.google.code.gson:gson:2.10.1")
	testImplementation("io.mockk:mockk:1.13.12")

	compileOnly("org.projectlombok:lombok:1.18.34")
	annotationProcessor("org.projectlombok:lombok:1.18.34")

	testCompileOnly("org.projectlombok:lombok:1.18.34")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.34")

	// If you need a specific kapt dependency, add it here, e.g.:
	// kapt("com.google.dagger:dagger-compiler:2.44")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

kapt {
	keepJavacAnnotationProcessors = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}