/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.core;

import de.citec.generator.config.LemonConstants;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.lemon.core.Language.EN;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.core.Provenance;
import de.citec.sc.lemon.core.Reference;
import de.citec.sc.lemon.core.Restriction;
import de.citec.sc.lemon.core.Sense;
import de.citec.sc.lemon.core.SenseArgument;
import de.citec.sc.lemon.core.SimpleReference;
import de.citec.sc.lemon.core.SyntacticArgument;
import de.citec.sc.lemon.core.SyntacticBehaviour;
import java.io.*;
import java.util.*;
import de.citec.generator.config.PredictionPatterns;
import edu.stanford.nlp.util.Pair;
import java.util.regex.Pattern;

/**
 *
 * @author elahi
 */
public class LemonCreator implements PredictionPatterns, LemonConstants, TextAnalyzer {

    private String lexiconDirectory = null;
    private Lexicon turtleLexicon = null;
    private Integer rankLimit = 0;
    private Map<String, List<LexiconUnit>> lexiconPosTaggged = new TreeMap<String, List<LexiconUnit>>();

    public LemonCreator(String outputDir, Lexicon turtleLexicon, Integer rankLimit) throws IOException {
        this.lexiconDirectory = outputDir;
        this.turtleLexicon = turtleLexicon;
        this.rankLimit = rankLimit;
    }

    public void preparePropertyLexicon(String prediction, String directory, String key, String interestingness, Map<String, List<LineInfo>> lineLexicon) throws IOException, Exception {
        Map<String, List<LexiconUnit>> posTaggedLex = new TreeMap<String, List<LexiconUnit>>();
        Integer count = 0, countJJ = 0, countVB = 0;
        for (String word : lineLexicon.keySet()) {
            String postagOfWord = null;
            LinkedHashMap<Integer, List<LineInfo>> kbList = new LinkedHashMap<Integer, List<LineInfo>>();
            Integer index = 0;
            List<LineInfo> LineInfos = lineLexicon.get(word);
            //Collections.sort(LineInfos,new LineInfo());  

            Set<String> duplicateCheck = new HashSet<String>();
            count = count + 1;
            for (LineInfo lineInfo : LineInfos) {
                postagOfWord = lineInfo.getPartOfSpeech();
                String value = null;

                String object = lineInfo.getObject();
                List<LineInfo> pairs = new ArrayList<LineInfo>();
                if (duplicateCheck.contains(object)) {
                    continue;
                }
                if (lineInfo.getProbabilityValue().isEmpty()) {
                    continue;
                } else {
                    value = lineInfo.getProbabilityValue(interestingness).toString();
                }
                pairs.add(lineInfo);
                kbList.put(index, pairs);
                index = index + 1;
                duplicateCheck.add(object);
            }
            LexiconUnit LexiconUnit = new LexiconUnit(count, word, postagOfWord, kbList);
            posTaggedLex = this.setPartsOfSpeech(postagOfWord, LexiconUnit, posTaggedLex);
        }
        this.writeFileLemon(prediction, posTaggedLex);

    }

