package com.github.gauthierj.metamodel.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Immutable representation of a dot-separated property path.
 *
 * <p>A path is composed of one or more string components joined by dots when rendered.
 * It is used internally by generated metamodel classes to accumulate path segments as
 * nested properties are accessed.
 *
 * <p>Example:
 * <pre>{@code
 * _Path root    = _Path.of();                         // ""
 * _Path address = _Path.of("address");                // "address"
 * _Path street  = address.with("street");             // "address.street"
 * _Path full    = _Path.of("a", "b", "c");            // "a.b.c"
 * }</pre>
 */
public class _Path {

    private final List<String> components;

    private _Path(List<String> components) {
        this.components = List.copyOf(components);
    }

    /**
     * Copy constructor. Creates a new {@code _Path} with the same components as the given path.
     *
     * @param path the path to copy
     */
    public _Path(_Path path) {
        this(path.components);
    }

    /**
     * Creates a new path from the given components.
     *
     * <p>Passing no arguments or {@code null} creates an empty path.
     *
     * @param components the path components
     * @return a new path composed of the given components
     */
    public static _Path of(String... components) {
        return new _Path(
                Optional.ofNullable(components).stream()
                        .flatMap(Arrays::stream)
                        .collect(Collectors.toList()));
    }

    /**
     * Returns a new path by appending the given components to this path.
     *
     * <p>This path is not modified.
     *
     * @param components the components to append
     * @return a new path with the given components appended
     */
    public _Path with(String... components) {
        return new _Path(
                Stream.concat(
                        this.components.stream(),
                        Optional.ofNullable(components).stream()
                                .flatMap(Arrays::stream)
                ).collect(Collectors.toList()));
    }

    /**
     * Returns this path as a dot-separated string.
     *
     * <p>An empty path returns an empty string. A single-component path returns
     * the component itself. Multiple components are joined with {@code "."}.
     *
     * @return the dot-separated string representation of this path
     */
    public String toString() {
        return String.join(".", components);
    }
}
