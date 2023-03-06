/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.generator.config.PredictionPatterns;
import de.citec.sc.generator.analyzer.PosAnalyzer;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class ProcessData implements PredictionPatterns, InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;

    public ProcessData(String inputDir, String outputDir, String pattern, String givenClass, LexicalEntryHelper lexicalEntryHelperT) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        this.organizeData(inputDir, outputDir, pattern, givenClass);
    }

    public ProcessData(String inputDir, String outputDir, String rulePattern, Parameters parameter, String interestingnessType, String givenProperty, LexicalEntryHelper lexicalEntryHelperT, Set<String> stopWords) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        this.paramterData(inputDir, outputDir, rulePattern, parameter, interestingnessType, givenProperty, stopWords);
    }

    public void organizeData(String inputDir, String outputDir, String rulePattern, String givenClass) throws Exception {
        Integer parameterIndex = 17;
        List<String> rawFiles = FileFolderUtils.getSelectedFiles(inputDir, rulePattern);
        if (rawFiles.isEmpty()) {
            throw new Exception("no raw files to process!!!");
        }

        if (parameterIndex != null) {
            ;
        } else {
            throw new Exception("parameter name is not corret");
        }

        Map<String, List<String[]>> propertyMap = new TreeMap<String, List<String[]>>();
        Integer index = 0;
        for (String fileName : rawFiles) {
            if (fileName.contains(".~lock")) {
                continue;
            }
            CsvFile inputCsvFile = new CsvFile();
            index = index + 1;

            if (givenClass.contains("all")) {
                ;
            } else {
                if (fileName.contains(givenClass))
                ; else {
                    continue;
                }

            }

            List<String[]> rows = inputCsvFile.getRowsManual(new File(inputDir + fileName), parameterIndex, 0.0);
            System.out.println("total::" + rawFiles.size() + " index::" + index + " now reading files ::" + fileName + " number of lines::" + rows.size());

            if (rows.isEmpty()) {
                continue;
            }

            for (String[] row : rows) {
                if (row.length > 9) {
                    try {
                        String property = row[8];
                        property = this.lexicalEntryHelper.formatPropertyLongToShort(property);

                        if (property != null)
                            ; else {
                            continue;
                        }
                        if (!lexicalEntryHelper.isPropertyQald(property)) {
                            continue;
                        }

                        List<String[]> list = new ArrayList<String[]>();
                        if (propertyMap.containsKey(property)) {
                            list = propertyMap.get(property);
                            list.add(row);
                            propertyMap.put(property, list);
                        } else {

                            list.add(row);
                            propertyMap.put(property, list);
                        }
                    } catch (Exception ex) {
                        continue;
                    }

                }
            }
        }
        this.saveProcessData(rulePattern, propertyMap, outputDir);
        //this.sortData(rulePattern, propertyMap, interestingnessType, outputDir, filterList);
    }
    
     public void saveProcessData(String rulePattern, Map<String, List<String[]>> propertyMap, String outputDir) throws Exception {
        Integer number = 0;
        for (String property : propertyMap.keySet()) {
            number = number + 1;
            List<String[]> resultList = propertyMap.get(property);
            System.out.println("total::" + propertyMap.size() + " index::" + number + " now sorting properties ::" + property + " number of lines::" + resultList.size());
            CsvFile outputCsvFile = new CsvFile();
            outputCsvFile.writeToCSV(new File(outputDir + property + "-" + rulePattern + ".csv"), resultList);
        }
    }

    public void paramterData(String inputDir, String outputDir, String rulePattern, Parameters givenParameter, String interestingnessType, String givenProperty, Set<String> filterList) throws Exception {
        Integer parameterIndex = 17;
        List<String> rawFiles = FileFolderUtils.getSelectedFiles(inputDir, rulePattern);
        Map<String, List<String[]>> propertyMap = new HashMap<String, List<String[]>>();
        if (rawFiles.isEmpty()) {
            throw new Exception("no raw files to process!!!");
        }

        if (parameterIndex != null) {
            ;
        } else {
            throw new Exception("parameter name is not corret");
        }

        Integer index = 0;
        for (String fileName : rawFiles) {
            if (fileName.contains(".~lock")) {
                continue;
            }
            CsvFile inputCsvFile = new CsvFile();
            index = index + 1;

            if (givenProperty.contains("all")) {
                ;
            } else {
                if (fileName.contains(givenProperty))
                ; else {
                    continue;
                }

            }

            List<String[]> rows = inputCsvFile.getRowsManual(new File(inputDir + fileName), parameterIndex, 0.03);

            if (rows.isEmpty()) {
                continue;
            }
            List<String[]> result = new ArrayList<String[]>();
            String property =null,parameterStr=givenParameter.getSearchString();
            
            for (String[] row : rows) {
                if (row.length > 9) {
                    try {
                        property = this.lexicalEntryHelper.cleanString(row[8]);
                        if (property != null)
                            ; else {
                            continue;
                        }

                        property = this.lexicalEntryHelper.formatPropertyLongToShort(property);
                        RowValue rowValue = new RowValue(row, interestingnessType, lexicalEntryHelper);
                        Parameters rawParameter = rowValue.getParameters();
                         
                        if (Parameters.checkParamter(givenParameter, rawParameter)) {
                            result.add(row);
                        } else {
                            continue;
                        }

                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        continue;
                    }

                }
            }
           this.sortData(rulePattern, parameterStr, property,result,interestingnessType, outputDir, filterList);
        }
        //this.sortData(rulePattern, propertyMap, interestingnessType, outputDir, filterList);
    }

   
    
    public void sortData(String rulePattern, String parameterStr, String property, List<String[]> list, String interestingnessType, String outputDir, Set<String> filterList) throws Exception {
        Map<Double, RowValue> sortLexEntry = new TreeMap<Double, RowValue>(Collections.reverseOrder());

        for (String[] row : list) {
            RowValue rowValue = new RowValue(row, interestingnessType, lexicalEntryHelper);
            sortLexEntry.put(rowValue.getValue(), rowValue);
        }
        CsvFile outputCsvFile = new CsvFile();
        List<String[]> resultList = new ArrayList<String[]>();
        for (Double doubleValue : sortLexEntry.keySet()) {
            RowValue rowValue = sortLexEntry.get(doubleValue);
            String orginalString = rowValue.getLingPattern();
            String nGram = rowValue.getnGram();
            String modifiedString = this.lexicalEntryHelper.deleteStopWord(orginalString, filterList);
            String[] posTags = this.lexicalEntryHelper.findPosTag(modifiedString);
            String postag = posTags[0];
            String frame = posTags[1];
            String className = rowValue.getClassName();

            Parameters parameters = rowValue.getParameters();

            System.out.println(doubleValue + "," + modifiedString + "," + frame + "," + postag + "," + nGram + ","
                    + orginalString + "," + className + "," + property + "," + parameterStr + ","
                    + parameters.getSupA() + "," + parameters.getSupB() + "," + parameters.getSupAB() + ","
                    + parameters.getConfAB() + "," + parameters.getConfBA());
            resultList.add(new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram,
                orginalString, className, property, parameterStr,
                parameters.getSupA().toString(), parameters.getSupB().toString(), parameters.getSupAB().toString(),
                parameters.getConfAB().toString(), parameters.getConfBA().toString()});
        }
        outputCsvFile.writeToCSV(new File(outputDir + property + "-raw" + "-" + parameterStr + ".csv"), resultList);

    }

    /*public void sortData(String rulePattern, Map<String, List<String[]>> propertyMap, String interestingnessType, String outputDir, Set<String> filterList) throws Exception {
        Integer number = 0;
        for (String property : propertyMap.keySet()) {
            number = number + 1;
            Map<Double, RowValue> sortLexEntry = new TreeMap<Double, RowValue>(Collections.reverseOrder());
            List<String[]> list = propertyMap.get(property);
            System.out.println("total::" + propertyMap.size() + " index::" + number + " now sorting properties ::" + property + " number of lines::" + list.size());

            for (String[] row : list) {
                
                RowValue rowValue = new RowValue(row, interestingnessType, lexicalEntryHelper);
                sortLexEntry.put(rowValue.getValue(), rowValue);
            }
            CsvFile outputCsvFile = new CsvFile();
            List<String[]> resultList = new ArrayList<String[]>();
            for (Double doubleValue : sortLexEntry.keySet()) {
                RowValue rowValue = sortLexEntry.get(doubleValue);
                String orginalString = rowValue.getLingPattern();
                String nGram = rowValue.getnGram();
                String modifiedString = this.lexicalEntryHelper.deleteStopWord(orginalString, filterList);
                String[] posTags = this.lexicalEntryHelper.findPosTag(modifiedString);
                String postag = posTags[0];
                String frame = posTags[1];
                String className = rowValue.getClassName();

                Parameters parameters = rowValue.getParameters();

               
                System.out.println(doubleValue + "," + modifiedString + "," + frame + "," + postag + "," + nGram + ","
                        + orginalString + "," + className + "," + property + "," + rulePattern + ","
                        + parameters.getSupA() + "," + parameters.getSupB() + "," + parameters.getSupAB() + ","
                        + parameters.getConfAB() + "," + parameters.getConfBA());
                resultList.add(new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram,
                    orginalString, className, property, rulePattern,
                    parameters.getSupA().toString(), parameters.getSupB().toString(), parameters.getSupAB().toString(),
                    parameters.getConfAB().toString(), parameters.getConfBA().toString()});
            }
            outputCsvFile.writeToCSV(new File(outputDir + property + "-raw" + "-" + rulePattern + ".csv"), resultList);
        }
    }*/

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
        } else if (parameterAttribute.contains(N_Gram)) {
            return gramI;
        } else if (parameterAttribute.contains(Linguistic_Pattern)) {
            return linguisticPatternI;
        } else if (parameterAttribute.contains(Property)) {
            return propertyI;
        } else if (parameterAttribute.contains(ClassName)) {
            return classI;
        } else {
            return null;
        }
    }

   

}
