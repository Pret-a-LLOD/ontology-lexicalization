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
public class ConfigLemon {

    private String uri_basic = null;
    private Integer rank_limit = 0;

    public String getUri_basic() {
        return uri_basic;
    }

    public Integer getRank_limit() {
        return rank_limit;
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
