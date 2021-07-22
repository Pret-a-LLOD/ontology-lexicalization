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
public class ResultDownload {

    @JsonProperty("linked_data")
    private String linked_data = null;
    @JsonProperty("status")
    private String status = null;

    public ResultDownload(String linked_data, String status) {
        this.linked_data = linked_data;
        this.status=status;
    }

    public String getLinked_data() {
        return linked_data;
    }

    public String getStatus() {
        return status;
    }
}
