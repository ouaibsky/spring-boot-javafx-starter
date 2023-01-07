# spring-boot-javafx-starter

Boot starter to easily Bridge JavaFx and Spring Boot.

It means JavaFx CDI will use the Spring one.

Inspired from: https://github.com/yoep/spring-boot-starter-javafx

## Usage

### Maven or gradle

* Add dependency to [latest version](https://central.sonatype.dev/search?q=spring-boot-javafx-starter&namespace=org.icroco) available into maven central

```xml

<dependency>
    <groupId>org.icroco</groupId>
    <artifactId>spring-boot-javafx-starter</artifactId>
    <version>x.y.z</version>
</dependency>
```

## Features

* Simple and do not re-write features available into spring boot or FXML
* Java FX view class generated at compile time through an annotation processor.
* Include a ViewManager that can be injected everywhere.

### Example

## Limitations

* FXML fx:include does not work properly.

## TODO

- [ ] Add Unit Test.
- [ ] I18N per view / controller.
- [ ] User preference abstraction.
- [X] Make java module