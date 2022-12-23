package com.github.gauthierj.metamodel.processor.writer;

import com.github.gauthierj.metamodel.generator.MetamodelGenerator;
import com.github.gauthierj.metamodel.generator.MetamodelGeneratorImpl;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class GeneratedClassWriter {

    private final Filer filer;
    private final MetamodelGenerator metamodelGenerator;

    public GeneratedClassWriter(Filer filer, Messager messager) {
        this.filer = filer;
        this.metamodelGenerator = new MetamodelGeneratorImpl(messager);
    }

    public void writeGeneratedClasses(List<TypeInformation> typeInformations) {
        for (TypeInformation typeInformation : typeInformations) {
            String content = metamodelGenerator.generateMetaModel(typeInformation);
            try(Writer fileWriter = filer.createSourceFile(typeInformation.fullyQualifiedGeneratedClassName()).openWriter()) {
                fileWriter.write(content);
            } catch (IOException e) {
                throw new IllegalStateException("Could not write source file", e);
            }
        }
    }
}
