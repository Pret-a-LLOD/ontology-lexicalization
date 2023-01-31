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

    public static void main(String[] args) {
        Map<String, Map<String, String>> frameUris = new HashMap<String, Map<String, String>>();

        String rulePattern = "rules-predict_l_for_s_given_p-";
        String rootDir = FileFolderUtils.getRootDir();
        Set<String> set = new HashSet<String>(frames);
        for (String frame : frames) {
            //if (frame.contains(InTransitivePPFrame)) {
                Map<String, String> data = new TreeMap<String, String>();
                data = FileFolderUtils.fileList(inputDir + frame + ".txt", frame);
                //System.out.println(frame+"  data.keySet()::"+data.keySet());
                frameUris.put(frame, data);
            //}

        }

        List<String> inputFiles = FileFolderUtils.getSelectedFiles(outputDir, rulePattern);

        //QaldUri.qaldUrisExtract(rulePattern, rootDir, frameUris);

        try {
            Integer thresold = 10;
            Integer limit = 50;
            ProcessData processData = new ProcessData(outputDir, outputDir + "sort/", "raw", Cosine);
            //LexiconCreation lexiconCreation = new LexiconCreation(outputDir + "sort/", "raw", thresold, limit);

        } catch (Exception ex) {
            Logger.getLogger(QuestionMain.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
