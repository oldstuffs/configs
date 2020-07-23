<img src="logo/logo.svg" width="92px"/>

Annotation based configuration library for any Java project.

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)

## Setup

<details>
<summary>Gradle</summary>

```gradle
plugins {
    id "com.github.johnrengelman.shadow" version "5.2.0"
}

repositories {
    mavenCentral()
}

dependencies {
    // The main dependency(Required)
    implementation("io.github.portlek:configs-core:${version}")
    // Add Yaml support(Optional)
    implementation("io.github.portlek:configs-yaml:${version}")
    // Add Json support(Optional)
    implementation("io.github.portlek:configs-json:${version}")
    // Add bukkit extensions(Optional)
    implementation("io.github.portlek:configs-bukkit:${version}")
}

shadowJar {
    relocate('io.github.portlek.configs', "your.package.path.to.relocate")
    // other stuffs.
}
```
</details>

<details>
<summary>Maven</summary>

```xml
<dependencies>
    <!-- The main dependency(Required) -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-core</artifactId>
      <version>${version}</version>
    </dependency>
    <!-- Add Yaml support(Optional) -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-yaml</artifactId>
      <version>${version}</version>
    </dependency>
    <!-- Add Json support(Optional) -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-json</artifactId>
      <version>${version}</version>
    </dependency>
    <!-- Add Bukkit extensions(Optional) -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-bukkit</artifactId>
      <version>${version}</version>
    </dependency>
</dependencies>
```

Also you have to make relocation for the library with;

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.4</version>
    <configuration>
        <!-- Other settings -->
        <relocations>
            <relocation>
                <pattern>io.github.portlek.configs</pattern>
                <!-- Replace this -->
                <shadedPattern>your.package.path.to.relocate</shadedPattern>
            </relocation>
        </relocations>
    </configuration>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>shade</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
</details>

## Supporters

[![Jetbrains](jetbrains/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
