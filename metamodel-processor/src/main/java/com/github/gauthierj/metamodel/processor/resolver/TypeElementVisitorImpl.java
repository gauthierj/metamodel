package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.SimplePropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.model.TypeInformationImpl;
import com.github.gauthierj.metamodel.generator.model.UnsupportedPropertyInformationImpl;
import com.github.gauthierj.metamodel.model._Model;
import com.github.gauthierj.metamodel.processor.util.StringUtils;
import com.github.gauthierj.metamodel.processor.util.TypeUtil;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static com.github.gauthierj.metamodel.processor.util.ElementUtil.*;

public class TypeElementVisitorImpl implements TypeElementVisitor {

    private static final TypeInformation _MODEL_TYPE_INFORMATION = TypeInformationImpl.of(
            _Model.class.getPackageName(),
            _Model.class.getSimpleName());

    private final Types types;
    private final Elements elements;
    private final PropertyNameProvider propertyNameProvider;
    private final TypeUtil typeUtil;

    public TypeElementVisitorImpl(Types types,
                                  Elements elements) {
        this.types = types;
        this.elements = elements;
        this.propertyNameProvider = new PropertyNameProvider();
        this.typeUtil = new TypeUtil(types, elements);
    }

    @Override
    public Set<PropertyInformation> visitType(TypeElement e, TypeElementVisitorContext context) {
        Set<PropertyInformation> propertyInformations = new LinkedHashSet<>();
        Optional.ofNullable(e.getSuperclass())
                .map(types::asElement)
                .filter(elm -> !((TypeElement) elm).getQualifiedName().contentEquals(Object.class.getCanonicalName()))
                .ifPresent(typeElement -> propertyInformations.addAll(typeElement.accept(this, context)));
        e.getInterfaces().stream()
                .map(tm -> types.asElement(tm))
                .forEach(elm -> propertyInformations.addAll(elm.accept(this, context)));
        e.getEnclosedElements().forEach(elm -> propertyInformations.addAll(elm.accept(this, context)));
        return propertyInformations;
    }

    @Override
    public Set<PropertyInformation> visitVariable(VariableElement variableElement, TypeElementVisitorContext context) {
        if (shouldVisitVariable(variableElement, context)) {
            return Set.of(getPropertyInformation(
                    variableElement,
                    typeUtil.getActualType(variableElement.asType()),
                    propertyNameProvider.getPropertyName(variableElement),
                    variableElement.getSimpleName().toString(),
                    context));
        }
        return Set.of();
    }

    @Override
    public Set<PropertyInformation> visitExecutable(ExecutableElement executableElement, TypeElementVisitorContext context) {
        if (shouldVisitExecutable(executableElement, context)) {
            String getterPattern = getGetterPattern(executableElement, context.getGetterPattern());
            return Set.of(getPropertyInformation(
                    executableElement,
                    typeUtil.getActualType(executableElement.getReturnType()),
                    propertyNameProvider.getPropertyName(executableElement, getterPattern),
                    StringUtils.getGetterPropertyName(executableElement.getSimpleName().toString(), getterPattern),
                    context));
        }
        return Set.of();
    }

    private static boolean shouldVisitVariable(VariableElement e, TypeElementVisitorContext context) {
        return PropertyAccessMode.FIELD.equals(context.propertyAccessMode())
                && !isIgnored(e);
    }

    private boolean shouldVisitExecutable(ExecutableElement e, TypeElementVisitorContext context) {
        return PropertyAccessMode.GETTER.equals(context.propertyAccessMode())
                && !isIgnored(e)
                && isGetter(e, context.getGetterPattern());
    }

    private PropertyInformation getPropertyInformation(Element element,
                                                       TypeMirror actualType,
                                                       String name,
                                                       String logicalName,
                                                       TypeElementVisitorContext context) {
        if (typeUtil.isSimpleType(actualType)) {
            return SimplePropertyInformationImpl.of(name, logicalName);
        }
        if (typeUtil.isMap(actualType)) {
            return StructuredPropertyInformationImpl.of(
                    name,
                    logicalName,
                    _MODEL_TYPE_INFORMATION);
        }
        return getTypeElement(actualType)
                .map(actualTypeElement -> getPropertyInformation(element, actualTypeElement, name, logicalName, context))
                .orElseGet(() -> UnsupportedPropertyInformationImpl.of(
                        name,
                        logicalName,
                        element.getEnclosingElement().getSimpleName().toString()));
    }

    private Optional<TypeElement> getTypeElement(TypeMirror actualType) {
        return Optional.of(actualType)
                .filter(t -> t.getKind().equals(TypeKind.DECLARED))
                .map(t -> (DeclaredType) t)
                .map(type -> type.asElement())
                .filter(elm -> elm instanceof TypeElement)
                .map(elm -> (TypeElement) elm);
    }

    private PropertyInformation getPropertyInformation(Element element,
                                                       TypeElement actualTypeElement,
                                                       String name,
                                                       String logicalName,
                                                       TypeElementVisitorContext context) {

        PropertyAccessMode propertyAccesMode = getPropertyAccesMode(element, context.propertyAccessMode());
        String getterPattern = getGetterPattern(element, context.getGetterPattern());
        String generatedClassName = getGeneratedClassName(element, actualTypeElement);

        return Optional.of(context.getResolvedTypes())
                .map(resolvedTypes -> resolvedTypes.get(TypeInformationKey.of(actualTypeElement.getQualifiedName().toString(), generatedClassName)))
                .map(resolvedType -> (PropertyInformation) StructuredPropertyInformationImpl.of(
                        name,
                        logicalName,
                        resolvedType))
                .orElseGet(() -> UnresolvedTypePropertyInformation.of(
                        actualTypeElement.getQualifiedName().toString(),
                        name,
                        logicalName,
                        propertyAccesMode,
                        getterPattern,
                        generatedClassName));
    }
}
