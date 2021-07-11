/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import java.util.*;
import com.example.config.*;
import com.example.process.*;
import de.citec.sc.lemon.io.LexiconSerialization;
import eu.monnetproject.lemon.LemonFactory;
import eu.monnetproject.lemon.LemonModel;
import eu.monnetproject.lemon.LemonModels;
import eu.monnetproject.lemon.LemonSerializer;
import eu.monnetproject.lemon.LinguisticOntology;
import eu.monnetproject.lemon.model.LexicalEntry;
import eu.monnetproject.lemon.model.LexicalForm;
import eu.monnetproject.lemon.model.Lexicon;
import eu.monnetproject.lemon.model.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lexinfo.LexInfo;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;


/**
 *
 * @author elahi
 */
public class ResponseTransfer {
    private  String baseDir = "results-v4/";
    private static String location = "perl/";
    private static String scriptName = "experiment.pl";
    private static String processData="processData/";
    //private static Lexicon  turtleLexicon = new de.citec.sc.lemon.core.Lexicon(LemonConstants.baseUri);

    
    private Map<String,String> lexicalEntries=new TreeMap<String,String>();
    
    
    public ResponseTransfer(Configuration config)  {
        //this.lexicalEntries.put(Constants.dummylexicalEntry,Constants.dummyLemon);
        //this.lexicalEntries.put(Constants.dummylexicalEntry2,Constants.dummyLemon2);
        //this.lexicalEntries.put(Constants.dummylexicalEntry3,Constants.dummyLemon3);
        /*PerlQuery PerlQuery = new PerlQuery(location, scriptName);
        String testString = PerlQuery.getResultString();*/
        //this.lexicalEntries.put(testString,testString);
        ProcessCsv process = null;
        String resourceDir = baseDir + processData;
        try {
            process = new ProcessCsv(baseDir, resourceDir);
            System.out.println(process.getTurtleLexicon());
            LexiconSerialization serializer = new LexiconSerialization();
            Model model = ModelFactory.createDefaultModel();
            serializer.serialize(process.getTurtleLexicon(), model);
            
            FileOutputStream out = new FileOutputStream(new File("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/lexicon.ttl"));
            RDFDataMgr.write(out, model, RDFFormat.JSONLD);
            out.close();
            System.out.println("lemon creation works!!!");
        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error!!!"+ex.getMessage());
        }
        
        
        //RDFDataMgr.write(out, model, RDFFormat.TURTLE);
        //out.close();
        //FileOutputStream out;
        //out = new FileOutputStream(new File("lexicon.ttl"));
       
    
           
            //LexiconSerialization serializer = new LexiconSerialization();
            //Model model = ModelFactory.createDefaultModel();
            //serializer.serialize(process.getTurtleLexicon(), model);
            /*Model model = ModelFactory.createDefaultModel();
            FileOutputStream out = new FileOutputStream(new File("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/lexicon.ttl"));
            RDFDataMgr.write(out, model, RDFFormat.JSONLD);
            out.close();*/
            /*Model model = ModelFactory.createDefaultModel() ;
            model.read("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/data.ttl") ;
            FileOutputStream out = new FileOutputStream(new File("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/test.json"));
            
            RDFDataMgr.write(out, model, RDFFormat.JSONLD);
            out.close();*/
            
              /* String triples
                       = "<http://dbpedia.org/resource/53debf646ad3465872522651> <http://dbpedia.org/resource/end> <http://dbpedia.org/resource/1407106906391> ."
                       + "\n<http://dbpedia.org/resource/53debf676ad3465872522655> <http://dbpedia.org/resource/foi> <http://dbpedia.org/resource/SpatialThing> .";
              */
               /*Model model = ModelFactory.createDefaultModel()
                       .read(IOUtils.toInputStream(triples, "UTF-8"), null, "N-TRIPLES");
               System.out.println("model size: " + model.size());*/
               
               /*String triples = "@prefix :        <http://localhost:8080/#> .\n"
                       + "\n"
                       + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                       + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                       + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
                       + "\n"
                       + "@base            <http://localhost:8080#> .\n"
                       + "\n"
                       + ":lexicon_en a    lemon:Lexicon ;\n"
                       + "  lemon:language \"en\" ;\n"
                       + "  lemon:entry    :spanish ;\n"
                       + "  lemon:entry    :spanish_res .\n"
                       + "\n"
                       + ":spanish a             lemon:LexicalEntry ;\n"
                       + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                       + "  lemon:canonicalForm  :spanish_lemma ;\n"
                       + "  lemon:synBehavior    :spanish_attrFrame, :spanish_predFrame ;\n"
                       + "  lemon:sense          :spanish_sense .\n"
                       + "\n"
                       + ":spanish_lemma lemon:writtenRep \"Spanish\"@en .\n"
                       + "\n"
                       + ":spanish_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                       + "  lexinfo:copulativeSubject :spanish_PredSynArg .\n"
                       + "\n"
                       + ":spanish_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                       + "  lexinfo:attributiveArg :spanish_AttrSynArg .\n"
                       + "\n"
                       + ":spanish_sense a  lemon:LexicalSense ;\n"
                       + "  lemon:reference :spanish_res ;\n"
                       + "  lemon:isA       :spanish_AttrSynArg, :spanish_PredSynArg .\n"
                       + "\n"
                       + ":spanish_res a   owl:Restriction ;\n"
                       + "  owl:onProperty <http://dbpedia.org/ontology/country> ;\n"
                       + "  owl:hasValue   <http://dbpedia.org/resource/Spain> .\n"
                       + "";*/
                       
               
               
               /*Model model = ModelFactory.createDefaultModel();
               FileOutputStream out = new FileOutputStream(new File("Atest.ttl"));
               RDFDataMgr.write(out, model, RDFFormat.TURTLE);*/

               /*Model model = ModelFactory.createDefaultModel();
                model= RDFDataMgr.loadModel("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/data.ttl") ;
                FileOutputStream out = new FileOutputStream(new File("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/test.json"));
                RDFDataMgr.write(out, model, RDFFormat.JSONLD);
                out.close();*/        
                //this.monnetProject();
                

       
        

    }

    

    public Map<String,String> getLexicalEntries() {
        return this.lexicalEntries;
    }

    private void monnetProject() {
        LemonSerializer serializer = LemonSerializer.newInstance();
        LemonModel model = serializer.create();
        Lexicon lexicon = model.addLexicon(
                URI.create("http://www.example.com/mylexicon"),
                "en" /*English*/);
        LexicalEntry entry = LemonModels.addEntryToLexicon(
                lexicon,
                URI.create("http://www.example.com/mylexicon/cat"),
                "cat",
                URI.create("http://dbpedia.org/resource/Cat"));

        LemonFactory factory = model.getFactory();
        LexicalForm pluralForm = factory.makeForm();
        pluralForm.setWrittenRep(new Text("cats", "en"));
        LinguisticOntology lingOnto = new LexInfo();
        pluralForm.addProperty(
                lingOnto.getProperty("number"),
                lingOnto.getPropertyValue("plural"));
        entry.addOtherForm(pluralForm);

        serializer.writeEntry(model, entry, lingOnto,
                new OutputStreamWriter(System.out));
    }



}
