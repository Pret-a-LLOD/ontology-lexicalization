/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.restservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.generator.config.ConfigDownload;
import de.citec.generator.config.ConfigLemon;
import de.citec.generator.config.ConfigLex;
import de.citec.generator.config.Constants;
import de.citec.generator.core.PerlQuery;
import de.citec.generator.core.ProcessCsv;
import de.citec.generator.results.ResultDownload;
import de.citec.generator.results.ResultJsonLD;
import de.citec.generator.results.ResultLex;
import de.citec.sc.generator.exceptions.PerlException;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.lemon.core.Lexicon;
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

    //private Configuration config=null;
    public ResponseTransfer() {

        
        /*else if (task.contains(PROCESS_OUTPUT)) {
            try {
                de.citec.sc.lemon.core.Lexicon turtleLexicon = this.runProcessOutput(config);
                LexiconSerialization serializer = new LexiconSerialization();
                Model model = ModelFactory.createDefaultModel();
                serializer.serialize(turtleLexicon, model);
                this.writeJsonLDToFile(model, jsonOutput, RDFFormat.JSONLD);
                //this.writeJsonLDToFile(model, turtleOutput, RDFFormat.TURTLE);
                this.writeJsonLDtoString(model, scriptName, RDFFormat.JSONLD);
                System.out.println("processing ends ");
            }  catch (IOException ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("writing to file failed!!" + ex.getMessage());

            } catch (Exception ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("System output process does not work!!" + ex.getMessage());
            }
        }*/



        /*try {
            String jsonString = this.makeJsonString(config);
            //this.jsonLDString = jsonString;
           

            //this.runPerlScript(jsonString);
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
        }*/
    }
    
    public ResultLex lexicalization(ConfigLex config) {
        String className = null;
        try {
            String jsonString = this.makeJsonString(config);
            className = config.getClass_url();
            FileFolderUtils.delete(new File(interDir));
            FileFolderUtils.delete(new File(resultDir));
            PerlQuery perlQuery = new PerlQuery(perlDir, scriptName, jsonString);
            Boolean flag = perlQuery.getProcessSuccessFlag();
            System.out.println("Lexicalization process successfuly ended!!");
            return new ResultLex(className, flag);
        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultLex(className, false);

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            return new ResultLex(className,  false);

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!" + ex.getMessage());
            return new ResultLex(className, false);
        }

    }
    
    public String createLemon(ConfigLemon config) {
        try {
            String resourceDir = resultDir + processData;
            Lexicon turtleLexicon = new ProcessCsv(resultDir, resourceDir, config).getTurtleLexicon();
            LexiconSerialization serializer = new LexiconSerialization();
            Model model = ModelFactory.createDefaultModel();
            serializer.serialize(turtleLexicon, model);
            System.out.println("lemon creating ends!! ");
            this.writeJsonLDToFile(model, jsonOutputDir, RDFFormat.JSONLD);
            //this.writeJsonLDToFile(model, turtleOutput, RDFFormat.TURTLE);
            return this.writeJsonLDtoString(model, scriptName, RDFFormat.JSONLD);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Json creation fails!!" + ex.getMessage());
            return defaultResult;

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            return defaultResult;

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!" + ex.getMessage());
            return defaultResult;
        }

    }
    
    
    private ResultJsonLD makeClass(String jsonString) throws JsonProcessingException  {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, ResultJsonLD.class);
    }


    /*private de.citec.sc.lemon.core.Lexicon runProcessOutput(Configuration config) throws Exception {
        String resourceDir = resultDir + processData;
        return new ProcessCsv(resultDir, resourceDir, config).getTurtleLexicon();
    }*/

    private void writeJsonLDToFile(Model model, String fileName, RDFFormat type) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(new File(fileName));
        RDFDataMgr.write(out, model, type);
        out.close();

    }

    private String writeJsonLDtoString(Model model, String fileName, RDFFormat type) throws FileNotFoundException, IOException {
        StringWriter stringWriter = new StringWriter();
        RDFDataMgr.write(stringWriter, model, type);
        String jsonLDString = stringWriter.toString();
        stringWriter.close();
        return jsonLDString;
    }


    public String makeJsonString(ConfigLex config) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(config);
    }

    public ResultDownload downloadData(ConfigDownload conf) {
        try {
            PerlQuery PerlQuery = new PerlQuery(conf.getUri_abstract());
            if (PerlQuery.getProcessSuccessFlag()) {
                return new ResultDownload(conf.getLinked_data(), "Successfull downloaded!!");
            } else {
                return new ResultDownload(conf.getLinked_data(), "downloaded failed!!");
            }

        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultDownload(conf.getLinked_data(), "downloaded failed!!");
        }

    }

    String searchLemon(ConfigDownload conf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

}
