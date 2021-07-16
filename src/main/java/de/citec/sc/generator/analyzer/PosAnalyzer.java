/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.analyzer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import edu.stanford.nlp.util.Sets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author elahi
 *
 */
public class PosAnalyzer implements TextAnalyzer {

    
    @JsonIgnore
    private static String stanfordModelFile = resources + "stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger";
    @JsonIgnore
    private static MaxentTagger taggerModel = new MaxentTagger(stanfordModelFile);
    @JsonIgnore
    private Integer numberOfSentences = 0;
    @JsonIgnore
    private Boolean flag = false;
    @JsonIgnore
    
    private String fullPosTag = null;
    
    static {
        taggerModel = new MaxentTagger(stanfordModelFile);
    }
    
    private Set<String> words = new HashSet<String>();
    private Set<String> adjectives = new HashSet<String>();
    private Set<String> nouns = new HashSet<String>();
    private Set<String> verbs = new HashSet<String>();
    private Set<String> pronouns = new HashSet<String>();


    private String inputText = null;

      public PosAnalyzer(String inputText, String analysisType, Integer numberOfSentences) throws Exception {
        this.numberOfSentences = numberOfSentences;
        this.inputText = inputText;
        BufferedReader reader = new BufferedReader(new StringReader(inputText));

        if (analysisType.contains(POS_TAGGER_WORDS)) {
            posTaggerWords(reader);
        }
        
    }
      
      private void posTaggerWords(BufferedReader reader) throws Exception {
        Map<Integer, Map<String, Set<String>>> sentencePosTags = new HashMap<Integer, Map<String, Set<String>>>();
        Map<Integer, Set<String>> sentenceWords = new HashMap<Integer, Set<String>>();

        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
        //List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputText)));
        Integer index = 0;
        for (List<HasWord> sentence : sentences) {
            index++;
            Set<String> wordsofSentence = new HashSet<String>();
            Map<String, Set<String>> posTaggers = new HashMap<String, Set<String>>();
            List<TaggedWord> tSentence = taggerModel.tagSentence(sentence);
            for (TaggedWord taggedWord : tSentence) {
                String word = taggedWord.word();
                word=this.modifyWord(word);
                if(isStopWord(word)){
                    continue;
                }
                if (taggedWord.tag().startsWith(TextAnalyzer.ADJECTIVE) 
                        || taggedWord.tag().startsWith(TextAnalyzer.NOUN)
                        || taggedWord.tag().startsWith(TextAnalyzer.VERB)) {
                    posTaggers = this.populateValues(taggedWord.tag(), word, posTaggers);
                }
                wordsofSentence.add(word);
            }
            sentenceWords.put(index, wordsofSentence);
            sentencePosTags.put(index, posTaggers);
        }
                
        sentenwisePosSeperated(sentenceWords, sentencePosTags);
    }
 
    private boolean isStopWord(String word) {
        word = word.trim().toLowerCase();
        if (ENGLISH_STOPWORDS.contains(word)) {
            return true;
        }
        return false;
    }

    
     public PosAnalyzer(String analysisType, Integer numberOfSentences) throws Exception {
        this.numberOfSentences = numberOfSentences;
    }
     
      public PosAnalyzer()  {
        
    }
    
    
    public Boolean posTaggerText(String inputText) throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(inputText));
        List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
        for (List<HasWord> sentence : sentences) {
            List<TaggedWord> tSentence = taggerModel.tagSentence(sentence);
            //System.out.println(tSentence);
            String taggedText = getSentenceFromWordListTagged(tSentence);
            String taggs = this.setTaggs(tSentence);
            this.fullPosTag = taggs;
            return true;
        }
        return false;
    }

    private String getSentenceFromWordListTagged(List<TaggedWord> tSentence) {
        String str="";
        for(TaggedWord taggedWord:tSentence){
            String line=taggedWord+" ";
            str+=line;
        }
        str=StringUtils.substring(str, 0, str.length()-1);
        return str;
    }
    private String getSentenceFromWordListTaggedOriginal(List<HasWord> tSentence) {
        String str="";
        for(HasWord taggedWord:tSentence){
            String line=taggedWord+" ";
            str+=line;
        }
        str=StringUtils.substring(str, 0, str.length()-1);
        return str;
    }


    private void sentenwisePosSeperated(Map<Integer, Set<String>> sentenceWords, Map<Integer, Map<String, Set<String>>> sentencePosTags) {
        for (Integer number : sentenceWords.keySet()) {
            Map<String, Set<String>>temp=sentencePosTags.get(number);            
            if (temp != null) {
                Set<String> set = sentenceWords.get(number);
                words.addAll(set);
            }
            for (String posTag : temp.keySet()) {
                 Set<String> set =temp.get(posTag);
                if (posTag.contains(TextAnalyzer.NOUN)) {
                    nouns.addAll(set);
                }
                else if (posTag.contains(TextAnalyzer.ADJECTIVE)) {
                    adjectives.addAll(set);
                }
                else if (posTag.contains(TextAnalyzer.VERB)) {
                    verbs.addAll(set);
                }
                else if (posTag.contains(TextAnalyzer.PRONOUN)) {
                    pronouns.addAll(set);
                }
            }

            number++;
            if (number == numberOfSentences) {
                break;
            }
            //System.out.println(sentenceInfo);
        }

    }

    private Map<String, Set<String>> populateValues(String key, String value, Map<String, Set<String>> posTaggers) {
        Set<String> words = new HashSet<String>();
        if (posTaggers.containsKey(key)) {
            words = posTaggers.get(key);
        }
        words.add(value);
        posTaggers.put(key, words);

        return posTaggers;
    }

    public String getText() {
        return inputText;
    }

    public String getFullPosTag() {
        return fullPosTag;
    }

    public Set<String> getWords() {
        return words;
    }

    public Set<String> getAdjectives() {
        return adjectives;
    }

    public Set<String> getNouns() {
        return nouns;
    }

    public Set<String> getVerbs() {
        return verbs;
    }

    public Set<String> getPronouns() {
        return pronouns;
    }

    public Boolean getPosTagFound() {
        return flag;
    }

   

    private void populate(Set<String> wordsOfSentence,String key, Map<String, Set<String>> hash) {
        if (hash.containsKey(key)) {
            Set<String> existingWords = hash.get(key);
            hash.put(key, Sets.union(existingWords, wordsOfSentence));
        } else {
            hash.put(key, wordsOfSentence);
        }
    }
   
    private String setTaggs(List<TaggedWord> tSentence) {
        String str = "";
        for (TaggedWord taggedWord : tSentence) {
            String line = taggedWord.tag() + "_";
            str += line;
        }
        return  StringUtils.substring(str, 0, str.length()-1);
    }

    private String modifyWord(String word) {
        return word.toLowerCase().trim().strip();
    }

   
}
