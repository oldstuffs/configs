<img src="logo/logo.svg" width="92px"/>

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)

Annotation based configuration library for any Java project.

## How to use

The main dependency(Required)
```xml
<dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-core</artifactId>
    <version>${version}</version>
</dependency>
```
```groovy
implementation("io.github.portlek:configs-core:${version}")
```
Add Yaml support(Optional)
```xml
<dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-yaml</artifactId>
    <version>${version}</version>
</dependency>
```
```groovy
implementation("io.github.portlek:configs-yaml:${version}")
```
Add Json support(Optional)
```xml
<dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-json</artifactId>
    <version>${version}</version>
</dependency>
```
```groovy
implementation("io.github.portlek:configs-json:${version}")
```
Add Bukkit extensions(Optional)
```xml
<dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-bukkit</artifactId>
    <version>${version}</version>
</dependency>
```
```groovy
implementation("io.github.portlek:configs-bukkit:${version}")
```
**Do not forget to relocate `io.github.portlek.configs` package into your package.**

Here is the examples for maven and gradle:
<details>
<summary>Maven</summary>

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
<details>
<summary>Gradle</summary>

```groovy
plugins {
    id "com.github.johnrengelman.shadow" version "6.0.0"
}

shadowJar {
    relocate('io.github.portlek.configs', "your.package.path.to.relocate")
    // other stuffs.
}
```
</details>

## Loading a file.
Loading and getting ready a file is just 1 method called FlManaged#load();
```java
final class CreatingConfigClass {
    void createConfig() {
        final ExampleConfigFile file = new ExampleConfigFile();
        // You can't use the class before run the load method.
        file.load();
        file.getString("path.to.string");
    }
}
```

## FileManaged
```java
public final class ExampleConfigFile extends FileManaged {

}
```

## Extensions

### Bukkit
First of all you need `configs-bukkit` dependencies.

Secondly, you have to run `BukkitExtensions.registerExtensions();` code before the all things.

You can also use BukkitComparable/BukkitLinked/BukkitManaged/BukkitSection classes instead of the core classes.

Done.

## How to contribute?
Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

`gradle shadowjar`

## Supporters

[![Jetbrains](jetbrains/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
