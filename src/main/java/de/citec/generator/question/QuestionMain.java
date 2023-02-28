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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    private static String RANK_PROPERTY_LEXICALIZATION = "RANK_PROPERTY_LEXICALIZATION";
    private static String CREATE_LEXICON = "CREATE_LEXICON";
    private static String PARAMETER_LEXICON = "PARAMETER_LEXICON";

    public static void main(String[] args) {
        Map<String, Map<String, String>> frameUris = new HashMap<String, Map<String, String>>();
        String domainRangeFileName = "src/main/resources/qald-lex/DomainAndRange.txt";
        //Set<String> rulePatterns = new TreeSet<String>(Arrays.asList("rules-predict_l_for_s_given_p-","rules-predict_localized_l_for_s_given_p-"));
        
        List<String> rulePatterns = new ArrayList<String>(Arrays.asList("rules-predict_l_for_s_given_p-","rules-predict_localized_l_for_s_given_p-"));

        String parameterPattern = "100-10000-4-5-5-5-5";
        String grammarInputDir = "/home/elahi/A-project/multilingual-grammar-generator/lexicon/";
        //List<Integer> rankThresolds = Arrays.asList(10, 20, 50, 100, 150, 200, 250, 300, 350, 400,450,500,700,800,1000);
        List<Integer> rankThresolds = Arrays.asList(10, 20, 50, 100, 150, 200, 250,300, 350,400,500);
        String stopWordFile="src/main/resources/qald-lex/stopword.txt";
        String prepositionFile="src/main/resources/qald-lex/preposition.txt";
        Set<String> stopWords=getEnglishStopWords(stopWordFile,prepositionFile); 
        

        String rootDir = FileFolderUtils.getRootDir();
        List<String> menu = Arrays.asList(new String[]{RANK_PROPERTY_LEXICALIZATION,CREATE_LEXICON});
        String inputDir = null, outputDir = null;

        try {
            Integer thresold = 10;
            Integer limit = 1000;
            String givenPropoerty = "all";
             //givenPropoerty = "dbo-Actor-";
            LexicalEntryHelper lexicalEntryHelper = new LexicalEntryHelper(domainRangeFileName);
            if (menu.contains(RANK_PROPERTY_LEXICALIZATION)) {
                inputDir = ldkDir + "raw/";
                outputDir = ldkDir + "sort/";
                FileFolderUtils.deleteFiles(new String[]{ldkDir + "sort/"});
                for(String rulePattern:rulePatterns){
                 ProcessData processData = new ProcessData(ldkDir + "raw/", ldkDir + "sort/", rulePattern, Cosine, givenPropoerty, lexicalEntryHelper,stopWords);
                }
            }
            if (menu.contains(CREATE_LEXICON)) {
                inputDir = resDir + "ldk/sort/";
                outputDir = resDir + "ldk/lexicon/";
                //FileFolderUtils.deleteFiles(new String[]{ldkDir + "lexicon/nouns/", ldkDir + "lexicon/verbs/"});
                //create lexicon 
                for(String rulePattern:rulePatterns){
                LexiconCreation lexiconCreation = new LexiconCreation(inputDir, "-raw", rankThresolds, limit, lexicalEntryHelper, outputDir, rulePattern, parameterPattern);
                // save lexicon names
                lexiconCreation.writeLexiconName(grammarInputDir);
                }
            }
            if (menu.contains(PARAMETER_LEXICON)) {
                String parameterDir = "/media/elahi/Elements/A-project/resources/ldk/parameter/";
                List<File> selectedFiles = FileUtils.getSpecificFiles(parameterDir, ".csv");
                String str = FileUtils.findParameterLexEntries(selectedFiles);
                FileUtils.stringToFiles(str, parameterDir + "parameter.txt");
            }
            
            

        } catch (Exception ex) {

            Logger.getLogger(QuestionMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private static Set<String> getEnglishStopWords(String stopWordFile,String prepositionFile) {
        Set<String> stopWords= FileUtils.fileToSetString(stopWordFile);
        Set<String> prepositions= FileUtils.fileToSetString(prepositionFile);
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
