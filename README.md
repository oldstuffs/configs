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
repositories {
  mavenCentral()
}

dependencies {
  // For the all project type
  implementation("io.github.portlek:configs-core:${version}")
  // For the bukkit projects
  // depend:
  //  - configs-bukkit
  implementation("io.github.portlek:configs-bukkit:${version}")
  // For the nukkit projects
  // depend:
  //  - configs-nukkit
  implementation("io.github.portlek:configs-nukkit:${version}")
  // For the sponge projects
  // @Plugin(id = "plugin-name", /** other stuffs,**/dependencies = @Dependency(id = "configs-sponge"))
  implementation("io.github.portlek:configs-sponge:${version}")
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
  <!-- Don't forget to put the released .jar file into your plugins folder -->
  <!-- For the bukkit projects -->
  <!-- depend:
         - configs-bukkit
  -->
  <dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-bukkit</artifactId>
    <version>${version}</version>
    <scope>provided</scope>
  </dependency>
  <!-- For the nukkit projects -->
  <!-- depend:
         - configs-nukkit
  -->
  <dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-nukkit</artifactId>
    <version>${version}</version>
    <scope>provided</scope>
  </dependency>
  <!-- For the sponge projects -->
  <!-- @Plugin(id = "plugin-name", /** other stuffs,**/dependencies = @Dependency(id = "configs-sponge")) -->
  <dependency>
    <groupId>io.github.portlek</groupId>
    <artifactId>configs-sponge</artifactId>
    <version>${version}</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
</details>

## Usage

### Config Example

```java
final class TestConfigLoader() {
  void createTestConfig() {
      final TestConfig testConfig = new TestConfig();
      testConfig.load();
  }
}
```
<details>
<summary>Core</summary>

```java
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

    @Property(path = "test-section-string")
    public String testSectionString = "test";

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

```yaml
depend:
  - configs-bukkit
```
```java
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

    @Property(path = "test-section-string")
    public String testSectionString = "test";

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

```yaml
depend:
  - configs-nukkit
```
```java
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

    @Property(path = "test-section-string")
    public String testSectionString = "test";

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
<summary>Sponge</summary>

```java
@Config(
  name = "config"
)
public final class TestConfig extends SpongeManaged {

  @Instance
  public final TestConfig.TestSection testSection = new TestConfig.TestSection();

  @Property
  public String test = "test";

  @Section(path = "test-section")
  public final class TestSection extends SpongeSection {

    @Property(path = "test-section-string")
    public String testSectionString = "test";

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

```java
final class TestLinkedConfigLoader() {
  void createTestLinkedConfig() {
      final TestLinkedConfig testLinkedConfig = new TestLinkedConfig(testConfig);
      testLinkedConfig.load();
  }
}
```
<details>
<summary>Core</summary>

```java
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
      Optional.of("Same in every language!"));

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
      Optional.of("Same in every language!"));

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
      Optional.of("Same in every language!"));

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
<summary>Sponge</summary>

```java
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
public final class TestLinkedConfig extends SpongeLinkedManaged {

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
For general configuration library https://github.com/SpongePowered/Configurate
