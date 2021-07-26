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
import de.citec.sc.generator.exceptions.ConfigException;
import de.citec.sc.generator.exceptions.PerlException;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.io.LexiconSerialization;
import edu.stanford.nlp.util.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

/**
 *
 * @author elahi
 */
public class ResponseTransfer implements Constants {

    public ResponseTransfer() {

    }
    
    public ResultLex lexicalization(ConfigLex config) {
        String className = null;
        try {
            FileFolderUtils.delete(new File(interDir));
            FileFolderUtils.delete(new File(resultDir));
            String class_url = config.getClass_url();
            PerlQuery perlQuery = new PerlQuery(perlDir, scriptName, class_url);
            Boolean flag = perlQuery.getProcessSuccessFlag();
            System.out.println("Lexicalization process successfuly ended!!");
            return new ResultLex(className, flag);

        } catch (ConfigException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultLex(className, "Configuration file is correct.");

        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultLex(className, false);

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            return new ResultLex(className, false);

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
            //this.writeJsonLDToFile(model, jsonOutputDir, RDFFormat.JSONLD);
            //this.writeJsonLDToFile(model, turtleOutputDir, RDFFormat.TURTLE);
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


    public ResultDownload downloadData(ConfigDownload conf) {
        
        /*long startTime = System.nanoTime();
        Path file = Paths.get("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/app/");
        try {
            //Java 8: Stream class
            Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8);

            for (String line : (Iterable<String>) lines::iterator) {
                //System.out.println(line);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        long endTime = System.nanoTime();
        long elapsedTimeInMillis = TimeUnit.MILLISECONDS.convert((endTime - startTime), TimeUnit.NANOSECONDS);
        System.out.println("Total elapsed time: " + elapsedTimeInMillis + " ms");*/
        
        
        
        URL url;
         System.out.println(conf);
        try {
            url = new URL(conf.getUri_abstract());
            Path path = Paths.get("/home/elahi/a-teanga/dockerTest/ontology-lexicalization/app/");
            Files.copy(url.openStream(), path);
            return new ResultDownload(conf.getUri_abstract(), "successfully download the file!");
        } catch (MalformedURLException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultDownload(conf.getUri_abstract(), "failed to download turtle files!");
        }
        catch (FileAlreadyExistsException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultDownload(conf.getUri_abstract(), "File already exists!!");
        }
        catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultDownload(conf.getUri_abstract(), "failed to download turtle files!");
        }
        
       /* try {
            PerlQuery PerlQuery = new PerlQuery(conf.getUri_abstract());
            if (PerlQuery.getProcessSuccessFlag()) {
                return new ResultDownload(conf.getLinked_data(), "Successfull downloaded!!");
            } else {
                return new ResultDownload(conf.getLinked_data(), "downloaded failed!!");
            }

        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            return new ResultDownload(conf.getLinked_data(), "downloaded failed!!");
        }*/

    }

    String searchLemon(ConfigDownload conf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    

}
