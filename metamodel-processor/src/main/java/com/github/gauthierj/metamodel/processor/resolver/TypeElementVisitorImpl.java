package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.Model;
import com.github.gauthierj.metamodel.annotation.Property;
import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.SimplePropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.UnsupportedPropertyInformationImpl;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class TypeElementVisitorImpl implements TypeElementVisitor {

    private final Types types;
    private final Elements elements;
    private final PropertyNameProvider propertyNameProvider;
    private final TypesUtil typesUtil;
    private final PropertyAccessMode propertyAccessMode;

    public TypeElementVisitorImpl(Types types,
                                  Elements elements,
                                  PropertyAccessMode propertyAccessMode) {
        this.types = types;
        this.elements = elements;
        this.propertyAccessMode = propertyAccessMode;
        this.propertyNameProvider = new PropertyNameProvider();
        this.typesUtil = new TypesUtil(types, elements);
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
    public Set<PropertyInformation> visitVariable(VariableElement e, TypeElementVisitorContext context) {
        if (propertyAccessMode.equals(PropertyAccessMode.FIELD) && !isIgnored(e)) {
            return Set.of(getPropertyInformationFromActualType(
                    typesUtil.getActualType(e.asType()),
                    propertyNameProvider.getPropertyName(e),
                    e.getSimpleName().toString(),
                    getPropertyAccesMode(e),
                    getGetterPattern(e),
                    context));
        }
        return Set.of();
    }

    @Override
    public Set<PropertyInformation> visitExecutable(ExecutableElement e, TypeElementVisitorContext context) {
        if (propertyAccessMode.equals(PropertyAccessMode.GETTER) && !isIgnored(e) && isGetter(e, context)) {
            return Set.of(getPropertyInformationFromActualType(
                    typesUtil.getActualType(e.getReturnType()),
                    propertyNameProvider.getPropertyName(e),
                    StringUtils.getGetterPropertyName(e.getSimpleName().toString()),
                    getPropertyAccesMode(e),
                    getGetterPattern(e),
                    context));
        }
        return Set.of();
    }

    private PropertyInformation getPropertyInformationFromActualType(TypeMirror actualType,
                                                                     String name,
                                                                     String logicalName,
                                                                     PropertyAccessMode accessMode,
                                                                     String getterPattern,
                                                                     TypeElementVisitorContext context) {
        System.out.println(String.format("Property - name: %s, logicalName: %s", name, logicalName));
        if (typesUtil.isSimpleType(actualType)) {
            return SimplePropertyInformationImpl.of(name, logicalName);
        }
        return Optional.of(actualType)
                .filter(t -> t.getKind().equals(TypeKind.DECLARED))
                .map(t -> (DeclaredType) t)
                .map(type -> type.asElement())
                .filter(elm -> elm instanceof TypeElement)
                .map(elm -> (TypeElement) elm)
                .map(te -> Optional.ofNullable(context.getResolvedTypes().get(te.getQualifiedName().toString() + "_" + accessMode.toString()))
                            .map(typeInformation -> (PropertyInformation) StructuredPropertyInformationImpl.of(name, logicalName, typeInformation))
                            .orElseGet(() -> UnresolvedTypePropertyInformation.of(
                                    te.getQualifiedName().toString(),
                                    name,
                                    logicalName,
                                    accessMode, getterPattern)))
                .orElseGet(() -> UnsupportedPropertyInformationImpl.of(name, logicalName, types.asElement(actualType).getSimpleName().toString()));
    }

    private boolean isGetter(ExecutableElement e, TypeElementVisitorContext context) {
        return e.getParameters().isEmpty() // No params
                && e.getModifiers().contains(Modifier.PUBLIC) // Public method
                && !e.getReturnType().getKind().equals(TypeKind.VOID) // Does not return void
                && e.getSimpleName().toString().matches(context.getGetterPattern()); // Matches provided pattern;
    }

    private PropertyAccessMode getPropertyAccesMode(Element e) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.accessMode())
                .orElse(Model.DEFAULT_ACCESS_MODE);
    }

    private String getGetterPattern(Element e) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Model.class))
                .map(model -> model.getterPattern())
                .orElse(Model.DEFAULT_GETTER_PATTERN);
    }

    private boolean isIgnored(Element e) {
        return Optional.of(e)
                .map(elm -> elm.getAnnotation(Property.class))
                .map(property -> property.ignore())
                .orElse(false);
    }
}
