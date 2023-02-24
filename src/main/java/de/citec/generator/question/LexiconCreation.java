/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class LexiconCreation implements InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;
    private List<String> lexiconNames = new ArrayList<String>();


    public LexiconCreation(String inputDir, String pattern, List<Integer> rankThresolds, Integer limit, LexicalEntryHelper lexicalEntryHelperT, String outputDir,String rulePattern,String parameterPattern) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        List<String> files = FileFolderUtils.getSelectedFiles(inputDir, pattern);

        /*Integer index = 0;
        for (String fileName : files) {
            if (fileName.contains("#")) {
                continue;
            }
            CsvFile inputCsvFile = new CsvFile();
            List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
            List<String[]> newRows = new ArrayList<String[]>();

            for (Integer parameter = 0; parameter <= limit;) {
                if (index >= parameter) {
                    break;
                }

                index = index + 1;
            }

        }*/
 /*
         List<String[]> nounRows=createLexicaEntry(fileName,resultList);
         outputCsvFile.writeToCSV(new File(inputDir+parameter+"-"+thresold+".csv"),nounRows);*/
 /*for (String frame : frames) {
            String[] header = new String[]{};
            String dir =null;
            if (frame.contains(NounPPFrame)) {
                header = NounPPFrameBuilder.getHeader();
                dir = NounPPFrameBuilder.getDir();
                FileFolderUtils.delete(new File(dir));

            } else if (frame.contains(TransitiveFrame)) {
                header = TransitiveFrameBuiler.getHeader();
                dir = TransitiveFrameBuiler.getDir();
                FileFolderUtils.delete(new File(dir));

            } else if (frame.contains(InTransitivePPFrame)) {
                header = InTransitivePPFrameBuilder.getHeader();
                dir = InTransitivePPFrameBuilder.getDir();
                FileFolderUtils.delete(new File(dir));

            }*/
        //for (Integer parameter = 0; parameter <= limit;) {
            //parameter += threshold;
            //Integer rankThresold=10;
            
          for (Integer rankThresold : rankThresolds) {
            SyntacticEntries syntacticEntries = new SyntacticEntries(lexicalEntryHelper, rankThresold, rulePattern, parameterPattern);
            for (String fileName : files) {
                if (fileName.contains("#")) {
                    continue;
                }
                CsvFile inputCsvFile = new CsvFile();
                //String reference = this.lexicalEntryHelper.makeReference(fileName);
                List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
                syntacticEntries.split(rows);
                System.out.println(fileName + " " + syntacticEntries.getNounPP().size());

            }
            syntacticEntries.write(outputDir, rulePattern, parameterPattern, rankThresold);
            this.lexiconNames.add(rulePattern + parameterPattern + "-" + rankThresold);
        }


            
            /*CsvFile outputCsvFile = new CsvFile();
                if (!allResults.isEmpty()) {
                    outputCsvFile.writeToCSV(new File(outputDir +dir+ parameter + "-" + thresold + "-" + frame + ".csv"), allResults);
                }*/
        //}
        //}
    }

    public void writeLexiconName(String dir) {
        for (String parameter : lexiconNames) {
            String fileName = dir + parameter + "inputConf_en.json";
            InputCofiguration inputCofiguration = new InputCofiguration(lexiconNames);
            JsonWriter.writeClassToJson(inputCofiguration, dir + "inputConf_en.json");
        }

    }

   

    private String[] gradableCsvRow(String fileName, String linguisticPattern, String value, String frame) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference = lexicalEntryHelper.makeReference(fileName);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        rowNoun[0] = id;
        rowNoun[1] = "adjective";
        rowNoun[2] = linguisticPattern;
        rowNoun[3] = linguisticPattern;
        rowNoun[4] = "of";
        rowNoun[5] = frame;
        rowNoun[6] = "domain";
        rowNoun[7] = "range";
        rowNoun[8] = "1";
        rowNoun[9] = fileName.replace("raw-", "").replace(".csv", "").replace("_", ":");
        rowNoun[10] = "domainIndex";
        rowNoun[11] = "rangeIndex";
        rowNoun[12] = value;
        rowNoun[13] = reference;
        return rowNoun;
    }

}
