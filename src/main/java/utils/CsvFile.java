/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

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

/**
 *
 * @author elahi
 */
public class CsvFile implements CsvConstants {

    private File filename = null;
    public String[] qaldHeader = null;
    private Map<String, List<String[]>> wordRows = new TreeMap<String, List<String[]>>();
    private Map<String, Integer> interestingnessIndexes = new HashMap<String, Integer>();

    private List<String[]> rows = new ArrayList<String[]>();
    private static Logger LOGGER = null;

    public CsvFile(File filename, Logger LOGGER) {
        this.filename = filename;
        this.LOGGER = LOGGER;
    }

    public CsvFile(File filename) {
        this.filename = filename;

    }

    public void writeToCSV(List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.out.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(this.filename))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.out.println("writing csv file failed!!!" + ex.getMessage());
        }
    }

    public void writeToCSV(File newQaldFile, List<String[]> csvData) {
        if (csvData.isEmpty()) {
            System.out.println("writing csv file failed!!!");
            return;
        }
        try ( CSVWriter writer = new CSVWriter(new FileWriter(newQaldFile))) {
            writer.writeAll(csvData);
        } catch (IOException ex) {
            System.out.println("writing csv file failed!!!" + ex.getMessage());
        }
    }

    public File getFilename() {
        return filename;
    }

    public String[] getQaldHeader() {
        return this.qaldHeader;
    }

    public Map<String, List<String[]>> getRow() {
        return wordRows;
    }

    public String getExperiment(String experiment, String interestingness) {
        String[] info = experiment.split("-");
        String str = null;
        str = experiment.replace(interestingness + "-", "");
        str = str.replace("-" + interestingness, ">");
        return str.substring(0, str.indexOf(">"));
    }

    public static String getInterestingnessThresold(String experiment, String interestingness) {
        String[] info = experiment.split("-");
        String str = null;
        for (Integer index = 0; info.length > index; index++) {
            str = info[index];
        }

        return str;
    }

    private String getColumnKey(String interestingness, Double thresoldValue, String posTag, String hitStr) {
        return interestingness + "_" + thresoldValue.toString() + "-" + posTag + "-" + hitStr;
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
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + qaldFile.getName()+" size:"+rows.size());
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
            System.out.println("..........." + qaldFile.getName());
            return rows;
        }*/
        Stack<String> stack = new Stack<String>();
        CSVReader reader;
        try {
            /*if (!FileFolderUtils.isFileBig(qaldFile, 100.0)) {
                rows = generateLinebyLine(qaldFile);
                 System.out.println("@@@@@@@@@@@@@@@@@@@@@@" + qaldFile.getName()+" size:"+rows.size());
            } else*/ {
                reader = new CSVReader(new FileReader(qaldFile));
                rows = reader.readAll();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV File not found:!!!" + ex.getMessage());
        } catch (CsvException ex) {
            Logger.getLogger(CsvFile.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.log(Level.SEVERE, "CSV problems:!!!" + ex.getMessage());
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

}
