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

class MongoMetamodelTest {

    @Test
    public void test_simpleModelByField() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("org/springframework/data/mongodb/core/mapping/Document.java"),
                        JavaFileObjects.forResource("org/springframework/data/mongodb/core/mapping/Field.java"),
                        JavaFileObjects.forResource("org/springframework/data/annotation/Id.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/mongo/extension/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/mongo/extension/model/SimpleModelByField.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.mongo.extension.model._SimpleModelByField");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anIntProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringListProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringArray"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anIdProperty", "_id"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPropertyWithADifferentName", "someDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anotherPropertyWithADifferentName", "someOtherDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPropertyThatKeepsOriginalName"));
        assertTrue(hasComplexProperty(simpleModelByField, "aMapProperty", "_Model"));
    }

    @Test
    public void test_simpleModelByGetter() throws IOException {
        Compilation compile = Compiler.javac()
                .withProcessors(new ModelProcessor())
                .compile(JavaFileObjects.forResource("org/springframework/data/mongodb/core/mapping/Document.java"),
                        JavaFileObjects.forResource("org/springframework/data/mongodb/core/mapping/Field.java"),
                        JavaFileObjects.forResource("org/springframework/data/annotation/Id.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/mongo/extension/model/SimpleModel.java"),
                        JavaFileObjects.forResource("com/github/gauthierj/metamodel/mongo/extension/model/SimpleModelByGetter.java"));

        assertEquals(Compilation.Status.SUCCESS, compile.status());
        assertEquals(1, compile.generatedSourceFiles().size());

        Optional<JavaFileObject> javaFileObject = compile.generatedSourceFile("com.github.gauthierj.metamodel.mongo.extension.model._SimpleModelByGetter");
        String simpleModelByField = javaFileObject.orElseThrow().getCharContent(true).toString();

        assertTrue(hasSimpleProperty(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anIntProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPrimitiveBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aBooleanProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringListProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringArray"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aStringProperty"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anIdProperty", "_id"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPropertyWithAnotherName", "someDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByField, "anotherPropertyWithADifferentName", "someOtherDifferentName"));
        assertTrue(hasSimpleProperty(simpleModelByField, "aPropertyThatKeepsOriginalName"));
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