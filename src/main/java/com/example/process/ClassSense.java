/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

/**
 *
 * @author elahi
 */
public class ClassSense {
    private String className="";
    private String property="";
    private String value="";
    
    public ClassSense(String classNameT, String propertyT,String valueT){
         this.className=classNameT;
         this.property=propertyT;
         this.value=valueT;
    
    }
    
    
    public String getClassName() {
        return className;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
    
}
