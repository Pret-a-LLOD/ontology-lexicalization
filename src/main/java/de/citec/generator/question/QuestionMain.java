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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class QuestionMain implements PredictionPatterns,InduceConstants {
    
    
    private static Map<String, String> frameUris = new HashMap<String, String>();
    
    public static void main(String[] args) {
        String GREP = "grep";
        String str = "#!/bin/sh" + "\n";
        String rulePattern = "rules-predict_l_for_s_given_p-";
        String givenFrame=InTransitivePPFrame;
        String rootDir = FileFolderUtils.getRootDir();
        Set<String> set = new HashSet<String>(frames);
        for (String frame : frames) {
            if (frame.contains(givenFrame)) {
                frameUris = FileFolderUtils.fileList(inputDir + frame + ".txt", frame, frameUris);                
            }
        }
        
        List<String> inputFiles = FileFolderUtils.getSelectedFiles(outputDir, rulePattern);
        
        qaldUrisExtract(rulePattern, GREP, str, rootDir, givenFrame);
        
      
         Map<Double, String> sortLexEntry=sortData(outputDir,"dbo_");
         
         System.out.println(sortLexEntry.keySet());
        
    }

    private static void qaldUrisExtract(String rulePattern, String GREP, String str, String rootDir, String givenFrame) {
        for (String key : frameUris.keySet()) {
            String url = UriFilter.getOriginal(key);
            url = "'" + url + "'";
            if(url.contains("dbo_inflow.csv")||url.contains("dbo_starring.csv")||url.contains("dbo_developer.csv")
                    ||url.contains("dbo_routeStart.csv")||url.contains("dbo_writer.csv"))
                continue;
             
            System.out.println(url);
            String outputFile = UriFilter.replaceColon(UriFilter.filter(key));
            //for (String inputFile : inputFiles) {
            String inputFile = outputDir + rulePattern + "*" + ".csv";
            outputFile = outputDir + outputFile + ".csv";
            String grep = GREP + " " + url + " " + inputFile + ">>" + outputFile + "\n";
            
            str += grep;

            //}
        }
        FileFolderUtils.stringToFiles(str, rootDir +  "grep" + givenFrame+".sh");
        System.out.println(str);
    }

    private static Map<Double, String> sortData(String outputDir, String pattern) {
        List<String> propertyFiles = FileFolderUtils.getSelectedFiles(outputDir, pattern);
        Map<Double, String> sortLexEntry = new TreeMap<Double, String>(Collections.reverseOrder());

        for (String file : propertyFiles) {
            CsvFile csvFile = new CsvFile();

            if (file.contains("dbo_developer")) {
                List<String[]> rows = csvFile.getRows(new File(outputDir + file));
                System.out.println(outputDir + file + " " + rows.size());
                for (String[] row : rows) {
                    //Integer index = 0;
                    //for(String kol:row){
                    //System.out.println("kol[" +index+++"]:"+kol);
                    if (row.length > 10) {
                        System.out.println("linguisticPatternI kol[" + linguisticPatternI + "]:" + row[linguisticPatternI]);
                        System.out.println("CosineI kol[" + InduceConstants.CosineI + "]:" + row[CosineI]);
                   
                    Double value = Double.parseDouble(row[CosineI]);
                    String lexEntry = row[linguisticPatternI];
                    sortLexEntry.put(value, lexEntry);
                    }

                    // }
                    //break;
                }
            }

        }
        return sortLexEntry;
    }

}
