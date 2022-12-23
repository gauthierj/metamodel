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

    private final String representation;

    Modifier(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}
