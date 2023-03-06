/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author elahi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InputCofiguration {
    @JsonProperty("languageCode")
    private String languageCode = "en";
    @JsonProperty("inputDir")
    private String inputDir = "../resources/en/lexicons";
    @JsonProperty("outputDir")
    private String outputDir = "../resources/en/lexicons";
    @JsonProperty("parameter")
    private String parameter = null;
    @JsonProperty("entityDir")
    private String entityDir = "../resources/en/property/";
    @JsonProperty("questionDir")
    private String questionDir = "../resources/en/lexicons";
    @JsonProperty("domainAndRangeDir")
    private String domainAndRangeDir = "../resources/en/lexicons/domainOrRange/";
    @JsonProperty("classDir")
    private String classDir = "../resources/en/entity/";
    @JsonProperty("wikiFile")
    private String wikiFile = "../resources/en/turtle/wikipedia_links_en_filter.ttl";
    @JsonProperty("abstractFile")
    private String abstractFile = "../resources/en/turtle/short_abstracts_sorted_en_filter.ttl";
    private String qaldDir = "qald7Modified/";
    @JsonProperty("numberOfEntities")
    private Integer numberOfEntities=100;
    @JsonProperty("similarityThresold")
    private Double similarityThresold=0.7;
    @JsonProperty("csvToTurtle")
    private Boolean csvToTurtle = true;
    @JsonProperty("turtleToProtoType")
    private Boolean turtleToProtoType = true;
    @JsonProperty("protoTypeToQuestion")
    private Boolean protoTypeToQuestion = true;
    @JsonProperty("inductive")
    private Boolean inductive = true;
    @JsonProperty("evalution")
    private Boolean evalution=false;
    @JsonProperty("composite")
    private Boolean compositeFlag=false;
    @JsonProperty("single")
    private Boolean singleFlag=true;
    @JsonProperty("online")
    private Boolean online=false;
    @JsonProperty("externalEntittyList")
    private Boolean externalEntittyList=false;
    @JsonProperty("evalutionQuestion")
    private Boolean evalutionQuestion = false;
    @JsonProperty("offlineQuestion")
    private Boolean offlineQuestion = true;
    @JsonProperty("evalutionBindingFile")
    private String evalutionBindingFile="src/main/resources/en/LcQuad/lcquad1_test_en_de.csv";
    @JsonProperty("batchFile")
    private String batchFile="../resources/en/batch.prop";


    public InputCofiguration() {

    }

    public InputCofiguration(String lexiconName) {
        this.parameter=lexiconName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public Language getLanguage() {
        if (languageCode.contains("de")) {
            return Language.DE;
        } else if (languageCode.contains("en")) {
            return Language.EN;
        } else if (languageCode.contains("es")) {
            return Language.ES;
        } else if (languageCode.contains("it")) {
            return Language.IT;
        }
        return Language.EN;
    }

    public String getInputDir() {
        return inputDir + File.separator + this.parameter;
    }

    public String getOutputDir() {
        return outputDir + File.separator + this.parameter;
    }

    public Integer getNumberOfEntities() {
        return numberOfEntities;
    }

    public Boolean isCsvToTurtle() {
        return csvToTurtle;
    }

    public Boolean getTurtleToProtoType() {
        return turtleToProtoType;
    }

    public Boolean isProtoTypeToQuestion() {
        return protoTypeToQuestion;
    }

    public Boolean isEvalution() {
        return evalution;
    }

    public Double getSimilarityThresold() {
        return similarityThresold;
    }

    public Boolean getOfflineQuestion() {
        return offlineQuestion;
    }

    public String getQaldDir() {
        return qaldDir;
    }

    public Boolean getCsvToTurtle() {
        return csvToTurtle;
    }

    public Boolean getProtoTypeToQuestion() {
        return protoTypeToQuestion;
    }

    public Boolean getEvalution() {
        return evalution;
    }

    public Boolean getCompositeFlag() {
        return compositeFlag;
    }

    public Boolean getSingleFlag() {
        return singleFlag;
    }

    public String getEntityDir() {
        return entityDir;
    }

    public String getQuestionDir() {
        return questionDir + File.separator + parameter + File.separator + "questions/";
    }

    public String getClassDir() {
        return classDir;
    }

    public Boolean getOnline() {
        return online;
    }

    public Boolean getExternalEntittyList() {
        return externalEntittyList;
    }

    public String getWikiFile() {
        return wikiFile;
    }

    public String getAbstractFile() {
        return abstractFile;
    }

    public String getEvalutionBindingFile() {
        return evalutionBindingFile;
    }

    public Boolean getEvalutionQuestion() {
        return evalutionQuestion;
    }

    public String getBatchFile() {
        return batchFile;
    }

    public String getParameter() {
        return parameter;
    }

    public String getDomainAndRangeDir() {
        return domainAndRangeDir;
    }

    public Boolean getInductive() {
        return inductive;
    }
    
  

    @Override
    public String toString() {
        return "InputCofiguration{" + "language=" + languageCode + ", inputDir=" + getInputDir() + ", outputDir=" + getOutputDir() + ", numberOfEntities=" + numberOfEntities + ", similarityThresold=" + similarityThresold + ", csvToTurtle=" + csvToTurtle + ", turtleToProtoType=" + turtleToProtoType + ", protoTypeToQuestion=" + protoTypeToQuestion + ", evalution=" + evalution + '}';
    }

}
