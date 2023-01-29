/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author elahi
 */
public class FileFolderUtils {

    public static String configDir = "src/main/resources/config/";
    public static String configFileName = "prefix.prop";

    public static void createDirectory(String location) throws IOException {
        Path location_path = Paths.get(location);
        Files.createDirectories(location_path);
    }

    public static List<File> getSpecificFiles(String fileDir, String category, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(category) && fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.err.println("file not found!!"+exp.getMessage());
            return new ArrayList<File>();
        }

        return selectedFiles;
    }

     public static BufferedReader getBufferedReaderForCompressedFile(File fileIn) throws FileNotFoundException, CompressorException {
         FileInputStream fin = new FileInputStream(fileIn);
        BufferedInputStream bis = new BufferedInputStream(fin);
        CompressorInputStream input = new CompressorStreamFactory().createCompressorInputStream(bis);
        BufferedReader br2 = new BufferedReader(new InputStreamReader(input));
        return br2;
    }
  
    public static void delete(File dir) throws Exception {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        } catch (Exception ex) {
           throw new Exception("file directory does not exist!!");
        }

    }
    
    public static void listToFiles(List<String> list, String fileName) {
        String str = "";
        Integer number = -1, index = 0;
        for (String element : list) {
            String line = element + "\n";
            str += line;
        }

        stringToFiles(str, fileName);

    }

    public static void stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void writeToTextFile(String str, String filename) {
        if (str != null) {
            stringToFiles(str, filename);
        } else {
            return;
        }
    }
    
     public static List<String> getSelectedFiles(String inputDir, String rulePattern) {
        String[] list = new File(inputDir).list();
        List<String> inputFiles = new ArrayList<String>();
        for (String fileString : list) {
            File file = new File(fileString);
            if (fileString.contains(rulePattern)) {
                inputFiles.add(file.getName());
            }
        }
        return inputFiles;
    }
     
    public static Map<String, String> fileList(String fileName, String value,Map<String, String> frameUris)  {
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = reader.readLine();
                line = line.strip().stripLeading().stripTrailing().trim();
                frameUris.put(line, value);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frameUris;
    }

    public static String getRootDir() {
        File currentDirFile = new File(".");
        return currentDirFile.getAbsolutePath().replace(".", "");
    }



}
