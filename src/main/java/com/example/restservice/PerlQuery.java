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

    //private static String location = "/home/elahi/a-teanga/dummy_teanga_service_java/";
    private static String location = "code-v3/";
    private static String scriptName = "experiment.pl";
    //private static String scriptName = "HelloWorld.pl";

    private static String resultString = null;

    public PerlQuery() {
        if (Test()) {
            this.resultString = "working perl1";
        } else {
            this.resultString = "not working!!";
        }

    }

    public static Boolean Test() {
        
        /*final String command = "/bin/tar -xvf " + zipFile + " " + filesString;
        logger.info("Start unzipping: {}    into the folder {}", command, folder.getPath());
        final Runtime r = Runtime.getRuntime();
        final Process p = r.exec(command, null, folder);
        final int returnCode = p.waitFor();*/

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

        /*String[] aCmdArgs = {"perl", location + scriptName};
        Runtime oRuntime = Runtime.getRuntime();
        Process oProcess = null;

        try {
            oProcess = oRuntime.exec(aCmdArgs);
            int value=oProcess.waitFor();
            System.out.println("value::"+value);
            return true;
        } catch (Exception e) {
            System.err.println( "error executing " + aCmdArgs[0]+" "+e.getMessage());
            return false;
        }*/

 /* try {
            String str = "";
            BufferedReader is = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
            String sLine;
            while ((sLine = is.readLine()) != null) {
                System.out.println(sLine);
                str += sLine;
            }
            System.out.flush();

            System.err.println("The process not finished!!!"+ "Exit status=" + oProcess.exitValue()+" "+str);
            return str;

        } catch (Exception e) {
            return "readin failure executing!! "+ e.getMessage();

        }*/
    }

    public static String getResultString() {
        return resultString;
    }

}
