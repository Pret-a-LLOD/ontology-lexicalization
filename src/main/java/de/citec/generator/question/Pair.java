/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

/**
 *
 * @author elahi
 */
public class Pair {
    private Boolean flag=false;
    private String value=null;
    
    public Pair(Boolean flag,String value){
        this.flag=flag;
        this.value=value;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getFlag() {
        return flag;
    }

    public String getValue() {
        return value;
    }
    
    
}
