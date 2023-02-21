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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class QuestionMain implements PredictionPatterns, InduceConstants {
    private  static String inputDir = "src/main/resources/qald-lex/";
    private  static String resDir = "../resources/";
    private  static String outputDir = resDir + "ldk/";

    public static void main(String[] args) {
        Map<String, Map<String, String>> frameUris = new HashMap<String, Map<String, String>>();
        String domainRangeFileName = "src/main/resources/qald-lex/DomainAndRange.txt";

        String rulePattern = "rules-predict_l_for_s_given_p-";
        String rootDir = FileFolderUtils.getRootDir();
        Set<String> set = new HashSet<String>(frames);
        for (String frame : frames) {
            Map<String, String> data = new TreeMap<String, String>();
            data = FileFolderUtils.fileList(inputDir + frame + ".txt", frame);
            frameUris.put(frame, data);
        }

        List<String> inputFiles = FileFolderUtils.getSelectedFiles(outputDir, rulePattern);
        try {
            Integer thresold = 10;
            Integer limit = 1000;
            String givenPropoerty = "all";
            LexicalEntryHelper lexicalEntryHelper = new LexicalEntryHelper(domainRangeFileName);
            //ProcessData processData = new ProcessData(outputDir, outputDir + "sort/", "raw", Cosine, givenPropoerty, lexicalEntryHelper);
            String inputDir="../resources/en/ldk/sort/";
            String lexiconDir="../resources/en/ldk/lexicon/";
            String fileType="raw";
            LexiconCreation lexiconCreation = new LexiconCreation(inputDir, fileType, thresold, limit, lexicalEntryHelper,lexiconDir);

        } catch (Exception ex) {
            Logger.getLogger(QuestionMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
