package com.somepackage;

public class SomeClass {

    private final String aStringField;

    public SomeClass() {
        this.aStringField = "aDefaultValue"
    }

    public SomeClass(String aParam, int anotherParam) {
        this.aStringField = aParam;
        if (anotherParam > 3) {
            System.out.println("Hello World!");
        } else {
            System.out.println("Goodbye World!");
        }
    }

    private void doSomething() {
        // Nothing to do
    }

    public static class AnInnerClass {

        private String anInnerClassField;

        public AnInnerClass(String anInnerClassField) {
            this.anInnerClassField = anInnerClassField;
        }

        public String getAnInnerClassField() {
            return anInnerClassField;
        }
    }
}
