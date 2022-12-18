package com.github.gauthierj.metamodel.classbuilder;

import java.util.Arrays;
import java.util.Collection;

public interface ClassBuilder extends _ClassBuilder<ClassBuilder> {

    static ClassBuilder of(Collection<Modifier> modifiers,
                           String packageName,
                           String name) {
        return new ClassBuilderImpl(modifiers, packageName, name, null);
    }

    static ClassBuilder of(Collection<Modifier> modifiers,
                           String packageName,
                           String name,
                           String superClassOpt,
                           String ... interfacesOpt) {
        return new ClassBuilderImpl(modifiers, packageName, name, superClassOpt, interfacesOpt);
    }

    static ClassBuilder of(Collection<Modifier> modifiers,
                           String packageName,
                           String name,
                           Class<?> superClassOpt,
                           Class<?> ... interfacesOpt) {
        return new ClassBuilderImpl(
                modifiers,
                packageName,
                name,
                superClassOpt != null ? superClassOpt.getSimpleName() : null,
                interfacesOpt != null ? Arrays.stream(interfacesOpt)
                        .map(Class::getSimpleName)
                        .toArray(String[]::new) : null);
    }

    default ClassBuilder addImport(Class<?> type) {
        return addImport(type.getCanonicalName());
    }

    ClassBuilder addImport(String type);
    InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name);

    InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name, String superClassOpt, String... interfacesOpt);

    String build();

}
