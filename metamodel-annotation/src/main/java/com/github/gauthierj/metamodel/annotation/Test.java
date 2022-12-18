package com.github.gauthierj.metamodel.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        Pattern compile = Pattern.compile("^(?:is|get)([A-Z][a-zA-Z0-9_$]*)$");
        Matcher getAProperty = compile.matcher("getAProperty");
        System.out.println(getAProperty.matches());
    }
}
