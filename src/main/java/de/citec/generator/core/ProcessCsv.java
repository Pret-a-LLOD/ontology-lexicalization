package de.citec.generator.core;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.StopWordRemoval;
import de.citec.sc.generator.utils.PropertyCSV;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.generator.config.LemonConstants;
import de.citec.sc.generator.analyzer.Lemmatizer;
import de.citec.generator.config.ConfigDownload;
import de.citec.generator.config.ConfigLemon;
import de.citec.generator.config.ConfigLex;
import static de.citec.generator.config.Constants.UNDERSCORE;
import java.io.File;
import java.util.*;
import de.citec.sc.lemon.core.Lexicon;
import java.util.logging.Logger;
import de.citec.generator.config.PredictionPatterns;

/**
 *
 * @author elahi
 */
public class ProcessCsv implements  PredictionPatterns,LemonConstants {

    private Lexicon turtleLexicon = null;
    private Integer rankLimit = 0;
    private Logger LOGGER = Logger.getLogger(ProcessCsv.class.getName());
    private Lemmatizer lemmatizer = new Lemmatizer();


    public  ProcessCsv(String baseDir,String resourceDir,ConfigLemon config) throws Exception {
        /*System.out.println("config::"+config);
        System.out.println("baseDir::"+baseDir);
        System.out.println("resourceDir::"+resourceDir);
        System.out.println("basic URI::"+config.getUri_basic());*/
        this.turtleLexicon=new Lexicon(config.getUri_basic());
        this.rankLimit=config.getRank_limit();
        Set<String> posTag = new HashSet<String>();
        posTag.add("JJ");
        posTag.add("NN");
        posTag.add("VB");
        String outputDir = resourceDir;
      
        List<String> predictKBGivenLInguistic = new ArrayList<String>(Arrays.asList(
                predict_l_for_s_given_po,
                predict_localized_l_for_s_given_po,
                predict_l_for_s_given_p,
                predict_localized_l_for_s_given_p,
                predict_l_for_s_given_o,
                predict_l_for_o_given_sp,
                predict_localized_l_for_o_given_sp,
                predict_l_for_o_given_s,
                predict_l_for_o_given_p,
                predict_localized_l_for_o_given_p,
                predict_p_for_s_given_l,
                predict_o_for_s_given_l,
                predict_p_for_o_given_l,
                predict_po_for_s_given_l,
                predict_s_for_o_given_l,
                predict_po_for_s_given_localized_l,
                predict_p_for_s_given_localized_l,
                predict_p_for_o_given_localized_l,
                predict_sp_for_o_given_localized_l,
                predict_sp_for_o_given_l
        ));

        List<String> interestingness = new ArrayList<String>();
        //interestingness.add(AllConf);
        //interestingness.add(Coherence);
        interestingness.add(Cosine);
        //interestingness.add(Kulczynski);
        //interestingness.add(IR);
        //interestingness.add(MaxConf);
        for (String prediction : predictKBGivenLInguistic) {
            //String inputDir = baseDir + prediction + "/" ;
            String inputDir = baseDir + "/";
            //System.out.println("prediction::"+prediction);
            for (String inter : interestingness) {
                outputDir = resourceDir + "/" + prediction + "/" + inter + "/";
                FileFolderUtils.createDirectory(outputDir);
                  this.generate(inputDir, outputDir, prediction, inter, LOGGER, ".csv");
                //System.out.println(outputDir);
                //CreateTXT.resultStrTxt(posTag,outputDir,txtDir, prediction, lemmatizer, inter);
            }
        }

        
       
    }

    public void generate(String rawFileDir, String outputDir, String prediction, String givenInterestingness, Logger givenLOGGER, String fileType) throws Exception {
        
        List<File> files = FileFolderUtils.getSpecificFiles(rawFileDir, prediction + "-", ".csv");
        if (!files.isEmpty()) {
            createExperimentLinesCsv(outputDir, prediction, givenInterestingness, files);
        } else {
            throw new Exception("NO ontology lexicalization files are found for processing"+". "+"Run lexicalization process first");
        }
    }

