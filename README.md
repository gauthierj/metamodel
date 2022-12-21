# Metamodel

A simple metamodel generator for java classes.

This tool is useful when property names of Java classes needs to be referenced in the code (_e.g._ in a validation errors objects indicating which field is faulty or in critera APIs such as MongoDB client, ...)

## Getting started

Add the following dependencies to you Maven project:

```xml
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-annotation</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-model</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.gauthierj</groupId>
            <artifactId>metamodel-processor</artifactId>
            <scope>provided</scope>
        </dependency>
```

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
        addCriteria(_SimpleClass.mode().someProperty(), value);
    }
}
```
## Features

### Property detection: fields vs. getters

The `@Model` annotation allows to specify how properties are detected with `accessMode` attribute.

Property can be inferred either by looking at the fields or the getter methods. The default behavior is field inference.

When looking at getter methods, the pattern of a getter method can be specified in the attribute `getterPattern`. 

It matches by default all methods starting with 'is' or 'get' followed by an upper case letter (`^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$`). 

Pay attention that the pattern must have a single matching group that must hold property name.

### Specify generated class name

The `@Model` annotation allows to specify the generated class name with `generatedClassName` attribute.

This is particulary useful if a class must have several Metamodel generated (_e.g._ one by field and one by getter), or if the default generated class name clashes with any another class.

### Simple property vs. Structured proprety vs. Map property

...WIP...



