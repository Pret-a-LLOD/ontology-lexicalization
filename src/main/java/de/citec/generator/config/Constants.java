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
    public static String dataSetConfig = appDir + "dataset/dbpedia.json";
    public static String language = "EN";
    public static Integer numberOfentitiesToConsider = 10;
    public static String quesAnslDir = "../question-grammar-generator/";
    public static String lexiconDir = quesAnslDir + "lexicon/en/";
    public static String questionGenScript = quesAnslDir + "target/QuestionGrammarGenerator.jar";
    public static String outputDir = quesAnslDir + "output";
    public static String datasetDir = quesAnslDir + "dataset/";
    public static String datasetConfig = "dbpedia.json";
    public static String ENDPOINT_CREATE_LEMON = "/createLemon";
    public static String ENDPOINT_QUESTION_ANSWER_LEX_ENTRY = "/createQaLexEntry";
    public static String ENDPOINT_QUESTION_ANSWER = "/createQuestionAnswer";
    public static String ENDPOINT_SEARCH_PATTERN = "/searchPattern";
    public static String ENDPOINT_LEX = "/lexicalization";
    public static String ENDPOINT_DOWNLOAD = "/download";
    public static String CSV="csv";
}
