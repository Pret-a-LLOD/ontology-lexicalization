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
import de.citec.generator.core.CommandLine;
import de.citec.generator.core.ProcessOutput;
import de.citec.generator.results.ResultDownload;
import de.citec.generator.results.ResultJsonLD;
import de.citec.generator.results.ResultLex;
import de.citec.sc.generator.exceptions.ConfigException;
import de.citec.sc.generator.exceptions.PerlException;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.lemon.core.Lexicon;
import de.citec.sc.lemon.io.LexiconSerialization;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    public ResponseTransfer() {

    }

    public ResultLex lexicalization(ConfigLex config) {
        String class_url = null;
        try {
            FileFolderUtils.delete(new File(interDir));
            FileFolderUtils.delete(new File(resultDir));
            class_url = config.getClass_url();
            CommandLine commandLine = new CommandLine(perlDir, lexGenScriptName, class_url);
            Boolean flag = commandLine.getProcessSuccessFlag();
            System.out.println("Lexicalization process successfuly ended!!");
            return new ResultLex(class_url, flag);

        } catch (ConfigException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultLex(class_url, "Configuration file is correct.");

        } catch (PerlException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return new ResultLex(class_url, false);

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            return new ResultLex(class_url, false);

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!" + ex.getMessage());
            return new ResultLex(class_url, false);
        }

    }

    public String createLemon(ConfigLemon config) {
        try {
            String resourceDir = resultDir + processData;
            Lexicon turtleLexicon = new ProcessOutput(resultDir, resourceDir, config, "createLemon").getTurtleLexicon();
            LexiconSerialization serializer = new LexiconSerialization();
            Model model = ModelFactory.createDefaultModel();
            serializer.serialize(turtleLexicon, model);
            System.out.println("lemon creating ends!! ");
            //this.writeJsonLDToFile(model, jsonOutputDir, RDFFormat.JSONLD);
            //this.writeJsonLDToFile(model, turtleOutputDir, RDFFormat.TURTLE);
            return this.writeJsonLDtoString(model, lexGenScriptName, RDFFormat.JSONLD);
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

    private ResultJsonLD makeClass(String jsonString) throws JsonProcessingException {
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

    /* public ResultDownload downloadData(ConfigDownload conf) {
  
        Map<String, String> urlOutputPath = new HashMap<String, String>();
        System.out.println(conf.toString());
        urlOutputPath.put(conf.getUri_abstract(), abstractOutputPath);
        urlOutputPath.put(conf.getUri_property(), propertyOutputPath);
        urlOutputPath.put(conf.getUri_label(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_literal(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_instance(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_object(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_anchor_sorted(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_anchor(), semanticOutputPath);

        for (String urlString : urlOutputPath.keySet()) {
            String outputPath = null, filename = null;
            outputPath = urlOutputPath.get(urlString);

            URL url;
            try {
                url= new File(urlString).toURI().toURL();
                filename = new File(url.getPath()).getName();
                outputPath = outputPath + filename;

            } catch (MalformedURLException ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(filename, "failed to download turtle files!" + ex.getMessage());
            }

            try (
                    InputStream inputStream = url.openStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    FileOutputStream fileOutputStream = new FileOutputStream(outputPath);) {
                byte[] bucket = new byte[2048];
                int numBytesRead;

                while ((numBytesRead = bufferedInputStream.read(bucket, 0, bucket.length)) != -1) {
                    fileOutputStream.write(bucket, 0, numBytesRead);
                }
                System.out.println("downloding:" + url + "to the location:" + outputPath + " is completed!!");
            } catch (MalformedURLException ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(urlString, "failed to download turtle files!" + ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(urlString, "failed to download turtle files!" + ex.getMessage());
            }

        }

        return new ResultDownload("all innput files", "successfully downloaded");

    }*/
    public ResultDownload downloadData(ConfigDownload conf) {

        Map<String, String> urlOutputPath = new HashMap<String, String>();
        System.out.println(conf.toString());
        urlOutputPath.put(conf.getUri_abstract(), abstractOutputPath);
        urlOutputPath.put(conf.getUri_property(), propertyOutputPath);
        /*urlOutputPath.put(conf.getUri_label(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_literal(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_instance(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_object(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_anchor_sorted(), semanticOutputPath);
        urlOutputPath.put(conf.getUri_anchor(), semanticOutputPath);*/

        for (String urlString : urlOutputPath.keySet()) {
            String outputPath = null, filename = null;

            outputPath = urlOutputPath.get(urlString);

            URL url;
            try {
                url = new URL(urlString);
                filename = new File(url.getPath()).getName();
                outputPath = outputPath + filename;

            } catch (MalformedURLException ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(filename, "failed to download turtle files!" + ex.getMessage());
            }

            try (
                    InputStream inputStream = url.openStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    FileOutputStream fileOutputStream = new FileOutputStream(outputPath);) {
                byte[] bucket = new byte[2048];
                int numBytesRead;

                while ((numBytesRead = bufferedInputStream.read(bucket, 0, bucket.length)) != -1) {
                    fileOutputStream.write(bucket, 0, numBytesRead);
                }
                System.out.println("downloding:" + url + "to the location:" + outputPath + " is completed!!");
            } catch (MalformedURLException ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(urlString, "failed to download turtle files!" + ex.getMessage());
            } catch (Exception ex) {
                Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
                return new ResultDownload(urlString, "failed to download turtle files!" + ex.getMessage());
            }

        }

        return new ResultDownload("all innput files", "successfully downloaded");

    }

    public String searchLemon(ConfigDownload conf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ResultLex createLexicalEntry(ConfigLemon config) {
        String class_url = "test"; Boolean flag =false;
        try {
            String resourceDir = resultDir + processData;
            Lexicon turtleLexicon = new ProcessOutput(resultDir, resourceDir, config, ENDPOINT_QUESTION_ANSWER_LEX_ENTRY).getTurtleLexicon();
            System.out.println("Lexicalization process successfuly ended!!");
            return new ResultLex(class_url, flag);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Json creation fails!!" + ex.getMessage());
            return new ResultLex(class_url, "Configuration file is correct.");

        } catch (IOException ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("writing to file failed!!" + ex.getMessage());
            return new ResultLex(class_url, "Configuration file is correct.");

        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!" + ex.getMessage());
            return new ResultLex(class_url, "Configuration file is correct.");
        }
    }
    
    public ResultLex createQuestionAnswer(ConfigLemon config) {
        String class_url = "test";
        Boolean flag = false;
        try {
            String command = "java -jar " + questionGenScript + " " + language + " " + lexiconDir + " " + outputDir + " " + numberOfentitiesToConsider + " "+CSV + " " + datasetDir + datasetConfig;
            System.out.println("command:" + command);
            CommandLine commandLine = new CommandLine(command);
            flag = commandLine.getProcessSuccessFlag();
            System.out.println("Lexicalization process successfuly ended!!");
            return new ResultLex(class_url, flag);
        } catch (Exception ex) {
            Logger.getLogger(ResponseTransfer.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("System output process does not work!!" + ex.getMessage());
            return new ResultLex(class_url, "Configuration file is correct.");
        }
    }

    private String writeLexicalEntryCSV(Lexicon turtleLexicon) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
