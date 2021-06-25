/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

import com.example.analyzer.PosAnalyzer;
import static com.example.analyzer.TextAnalyzer.OBJECT;
import  com.example.process.*;
import com.example.utils.FileFolderUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Lexicon implements PredictionRules{

    private String lexiconDirectory = null;
    private Map<String, List<LexiconUnit>> lexiconPosTaggged = new TreeMap<String, List<LexiconUnit>>();

    public Lexicon(String outputDir) throws IOException {
        this.lexiconDirectory = outputDir;
    }

    public void preparePropertyLexicon(String predictionRule, String directory, String key, String interestingness, Map<String, List<LineInfo>> lineLexicon) throws IOException, Exception {
        Map<String, List<LexiconUnit>> posTaggedLex = new TreeMap<String, List<LexiconUnit>>();
        Integer count = 0, countJJ = 0, countVB = 0;
        for (String word : lineLexicon.keySet()) {
            String postagOfWord = null;
            LinkedHashMap<Integer, List<String>> kbList = new LinkedHashMap<Integer, List<String>>();
            Integer index = 0;
            List<LineInfo> LineInfos = lineLexicon.get(word);
            //Collections.sort(LineInfos,new LineInfo());  

            Set<String> duplicateCheck = new HashSet<String>();

            /*if(postagOfWord.contains(Analyzer.NOUN))
                    countNN=countNN+1;
                else if (postagOfWord.contains(Analyzer.ADJECTIVE)){
                     countJJ=countJJ+1;
                }
                else if (postagOfWord.contains(Analyzer.VERB)){
                     countVB=countVB+1;
                }*/
            count = count + 1;
            for (LineInfo lineInfo : LineInfos) {
                postagOfWord = lineInfo.getPartOfSpeech();
                String value = null;

                String object = lineInfo.getObject();
                List<String> pairs = new ArrayList<String>();
                if (duplicateCheck.contains(object)) {
                    continue;
                }
                /*if(lineInfo.getWordOriginal().contains("legal journal"))
                    System.out.println("lineInfo2:"+lineInfo);
                 */
                if (lineInfo.getProbabilityValue().isEmpty()) {
                    continue;
                } else {
                    value = lineInfo.getProbabilityValue(interestingness).toString();
                }

               
                //pairs.add("pair=" + lineInfo.getPredicate() + "_" + lineInfo.getObject());
                String kb = this.getPair(lineInfo, predictionRule);
                pairs.add("kb"+"=" + kb);
                pairs.add(interestingness + "=" + value);
                pairs.add("triple" + "=" + lineInfo.getSubject() + " " + lineInfo.getPredicate() + " " + lineInfo.getObject());
                pairs.add("class" + "=" + lineInfo.getClassName());
                //pairs.add("line" + "=" +lineInfo.toString().replace("=", " ") );
                pairs.add(lineInfo.getProbabilityValue().toString().replace(",", ""));
                pairs.add("posTag" + "=" + lineInfo.getPosTag());
                pairs.add("interestingness" + "=" + interestingness);
                pairs.add("subject" + "=" + lineInfo.getSubjectOriginal());
                pairs.add("predicate" + "=" + lineInfo.getPredicateOriginal());
                pairs.add("object" + "=" + lineInfo.getObjectOriginal());
                pairs.add("fullPosTag" + "=" + lineInfo.getFullPosTag());
                pairs.add("string" + "=" +lineInfo.getLine().replace("=", "$").replace(",", "&"));

                kbList.put(index, pairs);
                index = index + 1;
                duplicateCheck.add(object);
            }
            LexiconUnit LexiconUnit = new LexiconUnit(count, word, postagOfWord, kbList);
            posTaggedLex = this.setPartsOfSpeech(postagOfWord, LexiconUnit, posTaggedLex);
        }
        
        this.writeFileLemon(posTaggedLex);

        /*for (String postag : posTaggedLex.keySet()) {
            String fileName = directory + "/" + interestingness + "-" + postag + "-" + key + ".json";
            System.out.println(fileName);
            List<LexiconUnit> lexiconUnts = posTaggedLex.get(postag);
            this.lexiconPosTaggged.put(postag, lexiconUnts);
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(Paths.get(fileName).toFile(), lexiconUnts);
        }*/

    }
    
    private void writeFileLemon(Map<String, List<LexiconUnit>> posTaggedLex) {
        for (String postag : posTaggedLex.keySet()) {
            List<LexiconUnit> lexiconUnts = posTaggedLex.get(postag);
            for (LexiconUnit lexiconUnit : lexiconUnts) {
                System.out.println("lexiconUnit:" + lexiconUnit);
            }
        }

    }

    private Map<String, List<LexiconUnit>> setPartsOfSpeech(String postagOfWord, LexiconUnit LexiconUnit, Map<String, List<LexiconUnit>> lexicon) {
        List<LexiconUnit> temp = new ArrayList<LexiconUnit>();
        if (lexicon.containsKey(postagOfWord)) {
            temp = lexicon.get(postagOfWord);
        }
        temp.add(LexiconUnit);
        lexicon.put(postagOfWord, temp);
        return lexicon;
    }

    public String getOutputDir() {
        return lexiconDirectory;
    }

    private String getFirstTag(String posTag) {
        String firstWord = null;
        if (posTag.contains("_")) {
            String info[] = posTag.split("_");
            firstWord = info[0];
        } else {
            firstWord = posTag;
        }
        return firstWord;
    }

    public String getLexiconDirectory() {
        return lexiconDirectory;
    }

    public Map<String, List<LexiconUnit>> getLexiconPosTaggged() {
        return lexiconPosTaggged;
    }

    private String getPair(LineInfo lineInfo, String predictionRule) throws Exception {
        return lineInfo.getSubject() + " " + lineInfo.getPredicate()+ lineInfo.getObject();
    }

  

}
