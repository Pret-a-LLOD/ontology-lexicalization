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
public class ConfigDownload {

    private String uri_abstract = null;
    private String uri_property = null;
    private String uri_instance = null;
    private String uri_label = null;
    private String uri_literal = null;
    private String uri_object = null;
    private String uri_anchor_sorted = null;
    private String uri_anchor = null;

    public String getUri_abstract() {
        return uri_abstract;
    }

    public String getUri_property() {
        return uri_property;
    }

    public String getUri_instance() {
        return uri_instance;
    }

    public String getUri_label() {
        return uri_label;
    }

    public String getUri_literal() {
        return uri_literal;
    }

    public String getUri_object() {
        return uri_object;
    }

    public String getUri_anchor_sorted() {
        return uri_anchor_sorted;
    }

    public String getUri_anchor() {
        return uri_anchor;
    }

    @Override
    public String toString() {
        return "ConfigDownload{" + "uri_abstract=" + uri_abstract + ", uri_property=" + uri_property + ", uri_instance=" + uri_instance + ", uri_label=" + uri_label + ", uri_literal=" + uri_literal + ", uri_object=" + uri_object + ", uri_anchor_sorted=" + uri_anchor_sorted + ", uri_anchor=" + uri_anchor + '}';
    }

    
}