    private void writeFileLemon(String prediction, Map<String, List<LexiconUnit>> posTaggedLex) {
        String posLexInfo = null, givenPosTag = null;
        if (prediction.equals(predict_po_for_s_given_localized_l)
                || prediction.equals(predict_po_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_adjective;
            givenPosTag = ADJECTIVE;
        } else if (prediction.equals(PredictionPatterns.predict_p_for_o_given_localized_l)
                || prediction.equals(PredictionPatterns.predict_p_for_o_given_l)
                || prediction.equals(PredictionPatterns.predict_p_for_s_given_localized_l)
                || prediction.equals(PredictionPatterns.predict_p_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_verb;
            givenPosTag = TextAnalyzer.VERB;
        } else if (prediction.equals(PredictionPatterns.predict_o_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_noun;
            givenPosTag = TextAnalyzer.NOUN;
        } else {
            return;
        }

        for (String postag : posTaggedLex.keySet()) {
            if (!postag.contains(givenPosTag)) {
                continue;
            }
            List<LexiconUnit> lexiconUnts = posTaggedLex.get(postag);
            for (LexiconUnit lexiconUnit : lexiconUnts) {
                LinkedHashMap<Integer, List<LineInfo>> ranks = lexiconUnit.getLineInfos();
                String writtenForm = lexiconUnit.getWord();
                //System.out.println("prediction::" + prediction);
                //System.out.println("writtenForm::" + writtenForm);
                /*if(!writtenForm.equals("born"))
                    continue;*/

                if (!isValidWrittenForm(writtenForm)) {
                    continue;
                }
                else
                    writtenForm=this.modify(writtenForm);
                    

                de.citec.sc.lemon.core.LexicalEntry entry = new de.citec.sc.lemon.core.LexicalEntry(EN);
                entry.setCanonicalForm(writtenForm);
                entry.setPOS(posLexInfo);
                entry.setURI(this.turtleLexicon.getBaseURI() + writtenForm);
                entry.setPOS(posLexInfo);
                Set<Sense> senses = new HashSet<Sense>();

                Integer index = 0;
                for (Integer rank : ranks.keySet()) {
                    List<LineInfo> rankLineInfo = ranks.get(rank);
                    index = index + 1;
                    if (index > this.rankLimit) {
                        break;
                    }
                    for (LineInfo lineInfo : rankLineInfo) {
                        //System.out.println("Pos tag::" + lineInfo.getPosTag());
                        //System.out.println("predicate::" + prediction);
                        //System.out.println("object::" + lineInfo.getObjectOriginal());
                        //entry=addSense(entry,writtenForm,lineInfo);
                        //System.out.println("entry::" + entry);
                        Sense sense = null;
                        SyntacticBehaviour behaviour = null;
                        Provenance provenance = null;

                        try {

                            Pair<Boolean, Sense> senseCheck = this.addSenseToEntry(this.turtleLexicon.getBaseURI(), writtenForm, lineInfo, postag);
                            //System.out.println("sense::::"+sense);
                            //System.out.println("index::" + index);
                            if (senseCheck.first()) {
                                sense = senseCheck.second();
                                senses.add(sense);
                                //System.out.println("sense::::" + sense);
                            } else {
                                continue;
                            }

                            behaviour = this.addBehaviourToEntry(sense, writtenForm, postag, lineInfo.getPreposition());
                            provenance = this.addProvinceToEntry();
                            if (sense != null && behaviour != null && provenance != null) {
                                entry.addSyntacticBehaviour(behaviour, sense);
                                //entry.addProvenance(provenance, sense);
                            }

                        } catch (NullPointerException ex) {
                            System.err.println("either sense or behavior or sense is not !!!" + ex.getMessage());
                        } catch (IOException ex) {
                            System.err.println("No sense is added to the entry!!!" + ex.getMessage());
                        } catch (Exception ex) {
                            System.err.println("No behaviour is added to the entry!!!" + ex.getMessage());
                        }

                    }

                }
                if (!senses.isEmpty()) {
                    turtleLexicon.addEntry(entry);
                }
            }
        }

    }

    private Pair<Boolean, Sense> addSenseToEntry(String baseUri, String writtenForm, LineInfo lineInfo, String posTag) throws FileNotFoundException, IOException {
        Sense sense = new Sense();
        Boolean flag = false;
        if (posTag.contains(ADJECTIVE)) {
            flag = this.isValidReference(lineInfo.getObjectOriginal());

            Reference ref = new Restriction(baseUri + "RestrictionClass" + "_" + writtenForm,
                    lineInfo.getObjectOriginal(),
                    lineInfo.getPredicateOriginal());
            sense.setReference(ref);
        } else if (posTag.contains(NOUN) || posTag.contains(VERB)) {
            flag = this.isValidReference(lineInfo.getObjectOriginal());
            Reference ref = new SimpleReference(lineInfo.getObjectOriginal());
            sense.setReference(ref);
        }

        return new Pair<Boolean, Sense>(flag, sense);

    }

    public boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    private SyntacticBehaviour addBehaviourToEntry(Sense sense, String writtenForm, String posTag, String preposition) throws FileNotFoundException, IOException {
        SyntacticBehaviour behaviour = new SyntacticBehaviour();

        if (posTag.contains(ADJECTIVE)) {
            /*behaviour.setFrame(lexinfo + AdjectivePredicateFrame);
            behaviour.add(new SyntacticArgument(lexinfo + attributiveArg, writtenForm + "_" + AttrSynArg, null));
            behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, writtenForm + "_" + PredSynArg, null));
            sense.addSenseArg(new SenseArgument(lemon + attributiveArg, writtenForm + "_" + AttrSynArg));
            sense.addSenseArg(new SenseArgument(lemon + copulativeSubject, writtenForm + "_" + PredSynArg));*/
        } else if (posTag.contains(VERB)) {
            if (preposition != null) {
                /*behaviour.setFrame(lexinfo + IntransitivePPFrame);
                behaviour.add(new SyntacticArgument(lexinfo + prepositionalAdjunct, object, preposition));
                behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, subject, null));
                sense.addSenseArg(new SenseArgument(lemon + subjOfProp, subject));
                sense.addSenseArg(new SenseArgument(lemon + objOfProp, object));*/
            } else {
                /*behaviour.setFrame(lexinfo + TransitiveFrame);
                behaviour.add(new SyntacticArgument(lexinfo + subject, subject, null));
                behaviour.add(new SyntacticArgument(lexinfo + directObject, object, null));
                sense.addSenseArg(new SenseArgument(lemon + subjOfProp, subject));
                sense.addSenseArg(new SenseArgument(lemon + objOfProp, object));*/
            }

        } else if (posTag.contains(NOUN)) {
            /*behaviour.setFrame(lexinfo + NounPPFrame);
            behaviour.add(new SyntacticArgument(lexinfo + prepositionalAdjunct, object, preposition));
            behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, subject, null));
            sense.addSenseArg(new SenseArgument(lemon + subjOfProp, object));
            sense.addSenseArg(new SenseArgument(lemon + objOfProp, subject));*/
        }

        return behaviour;

    }

    private Provenance addProvinceToEntry() throws FileNotFoundException, IOException {
        Provenance provenance = new Provenance();
        //provenance.setFrequency(1);
        return provenance;
    }

    private Map<String, List<LexiconUnit>> setPartsOfSpeech(String postagOfWord, LexiconUnit LexiconUnit, Map<String, List<LexiconUnit>> lexicon) {
        List<LexiconUnit> temp = new ArrayList<LexiconUnit>();
        if (lexicon.containsKey(postagOfWord)) {
            temp = lexicon.get(postagOfWord);
        }
        temp.add(LexiconUnit);
        lexicon.put(postagOfWord, temp);
        return lexicon;
    }

    public String getOutputDir() {
        return lexiconDirectory;
    }

    private String getFirstTag(String posTag) {
        String firstWord = null;
        if (posTag.contains("_")) {
            String info[] = posTag.split("_");
            firstWord = info[0];
        } else {
            firstWord = posTag;
        }
        return firstWord;
    }

    public String getLexiconDirectory() {
        return lexiconDirectory;
    }

    public Map<String, List<LexiconUnit>> getLexiconPosTaggged() {
        return lexiconPosTaggged;
    }

    private String getPair(LineInfo lineInfo, String predictionRule) throws Exception {
        return lineInfo.getSubject() + " " + lineInfo.getPredicate() + lineInfo.getObject();
    }

    private Boolean isValidReference(String objectOriginal) {
        if (objectOriginal.contains("http://www.w3.org/2001/XMLSchema")
                || objectOriginal.contains("http://dbpedia.org/datatype/centimetre")) {
            return false;
        } else if (objectOriginal.contains("http") && objectOriginal.contains("http")) {
            return true;
        } else {
            return false;
        }
    }
    
    private Boolean isValidWrittenForm(String writtenForm) {
        if (this.isNumeric(writtenForm)) {
            return false;
        } else if (writtenForm.equals("also") ) {
            return false;
        } else {
            return true;
        }
    }

    private String modify(String writtenForm) {
        if(writtenForm.contains("_")){
            writtenForm=writtenForm.replace("_", " ") ; 
            //System.out.println("multiword written form::"+writtenForm);
           return writtenForm;
        } 
        return writtenForm;
    }
}
