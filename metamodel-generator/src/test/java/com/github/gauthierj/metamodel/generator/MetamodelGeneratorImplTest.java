package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.generator.model.SimplePropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.processing.Messager;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
}