/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.FrameConstants.domain;
import static de.citec.generator.question.FrameConstants.range;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.noun;
import de.citec.sc.generator.utils.PairValues;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class TransitiveFrameBuiler implements FrameConstants, TextAnalyzer {

    private Integer length = 17;
    private String[] row = new String[length];
    private Integer senseIndex = 1;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenFormInfinitive/2ndPerson", "writtenForm3rdPerson", "writtenFormPast", "writtenFormPerfect", "SyntacticFrame", "subject", "directObject", "sense", "reference", "domain", "range", "passivePreposition", "value", "filename", "gram"};
    private static String dir="verbs/";

    public TransitiveFrameBuiler(String fileName, String linguisticPattern, String value, String frame, String nGram, Integer index, LexicalEntryHelper lexicalEntryHelper) {
        String preposition = "by";
        String reference = lexicalEntryHelper.makeReference(fileName);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;

        List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange(reference);

        if (!domainAndRanges.isEmpty()) {
            PairValues pairValues = domainAndRanges.get(0);
            String domain = pairValues.getKey();
            String range = pairValues.getValue();
            System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
            this.buildRow(id, linguisticPattern, reference, domain, range, preposition, value, frame, nGram);
        }
    }

    private void buildRow(String id, String linguisticPattern, String reference, String domainR, String rangeR, String preposition, String value, String frame, String nGram) {
        row[0] = id;
        row[1] = verb;
        row[2] = linguisticPattern;
        row[3] = linguisticPattern;
        row[4] = linguisticPattern;
        row[5] = linguisticPattern;
        row[6] = frame;
        row[7] = domain;
        row[8] = range;
        row[9] = senseIndex.toString();
        row[10] = reference;
        row[11] = domainR;
        row[12] = rangeR;
        row[13] = preposition;
        row[14] = value;
        row[15] = reference;
        row[16] = nGram;
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
