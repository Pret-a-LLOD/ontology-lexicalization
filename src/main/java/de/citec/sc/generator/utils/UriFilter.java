/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

/**
 *
 * @author elahi
 */
public class UriFilter {

    public static String filter(String key) {
        key = key.replace("http://dbpedia.org/ontology/", "");
        key = key.replace("http://dbpedia.org/property/", "");
        return key;
    }

    public static String getOriginal(String key) {
        if (key.contains("dbo:")) {
            key = key.replace("dbo:", "");
            key = "http://dbpedia.org/ontology/" + key;
        } else if (key.contains("dbp:")) {
            key = key.replace("dbp:", "");
            key = "http://dbpedia.org/property/" + key;
        }

        return key;
    }

    public static String replaceColon(String key) {
        key = key.replace(":", "_");
        return key;
    }

}
