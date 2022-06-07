package com.fullcyle.admin.catalog.infastructure;

import com.fullcyle.admin.catalog.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(new UseCase().execute());
    }
}