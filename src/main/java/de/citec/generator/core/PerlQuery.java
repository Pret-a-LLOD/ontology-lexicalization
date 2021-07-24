/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.generator.config.ConfigLex;
import de.citec.generator.config.Constants;
import de.citec.sc.generator.exceptions.PerlException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class PerlQuery implements Constants {

    private Boolean processSuccessFlag = false;
    private String configJson = null;

    public PerlQuery(String location, String scriptName, String class_url) throws PerlException {
        try {
            System.out.println("Reading DBpedia abstract and knowledge graph and corpus based lexicalization!!\n");
            //System.out.println("Step 1. find frequent entities and process abstracts. Wait..." + "\n");
            this.runCommandLine(location, scriptName, class_url);
            /*if (runCommandLine(location, "frequentClass.pl", class_url)) {
                System.out.println("done with step 1." + "\n");
                if (runCommandLine(location, "tripleProcess.pl", class_url)) {
                    System.out.println("done with step 1." + "\n");
                }
            } else
                throw new PerlException("Step 1. failed to process");*/

        } catch (InterruptedException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("Perl script is not working!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(PerlQuery.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            throw new PerlException("process error exceptions!!" + ex.getMessage());
        }

    }

    public PerlQuery(String urlString) throws PerlException {
        this.configJson = urlString;

        /*try {
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
        }*/
    }

    public Boolean runCommandLine(String location, String scriptName, String class_url) throws IOException, InterruptedException {

        String command = "perl " + location + scriptName + " " + appDir + " " + class_url;
        Runtime runTime = Runtime.getRuntime();
        //System.out.println("location + scriptName::" + location + scriptName);
        //String[] commands = {"perl", location + scriptName};
        //System.out.println("command::"+command);
        Process process = runTime.exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        // Read the output from the command
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
            return true;
        } else {
            return false;
        }

    }

    public Boolean getProcessSuccessFlag() {
        return processSuccessFlag;
    }

    public String makeJsonString(ConfigLex config) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
    }

}
