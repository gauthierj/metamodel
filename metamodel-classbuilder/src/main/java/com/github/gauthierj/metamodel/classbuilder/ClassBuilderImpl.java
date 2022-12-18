package com.github.gauthierj.metamodel.classbuilder;

import java.util.Collection;

public class ClassBuilderImpl extends _ClassBuilderImpl<ClassBuilder> implements ClassBuilder {

    private boolean hasImport = false;

    protected ClassBuilderImpl(Collection<Modifier> modifiers,
                               String packageName,
                               String name,
                               String superClassOpt,
                               String ... interfacesOpt) {
        super(modifiers, packageName, name, superClassOpt, interfacesOpt);
    }

    @Override
    public ClassBuilder addImport(String type) {
        this.hasImport = true;
        importWriter().writeln("import " + type + ";");
        return this;
    }

    @Override
    public InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name) {
        if(hasMember()) {
            this.innerClassWriter().writeln("");
        }
        return new InnerClassBuilderImpl(this, this.innerClassWriter(), modifiers, name, null);
    }

    @Override
    public InnerClassBuilder innerClass(Collection<Modifier> modifiers, String name, String superClassOpt, String... interfacesOpt) {
        if(hasMember()) {
            this.innerClassWriter().writeln("");
        }
        return new InnerClassBuilderImpl(this, this.innerClassWriter(), modifiers, name, superClassOpt, interfacesOpt);
    }

    @Override
    public String build() {
        super.doBuild();
        if(hasImport) {
            importWriter().writeln("");
        }
        return topWriter().toString().replaceAll("\n\\s+\n", "\n\n");
    }
}
