/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author elahi
 */
public interface InduceConstants {
    public static String inputDir = "src/main/resources/qald-lex/";
    public static String resDir = "../resources/";
    public static String outputDir = resDir + "qald-lex/";
    public static String NounPPFrame = "NounPPFrame";
    public static String TransitiveFrame = "TransitiveFrame";
    public static String InTransitivePPFrame = "InTransitivePPFrame";
    public static String GradableFrame = "GradableFrame";
    public static String AttibutiveFrame = "AttibutiveFrame";
    public static Set<String> frames = new HashSet<String>(Arrays.asList(NounPPFrame, TransitiveFrame, InTransitivePPFrame));
    
    // csv file column
    public static Integer classI = 0;
    public static Integer ruletypeLongnameI = 1;
    public static Integer ruletypeShortnameI = 2;
    public static Integer linguisticPatternI = 3;
    public static Integer gramI = 5;
    public static Integer propertyI = 7;
    public static Integer condABI = 9;
    public static Integer condBAI = 10;
    public static Integer supAI = 11;
    public static Integer supBI = 12;
    public static Integer supABI = 13;
    public static Integer AllConfI = 14;
    public static Integer CoherenceI = 15;
    public static Integer CosineI = 16;
    public static Integer IRI = 17;
    public static Integer KulczynskiI = 18;
    public static Integer MaxConfI = 19;
    public static Integer stringI = 20;

}
