/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.generator.config.PredictionPatterns;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.UriFilter;
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

    public static void main(String[] args) {
        Map<String, Map<String, String>> frameUris = new HashMap<String, Map<String, String>>();
        String domainRangeFileName = "src/main/resources/qald-lex/DomainAndRange.txt";
        String rulePattern = "rules-predict_l_for_s_given_p-";
        String parameterPattern = "100-10000-4-5-5-5-5";
        String grammarInputDir = "/home/elahi/A-project/multilingual-grammar-generator/lexicon/";
        List<Integer> rankThresolds = Arrays.asList(10, 20, 50, 100, 150, 200, 250, 300, 350, 400);
        String rootDir = FileFolderUtils.getRootDir();
        List<String> menu = Arrays.asList(new String[]{RANK_PROPERTY_LEXICALIZATION, CREATE_LEXICON});
        String inputDir = null, outputDir = null;

        try {
            Integer thresold = 10;
            Integer limit = 1000;
            String givenPropoerty = "all";
            LexicalEntryHelper lexicalEntryHelper = new LexicalEntryHelper(domainRangeFileName);
            if (menu.contains(RANK_PROPERTY_LEXICALIZATION)) {
                inputDir = ldkDir + "raw/";
                outputDir = ldkDir + "sort/";
                FileFolderUtils.deleteFiles(new String[]{ldkDir + "sort/"});
                ProcessData processData = new ProcessData(ldkDir + "raw/", ldkDir + "sort/", rulePattern, Cosine, givenPropoerty, lexicalEntryHelper);
            }
            if (menu.contains(CREATE_LEXICON)) {
                inputDir = resDir + "ldk/sort/";
                outputDir = resDir + "ldk/lexicon/";
                FileFolderUtils.deleteFiles(new String[]{ldkDir + "lexicon/nouns/", ldkDir + "lexicon/verbs/"});
                //create lexicon 
                LexiconCreation lexiconCreation = new LexiconCreation(inputDir, "-raw", rankThresolds, limit, lexicalEntryHelper, outputDir, rulePattern, parameterPattern);
                // save lexicon names
                lexiconCreation.writeLexiconName(grammarInputDir);
            }

        } catch (Exception ex) {

            Logger.getLogger(QuestionMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
