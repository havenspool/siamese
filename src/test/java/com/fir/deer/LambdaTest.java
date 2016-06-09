package com.havens.siamese;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by havens on 16-6-2.
 */
public class LambdaTest {

    public static void listLambda(){
        List<String> names = new ArrayList<String>();
        names.add("TaoBao");
        names.add("ZhiFuBao");


        List<String> lowercaseNames = new ArrayList<String>();
        for (String name : names) {
            lowercaseNames.add(name.toLowerCase());
        }

       // List<String> lowercaseNames2 = names.stream().map(name -> {return name.toLowerCase();}).collect(Collectors.toList());
        System.out.println(lowercaseNames);
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        listLambda();
    }
}
