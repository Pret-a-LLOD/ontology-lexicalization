/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

/**
 *
 * @author elahi
 */
public class Conversion {
    
    public static Double stringToDouble(String string) throws Exception {
        try {
            string = cleanString(string);
            return Double.parseDouble(string);
        } catch (Exception ex) {
            throw new Exception("string To Double conversion fails!!"+ex.getMessage());
        }

    }
    
     public static Integer stringToInteger(String string) throws Exception {
        try {
            string = cleanString(string);
            return Integer.parseInt(string);
        } catch (Exception ex) {
            throw new Exception("string To Double conversion fails!!"+ex.getMessage());
        }

    }

    private static String cleanString(String string) {
        return string.replace("\"", "");
    }
    
}
