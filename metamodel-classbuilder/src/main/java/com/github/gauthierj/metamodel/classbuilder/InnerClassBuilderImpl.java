package com.github.gauthierj.metamodel.classbuilder;

import org.ainslec.picocog.PicoWriter;

import java.util.Collection;

public class InnerClassBuilderImpl extends _ClassBuilderImpl<InnerClassBuilder> implements InnerClassBuilder {

    private final ClassBuilder parent;

    protected InnerClassBuilderImpl(ClassBuilder parent,
                                    PicoWriter topWriter,
                                    Collection<Modifier> modifiers,
                                    String name,
                                    String superClassOpt,
                                    String... interfacesOpt) {
        super(topWriter, modifiers, null, name, superClassOpt, interfacesOpt);
        this.parent = parent;
    }

    @Override
    public ClassBuilder build() {
        super.doBuild();
        return parent;
    }
}
