/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

/**
 *
 * @author elahi
 */
public class ConfigQuestion {

    private String baseDir = "question-grammar-generator/";
    private String targetDir = baseDir + "target/";
    private String jarFileName =  "QuestionGrammarGenerator.jar";
    private String language = "EN";
    private String lexiconDir = baseDir + "lexicon/en";
    private String output = baseDir + "output/";
    private String dataset = baseDir + "dataset/dbpedia.json";
    
    public ConfigQuestion(){
        
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getLocation() {
        return targetDir;
    }

    public String getLanguage() {
        return language;
    }

    public String getLexiconDir() {
        return lexiconDir;
    }

    public String getOutput() {
        return output;
    }

    public String getDataset() {
        return dataset;
    }

}
