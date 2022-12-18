package com.github.gauthierj.metamodel.classbuilder;

public interface MethodSignatureBuilder<T extends _ClassBuilder<T>> {

    default MethodSignatureBuilder<T> parameter(Class<?> type, String name) {
        return parameter(type.getSimpleName(), name);
    }

    MethodSignatureBuilder<T> parameter(String type, String name);

    MethodBodyBuilder<T> body();
}
