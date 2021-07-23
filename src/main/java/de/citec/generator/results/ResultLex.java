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
public class ResultLex {

    @JsonProperty("class_name")
    private String className = null;
    @JsonProperty("status")
    private String status  =null;
    
    public ResultLex(String className,Boolean flag){
        this.className=className;
        if(flag)
            this.status="Successfull complete lexicalization!!";
        else 
           this.status="Lexicalization process failed!!"; 
        
    }
    
    public ResultLex(String className, String linked_data, String status) {
        this.className = className;
        this.status = status;
    }
    public String getClassName() {
        return className;
    }

}