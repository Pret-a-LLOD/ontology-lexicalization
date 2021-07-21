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
public class Configuration {
    private String class_name = null;
    private String linked_data=null;
    private String uri_basic = null;
    private String uri_abstract = null;
    private String uri_property = null;
    private Integer min_entities_per_class = 0;
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
    private Integer rank_limit = 0;


    public String getClass_name() {
        return class_name;
    }

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

    public Integer getRank_limit() {
        return rank_limit;
    }

    public String getUri_basic() {
        return uri_basic;
    }

    public String getUri_abstract() {
        return uri_abstract;
    }

    public String getUri_property() {
        return uri_property;
    }

    public String getClassName() throws Exception {
        if (this.class_name.isEmpty()) {
            throw new Exception("The class is invalid!!!:");
        } else if (this.class_name.contains("http:")) {
            return this.class_name.substring(class_name.lastIndexOf("/") + 1);
        } else if (class_name.contains("dbo:")) {
            String[] info = class_name.split(":");
            return info[1];
        } else {
            return this.class_name;
        }
    }

    public String getLinked_data() {
        return linked_data;
    }

    @Override
    public String toString() {
        return "Configuration{" + "class_name=" + class_name + ", linked_data=" + linked_data + ", uri_basic=" + uri_basic + ", uri_abstract=" + uri_abstract + ", uri_property=" + uri_property + ", min_entities_per_class=" + min_entities_per_class + ", max_entities_per_class=" + max_entities_per_class + ", min_onegram_length=" + min_onegram_length + ", min_pattern_count=" + min_pattern_count + ", min_anchor_count=" + min_anchor_count + ", min_propertyonegram_length=" + min_propertyonegram_length + ", min_propertypattern_count=" + min_propertypattern_count + ", min_propertystring_length=" + min_propertystring_length + ", max_propertystring_length=" + max_propertystring_length + ", min_supA=" + min_supA + ", min_supB=" + min_supB + ", min_supAB=" + min_supAB + ", rank_limit=" + rank_limit + '}';
    }

}
