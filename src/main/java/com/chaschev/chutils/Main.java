package com.chaschev.chutils;

import com.google.common.base.Strings;

/**
 * @author Andrey Chaschev chaschev@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("USAGE: chutils <your name> <a word>");
        }

        System.out.printf("hi from chutils, %s! have a good %s!%n", args[0], args[1]);
        System.out.printf("are we alone, %s?%n%n", args[0]);

        System.out.print("no, we celebrate! guava is");

        System.out.flush();

        System.out.println(Strings.commonSuffix(" not with us!", " with us!") );
    }
}
