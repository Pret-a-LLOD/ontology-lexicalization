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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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
            System.err.println("file not found!!" + exp.getMessage());
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
            //throw new Exception("file directory does not exist!!");
        }

    }
     public static void delete(File dir,String extension) throws Exception {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if(file.getName().contains(extension))
                   file.delete();
            }
        } catch (Exception ex) {
            //throw new Exception("file directory does not exist!!");
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

    public static List<String> getSelectedFiles(String inputDir, String rulePattern, String extension) {
        String[] list = new File(inputDir).list();
        List<String> inputFiles = new ArrayList<String>();
        for (String fileString : list) {
            File file = new File(fileString);
            if (fileString.contains(rulePattern) && fileString.contains(rulePattern)) {
                inputFiles.add(file.getName());
            }
        }
        return inputFiles;
    }

    public static Map<String, String> fileList(String fileName, String value) {
        BufferedReader reader;
        String line = "";
        Map<String, String> frameUris = new TreeMap<String, String>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                //line = reader.readLine();
                line = line.strip().stripLeading().stripTrailing().trim();
                frameUris.put(line, value);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frameUris;
    }
    
    public static LinkedHashMap<String, List<String>> fileToHash(String fileName, String divide) {
        BufferedReader reader;
        String line = "";
         Integer index = 0, grammarNumber=1;
            List<String> lexs = new ArrayList<String>();
        LinkedHashMap<String, List<String>> frameUris = new LinkedHashMap<String, List<String>>();
        try {
           
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                if (line.contains(divide)) {
                    String info[] = line.split(divide);
                    String key = info[0].strip().stripLeading().stripTrailing().trim();
                    lexs.add(key);
                    if (index >= 40) {
                        String grammarDir = "multilingual-grammar-generator" + grammarNumber + "/";
                        frameUris.put(grammarDir, lexs);
                        index = 0;
                        lexs = new ArrayList<String>();
                        grammarNumber=grammarNumber+1;
                    } else {
                        index = index + 1;
                    }
                }

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!lexs.isEmpty()){
            String grammarDir = "multilingual-grammar-generator" + grammarNumber + "/";
            frameUris.put(grammarDir, lexs);
        }
        return frameUris;
    }
    
    
    public static Map<String, Set<String>> findParameters(String fileName) {
        BufferedReader reader;
        String line = "";
        Map<String, Set<String>> frameUris = new TreeMap<String, Set<String>>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = line.strip().stripLeading().stripTrailing().trim();
                String[]info=line.split(",");
                Set<String> set=new TreeSet<String>();
                String first=filter(info[0]);
                String second=filter(info[1]);
                String third=filter(info[2]);
                String parameter=first+"-"+second+"-"+third;
                set.add(first);
                set.add(second);
                set.add(third);
                frameUris.put(parameter, set);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frameUris;
    }
    
    public static String filter(String str) {
        return str.strip().stripLeading().stripTrailing().trim().replace("[", "").replace("]", "");
    }

    public static String getRootDir() {
        File currentDirFile = new File(".");
        return currentDirFile.getAbsolutePath().replace(".", "");
    }

    public static void hashMapToFile(Map<Double, String> sortLexEntry, String fileName) {
        String str = "";
        for (Double value : sortLexEntry.keySet()) {
            String line = value.toString() + sortLexEntry.get(value) + "\n";
            str += line;
        }

        stringToFiles(str, fileName);
    }

    public static Map<String, List<PairValues>> filetoTabDelimiatedResult(String fileName) {
        BufferedReader reader;
        String line = "";
        Map<String, List<PairValues>> map = new TreeMap<String, List<PairValues>>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = line.strip().stripLeading().stripTrailing().trim();
                List<PairValues> list = new ArrayList<PairValues>();
                if (line.contains("\t")) {
                    //System.out.println("line:"+line);
                    String[] info = line.split("\t");
                    String referene = info[0];
                    PairValues pair = new PairValues(info[1], info[2]);
                    list.add(pair);
                    map.put(referene, list);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    
    

    public static void deleteFiles(String[] dirs) {
        for (String dir : dirs) {
            File dirFile = new File(dir);
            String[] files = dirFile.list();
            for (String fileName : files) {
                File file = new File(dir + fileName);
                if (file.delete()) {
                    System.out.println("Deleted the file: " + fileName);
                } else {
                    System.out.println("Failed to delete the file." + fileName);
                }
            }

        }

    }
    
    public static void main(String[] args) {
        String fileName = "src/main/resources/a-nounPP.txt";
        String outputFile = null;
        Map<String, String> map = new TreeMap<String, String>();
        map.put("NounPPFrame", "src/main/resources/a-nounPP.txt");
        map.put("TransitiveFrame", "src/main/resources/a-transitive.txt");
        map.put("InTransitivePPFrame", "src/main/resources/a-intransitivePP.txt");
        String str = "";
        String header = "#!/bin/sh" + "\n";
        /*for (String key : map.keySet()) {
            fileName = map.get(key);
            List<String[]> nounPpParameters = FileFolderUtils.filetoTabDelimiatedStringArray(fileName, key);
            for (String[] row : nounPpParameters) {
                String grepKommend = "grep '" + row[1] + "' " + row[2] + "*.csv >>" + "lexEntries/" + row[0] + "-" +row[2]+ "-"+key + ".csv" + "\n";
                str += grepKommend;
            }
            outputFile = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/propertyQaldLex/" + key + ".sh";
            stringToFiles(header + str, outputFile);
        }

        System.out.println(header + str);
        System.out.println(outputFile);*/
        
         for (String key : map.keySet()) {
            fileName = map.get(key);
            List<String[]> nounPpParameters = FileFolderUtils.filetoTabDelimiatedStringArray(fileName, key);
            for (String[] row : nounPpParameters) {
                String grepKommend = "grep '" + row[1] + "' " + "*.csv >>" + "lexEntries/" + row[0] + "-" +row[2]+ "-"+key + ".csv" + "\n";
                str += grepKommend;
            }
            outputFile = "/media/elahi/Elements/A-project/LDK2023/resources/ldk/lexicon_last_4/" + key + ".sh";
            stringToFiles(header + str, outputFile);
        }

        System.out.println(header + str);
        System.out.println(outputFile);

    }
    
    public static List<String[]> filetoTabDelimiatedStringArray(String fileName, String type) {
        BufferedReader reader;
        String line = "";
        List<String[]> rows = new ArrayList<String[]>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = line.strip().stripLeading().stripTrailing().trim();
                if (line.contains("\t")) {
                    String[] info = line.split("\t");

                    if (info.length > 2) {
                        System.out.println(line+" "+info.length+" "+type);

                        if (type.contains("NounPPFrame")) {
                            rows.add(new String[]{info[0], info[1], info[2]});
                        }
                        else if (type.contains("TransitiveFrame")) {
                            System.out.println(line);
                            rows.add(new String[]{info[0], info[1], info[2]});
                        }
                         else if (type.contains("InTransitivePPFrame")) {
                            System.out.println(line);
                            rows.add(new String[]{info[0], info[1], info[2]});
                        }
                    }

                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    
   
}
