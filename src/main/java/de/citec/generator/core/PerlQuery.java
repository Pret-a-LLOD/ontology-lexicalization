/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.core;

import de.citec.generator.config.Configuration;
import de.citec.sc.generator.exceptions.PerlException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class PerlQuery {

    private  Boolean processSuccessFlag = false;
     private String configJson = null;

    public PerlQuery(String location, String scriptName,String jsonString) throws PerlException {
        this.configJson=jsonString;
        try { 
            runPerl(location,scriptName);
        } catch (InterruptedException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("Perl script is not working!!"+ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("process error exceptions!!"+ex.getMessage());
        }

    }

    public void runPerl(String location, String scriptName) throws IOException, InterruptedException {
        Runtime runTime = Runtime.getRuntime();
        //System.out.println("location + scriptName::" + location + scriptName);
        //String[] commands = {"perl", location + scriptName};
        String command = "perl "+ location + scriptName+" "+this.configJson;
        //System.out.println("command::"+command);
        Process process = runTime.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        // Read the output from the command
        System.out.println("reading DBpedia and processing data for ontology lexicalization:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        // Read any errors from the attempted command
        System.out.println("Error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }

        if (process.waitFor() == 0) {
            System.err.println("Process terminated ");
            processSuccessFlag = true;
        }

    }

    public  Boolean getProcessSuccessFlag() {
        return processSuccessFlag;
    }

    
}
