package com.github.gauthierj.metamodel.classbuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassBuilderImpl extends _ClassBuilderImpl<ClassBuilder> implements ClassBuilder {

    private final Set<String> imports = new HashSet<>();

    protected ClassBuilderImpl(Collection<Modifier> modifiers,
                               String packageName,
                               String name,
                               String superClassOpt,
                               String ... interfacesOpt) {
        super(modifiers, packageName, name, superClassOpt, interfacesOpt);
    }

    @Override
    public ClassBuilder addImport(String type) {
        if(this.imports.add(type)) {
            importWriter().writeln("import " + type + ";");
        }
        return this;
    }

    @Override
    public InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name) {
        writeEmptyLineIfNeeded(this.innerClassWriter());
        return new InnerClassBuilderImpl(this, this.innerClassWriter(), modifiers, name, null);
    }

    @Override
    public InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name, String superClassOpt, String... interfacesOpt) {
        writeEmptyLineIfNeeded(this.innerClassWriter());
        return new InnerClassBuilderImpl(this, this.innerClassWriter(), modifiers, name, superClassOpt, interfacesOpt);
    }

    @Override
    public String build() {
        super.doBuild();
        if(!this.imports.isEmpty()) {
            importWriter().writeln("");
        }
        return topWriter().toString().replaceAll("\n\\s+\n", "\n\n");
    }
}
