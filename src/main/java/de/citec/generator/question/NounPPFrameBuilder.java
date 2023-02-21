/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.sc.generator.analyzer.TextAnalyzer;
import de.citec.sc.generator.utils.PairValues;
import java.util.List;

/**
 *
 * @author elahi
 */
public class NounPPFrameBuilder implements FrameConstants, TextAnalyzer {

    private Integer length = 15;
    private String[] row = new String[length];
    private Integer senseIndex = 1;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenForm(singular)", "writtenForm(plural)", "preposition", "SyntacticFrame", "copulativeArg", "prepositionalAdjunct", "sense", "reference", "domain", "range", "value", "filename", "gram"};
    private static String dir="nouns/";
    
    public NounPPFrameBuilder(String fileName, String linguisticPattern, String value, String frame, String nGram, Integer index, LexicalEntryHelper lexicalEntryHelper) {
        String preposition = null;
        String reference = lexicalEntryHelper.makeReference(fileName);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        if (nGram.contains("2-gram")) {
            PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION);
            if (pairValues.getFlag()) {

                preposition = pairValues.getKey();
            } else {
                return;
            }

        }
        List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange(reference);

        if (!domainAndRanges.isEmpty()) {
            PairValues pairValues = domainAndRanges.get(0);
            String domain = pairValues.getKey();
            String range = pairValues.getValue();
            System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
            buildRow(id, linguisticPattern, reference, domain, range, preposition, value, frame, nGram);
        }
    }

    private void buildRow(String id, String linguisticPattern, String reference, String domainR, String rangeR, String preposition, String value, String frame, String nGram) {
        row[0] = id;
        row[1] = noun;
        row[2] = linguisticPattern;
        row[3] = linguisticPattern;
        row[4] = preposition;
        row[5] = frame;
        row[6] = domain;
        row[7] = range;
        row[8] = senseIndex.toString();
        //rowNoun[9] = fileName.replace("raw-", "").replace(".csv", "").replace("_", ":");
        row[9] = reference;
        row[10] = domainR;
        row[11] = rangeR;
        row[12] = value;
        row[13] = reference;
        row[14] = nGram;
    }

    public String[] getRow() {
        return row;
    }

    public static String[] getHeader() {
        return header;
    }

    public static String getDir() {
        return dir;
    }

}
