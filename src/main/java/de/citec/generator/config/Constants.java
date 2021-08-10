/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

/**
 *
 * @author elahi
 */
public interface Constants {

    /*public static String predict_l_for_s_given_po = "predict_l_for_s_given_po";
    public static String predict_po_for_s_given_l = "predict_po_for_s_given_l";
    public static String predict_localized_l_for_s_given_po = "predict_localized_l_for_s_given_po";
    public static String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l";
    public static String predict_l_for_s_given_p = "predict_l_for_s_given_p";
    public static String predict_p_for_s_given_l = "predict_p_for_s_given_l";
    public static String predict_localized_l_for_s_given_p = "predict_localized_l_for_s_given_p";
    public static String predict_p_for_s_given_localized_l = "predict_p_for_s_given_localized_l";
    public static String predict_l_for_s_given_o = "predict_l_for_s_given_o";
    public static String predict_o_for_s_given_l = "predict_o_for_s_given_l";
    public static String predict_l_for_o_given_sp = "predict_l_for_o_given_sp";
    public static String predict_sp_for_o_given_l = "predict_sp_for_o_given_l";
    public static String predict_localized_l_for_o_given_sp = "predict_localized_l_for_o_given_sp";
    public static String predict_sp_for_o_given_localized_l = "predict_sp_for_o_given_localized_l";
    public static String predict_l_for_o_given_s = "predict_l_for_o_given_s";
    public static String predict_s_for_o_given_l = "predict_s_for_o_given_l";
    public static String predict_l_for_o_given_p = "predict_l_for_o_given_p";
    public static String predict_p_for_o_given_l = "predict_p_for_o_given_l";
    public static String predict_localized_l_for_o_given_p = "predict_localized_l_for_o_given_p";
    public static String predict_p_for_o_given_localized_l = "predict_p_for_o_given_localized_l";*/
    public static String UNDERSCORE = "UNDERSCORE";
    public static String perlDir = "perl/";
    public static String lexGenScriptName = "experiment.pl";
    public static String quesGenScriptName = "QuestionGrammarGenerator.jar";

    public static String processData = "processData/";
    public String appDir = "app/";
    public String interDir = appDir + "inter/";
    public String resultDir = appDir + "results/";
    public String inputDir = appDir + "input/";
    public static String inputAbstract = appDir + "input/inputAbstract";
    public static String jsonOutputDir = resultDir + "lexicon.json";
    public static String turtleOutputDir = resultDir + "lexicon.ttl";
    public static String defaultResult = "{\"@graph\" : [],\"@context\":{}}";
    public static String abstractOutputPath = inputDir + "inputAbstract/";
    public static String propertyOutputPath = inputDir + "inputProperty/";
    public static String semanticOutputPath = inputDir + "inputSemantic/";
    //public static String datasetDir = appDir + "dataset/";
    public static String quesAnslDir = "question-grammar-generator/";
    public static String dataSetConfig = appDir + "dataset/dbpedia.json";
    public static String lexiconDir = quesAnslDir + "lexicon/en/";
    public static String language = "EN";
    public static Integer numberOfentitiesToConsider = 10;

}
