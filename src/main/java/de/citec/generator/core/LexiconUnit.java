/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.core;

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
    private LinkedHashMap<Integer, List<LineInfo>> entityInfos = new LinkedHashMap<Integer, List<LineInfo>>();
    @JsonIgnore
    private LinkedHashMap<Integer, LineInfo> lineInfos = new LinkedHashMap<Integer, LineInfo>();
    
    public LexiconUnit() {

    }
    
    
      public LexiconUnit(Integer id,String word, String partsOfSpeech, LinkedHashMap<Integer, List<LineInfo>> entityInfos) {
        this.id=id;
        this.partsOfSpeech = partsOfSpeech;
        this.word = word;
        this.entityInfos = entityInfos;
    }

    public String getWord() {
        return word;
    }

    public String getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public Integer getId() {
        return id;
    }

    public LinkedHashMap<Integer, List<LineInfo>>  getLineInfos() {
        return entityInfos;
    }

    @Override
    public String toString() {
        return "LexiconUnit{" + "Integer=" + id + ", word=" + word + ", partsOfSpeech=" + partsOfSpeech + ", entityInfos=" + entityInfos + '}';
    }
    
   

}
