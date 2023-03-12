/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.FrameConstants.domain;
import static de.citec.generator.question.FrameConstants.range;
import static de.citec.generator.question.InduceConstants.TransitiveFrame;
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
    private String[] objektRow = new String[length];
    private String[] subjtRow = new String[length];
    private Integer senseIndex = 1;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenFormInfinitive/2ndPerson", "writtenForm3rdPerson", "writtenFormPast", "writtenFormPerfect", "preposition", "SyntacticFrame", "subject", "prepositionalAdjuct", "sense", "reference", "domain", "range", "value", "filename", "gram"};
    private static String dir = "verbs/";
    private Boolean flag = false;
    private LexicalEntryHelper lexicalEntryHelper = null;
    private VerbForm verbForms = null;

    public TransitiveFrameBuiler(String reference, String linguisticPattern, String value, String frame, String nGram, Integer index, String []parameterValues,LexicalEntryHelper lexicalEntryHelper) {
        this.lexicalEntryHelper = lexicalEntryHelper;
        String preposition = "by";
        String id = lexicalEntryHelper.makeReference(index);
        List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange(reference);
        linguisticPattern = linguisticPattern.strip().stripLeading().stripTrailing().trim();
        if (lexicalEntryHelper.getVerbForms().getForm().containsKey(linguisticPattern)) {
            this.verbForms = lexicalEntryHelper.getVerbForms().getForm().get(linguisticPattern);
        } else {
            this.verbForms = new VerbForm(linguisticPattern, linguisticPattern, linguisticPattern, linguisticPattern);
        }
        
        /*nGram=nGram.replace("\"", "");
        if (!nGram.contains("1-gram")) {
            this.flag = false;
            return;
        }*/

        if (!domainAndRanges.isEmpty()) {
            PairValues pairValues = domainAndRanges.get(0);
            String domain = pairValues.getKey();
            String range = pairValues.getValue();
            //System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
            this.flag = true;
            this.buildRow(id, linguisticPattern, reference, domain, range, preposition, value, frame, nGram,parameterValues);
        }
    }

    //Develop_3	verb	develop	develops	developed	developed	TransitiveFrame	domain	range	1	dbo:product	dbo:Company	dbo:Software	by
    private void buildRow(String id, String linguisticPattern, String reference, String domainR, String rangeR, String preposition, String value, String frame, String nGram,String [] parameterValues) {
        //System.out.println(verbForms);
        objektRow[0] = id;
        objektRow[1] = verb;
        objektRow[2] = verbForms.getForm2ndPerson();
        objektRow[3] = verbForms.getForm3rdPerson();
        objektRow[4] = verbForms.getFormPast();
        objektRow[5] = verbForms.getFormPerfect();
        objektRow[6] = TransitiveFrame;
        objektRow[7] = range;
        objektRow[8] = domain;
        objektRow[9] = senseIndex.toString();
        objektRow[10] = reference;
        objektRow[11] = domainR;
        objektRow[12] = rangeR;
        objektRow[13] = preposition;
        objektRow[14] = value;
        objektRow[15] = reference;
        objektRow[16] = nGram;
        /*objektRow[17] = parameterValues[0];
        objektRow[18] = parameterValues[1];
        objektRow[19] = parameterValues[2];
        objektRow[20] = parameterValues[3];
        objektRow[21] = parameterValues[4];*/

        subjtRow[0] = id;
        subjtRow[1] = verb;
        subjtRow[2] = verbForms.getForm2ndPerson();
        subjtRow[3] = verbForms.getForm3rdPerson();
        subjtRow[4] = verbForms.getFormPast();
        subjtRow[5] = verbForms.getFormPerfect();
        subjtRow[6] = TransitiveFrame;
        subjtRow[7] = domain;
        subjtRow[8] = range;
        subjtRow[9] = senseIndex.toString();
        subjtRow[10] = reference;
        subjtRow[11] = domainR;
        subjtRow[12] = rangeR;
        subjtRow[13] = preposition;
        subjtRow[14] = value;
        subjtRow[15] = reference;
        subjtRow[16] = nGram;
        /*subjtRow[17] = parameterValues[0];
        subjtRow[18] = parameterValues[1];
        subjtRow[19] = parameterValues[2];
        subjtRow[20] = parameterValues[3];
        subjtRow[21] = parameterValues[4];*/

    }

    public String[] getObjektRow() {
        return objektRow;
    }

    public String[] getSubjtRow() {
        return subjtRow;
    }

    public Boolean getFlag() {
        return flag;
    }

    public static String[] getHeader() {
        return header;
    }

}
