/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Result {

    private List<String> rows = new ArrayList<String>();
    private String content = "";
    private Boolean flag = false;
    private static String seperator = "+";

    public void resultStr(String outputDir, String lexicalElement, String parts_of_speech, String prediction, String interestingness, Map<String, String> prefix) throws Exception {
        lexicalElement = lexicalElement.toLowerCase().strip();

        Map<Double, Set<String>> sortedLines = new TreeMap<Double, Set<String>>();
        Map<String, List<String>> lexiconDic = fileToHash(outputDir, lexicalElement, parts_of_speech, interestingness, prediction);

        for (String key : lexiconDic.keySet()) {
            flag = true;
            List<String> LineInfos = lexiconDic.get(key);
            for (String lineinfo : LineInfos) {
                //System.out.println("lineinfo::" + lineinfo);
                 Double value = 0.0;
                try {
                    String doubleValue = lineinfo.split("\"" + ",")[2];
                    //System.out.println("double String::" + doubleValue);
                    doubleValue = doubleValue.replace("\"", "");
                    //System.out.println("After double quote::" + doubleValue);
                    value = Double.parseDouble(doubleValue);
                } catch (NumberFormatException ex) {
                    continue;
                }

                Set<String> tempList = new HashSet<String>();
                if (sortedLines.containsKey(value)) {
                    tempList = sortedLines.get(value);
                    tempList.add(lineinfo);
                    sortedLines.put(value, tempList);
                } else {
                    tempList.add(lineinfo);
                    sortedLines.put(value, tempList);
                }
            }
        }

        content = "";
        List<Double> keyValues = new ArrayList<Double>(sortedLines.keySet());
        Collections.sort(keyValues, Collections.reverseOrder());
        Integer index = 0;
        for (Double value : keyValues) {
            index = index + 1;
            Set<String> stringList = sortedLines.get(value);
            for (String string : stringList) {
                string = modifyLine(string);
                String line = string + "\n";
                rows.add(string);
                content += line;

            }
        }

        content = replaceString(prefix, content);

    }

    private Map<String, List<String>> fileToHash(String outputDir, String lexicalElement, String part_of_speech, String interestingness, String prediction) throws FileNotFoundException, IOException {
        Map<String, List<String>> classNameLines = new TreeMap<String, List<String>>();
        Process process = null;
        String className = null, line = null;
        File folder = new File(outputDir);
        //String[] listOfFiles = folder.list();
        //System.out.println(outputDir+" prediction:"+prediction+" interestingness:"+interestingness);
        List<String> listOfFiles = FileUtils.getSpecificFiles(outputDir, prediction, interestingness);
        if (listOfFiles.isEmpty()) {
            return new TreeMap<String, List<String>>();
        }

        try {
            for (String fileName : listOfFiles) {
                fileName = outputDir + fileName;
                String command = "grep -w " + lexicalElement + " " + fileName;
                //System.out.println("fileName::"+fileName);
                process = Runtime.getRuntime().exec(command);
                List<String> lines = new ArrayList<String>();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                Boolean flag=false;
                while ((line = br.readLine()) != null) {
                    if (line.contains("XMLSchema#integer")) {
                        continue;
                    }
                    className = line.split(",")[4];
                    flag=true;
                    lines.add(line);

                }
                if(flag)
                   classNameLines.put(className, lines);
            }

            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return classNameLines;
    }

    private static String modifyLine(String line) {
        String rankLine = line;
        if (line.contains(",")) {

            String[] info = line.split(",");
            String subject = info[8];
            String predicate = info[9];
            String object = info[10];
            subject = checkInvalid(subject, 8);
            predicate = checkInvalid(predicate, 9);
            object = checkInvalid(object, 10);
            
            String className="dbo:"+info[4].replace(" ", "");
            if (object.contains("@")) {
                object=removeFirstandLastRule(object);
            }

            rankLine = addQuote(checkLabel(getValue(subject))) + seperator
                    + addQuote(checkLabel(getValue(predicate))) + seperator
                    + addQuote(checkLabel(getValue(object))) + seperator
                    + addQuote(getValue(className)) + seperator;
            //System.out.println("info[10]::"+info[10]);

            List<String> list = getValueSpace(info[5]);
            for (String value : list) {
                rankLine += addQuote(value) + seperator;
            }
            String part_of_speech = formatPosTag(info[11]).strip().trim();
            String rule = info[12].replace("&", ",").strip().trim();
            
            if (rule.contains("@")) {
                rule=removeFirstandLastRule(rule);
                //System.out.println("rule::" + rule);

            }
               
            rankLine += addQuote(part_of_speech) + seperator + addQuote(rule);
            //System.out.println(rankLine);
            rankLine = rankLine.replace("\"", "");

        }
        return rankLine;
    }
    
    private static  String removeFirstandLastRule(String str) {
        

        if(str!=null&&str.length()>4){
           str = str.substring(1, str.length() - 1);
           str = str.replace("\"", "$"); 
           str=str.replace("$$", "$");
           return str;
        }
        /*else if(str.length()<4){
           String info[]=str.split("@");
            String label=info[0].replace("\"", "$");
            String lang=info[1];
        }*/
        return str;
    }
    
    private static  String removeFirstandLastObject(String str) {
        
         if (str.contains("@")) {
            String info[] = str.split("@");
            String label = info[0].replace("\"", "$");
            String lang = info[1];
            return label+"@"+lang;
        }
        /*else if(str.length()<4){
           String info[]=str.split("@");
            String label=info[0].replace("\"", "$");
            String lang=info[1];
        }*/
        return str;
    }

    private static String formatPosTag(String string) {
        string = string.replace("_", " ");
        return string;
    }

    public static String replaceString(Map<String, String> tempHash, String string) throws FileNotFoundException, IOException, Exception {
        for (String key : tempHash.keySet()) {
            String value = tempHash.get(key);
            string = string.replace(key, value + ":");
        }
        return string;
    }

    private static String getValue(String string) {

        string = string.replace("\"", "");

        if (string.contains("=")) {
            String[] info = string.split("=");
            return info[1].strip().trim();
        }

        return string.strip().stripLeading().stripTrailing();
    }

    private static String addQuote(String string) {
        return "\"" + string + "\"";
    }

  

    private static List<String> getValueSpace(String string) {
        List<String> arrayList = new ArrayList<String>();
        string = string.replace("\"", "");
        String[] info = string.split(" ");
        //System.out.println("!!!!!!!Start!!!!!!!!!:");
        for (String key : info) {
            //System.out.println("key:"+key);
            if (key.isEmpty()) {
                continue;
            }
            key = getValue(key);
            if (key.length() >= 4) {
                key = key.substring(0, 4);

            }
            arrayList.add(key);
        }
        //System.out.println("!!!!!!!End!!!!!!!!!:");

        return arrayList;
    }

    public Result() {
    }

    public List<String> getRows() {
        return rows;
    }

    public String getContent() {
        return content;
    }

    private static String checkInvalid(String string, Integer index) {
        if (string.contains("http") || string.contains("@")) {
            return string;
        }
        if (index == 8 || index == 9 || index == 10) {
            string = string.strip().trim().replace("\"", "");
            if (index == 8 && string.equals("e")) {
                return "";

            } else if (index == 9 && string.equals("p")) {
                return "";

            } else if (index == 10 && string.equals("o")) {
                return "";

            }
        }

        return string;
    }

    private static String checkLabel(String string) {

        if (string.contains("@")) {
            String[] info = string.split("@");
            string = "\"" + info[0] + "\"" + "@" + info[1];
            return string;
        }
        if (string.contains("http")) {
            return string;
        }

        return string;
    }


    /*
    key:Coherence=0.0354609929078014
    key:condAB=0.833333333333333
    key:condBA=0.037037037037037
    key:supA=6.0
    key:supAB=5.0
    key:supB=135.0

     */
 /*
    0 "located",
1 "dbo:location India", 
2 "0.161290322580645", 
3 " dbo:location India", 
4 "Monument", 
5 "MaxConf=0.161290322580645 condAB=0.0704225352112676 condBA=0.161290322580645 supA=71.0 supAB=5.0 supB=31.0", 
6 "JJ", 
7 "MaxConf", 
8 "e", 
9 "http://dbpedia.org/ontology/location", 
10 "http://dbpedia.org/resource/India", 
11 "JJ", "dbo:Monument in c_e and 'located' in l_e(c&p&so) => (e& dbo:location& dbr:India) in G",
     */
    public static void main(String[] args) {
        String line = "\"british_irish\",\"dbp:ruNationalteam British_and_Irish_Lions\", \"1.0\", \" dbp:ruNationalteam British_and_Irish_Lions\", \"RugbyPlayer\", \"MaxConf=1.0 condAB=1.0 condBA=0.0614525139664804 supA=11.0 supAB=11.0 supB=179.0\", \"JJ\", \"MaxConf\", \"e\", \"http://dbpedia.org/property/ruNationalteam\", \"\\\"british_irish\\\"\"+\"@en\", \"JJ_JJ\", \"dbo:RugbyPlayer in c_e and 'British and Irish' in l_e(c&p&so) => (e& dbp:ruNationalteam& dbr:British_and_Irish_Lions) in G\"";

        String check = checkInvalid("e", 8);
        String modifyLine = modifyLine(line);
        System.out.println(modifyLine);

        check = checkLabel("\"british_irish\"" + "@en");
        System.out.println(check);
    }

}
