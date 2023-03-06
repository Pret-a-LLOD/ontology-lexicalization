/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.util.*;

/**
 *
 * @author elahi
 */
public class LexiconCreation implements InduceConstants {

    private LexicalEntryHelper lexicalEntryHelper = null;
    private List<String> lexiconNames = new ArrayList<String>();

    public LexiconCreation(String inputDir, String pattern, List<Integer> rankThresolds, Integer limit, LexicalEntryHelper lexicalEntryHelperT, String outputDir,  String parameterPattern) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelperT;
        List<String> files = FileFolderUtils.getSelectedFiles(inputDir, pattern);

        Integer lexIndex = 0;
        for (Integer rankThresold : rankThresolds) {
            SyntacticEntries syntacticEntries = new SyntacticEntries(lexicalEntryHelper, rankThresold);
            for (String fileName : files) {
                if (fileName.contains("#")) {
                    continue;
                }
                CsvFile inputCsvFile = new CsvFile();
                //String reference = this.lexicalEntryHelper.makeReference(fileName);
                List<String[]> rows = inputCsvFile.getRows(new File(inputDir + fileName));
                lexIndex = syntacticEntries.split(rows, lexIndex);
                System.out.println(fileName + " " + syntacticEntries.getNounPP().size());

            }
            syntacticEntries.write(outputDir, parameterPattern, rankThresold);
            this.lexiconNames.add(parameterPattern + "-" + rankThresold);
            break;
        }

    }

    public void writeLexiconName(String dir, Integer index) {
        for (String parameter : lexiconNames) {
            String fileName = dir + parameter + "inputConf_en.json";
            InputCofiguration inputCofiguration = new InputCofiguration(lexiconNames);
            JsonWriter.writeClassToJson(inputCofiguration, dir + "inputConf_en" + "_" + index + ".json");
        }

    }

    private String[] gradableCsvRow(String fileName, String linguisticPattern, String value, String frame) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference = lexicalEntryHelper.makeReference(index);
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
