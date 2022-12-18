package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.generator.model.PropertyInformation;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import java.util.Set;

public interface TypeElementVisitor extends ElementVisitor<Set<PropertyInformation>, TypeElementVisitorContext> {

    @Override
    default Set<PropertyInformation> visit(Element e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitPackage(PackageElement e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitType(TypeElement e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitVariable(VariableElement e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitExecutable(ExecutableElement e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitTypeParameter(TypeParameterElement e, TypeElementVisitorContext context) {
        return Set.of();
    }

    @Override
    default Set<PropertyInformation> visitUnknown(Element e, TypeElementVisitorContext context) {
        return Set.of();
    }
}
