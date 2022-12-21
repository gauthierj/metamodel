package com.github.gauthierj.metamodel.processor.resolver;


public class TypeInformationKey {

    private final String type;
    private final String generatedClassName;

    private TypeInformationKey(String type, String generatedClassName) {
        this.type = type;
        this.generatedClassName = generatedClassName;
    }

    public static TypeInformationKey of(String type, String generatedClassName) {
        return new TypeInformationKey(type, generatedClassName);
    }

    public String type() {
        return type;
    }

    public String generatedClassName() {
        return generatedClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeInformationKey)) return false;

        TypeInformationKey that = (TypeInformationKey) o;

        if (!type.equals(that.type)) return false;
        if (!generatedClassName.equals(that.generatedClassName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + generatedClassName.hashCode();
        return result;
    }
}
