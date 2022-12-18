package com.github.gauthierj.metamodel.classbuilder;

import org.ainslec.picocog.PicoWriter;

public class MethodBodyBuilderImpl<T extends _ClassBuilder<T>> implements MethodBodyBuilder<T> {

    private final T parent;
    private final PicoWriter methodBodyWriter;

    protected MethodBodyBuilderImpl(T parent, PicoWriter methodBodyWriter) {
        this.parent = parent;
        this.methodBodyWriter = methodBodyWriter;
    }

    @Override
    public MethodBodyBuilder<T> writeln(String line) {
        methodBodyWriter.writeln(line);
        return this;
    }

    @Override
    public MethodBodyBuilder<T> writeln_indentRight(String line) {
        methodBodyWriter.writeln_r(line);
        return this;
    }

    @Override
    public MethodBodyBuilder<T> indentLeft_writeln_indentRight(String line) {
        methodBodyWriter.writeln_lr(line);
        return this;
    }

    @Override
    public MethodBodyBuilder<T> indentLeft_writeln(String line) {
        methodBodyWriter.writeln_l(line);
        return this;
    }

    @Override
    public T build() {
        return parent;
    }
}
