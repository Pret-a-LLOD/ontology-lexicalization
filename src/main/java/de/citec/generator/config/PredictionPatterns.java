/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

import java.util.*;

/**
 *
 * @author elahi
 */
public interface PredictionPatterns {

    public static final String predict_l_for_s_given_po = "predict_l_for_s_given_po";
    public static final String predict_localized_l_for_s_given_po = "predict_localized_l_for_s_given_po";
    public static final String predict_l_for_s_given_p = "predict_l_for_s_given_p";
    public static final String predict_localized_l_for_s_given_p = "predict_localized_l_for_s_given_p";
    public static final String predict_l_for_s_given_o = "predict_l_for_s_given_o";
    public static final String predict_l_for_o_given_sp = "predict_l_for_o_given_sp";
    public static final String predict_localized_l_for_o_given_sp = "predict_localized_l_for_o_given_sp";
    public static final String predict_l_for_o_given_s = "predict_l_for_o_given_s";
    public static final String predict_l_for_o_given_p = "predict_l_for_o_given_p";
    public static final String predict_localized_l_for_o_given_p = "predict_localized_l_for_o_given_p";
    public static final String predict_p_for_s_given_l = "predict_p_for_s_given_l";
    public static final String predict_o_for_s_given_l = "predict_o_for_s_given_l";
    public static final String predict_p_for_o_given_l = "predict_p_for_o_given_l";
    public static final String predict_po_for_s_given_l = "predict_po_for_s_given_l";
    public static final String predict_s_for_o_given_l = "predict_s_for_o_given_l";
    public static final String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l";
    public static final String predict_p_for_s_given_localized_l = "predict_p_for_s_given_localized_l";
    public static final String predict_p_for_o_given_localized_l = "predict_p_for_o_given_localized_l";
    public static final String predict_sp_for_o_given_localized_l = "predict_sp_for_o_given_localized_l";
    public static final String predict_sp_for_o_given_l = "predict_sp_for_o_given_l";
    
    public static final String supA = "supA";
    public static final String supB = "supB";
    public static final String supAB = "supAB";
    public static final String condAB = "condAB";
    public static final String condBA = "condBA";
    public static final String AllConf = "AllConf";
    public static final String MaxConf = "MaxConf";
    public static final String IR = "IR";
    public static final String Kulczynski = "Kulczynski";
    public static final String Cosine = "Cosine";
    public static final String Coherence = "Coherence";
    public static final LinkedHashSet<String> interestingness = new LinkedHashSet(new ArrayList<String>(Arrays.asList(Cosine, Coherence, AllConf, MaxConf, IR, Kulczynski)));

}
