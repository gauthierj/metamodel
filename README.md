![Build](https://github.com/gauthierj/metamodel/actions/workflows/deploy.yml/badge.svg) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=coverage)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel) [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=gauthierj_metamodel&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=gauthierj_metamodel)

# Metamodel

A simple metamodel generator for java classes.

This tool is useful when property names of Java classes needs to be referenced in the code (_e.g._ in a validation errors objects indicating which field is faulty or in critera APIs such as MongoDB client, ...).

This tools generates a metamodel class that allows to easily manipulates properties names and pathes.

## Getting started

Add the following dependencies to you Maven project:

```xml
    <dependecies>
        ...
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-annotation</artifactId>
            <version>xxx</version>
        </dependency>
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-model</artifactId>
            <version>xxx</version>
        </dependency>
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-processor</artifactId>
            <version>xxx</version>
            <scope>provided</scope>
        </dependency>
        ...
    </dependencies>
    
    <repositories>
        ...
        <repository>
            <id>github-metamodel</id>
            <url>https://github.com/gauthierj/metamodel/raw/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
        ...
    </repositories>
```

## TL;DR

Have a look at the integration tests to have some practical examples : https://github.com/gauthierj/metamodel/tree/master/metamodel-integration-test

## Introduction

This APT processor allows to generate simple metamodel for Java classes at compilation.

This allows to deal with any Java class's properties names very easily with `@Model` annotation:

Given a Java class:
```java
@Model
public class SimpleClass {
    
    private final int someProperty;
    
    public SimpleClass(int someProperty) {
        this.someProperty = someProperty;
    }
    
    public int getSomeProperty() {
        return someProperty;
    }
}
```

It generates a metamodel class `_SimpleClass` that gives a structural way to access the names of the properties of `SimpleClass`:

```java
public class SomeUtilClass {
    
    public void addCriteriaOnSomeProperty(int value) {
        addCriteria(_SimpleClass.model().someProperty(), value);
    }
}
```

## The metamodel 

### `@Model`, `_Model` and `_Path`

Metamodels are generated by adding an annotation `@Model` on any Java class.

Properties' pathes are represented by a `_Path` instance that encapsulates every path component from the root class to the property.

Every generated metamodel inherits from the `_Model` class. This object encapsulate a `_Path` instance containing the current root path of the metamodel. This path can be retreived either as a String via the `path()` method, or as a `_Path` instance with the `rootPath()` method.

The `_Model` class also allows to retreive the complete path as a String of any property (even for not generated ones) via the `_property(String name)` method. This is also possible to retreive a `_Path` instance with the method `_propertyPath(String name)`

The processor recognizes three types of properties : simple, structured and map properties.

### Simple properties

All basic Java types (`String`, `Number`, `Boolean`, primitives, ...), as well as `Collection` (and all its descendant) and `Optional` of such types are detected as simple properties. The processor generates a property name method in the generated metamodel.

```java
    String someSimplePropertyPath = _MyModel.model().someSimpleProperty();
    // someSimplePropertyPath == "someSimplePropertyPath"
```

### Structured properties

All other types (except `Map`) are detected as structured properties. A whole metamodel is generated for such types and an instance of this metamodel with a root path corresponding to the whole path of the property is returned by the property method in the main metamodel.

This allows to build the whole path of properties in these objects. 

```java
    String aSimplePropertyPath = _MyModel.model()
                                          .structuredProperty()
                                          .againAstructuredProperty()
                                          .aSimpleProperty();
    // aSimplePropertyPath == "structuredProperty.againAstructuredProperty.aSimpleProperty"
```

### Map properties

Map are detected as basic metamodel without any defined additional property.
The method `_property(String name)` from the `_Model` class allows to build the path of an entry in the Map.

```java
    String aMapEntryPath = _MyModel.model()
                                    .structuredProperty()
                                    .aMapProperty()
                                    ._property("myEntry");
    // aMapEntryPath == "structuredProperty.aMapProperty.myEntry"
```

### Property detection

#### Fields vs. getters

The `@Model` annotation allows to specify how properties are detected with `accessMode` attribute.

Property can be inferred either by looking at the fields or the getter methods. The default behavior is field inference.

#### Getter pattern

When looking at getter methods, the pattern of a getter method can be specified in the attribute `getterPattern`. 

It matches by default all methods starting with 'is' or 'get' followed by an upper case letter: `^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$`. 

The pattern must have a single matching group that must hold property name.

### Specify generated class name

The `@Model` annotation allows to specify the generated class name with `generatedClassName` attribute.

This is particulary useful if a class must have several Metamodel generated (_e.g._ one by field and one by getter), or if the default generated class name clashes with any another class.

### Override property name

It may be useful to specify the property name you want to retreive when it is different from the logical name of the property defined by the Java code (_e.g._: an `id` poprety in the class that must translates to `_id` in the metamodel).

This is possible by adding an annotation `@Property(name = 'xxx')` on the field or the getter of the property (depending on the detection mode).

### Ignore property

It may be useful to ignore some properties from the Java code (_e.g._ a matching method that is not a true getter, or a property that is not well handled by the processor, ...).

This is possible by adding an annotation `@Property(ignore = true)` on the field or the getter of the property (depending on the detection mode).

### Metamodel generated via a structured property

Structured properties are mapped as another metamodel. If a structured property type is not annotated with `@Model`, a metamodel for that type will be automatically generated, using same paramaters as the current metalmodel (`accessMode` and `getterPattern`).

It is possible to override these parameter, as well as specifying the generated class name by adding an annotation `@Model` on the property.

If the type is already mapped by a metamodel and the property should generate a different one (_e.g._ field vs. getter) than a different generated class name must be provided.

```java
@Model(accessMode = PropertyAccessMode.FIELD)
public class MyModel {

    // The _OtherModel class will be generated with FIELD access mode
    // Unless OtherModel is annotated by @Model
    private OtherModel otherModel; 
}
```

```java
@Model(accessMode = PropertyAccessMode.FIELD)
public class MyModel {

    // The _OtherModel class will be generated with GETTER access mode
    // Unless OtherModel is annotated by @Model
    @Model(accessMode = PropertyAccessMode.GETTER)
    private OtherModel otherModel; 
}
```

```java
@Model(accessMode = PropertyAccessMode.FIELD)
public class MyModel {

    // The _MyModel_OtherModel class will be generated with GETTER access mode
    @Model(accessMode = PropertyAccessMode.GETTER, generatedClassName = "_MyModel_OtherModel")
    private OtherModel otherModel; 
}
```
