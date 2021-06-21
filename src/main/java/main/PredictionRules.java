/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author elahi
 */
public interface PredictionRules {
     public static final String predict_l_for_s_given_p = "predict_l_for_s_given_p";
    public static final String predict_l_for_s_given_o = "predict_l_for_s_given_o";
    public static final String predict_l_for_o_given_p = "predict_l_for_o_given_p";
    public static final String predict_l_for_s_given_po = "predict_l_for_s_given_po";
    public static final String predict_l_for_o_given_s ="predict_l_for_o_given_s";
    public static final String predict_l_for_o_given_sp="predict_l_for_o_given_sp";
    public static final String predict_localized_l_for_s_given_p="predict_localized_l_for_s_given_p";
     public static final String predict_localized_l_for_o_given_p="predict_localized_l_for_o_given_p";


    public static final String predict_p_for_s_given_l = "predict_p_for_s_given_l";
    public static final String predict_o_for_s_given_l = "predict_o_for_s_given_l";
    public static final String predict_p_for_o_given_l = "predict_p_for_o_given_l";
    public static final String predict_po_for_s_given_l = "predict_po_for_s_given_l";
    public static final String predict_s_for_o_given_l = "predict_s_for_o_given_l";
    public static final String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l";
    public static final String predict_p_for_s_given_localized_l = "predict_p_for_s_given_localized_l";
    public static final String predict_p_for_o_given_localized_l = "predict_p_for_o_given_localized_l";

    public static final List<String> predictKBGivenLInguistic = new ArrayList<String>(Arrays.asList(
            predict_p_for_s_given_l,
            predict_o_for_s_given_l,
            predict_p_for_o_given_l,
            predict_po_for_s_given_l,
            predict_s_for_o_given_l,
            predict_po_for_s_given_localized_l,
            predict_p_for_s_given_localized_l,
            predict_p_for_o_given_localized_l));
    
    
    public static final List<String> predictLinguisticGivenKB = new ArrayList<String>(Arrays.asList(
            predict_l_for_o_given_p,
            predict_l_for_o_given_s,
            predict_l_for_o_given_sp,
            predict_l_for_s_given_o,
            predict_l_for_s_given_p,
            predict_l_for_s_given_po,
            predict_localized_l_for_s_given_p
    ));

    /*public static final List<String> predictKBGivenLinguisticPattern = new ArrayList<String>(Arrays.asList(
            predict_p_for_s_given_l,
            predict_o_for_s_given_l,
            predict_p_for_o_given_l,
            predict_po_for_s_given_l
    ));*/


     
    public  Boolean isPredict_l_for_s_given_po(String predictionRule);

    public Boolean isPredict_l_for_s_given_o(String predictionRule);

    public Boolean isPredict_l_for_o_given_s(String predictionRule);

    public Boolean isPredict_l_for_o_given_sp(String predictionRule);

    public Boolean isPredict_l_for_o_given_p(String predictionRule);

    public Boolean isPredict_l_for_s_given_p(String predictionRule);

    public Boolean isPredict_localized_l_for_s_given_p(String predictionRule);
    
    public Boolean isPredict_po_for_s_given_l(String predictionRule);
    
    public Boolean isPredict_po_for_s_given_localized_l(String predictionRule);
    
    public Boolean isPredict_p_for_s_given_localized_l(String predictionRule);
    
    public Boolean isPredict_p_for_o_given_localized_l(String predictionRule);

    
}
