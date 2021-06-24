/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import java.util.*;  
import com.example.config.*;
import  main.*;


/**
 *
 * @author elahi
 */
public class ResponseTransfer {
    private  String baseDir = "/app/results-v4/";
    private  String resourceDir = "";
    
    private Map<String,String> lexicalEntries=new TreeMap<String,String>();
    
    
    public ResponseTransfer(Configuration config) {
        //this.lexicalEntries.put(Constants.dummylexicalEntry,Constants.dummyLemon);
        //this.lexicalEntries.put(Constants.dummylexicalEntry2,Constants.dummyLemon2);
        //this.lexicalEntries.put(Constants.dummylexicalEntry3,Constants.dummyLemon3);
        //PerlQuery PerlQuery=new PerlQuery();
        //String testString=PerlQuery.getResultString();
        //this.lexicalEntries.put(testString,testString);
        try {
            ProcessCsv process = new ProcessCsv();
            System.out.println("Processing files are ended!!!");
        } catch (Exception ex) {
          System.out.println("file process does not work!!!"+ex.getMessage());
        }
        
        

    }
    
    

    public Map<String,String> getLexicalEntries() {
        return this.lexicalEntries;
    }

    

}
