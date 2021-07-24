/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

import de.citec.sc.generator.exceptions.ConfigException;
import edu.stanford.nlp.util.Pair;


/**
 *
 * @author elahi
 */
public class ConfigLex {
    private String class_url = null;
    private Integer minimum_entities_per_class = 0;
    private Integer maximum_entities_per_class = 0;
    private Integer minimum_onegram_length = 0;
    private Integer minimum_pattern_count = 0;
    private Integer minimum_anchor_count = 0;
    private Integer minimum_propertyonegram_length =0;
    private Integer minimum_propertypattern_count = 0;
    private Integer minimum_propertystring_length = 0;
    private Integer maximum_propertystring_length = 0;
    private Integer minimum_supportA = 0;
    private Integer minimum_supportB = 0;
    private Integer minimum_supportAB = 0;    

 
    public String getClass_url() throws ConfigException  {
        Pair<Boolean, String> check = checkClass(class_url);
        if (check.first()) {
            this.class_url = check.second();
            return this.class_url;
        } else {
            throw new ConfigException("");
        }
    }

    public Integer getMinimum_entities_per_class() {
        return minimum_entities_per_class;
    }

    public Integer getMaximum_entities_per_class() {
        return maximum_entities_per_class;
    }

    public Integer getMinimum_onegram_length() {
        return minimum_onegram_length;
    }

    public Integer getMinimum_pattern_count() {
        return minimum_pattern_count;
    }

    public Integer getMinimum_anchor_count() {
        return minimum_anchor_count;
    }

    public Integer getMinimum_propertyonegram_length() {
        return minimum_propertyonegram_length;
    }

    public Integer getMinimum_propertypattern_count() {
        return minimum_propertypattern_count;
    }

    public Integer getMinimum_propertystring_length() {
        return minimum_propertystring_length;
    }

    public Integer getMaximum_propertystring_length() {
        return maximum_propertystring_length;
    }

    public Integer getMinimum_supA() {
        return minimum_supportA;
    }

    public Integer getMinimum_supB() {
        return minimum_supportB;
    }

    public Integer getMinimum_supAB() {
        return minimum_supportAB;
    }

    public Pair<Boolean,String> checkClass(String class_url)  {
        if (class_url.isEmpty()) {
            return new Pair<Boolean,String>(Boolean.FALSE,class_url);
        } else if (class_url.contains("http:")) {
            class_url=class_url.substring(class_url.lastIndexOf("/") + 1);
             return new Pair<Boolean,String>(Boolean.TRUE,class_url);
        } else if (class_url.contains("dbo:")) {
            String[] info = class_url.split(":");
             class_url= info[1];
             return new Pair<Boolean,String>(Boolean.TRUE,class_url);
        } else {
            return new Pair<Boolean,String>(Boolean.FALSE,class_url);
        }
    }
   
}
