package com.github.gauthierj.metamodel.classbuilder;

import org.ainslec.picocog.PicoWriter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;

public abstract class _ClassBuilderImpl<T extends _ClassBuilder<T>> implements _ClassBuilder<T> {

    private boolean hasField = false;
    private boolean hasMember = false;

    private final PicoWriter topWriter;
    private final PicoWriter importWriter;
    private final PicoWriter fieldWriter;
    private final PicoWriter constructorWriter;
    private final PicoWriter methodWriter;
    private final PicoWriter innerClassWriter;

    private final String className;

    protected  _ClassBuilderImpl(Collection<Modifier> modifiers,
                                 String packageNameOpt,
                                 String className,
                                 String superClassOpt,
                                 String ... interfacesOpt) {
        this(new PicoWriter("    "), modifiers, packageNameOpt, className, superClassOpt, interfacesOpt);
    }

    protected  _ClassBuilderImpl(PicoWriter topWriter,
                                 Collection<Modifier> modifiers,
                                 String packageNameOpt,
                                 String className,
                                 String superClassOpt,
                                 String ... interfacesOpt) {
        this.className = className;
        this.topWriter = topWriter.createDeferredWriter();
        Optional.ofNullable(packageNameOpt)
                .ifPresent(pckg -> this.topWriter.writeln("package " + packageNameOpt + ";")
                        .writeln(""));
        importWriter = this.topWriter.createDeferredWriter();
        String extendsClause = StringUtils.isNotBlank(superClassOpt) ? " extends " + superClassOpt : "";
        String implementsClause = Optional.ofNullable(interfacesOpt)
                .filter(interfs -> interfs.length > 0)
                .map(interfs -> StringUtils.toString(Arrays.asList(interfs), ", "))
                .map(interfs -> " implements " + interfs)
                .orElse("");
        this.topWriter.writeln(StringUtils.toString(requireNonNullElse(modifiers, List.of()), " ") + " class " + className + extendsClause + implementsClause + " {");
        this.topWriter.writeln("");
        this.topWriter.indentRight();
        fieldWriter = this.topWriter.createDeferredWriter();
        constructorWriter = this.topWriter.createDeferredWriter();
        methodWriter = this.topWriter.createDeferredWriter();
        innerClassWriter = this.topWriter.createDeferredWriter();
        this.topWriter.indentLeft();
        this.topWriter.writeln("}");
    }

    @Override
    @SuppressWarnings("unchecked")
    public T field(Collection<Modifier> modifiers, String type, String name, String initializerOpt) {
        this.hasField = true;
        String initializer = Optional.ofNullable(initializerOpt)
                .map(init -> " = " + init)
                .orElse("");
        fieldWriter.writeln(StringUtils.toString(modifiers, " ") + " " + type + " " + name + initializer + ";");
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MethodSignatureBuilder<T> constructor(Collection<Modifier> modifiers) {
        if(hasMember()) {
            this.constructorWriter.writeln("");
        }
        return new MethodSignatureBuilderImpl<>((T) this, constructorWriter.createDeferredWriter(), modifiers, null, className);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MethodSignatureBuilder<T> method(Collection<Modifier> modifiers, String returnType, String name) {
        if(hasMember()) {
            this.methodWriter.writeln("");
        }
        return new MethodSignatureBuilderImpl<>((T) this, methodWriter.createDeferredWriter(), modifiers, returnType, name);
    }

    protected boolean hasMember() {
        if(hasMember) {
            hasMember = true;
            return true;
        }
        hasMember = true;
        return false;
    }

    protected void doBuild() {
        if(hasField) {
            this.fieldWriter.writeln("");
        }
    }

    protected PicoWriter topWriter() {
        return topWriter;
    }

    protected PicoWriter importWriter() {
        return importWriter;
    }

    protected PicoWriter innerClassWriter() {
        return innerClassWriter;
    }
}
