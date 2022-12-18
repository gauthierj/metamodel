package com.github.gauthierj.metamodel.generator;

import com.github.gauthierj.metamodel.generator.model.TypeInformation;

public interface MetamodelGenerator {

    String generateMetaModel(TypeInformation typeInformation);

}
