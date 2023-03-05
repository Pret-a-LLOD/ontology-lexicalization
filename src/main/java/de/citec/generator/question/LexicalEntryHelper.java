/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.InduceConstants.GradableFrame;
import static de.citec.generator.question.InduceConstants.InTransitivePPFrame;
import static de.citec.generator.question.InduceConstants.NounPPFrame;
import static de.citec.generator.question.InduceConstants.TransitiveFrame;
import de.citec.sc.generator.analyzer.PosAnalyzer;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.ENGLISH_SELECTED_STOPWORDS;
import static de.citec.sc.generator.analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.PairValues;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class LexicalEntryHelper implements FrameConstants {

    private Map<String, List<PairValues>> referenceDomainRange = new TreeMap<String, List<PairValues>>();
    private Set<String> stopWords = new TreeSet<String>();
    private Set<String> prepositions = new TreeSet<String>();
    private Set<String> transitiveVerbs = new TreeSet<String>();
    private Set<String> inTransitiveVerbs = new TreeSet<String>();
    private String stopWordFile = "src/main/resources/qald-lex/stopword.txt";
    private String prepositionFile = "src/main/resources/qald-lex/preposition.txt";
    private String transitiveVerbsFile = "src/main/resources/qald-lex/TransitiveVerbs.txt";
    private String inTransitiveVerbsFile = "src/main/resources/qald-lex/InTransitiveVerbs.txt";

    public LexicalEntryHelper() {

    }

    public LexicalEntryHelper(String fileName) {
        referenceDomainRange = FileFolderUtils.filetoTabDelimiatedResult(fileName);
        getEnglishStopWords(stopWordFile, prepositionFile);
        this.transitiveVerbs = getTransitiveVerbs(transitiveVerbsFile);
        this.inTransitiveVerbs = getTransitiveVerbs(inTransitiveVerbsFile);
        //System.out.println(this.transitiveVerbs.toString());
        //System.out.println(this.inTransitiveVerbs.toString());

    }

    public String makeLinguistc(String linguisticPattern, Integer index) {
        linguisticPattern = linguisticPattern.replace(" ", "_");
        linguisticPattern = linguisticPattern + "-" + index.toString();
        return linguisticPattern;
    }

    public String makeReference(Integer index) {

        return "lexicalEntry" + "-" + index.toString();
    }

    public PairValues findPreposition(String referenceForm, Set<String> filterList) {
        StringTokenizer st = new StringTokenizer(referenceForm);
        String str = "";

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (filterList.contains(token)) {
                token = this.filerString(token);
                return new PairValues(Boolean.TRUE, token, referenceForm);
            }
        }
        return new PairValues(Boolean.FALSE, null, referenceForm);
    }

    public List<PairValues> findDomainRange(String reference) {
        reference = this.formatPropertyToColon(reference);
        if (referenceDomainRange.containsKey(reference)) {
            return referenceDomainRange.get(reference);
        }
        return new ArrayList<PairValues>();
    }
    
     public boolean isPropertyQald(String reference) {
        reference = this.formatPropertyToColon(reference);
        if (referenceDomainRange.containsKey(reference)) {
            return true;
        }
        return false;
    }

    public String deleteStopWord(String referenceForm, Set<String> filterList) {
        StringTokenizer st = new StringTokenizer(referenceForm);
        String str = "";
        while (st.hasMoreTokens()) {
            String token = st.nextToken().replace("\"", "").strip().stripLeading().stripTrailing().trim();
            //if(token.contains("was"))
            //    System.out.println(token);
            if (filterList.contains(token)) {
                continue;
            } else {
                str += token + " ";
            }
        }
        return str.trim().strip().stripTrailing().stripLeading();
    }

    private String filerString(String token) {
        return token.toLowerCase().trim().strip().stripLeading().stripTrailing();
    }

    public Map<String, List<PairValues>> getReferenceDomainRange() {
        return referenceDomainRange;
    }

    private String formatPropertyToColon(String reference) {
        reference = reference.replace("dbo_", "dbo:");
        reference = reference.replace("dbp_", "dbp:");
        return reference;
    }

    private String formatPropertyColonToSlash(String reference) {
        reference = reference.replace("dbo_", "dbo:");
        reference = reference.replace("dbp_", "dbp:");
        return reference;
    }

    public String formatPropertyLongToShort(String reference) {
        reference = reference.replace("http://dbpedia.org/ontology/", "dbo:");
        reference = reference.replace("http://dbpedia.org/property/", "dbp:");
        if (reference.contains("dbo:") || reference.contains("dbp:")) {
            return reference;
        }
        return null;
    }

    private void getEnglishStopWords(String stopWordFile, String prepositionFile) {
        this.stopWords = FileUtils.fileToSetString(stopWordFile);
        this.prepositions = FileUtils.fileToSetString(prepositionFile);
    }

    public Set<String> getStopWords() {
        return stopWords;
    }

    public Set<String> getPrepositions() {
        return prepositions;
    }

    public String getStopWordFile() {
        return stopWordFile;
    }

    public String getPrepositionFile() {
        return prepositionFile;
    }

    public static void main(String[] args) {
        String domainRangeFileName = "src/main/resources/qald-lex/DomainAndRange.txt";
        LexicalEntryHelper lexicalEntryHelper = new LexicalEntryHelper(domainRangeFileName);
        //Map<String, List<PairValues>> referenceDomainRange = new TreeMap<String, List<PairValues>>();
        //referenceDomainRange = FileFolderUtils.fileToMap(domainRangeFileName);
        System.out.println(lexicalEntryHelper.referenceDomainRange);
        String nGram = "2-gram";
        String linguisticPattern = "book by";
        String preposition = null;
        if (nGram.contains("2-gram")) {
            PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, lexicalEntryHelper.getPrepositions());
            if (pairValues.getFlag()) {

                preposition = pairValues.getKey();
            }
            System.out.println(" preposition::" + preposition);

            System.out.println(" lexicalEntryHelper.referenceDomainRange::" + lexicalEntryHelper.referenceDomainRange);
            List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange("dbo_areaCode");
            System.out.println("domainAndRanges::" + domainAndRanges);
            if (!domainAndRanges.isEmpty()) {
                pairValues = domainAndRanges.get(0);
                String domain = pairValues.getKey();
                String range = pairValues.getValue();
                System.out.println(domain + " " + range);
            }

        }

    }

    private Set<String> getTransitiveVerbs(String transitiveVerbsFile) {
        String str = FileUtils.fileToString(transitiveVerbsFile);
        Set<String> verbs = new TreeSet<String>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().replace("\"", "").strip().stripLeading().stripTrailing().trim().toLowerCase();
            verbs.add(token);
        }
        return verbs;
    }

    String[] findPosTag(String inputText) {
        try {
            PosAnalyzer posAnalyzer = new PosAnalyzer(inputText, POS_TAGGER_WORDS, 10);
            String noun = posAnalyzer.isNoun(inputText);
            String verb = posAnalyzer.isVerb(inputText);
            String adj = posAnalyzer.isAdjective(inputText);
            if (noun != null) {
                return new String[]{TextAnalyzer.NN, this.findNounType(noun)};
            } else if (verb != null) {
                return new String[]{TextAnalyzer.VB, this.findVerbType(verb)};
            } else if (adj != null) {
                return new String[]{TextAnalyzer.JJ, this.findAdjType(adj)};
            } else {
                return new String[]{"unknown", "unknown"};
            }
            /*if(posAnalyzer.posTaggerText(inputText)){
               return posAnalyzer.getFullPosTag();
            }*/

        } catch (Exception ex) {
            return new String[]{"unknown", "unknown"};
        }
    }

    private String findVerbType(String reference) {

        /*if (isTransitiveVerb(reference)) {
            return TransitiveFrame;
        } else*/ if (isInTransitiveVerb(reference)) {
            return InTransitivePPFrame;
        }
        else{
            System.out.println(reference);
        }
        return TransitiveFrame;
    }
    
    
    private boolean isTransitiveVerb(String reference) {
        reference = reference.toLowerCase().trim().strip().stripLeading().stripTrailing();

        for (String verb : this.transitiveVerbs) {
            verb = verb.toLowerCase().trim().strip().stripLeading().stripTrailing();
            if (reference.contains(verb)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInTransitiveVerb(String reference) {
        reference = reference.toLowerCase().trim().strip().stripLeading().stripTrailing();
        for (String verb : this.inTransitiveVerbs) {
            verb = verb.toLowerCase().trim().strip().stripLeading().stripTrailing();
            System.out.println(reference+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+verb);
            if (reference.contains(verb)) {
                return true;
            }
        }
        return false;
    }


    private String findAdjType(String adj) {
        return GradableFrame;
    }

    private String findNounType(String noun) {
        return NounPPFrame;
    }

}
