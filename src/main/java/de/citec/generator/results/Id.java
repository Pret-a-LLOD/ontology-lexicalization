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
public class Id {

    @JsonProperty("@id")
    private String id = null;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Id{" + "id=" + id + '}';
    }

}
