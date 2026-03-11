# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Java APT (Annotation Processing Tool) that generates metamodel classes for Java classes at compile time. It enables type-safe, refactor-safe access to property names and paths — useful for MongoDB criteria, validation error reporting, etc.

**Java version:** 11
**Build tool:** Maven (multi-module)

## Build & Test Commands

```bash
# Build all modules
./mvnw clean install

# Run tests only
./mvnw test

# Run tests in a specific module
./mvnw test -pl metamodel-processor

# Build with code coverage report
./mvnw clean install -Pcode-coverage

# Build and deploy artifacts to GitHub Maven repo
./mvnw clean deploy -Prelease,github-deploy
```

The processor module uses `<proc>none</proc>` to prevent it from running APT on itself during compilation.

## Module Architecture

```
metamodel-annotation       → @Model, @Property, PropertyAccessMode annotations
metamodel-model            → Runtime classes: _Model (base), _Path
metamodel-classbuilder     → Fluent Java source code builder (ClassBuilder API)
metamodel-generator        → MetamodelGenerator: converts TypeInformation → Java source string
metamodel-processor        → javax APT processor (ModelProcessor extends AbstractProcessor)
metamodel-mongo-extension  → Extension: triggers on @Document (Spring Data MongoDB)
metamodel-integration-test → End-to-end tests using generated metamodel classes
```

### Processing Pipeline

1. **`ModelProcessor`** (APT entry point) — discovers annotated types via `ServiceLoader<ModelAnnotationProvider>`, delegates to:
2. **`TypeInformationResolver`** — inspects `TypeElement` via the Java compiler API, classifies each property as simple/structured/unsupported, produces `TypeInformation` objects.
3. **`MetamodelGeneratorImpl`** — uses `ClassBuilder` to emit the `_ClassName` Java source.
4. **`GeneratedClassWriter`** — writes output via `javax.annotation.processing.Filer`.

### Extensibility via SPI

`ModelAnnotationProvider` is a `ServiceLoader`-discovered SPI. Implementations declare which annotations trigger metamodel generation. The built-in one handles `@Model`; `metamodel-mongo-extension` adds `@Document`. New extensions register via `META-INF/services/com.github.gauthierj.metamodel.processor.ModelAnnotationProvider`.

Similarly, `PropertyNameResolver` is SPI-discovered and controls how property names are resolved (e.g., `@Field` from Spring Data MongoDB remaps names).

### Generated Class Conventions

- Generated class name: `_ClassName` (underscore prefix, same package)
- Inherits `_Model` from `metamodel-model`
- Static factory: `_ClassName.model()` returns root instance
- Simple properties → `String` method returning property name
- Structured properties → sub-metamodel instance with accumulated path
- Map properties → bare `_Model` (use `._property("key")` to navigate)

### Property Detection

- Default: **field-based** (`PropertyAccessMode.FIELD`)
- Can be set to **getter-based** (`PropertyAccessMode.GETTER`)
- Override name: `@Property(name = "actualName")`
- Ignore: `@Property(ignore = true)`
- Getter pattern defaults to `^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$`

## Branch Strategy

- **All development must happen on the `develop` branch.**
- **Never commit or push code changes directly to `master`.** Master is managed exclusively by the release workflow.
- **IMPORTANT:** If the current branch is `master` and there are uncommitted changes or the user asks to modify code, warn the user immediately before doing anything else.

## Release Process

Releases are managed by the GitHub Actions `release.yml` workflow (manual trigger). It:
1. Strips `-SNAPSHOT` from `develop`, merges to `master`, tags, then bumps `develop` to the new version.
2. Deploys artifacts to the `mvn-repo` branch of this repo via `site-maven-plugin`.
