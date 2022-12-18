package com.github.gauthierj.metamodel.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _Path {

    private final List<String> components;

    private _Path(List<String> components) {
        this.components = List.copyOf(components);
    }

    public _Path(_Path path) {
        this(path.components);
    }

    public static _Path of(String... components) {
        return new _Path(
                Optional.ofNullable(components).stream()
                        .flatMap(Arrays::stream)
                        .collect(Collectors.toList()));
    }

    public _Path with(String... components) {
        return new _Path(
                Stream.concat(
                        this.components.stream(),
                        Optional.ofNullable(components).stream()
                                .flatMap(Arrays::stream)
                ).collect(Collectors.toList()));
    }

    public String toString() {
        return String.join(".", components);
    }
}
