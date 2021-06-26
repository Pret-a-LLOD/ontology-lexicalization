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
            LinkedHashMap<Integer, List<LineInfo>> kbList = new LinkedHashMap<Integer, List<LineInfo>>();
            Integer index = 0;
            List<LineInfo> LineInfos = lineLexicon.get(word);
            //Collections.sort(LineInfos,new LineInfo());  

            Set<String> duplicateCheck = new HashSet<String>();
            count = count + 1;
            for (LineInfo lineInfo : LineInfos) {
                postagOfWord = lineInfo.getPartOfSpeech();
                String value = null;

                String object = lineInfo.getObject();
                List<LineInfo> pairs = new ArrayList<LineInfo>();
                if (duplicateCheck.contains(object)) {
                    continue;
                }
                if (lineInfo.getProbabilityValue().isEmpty()) {
                    continue;
                } else {
                    value = lineInfo.getProbabilityValue(interestingness).toString();
                }
                pairs.add(lineInfo);
                kbList.put(index, pairs);
                index = index + 1;
                duplicateCheck.add(object);
            }
            LexiconUnit LexiconUnit = new LexiconUnit(count, word, postagOfWord, kbList);
            posTaggedLex = this.setPartsOfSpeech(postagOfWord, LexiconUnit, posTaggedLex);
        }
        
        this.writeFileLemon(posTaggedLex);
    }
    
    private void writeFileLemon(Map<String, List<LexiconUnit>> posTaggedLex) {
        for (String postag : posTaggedLex.keySet()) {
            List<LexiconUnit> lexiconUnts = posTaggedLex.get(postag);
            for (LexiconUnit lexiconUnit : lexiconUnts) {
                LinkedHashMap<Integer, List<LineInfo>> ranks=lexiconUnit.getLineInfos();
                String writtenForm=lexiconUnit.getWord();
                for(Integer rank:ranks.keySet()){
                    
                }
                if (postag.contains("JJ")) {
                    Templates templates = new Templates(postag, "spanish", writtenForm);
                    System.out.println("templates:" + templates.getResultStr());
                }
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
