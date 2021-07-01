/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import com.example.process.ProcessCsv;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Main {
    private static String mainDir="/home/elahi/a-teanga/dockerTest/ontology-lexicalization/";
    private static String baseDir = mainDir+"results-v4/";
    private static String location = "perl/";
    private static String scriptName = "experiment.pl";
    private static String processData = "processData/";

    private Map<String, String> lexicalEntries = new TreeMap<String, String>();

    public static void main(String[] args) {

        try {
            String resourceDir = baseDir + processData;
            ProcessCsv process = new ProcessCsv(baseDir, resourceDir);
            System.out.println("Processing files are ended!!!");
        } catch (Exception ex) {
            System.out.println("file process does not work!!!" + ex.getMessage());
        }

    }

}
