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
    private String[] objRow = new String[length];
    private String[] subjRow = new String[length];
    private Integer senseIndex = 1;
    private String idObj=null;
    private String idSubj=null;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenForm(singular)", "writtenForm(plural)", "preposition", "SyntacticFrame", "copulativeArg", "prepositionalAdjunct", "sense", "reference", "domain", "range", "value", "filename", "gram"};
    private static String dir="nouns/";
    private Boolean flag = false;
    
    public NounPPFrameBuilder(String reference, String linguisticPattern, String value, String frame, String nGram, Integer lexIndex,String [] parameterValues,LexicalEntryHelper lexicalEntryHelper) {
        String preposition = null;
        this.idObj = lexicalEntryHelper.makeReference(lexIndex+1);
        this.idSubj=lexicalEntryHelper.makeReference(lexIndex+2);

        /*//if (nGram.contains("2-gram")) {
            PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, lexicalEntryHelper.getPrepositions());
            if (pairValues.getFlag()) {
                preposition = pairValues.getKey();
               
            } else {
                return;
            }

        //}*/
        List<PairValues> domainAndRanges = lexicalEntryHelper.findDomainRange(reference);

        if (!domainAndRanges.isEmpty()) {
            PairValues pairValuesT = domainAndRanges.get(0);
            String domain = pairValuesT.getKey();
            String range = pairValuesT.getValue();
            //System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
             this.flag=true;
            buildRow(linguisticPattern, reference, domain, range, preposition, value, frame, nGram,parameterValues);
        }
    }

    private void buildRow(String linguisticPattern, String reference, String domainR, String rangeR, String preposition, String value, String frame, String nGram,String []parameterValues) {         
        objRow[0] = idObj;
        objRow[1] = noun;
        objRow[2] = linguisticPattern;
        objRow[3] = linguisticPattern;
        objRow[4] = preposition;
        objRow[5] = frame;
        objRow[6] = range;
        objRow[7] = domain;
        objRow[8] = senseIndex.toString();
        objRow[9] = reference;
        objRow[10] = domainR;
        objRow[11] = rangeR;
        objRow[12] = value;
        objRow[13] = reference;
        objRow[14] = nGram;     
        
        subjRow[0] = idSubj;
        subjRow[1] = noun;
        subjRow[2] = linguisticPattern;
        subjRow[3] = linguisticPattern;
        subjRow[4] = preposition;
        subjRow[5] = frame;
        subjRow[6] = domain;
        subjRow[7] = range;
        subjRow[8] = senseIndex.toString();
        subjRow[9] = reference;
        subjRow[10] = domainR;
        subjRow[11] = rangeR;
        subjRow[12] = value;
        subjRow[13] = reference;
        subjRow[14] = nGram;
        /*subjRow[17] = parameterValues[0];
        subjRow[18] = parameterValues[1];
        subjRow[19] = parameterValues[2];
        subjRow[20] = parameterValues[3];
        subjRow[21] = parameterValues[4];*/
    }

    public String[] getObjRow() {
        return objRow;
    }

    public String[] getSubjRow() {
        return subjRow;
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
