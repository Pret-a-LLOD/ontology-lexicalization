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
    public static String InTransitivePPFrame = "InTransitivePPFrame";
    public static String GradableFrame = "GradableFrame";
    public static String AttibutiveFrame = "AttibutiveFrame";
    public static String GREP = "grep";
    public static String shHeading = "#!/bin/sh" + "\n";
    public static Set<String> frames = new HashSet<String>(Arrays.asList(NounPPFrame, TransitiveFrame, InTransitivePPFrame));
    //public static Set<String> frames = new HashSet<String>(Arrays.asList(InTransitivePPFrame));

   
}
