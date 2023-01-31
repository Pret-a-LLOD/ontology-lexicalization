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

/**
 *
 * @author elahi
 */
public class LexiconCreation implements InduceConstants{

    public LexiconCreation(String inputDir, String pattern, Integer thresold, Integer limit) {
        List<String> files = FileFolderUtils.getSelectedFiles(inputDir, pattern);
        String[] header = new String[]{"lemon", "partOfSpeech", "writtenForm(singular)", "writtenForm(plural)", "preposition", "SyntacticFrame", "copulativeArg", "prepositionalAdjunct", "sense", "reference", "domain", "range", "value", "filename"};

        /*
         List<String[]> nounRows=createLexicaEntry(fileName,resultList);
         outputCsvFile.writeToCSV(new File(inputDir+parameter+"-"+thresold+".csv"),nounRows);*/
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
                List<String[]> resultList = this.split(inputDir, fileName, rows, parameter);
                allResults.addAll(resultList);
            }
            CsvFile outputCsvFile = new CsvFile();
            outputCsvFile.writeToCSV(new File(inputDir + parameter + "-" + thresold + ".csv"), allResults);
        }

    }

    private List<String[]> split(String inputDir, String fileName, List<String[]> rows, Integer parameter) {

        Integer index = 0;
        List<String[]> result = new ArrayList<String[]>();
        for (String[] row : rows) {
            String value = row[0];
            String linguisticPattern = row[1];
            if(isNoun(linguisticPattern)){
                
            }
            String[] nounRow = createRowNoun(fileName, linguisticPattern, value);
            result.add(nounRow);
            if (index >= parameter) {
                break;
            }
            index = index + 1;

        }
        return result;

    }

    private String[] createRowNoun(String fileName, String linguisticPattern, String value) {

        Integer index = 1;
        List<String[]> nounResult = new ArrayList<String[]>();
        String[] rowNoun = new String[14];
        String reference=this.makeReference(fileName);
        String id=this.makeLinguistc(linguisticPattern,index)+"-"+reference;
        rowNoun[0] = id;
        rowNoun[1] = "noun";
        rowNoun[2] = linguisticPattern;
        rowNoun[3] = linguisticPattern;
        rowNoun[4] = "of";
        rowNoun[5] = NounPPFrame;
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

    private boolean isNoun(String linguisticPattern) {
        return true;
    }

    private String makeLinguistc(String linguisticPattern,Integer index) {
        linguisticPattern=linguisticPattern.replace(" ","_");
        linguisticPattern= linguisticPattern + "-" + index.toString();
        return linguisticPattern;
    }
    
     private String makeReference(String reference) {
        reference=reference.replace("raw-","");
        reference=reference.replace(".csv","");
        return reference;
    }

}
