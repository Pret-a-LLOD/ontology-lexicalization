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
    private  String baseDir = "results-v4/";
    private static String location = "perl/";
    private static String scriptName = "experiment.pl";
    private static String processData="processData/";

    
    private Map<String,String> lexicalEntries=new TreeMap<String,String>();
    
    
    public ResponseTransfer(Configuration config) {
        //this.lexicalEntries.put(Constants.dummylexicalEntry,Constants.dummyLemon);
        //this.lexicalEntries.put(Constants.dummylexicalEntry2,Constants.dummyLemon2);
        //this.lexicalEntries.put(Constants.dummylexicalEntry3,Constants.dummyLemon3);
          /*PerlQuery PerlQuery=new PerlQuery(location,scriptName);
          String testString=PerlQuery.getResultString();
          this.lexicalEntries.put(testString,testString);*/
        try {
            String resourceDir = baseDir+processData;
            ProcessCsv process = new ProcessCsv(baseDir,resourceDir);
            System.out.println("Processing files are ended!!!");
        } catch (Exception ex) {
          System.out.println("file process does not work!!!"+ex.getMessage());
        }
        
        

    }
    
    

    public Map<String,String> getLexicalEntries() {
        return this.lexicalEntries;
    }

    

}
