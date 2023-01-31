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
import com.opencsv.exceptions.CsvMalformedLineException;
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
    private static Logger LOGGER = null;

     public CsvFile() {
         
     }
 

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
    
     public void writeToCSV(List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.err.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(this.csvFile))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.err.println("writing csv file failed!!!" + ex.getMessage());
        }
    }

    public void writeToCSV(File newQaldFile, List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.err.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(newQaldFile))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.err.println("writing csv file failed!!!" + ex.getMessage());
            ex.printStackTrace();
        }
    }

   
    public List<String[]> getManualRow(File qaldFile, Double limit, Integer lineLimit) {
        List<String[]> rows = new ArrayList<String[]>();

        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            rows = generateLinebyLine(qaldFile, lineLimit);
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*try {
            
            if (FileFolderUtils.isFileBig(qaldFile, limit)) {
                rows = generateLinebyLine(qaldFile,lineLimit);
                //System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + qaldFile.getName()+" size:"+rows.size());
            } else {
                reader = new CSVReader(new FileReader(qaldFile));
                rows = reader.readAll();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        }  catch (CsvException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV problems:!!!" + ex.getMessage());
        }
         catch (Exception ex) {
            try {
                rows = generateLinebyLine(qaldFile,lineLimit);
            } catch (IOException ex1) {
                Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }*/
        return rows;
    }

    public List<String[]> getRows(File qaldFile) {
        List<String[]> rows = new ArrayList<String[]>();

        /*if (FileFolderUtils.isFileSizeManageable(qaldFile, 40.0)) {
            //System.out.println("..........." + qaldFile.getName());
            return rows;
        }*/
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {

            reader = new CSVReader(new FileReader(qaldFile));
            rows = reader.readAll();

        } catch (CsvMalformedLineException ex) {
            System.out.println(qaldFile.getName()+"::"+ex.getMessage());
            return new ArrayList<String[]>();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (CsvException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rows;
    }
    
     public List<String[]> getRows() {
        List<String[]> rows = new ArrayList<String[]>();

        /*if (FileFolderUtils.isFileSizeManageable(qaldFile, 40.0)) {
            //System.out.println("..........." + qaldFile.getName());
            return rows;
        }*/
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {

            reader = new CSVReader(new FileReader(this.csvFile));
            rows = reader.readAll();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (CsvException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return rows;
    }

     public List<String[]> getRowsManual(File qaldFile) {
        List<String[]> rows = new ArrayList<String[]>();

        /*if (FileFolderUtils.isFileSizeManageable(qaldFile, 40.0)) {
            //System.out.println("..........." + qaldFile.getName());
            return rows;
        }*/
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            //if (!FileFolderUtils.isFileBig(qaldFile, 100.0)) {
                rows = generateLinebyLine(qaldFile,100000);
                 //System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + qaldFile.getName()+" size:"+rows.size());
            /*} else {
                reader = new CSVReader(new FileReader(qaldFile));
                rows = reader.readAll();
            }*/
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } 

        return rows;
    }

    public List<String[]> cvsModifier(File qaldFile) throws Exception {
        List<String[]> modifyrows = new ArrayList<String[]>();
        Map<String, List<String[]>> sort = new TreeMap<String, List<String[]>>();
        List<String[]> rows = getRows(qaldFile);
        String[] header = null;
        Integer j = 0;
        for (String[] row : rows) {
            if (j == 0) {
                header = row;

                j = j + 1;
                continue;
            }

            String key = null;
            String[] newRow = new String[row.length];
            for (Integer index = 0; index < row.length; index++) {
                //String query = " \" " + row[index].replace("$", ",") + " \" ";
                String query = row[index].replace("$", ",");
                if (index == 0) {
                    key = row[index];
                    key = key.toLowerCase();
                    key = key.replace(" ", "_").strip().trim();
                    newRow[index] = query;
                }
                newRow[index] = query;

            }
            List<String[]> list = new ArrayList<String[]>();
            if (sort.containsKey(key)) {
                list = sort.get(key);
            }

            list.add(newRow);
            sort.put(key, list);
        }

        modifyrows.add(header);
        for (String key : sort.keySet()) {
            List<String[]> list = sort.get(key);
            for (String[] row : list) {
                modifyrows.add(row);
            }
        }
        return modifyrows;
    }

  

    private List<String[]> generateLinebyLine(File pathToCsv, Integer lineLimit) throws FileNotFoundException, IOException {
        List<String[]> rows = new ArrayList<String[]>();
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String line = null;
        Integer index = 0;
        while ((line = csvReader.readLine()) != null) {
            try {
                line = line.replace("\"", "");

                String[] data = line.split(",");
                
                rows.add(data);

            } catch (Exception ex) {
                ;
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
            // do something with the data
        }
        csvReader.close();
        return rows;
    }
    
     private Map<String,String[]> generateLinebyLine(File pathToCsv, Integer lineLimit,Integer keyIndex) throws FileNotFoundException, IOException {
        Map<String,String[]> map = new TreeMap<String,String[]>();
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String line = null;
        Integer index = 0;
        while ((line = csvReader.readLine()) != null) {
            try {
                String[] data = line.split(",");
                String key=data[keyIndex];
                map.put(key, data);

            } catch (Exception ex) {
                ;
            }
            index = index + 1;
            if (index > lineLimit) {
                break;
            }
            // do something with the data
        }
        csvReader.close();
        return map;
    }
     
    public Map<String, String[]> generateBindingMapL(Integer keyIndex, Integer classIindex, String givenClassName) throws FileNotFoundException, IOException {
        Map<String, String[]> map = new TreeMap<String, String[]>();
        String line = null;
        Integer index = 0;
        List<String[]> rows = this.getRows(this.csvFile);

        for (String[] data : rows) {
            String key = data[keyIndex];
            String className = data[classIindex];
            if (this.isClassMatched(className, givenClassName)) {
                map.put(key, data);
            }

        }
        return map;
    }


    private boolean isClassMatched(String className, String givenClassName) {
        className = className.toLowerCase().trim().strip().stripLeading().stripTrailing().replace(" ", "_");
        givenClassName = givenClassName.toLowerCase().trim().strip().stripLeading().stripTrailing().replace(" ", "_");
        // System.out.println("givenClassName::"+givenClassName+" bindingClass::"+className);

        if (className.contains(givenClassName)) {
            return true;
        }
        return false;

    }



}
