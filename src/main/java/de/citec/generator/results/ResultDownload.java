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

    private String url = null;
    private String status = null;

    public ResultDownload(String url, String status) {
        this.url = url;
        this.status=status;
    }

    public String getLinked_data() {
        return url;
    }

    public String getStatus() {
        return status;
    }
}
