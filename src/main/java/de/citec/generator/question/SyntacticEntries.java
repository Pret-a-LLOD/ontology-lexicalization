/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.opencsv.CSVWriter;
import static de.citec.generator.question.InduceConstants.InTransitivePPFrame;
import static de.citec.generator.question.InduceConstants.NounPPFrame;
import static de.citec.generator.question.InduceConstants.TransitiveFrame;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author elahi
 */
public class SyntacticEntries {

    private List<String[]> nounPP = new ArrayList<String[]>();
    private List<String[]> inTransitivePP = new ArrayList<String[]>();
    private List<String[]> transitive = new ArrayList<String[]>();
    private List<String[]> attibutive = new ArrayList<String[]>();
    private List<String[]> gradable = new ArrayList<String[]>();
    private LexicalEntryHelper lexicalEntryHelper = null;
    private String parameterString = null;
    private Integer rankThresold=0;

    public SyntacticEntries(LexicalEntryHelper lexicalEntryHelper, String parameterPattern,Integer rankThresold) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelper;
        this.nounPP.add(NounPPFrameBuilder.getHeader());
        this.inTransitivePP.add(TransitiveFrameBuiler.getHeader());
        this.transitive.add(InTransitivePPFrameBuilder.getHeader());
        this.parameterString = parameterPattern+"-"+rankThresold;
        this.rankThresold=rankThresold;
       

    }

    public Integer split(List<String[]> rows, Integer lexIndex) {
        List<String[]> nounPP = new ArrayList<String[]>();
        List<String[]> transitive = new ArrayList<String[]>();
        List<String[]> inTransitivePP = new ArrayList<String[]>();
        List<String[]> attibutive = new ArrayList<String[]>();
        List<String[]> gradable = new ArrayList<String[]>();
        Integer index = 0;
        Set<String>duplicates=new HashSet<String>();
        for (String[] row : rows) {
            lexIndex = lexIndex + 1;
            try {
                String doubleValueString = row[0];
                String modifiedLinguisticPattern = row[1];
                String frame = row[2];
                String nGram = row[4];
                String reference = row[7];
                /*Double doubleValue=Conversion.stringToDouble(doubleValueString);
                DecimalFormat df = new DecimalFormat("#.00000");
                String doubleValueStr2=df.format(doubleValue);
                String repeatID=modifiedLinguisticPattern+"-"+doubleValueStr2;

                if(duplicates.contains(repeatID)){
                   continue; 
                }
                else
                    duplicates.add(repeatID);*/
                

                if (frame.contains(NounPPFrame)) {
                    NounPPFrameBuilder nounPPFrame = new NounPPFrameBuilder(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, lexicalEntryHelper);
                    if (nounPPFrame.getFlag()) {
                        nounPP.add(nounPPFrame.getRow());
                    }
                } else if (frame.contains(TransitiveFrame)) {
                    TransitiveFrameBuiler transitiveFrame = new TransitiveFrameBuiler(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, lexicalEntryHelper);
                    if (transitiveFrame.getFlag()) {
                        transitive.add(transitiveFrame.getRow());
                    }
                } else if (frame.contains(InTransitivePPFrame)) {
                    InTransitivePPFrameBuilder inTransitiveFrame = new InTransitivePPFrameBuilder(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, lexicalEntryHelper);
                    if (inTransitiveFrame.getFlag()) {
                        inTransitivePP.add(inTransitiveFrame.getRow());
                    }
                }
                /*else if (frame.contains(AttibutiveFrame)) {
                String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                resultAttibutiveFrame.add(nounRow);
            } else if (frame.contains(GradableFrame)) {
                String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                resultGradableFrame.add(nounRow);
            }*/

                if (index >= this.rankThresold) {
                    break;
                }
                index = index + 1;
            } catch (Exception ex) {
                System.out.println("lexicon creation failed!!!" + row[0]);
                continue;
            }
           

        }
        if (!nounPP.isEmpty()) {
            this.nounPP.addAll(nounPP);

        }
        if (!transitive.isEmpty()) {
            this.transitive.addAll(transitive);

        }
        if (!inTransitivePP.isEmpty()) {
            this.inTransitivePP.addAll(inTransitivePP);

        }
        return lexIndex;
    }

    public void write(String outputDir,String parameterPattern, Integer rankThresold) {
        // for unknown reason noun folder it gets wrong
        CsvFile outputCsvFile = new CsvFile();
        this.nounPP=filterDuplicate(this.nounPP,2);
        this.transitive=filterDuplicate(this.transitive,2);
        this.inTransitivePP=filterDuplicate(this.inTransitivePP,2);
        if (this.nounPP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir +parameterPattern+"-"+rankThresold + "-" + "NounPPFrame" + ".csv"), this.nounPP);

        }
        if (this.transitive.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir + parameterPattern+"-"+rankThresold + "-" + "TransitiveFrame" + ".csv"), this.transitive);
        }
        if (this.inTransitivePP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir +  parameterPattern+"-"+rankThresold + "-" +  "InTransitivePPFrame" + ".csv"), this.inTransitivePP);
        }
    }
 
    public List<String[]> filterDuplicate(List<String[]> csvData, Integer index) {
        if (csvData.isEmpty()) {
            System.err.println("writing csv file failed!!!");
            return new ArrayList<String[]>();
        }
        List<String[]> csvNewData = new ArrayList<String[]>();
        Set<String> lexEntries = new HashSet<String>();

        for (String[] row : csvData) {
            String lexEntry = row[index];
            if (lexEntries.contains(lexEntry)) {
                continue;
            } else {
                lexEntries.add(lexEntry);
                csvNewData.add(row);
            }

        }
        return csvNewData;
    }

    public List<String[]> getNounPP() {
        return nounPP;
    }

    public List<String[]> getInTransitivePP() {
        return inTransitivePP;
    }

    public List<String[]> getTransitive() {
        return transitive;
    }

    public List<String[]> getAttibutive() {
        return attibutive;
    }

    public List<String[]> getGradable() {
        return gradable;
    }

    public LexicalEntryHelper getLexicalEntryHelper() {
        return lexicalEntryHelper;
    }

    public String getParameterString() {
        return parameterString;
    }

    public Integer getRankThresold() {
        return rankThresold;
    }

    @Override
    public String toString() {
        return "SyntacticEntries{" + "NounPPFrameResult=" + nounPP + ", TransitiveFrameResult=" + transitive + ", TransitivePPFrameResult=" + inTransitivePP + ", resultAttibutiveFrame=" + attibutive + ", resultGradableFrame=" + gradable + '}';
    }

   

}
