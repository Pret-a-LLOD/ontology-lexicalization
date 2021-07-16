/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

/**
 *
 * @author elahi
 */
public class PairCheck {
    private Boolean key=null;
     private String value=null;
     
     public PairCheck(Boolean key,String value){
           this.key=key;
           this.value=value;
         
     }

    public Boolean getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    
}
