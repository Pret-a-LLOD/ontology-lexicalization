/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

import com.example.analyzer.PosAnalyzer;
import static com.example.analyzer.TextAnalyzer.OBJECT;
import com.example.process.*;
import com.example.utils.FileFolderUtils;
import static de.citec.sc.lemon.core.Language.EN;
import de.citec.sc.lemon.core.Lexicon;
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
public class LexiconJson implements PredictionRules {

    private String lexiconDirectory = null;
    private String lexinfo = "http://www.lexinfo.net/ontology/2.0/lexinfo#";
    private String lemon = "http://lemon-model.net/lemon#";
    private String lexinfo_adjective = lexinfo + "/" + "adjective";
    private String baseUri = "http://localhost:8080/";
    private Lexicon turtleLexicon=null;

    private Map<String, List<LexiconUnit>> lexiconPosTaggged = new TreeMap<String, List<LexiconUnit>>();

    public LexiconJson(String outputDir,Lexicon turtleLexicon) throws IOException {
        this.lexiconDirectory = outputDir;
        this.turtleLexicon=turtleLexicon;
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

    private void writeFileLemon(String prediction, Map<String, List<LexiconUnit>> posTaggedLex) throws IOException {
        String posLexInfo = null, givenPosTag = null;
        if (prediction.equals(predict_po_for_s_given_localized_l)
                || prediction.equals(predict_po_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_adjective;
            givenPosTag = "JJ";
        }/*else if (prediction.equals(PredictionRules.predict_p_for_o_given_localized_l)
                || prediction.equals(PredictionRules.predict_p_for_s_given_l)) {
            //System.out.println("prediction::" + prediction);
            posLexInfo = lexinfo_adjective;
            givenPosTag = "VB";
        }*/
        else {
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
                //System.out.println("writtenForm::" + writtenForm);
                de.citec.sc.lemon.core.LexicalEntry entry = new de.citec.sc.lemon.core.LexicalEntry(EN);
                entry.setCanonicalForm(writtenForm);
                entry.setPOS(posLexInfo);
                entry.setURI(baseUri + writtenForm);

                for (Integer rank : ranks.keySet()) {
                    List<LineInfo> rankLineInfo = ranks.get(rank);
                    for (LineInfo lineInfo : rankLineInfo) {
                        //System.out.println("predicate::" + lineInfo.getPredicateOriginal());
                        //System.out.println("object::" + lineInfo.getObjectOriginal());
                        //entry=addSense(entry,writtenForm,lineInfo);
                        //System.out.println("entry::" + entry);
        
                        Sense sense = new Sense();
                        Reference ref = new Restriction(baseUri + "RestrictionClass" + writtenForm,
                                lineInfo.getPredicateOriginal(),
                                lineInfo.getObjectOriginal());
                        sense.setReference(ref);

                        SyntacticBehaviour behaviour = new SyntacticBehaviour();
                        behaviour.setFrame(lexinfo + "AdjectivePredicateFrame");
                        /*behaviour.add(new SyntacticArgument(lexinfo + "prepositionalObject", "object", preposition));
                          behaviour.add(new SyntacticArgument(lexinfo + "copulativeArg", "subject", null));
                          sense.addSenseArg(new SenseArgument(lemon + "subjOfProp", "subject"));
                          sense.addSenseArg(new SenseArgument(lemon + "objOfProp", "object"));*/
                        entry.addSyntacticBehaviour(behaviour, sense);
                    }
                    
                }
                turtleLexicon.addEntry(entry);
            }
        }

      

        /*LexiconSerialization serializer = new LexiconSerialization();
        Model model = ModelFactory.createDefaultModel();
        serializer.serialize(lexicon, model);

        FileOutputStream out = new FileOutputStream(new File("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/" + "lexicon.ttl"));
        RDFDataMgr.write(out, model, RDFFormat.TURTLE);
        out.close();*/
    }

    private de.citec.sc.lemon.core.LexicalEntry addSense(de.citec.sc.lemon.core.LexicalEntry entry,String writtenForm, LineInfo lineInfo) throws FileNotFoundException, IOException {
        Sense sense = new Sense();
        Reference ref = new Restriction(baseUri + "RestrictionClass" + writtenForm,
                lineInfo.getPredicateOriginal(),
                lineInfo.getObjectOriginal());
        sense.setReference(ref);
       
        SyntacticBehaviour behaviour  = new SyntacticBehaviour();
        behaviour.setFrame(lexinfo + "AdjectivePredicateFrame");
        /*behaviour.add(new SyntacticArgument(lexinfo + "prepositionalObject", "object", preposition));
        behaviour.add(new SyntacticArgument(lexinfo + "copulativeArg", "subject", null));
        sense.addSenseArg(new SenseArgument(lemon + "subjOfProp", "subject"));
        sense.addSenseArg(new SenseArgument(lemon + "objOfProp", "object"));*/
        entry.addSyntacticBehaviour(behaviour, sense);

        return entry;
    }

    /*public  void buildLemon(String posTag,String mylexicon) throws Exception {
        final LemonSerializer serializer = LemonSerializer.newInstance();
        final LemonModel model = serializer.create();
        final eu.monnetproject.lemon.model.Lexicon lexicon = model.addLexicon(
                URI.create(uri+mylexicon),
                "en");
        final LexicalEntry entry = LemonModels.addEntryToLexicon(
                lexicon,
                URI.create("http://localhost:8080/mylexicon/cat"),
                "cat",
                URI.create("http://dbpedia.org/resource/Cat"));

        final LemonFactory factory = model.getFactory();
        final LexicalForm pluralForm = factory.makeForm();
        
        entry.

        pluralForm.setWrittenRep(
                new Text("cats", "en"));
        final LinguisticOntology lingOnto = new LexInfo();

        pluralForm.addProperty(
                lingOnto.getProperty("number"),
                lingOnto.getPropertyValue("plural"));
        entry.addOtherForm(pluralForm);

        serializer.writeEntry(model, entry, lingOnto,
                new OutputStreamWriter(System.out));

    }*/
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

    /*for (String postag : posTaggedLex.keySet()) {
            List<LexiconUnit> lexiconUnts = posTaggedLex.get(postag);
            for (LexiconUnit lexiconUnit : lexiconUnts) {
                LinkedHashMap<Integer, List<LineInfo>> ranks = lexiconUnit.getLineInfos();
                String writtenForm = lexiconUnit.getWord();
                for (Integer rank : ranks.keySet()) {

                }
                if (postag.contains("JJ")) {
                    Templates templates = new Templates(postag, "spanish", writtenForm);
                    System.out.println("templates:" + templates.getResultStr());
                }
            }
        }*/
}
