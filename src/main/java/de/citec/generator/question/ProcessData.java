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
        this.organizeData(inputDir, outputDir, pattern, parameterAttribute, givenProperty, TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION);
    }

    public void organizeData(String inputDir, String outputDir, String rulePattern, String parameterAttribute, String givenProperty, List<String> filterList) throws Exception {
        Integer parameterIndex = 17;
        List<String> rawFiles = FileFolderUtils.getSelectedFiles(inputDir, rulePattern);
        if(rawFiles.isEmpty())
           throw new Exception("no raw files to process!!!");
        //Integer parameterIndex = this.findParameterIndex(parameterAttribute);
        Integer propertyIndex = this.findParameterIndex(Property);

        if (parameterIndex != null) {
            ;
        } else {
            throw new Exception("parameter name is not corret");
        }

        Map<String, List<List<String>>> propertyMap = new TreeMap<String, List<List<String>>>();
        Integer index=0;
        for (String fileName : rawFiles) {
            CsvFile inputCsvFile = new CsvFile();
            index=index+1;

            if (givenProperty.contains("all")) {
                ;
            } else {
                if (fileName.contains(givenProperty))
                ; else {
                    continue;
                }

            }
            
            if(fileName.contains(".~lock"))
                continue;

            //List<String[]> rows = inputCsvFile.getManualRow(new File(inputDir + fileName),0.0,-1);
            //List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
            
            List<List<String>> rows=inputCsvFile.getRowsManual(new File(inputDir+fileName),parameterIndex,0.4);

            System.out.println("total::"+rawFiles.size()+" index::"+index+" now reading files ::"+fileName+" number of lines::"+rows.size());

            if (rows.isEmpty()) {
                continue;
            }

            for (List<String> row : rows) {
                if (row.size() > 9) {
                    //System.out.println("linguisticPatternI kol[" + lingPatternIndex + "]:" + row[lingPatternIndex]);
                    //System.out.println("CosineI kol[" + parameterIndex + "]:" + row[parameterIndex]);

                    try {
                        //Double value = Double.parseDouble(row[parameterIndex]);
                        //String lexEntry = row[lingPatternIndex];
                        //String nGram = row[nGramIndex];
                        String property = row.get(8);
                        property = this.lexicalEntryHelper.formatPropertyLongToShort(property);
                        //System.out.println("property::"+property);
                        //sortLexEntry.put(value, new String[]{lexEntry, nGram,property});
                        if (property != null)
                            ; else {
                            continue;
                        }
                        List<List<String>> list = new ArrayList<List<String>>();
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
        this.sortData(propertyMap, parameterAttribute,outputDir,filterList);

    }
    
  
    //using manual lines  
    //0: http://dbpedia.org/ontology/AcademicJournal,
    //1: predict_l_for_s_given_p,
    //2: "c_s
    //3: ,p => l_s",
    //4: "medical journal in the field",
    //5: ,
    //6: 5-gram,
    //7: ,
    //8: http://dbpedia.org/property/eissn,
    //9: ,
    //AB 10: 0.0024330900243309,
    //BA 11: 0.875,5754,
    //supA 12: 16,
    //supB 13: 14,
    //supAB 14: 0.0024330900243309,
    //Al 15: 0.0024263431542461,
    //coher 16: 0.0461405870280119,
    //cos 17: 0.996872828353023,
    //IR 18: 0.438716545012165,
    //Kul 19: 0.875,
    //Max 20: 
    //string 21: "dbo:AcademicJournal in c_e and exists o : (e, dbp:eissn, o) in G => 'medical journal in the field' in l_e"

    public void sortData(Map<String, List<List<String>>> propertyMap, String parameterAttribute,String outputDir,List<String> filterList) throws Exception {
        /*Integer parameterIndex = this.findParameterIndex(parameterAttribute);
        Integer nGramIndex = this.findParameterIndex(N_Gram);
        Integer lingPatternIndex = this.findParameterIndex(Linguistic_Pattern);
        Integer propertyIndex = this.findParameterIndex(Property);
        Integer classIndex = this.findParameterIndex(ClassName);*/
        Integer parameterIndex = 17;
        Integer nGramIndex = 6;
        Integer lingPatternIndex = 4;
        //Integer propertyIndex = 8;
        Integer classIndex = 0;
        Integer number=0;
        for (String property : propertyMap.keySet()) {
            number=number+1;
            Map<Double, String[]> sortLexEntry = new TreeMap<Double, String[]>(Collections.reverseOrder());
            List<List<String>> list = propertyMap.get(property);
            System.out.println("total::"+propertyMap.size()+" index::"+number+" now sorting properties ::"+property+" number of lines::"+list.size());

            for (List<String> row : list) {
                String lexEntry = row.get(lingPatternIndex);
                Double value = Double.parseDouble(row.get(parameterIndex));
                String className = row.get(classIndex);
                className = this.lexicalEntryHelper.formatPropertyLongToShort(className);
                String nGram = row.get(nGramIndex);
                //System.out.println("lexEntry:"+lexEntry+" value:"+value+" className:"+className);
                sortLexEntry.put(value, new String[]{lexEntry,nGram,className});

            }
            CsvFile outputCsvFile = new CsvFile();
            List<String[]> resultList = new ArrayList<String[]>();
            for (Double doubleValue : sortLexEntry.keySet()) {
                String[] info = sortLexEntry.get(doubleValue);
                String orginalString = info[0];
                String nGram = info[1];
                String modifiedString = this.lexicalEntryHelper.deleteStopWord(orginalString, filterList);
                String[] posTags = this.findPosTag(modifiedString);
                String postag = posTags[0];
                String frame = posTags[1];
                String className = info[2];
                System.out.println(doubleValue + "," + modifiedString + "," + frame + "," + postag + "," + nGram + "," + orginalString + "," + className+","+property);
                resultList.add(new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram, orginalString, className,property});
            }
            outputCsvFile.writeToCSV(new File(outputDir + property + "-raw" + ".csv"), resultList);
        }

        /*CsvFile outputCsvFile = new CsvFile();
            List<String[]> resultList = new ArrayList<String[]>();
            for (Double doubleValue : sortLexEntry.keySet()) {
                String[] info = sortLexEntry.get(doubleValue);
                String orginalString = info[0];
                String nGram = info[1];
                String modifiedString = this.lexicalEntryHelper.deleteStopWord(orginalString, filterList);
                String[] posTags = this.findPosTag(modifiedString);
                String postag = posTags[0];
                String frame = posTags[1];
                System.out.println(modifiedString);
                String property = info[2];
                System.out.println(doubleValue + "," + modifiedString + "," + frame + "," + postag + "," + nGram + "," + orginalString + "," + property);
                String[] row = new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram, orginalString, property};

                List<String[]> list = new ArrayList<String[]>();
                if (propertyMap.containsKey(property)) {
                    list = propertyMap.get(property);
                    list.add(row);
                    propertyMap.put(property, list);
                } else {

                    list.add(row);
                    propertyMap.put(property, list);
                }
                //resultList.add(new String[]{doubleValue.toString(), modifiedString, frame, postag, nGram, orginalString,property});

            }

            for (String property : propertyMap.keySet()) {
                List<String[]> list = propertyMap.get(property);
                for (String[] row : list) {
                    resultList.add(row);
                }
            }
            outputCsvFile.writeToCSV(new File(outputDir + fileName), resultList);*/
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
