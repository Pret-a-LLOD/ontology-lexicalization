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
    public static String NounPPFrame = "NounPPFrame";
    public static String TransitiveFrame = "TransitiveFrame";
    public static String IntransitivePPFrame = "IntransitivePPFrame";
    public static String AdjectiveSuperlativeFrame = "AdjectiveSuperlativeFrame";
    public static String AdjectiveAttributiveFrame = "AdjectiveAttributiveFrame";
    public static String GREP = "grep";
    public static String shHeading = "#!/bin/sh" + "\n";
    public static Set<String> frames = new HashSet<String>(Arrays.asList(NounPPFrame, TransitiveFrame, IntransitivePPFrame));
    //public static Set<String> frames = new HashSet<String>(Arrays.asList(InTransitivePPFrame));

   
}
