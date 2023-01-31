/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.opencsv.exceptions.CsvMalformedLineException;
import de.citec.generator.config.PredictionPatterns;
import static de.citec.generator.question.InduceConstants.outputDir;
import de.citec.sc.generator.analyzer.PosAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
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
public class ProcessData implements PredictionPatterns {

    public ProcessData(String inputDir, String outputDir, String pattern, String parameterAttribute) throws Exception {
        sortData(inputDir, outputDir, pattern, parameterAttribute);
    }

    public void sortData(String inputDir, String outputDir, String pattern, String parameterAttribute) throws Exception {
        List<String> propertyFiles = FileFolderUtils.getSelectedFiles(inputDir, pattern);
        Integer parameterIndex = this.findParameterIndex(parameterAttribute);
        Integer lingPatternIndex = this.findParameterIndex(linguisticPattern);
        if (parameterIndex != null) {
            ;
        } else {
            throw new Exception("parameter name is not corret");
        }

        for (String fileName : propertyFiles) {
            CsvFile inputCsvFile = new CsvFile();
            //System.out.println("fileName::"+fileName);

            Map<Double, String> sortLexEntry = new TreeMap<Double, String>(Collections.reverseOrder());
            if (fileName.contains("author")) {
               
                List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
                if(rows.isEmpty()){
                    continue;
                }
               
                for (String[] row : rows) {
                    if (row.length > 9) {
                        //System.out.println("linguisticPatternI kol[" + lingPatternIndex + "]:" + row[lingPatternIndex]);
                        //System.out.println("CosineI kol[" + parameterIndex + "]:" + row[parameterIndex]);

                        try {
                            Double value = Double.parseDouble(row[parameterIndex]);
                            String lexEntry = row[lingPatternIndex];
                            sortLexEntry.put(value, lexEntry);
                        } catch (Exception ex) {
                            continue;
                        }

                    }
                }
                CsvFile outputCsvFile = new CsvFile();
                List<String[]> resultList = new ArrayList<String[]>();
                for (Double doubleValue : sortLexEntry.keySet()) {
                    String referenceForm = sortLexEntry.get(doubleValue);
                    String posTag=this.findPosTag(referenceForm);
                    System.out.println(referenceForm+" "+posTag);
                    exit(1);
                    resultList.add(new String[]{doubleValue.toString(), referenceForm,posTag});

                }
                outputCsvFile.writeToCSV(new File(outputDir + fileName), resultList);

           }

        }

    }

    private Integer findParameterIndex(String parameterAttribute) {
        if (parameterAttribute.contains(PredictionPatterns.Cosine)) {
            return PredictionPatterns.CosineI;
        } else if (parameterAttribute.contains(PredictionPatterns.Coherence)) {
            return PredictionPatterns.CoherenceI;
        } else if (parameterAttribute.contains(PredictionPatterns.MaxConf)) {
            return PredictionPatterns.MaxConfI;
        } else if (parameterAttribute.contains(PredictionPatterns.Kulczynski)) {
            return PredictionPatterns.KulczynskiI;
        } else if (parameterAttribute.contains(PredictionPatterns.IR)) {
            return PredictionPatterns.IRI;
        } else if (parameterAttribute.contains(PredictionPatterns.condAB)) {
            return PredictionPatterns.condABI;
        } else if (parameterAttribute.contains(PredictionPatterns.condBA)) {
            return PredictionPatterns.condBAI;
        } else if (parameterAttribute.contains(PredictionPatterns.supA)) {
            return PredictionPatterns.supAI;
        } else if (parameterAttribute.contains(PredictionPatterns.supB)) {
            return PredictionPatterns.supBI;
        } else if (parameterAttribute.contains(PredictionPatterns.supAB)) {
            return PredictionPatterns.supABI;
        } else if (parameterAttribute.contains(linguisticPattern)) {
            return linguisticPatternI;
        } else {
            return null;
        }
    }

    private String findPosTag(String inputText) {
        try {
            PosAnalyzer posAnalyzer=new PosAnalyzer( inputText,POS_TAGGER_WORDS , 10) ;
            if(posAnalyzer.posTaggerText(inputText)){
               return posAnalyzer.getFullPosTag();
            }
        } catch (Exception ex) {
            return "unknown";
        }
        return null;
    }

}
