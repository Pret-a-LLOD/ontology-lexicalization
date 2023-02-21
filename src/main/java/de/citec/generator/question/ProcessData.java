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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class ProcessData implements PredictionPatterns, InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;

    public ProcessData(String inputDir, String outputDir, String pattern, String parameterAttribute, String givenProperty, LexicalEntryHelper lexicalEntryHelperT) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        this.sortData(inputDir, outputDir, pattern, parameterAttribute, givenProperty,TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION);
    }

    public void sortData(String inputDir, String outputDir, String pattern, String parameterAttribute, String givenProperty,List<String> filterList) throws Exception {
        List<String> propertyFiles = FileFolderUtils.getSelectedFiles(inputDir, pattern);
        Integer parameterIndex = this.findParameterIndex(parameterAttribute);
        Integer nGramIndex = this.findParameterIndex(gram);
        Integer lingPatternIndex = this.findParameterIndex(linguisticPattern);
        if (parameterIndex != null) {
            ;
        } else {
            throw new Exception("parameter name is not corret");
        }

        for (String fileName : propertyFiles) {
            CsvFile inputCsvFile = new CsvFile();
            //System.out.println("fileName::"+fileName);

            Map<Double, String[]> sortLexEntry = new TreeMap<Double, String[]>(Collections.reverseOrder());

            if (givenProperty.contains("all")) {
                ;
            } else {
                if (fileName.contains(givenProperty))
                ; else {
                    continue;
                }

            }

            List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
            if (rows.isEmpty()) {
                continue;
            }

            for (String[] row : rows) {
                if (row.length > 9) {
                    //System.out.println("linguisticPatternI kol[" + lingPatternIndex + "]:" + row[lingPatternIndex]);
                    //System.out.println("CosineI kol[" + parameterIndex + "]:" + row[parameterIndex]);

                    try {
                        Double value = Double.parseDouble(row[parameterIndex]);
                        String lexEntry = row[lingPatternIndex];
                        String nGram = row[nGramIndex];
                        //System.out.println(value+" "+lexEntry+" "+nGram);
                        sortLexEntry.put(value, new String[]{lexEntry, nGram});
                    } catch (Exception ex) {
                        continue;
                    }

                }
            }
            CsvFile outputCsvFile = new CsvFile();
            List<String[]> resultList = new ArrayList<String[]>();
            for (Double doubleValue : sortLexEntry.keySet()) {
                String[] info = sortLexEntry.get(doubleValue);
                String orginalString = info[0];
                String nGram = info[1];
                String modifiedString = this.lexicalEntryHelper.deleteStopWord(orginalString,filterList);
                String[] posTags = this.findPosTag(modifiedString);
                String postag = posTags[0];
                String frame = posTags[1];
                //System.out.println(modifiedString);
                //System.out.println(doubleValue + "," + modifiedString + "," + frame + "," + postag + "," + nGram + "," + orginalString);
                resultList.add(new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram, orginalString});

            }
            outputCsvFile.writeToCSV(new File(outputDir + fileName), resultList);

        }

    }

    private String[] findPosTag(String inputText) {
        try {
            PosAnalyzer posAnalyzer = new PosAnalyzer(inputText, POS_TAGGER_WORDS, 10);
            String noun = posAnalyzer.isNoun(inputText);
            String verb = posAnalyzer.isVerb(inputText);
            String adj = posAnalyzer.isAdjective(inputText);
            if (noun != null) {
                return new String[]{TextAnalyzer.NN, this.findNounType(noun)};
            } else if (verb != null) {
                return new String[]{TextAnalyzer.VB, this.findVerbType(verb)};
            } else if (adj != null) {
                return new String[]{TextAnalyzer.JJ, this.findAdjType(adj)};
            } else {
                return new String[]{"unknown", "unknown"};
            }
            /*if(posAnalyzer.posTaggerText(inputText)){
               return posAnalyzer.getFullPosTag();
            }*/

        } catch (Exception ex) {
            return new String[]{"unknown", "unknown"};
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
        } else if (parameterAttribute.contains(gram)) {
            return gramI;
        } else if (parameterAttribute.contains(linguisticPattern)) {
            return linguisticPatternI;
        } else {
            return null;
        }
    }

    private String findVerbType(String postag) {
        return TransitiveFrame;
    }

    private String findAdjType(String adj) {
        return GradableFrame;
    }

    private String findNounType(String noun) {
        return NounPPFrame;
    }

}
