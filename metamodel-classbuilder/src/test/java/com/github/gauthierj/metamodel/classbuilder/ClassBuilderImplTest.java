package com.github.gauthierj.metamodel.classbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.gauthierj.metamodel.classbuilder.Modifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassBuilderImplTest {

    @Test
    public void test() throws IOException {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.somepackage", "SomeClass")
                .addImport("com.some.otherpackage.SomeOtherclass")
                .addImport("com.some.otherpackage.Anotherclass")
                .field(List.of(PRIVATE, FINAL), "String", "aStringField")
                .constructor(List.of(PUBLIC))
                    .body()
                    .writeln("this.aStringField = \"aDefaultValue\"")
                    .build()
                .constructor(List.of(PUBLIC))
                    .parameter("String", "aParam")
                    .parameter("int", "anotherParam")
                    .body()
                        .writeln("this.aStringField = aParam;")
                        .writeln_indentRight("if (anotherParam > 3) {")
                        .writeln("System.out.println(\"Hello World!\");")
                        .indentLeft_writeln_indentRight("} else {")
                        .writeln("System.out.println(\"Goodbye World!\");")
                        .indentLeft_writeln("}")
                        .build()
                .method(List.of(PRIVATE), "void", "doSomething")
                    .body()
                        .writeln("// Nothing to do")
                        .build()
                .innerClass(List.of(PUBLIC, STATIC), "AnInnerClass")
                    .field(List.of(PRIVATE), "String", "anInnerClassField")
                    .constructor(List.of(PUBLIC))
                        .parameter("String", "anInnerClassField")
                        .body()
                            .writeln("this.anInnerClassField = anInnerClassField;")
                            .build()
                    .method(List.of(PUBLIC), "String", "getAnInnerClassField")
                    .body()
                        .writeln("return anInnerClassField;")
                        .build()
                    .build()
                .build();

        String expectedClass = new String(this.getClass().getResourceAsStream("/com/github/gauthierj/metamodel/classbuilder/SomeClass.java").readAllBytes());

        Assertions.assertEquals(
                expectedClass,
                clazz);
    }

    @Test
    public void test_classWithSuperclass() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "Child", "Parent")
                .build();
        assertTrue(clazz.contains("public class Child extends Parent {"));
    }

    @Test
    public void test_classWithInterfaces() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass", null, "Runnable", "Serializable")
                .build();
        assertTrue(clazz.contains("public class MyClass implements Runnable, Serializable {"));
    }

    @Test
    public void test_classWithSuperclassAndInterfaces() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "Child", "Parent", "Runnable")
                .build();
        assertTrue(clazz.contains("public class Child extends Parent implements Runnable {"));
    }

    @Test
    public void test_classWithClassSuperclass() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "Child", Number.class)
                .build();
        assertTrue(clazz.contains("public class Child extends Number {"));
    }

    @Test
    public void test_addImportClass() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .addImport(java.util.List.class)
                .build();
        assertTrue(clazz.contains("import java.util.List;"));
    }

    @Test
    public void test_fieldWithInitializer() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .field(List.of(PRIVATE, STATIC, FINAL), "int", "MAX_SIZE", "100")
                .build();
        assertTrue(clazz.contains("private static final int MAX_SIZE = 100;"));
    }

    @Test
    public void test_privateStaticFinalField() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .privateStaticFinalField("String", "INSTANCE", "\"default\"")
                .build();
        assertTrue(clazz.contains("private static final String INSTANCE = \"default\";"));
    }

    @Test
    public void test_publicConstructorShorthand() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .publicConstructor()
                    .body()
                    .writeln("// empty")
                    .build()
                .build();
        assertTrue(clazz.contains("public MyClass()"));
    }

    @Test
    public void test_publicMethodShorthand() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .publicMethod("String", "getName")
                    .body()
                    .writeln("return null;")
                    .build()
                .build();
        assertTrue(clazz.contains("public String getName()"));
    }

    @Test
    public void test_publicStaticMethodShorthand() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .publicStaticMethod("MyClass", "instance")
                    .body()
                    .writeln("return null;")
                    .build()
                .build();
        assertTrue(clazz.contains("public static MyClass instance()"));
    }

    @Test
    public void test_innerClassWithSuperclass() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "Outer")
                .innerClass(List.of(PUBLIC, STATIC), "Inner", "AbstractBase")
                    .build()
                .build();
        assertTrue(clazz.contains("public static class Inner extends AbstractBase {"));
    }

    @Test
    public void test_classWithNoPackage() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), null, "MyClass")
                .build();
        assertFalse(clazz.contains("package"));
        assertTrue(clazz.contains("public class MyClass {"));
    }

    @Test
    public void test_parameterWithClassType() {
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .publicConstructor()
                    .parameter(String.class, "name")
                    .parameter(int.class, "age")
                    .body()
                    .writeln("// body")
                    .build()
                .build();
        assertTrue(clazz.contains("public MyClass(String name, int age)"));
    }

    @Test
    public void test_duplicateImportIsEmittedOnce() {
        // Reproduces issue #3: addImport() has no deduplication — calling it twice
        // with the same type emits the import statement twice in the output.
        String clazz = ClassBuilder.of(List.of(PUBLIC), "com.pkg", "MyClass")
                .addImport("com.other.SomeType")
                .addImport("com.other.SomeType")
                .build();

        int count = 0;
        int idx = 0;
        while ((idx = clazz.indexOf("import com.other.SomeType;", idx)) != -1) {
            count++;
            idx++;
        }
        assertEquals(1, count, "import should appear exactly once, but appeared " + count + " times");
    }

}