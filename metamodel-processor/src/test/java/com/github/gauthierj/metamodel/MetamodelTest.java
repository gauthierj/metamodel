package com.github.gauthierj.metamodel;

import com.github.gauthierj.metamodel.generator.util.StringUtils;
import com.github.gauthierj.metamodel.processor.ModelProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
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

        assertTrue(hasSimpleProperty(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anIntProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringListProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringArray"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPropertyWithADifferentName", "someDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anEnumProperty"));
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

        assertTrue(hasSimpleProperty(simpleModelByGetter, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "anIntProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aStringListProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aStringArray"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "aPropertyWithAnotherName", "someDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByGetter, "anEnumProperty"));
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

        assertTrue(hasSimpleProperty(simpleModelByField, "getAStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getAnIntProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "isAPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getABooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getAStringListProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getAStringArray"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getAStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "getAPropertyWithAnotherName", "someDifferentName"));
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

        assertTrue(hasSimpleProperty(modelClass, "aSimpleProperty"));
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

        assertTrue(hasSimpleProperty(modelClass, "aSimpleProperty"));
        assertTrue(hasComplexProperty(modelClass, "aStructuredProperty", "_OtherSimpleModelByGetter"));
        assertTrue(hasComplexProperty(modelClass, "aListStructuredProperty", "_OtherSimpleModelByField"));
    }

    @Test
    public void test_complexModelByField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ComplexModelByField.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(2, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ComplexModelByField");
        String modelClass = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(modelClass, "aSimpleProperty"));
        assertTrue(hasComplexProperty(modelClass, "aStructuredProperty", "_SimpleModel"));
        assertTrue(hasComplexProperty(modelClass, "aListStructuredProperty", "_SimpleModel"));
    }

    @Test
    public void test_ignoredField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ModelWithIgnoredField.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ModelWithIgnoredField");
        String source = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(source, "aVisibleProperty"));
        assertFalse(hasSimpleProperty(source, "anIgnoredProperty"));
    }

    @Test
    public void test_ignoredGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ModelWithIgnoredGetter.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ModelWithIgnoredGetter");
        String source = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(source, "aVisibleProperty"));
        assertFalse(hasSimpleProperty(source, "anIgnoredProperty"));
    }

    @Test
    public void test_optionalProperty() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ModelWithOptionalProperty.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(2, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._ModelWithOptionalProperty");
        String source = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(source, "anOptionalStringProperty"));
        assertTrue(hasComplexProperty(source, "anOptionalStructuredProperty", "_SimpleModel"));
    }

    @Test
    public void test_customGeneratedClassName() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/ModelWithCustomGeneratedClassName.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._CustomModelName");
        String source = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(source, "aProperty"));
        assertTrue(hasSimpleProperty(source, "anotherProperty"));
    }

    @Test
    public void test_circularReference() {
        // Regression test for issue #1: circular references (A references B, B references A)
        // should not cause an infinite loop in TypeInformationResolver
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            Compilation compile = Compiler.javac()
                    .withProcessors(new ModelProcessor())
                    .compile(
                            JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/CircularA.java"),
                            JavaFileObjects.forResource("com/github/gauthierj/metamodel/processor/model/CircularB.java"));

            assertEquals(Compilation.Status.SUCCESS, compile.status());
            assertEquals(2, compile.generatedSourceFiles().size());

            String circularA = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._CircularA")
                    .orElseThrow().getCharContent(true).toString();
            String circularB = compile.generatedSourceFile("com.github.gauthierj.metamodel.processor.model._CircularB")
                    .orElseThrow().getCharContent(true).toString();

            assertTrue(hasSimpleProperty(circularA, "aSimpleProperty"));
            assertTrue(hasComplexProperty(circularA, "b", "_CircularB"));
            assertTrue(hasSimpleProperty(circularB, "anotherSimpleProperty"));
            assertTrue(hasComplexProperty(circularB, "a", "_CircularA"));
        });
    }

    private boolean hasSimpleProperty(String javaFile, String logicalName) {
        return hasSimpleProperty(javaFile, logicalName, logicalName);
    }

    private boolean hasSimpleProperty(String javaFile, String logicalName, String name) {
        boolean hasStaticPropertyField = javaFile.contains(String.format("private static final String PROPERTY_%s = \"%s\"", StringUtils.upperSnakeCase(logicalName), name));
        boolean hasMethod = javaFile.contains(String.format("public String %s()", logicalName));
        return hasStaticPropertyField && hasMethod;
    }

    private boolean hasComplexProperty(String javaFile, String logicalName, String generatedClassName) {
        return javaFile.contains(String.format("public %s %s()", generatedClassName, logicalName));
    }
}