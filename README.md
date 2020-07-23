<img src="logo/logo.svg" width="92px"/>

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)

Annotation based configuration library for any Java project.

## How to use

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


```xml
<build>
    <plugins>
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
    </plugins>
</build>

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

## Loading a file.
Loading and getting ready a file is just 1 method called FlManaged#load();
```java
public void createConfig() {
    final ExampleConfigFile file = new ExampleConfigFile();
    // You can't use the class before run the load method.
    file.load();
    file.getString("path.to.string");
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

## Supporters

[![Jetbrains](jetbrains/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
