/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import java.util.*;  

/**
 *
 * @author elahi
 */
public class ResponseTransfer {
    
    private Map<String,String> lexicalEntries=new TreeMap<String,String>();
    
    
    public ResponseTransfer(Configuration config) {
        //this.lexicalEntries.put(Constants.dummylexicalEntry,Constants.dummyLemon);
        //this.lexicalEntries.put(Constants.dummylexicalEntry2,Constants.dummyLemon2);
        //this.lexicalEntries.put(Constants.dummylexicalEntry3,Constants.dummyLemon3);
        PerlQuery PerlQuery=new PerlQuery();
        String testString=PerlQuery.Test();
        this.lexicalEntries.put(testString,testString);


    }

    public Map<String,String> getLexicalEntries() {
        return this.lexicalEntries;
    }

    

}
