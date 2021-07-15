/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

import com.example.core.PerlQuery;
import com.example.core.ProcessCsv;
import com.example.exceptions.PerlException;
import com.example.config.*;
import de.citec.sc.lemon.io.LexiconSerialization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
    private static String jsonOutput = "lexicon.json";
    private static String turtleOutput = "lexicon.ttl";
    private String jsonLDString = null;

    public ResponseTransfer(Configuration config) {
        try {
            //this.runPerlScript();
            de.citec.sc.lemon.core.Lexicon turtleLexicon=this.runProcessOutput(config);
            LexiconSerialization serializer = new LexiconSerialization();
            Model model = ModelFactory.createDefaultModel();
            serializer.serialize(turtleLexicon, model);
            this.writeJsonLDToFile(model, jsonOutput, RDFFormat.JSONLD);
             this.writeJsonLDToFile(model, turtleOutput, RDFFormat.TURTLE);
            this.writeJsonLDtoString(model, scriptName, RDFFormat.JSONLD);
        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ontology lexicalization::" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("File not found!!" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("writing to file failed!!" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("System output process does not work!!");
            this.jsonLDString = ex.getMessage();
        }

    }

    private Boolean runPerlScript() throws PerlException {
        PerlQuery PerlQuery = new PerlQuery(location, scriptName);
        if(PerlQuery.getProcessSuccessFlag())
            return true;
        else 
            throw new PerlException("Perl script does not work!!");
    }

    private de.citec.sc.lemon.core.Lexicon runProcessOutput(Configuration config) throws Exception {
        String resourceDir = baseDir + processData;
        return new ProcessCsv(baseDir, resourceDir,config.getBaseUri()).getTurtleLexicon();
    }

    private void writeJsonLDToFile(Model model, String fileName, RDFFormat type) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(new File(fileName));
        RDFDataMgr.write(out, model, type);
        out.close();

    }

    private void writeJsonLDtoString(Model model, String fileName, RDFFormat type) throws FileNotFoundException, IOException {
        StringWriter stringWriter = new StringWriter();
        RDFDataMgr.write(stringWriter, model, type);
        jsonLDString = stringWriter.toString();
        stringWriter.close();
    }

    public String getJsonLDString() {
        return jsonLDString;
    }

}
