[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=old)](https://travis-ci.com/portlek/configs)
## Setup

<details>
<summary>Maven</summary>

```xml
<repository>
    <id>portlek</id>
    <url>https://dl.bintray.com/portlek/maven</url>
</repository>

<!-- For the all project type -->
<dependency>
  <groupId>io.github.portlek</groupId>
  <artifactId>configs-core</artifactId>
  <version>1.0</version>
</dependency>
<!-- For the bukkit projects -->
<dependency>
  <groupId>io.github.portlek</groupId>
  <artifactId>configs-bukkit</artifactId>
  <version>1.0</version>
</dependency>
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
@Config(
  name = "config"
)
public final class TestConfig extends ManagedBase {

  @Value
  public String test = "test";

  @Instance
  public final TestSection testSection = new TestSection();

  @Section(path = "test-section")
  public final class TestSection extends ConfigSectionBase {

    @Value
    public String test_section_string = "test";

  }

}
```
</details>

<details>
<summary>Bukkit</summary>

@Config(
  name = "config"
)
public final class TestConfig extends BukkitManagedBase {

  @Value
  public String test = "test";

  @Instance
  public final TestSection testSection = new TestSection();

  @Section(path = "test-section")
  public final class TestSection extends BukkitConfigSection {

    @Value
    public String test_section_string = "test";

  }

}
```
</details>
```java

### LinkedConfig Example

#### Core
```java
@LinkedConfig(configs = {
  @Config(
    name = "en"
  ),
  @Config(
    name = "tr"
  ),
})
public final class TestLinkedConfig extends LinkedManagedBase {

  public TestLinkedConfig(@NotNull TestConfig testConfig) {
    super(testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

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

#### Bukkit
```java
@LinkedConfig(configs = {
  @Config(
    name = "en"
  ),
  @Config(
    name = "tr"
  ),
})
public final class TestLinkedConfig extends BukkitLinkedManaged {

  public TestLinkedConfig(@NotNull TestConfig testConfig) {
    super(testConfig.language, MapEntry.from("config", testConfig));
  }

  @NotNull
  public TestConfig getConfig() {
    return (TestConfig) this.pull("config");
  }

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
The result of the config example will be like that;

```yml
test: 'test'
test-section:
  test-section-string: 'test'
```

The result of the linked config example will be like that;

(en.yml file)
```yml
test: 'English words!'
```
(tr.yml file)
```yml
test: 'Türkçe kelimeler!'
```
