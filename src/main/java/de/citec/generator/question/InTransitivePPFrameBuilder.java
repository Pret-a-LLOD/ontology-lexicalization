/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.FrameConstants.domain;
import static de.citec.generator.question.FrameConstants.range;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.verb;
import de.citec.sc.generator.utils.PairValues;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elahi
 */
public class InTransitivePPFrameBuilder {

    private Integer length = 17;
    private String[] row = new String[length];
    private Integer senseIndex = 1;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenFormInfinitive/2ndPerson", "writtenForm3rdPerson", "writtenFormPast", "writtenFormPerfect", "SyntacticFrame", "subject", "directObject", "sense", "reference", "domain", "range", "passivePreposition", "value", "filename", "gram"};
    private static String dir="verbs/";
    private Boolean flag=false;

    public InTransitivePPFrameBuilder(String reference, String linguisticPattern, String value, String frame, String nGram, Integer index, LexicalEntryHelper lexicalEntryHelper) {
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        String preposition=null;

        List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange(reference);
        
        if (nGram.contains("2-gram")) {
            PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION);
            if (pairValues.getFlag()) {

                preposition = pairValues.getKey();
            } else {
                return;
            }

        }

        if (!domainAndRanges.isEmpty()) {
            PairValues pairValuesT = domainAndRanges.get(0);
            String domain = pairValuesT.getKey();
            String range = pairValuesT.getValue();
            System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
            this.flag=true;
            buildRow(id, linguisticPattern, reference, domain, range, preposition, value, frame, nGram);
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
        row[13] = value;
        row[14] = reference;
        row[15] = nGram;
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

    public Boolean getFlag() {
        return flag;
    }

   

}
