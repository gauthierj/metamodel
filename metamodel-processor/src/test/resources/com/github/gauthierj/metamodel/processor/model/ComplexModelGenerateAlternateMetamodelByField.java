package com.github.gauthierj.metamodel.processor.model;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;

import java.util.List;

@Model
public class ComplexModelGenerateAlternateMetamodelByField {

    private final String aSimpleProperty;

    @Model(accessMode = PropertyAccessMode.GETTER, generatedClassName = "_OtherSimpleModelByGetter")
    private final SimpleModel aStructuredProperty;

    @Model(accessMode = PropertyAccessMode.FIELD, generatedClassName = "_OtherSimpleModelByField")
    private final List<SimpleModel> aListStructuredProperty;

    public ComplexModelGenerateAlternateMetamodelByField(String aSimpleProperty,
                        SimpleModel aStructuredProperty,
                        List<SimpleModel> aListStructuredProperty) {
        this.aSimpleProperty = aSimpleProperty;
        this.aStructuredProperty = aStructuredProperty;
        this.aListStructuredProperty = aListStructuredProperty;
    }

    public String getASimpleProperty() {
        return aSimpleProperty;
    }

    public SimpleModel getAStructuredProperty() {
        return aStructuredProperty;
    }

    public List<SimpleModel> getAListStructuredProperty() {
        return aListStructuredProperty;
    }
}