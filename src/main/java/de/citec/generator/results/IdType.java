/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.results;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author elahi
 */
public class IdType {

    @JsonProperty("@id")
    private String id = null;
    @JsonProperty("@type")
    private String type = null;

    public IdType() {

    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "IdType{" + "id=" + id + ", type=" + type + '}';
    }

}
