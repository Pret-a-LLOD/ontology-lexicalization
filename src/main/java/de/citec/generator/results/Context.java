/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.generator.config.Configuration;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author elahi
 */
public class Context {

    @JsonProperty("frequency")
    private IdType frequency = new IdType();

    @JsonProperty("prepositionalAdjunct")
    private IdType prepositionalAdjunct = new IdType();

    @JsonProperty("copulativeSubject")
    private IdType copulativeSubject = new IdType();
    
     @JsonProperty("attributiveArg")
    private IdType attributiveArg = new IdType();

    @JsonProperty("reference")
    private IdType reference = new IdType();

    @JsonProperty("generatedBy")
    private IdType generatedBy = new IdType();

    @JsonProperty("subjOfProp")
    private IdType subjOfProp = new IdType();

    @JsonProperty("objOfProp")
    private IdType objOfProp = new IdType();

    @JsonProperty("writtenRep")
    private Id writtenRep = new Id();

    @JsonProperty("hasValue")
    private Id hasValue = new Id();

    @JsonProperty("onProperty")
    private IdType onProperty = new IdType();

    @JsonProperty("label")
    private Id label = new Id();

    @JsonProperty("sense")
    private IdType sense = new IdType();

    @JsonProperty("synBehavior")
    private IdType synBehavior = new IdType();

    @JsonProperty("partOfSpeech")
    private IdType partOfSpeech = new IdType();

    @JsonProperty("language")
    private Id language = new Id();

    @JsonProperty("canonicalForm")
    private IdType canonicalForm = new IdType();

    @JsonProperty("entry")
    private IdType entry = new IdType();

    @Override
    public String toString() {
        return "Context{" + "frequency=" + frequency + ", prepositionalAdjunct=" + prepositionalAdjunct + ", copulativeSubject=" + copulativeSubject + ", reference=" + reference + ", generatedBy=" + generatedBy + ", subjOfProp=" + subjOfProp + ", objOfProp=" + objOfProp + ", writtenRep=" + writtenRep + ", hasValue=" + hasValue + ", onProperty=" + onProperty + ", label=" + label + ", sense=" + sense + ", synBehavior=" + synBehavior + ", partOfSpeech=" + partOfSpeech + ", language=" + language + ", canonicalForm=" + canonicalForm + ", entry=" + entry + '}';
    }

}
