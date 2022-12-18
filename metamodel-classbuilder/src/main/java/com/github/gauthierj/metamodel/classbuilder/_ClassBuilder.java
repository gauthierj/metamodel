package com.github.gauthierj.metamodel.classbuilder;

import java.util.Collection;
import java.util.List;

import static com.github.gauthierj.metamodel.classbuilder.Modifier.*;

public interface _ClassBuilder<T extends _ClassBuilder<T>> {

    default T privateStaticFinalField(String type, String name, String initializer) {
        return field(List.of(PRIVATE, STATIC, FINAL), type, name, initializer);
    }

    default T field(Collection<Modifier> modifiers, String type, String name) {
        return field(modifiers, type, name, null);
    }

    T field(Collection<Modifier> modifiers, String type, String name, String initializerOpt);

    default MethodSignatureBuilder<T> publicConstructor() {
        return constructor(List.of(PUBLIC));
    }

    MethodSignatureBuilder<T> constructor(Collection<Modifier> modifiers);

    default MethodSignatureBuilder<T> publicStaticMethod(String returnType, String name) {
        return method(List.of(PUBLIC, STATIC), returnType, name);
    }

    default MethodSignatureBuilder<T> publicMethod(String returnType, String name) {
        return method(List.of(PUBLIC), returnType, name);
    }

    MethodSignatureBuilder<T> method(Collection<Modifier> modifiers, String returnType, String name);
}
