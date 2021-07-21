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
public class LexProcessResult {

    @JsonProperty("class_name")
    private String className = null;
    @JsonProperty("linked_data")
    private String linked_data  =null;
    @JsonProperty("status")
    private String status  =null;
    
    public LexProcessResult(String className,String linked_data,Boolean flag){
        this.className=className;
        this.linked_data=linked_data;
        if(flag)
            this.status="Successfull complete lexicalization!!";
        else 
           this.status="Lexicalization process failed!!"; 
        
    }
    public String getClassName() {
        return className;
    }

    public String getLinked_data() {
        return linked_data;
    }
    

}
