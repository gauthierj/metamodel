package com.github.gauthierj.metamodel.processor.resolver;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.Optional;

public interface PropertyNameResolver {

    Optional<String> resolve(VariableElement variableElement);

    Optional<String> resolve(ExecutableElement variableElement);
}
