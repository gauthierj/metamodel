package com.github.gauthierj.metamodel;

import com.github.gauthierj.metamodel.generator.util.StringUtils;
import com.github.gauthierj.metamodel.processor.ModelProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MetamodelTest {

    @Test
    public void test_simpleModelByField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModelByField.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._SimpleModelByField");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "anIntProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aBooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aStringListProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aStringArray"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "aPropertyWithADifferentName", "someDifferentName"));
        assertTrue(hasSimpleProprety(simpleModelByField, "anEnumProperty"));
        assertTrue(hasComplexProperty(simpleModelByField, "aMapProperty", "_Model"));
    }

    @Test
    public void test_simpleModelByGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModelByGetter.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._SimpleModelByGetter");
        String simpleModelByGetter = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProprety(simpleModelByGetter, "aStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "anIntProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aBooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aStringListProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aStringArray"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "aPropertyWithAnotherName", "someDifferentName"));
        assertTrue(hasSimpleProprety(simpleModelByGetter, "anEnumProperty"));
    }

    @Test
    public void test_simpleModelByGetterPattern() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModelByGetterPattern.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._SimpleModelByGetterPattern");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProprety(simpleModelByField, "getAStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getAnIntProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "isAPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getABooleanProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getAStringListProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getAStringArray"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getAStringProperty"));
        assertTrue(hasSimpleProprety(simpleModelByField, "getAPropertyWithAnotherName", "someDifferentName"));
    }

    @Test
    public void test_complexModelByGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModelByGetter.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(2, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ComplexModelByGetter");
        String modelClass = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProprety(modelClass, "aSimpleProperty"));
        assertTrue(hasComplexProperty(modelClass, "aStructuredProperty", "_SimpleModel"));
        assertTrue(hasComplexProperty(modelClass, "aListStructuredProperty", "_SimpleModel"));
    }

    @Test
    public void test_complexModelGenerateAlternateMetamodelByField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModelGenerateAlternateMetamodelByField.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(3, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ComplexModelGenerateAlternateMetamodelByField");
        String modelClass = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProprety(modelClass, "aSimpleProperty"));
        assertTrue(hasComplexProperty(modelClass, "aStructuredProperty", "_OtherSimpleModelByGetter"));
        assertTrue(hasComplexProperty(modelClass, "aListStructuredProperty", "_OtherSimpleModelByField"));
    }

    private boolean hasSimpleProprety(String javaFile, String logicalName) {
        return hasSimpleProprety(javaFile, logicalName, logicalName);
    }

    private boolean hasSimpleProprety(String javaFile, String logicalName, String name) {
        boolean hasStaticPropertyField = javaFile.contains(String.format("private static final String PROPERTY_%s = \"%s\"", StringUtils.upperSnakeCase(logicalName), name));
        boolean hasMethod = javaFile.contains(String.format("public String %s()", logicalName));
        return hasStaticPropertyField && hasMethod;
    }

    private boolean hasComplexProperty(String javaFile, String logicalName, String generatedClassName) {
        return javaFile.contains(String.format("public %s %s()", generatedClassName, logicalName));
    }
}