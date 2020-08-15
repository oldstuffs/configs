<img src="logo/logo.svg" width="92px"/>

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)
[![rultor](https://www.rultor.com/b/yegor256/rultor)](https://www.rultor.com/p/portlek/configs)

[![Build Status](https://travis-ci.com/portlek/configs.svg?branch=master)](https://travis-ci.com/portlek/configs)
![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)
## How to use
### Main dependency(Required). 
**You have to add at least 1 dependency which is `Optional Required`**
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
### Yaml support(Optional Required)
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
### Json support(Optional Required)
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
### Bukkit extensions(Optional)
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
## Loading a file.
To load and get ready a file method is FlManaged#load(). Here is the example:
```java
final class CreatingConfigClass {

  void createConfig() {
    final ExampleConfigFile file = new ExampleConfigFile();
    // You can't use the class before run the load method.
    file.load();
    file.getString("path.to.string");
    // You can also use fields as real file's values.
    //file.test_property;
    //file.language;
    //file.aSection.test_property;
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
  public final ExampleConfigFile.ExampleSection aSection = new ExampleSectio();

  @Property
  public String test_property = "my test property";

  @Property
  public String language = "en";

  @Section("example-section")
  public final class ExampleSection extends ConfigSection {

    @Property
    public String test_property = "my test property in a section";

  }

}
```
### void onCreate()
The method runs before the file and the managed load. You can't use getString or other methods in the method.
```java
@Config(
  name = "config",
  type = YamlFileType.class // JsonFileType.class
)
public final class ExampleConfigFile extends FileManaged {

  @Override
  public void onCreate() {

  }

}
```
### void onLoad()
The method runs after the file and the managed load. You can use getString and other methods in the method.
```java
@Config(
  name = "config",
  type = YamlFileType.class // JsonFileType.class
)
public final class ExampleConfigFile extends FileManaged {

  @Override
  public void onLoad() {

  }

}
```
## LinkedManaged
```java
@LinkedConfig({
  @LinkedFile(
    key = "en",
    config = @Config(
      name = "en_US",
      type = YamlFileType.class,
      location = "%basedir%/TestDirectory/languages"
    )
  ),
  @LinkedFile(
    key = "tr",
    config = @Config(
      name = "tr_TR",
      type = YamlFileType.class,
      location = "%basedir%/TestDirectory/languages"
    )
  )
})
public final class ExampleLinkedFile extends LinkedManaged {

  public ExampleLinkedFile(@NotNull final ExampleConfigFile config) {
      super(() -> config.language, MapEntry.from("config", config));
  }

  @NotNull
  public Map.Entry<String, Supplier<String>> getPrefix() {
    return MapEntry.from("%prefix%", () -> this.getConfig().plugin_prefix.build());
  }

  @NotNull
  private ExampleConfigFile getConfig() {
    return (ExampleConfigFile) this.object("config").orElseThrow(() ->
      new IllegalStateException("Config couldn't put into the objects!"));
  }

  @Property
  public Scalar<RpString> help_messages = this.match(m -> {
    m.put("en", Replaceable.from(
      new StringBuilder()
        .append("&a====== %prefix% &a======")
        .append('\n')
        .append("&7/examplecommand &r> &eShows help message.")
        .append('\n')
        .append("&7/examplecommand help &r> &eShows help message.")
        .append('\n')
        .append("&7/examplecommand reload &r> &eReloads the plugin.")
        .append('\n')
        .append("&7/examplecommand version &r> &eChecks for update.")
        .append('\n')
        .append("&7/examplecommand message <player> <message> &r> &eSends the message to the player."))
      .map(ColorUtil::colored)
      .replace(this.getPrefix()));
    m.put("tr", Replaceable.from(
      new StringBuilder()
        .append("&a====== %prefix% &a======")
        .append('\n')
        .append("&7/examplecommand &r> &eYardım mesajını görüntüler.")
        .append('\n')
        .append("&7/examplecommand help &r> &eYardım mesajını görüntüler.")
        .append('\n')
        .append("&7/examplecommand reload &r> &eEklentiyi yeniden başlatır.")
        .append('\n')
        .append("&7/examplecommand version &r> &eGüncellemeleri kontrol eder.")
        .append('\n')
        .append("&7/examplecommand message <oyuncu> <mesaj> &r> &eMesajı oyuncuya gönderir."))
      .map(ColorUtil::colored)
      .replace(this.getPrefix()));
  });

  @Property
  public Scalar<String> help_messages_normal = this.match(m -> {
    m.put("en", new StringBuilder()
      .append("====== Test Prefix ======")
      .append('\n')
      .append("/examplecommand > Shows help message.")
      .append('\n')
      .append("/examplecommand help > Shows help message.")
      .append('\n')
      .append("/examplecommand reload > Reloads the plugin.")
      .append('\n')
      .append("/examplecommand version > Checks for update.")
      .append('\n')
      .append("/examplecommand message <player> <message> > Sends the message to the player."));
    m.put("tr", new StringBuilder()
      .append("====== Test Prefix ======")
      .append('\n')
      .append("/examplecommand > Yardım mesajını görüntüler.")
      .append('\n')
      .append("/examplecommand help > Yardım mesajını görüntüler.")
      .append('\n')
      .append("/examplecommand reload &r> Eklentiyi yeniden başlatır.")
      .append('\n')
      .append("/examplecommand version > Güncellemeleri kontrol eder.")
      .append('\n')
      .append("/examplecommand message <oyuncu> <mesaj> > Mesajı oyuncuya gönderir."));
  });

}
```
### Scalar< T > match(Consumer<Map<String, T>>)
```java
final class Test {

  void getValueFromLinkedFile(@NotNull final ExampleLinkedFile file) {
    // Returns the help message depending on which ExampleConfigFile#language is.
    final String helpMessage = file.help_messages.get()
      // Came from Replaceable.
      .build();
    /*
    public ExampleLinkedFile(@NotNull final ExampleConfigFile config) {
      This is lamba, so if you change the language, the Scalar#get() method will
      run the lambda and will return the currently selected language's messsage.
      super(() -> config.language, MapEntry.from("config", config));
    }
    */
    // You can also use string, not just Repalceable objects, everything.
    final String helpMessage = file.help_messages_normal.get();
 }

}
```
## ComparableManaged
## ConfigSection
## Extensions
### Bukkit
- First of all you need `configs-bukkit` dependency.
- Secondly, you have to run `BukkitExtensions.registerExtensions();` before everything.
- You can also use BukkitComparable/BukkitLinked/BukkitManaged/BukkitSection classes instead of the core classes.
## How to contribute?
Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:
```
mvn clean install -Dgpg.skip=true
```
## Supporters
[![Jetbrains](logo/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
