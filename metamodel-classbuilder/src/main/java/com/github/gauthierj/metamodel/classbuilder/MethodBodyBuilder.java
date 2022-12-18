package com.github.gauthierj.metamodel.classbuilder;

public interface MethodBodyBuilder<T extends _ClassBuilder<T>> {

    MethodBodyBuilder<T> writeln(String line);

    MethodBodyBuilder<T> writeln_indentRight(String line);

    MethodBodyBuilder<T> indentLeft_writeln_indentRight(String line);

    MethodBodyBuilder<T> indentLeft_writeln(String line);

    T build();
}
