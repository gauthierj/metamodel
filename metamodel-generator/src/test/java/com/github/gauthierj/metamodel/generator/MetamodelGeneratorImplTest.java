package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.generator.model.SimplePropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.TypeInformationImpl;
import com.github.gauthierj.metamodel.generator.model.UnsupportedPropertyInformationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class MetamodelGeneratorImplTest {

    @Test
    void generateMetaModel() throws IOException {
        MetamodelGenerator metamodelGenerator = new MetamodelGeneratorImpl(Mockito.mock(Messager.class));

        TypeInformationImpl typeInformation = TypeInformationImpl.of(
                        "com.github.gauthierj.test",
                        "TestClass")
                .withProperties(
                        SimplePropertyInformationImpl.of("aSimpleProperty", "aSimpleProperty"),
                        SimplePropertyInformationImpl.of("anotherSimpleProperty", "anotherSimpleProperty"),
                        StructuredPropertyInformationImpl.of(
                                "aStructuredProperty",
                                "aStructuredProperty",
                                TypeInformationImpl.of(
                                                "com.github.gauthierj.test.subpackage",
                                                "AnotherTestClass")
                                        .withProperties(
                                                SimplePropertyInformationImpl.of("someSimpleProperty", "someSimpleProperty"),
                                                SimplePropertyInformationImpl.of("yetAnotherSimpleProperty", "yetAnotherSimpleProperty"))));

        String metamodel = metamodelGenerator.generateMetaModel(typeInformation);

        String expectedMetamodel = new String(this.getClass().getResourceAsStream("/com/github/gauthierj/metamodel/generator/_TestClass.java").readAllBytes());

        Assertions.assertEquals(expectedMetamodel, metamodel);
    }

    @Test
    void generateMetaModel_noProperties() {
        MetamodelGenerator generator = new MetamodelGeneratorImpl(Mockito.mock(Messager.class));

        TypeInformationImpl typeInformation = TypeInformationImpl.of("com.test", "EmptyClass");

        String metamodel = generator.generateMetaModel(typeInformation);

        assertTrue(metamodel.contains("public class _EmptyClass extends _Model {"));
        assertTrue(metamodel.contains("private static final _EmptyClass MODEL"));
        assertTrue(metamodel.contains("public _EmptyClass(_Path rootPath)"));
        assertTrue(metamodel.contains("public static _EmptyClass model()"));
        assertFalse(metamodel.contains("private static final String PROPERTY_"));
    }

    @Test
    void generateMetaModel_customGeneratedClassName() {
        MetamodelGenerator generator = new MetamodelGeneratorImpl(Mockito.mock(Messager.class));

        TypeInformationImpl typeInformation = TypeInformationImpl.of("com.test", "MyClass")
                .withGeneratedClassName("_CustomName")
                .withProperties(SimplePropertyInformationImpl.of("aProperty", "aProperty"));

        String metamodel = generator.generateMetaModel(typeInformation);

        assertTrue(metamodel.contains("public class _CustomName extends _Model {"));
        assertTrue(metamodel.contains("private static final _CustomName MODEL"));
        assertTrue(metamodel.contains("public _CustomName(_Path rootPath)"));
    }

    @Test
    void generateMetaModel_unsupportedProperty() {
        Messager messager = Mockito.mock(Messager.class);
        MetamodelGenerator generator = new MetamodelGeneratorImpl(messager);

        TypeInformationImpl typeInformation = TypeInformationImpl.of("com.test", "SomeClass")
                .withProperties(UnsupportedPropertyInformationImpl.of("unsupportedProp", "unsupportedProp", "UnknownType"));

        String metamodel = generator.generateMetaModel(typeInformation);

        Mockito.verify(messager).printMessage(eq(Diagnostic.Kind.WARNING), any(CharSequence.class));
        assertFalse(metamodel.contains("public String unsupportedProp()"));
    }

    @Test
    void generateMetaModel_structuredPropertyInDifferentPackage() {
        MetamodelGenerator generator = new MetamodelGeneratorImpl(Mockito.mock(Messager.class));

        TypeInformationImpl otherType = TypeInformationImpl.of("com.other.pkg", "OtherClass");
        TypeInformationImpl typeInformation = TypeInformationImpl.of("com.test.pkg", "SomeClass")
                .withProperties(StructuredPropertyInformationImpl.of("otherProp", "otherProp", otherType));

        String metamodel = generator.generateMetaModel(typeInformation);

        assertTrue(metamodel.contains("import com.other.pkg._OtherClass;"));
        assertTrue(metamodel.contains("public _OtherClass otherProp()"));
    }
}