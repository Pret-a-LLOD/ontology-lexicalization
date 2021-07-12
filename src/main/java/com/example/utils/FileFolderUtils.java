/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            System.out.println("file not found!!");
            return new ArrayList<File>();
        }

        return selectedFiles;
    }

    public static List<File> getSpecificFiles(String fileDir, String prediction, String inter, String posTag, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(inter) && fileName.contains(prediction) && fileName.contains(posTag) && fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
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
            System.out.println("file not found!!:" + fileName + " " + ex.getMessage());
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public static Map<String, String> getHash(String fileName, String symbol) throws FileNotFoundException, IOException, Exception {
        Map<String, String> tempHash = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
        System.out.println("Now the output is redirected!" + fileName);
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
            throw new Exception("No prefix found!!" + e.getMessage());
        }
        return tempHash;
    }

    public static Map<String, String> getHash(String fileName, String symbol1, String symbol2) throws FileNotFoundException, IOException, Exception {
        Map<String, String> tempHash = new TreeMap<String, String>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = line.replace(symbol1, "$");
                if (line.contains(symbol2)) {
                    String[] info = line.split(symbol2);
                    String key = info[0].trim().strip().replace("$", symbol1).replace(" ", "_");
                    String value = info[1].trim().strip().replace("$", symbol1).replace(" ", "_");
                    tempHash.put(key, value);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new Exception("No prefix found!!" + e.getMessage());
        }
        return tempHash;
    }

  }
