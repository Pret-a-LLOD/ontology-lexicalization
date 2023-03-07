/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.generator.config.PredictionPatterns;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.UriFilter;
import static edu.stanford.nlp.patterns.ConstantsAndVariables.getStopWords;
import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class QuestionMain implements PredictionPatterns, InduceConstants {

    private static String qaldResourceDir = "src/main/resources/qald-lex/";
    private static String resDir = "../resources/";
    private static String ldkDir = resDir + "ldk/";
    private static String SEPERATE_PROPERTY_DATA = "SEPERATE_PROPERTY_DATA";
     private static String FIND_PARAMETER_LEXICON = "FIND_PARAMETER_LEXICON";
    private static String CREATE_LEXICON = "CREATE_LEXICON";
    private static String PARAMETER_LEXICON = "PARAMETER_LEXICON";
    private static String GENERATE_PARAMETERS = "FIND_PARAMETERS";

    public static void main(String[] args) {
        String domainRangeFileName = "src/main/resources/qald-lex/DomainAndRange.txt";
        Set<String> rulePatterns = new TreeSet<String>(Arrays.asList("rules-predict_l_for_s_given_p-","rules-predict_localized_l_for_s_given_p-"));

        //List<String> rulePatterns = new ArrayList<String>(Arrays.asList("rules-predict_l_for_s_given_p-", "rules-predict_localized_l_for_s_given_p-"));

        //String parameterPattern = "100-10000-4-5-5-5-5";
        String grammarInputDir = "/media/elahi/Elements/A-project/LDK2023/multilingual-grammar-generator/conf/";
        //List<Integer> rankThresolds = Arrays.asList(10, 20, 50, 100, 150, 200, 250, 300, 350, 400,450,500,700,800,1000);
        List<Integer> rankThresolds = Arrays.asList(10, 50, 200,500);
        String stopWordFile = "src/main/resources/qald-lex/stopword.txt";
        String prepositionFile = "src/main/resources/qald-lex/preposition.txt";
        Set<String> stopWords = getEnglishStopWords(stopWordFile, prepositionFile);
        LinkedHashMap<String, List<Parameters>> rulePatternsParmeters = new LinkedHashMap<String, List<Parameters>>();


        String rootDir = FileFolderUtils.getRootDir();
        List<String> menu = Arrays.asList(new String[]{GENERATE_PARAMETERS,FIND_PARAMETER_LEXICON});
        String inputDir = null, outputDir = null;

        try {
            Integer thresold = 10;
            Integer limit = 1000;
            
            LexicalEntryHelper lexicalEntryHelper = new LexicalEntryHelper(domainRangeFileName);
            if (menu.contains(GENERATE_PARAMETERS)) {
                String parameterFile = "src/main/resources/parameter.txt";
                String thresoldFile = "src/main/resources/thresold.txt";
                Set<String> mergeParameters = GetAllPermutations.mergeParameters(parameterFile, thresoldFile);
                
                /*List<String> rowsT = new ArrayList<String>();
                for (String parameterString : mergeParameters) {
                    for(Integer rankThresold:rankThresolds){
                        rowsT.add(parameterString+"-"+rankThresold);
                         System.out.println(parameterString+"-"+rankThresold);
                    }
                }*/

                for (String rulePattern : rulePatterns) {
                    List<Parameters> rows = new ArrayList<Parameters>();
                    for (String parameterString : mergeParameters) {
                        Parameters parametersValue = new Parameters(rulePattern,parameterString, PredictionPatterns.Cosine,rankThresolds);
                        rows.add(parametersValue);
                    }
                    System.out.println("rulePattern::"+rulePattern+" size:"+rows.size());
                    rulePatternsParmeters.put(rulePattern, rows);
                }
            }
            
            if (menu.contains(SEPERATE_PROPERTY_DATA)) {
                inputDir = ldkDir + "raw/";
                outputDir = ldkDir + "property/";
                String givenClass = "all";
                //givenClass = "dbo-Actor-";
                FileFolderUtils.deleteFiles(new String[]{ldkDir + "property/"});
                if(rulePatterns.isEmpty()){
                    System.out.println("no rule patterns to go, first run!!");
                }
                for (String rulePattern : rulePatterns) {
                    ProcessData processData = new ProcessData(ldkDir + "raw/", ldkDir + "property/", rulePattern, givenClass, lexicalEntryHelper);
                }
            }
             if (menu.contains(FIND_PARAMETER_LEXICON)) {
                inputDir = ldkDir + "property/";
                outputDir = ldkDir + "sort/";
                FileFolderUtils.deleteFiles(new String[]{ldkDir + "sort/"});
                if(rulePatternsParmeters.isEmpty()){
                    System.out.println("no rule patterns to go, first run!!");
                }
                String givenProperty="dbo:birthPlace";
                givenProperty="all";
                 for (String rulePattern : rulePatternsParmeters.keySet()) {
                     List<Parameters> paramters =rulePatternsParmeters.get(rulePattern);
                     Integer parameterNumber=0;Integer totalParameter=paramters.size();
                     for (Parameters paramter : paramters) {
                         System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+paramter.getSearchString());
                         ProcessData processData = new ProcessData(ldkDir + "property/", ldkDir + "sort/", rulePattern,paramter, Cosine, givenProperty, lexicalEntryHelper, stopWords,totalParameter,parameterNumber);
                         parameterNumber=parameterNumber+1;
                     }
                 }
            }
            if (menu.contains(CREATE_LEXICON)) {
                inputDir = resDir + "ldk/sort/";
                outputDir = resDir + "ldk/lexicon/";
                //FileFolderUtils.deleteFiles(new String[]{ldkDir + "lexicon/nouns/", ldkDir + "lexicon/verbs/"});
                //create lexicon 
                Integer index=0;
                FileFolderUtils.delete(new File(outputDir),".csv");
                
                for (String rulePattern : rulePatternsParmeters.keySet()) {
                    List<Parameters> paramters = rulePatternsParmeters.get(rulePattern);
                    for (Parameters paramter : paramters) {
                        String parameterPattern=paramter.getSearchString();
                        LexiconCreation lexiconCreation = new LexiconCreation(inputDir, "-raw", rankThresolds, limit, lexicalEntryHelper, outputDir,parameterPattern);
                        // save lexicon names
                        //lexiconCreation.writeLexiconName(outputDir,finalDir,index);
                       index=index+1;
                    }
                }
                //LexiconCreation.writeLexiconName(inputDir, outputDir);
                //System.out.println(LexiconCreation.getLexiconNames());
                inputDir = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/lexicon/";
                outputDir = "/media/elahi/Elements/A-project/LDK2023/resources/en/lexicons/";
                String confDir = "/media/elahi/Elements/A-project/LDK2023/multilingual-grammar-generator/";
               
               LexiconCreation.writeLexiconName(inputDir, outputDir,confDir);
            }
            /*if (menu.contains(PARAMETER_LEXICON)) {
                String parameterDir = "../resources/ldk/lexicon/";
                List<File> selectedFiles = FileUtils.getSpecificFiles(parameterDir, ".csv");
                String str = FileUtils.findParameterLexEntries(selectedFiles);
                System.out.println(str);
                FileUtils.stringToFiles(str, parameterDir + "parameter.txt");
            }*/
           

        } catch (Exception ex) {

            Logger.getLogger(QuestionMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Set<String> getEnglishStopWords(String stopWordFile, String prepositionFile) {
        Set<String> stopWords = FileUtils.fileToSetString(stopWordFile);
        Set<String> prepositions = FileUtils.fileToSetString(prepositionFile);
        //if(stopWords.contains("of"))
        //    System.out.println("of");
        stopWords.removeAll(prepositions);
        //if(stopWords.contains("of"))
        //    System.out.println("of");
        return stopWords;

    }


    /*private static List<String> getEnglishStopWords() {
        return TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION;
    }*/
}
