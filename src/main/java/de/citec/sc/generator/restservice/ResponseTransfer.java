/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.generator.config.Configuration;
import de.citec.generator.config.Constants;
import de.citec.generator.core.PerlQuery;
import de.citec.generator.core.ProcessCsv;
import de.citec.sc.generator.exceptions.PerlException;
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
public class ResponseTransfer implements Constants {

    private String jsonLDString = null;

    public ResponseTransfer(Configuration config) {
        try {
            String jsonString = this.makeJsonString(config);
            //this.jsonLDString = jsonString;
            this.runPerlScript(jsonString);
            de.citec.sc.lemon.core.Lexicon turtleLexicon = this.runProcessOutput(config);
            LexiconSerialization serializer = new LexiconSerialization();
            Model model = ModelFactory.createDefaultModel();
            serializer.serialize(turtleLexicon, model);
            this.writeJsonLDToFile(model, jsonOutput, RDFFormat.JSONLD);
            //this.writeJsonLDToFile(model, turtleOutput, RDFFormat.TURTLE);
            this.writeJsonLDtoString(model, scriptName, RDFFormat.JSONLD);
            System.out.println("processing ends ");

        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ontology lexicalization::" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("File not found!!" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            this.jsonLDString = ex.getMessage();

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!");
            this.jsonLDString = ex.getMessage();
        }

    }

    private Boolean runPerlScript(String jsonString) throws PerlException {
        PerlQuery PerlQuery = new PerlQuery(location, scriptName,jsonString);
        if (PerlQuery.getProcessSuccessFlag()) {
            return true;
        } else {
            throw new PerlException("Perl script does not work!!");
        }
    }

    private de.citec.sc.lemon.core.Lexicon runProcessOutput(Configuration config) throws Exception {
        String resourceDir = baseDir + processData;
        return new ProcessCsv(baseDir, resourceDir, config).getTurtleLexicon();
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
    
    public String makeJsonString(Configuration config) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(config);
        return json;
    }


}
