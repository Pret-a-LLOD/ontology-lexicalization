/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author elahi
 */
public class LexiconUnit {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("pattern")
    private String word;
    @JsonProperty("partsOfSpeech")
    private String partsOfSpeech;
    @JsonProperty("index")
    private LinkedHashMap<Integer, List<String>> entityInfos = new LinkedHashMap<Integer, List<String>>();
    @JsonIgnore
    private LinkedHashMap<Integer, LineInfo> lineInfos = new LinkedHashMap<Integer, LineInfo>();
    
   
   

    public LexiconUnit() {

    }
    
    public LexiconUnit(LexiconUnit existLexiconUnit, LexiconUnit givenLexiconUnit) {
        Integer newIndex = 0;
        LinkedHashMap<Integer, List<String>> entityInfos = new LinkedHashMap<Integer, List<String>>();
        for (Integer index : existLexiconUnit.getEntityInfos().keySet()) {
            List<String> pairs = existLexiconUnit.getEntityInfos().get(index);
            entityInfos.put(newIndex, pairs);
            newIndex = newIndex + 1;
        }
        for (Integer index : givenLexiconUnit.getEntityInfos().keySet()) {
            List<String> pairs = givenLexiconUnit.getEntityInfos().get(index);
            entityInfos.put(newIndex, pairs);
            newIndex = newIndex + 1;
        }
        this.id = existLexiconUnit.getId();
        this.partsOfSpeech = existLexiconUnit.getPartsOfSpeech();
        this.word = existLexiconUnit.getWord();
        this.entityInfos = entityInfos;
    }
    
      public LexiconUnit(Integer id,String word, String partsOfSpeech, LinkedHashMap<Integer, List<String>> entityInfos) {
        this.id=id;
        this.partsOfSpeech = partsOfSpeech;
        this.word = word;
        this.entityInfos = entityInfos;
    }


    public LexiconUnit(Integer id,String word, String partsOfSpeech, LinkedHashMap<Integer, List<String>> kbLineList,LinkedHashMap<Integer, LineInfo> kbLineListLine) {
        this.id=id;
        this.partsOfSpeech = partsOfSpeech;
        this.word = word;
        this.entityInfos = kbLineList;
        this.lineInfos=kbLineListLine;
    }

    /*public LexiconUnit(LexiconUnit LexiconUnit, LinkedHashMap<Integer, List<String>> newEntityInfos) {
        this.id = LexiconUnit.getId();
        this.partsOfSpeech = LexiconUnit.getPartsOfSpeech();
        this.word = LexiconUnit.getWord();
        this.entityInfos = newEntityInfos;
    }*/

    public String getWord() {
        return word;
    }

    public LinkedHashMap<Integer, List<String>> getEntityInfos() {
        return entityInfos;
    }

    public String getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public Integer getId() {
        return id;
    }

    public LinkedHashMap<Integer, LineInfo> getLineInfos() {
        return lineInfos;
    }

    @Override
    public String toString() {
        return "LexiconUnit{" + "Integer=" + id + ", word=" + word + ", partsOfSpeech=" + partsOfSpeech + ", entityInfos=" + entityInfos + '}';
    }
    
   

}
