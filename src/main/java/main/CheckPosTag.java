/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import analyzer.PosAnalyzer;
import static analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
//,Comparator
public class CheckPosTag {

    private String posTag = null;
    private String fullPosTag = null;
    private String word = null;
    private PosAnalyzer analyzer = null;
    private Boolean found = false;
    private String prediction = null;
    @JsonIgnore

    private Set<String> adjective = new TreeSet<String>();
    private Set<String> noun = new TreeSet<String>();
    private static Set<String> verb = new TreeSet<String>();

    public CheckPosTag(PosAnalyzer analyzer, String word) throws Exception {
        this.analyzer = analyzer;
        this.findPosTagFromTagger(word);

    }

    private void findPosTagFromTagger(String word) throws Exception {
        
         analyzer = new PosAnalyzer(word, POS_TAGGER_WORDS, 5);
        if (!analyzer.getNouns().isEmpty()) {
            this.posTag = PosAnalyzer.NOUN;
              this.found = true;
        } else if (!analyzer.getAdjectives().isEmpty()) {
            this.posTag = PosAnalyzer.ADJECTIVE;
              this.found = true;
        } else if (!analyzer.getVerbs().isEmpty()) {
            this.posTag = PosAnalyzer.VERB;
              this.found = true;
        } else {
            this.posTag = PosAnalyzer.NOUN;
              this.found = true;
        }
                
        if(analyzer.posTaggerText(word)){
        this.fullPosTag=analyzer.getFullPosTag();
        /*System.out.println("word::" + word);
        System.out.println("adjective::" + analyzer.getAdjectives());
        System.out.println("noun::" + analyzer.getNouns());
        System.out.println("verb::" + analyzer.getVerbs());
        System.out.println("fullPosTag::" + fullPosTag);*/
        }
        this.word = word.trim().strip();
         /* analyzer.posTaggerText(word);

        if (!analyzer.getNouns().isEmpty()) {
            this.posTag = PosAnalyzer.NOUN;
            this.found = true;
        } else if (!analyzer.getAdjectives().isEmpty()) {
            this.posTag = PosAnalyzer.ADJECTIVE;
            this.found = true;

        } else if (!analyzer.getVerbs().isEmpty()) {
            this.posTag = PosAnalyzer.VERB;
            this.found = true;

        } else {
            this.posTag = PosAnalyzer.NOUN;
            this.found = true;

        }

        if (analyzer.posTaggerText(word)) {
            this.fullPosTag = analyzer.getFullPosTag();
        System.out.println("word::" + word);
        System.out.println("adjective::" + analyzer.getAdjectives());
        System.out.println("noun::" + analyzer.getNouns());
        System.out.println("verb::" + analyzer.getVerbs());
        System.out.println("fullPosTag::" + fullPosTag);
        }
        this.word = word.trim().strip();*/
    }

    public CheckPosTag(String outputDir, String prediction, String interestingness, String lexicalElement) throws IOException {
        List<File> listOfFiles = FileUtils.getSpecificFiles(outputDir, prediction, interestingness, "z_", ".txt");

        for (File file : listOfFiles) {
            if (file.getName().contains("JJ")) {
                adjective = getList(file);
            }
            if (file.getName().contains("NN")) {
                noun = getList(file);
            }
            if (file.getName().contains("VB")) {
                verb = getList(file);
            }
        }

        findPosTag(lexicalElement);

    }

    private void findPosTag(String lexicalElement) {
        lexicalElement = lexicalElement.replace("\"", "");
        lexicalElement = lexicalElement.toLowerCase().strip().replace(" ", "_");
        if (adjective.contains(lexicalElement)) {
            posTag = "JJ";
            found = true;
        } else if (noun.contains(lexicalElement)) {
            posTag = "NN";
            found = true;
        } else if (verb.contains(lexicalElement)) {
            posTag = "VB";
            found = true;
        }

    }

    private Set<String> getList(File fileName) throws FileNotFoundException, IOException {
        Set<String> entities = new TreeSet<String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();

            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String url = line.toLowerCase().strip().replace(" ", "_").trim();
                    entities.add(url);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public Boolean findRulePattern(Map<String, String> pattern_rules, String string) {
        this.found = false;
        string=string.replace(" ", "_").strip().trim();
        if (pattern_rules.containsKey(string)) {
            this.found = true;
            this.prediction = pattern_rules.get(string);
        }
        return found;
    }

    private String correct(String string) {
        return string.trim().strip();
    }

    public String getPosTag() {
        return posTag;
    }

    public String getFullPosTag() {
        return fullPosTag;
    }

    public String getWord() {
        return word;
    }

    public PosAnalyzer getAnalyzer() {
        return analyzer;
    }

    public Boolean getFound() {
        return found;
    }

    public String getPrediction() {
        return prediction;
    }

}
