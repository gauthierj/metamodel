package com.github.gauthierj.metamodel.processor.util;

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
    private final Set<TypeMirror> TYPE_WRAPPER_TYPES;
    private final Set<TypeMirror> SIMPLE_TYPES;

    private final TypeMirror MAP_TYPE;

    public TypeUtil(Types types, Elements elements) {
        this.types = types;

        this.TYPE_WRAPPER_TYPES = Stream.of(
                        Optional.class,
                        OptionalDouble.class,
                        OptionalInt.class,
                        OptionalLong.class,
                        Collection.class)
                .map(cls -> elements.getTypeElement(cls.getCanonicalName()))
                .map(typeElement -> typeElement.asType())
                .map(typeMirror -> types.erasure(typeMirror))
                .collect(Collectors.toSet());

        this.SIMPLE_TYPES = Stream.of(
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
                .map(typeElement -> typeElement.asType())
                .collect(Collectors.toSet());

        this.MAP_TYPE = elements.getTypeElement(Map.class.getCanonicalName()).asType();
    }

    public boolean isMap(TypeMirror typeMirror) {
        return typeMirror.getKind().equals(TypeKind.DECLARED)
            && types.isSubtype(typeMirror, MAP_TYPE);
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
        return SIMPLE_TYPES.stream()
                .anyMatch(simpleType -> types.isSubtype(declaredType, simpleType));
    }

    private TypeMirror getTypeWrapperWrappedType(DeclaredType declaredType) {
        return declaredType.getTypeArguments().get(0);
    }

    private boolean isSubTypeOfTypeWrapper(DeclaredType declaredType) {
        return TYPE_WRAPPER_TYPES.stream().anyMatch(typeWrapper -> types.isSubtype(types.erasure(declaredType), typeWrapper));
    }
}
