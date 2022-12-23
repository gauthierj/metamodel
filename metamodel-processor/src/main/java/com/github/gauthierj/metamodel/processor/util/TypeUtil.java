package com.github.gauthierj.metamodel.processor.util;

import javax.lang.model.element.Element;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeUtil {

    private final Types types;
    private final Set<TypeMirror> typeWrapperTypes;
    private final Set<TypeMirror> simpleTypes;
    private final TypeMirror mapType;

    public TypeUtil(Types types, Elements elements) {
        this.types = types;

        this.typeWrapperTypes = Stream.of(
                        Optional.class,
                        OptionalDouble.class,
                        OptionalInt.class,
                        OptionalLong.class,
                        Collection.class)
                .map(cls -> elements.getTypeElement(cls.getCanonicalName()))
                .map(Element::asType)
                .map(types::erasure)
                .collect(Collectors.toSet());

        this.simpleTypes = Stream.of(
                        String.class,
                        Number.class,
                        Boolean.class,
                        Byte.class,
                        Character.class,
                        TemporalAccessor.class,
                        Enum.class,
                        Date.class,
                        java.sql.Date.class)
                .map(cls -> elements.getTypeElement(cls.getCanonicalName()))
                .map(Element::asType)
                .collect(Collectors.toSet());

        this.mapType = elements.getTypeElement(Map.class.getCanonicalName()).asType();
    }

    public boolean isMap(TypeMirror typeMirror) {
        return typeMirror.getKind().equals(TypeKind.DECLARED)
            && types.isSubtype(typeMirror, mapType);
    }

    public boolean isSimpleType(TypeMirror typeMirror) {
        if (typeMirror.getKind().isPrimitive()) {
            return true;
        }
        switch (typeMirror.getKind()) {
            case ARRAY:
                return isSimpleType(((ArrayType) typeMirror).getComponentType());
            case DECLARED:
                return isSubTypeOfSimpleType((DeclaredType) typeMirror);
            default:
                return false;
        }
    }

    public TypeMirror getActualType(TypeMirror typeMirror) {
        if (typeMirror.getKind().isPrimitive()) {
            return typeMirror;
        }
        switch (typeMirror.getKind()) {
            case ARRAY: return ((ArrayType) typeMirror).getComponentType();
            case DECLARED: return isSubTypeOfTypeWrapper((DeclaredType) typeMirror) ? getTypeWrapperWrappedType((DeclaredType) typeMirror) : typeMirror;
            default: return typeMirror;
        }
    }

    private boolean isSubTypeOfSimpleType(DeclaredType declaredType) {
        return simpleTypes.stream()
                .anyMatch(simpleType -> types.isSubtype(declaredType, simpleType));
    }

    private TypeMirror getTypeWrapperWrappedType(DeclaredType declaredType) {
        return declaredType.getTypeArguments().get(0);
    }

    private boolean isSubTypeOfTypeWrapper(DeclaredType declaredType) {
        return typeWrapperTypes.stream().anyMatch(typeWrapper -> types.isSubtype(types.erasure(declaredType), typeWrapper));
    }
}
