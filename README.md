# Configs
Annotation based configuration library for any Java project.

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=configs-core)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-bukkit?label=configs-bukkit)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-nukkit?label=configs-nukkit)
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
    implementation("io.github.portlek:configs-core:${version}")
    implementation("org.yaml:snakeyaml:1.26")
    // For the bukkit projects
    implementation("io.github.portlek:configs-bukkit:${version}")
    // For the nukkit projects
    implementation("io.github.portlek:configs-nukkit:${version}")
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
      <version>${version}</version>
    </dependency>
    <dependency>
      <groupId>org.yaml</groupId>
      <artifactId>snakeyaml</artifactId>
      <version>1.26</version>
    </dependency>
    <!-- For the bukkit projects -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-bukkit</artifactId>
      <version>${version}</version>
    </dependency>
    <!-- For the nukkit projects -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-nukkit</artifactId>
      <version>${version}</version>
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
import io.github.portlek.configs.structure.managed.FileManaged;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import io.github.portlek.configs.annnotations.*;

@Config(
  name = "config"
)
public final class TestConfig extends FileManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends ConfigSection {

    @Property
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
import io.github.portlek.configs.bukkit.BukkitManaged;
import io.github.portlek.configs.bukkit.BukkitSection;
import io.github.portlek.configs.annnotations.*;

@Config(
  name = "config"
)
public final class TestConfig extends BukkitManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends BukkitSection {

    @Property
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
<summary>Nukkit</summary>

```java
import io.github.portlek.configs.nukkit.NukkitManaged;
import io.github.portlek.configs.nukkit.NukkitSection;
import io.github.portlek.configs.annnotations.*;

@Config(
  name = "config"
)
public final class TestConfig extends NukkitManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends NukkitSection {

    @Property
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
import io.github.portlek.configs.structure.LinkedFileManaged;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.annnotations.*;

@LinkedConfig(files = {
  @LinkedFile(
    id = "en",
    config = Config(
      name = "en"
    )
  ),
  @LinkedFile(
    id = "tr",
    config = @Config(
      name = "tr"
    )
  ),
})
public final class TestLinkedConfig extends LinkedFileManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(() -> testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Property
  public String same_in_every_language = match(s -> 
      Optional.of("Same in every language!")
  );

  @Property
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
same-in-every-language: 'Same in every language!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
same-in-every-language: 'Same in every language!'
```
</details>

<details>
<summary>Bukkit</summary>

```java
import io.github.portlek.configs.bukkit.BukkitLinkedManaged;
import io.github.portlek.configs.util.MapEntry;

@LinkedConfig(files = {
  @LinkedFile(
    id = "en",
    config = @Config(
      name = "en"
    )
  ),
  @LinkedFile(
    id = "tr",
    config = @Config(
      name = "tr"
    )
  ),
})
public final class TestLinkedConfig extends BukkitLinkedManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(() -> testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Property
  public String same_in_every_language = match(s -> 
      Optional.of("Same in every language!")
  );

  @Property
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
same-in-every-language: 'Same in every language!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
same-in-every-language: 'Same in every language!'
```
</details>

<details>
<summary>Nukkit</summary>

```java
import io.github.portlek.configs.nukkit.NukkitLinkedManaged;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.annotations.*;

@LinkedConfig(files = {
  @LinkedFile(
    id = "en",
    config = @Config(
      name = "en"
    )
  ),
  @LinkedFile(
    id = "tr",
    config = @Config(
      name = "tr"
    )
  ),
})
public final class TestLinkedConfig extends NukkitLinkedManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(() -> testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Property
  public String same_in_every_language = match(s -> 
      Optional.of("Same in every language!")
  );

  @Property
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
same-in-every-language: 'Same in every language!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
same-in-every-language: 'Same in every language!'
```
</details>

## 3rd Party Libraries
For JSON file type https://github.com/dumptruckman/JsonConfiguration

For YAML file type and general configuration https://github.com/Carleslc/Simple-YAML

For JSON parsing https://github.com/ralfstx/minimal-json
