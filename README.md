# Configs

[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![DevOps By Rultor.com](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)
![Java CI with Maven](https://github.com/portlek/configs/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master)
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
    // For the all project type
    implementation("io.github.portlek:configs-core:1.2")
    // If you don't make an application which has not the snakeyaml add the implementation.
    implementation("org.yaml:snakeyaml:1.26")

    // For the bukkit projects
    // You don't have to add snakeyaml implementation into bukkit plugins,
    // it because Bukkit has already snakeyaml implementation in it.
    implementation("io.github.portlek:configs-bukkit:1.2")
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
    <!-- For the all project type -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-core</artifactId>
      <version>1.2</version>
    </dependency>
    <!-- If you don't make an application which has not the snakeyaml add the implementation. -->
    <dependency>
      <groupId>org.yaml</groupId>
      <artifactId>snakeyaml</artifactId>
      <version>1.26</version>
    </dependency>
    <!-- For the bukkit projects -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-bukkit</artifactId>
      <version>1.2</version>
    </dependency>
</dependencies>
```

Also you have to make relocation for the library with;

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <!-- Other settings -->
        <relocations>
            <relocation>
                <pattern>io.github.portlek.configs</pattern>
                <!-- Replace this -->
                <shadedPattern>[YOUR PACKAGE].configs</shadedPattern>
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

## Usage

### Config Example

<details>
<summary>Core</summary>

```java
import io.github.portlek.configs.FileManaged;
import io.github.portlek.configs.ConfigSection;

@Config(
  name = "config"
)
public final class TestConfig extends FileManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Value
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends ConfigSection {

    @Value
    public String test_section_string = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
test-section:
  test-section-string: 'test'
```
</details>

<details>
<summary>Bukkit</summary>

```java
import io.github.portlek.configs.BukkitManaged;
import io.github.portlek.configs.BukkitSection;

@Config(
  name = "config"
)
public final class TestConfig extends BukkitManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Value
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends BukkitSection {

    @Value
    public String test_section_string = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
test-section:
  test-section-string: 'test'
```
</details>

### LinkedConfig Example

<details>
<summary>Core</summary>

```java
import io.github.portlek.configs.LinkedFileManaged;
import io.github.portlek.configs.util.MapEntry;

@LinkedConfig(configs = {
  @Config(
    name = "en"
  ),
  @Config(
    name = "tr"
  ),
})
public final class TestLinkedConfig extends LinkedFileManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Value
  public String same_in_every_language = match(s -> 
      Optional.of("Same in every language!")
  );

  @Value
  public String test = match(s -> {
    if (s.equals("en")) {
      return Optional.of("English words!");
    } else if (s.equals("tr")) {
      return Optional.of("Türkçe kelimeler!");
    }
    return Optional.empty();
  });

}
```

The result will be like that;

(en.yml file)
```yml
test: 'English words!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
```
</details>

<details>
<summary>Bukkit</summary>

```java
import io.github.portlek.configs.BukkitLinkedManaged;
import io.github.portlek.configs.util.MapEntry;

@LinkedConfig(configs = {
  @Config(
    name = "en"
  ),
  @Config(
    name = "tr"
  ),
})
public final class TestLinkedConfig extends BukkitLinkedManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Value
  public String same_in_every_language = match(s -> 
      Optional.of("Same in every language!")
  );

  @Value
  public String test = match(s -> {
    if (s.equals("en")) {
      return Optional.of("English words!");
    } else if (s.equals("tr")) {
      return Optional.of("Türkçe kelimeler!");
    }
    return Optional.empty();
  });

}
```

The result will be like that;

(en.yml file)
```yml
test: 'English words!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
```
</details>

## 3rd Party Libraries
For JSON file type https://github.com/dumptruckman/JsonConfiguration

For YAML file type and general configuration https://github.com/Carleslc/Simple-YAML

For JSON parsing https://github.com/ralfstx/minimal-json
