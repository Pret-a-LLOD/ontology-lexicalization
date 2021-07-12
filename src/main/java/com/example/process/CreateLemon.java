/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

import com.example.analyzer.PosAnalyzer;
import com.example.analyzer.TextAnalyzer;
import static com.example.analyzer.TextAnalyzer.OBJECT;
import com.example.process.*;
import com.example.utils.FileFolderUtils;
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
import de.citec.sc.lemon.io.LexiconSerialization;
import static de.citec.sc.lemon.vocabularies.LEXINFO.preposition;
import eu.monnetproject.lemon.LemonFactory;
import eu.monnetproject.lemon.LemonModel;
import eu.monnetproject.lemon.LemonModels;
import eu.monnetproject.lemon.LemonSerializer;
import eu.monnetproject.lemon.LinguisticOntology;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.lexinfo.LexInfo;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/**
 *
 * @author elahi
 */
public class CreateLemon implements PredictionRules, LemonConstants, TextAnalyzer {

    private String lexiconDirectory = null;
    private Lexicon turtleLexicon = null;

    private Map<String, List<LexiconUnit>> lexiconPosTaggged = new TreeMap<String, List<LexiconUnit>>();

    public CreateLemon(String outputDir, Lexicon turtleLexicon) throws IOException {
        this.lexiconDirectory = outputDir;
        this.turtleLexicon = turtleLexicon;
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
        } else if (prediction.equals(PredictionRules.predict_p_for_o_given_localized_l)
                || prediction.equals(PredictionRules.predict_p_for_o_given_l)
                || prediction.equals(PredictionRules.predict_p_for_s_given_localized_l)
                || prediction.equals(PredictionRules.predict_p_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_verb;
            givenPosTag = TextAnalyzer.VERB;
        } else if (prediction.equals(PredictionRules.predict_o_for_s_given_l)) {
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
                System.out.println("prediction::" + prediction);
                System.out.println("writtenForm::" + writtenForm);
                de.citec.sc.lemon.core.LexicalEntry entry = new de.citec.sc.lemon.core.LexicalEntry(EN);
                entry.setCanonicalForm(writtenForm);
                entry.setPOS(posLexInfo);
                entry.setURI(baseUri + writtenForm);
                entry.setPOS(posLexInfo);

                for (Integer rank : ranks.keySet()) {
                    List<LineInfo> rankLineInfo = ranks.get(rank);
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

                            sense = this.addSenseToEntry(writtenForm, lineInfo, postag);
                            System.out.println(sense);
                            behaviour = this.addBehaviourToEntry(sense, writtenForm, postag,lineInfo.getPreposition());
                            provenance = this.addProvinceToEntry();

                            if (sense != null && behaviour != null && provenance != null) {
                                entry.addSyntacticBehaviour(behaviour, sense);
                                entry.addProvenance(provenance, sense);
                            }

                        } catch (NullPointerException ex) {
                            System.out.println("either sense or behavior or sense is not !!!" + ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("No sense is added to the entry!!!" + ex.getMessage());
                        } catch (Exception ex) {
                            System.out.println("No behaviour is added to the entry!!!" + ex.getMessage());
                        }

                    }

                }
                turtleLexicon.addEntry(entry);
            }
        }

    }

    private Sense addSenseToEntry(String writtenForm, LineInfo lineInfo, String posTag) throws FileNotFoundException, IOException {
        Sense sense = new Sense();
        if (posTag.contains(ADJECTIVE)) {
            Reference ref = new Restriction(baseUri + "RestrictionClass" + "_" + writtenForm,
                    lineInfo.getObjectOriginal(),
                    lineInfo.getPredicateOriginal());
            sense.setReference(ref);
        } else if (posTag.contains(NOUN)) {
            Reference ref = new SimpleReference(lineInfo.getObjectOriginal());
            sense.setReference(ref);
        } else if (posTag.contains(VERB)) {
            Reference ref = new SimpleReference(lineInfo.getPredicateOriginal());
            sense.setReference(ref);
        }

        return sense;

    }

    private SyntacticBehaviour addBehaviourToEntry(Sense sense, String writtenForm, String posTag, String preposition) throws FileNotFoundException, IOException {
        SyntacticBehaviour behaviour = new SyntacticBehaviour();

        if (posTag.contains(ADJECTIVE)) {
            behaviour.setFrame(lexinfo + AdjectivePredicateFrame);
            behaviour.add(new SyntacticArgument(lexinfo + attributiveArg, writtenForm + "_" + AttrSynArg, null));
            behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, writtenForm + "_" + PredSynArg, null));
            sense.addSenseArg(new SenseArgument(lemon + attributiveArg, writtenForm + "_" + AttrSynArg));
            sense.addSenseArg(new SenseArgument(lemon + copulativeSubject, writtenForm + "_" + PredSynArg));
        } else if (posTag.contains(VERB)) {
            if (preposition != null) {
                behaviour.setFrame(lexinfo + IntransitivePPFrame);
                behaviour.add(new SyntacticArgument(lexinfo + prepositionalAdjunct, object, preposition));
                behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, subject, null));
                sense.addSenseArg(new SenseArgument(lemon + subjOfProp, subject));
                sense.addSenseArg(new SenseArgument(lemon + objOfProp, object));
            } else {
                behaviour.setFrame(lexinfo + TransitiveFrame);
                behaviour.add(new SyntacticArgument(lexinfo + subject, subject, null));
                behaviour.add(new SyntacticArgument(lexinfo + directObject, object, null));
                sense.addSenseArg(new SenseArgument(lemon + subjOfProp, subject));
                sense.addSenseArg(new SenseArgument(lemon + objOfProp, object));
            }

        } else if (posTag.contains(NOUN)) {
            behaviour.setFrame(lexinfo + NounPPFrame);
            behaviour.add(new SyntacticArgument(lexinfo + prepositionalAdjunct, object, preposition));
            behaviour.add(new SyntacticArgument(lexinfo + copulativeSubject, subject, null));
            sense.addSenseArg(new SenseArgument(lemon + subjOfProp, object));
            sense.addSenseArg(new SenseArgument(lemon + objOfProp, subject));
        }

        return behaviour;

    }

    private Provenance addProvinceToEntry() throws FileNotFoundException, IOException {
        Provenance provenance = new Provenance();
        provenance.setFrequency(1);
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
}
