package com.github.gauthierj.metamodel.classbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.gauthierj.metamodel.classbuilder.Modifier.*;

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

}