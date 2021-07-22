/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.core;

import de.citec.generator.config.ConfigDownload;
import de.citec.generator.config.Constants;
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
public class PerlQuery implements Constants {

    private  Boolean processSuccessFlag = false;
     private String configJson = null;

    public PerlQuery(String location, String scriptName,String jsonString) throws PerlException {
        this.configJson=jsonString;
        try { 
            String command = "perl "+ location + scriptName+" "+this.configJson+" "+appDir;
            runCommandLine(command);
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
    
    public PerlQuery(String urlString) throws PerlException {
        this.configJson = urlString;

        try {
            String command = "wget --reject=\"index.html*\" " + urlString + " -P " + inputAbstract;
            System.out.print("downloading "+urlString+" ......");
            System.out.print("It may take some time .... ");
            runCommandLine(command);
            System.out.print("download is finished!");
        } catch (InterruptedException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("download failed!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("process error exceptions!!" + ex.getMessage());
        }

    }

    public void runCommandLine(String command) throws IOException, InterruptedException {
        Runtime runTime = Runtime.getRuntime();
        //System.out.println("location + scriptName::" + location + scriptName);
        //String[] commands = {"perl", location + scriptName};
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
