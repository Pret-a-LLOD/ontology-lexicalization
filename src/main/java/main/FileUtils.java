/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class FileUtils {
      public static String configDir = "src/main/resources/config/";
     public static String configFileName = "prefix.prop";

    public static List<String> getSpecificFiles(String fileDir, String prediction, String extension) {
        List<String> selectedFiles = new ArrayList<String>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(extension) && fileName.contains(prediction)) {
                    selectedFiles.add(fileName);
                }
            }

        } catch (Exception exp) {
            System.out.println("file not found!!");
            return selectedFiles;
        }

        return selectedFiles;
    }
    
     public static List<File> getSpecificFiles(String fileDir, String prediction, String inter,String posTag,String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(inter) && fileName.contains(prediction)&& fileName.contains(posTag)&& fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir+fileName));
                }
            }

        } catch (Exception exp) {
            System.out.println("file not found!!");
            return selectedFiles;
        }

        return selectedFiles;
    }
    
     public static Boolean stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
            return true;
        } catch (IOException ex) {
             System.out.println("file not found!!:"+fileName+" "+ex.getMessage());
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
     
   

     
    public static Map<String, String> getHash(String fileName,String symbol) throws FileNotFoundException, IOException, Exception {
        Map<String, String> tempHash = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
              System.out.println("Now the output is redirected!"+fileName);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                if (line.contains(symbol)) {
                    String[] info = line.split(symbol);
                    String key = info[0].trim().strip();
                    String value = info[1].trim().strip();
                    tempHash.put(key, value);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new Exception("No prefix found!!"+e.getMessage());
        }
        return tempHash;
    }
    
      public static Map<String, String> getHash(String fileName,String symbol1, String symbol2) throws FileNotFoundException, IOException, Exception {
        Map<String, String> tempHash = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line=line.replace(symbol1, "$");
                if (line.contains(symbol2)) {
                    String[] info = line.split(symbol2);
                    String key = info[0].trim().strip().replace("$",symbol1).replace(" ","_");
                    String value = info[1].trim().strip().replace("$",symbol1).replace(" ","_");
                    tempHash.put(key, value);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new Exception("No prefix found!!"+e.getMessage());
        }
        return tempHash;
    }
    
    public static void main(String []args) throws Exception{
        Map<String,String> temp=getHash(configDir+configFileName,"=");
        String string="+http://xmlns.com/foaf/0.1/name+@en+SoccerTournament+0.03+0.83+0.03+6.0+5.0+135.+JJ CD+dbo:SoccerTournament in c_e and 'total of 28' in l_e(c,p,os) => (e, foaf:name, @en) in G";
        string=Result.replaceString(temp,string);
                System.out.println(string);

    }

}
