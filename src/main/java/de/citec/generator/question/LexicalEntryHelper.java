/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.ENGLISH_SELECTED_STOPWORDS;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.PairValues;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class LexicalEntryHelper implements FrameConstants {

    private Map<String, List<PairValues>> referenceDomainRange = new TreeMap<String, List<PairValues>>();

    public LexicalEntryHelper() {

    }

    public LexicalEntryHelper(String fileName) {
        referenceDomainRange = FileFolderUtils.filetoTabDelimiatedResult(fileName);

    }

    public String makeLinguistc(String linguisticPattern, Integer index) {
        linguisticPattern = linguisticPattern.replace(" ", "_");
        linguisticPattern = linguisticPattern + "-" + index.toString();
        return linguisticPattern;
    }
    
    

    public String makeReference(String reference) {
        reference = reference.replace("raw-", "");
        reference = reference.replace(".csv", "");
        reference=this.formatPropertyColonToSlash(reference);
        return reference;
    }

    public PairValues findPreposition(String referenceForm, List<String> filterList) {
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

    public String deleteStopWord(String referenceForm, List<String> filterList) {
        StringTokenizer st = new StringTokenizer(referenceForm);
        String str = "";
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
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
        if(reference.contains("dbo:")||reference.contains("dbp:"))
           return reference;
        return null;
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
            PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, TextAnalyzer.PREPOSTION_LIST);
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

}
