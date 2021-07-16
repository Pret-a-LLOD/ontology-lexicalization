/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

import de.citec.sc.generator.analyzer.PosAnalyzer;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author elahi
 */
public class StopWordRemoval {

    public static String deleteStopWord(String nGramStr) {
        String tokenStr = "";
        if (nGramStr.contains("_")) {
            String[] tokens = nGramStr.split("_");
            for (String token : tokens) {
                if (PosAnalyzer.ENGLISH_STOPWORDS.contains(token)) {
                    continue;
                } else {
                    String line = token;
                    tokenStr += line + "_";
                }

            }
            Integer length = tokenStr.length() - 1;
            //System.out.println(nGramStr+" "+length);
            if (length > 1) {
                return tokenStr.substring(0, tokenStr.length() - 1);
            } else {
                return tokenStr;
            }

        } else {
            return nGramStr;
        }

    }

    public static Boolean isInExcludeList(String nGram) {
        String[] months = new String[]{"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        String[] splitTerms = getNgramStringList(nGram);

        HashSet<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(months));
        set.retainAll(Arrays.asList(splitTerms));
        if (set.isEmpty()) {
            return false;
        }
        return true;

    }

    private static String[] getNgramStringList(String nGram) {
        if (nGram.contains("_")) {
            return nGram.split("_");
        } else {
            return new String[]{nGram};
        }

    }

    public static void main(String[] args) {
        String string = "a_australian";
        String modString = deleteStopWord(string);
        //System.out.println("modString:"+modString);

    }

}
