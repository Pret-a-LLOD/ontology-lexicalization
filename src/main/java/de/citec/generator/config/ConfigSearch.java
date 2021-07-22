/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

import de.citec.sc.generator.analyzer.TextAnalyzer;

/**
 *
 * @author elahi
 */
public class ConfigSearch {

    private String term_to_search = "born";
    private String parts_of_speech = TextAnalyzer.NOUN;

    public String getTerms() {
        return term_to_search;
    }

    public String getParts_of_speech() {
        return parts_of_speech;
    }

}
