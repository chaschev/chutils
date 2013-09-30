package com.chaschev.chutils;

import com.google.common.base.Strings;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        System.out.printf("hi from chutils, %s! have a good %s!%n", args[0], args[1]);
        System.out.println("is guava with us?");

        System.out.print("guava is");

        System.out.println(Strings.commonSuffix(" no, not here!", " here!") );
    }
}
