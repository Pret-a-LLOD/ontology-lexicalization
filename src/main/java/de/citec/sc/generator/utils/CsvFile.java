/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

import de.citec.sc.generator.analyzer.PosAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.OBJECT;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_o_given_p;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_o_given_s;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_o_given_sp;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_s_given_o;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_s_given_p;
import static de.citec.generator.config.PredictionPatterns.predict_l_for_s_given_po;
import static de.citec.generator.config.PredictionPatterns.predict_localized_l_for_o_given_p;
import static de.citec.generator.config.PredictionPatterns.predict_localized_l_for_o_given_sp;
import static de.citec.generator.config.PredictionPatterns.predict_localized_l_for_s_given_p;
import static de.citec.generator.config.PredictionPatterns.predict_localized_l_for_s_given_po;
import static de.citec.generator.config.PredictionPatterns.predict_o_for_s_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_p_for_o_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_p_for_o_given_localized_l;
import static de.citec.generator.config.PredictionPatterns.predict_p_for_s_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_p_for_s_given_localized_l;
import static de.citec.generator.config.PredictionPatterns.predict_po_for_s_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_po_for_s_given_localized_l;
import static de.citec.generator.config.PredictionPatterns.predict_s_for_o_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_sp_for_o_given_l;
import static de.citec.generator.config.PredictionPatterns.predict_sp_for_o_given_localized_l;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.compress.compressors.CompressorException;
import de.citec.generator.config.PredictionPatterns;

/**
 *
 * @author elahi
 */
public class CsvFile  implements PredictionPatterns {

    private File csvFile = null;
    private BufferedReader bufferedReader=null;
    public String[] qaldHeader = null;
    private Map<String, List<String[]>> wordRows = new TreeMap<String, List<String[]>>();
    private Map<String, Integer> interestingnessIndexes = new HashMap<String, Integer>();
    private List<String[]> rows = new ArrayList<String[]>();


    public CsvFile(File filename) {
        this.csvFile = filename;
        try {
            this.bufferedReader = FileFolderUtils.getBufferedReaderForCompressedFile(csvFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("input file not found::"+ex.getMessage());
        } catch (CompressorException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("the output file needs to be compressed::"+ex.getMessage());
        }

    }

    public List<String[]> getRowsManual() {
        List<String[]> rows = new ArrayList<String[]>();
        Stack<String> stack = new Stack<String>();
        try {
            rows = generateLinebyLine(this.bufferedReader, 300000);
            //System.out.println("rows:::"+rows.size());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("The file is not found!!!" + ex.getMessage());
        }
        catch (CompressorException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println( "The system can read only compressed file!!" + ex.getMessage());
        }
        catch (NullPointerException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println( "The system can read only compressed file!!" + ex.getMessage());
        }
        catch (Exception ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
           System.err.println( "The file reading is failed!!" + ex.getMessage());
        }

        return rows;
    }
    
     

    /*public List<String[]> getRows(File qaldFile) {
        List<String[]> rows = new ArrayList<String[]>();
        System.out.println("file name:"+qaldFile);
    
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            {
                reader = new CSVReader(new FileReader(qaldFile));
                rows = reader.readAll();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("The file is not found!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
               System.err.println("The file is not found!!!" + ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error in csv file!!!" + ex.getMessage());

        }

        return rows;
    }*/

   

  

    private List<String[]> generateLinebyLine(BufferedReader manualReader, Integer lineLimit) throws FileNotFoundException, IOException, Exception {
        List<String[]> rows = new ArrayList<String[]>();
        //BufferedReader manualReader = new BufferedReader(new FileReader(pathToCsv));
        String line = null;
        Integer index = 0;
        while ((line = manualReader.readLine()) != null) {
            line = this.modifyLine(line);
            try {
                if (line.contains(",")) {
                    String[] data = line.split(",");
                    rows.add(data);
                } else {
                    throw new Exception("the line does not contain comma:"  + line);
                }

            } catch (Exception ex) {
                throw new Exception("invalid lin in CSV ::" + line+" "+ex.getMessage());
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
        }
        manualReader.close();
        return rows;
    }

    private String modifyLine(String line) {
        line = line.replace("c_s,ll_s => po", predict_po_for_s_given_l);
        line = line.replace("c_s,l_s => po", predict_po_for_s_given_localized_l);
        line = line.replace("c_s,l_s => p", predict_p_for_s_given_l);
        line = line.replace("c_s,ll_s => p", predict_p_for_s_given_localized_l);
        line = line.replace("c_s,l_s => o", predict_o_for_s_given_l);
        line = line.replace("c_o,l_o => sp", predict_sp_for_o_given_l);
        line = line.replace("c_o,ll_o => sp", predict_sp_for_o_given_localized_l);
        line = line.replace("c_o,l_o => s", predict_s_for_o_given_l);
        line = line.replace("c_o,l_o => p", predict_p_for_o_given_l);
        line = line.replace("c_o,ll_o => p", predict_p_for_o_given_localized_l);
        line = line.replace("c_s,po => l_s", predict_l_for_s_given_po);
        line = line.replace("c_s,po => ll_s", predict_localized_l_for_s_given_po);
        line = line.replace("c_s,p => l_s", predict_l_for_s_given_p);
        line = line.replace("c_s,p => ll_s", predict_localized_l_for_s_given_p);
        line = line.replace("c_s,o => l_s", predict_l_for_s_given_o);
        line = line.replace("c_o,sp => l_o", predict_l_for_o_given_sp);
        line = line.replace("c_o,sp => ll_o", predict_localized_l_for_o_given_sp);
        line = line.replace("c_o,s => l_o", predict_l_for_o_given_s);
        line = line.replace("c_o,p => l_o", predict_l_for_o_given_p);
        line = line.replace("c_o,p => ll_o", predict_localized_l_for_o_given_p);
        return line;
    }
  
    public File getFilename() {
        return csvFile;
    }

    public String[] getQaldHeader() {
        return this.qaldHeader;
    }

    public Map<String, List<String[]>> getRow() {
        return wordRows;
    }


}
