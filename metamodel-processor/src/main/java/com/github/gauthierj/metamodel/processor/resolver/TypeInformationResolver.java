package com.github.gauthierj.metamodel.processor.resolver;

import com.github.gauthierj.metamodel.annotation.PropertyAccessMode;
import com.github.gauthierj.metamodel.generator.model.PropertyInformation;
import com.github.gauthierj.metamodel.generator.model.StructuredPropertyInformationImpl;
import com.github.gauthierj.metamodel.generator.model.TypeInformation;
import com.github.gauthierj.metamodel.generator.model.UnsupportedPropertyInformationImpl;
import com.github.gauthierj.metamodel.processor.util.ElementUtil;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TypeInformationResolver {

    private final Elements elements;
    private final TypeElementVisitorImpl typeElementVisitor;
    private final Map<TypeInformationKey, TypeInformationImpl> resolvedTypeInformation = new ConcurrentHashMap<>();

    public TypeInformationResolver(Types types, Elements elements) {
        this.elements = elements;
        typeElementVisitor = new TypeElementVisitorImpl(types, elements);
    }

    public List<TypeInformation> resolveTypeInformations(List<TypeElement> annotatedTypeElements) {
        annotatedTypeElements.forEach(this::resolveTypeElement);
        while (resolvedTypeInformation.values().stream()
                .anyMatch(TypeInformationImpl::hasUnresolvedPropertyInformation)) {
            for (TypeInformationImpl value : resolvedTypeInformation.values()) {
                resoloveUnresolvedTypeInformation(value);
            }
        }
        return List.copyOf(resolvedTypeInformation.values());
    }

    private void resoloveUnresolvedTypeInformation(TypeInformationImpl typeInformationImpl) {
        typeInformationImpl.withProperties(
                typeInformationImpl.properties().stream()
                        .map(propertyInformation -> {
                            if(propertyInformation instanceof UnresolvedTypePropertyInformation) {
                                return resolveTypeInformation((UnresolvedTypePropertyInformation) propertyInformation);
                            }
                            return propertyInformation;
                        }).collect(Collectors.toList()));
    }

    private PropertyInformation resolveTypeInformation(UnresolvedTypePropertyInformation unresolvedTypePI) {
        TypeInformationKey key = TypeInformationKey.of(unresolvedTypePI.type(), unresolvedTypePI.generatedClassName());
        if(resolvedTypeInformation.containsKey(key)) {
            TypeInformationImpl ti = resolvedTypeInformation.get(key);
            return StructuredPropertyInformationImpl.of(unresolvedTypePI.name(), unresolvedTypePI.logicalName(), ti);
        }
        return Optional.of(unresolvedTypePI)
                .map(pi -> elements.getTypeElement(pi.type()))
                .map(te -> resolveTypeElement(te, unresolvedTypePI.propertyAccessMode(), unresolvedTypePI.getterPattern(), unresolvedTypePI.generatedClassName()))
                .map(ti -> (PropertyInformation) StructuredPropertyInformationImpl.of(unresolvedTypePI.name(), unresolvedTypePI.logicalName(), ti))
                .orElseGet(() -> UnsupportedPropertyInformationImpl.of(unresolvedTypePI.name(), unresolvedTypePI.logicalName(), unresolvedTypePI.type()));
    }

    private TypeInformationImpl resolveTypeElement(TypeElement typeElement) {
        return resolveTypeElement(
                typeElement,
                ElementUtil.getPropertyAccesMode(typeElement),
                ElementUtil.getGetterPattern(typeElement),
                ElementUtil.getGeneratedClassName(typeElement)
        );
    }

    private TypeInformationImpl resolveTypeElement(TypeElement typeElement,
                                                   PropertyAccessMode propertyAccessMode,
                                                   String getterPattern,
                                                   String generatedClassName) {

        TypeInformationImpl typeInformation = doResolveTypeElement(
                typeElement,
                propertyAccessMode,
                getterPattern,
                generatedClassName);

        resolvedTypeInformation.put(TypeInformationKey.of(typeInformation.fullyQualifiedName(), typeInformation.generatedClassName()), typeInformation);

        return typeInformation;
    }

    private TypeInformationImpl doResolveTypeElement(TypeElement typeElement,
                                                     PropertyAccessMode propertyAccessMode,
                                                     String getterPattern,
                                                     String generatedClassName) {

        String qualifiedName = typeElement.getQualifiedName().toString();
        int lastDotIndex = qualifiedName.lastIndexOf('.');
        String packageName = lastDotIndex > 0 ? qualifiedName.substring(0, lastDotIndex) : "";
        String className = lastDotIndex > 0 ? qualifiedName.substring(lastDotIndex + 1) : qualifiedName;

        Set<PropertyInformation> propertyInformations = typeElement.accept(
                typeElementVisitor,
                TypeElementVisitorContext.of(
                        resolvedTypeInformation,
                        propertyAccessMode,
                        getterPattern));

        return TypeInformationImpl.of(packageName, className, generatedClassName, propertyAccessMode)
                .withProperties(propertyInformations);
    }
}
