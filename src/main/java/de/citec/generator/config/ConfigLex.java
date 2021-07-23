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
    


    public String getClass_url() throws Exception {
        return checkClass(class_url);
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

    public String checkClass(String class_url) throws Exception {
        if (class_url.isEmpty()) {
            throw new Exception("The class is invalid!!!:");
        } else if (class_url.contains("http:")) {
            return class_url.substring(class_url.lastIndexOf("/") + 1);
        } else if (class_url.contains("dbo:")) {
            String[] info = class_url.split(":");
            return info[1];
        } else {
            return class_url;
        }
    }
   
}
