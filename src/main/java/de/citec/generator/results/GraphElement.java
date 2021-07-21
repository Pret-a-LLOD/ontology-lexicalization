/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class GraphElement {
    @JsonProperty("@id")
    private String id = null;

    @JsonProperty("@type")
    private String type = null;

    @JsonProperty("entry")
    private List<String> entries = new ArrayList<String>();

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<String> getEntries() {
        return entries;
    }
  }