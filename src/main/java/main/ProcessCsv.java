package main;

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
import com.example.analyzer.Lemmatizer;
import utils.CsvFile;
import utils.FileFolderUtils;
import utils.PropertyCSV;
import utils.StopWordRemoval;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.CreateTXT;
import main.Lexicon;
import main.LineInfo;
import main.NullInterestingness;
import static main.NullInterestingness.AllConf;
import static main.NullInterestingness.OBJECT;
import static main.NullInterestingness.PREDICATE;
import main.PredictionRules;

/**
 *
 * @author elahi
 */
public class ProcessCsv implements NullInterestingness, PredictionRules {
    
    public  ProcessCsv(String baseDir) throws Exception {
        String resourceDir = baseDir+"processData/";

        Set<String> posTag = new HashSet<String>();
        posTag.add("JJ");
        posTag.add("NN");
        posTag.add("VB");
        String txt = "txt";

        Logger LOGGER = Logger.getLogger(CreateTXT.class.getName());
        String outputDir = resourceDir;
        Lemmatizer lemmatizer = new Lemmatizer();
        //String  txtDir =  "src/main/resources/data" + "/" + "txt" +  "/"  ;
        //String  txtDir =  "src/main/resources/data" + "/" + "txt" +  "/"  ;
        String txtDir = resourceDir + "/" + txt + "/";
        //txtDir = "/var/www/html/ontologyLexicalization/data/";

        FileFolderUtils.createDirectory(txtDir);

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
        interestingness.add(AllConf);
        interestingness.add(Coherence);
        interestingness.add(Cosine);
        interestingness.add(Kulczynski);
        interestingness.add(IR);
        interestingness.add(MaxConf);
        for (String prediction : predictKBGivenLInguistic) {
            //String inputDir = baseDir + prediction + "/" ;
            String inputDir = baseDir + "/";
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
            throw new Exception("NO files found for " + prediction + " " + rawFileDir);
        }
    }

    private static void createExperimentLinesCsv(String directory, String dbo_prediction, String interestingness, List<File> classFiles) throws Exception {

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
            rows = csvFile.getRows(classFile);
            PropertyCSV propertyCSV = new PropertyCSV();
            numberOfClass = numberOfClass + 1;
            String className = classFile.getName().replace("http%3A%2F%2Fdbpedia.org%2Fontology%2F", "");
            System.out.println("interestingness:" + interestingness + " now running clssName::" + className + " " + dbo_prediction);

            Integer index = 0;
            for (String[] row : rows) {

                LineInfo lineInfo = new LineInfo(index, row, dbo_prediction, interestingness, propertyCSV);
                //System.out.println("lineInfo::"+lineInfo);

                if (lineInfo.getLine() != null) {
                    if (lineInfo.getLine().contains("XMLSchema#integer") || lineInfo.getLine().contains("XMLSchema#gYear")) {
                        continue;
                    } else if (lineInfo.getProbabilityValue().isEmpty()) {
                        continue;
                    }

                }
                /*for(String string:row){
                     if(string.contains("real")){
                      //System.out.println(string);
  
                     }
             
  
                 }*/

                try {
                    String nGram = lineInfo.getWord();
                    nGram = nGram.replace("\"", "");
                    nGram = nGram.toLowerCase().trim().strip();
                    nGram = nGram.replaceAll(" ", "_");
                    nGram = StopWordRemoval.deleteStopWord(nGram);

                    List<LineInfo> results = new ArrayList<LineInfo>();
                    if (lineLexicon.containsKey(nGram)) {
                        results = lineLexicon.get(nGram);
                        results.add(lineInfo);
                        lineLexicon.put(nGram, results);
                    } else {
                        results.add(lineInfo);
                        lineLexicon.put(nGram, results);

                    }
                } catch (Exception ex) {
                    // System.out.println("nGram::"+nGram);
                    continue;
                }

            }
            Lexicon lexicon = new Lexicon(directory);
            lexicon.preparePropertyLexicon(dbo_prediction, directory, className, interestingness, lineLexicon);

        }

    }

    private static Lexicon createLexicon(String qald9Dir, String directory, String dbo_prediction, String interestingness, String experimentID, Integer numberOfRules) throws Exception {
        Map<String, List<LineInfo>> lineLexicon = new TreeMap<String, List<LineInfo>>();
        List<String[]> rows = new ArrayList<String[]>();
        PropertyCSV propertyCSV = new PropertyCSV();

        File file = new File(directory + "/" + experimentID);
        CsvFile csvFile = new CsvFile(file);
        rows = csvFile.getRows(file);
        //System.out.println("number of rows:"+rows.size());
        //rows = csvFile.getRows(file, 1000.0, 300000);

        Integer index = 0, rowCount = 0;
        for (String[] row : rows) {
            if (rowCount == 0) {
                rowCount = rowCount + 1;
                continue;
            } else {
                rowCount = rowCount + 1;
            }
            LineInfo lineInfo = new LineInfo(index, row, dbo_prediction, interestingness, propertyCSV);
            index = index + 1;

            if (index >= numberOfRules) {
                break;
            }
            if (!lineInfo.getValidFlag()) {
                continue;
            }

            String nGram = lineInfo.getWord();
            nGram = nGram.replace("\"", "");
            nGram = nGram.toLowerCase().trim().strip();
            nGram = nGram.replaceAll(" ", "_");
            nGram = StopWordRemoval.deleteStopWord(nGram);

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
        Lexicon lexicon = new Lexicon(qald9Dir);
        lexicon.preparePropertyLexicon(dbo_prediction, directory, experimentID, interestingness, lineLexicon);
        return lexicon;
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


    

}