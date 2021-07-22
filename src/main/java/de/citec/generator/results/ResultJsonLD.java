/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.generator.config.Constants;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties
public class ResultJsonLD implements Constants{

    private String jsonString = null;
    
    public ResultJsonLD(){
      this.jsonString=defaultResult;  
        
    }

    public ResultJsonLD(String jsonString) {
        this.jsonString = jsonString;
    }

    /* @JsonProperty("@context")
    LinkedHashMap<Object, LinkedHashMap<Object, Object>> context = new LinkedHashMap<Object, LinkedHashMap<Object, Object>>();

    @JsonProperty("@graph")
    List<LinkedHashMap<Object, Object>> graph = new ArrayList<LinkedHashMap<Object, Object>>();

    public LinkedHashMap<Object, LinkedHashMap<Object, Object>> getContext() {
        return context;
    }

    public List<LinkedHashMap<Object, Object>> getGraph() {
        return graph;
    }*/

    public String getJsonString() {
        return jsonString;
    }
    
    
}
