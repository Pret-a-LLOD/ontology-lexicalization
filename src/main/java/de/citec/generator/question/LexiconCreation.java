/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class LexiconCreation implements InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;

    public LexiconCreation(String inputDir, String pattern, Integer thresold, Integer limit, LexicalEntryHelper lexicalEntryHelperT, String outputDir) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        List<String> files = FileFolderUtils.getSelectedFiles(inputDir, pattern);
        

        /*
         List<String[]> nounRows=createLexicaEntry(fileName,resultList);
         outputCsvFile.writeToCSV(new File(inputDir+parameter+"-"+thresold+".csv"),nounRows);*/
        for (String frame : frames) {
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

            }

            for (Integer parameter = 0; parameter <= limit;) {
                parameter += thresold;
                List<String[]> allResults = new ArrayList<String[]>();
                allResults.add(header);
                for (String fileName : files) {
                    if (fileName.contains("#")) {
                        continue;
                    }
                    CsvFile inputCsvFile = new CsvFile();
                    List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
                    List<String[]> resultList = this.split(inputDir, fileName, rows, parameter, frame);
                    allResults.addAll(resultList);

                }
                CsvFile outputCsvFile = new CsvFile();
                if (!allResults.isEmpty()) {
                    outputCsvFile.writeToCSV(new File(outputDir +dir+ parameter + "-" + thresold + "-" + frame + ".csv"), allResults);
                }
            }
        }
    }

    private List<String[]> split(String inputDir, String fileName, List<String[]> rows, Integer parameter, String givenFrame) {
        //0.927866938384532,story,NounPPFrame,NN,1-gram,story
        Integer index = 0;
        List<String[]> result = new ArrayList<String[]>();
        for (String[] row : rows) {
            String doubleValue = row[0];
            String modifiedLinguisticPattern = row[1];
            String frame = row[2];
            String nGram = row[4];

            if (frame.contains(givenFrame)) {
                if (givenFrame.contains(NounPPFrame)) {
                    NounPPFrameBuilder nounPPFrame = new NounPPFrameBuilder(fileName, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
                    String[] nounRow = nounPPFrame.getRow();
                    result.add(nounRow);
                } else if (givenFrame.contains(TransitiveFrame)) {
                    TransitiveFrameBuiler transitiveFrame = new TransitiveFrameBuiler(fileName, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
                    String[] nounRow = transitiveFrame.getRow();
                    result.add(nounRow);
                } else if (givenFrame.contains(InTransitivePPFrame)) {
                    InTransitivePPFrameBuilder transitiveFrame = new InTransitivePPFrameBuilder(fileName, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
                    String[] nounRow = transitiveFrame.getRow();
                    result.add(nounRow);
                } else if (givenFrame.contains(AttibutiveFrame)) {
                    String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                    result.add(nounRow);
                } else if (givenFrame.contains(GradableFrame)) {
                    String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                    result.add(nounRow);
                }

            }
            /*if (frame.contains(NounPPFrame)) {
                String[] nounRow = createNounPPFrameCsvRow(fileName, modifiedLinguisticPattern, doubleValue);
                result.add(nounRow);
            }*/

            if (index >= parameter) {
                break;
            }
            index = index + 1;

        }
        return result;

    }

    private String[] transitiveCsvRow(String fileName, String linguisticPattern, String value, String frame) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference = lexicalEntryHelper.makeReference(fileName);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        rowNoun[0] = id;
        rowNoun[1] = "verb";
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

    private String[] inTransitivePPCsvRow(String fileName, String linguisticPattern, String value, String frame) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference = lexicalEntryHelper.makeReference(fileName);
        String id = lexicalEntryHelper.makeLinguistc(linguisticPattern, index) + "-" + reference;
        rowNoun[0] = id;
        rowNoun[1] = "verb";
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

    private String[] attrubuitiveCsvRow(String fileName, String linguisticPattern, String value, String frame) {

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
