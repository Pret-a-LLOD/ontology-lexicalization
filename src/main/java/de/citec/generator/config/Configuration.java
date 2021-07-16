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

import java.util.*;



public class Configuration {

    private Integer min_entities_per_class = 100;
    private Integer max_entities_per_class = 10000;
    private Integer min_onegram_length = 4;
    private Integer min_pattern_count = 5;
    private Integer min_anchor_count = 10;
    private Integer min_propertyonegram_length = 4;
    private Integer min_propertypattern_count = 5;
    private Integer min_propertystring_length = 5;
    private Integer max_propertystring_length = 50;
    private Integer min_supA = 5;
    private Integer min_supB = 5;
    private Integer min_supAB = 5;
    private Map<String, Integer> rulepattern = new TreeMap<String, Integer>();
    private String baseUri = "http://localhost:8080/";
    private Integer rank_limit = 20;
    /*private Integer min_entities_per_class = 0;
    private Integer max_entities_per_class = 0;
    private Integer min_onegram_length = 0;
    private Integer min_pattern_count = 0;
    private Integer min_anchor_count = 0;
    private Integer min_propertyonegram_length = 0;
    private Integer min_propertypattern_count = 0;
    private Integer min_propertystring_length = 0;
    private Integer max_propertystring_length = 0;
    private Integer min_supA = 0;
    private Integer min_supB = 0;
    private Integer min_supAB = 0;
    private Map<String, Integer> rulepattern = new TreeMap<String, Integer>();
    private String baseUri = null;
    private Integer rank_limit = 0;*/

    public Integer getMin_entities_per_class() {
        return min_entities_per_class;
    }

    public Integer getMax_entities_per_class() {
        return max_entities_per_class;
    }

    public Integer getMin_onegram_length() {
        return min_onegram_length;
    }

    public Integer getMin_pattern_count() {
        return min_pattern_count;
    }

    public Integer getMin_anchor_count() {
        return min_anchor_count;
    }

    public Integer getMin_propertyonegram_length() {
        return min_propertyonegram_length;
    }

    public Integer getMin_propertypattern_count() {
        return min_propertypattern_count;
    }

    public Integer getMin_propertystring_length() {
        return min_propertystring_length;
    }

    public Integer getMax_propertystring_length() {
        return max_propertystring_length;
    }

    public Integer getMin_supA() {
        return min_supA;
    }

    public Integer getMin_supB() {
        return min_supB;
    }

    public Integer getMin_supAB() {
        return min_supAB;
    }

    public Map<String, Integer>getRulepattern() {
        return rulepattern;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public Integer getRankLimit() {
        return rank_limit;
    }

}
