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
    private static String location = "perl/";
    private static String scriptName = "HelloWorld.pl";


    private static String resultString = null;

    public PerlQuery() {
        this.resultString = Test();
    }

    public static String Test() {
        String[] aCmdArgs = {"perl", location + "experiment.pl"};
        Runtime oRuntime = Runtime.getRuntime();
        Process oProcess = null;

        try {
            oProcess = oRuntime.exec(aCmdArgs);
            oProcess.waitFor();
        } catch (Exception e) {
            System.out.println("error executing " + aCmdArgs[0]);
        }

        try {
            /* dump output stream */
            String str = "";
            BufferedReader is = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
            String sLine;
            while ((sLine = is.readLine()) != null) {
                System.out.println(sLine);
                str += sLine;
            }
            System.out.flush();

            /* print final result of process */
            System.err.println("Exit status=" + oProcess.exitValue());
            return str;

        } catch (Exception e) {
            System.out.println("readin failure executing!! ");
            return "readin failure executing!! ";

        }

    }

    public static String getResultString() {
        return resultString;
    }

}