    private  void createExperimentLinesCsv(String outputDir, String prediction, String interestingness, List<File> classFiles) throws Exception {

        List<String[]> rows = new ArrayList<String[]>();
        Integer numberOfClass = 0;
        Integer maximumNumberOflines = 300000;

        for (File classFile : classFiles) {
            Map<String, List<LineInfo>> lineLexicon = new TreeMap<String, List<LineInfo>>();
            String fileName = classFile.getName();
            /*if (!fileName.contains("AcademicJournal")) {
                continue;
            }*/
            CsvFile csvFile = new CsvFile(classFile);
            //rows = csvFile.getManualRow(classFile, 1000.0, 300000);
            //rows = csvFile.getRows(classFile);
            rows = csvFile.getRowsManual();
            PropertyCSV propertyCSV = new PropertyCSV();
            numberOfClass = numberOfClass + 1;
            String className = classFile.getName().replace("http%3A%2F%2Fdbpedia.org%2Fontology%2F", "");
            //important //System.out.println
            //System.out.println("interestingness:" + interestingness + " now running clssName::" + className + " " + prediction);

            Integer index = 0;
            for (String[] row : rows) {

                LineInfo lineInfo = new LineInfo(index, row, prediction, interestingness, propertyCSV);
                //System.out.println("lineInfo::"+lineInfo);

                if (lineInfo.getLine() != null) {
                    if (lineInfo.getLine().contains("XMLSchema#integer") || lineInfo.getLine().contains("XMLSchema#gYear")) {
                        continue;
                    } else if (lineInfo.getProbabilityValue().isEmpty()) {
                        continue;
                    }else if (!lineInfo.getValidFlag()) {
                        continue;
                    }

                }
                /*for(String string:row){
                     if(string.contains("real")){
                      //System.out.println(string);
  
                     }
             
  
                 }*/

                try {
                    /*String nGram = lineInfo.getWord();
                    nGram = nGram.replace("\"", "");
                    nGram = nGram.toLowerCase().trim().strip();
                    nGram = nGram.replaceAll(" ", "_");
                    nGram = StopWordRemoval.deleteStopWord(nGram);*/
                    String nGram = this.isValidWord(lineInfo.getWord(),lineInfo.getnGramNumber());

                    if (nGram != null) {
                        //System.out.println("nGram::::::::::::::::::::::::::::::;;" + nGram);
                        List<LineInfo> results = new ArrayList<LineInfo>();
                        if (lineLexicon.containsKey(nGram)) {
                            results = lineLexicon.get(nGram);
                            results.add(lineInfo);
                            lineLexicon.put(nGram, results);
                        } else {
                            results.add(lineInfo);
                            lineLexicon.put(nGram, results);

                        }
                    }
                   
                } catch (Exception ex) {
                    // System.out.println("nGram::"+nGram);
                    continue;
                }

            }
            LemonCreator lexicon = new LemonCreator(outputDir,turtleLexicon,rankLimit);
            lexicon.preparePropertyLexicon(prediction, outputDir, className, interestingness, lineLexicon);

        }
               
    }

    private static String[] findParameter(String[] info) {
        String[] parameters = new String[3];
        for (Integer index = 0; index < info.length; index++) {
            if (index == 0) {
                parameters[index] = info[index];
            }
            if (index == 1) {
                parameters[index] = info[index];
            } else if (index == 2) {
                parameters[index] = info[index];
            }
        }
        return parameters;
    }

    public static boolean isKBValid(String word) {

        if (word.contains("#integer") || word.contains("#double")) {
            return true;
        }
        return false;
    }
    
    private String isValidWord(String word,Integer nGramNumber) {
        String nGram = word;
        nGram = nGram.replace("\"", "");
        nGram = nGram.toLowerCase().trim().strip();
        nGram = nGram.replaceAll(" ", "_");
        nGram = StopWordRemoval.deleteStopWord(nGram);
        nGram = nGram.replaceAll("_", UNDERSCORE);
        nGram = nGram.replaceAll("[^A-Za-z0-9]", "");
        nGram = nGram.replace(UNDERSCORE, "_");
        
        if (nGram.contains("_")) {
            if ((nGram.split("_").length > 2)) {
                return null;
            } else {
                return nGram;
            }
        } else if (nGram.length() > 2) {
            return nGram;
        }

        return null;
    }

   
    public  Lexicon getTurtleLexicon() {
        return turtleLexicon;
    }


    

}
