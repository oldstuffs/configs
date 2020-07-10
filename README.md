# Configs
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
    // For the all project type
    implementation("io.github.portlek:configs-core:${version}")
    // For the bukkit projects
    implementation("io.github.portlek:configs-bukkit:${version}")
    // For the nukkit projects
    implementation("io.github.portlek:configs-nukkit:${version}")
    // For the bungeecord projects
    implementation("io.github.portlek:configs-bungeecord:${version}")
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
    <!-- For the bungeecord projects -->
    <dependency>
      <groupId>io.github.portlek</groupId>
      <artifactId>configs-bungeecord</artifactId>
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

## Usage

### Config Example

<details>
<summary>Core</summary>

```java
@Config("config")
public final class TestConfig extends FileManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section("test-section")
  public final class TestSection extends ConfigSection {

    @Property("test-section-string")
    public String testSectionString = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
"test-section":
  "test-section-string": 'test'
```
</details>

<details>
<summary>Bukkit</summary>

```java
@Config("config")
public final class TestConfig extends BukkitManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section("test-section")
  public final class TestSection extends BukkitSection {

    @Property("test-section-string")
    public String testSectionString = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
"test-section":
  "test-section-string": 'test'
```
</details>

<details>
<summary>Nukkit</summary>

```java
@Config("config")
public final class TestConfig extends NukkitManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section("test-section")
  public final class TestSection extends NukkitSection {

    @Property("test-section-string")
    public String testSectionString = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
"test-section":
  "test-section-string": 'test'
```
</details>

<details>
<summary>BungeeCord</summary>

```java
@Config("config")
public final class TestConfig extends BungeeManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section("test-section")
  public final class TestSection extends BungeeSection {

    @Property("test-section-string")
    public String testSectionString = "test";

  }

}
```

The result will be like that;

```yml
test: 'test'
"test-section":
  "test-section-string": 'test'
```
</details>

### LinkedConfig Example

<details>
<summary>Core</summary>

```java
@LinkedConfig({
  @LinkedFile(
    id = "en",
    config = Config("en_US")
  ),
  @LinkedFile(
    id = "tr",
    config = @Config("tr_TR")
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
      Optional.of("Same in every language!"));

  @Property
  public String test = match(m -> {
    m.put("en", "English words!");
    m.put("tr", "Türkçe kelimeler!");
  });

}
```

The result will be like that;

(en_US.yml file)
```yml
test: 'English words!'
"same-in-every-language": 'Same in every language!'
```
(tr_TR.yml file)
```yml
test: 'Türkçe kelimeler!'
"same-in-every-language": 'Same in every language!'
```
</details>

<details>
<summary>Bukkit</summary>

```java
@LinkedConfig({
  @LinkedFile(
    id = "en",
    config = Config("en_US")
  ),
  @LinkedFile(
    id = "tr",
    config = @Config("tr_TR")
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
  public String same_in_every_language = match(m -> 
      Optional.of("Same in every language!"));

  @Property
  public String test = match(s -> {
    m.put("en", "English words!");
    m.put("tr", "Türkçe kelimeler!");
  });

}
```

The result will be like that;

(en_US.yml file)
```yml
test: 'English words!'
"same-in-every-language": 'Same in every language!'
```
(tr_TR.yml file)
```yml
test: 'Türkçe kelimeler!'
"same-in-every-language": 'Same in every language!'
```
</details>

<details>
<summary>Nukkit</summary>

```java
@LinkedConfig({
  @LinkedFile(
    id = "en",
    config = Config("en_US")
  ),
  @LinkedFile(
    id = "tr",
    config = @Config("tr_TR")
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
  public String same_in_every_language = match(m -> 
      Optional.of("Same in every language!"));

  @Property
  public String test = match(s -> {
    m.put("en", "English words!");
    m.put("tr", "Türkçe kelimeler!");
  });

}
```

The result will be like that;

(en_US.yml file)
```yml
test: 'English words!'
"same-in-every-language": 'Same in every language!'
```
(tr_TR.yml file)
```yml
test: 'Türkçe kelimeler!'
"same-in-every-language": 'Same in every language!'
```
</details>

<details>
<summary>BungeeCord</summary>

```java
@LinkedConfig({
  @LinkedFile(
    id = "en",
    config = Config("en_US")
  ),
  @LinkedFile(
    id = "tr",
    config = @Config("tr_TR")
  ),
})
public final class TestLinkedConfig extends BungeeLinkedManaged {

  public TestLinkedConfig(@NotNull final TestConfig testConfig) {
    super(() -> testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

  @Property
  public String same_in_every_language = match(m -> 
      Optional.of("Same in every language!"));

  @Property
  public String test = match(m -> {
    m.put("en", "English words!");
    m.put("en", "Türkçe kelimeler!");
  });

}
```

The result will be like that;

(en_US.yml file)
```yml
test: 'English words!'
"same-in-every-language": 'Same in every language!'
```
(tr_TR.yml file)
```yml
test: 'Türkçe kelimeler!'
"same-in-every-language": 'Same in every language!'
```
</details>

## 3rd Party Libraries
For JSON configuration https://github.com/dumptruckman/JsonConfiguration

For YAML configuration https://github.com/Carleslc/Simple-YAML

For YAML parsing https://github.com/decorators-squad/eo-yaml

For JSON parsing https://github.com/ralfstx/minimal-json

## Supporters

[![Jetbrains](jetbrains/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
