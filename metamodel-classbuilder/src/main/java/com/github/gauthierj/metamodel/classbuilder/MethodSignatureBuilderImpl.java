package com.github.gauthierj.metamodel.classbuilder;

import org.ainslec.picocog.PicoWriter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodSignatureBuilderImpl<T extends _ClassBuilder<T>> implements MethodSignatureBuilder<T> {

    private final T parent;
    private final PicoWriter methodWriter;
    private final Collection<Modifier> modifiers;
    private final String returnTypeOpt;
    private final String methodName;
    private final Map<String, String> parameters = new LinkedHashMap<>();

    protected MethodSignatureBuilderImpl(T parent,
                                         PicoWriter methodWriter,
                                         Collection<Modifier> modifiers,
                                         String returnTypeOpt,
                                         String methodName) {
        this.parent = parent;
        this.methodWriter = methodWriter.createDeferredWriter();
        this.modifiers = List.copyOf(Optional.ofNullable(modifiers).orElse(List.of()));
        this.returnTypeOpt = returnTypeOpt;
        this.methodName = methodName;
    }


    @Override
    public MethodSignatureBuilder<T> parameter(String type, String name) {
        this.parameters.put(name, type);
        return this;
    }

    @Override
    public MethodBodyBuilder<T> body() {
        String parameters = getParametersString();
        Optional.of(modifiers)
                .filter(mods -> !mods.isEmpty())
                .ifPresent(mods -> methodWriter.write(StringUtils.toString(mods, " ") + " "));
        ;
        Optional.ofNullable(returnTypeOpt).ifPresent(returnType -> methodWriter.write(returnType + " "));
        methodWriter.write(methodName + "(");
        methodWriter.write(parameters);
        methodWriter.writeln_r(") {");
        PicoWriter methodBodyWriter = methodWriter.createDeferredWriter();
        methodWriter.writeln_l("}");
        return new MethodBodyBuilderImpl<>(parent, methodBodyWriter);
    }

    private String getParametersString() {
        return StringUtils.toString(this.parameters.entrySet().stream()
                .map(entry -> entry.getValue() + " " + entry.getKey())
                .collect(Collectors.toList()), ", ");
    }
}
