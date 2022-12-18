package com.github.gauthierj.metamodel.classbuilder;

public enum Modifier {

    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    FINAL("final"),
    STATIC("static"),
    ABSTRACT("abstract"),
    TRANSIENT("transient"),
    SYNCHRONIZED("synchronized"),
    VOLATILE("volatile");

    private final String modifier;

    Modifier(String modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return modifier;
    }
}
