/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class LexiconCreation implements InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;
    private static List<String> lexiconNames = new ArrayList<String>();


    public LexiconCreation(String inputDir, String pattern, List<Integer> rankThresolds, Integer limit, LexicalEntryHelper lexicalEntryHelperT
            , String outputDir,String parameterPattern,
            Integer totalParameter,Integer parameterNumber) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        List<String> files = FileFolderUtils.getSelectedFiles(inputDir, pattern);

           Integer lexIndex=0; 
          for (Integer rankThresold : rankThresolds) {
            SyntacticEntries syntacticEntries = new SyntacticEntries(lexicalEntryHelper, parameterPattern,rankThresold);
             //System.out.println(parameterPattern+" "+rankThresold);
            for (String fileName : files) {
                
                /*if(!fileName.contains("dbo:capital"))
                    continue;
                */
                if (fileName.contains("#")) {
                    continue;
                }
                //System.out.println(fileName);
                CsvFile inputCsvFile = new CsvFile();
                //String reference = this.lexicalEntryHelper.makeReference(fileName);
                List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
                lexIndex=syntacticEntries.split(rows,lexIndex);
                lexIndex=lexIndex+1;
            }
            System.out.println("totalParameter::"+totalParameter+" parameterNumber::"+parameterNumber+" parameterPattern::"+parameterPattern+" rankThresolds::"+rankThresolds);
            syntacticEntries.write(outputDir,parameterPattern,rankThresold);
            this.lexiconNames.add(syntacticEntries.getParameterString());
        }
    }

     public static void writeLexiconName(LinkedHashMap<String,String> lexiconNames,String grammarDir) {
        String header="#!/bin/sh"+"\n";
        String run_lexicon="run-lexicon.sh";
        String run="";
       
        for (String lexiconT : lexiconNames.keySet()) {
            String fileName ="conf/"+"inputConf_"+lexiconT+"_en"+".json";
            InputCofiguration inputCofiguration = new InputCofiguration(lexiconT);
            JsonWriter.writeClassToJson(inputCofiguration, grammarDir+fileName);
            String runcom="java -jar target/QuestionGrammarGenerator.jar "+fileName+" dataset/dbpedia_en.json"+"\n";
            run+=runcom;
        }
       
        //sekond run this
        //"/media/elahi/Elements/A-project/LDK2023/multilingual-grammar-generator/";
        FileFolderUtils.writeToTextFile(header+run, grammarDir+run_lexicon);
    
    }
    
     public static void writeLexiconName(String inputDir, String outputDir,String grammarDir) {
        String header="#!/bin/sh"+"\n";
        String str="";
        String bashFileName="lexiconMove.sh";
        String run_lexicon="run-lexicon.sh";
        String paramterFile="parameter.txt";
        Integer index=1;
        String lexicon="lexicon_";
        
        String run="";
        String parameterAll="";
       
        Map<Integer,String> lexicons=new HashMap<Integer,String>();
        for (String lexiconName : lexiconNames) {
            String lexiconT="lexicon_"+index.toString();
            String mkdirNoun="mkdir -p "+outputDir+File.separator+lexiconT+"/nouns"+"\n";
            String mkdirverbs="mkdir -p "+outputDir+File.separator+lexiconT+"/verbs"+"\n";
            String mkdirquestions="mkdir -p "+outputDir+File.separator+lexiconT+"/questions"+"\n";
            String line=mkdirNoun+mkdirverbs+mkdirquestions;
            String comNoun= "cp -r "+inputDir+lexiconName+"-NounPPFrame.csv"+" "+outputDir+lexiconT+"/nouns/"+"\n";
            String comTran= "cp -r "+inputDir+lexiconName+"-TransitiveFrame.csv"+" "+outputDir+lexiconT+"/verbs/"+"\n";
            String comInTran= "cp -r "+inputDir+lexiconName+"-InTransitivePPFrame.csv"+" "+outputDir+lexiconT+"/verbs/"+"\n";
            String comAll=comNoun+comTran+comInTran;
            String content=line+comAll+"\n"+"\n";
            str+=content;            lexicons.put(index, lexiconName);
            index=index+1;
            String fileName ="conf/"+"inputConf_"+lexiconT+"_en"+".json";
            InputCofiguration inputCofiguration = new InputCofiguration(lexiconT);
            JsonWriter.writeClassToJson(inputCofiguration, grammarDir+fileName);
            String runcom="java -jar target/QuestionGrammarGenerator.jar "+fileName+" dataset/dbpedia_en.json"+"\n";
            run+=runcom;
            String parameter=lexiconT+"="+lexiconName+"\n";
            parameterAll+=parameter;
        }
        //First run this
        //outputDir = "/media/elahi/Elements/A-project/LDK2023/resources/en/lexicons/";
        FileFolderUtils.writeToTextFile(header+str, outputDir+bashFileName);
        //sekond run this
        //"/media/elahi/Elements/A-project/LDK2023/multilingual-grammar-generator/";
        FileFolderUtils.writeToTextFile(header+run, grammarDir+run_lexicon);
        FileFolderUtils.writeToTextFile(parameterAll, outputDir+paramterFile);
    
    }
    
     
    public LexicalEntryHelper getLexicalEntryHelper() {
        return lexicalEntryHelper;
    }

    public static List<String> getLexiconNames() {
        return lexiconNames;
    }
   

    private String[] gradableCsvRow(String fileName, String linguisticPattern, String value, String frame) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference = lexicalEntryHelper.makeReference(index);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        rowNoun[0] = id;
        rowNoun[1] = "adjective";
        rowNoun[2] = linguisticPattern;
        rowNoun[3] = linguisticPattern;
        rowNoun[4] = "of";
        rowNoun[5] = frame;
        rowNoun[6] = "domain";
        rowNoun[7] = "range";
        rowNoun[8] = "1";
        rowNoun[9] = fileName.replace("raw-", "").replace(".csv", "").replace("_", ":");
        rowNoun[10] = "domainIndex";
        rowNoun[11] = "rangeIndex";
        rowNoun[12] = value;
        rowNoun[13] = reference;
        return rowNoun;
    }

}
