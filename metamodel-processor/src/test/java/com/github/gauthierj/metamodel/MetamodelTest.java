package com.github.gauthierj.metamodel;

import com.github.gauthierj.metamodel.generator.util.StringUtils;
import com.github.gauthierj.metamodel.processor.ModelProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Optional;

class MetamodelTest {

    @Test
    public void test_simpleModelByField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModelByField.java"));

        Assertions.assertEquals(compile.status(), Compilation.Status.SUCCESS);
        Assertions.assertEquals(compile.generatedSourceFiles().size(), 1);

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._SimpleModelByField");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "anIntProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aPrimitiveBooleanProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aBooleanProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringListProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringArray"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aPropertyWithADifferentName", "someDifferentName"));

        System.out.println(compile);
    }

    @Test
    public void test_simpleModelByGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModelByGetter.java"));

        Assertions.assertEquals(compile.status(), Compilation.Status.SUCCESS);
        Assertions.assertEquals(compile.generatedSourceFiles().size(), 1);

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._SimpleModelByGetter");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "anIntProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aPrimitiveBooleanProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aBooleanProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringListProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringArray"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        Assertions.assertTrue(hasSimpleProprety(simpleModelByField, "aPropertyWithAnotherName", "someDifferentName"));

        System.out.println(compile);
    }

    @Test
    public void test_complexModelByGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModelByGetter.java"));

        Assertions.assertEquals(compile.status(), Compilation.Status.SUCCESS);
        Assertions.assertEquals(compile.generatedSourceFiles().size(), 2);

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ComplexModelByGetter");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();
    }

    private boolean hasSimpleProprety(String javaFile, String logicalName) {
        return hasSimpleProprety(javaFile, logicalName, logicalName);
    }

    private boolean hasSimpleProprety(String javaFile, String logicalName, String name) {
        boolean hasStaticPropertyField = javaFile.contains(String.format("private static final String PROPERTY_%s = \"%s\"", StringUtils.upperSnakeCase(logicalName), name));
        boolean hasMethod = javaFile.contains(String.format("public String %s()", logicalName));
        return hasStaticPropertyField && hasMethod;
    }
}