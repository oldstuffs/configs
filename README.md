<img src="logo/logo.svg" width="92px"/>

[![idea](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

![master](https://github.com/portlek/configs/workflows/build/badge.svg)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.portlek/configs-core?label=version)](https://repo1.maven.org/maven2/io/github/portlek/configs-core/)

## How to use

### core

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

### yaml

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

### json

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

## How to contribute?

Just fork the repo and send us a pull request.

Make sure your branch builds without any warnings/issues:

```
mvn clean install
```

## Supporters

[![Jetbrains](logo/jetbrains.svg)](https://www.jetbrains.com/?from=configs)
