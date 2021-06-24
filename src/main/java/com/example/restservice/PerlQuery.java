/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author elahi
 */
public class PerlQuery {

    private static String resultString = null;

    public PerlQuery(String location, String scriptName) {
        if (runPerl(location,scriptName)) {
            this.resultString = "working perl1";
        } else {
            this.resultString = "not working!!";
        }

    }

    public static Boolean runPerl(String location,String scriptName) {

        try {

            Runtime rt = Runtime.getRuntime();
            String[] commands = {"perl", location + scriptName};
            Process proc = rt.exec(commands);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

// Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

// Read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (Exception e) {
            System.err.println("error executing  " + e.getMessage());
            return false;
        }

        return true;

    }

    public static String getResultString() {
        return resultString;
    }

}
