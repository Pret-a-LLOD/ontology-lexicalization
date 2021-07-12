/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import java.util.*;
import com.example.config.*;
import com.example.exceptions.PerlException;
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

    private String baseDir = "results-v4/";
    private static String location = "perl/";
    private static String scriptName = "experiment.pl";
    private static String processData = "processData/";
    private static String lexiconFile = "lexicon.json";

    private Map<String, String> lexicalEntries = new TreeMap<String, String>();

    public ResponseTransfer(Configuration config) {
        try {
            this.runPerlScript();
            this.writeJsonLD(this.runProcessOutput(), lexiconFile);
        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("perl script is not working!!" + ex.getMessage());
            return;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!!" + ex.getMessage());
            return;
        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("writing to file failed!!" + ex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("System output process does not work!!");
        }

    }

    private Boolean runPerlScript() {
        PerlQuery PerlQuery = new PerlQuery(location, scriptName);
        String testString = PerlQuery.getResultString();
        return true;
    }

    private de.citec.sc.lemon.core.Lexicon runProcessOutput() throws Exception {
        String resourceDir = baseDir + processData;
        return new ProcessCsv(baseDir, resourceDir).getTurtleLexicon();
    }

    private void writeJsonLD(de.citec.sc.lemon.core.Lexicon lexicon, String fileName) throws FileNotFoundException, IOException {
        LexiconSerialization serializer = new LexiconSerialization();
        Model model = ModelFactory.createDefaultModel();
        serializer.serialize(lexicon, model);
        FileOutputStream out = new FileOutputStream(new File(fileName));
        RDFDataMgr.write(out, model, RDFFormat.JSONLD);
        out.close();
        System.out.println("lemon creation works!!!");
    }

    public Map<String, String> getLexicalEntries() {
        return this.lexicalEntries;
    }

}
