/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.FrameConstants.domain;
import static de.citec.generator.question.FrameConstants.range;
import static de.citec.generator.question.InduceConstants.IntransitivePPFrame;
import static de.citec.sc.generator.analyzer.TextAnalyzer.verb;
import de.citec.sc.generator.utils.PairValues;
import java.util.List;

/**
 *
 * @author elahi
 */
public class InTransitivePPFrameBuilder {

    private Integer length = 17;
    private String[] objRow = new String[length];
    private String[] subjRow = new String[length];
    private Integer senseIndex = 1;
    private static String[] header = new String[]{"lemon", "partOfSpeech", "writtenFormInfinitive/2ndPerson", "writtenForm3rdPerson", "writtenFormPast", "writtenFormPerfect", "SyntacticFrame", "subject", "directObject", "sense", "reference", "domain", "range", "passivePreposition", "value", "filename", "gram"};
    private static String dir = "verbs/";
    private Boolean flag = false;
    private VerbForm verbForms = null;


    public InTransitivePPFrameBuilder(String reference, String linguisticPattern, String value, String frame, String nGram, Integer index, String [] parameterValues,LexicalEntryHelper lexicalEntryHelper) {
        //String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index);
        String id = lexicalEntryHelper.makeReference(index);
        String preposition = null;
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

        //if (nGram.contains("2-gram")) {
        PairValues pairValues = lexicalEntryHelper.findPreposition(linguisticPattern, lexicalEntryHelper.getPrepositions());
        if (pairValues.getFlag()) {

            preposition = pairValues.getKey();
        } else {
            preposition = "in";
        }

        //}*/
        if (!domainAndRanges.isEmpty()) {
            PairValues pairValuesT = domainAndRanges.get(0);
            String domain = pairValuesT.getKey();
            String range = pairValuesT.getValue();
            //System.out.println(linguisticPattern + " " + reference + " " + domain + " " + range + " " + preposition);
            this.flag = true;
            buildRow(id, linguisticPattern, reference, domain, range, preposition, value, frame, nGram,parameterValues);
        }
    }

    //born_in_1	verb	born	-	born	born	in	IntransitivePPFrame	range	domain	1	dbo:birthYear	dbo:Person	xsd:date
    private void buildRow(String id, String linguisticPattern, String reference, String domainR, String rangeR, String preposition, String value, String frame, String nGram,String []parameterValues) {
        objRow[0] = id;
        objRow[1] = verb;
        objRow[2] = verbForms.getForm2ndPerson();
        objRow[3] = verbForms.getForm3rdPerson();
        objRow[4] = verbForms.getFormPast();
        objRow[5] = verbForms.getFormPerfect();
        objRow[6] = preposition;
        objRow[7] = IntransitivePPFrame;
        objRow[8] = domain;
        objRow[9] = range;
        objRow[10] = senseIndex.toString();
        objRow[11] = reference;
        objRow[12] = domainR;
        objRow[13] = rangeR;
        objRow[14] = value;
        objRow[15] = reference;
        objRow[16] = nGram;
        /*objRow[17] = parameterValues[0];
        objRow[18] = parameterValues[1];
        objRow[19] = parameterValues[2];
        objRow[20] = parameterValues[3];
        objRow[21] = parameterValues[4];*/


        subjRow[0] = id;
        subjRow[1] = verb;
        subjRow[2] = verbForms.getForm2ndPerson();
        subjRow[3] = verbForms.getForm3rdPerson();
        subjRow[4] = verbForms.getFormPast();
        subjRow[5] = verbForms.getFormPerfect();
        subjRow[6] = preposition;
        subjRow[7] = IntransitivePPFrame;
        subjRow[8] = range;
        subjRow[9] = domain;
        subjRow[10] = senseIndex.toString();
        subjRow[11] = reference;
        subjRow[12] = domainR;
        subjRow[13] = rangeR;
        subjRow[14] = value;
        subjRow[15] = reference;
        subjRow[16] = nGram;
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

    public Boolean getFlag() {
        return flag;
    }

}
