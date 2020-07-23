<img src="logo/logo.svg" width="92px"/>

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)

Annotation based configuration library for any Java project.

## How to use

The main dependency(Required). You have to add at least 1 dependency which is `Optional Required`
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
Add Yaml support(Optional Required)
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
Add Json support(Optional Required)
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
To load and get ready a file method is FlManaged#load(). Here is the example:
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
Your class that extends FileManaged is your actual file interface. You can put sections and properties into it.
```java
@Config(
    name = "config",
    type = YamlFileType.class // JsonFileType.class
)
public final class ExampleConfigFile extends FileManaged {

    // You have to add this instance.
    @Instance
    public final ExampleSection aSection = new ExampleSectio();

    @Property
    public String test_property = "my test property";

    @Section("example-section")
    public final class ExampleSection extends ConfigSection {
    
        @Property
        public String test_property = "my test property";

    }

}
```
### onCreate()
The method runs before the file and the managed load. You can't use getString or other method in the case.
```java
public final class ExampleConfigFile extends FileManaged {

    @Override
    public void onCreate() {
        
    }

}
```
### onLoad()
The method runs after the file and the managed load. You can use getString and other method in the method.
```java
public final class ExampleConfigFile extends FileManaged {

    @Override
    public void onLoad() {
        
    }

}
```
## Extensions
### Bukkit
First of all you need `configs-bukkit` dependency.

Secondly, you have to run `BukkitExtensions.registerExtensions();` before the all things.

You can also use BukkitComparable/BukkitLinked/BukkitManaged/BukkitSection classes instead of the core classes.

Done.

## How to contribute?
Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

```
gradle shadowjar
```

## Supporters
[![Jetbrains](jetbrains/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
