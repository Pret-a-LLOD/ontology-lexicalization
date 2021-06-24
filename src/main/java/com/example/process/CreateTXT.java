/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

import com.example.analyzer.Lemmatizer;
import com.example.utils.FileFolderUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author elahi
 */
public class CreateTXT implements NullInterestingness{

   
    public static String resultStrTxt(Set<String> posTag,String inputDir,String outputDirT, String prediction,  Lemmatizer lemmatizer,String interestingness) throws Exception {
        String stringAdd = "";
       
        
        /*Set<String> adjectives=new TreeSet<String>();
        Set<String> verbs=new TreeSet<String>();
        Set<String> nouns=new TreeSet<String>();*/

        
        for(String parts_of_speech:posTag){
             List<File> files = FileFolderUtils.getSpecificFiles(inputDir, "-"+parts_of_speech+"-");
        if (!files.isEmpty()) {
            for (File file : files) {
                stringAdd = "";
                //System.out.println("file:"+file.getName());
              
                Map<String, LexiconUnit> lexiconDic = getLexiconTxt(file, parts_of_speech, lemmatizer);
                for (String lexical : lexiconDic.keySet()) {
                    LexiconUnit lexiconUnit = lexiconDic.get(lexical);
                    //System.out.println("lexical:"+lexical+" postag:"+lexiconUnit.getPartsOfSpeech());
                    //String partOfSpeech = lexiconUnit.getPartsOfSpeech();

                    /*if (partOfSpeech.contains("JJ")) {
                        adjectives.add(lexical);
                    } else if (partOfSpeech.contains("VB")) {
                        verbs.add(lexical);
                    } else if (partOfSpeech.contains("NN")) {
                        nouns.add(lexical);
                    }*/
                    
                    lexical="\""+ lexical +"\"";
                    String lines = "";
                    for (Integer index : lexiconUnit.getEntityInfos().keySet()) {
                        List<String> resultList = lexiconUnit.getEntityInfos().get(index);
                        String line = "";
                        for (Integer i = 0; resultList.size() > i; i++) {
                            String value = resultList.get(i);
                            //System.out.println("value:"+value);
                            
                            if(i==4){
                              value= value.replace("{", "");
                              value= value.replace("}", "");
                            }
                            else if (value.contains("=")) {
                                 String att = value.split("=")[0];
                                 value = value.split("=")[1];
                                  if(att.contains("string")){
                                     value=value.replace("$", "=");

                                  }
                            }
                            
                           /*if (i == 0) {
                                //String replaceStr =getAtrribute(value);
                                String key = EvaluationTriple.getType(prediction);
                                value = value.replace("kb=", key + "=");

                            }*/
                           

                            value ="\""+ value +"\""+ ", ";
                            line += value;
                        }
                        lines += lexical + "," + line + "\n";
                    }
                    stringAdd += lines;
                }
            String csvFileName=    outputDirT+file.getName().replace(".json","");
            System.out.println(csvFileName);
            FileFolderUtils.writeToTextFile(stringAdd, csvFileName);
           
            }
        }
            
        }
        /*String nounFileName=outputDirT+"z_NN_"+prediction+"_"+interestingness+".txt";
        String adjectiveFileName=outputDirT+"z_JJ_"+prediction+"_"+interestingness+".txt";
        String verbFileName=outputDirT+"z_VB"+prediction+"_"+interestingness+".txt";

        listToFiles(nouns,nounFileName);
        listToFiles(adjectives,adjectiveFileName);
        listToFiles(verbs,verbFileName);*/

       
         return stringAdd;
    }
    
      private static Map<String, LexiconUnit> getLexiconTxt(File file, String posTag, Lemmatizer lemmatizer) throws IOException {
        Map<String, LexiconUnit> lexiconDic = new TreeMap<String, LexiconUnit>();

        ObjectMapper mapper = new ObjectMapper();
        List<LexiconUnit> lexiconUnits = new ArrayList<LexiconUnit>();

        lexiconUnits = mapper.readValue(file, new TypeReference<List<LexiconUnit>>() {
        });
        for (LexiconUnit lexiconUnit : lexiconUnits) {
            List<LexiconUnit> modifyLexiconUnits = new ArrayList<LexiconUnit>();
            String word = lexiconUnit.getWord();

            word = lemmatizer.getGeneralizedPosTagLemma(word, posTag);
            word = word.replaceAll("\\d", " ");
            word = word.replaceAll("[^a-zA-Z0-9]", " ");
            word = word.strip().trim();
            word = word.replaceAll(" ", "_");

            if (lexiconDic.containsKey(word)) {
                LexiconUnit existLexiconUnit = lexiconDic.get(word);
                LexiconUnit newLexiconUnit = new LexiconUnit(existLexiconUnit, lexiconUnit);
                lexiconDic.put(word, newLexiconUnit);

            } else {
                lexiconDic.put(word, lexiconUnit);
            }

        }
        return lexiconDic;

    }
    
    public static void listToFiles(Set<String> list, String fileName) {
        String str = "";
        Integer number = -1, index = 0;
        if (list.isEmpty()) {
            return;
        }
        for (String element : list) {
            index++;
            String line = element + "\n";
            str += line;
            if (index == number) {
                break;
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            System.out.println("no file is found!!::"+ex.getMessage());
            Logger.getLogger(FileFolderUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
