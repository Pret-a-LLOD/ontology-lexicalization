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

    public String getUri_abstract() {
        return uri_abstract;
    }

    public String getUri_property() {
        return uri_property;
    }

    @Override
    public String toString() {
        return "ConfigDownload{" + "uri_abstract=" + uri_abstract + ", uri_property=" + uri_property + '}';
    }

}
