/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedData {

    @JsonProperty("endpoint")
    private String endpoint = null;
    @JsonProperty("linkedDataName")
    private String linkedDataName = null;
    @JsonProperty("prefix")
    private LinkedHashMap<String, String> prefix = new LinkedHashMap<String, String>();
    @JsonProperty("gradablePrefix")
    private LinkedHashMap<String, String> gradablePrefix = new LinkedHashMap<String, String>();
    @JsonProperty("bindingLimit")
    private long bindingLimit;
    @JsonProperty("classLabels")
    private LinkedHashMap<String, String> classLabels = new LinkedHashMap<String, String>();
    @JsonProperty("classProperty")
    private String classProperty = null;
    

    @JsonIgnore
    public static String dbpedia = "dbpedia";
    @JsonIgnore
    public static String wikidata = "wikidata";
    @JsonIgnore
    public static String http = "http";
    @JsonIgnore
    public static String colon = ":";
    
    public static String rdfType = null;

    public LinkedData(String endpoint, LinkedHashMap<String, String> prefix) {
        this.endpoint = endpoint;
        this.prefix = prefix;
    }

    public LinkedData() {

    }

    public String getEndpoint() {
        return endpoint;
    }

    public LinkedHashMap<String, String> getPrefixes() {
        this.prefix.putAll(this.gradablePrefix);
        return prefix;
    }

    /*LinkedHashMap<String, String> prefix = FileFolderUtils.fileToHashOrg(dataSetConfFile);
        LinkedData linkedData=new LinkedData("https://dbpedia.org/sparql",prefix);
        FileFolderUtils.write(linkedData, dataSetConfFile);*/
    public long getBindingLimit() {
        return bindingLimit;
    }

    @Override
    public String toString() {
        return "LinkedData{" + "endpoint=" + endpoint + ", prefix=" + prefix + ", bindingLimit=" + bindingLimit + '}';
    }

    public String getRdfPropertyType() {
        if(endpoint.contains("dbpedia")&&this.prefix.containsKey("rdf")){
            return this.prefix.get("rdf")+"type";
        }
        return null;
    }
    public String getRdfPropertyClass(String className) {
        if(endpoint.contains("dbpedia")&&this.prefix.containsKey("dbo")){
            return this.prefix.get("dbo")+className;
        }
        return null;
    }
    
    public String getRdfPropertyLabel(String className) {
        return this.classLabels.get(className);
    }

    public String getLinkedDataName() {
        return linkedDataName;
    }

    public String getClassProperty() {
        return classProperty;
    }
    
    
}