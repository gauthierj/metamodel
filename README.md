![SNAPSHOT Build](https://github.com/gauthierj/metamodel/actions/workflows/deploy.yml/badge.svg?branch=develop) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=coverage)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel)

# Metamodel

A simple metamodel generator for Java classes.

This tool is useful when property names of Java classes need to be referenced in the code — for example in validation error objects indicating which field is faulty, or in criteria APIs such as the MongoDB client. It generates a metamodel class that provides type-safe, refactor-safe access to property names and paths.

**Java 17+** required.

---

## Table of contents

- [Getting started](#getting-started)
  - [Maven](#maven)
  - [Gradle](#gradle)
  - [Spring Data MongoDB extension](#spring-data-mongodb-extension)
- [How it works](#how-it-works)
  - [What gets generated](#what-gets-generated)
  - [Real-world example](#real-world-example)
- [Property types](#property-types)
  - [Simple properties](#simple-properties)
  - [Structured properties](#structured-properties)
  - [Map properties](#map-properties)
- [Configuration](#configuration)
  - [Field vs. getter detection](#field-vs-getter-detection)
  - [Getter pattern](#getter-pattern)
  - [Override property name](#override-property-name)
  - [Ignore a property](#ignore-a-property)
  - [Custom generated class name](#custom-generated-class-name)
  - [Per-property metamodel overrides](#per-property-metamodel-overrides)
- [Extensibility](#extensibility)
- [Integration tests](#integration-tests)

---

## Getting started

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.github.gauthierj</groupId>
        <artifactId>metamodel-annotation</artifactId>
        <version>xxx</version>
    </dependency>
    <dependency>
        <groupId>io.github.gauthierj</groupId>
        <artifactId>metamodel-model</artifactId>
        <version>xxx</version>
    </dependency>
    <dependency>
        <groupId>io.github.gauthierj</groupId>
        <artifactId>metamodel-processor</artifactId>
        <version>xxx</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Gradle

```groovy
dependencies {
    implementation 'io.github.gauthierj:metamodel-annotation:xxx'
    implementation 'io.github.gauthierj:metamodel-model:xxx'
    annotationProcessor 'io.github.gauthierj:metamodel-processor:xxx'
}
```

### Spring Data MongoDB extension

To automatically generate metamodels for classes annotated with Spring Data MongoDB's `@Document`, add the following dependency as well:

**Maven:**
```xml
<dependency>
    <groupId>io.github.gauthierj</groupId>
    <artifactId>metamodel-mongo-extension</artifactId>
    <version>xxx</version>
    <scope>provided</scope>
</dependency>
```

**Gradle:**
```groovy
annotationProcessor 'io.github.gauthierj:metamodel-mongo-extension:xxx'
```

With this extension on the annotation processor classpath, any `@Document`-annotated class will have a metamodel generated automatically — no `@Model` annotation needed. The extension also maps `@Id`-annotated fields to `"_id"` and respects `@Field` name overrides.

---

## How it works

### What gets generated

Annotating a class with `@Model`:

```java
@Model
public class Person {
    private String firstName;
    private String lastName;
    private Address address;
}
```

causes the processor to generate a `_Person` class at compile time:

```java
public class _Person extends _Model {

    private static final _Person MODEL = new _Person(_Path.of());

    public static _Person model() { return MODEL; }

    public String firstName() { return _property("firstName"); }
    public String lastName()  { return _property("lastName"); }

    public _Address address() {
        return new _Address(super._rootPath().with("address"));
    }
}
```

Simple properties return a `String` (the property name). Structured properties return a sub-metamodel instance rooted at that property's path, enabling further navigation.

### Real-world example

A common use case is building MongoDB criteria in a type-safe, refactor-safe way:

```java
@Document
public class Order {
    @Id
    private String id;
    private String customerId;
    private Address shippingAddress;
}

@Document
public class Address {
    private String street;
    private String city;
    private String zipCode;
}
```

Without metamodel, criteria are stringly-typed and break silently on rename:

```java
// Fragile: renaming "city" won't cause a compile error
Criteria.where("shippingAddress.city").is("Paris");
```

With metamodel:

```java
// Refactor-safe: renaming the field breaks the build
Criteria.where(_Order.model().shippingAddress().city()).is("Paris");
// -> "shippingAddress.city"
```

---

## Property types

### Simple properties

Primitives, `String`, `Number`, `Boolean`, `Enum`, `Collection`, `Optional`, and arrays are all detected as simple properties. The generated method returns the fully-qualified property path as a `String`.

```java
@Model
public class Product {
    private String name;
    private double price;
    private List<String> tags;
}

_Product.model().name()  // -> "name"
_Product.model().price() // -> "price"
_Product.model().tags()  // -> "tags"
```

### Structured properties

Any type that is not a simple property and not a `Map` is detected as a structured property. The processor generates a sub-metamodel for it and returns an instance rooted at the property path.

```java
@Model
public class Order {
    private Address shippingAddress;
}

_Order.model().shippingAddress().street() // -> "shippingAddress.street"
_Order.model().shippingAddress().city()   // -> "shippingAddress.city"
```

This works transitively across any depth of nesting:

```java
_Order.model().shippingAddress().country().name() // -> "shippingAddress.country.name"
```

### Map properties

`Map` properties are exposed as a bare `_Model`, allowing dynamic key navigation via `_property(String name)`:

```java
@Model
public class Product {
    private Map<String, String> attributes;
}

_Product.model().attributes()._property("color") // -> "attributes.color"
```

---

## Configuration

### Field vs. getter detection

By default, properties are discovered from **fields**. Switch to getter-based detection with `accessMode`:

```java
@Model(accessMode = PropertyAccessMode.GETTER)
public class Person {
    private String firstName; // ignored — no getter named getFirstName found

    public String getFirstName() { return firstName; } // -> "firstName"
}
```

### Getter pattern

When using getter-based detection, the getter pattern can be customized. The default matches `getXxx()` and `isXxx()`. The first capturing group of the regex defines the property name (first letter is decapitalized).

```java
@Model(accessMode = PropertyAccessMode.GETTER, getterPattern = "^fetch([A-Z][a-zA-Z0-9_$]*)$")
public class Person {
    public String fetchFirstName() { return firstName; } // -> "firstName"
}
```

### Override property name

Use `@Property(name = "...")` to specify the property name returned at runtime, independently of the Java field or method name. This is useful when the serialized name differs from the Java name.

```java
@Model
public class Person {
    @Property(name = "first_name")
    private String firstName;
}

_Person.model().firstName() // -> "first_name"
```

### Ignore a property

Use `@Property(ignore = true)` to exclude a field or method from the generated metamodel.

```java
@Model
public class Person {
    private String firstName;

    @Property(ignore = true)
    private transient String cachedFullName;
}
```

### Custom generated class name

By default the generated class is named `_ClassName`. Use `generatedClassName` to override this — for example when two metamodels of the same type are needed, or to avoid a name clash.

```java
@Model(generatedClassName = "_PersonByGetter", accessMode = PropertyAccessMode.GETTER)
public class Person { ... }
```

### Per-property metamodel overrides

`@Model` can also be placed on a field or method to override how the sub-metamodel for that specific property is generated. This is useful when the property type is not itself annotated with `@Model` but you want a specific access mode or generated class name for it.

```java
@Model(accessMode = PropertyAccessMode.FIELD)
public class Order {

    // Address is not annotated @Model, but generate its metamodel with GETTER mode
    @Model(accessMode = PropertyAccessMode.GETTER)
    private Address shippingAddress;
}
```

If two properties reference the same type but need different metamodels, assign distinct generated class names:

```java
@Model(accessMode = PropertyAccessMode.FIELD)
public class Order {

    @Model(accessMode = PropertyAccessMode.GETTER, generatedClassName = "_AddressByGetter")
    private Address shippingAddress;

    private Address billingAddress; // uses default _Address (FIELD mode)
}
```

---

## Extensibility

The processor is designed to be extended via two `ServiceLoader`-discovered SPI interfaces:

**`ModelAnnotationProvider`** — declares which annotations trigger metamodel generation. The built-in implementation handles `@Model`. The MongoDB extension adds `@Document`. Implement this interface to trigger generation from your own annotation:

```java
public class MyAnnotationProvider implements ModelAnnotationProvider {
    @Override
    public Set<String> getModelAnnotations() {
        return Set.of("com.example.MyTriggerAnnotation");
    }
}
```

Register it in `META-INF/services/com.github.gauthierj.metamodel.processor.ModelAnnotationProvider`.

**`PropertyNameResolver`** — customizes how property names are resolved. The MongoDB extension uses this to map `@Id` → `"_id"` and `@Field` → the declared field name. Implement this interface to remap names from your own annotations:

```java
public class MyPropertyNameResolver implements PropertyNameResolver {
    @Override
    public Optional<String> resolve(VariableElement field) {
        MyAnnotation ann = field.getAnnotation(MyAnnotation.class);
        return ann != null ? Optional.of(ann.value()) : Optional.empty();
    }

    @Override
    public Optional<String> resolve(ExecutableElement method) {
        return Optional.empty();
    }
}
```

Register it in `META-INF/services/com.github.gauthierj.metamodel.processor.resolver.PropertyNameResolver`.

---

## Integration tests

See the integration tests for comprehensive working examples: https://github.com/gauthierj/metamodel/tree/master/metamodel-integration-test
