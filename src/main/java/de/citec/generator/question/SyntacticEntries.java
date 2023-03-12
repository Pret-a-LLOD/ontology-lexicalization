/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import com.opencsv.CSVWriter;
import static de.citec.generator.question.InduceConstants.NounPPFrame;
import static de.citec.generator.question.InduceConstants.TransitiveFrame;
import static de.citec.sc.generator.analyzer.TextAnalyzer.ENGLISH_STOPWORDS_WITHOUT_PREPOSITION;
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
import static de.citec.generator.question.InduceConstants.IntransitivePPFrame;

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
    
     //AB 10: 0.0024330900243309,
    //BA 11: 0.875,5754,
    //supA 12: 16,
    //supB 13: 14,
    //supAB 14: 0.0024330900243309,
    //Al 15: 0.0024263431542461,
    //coher 16: 0.0461405870280119,
    //cos 17: 0.996872828353023,
    //IR 18: 0.438716545012165,
    //Kul 19: 0.875,
    //Max 20: 

    public Integer split(List<String[]> rows, Integer lexIndex) {
        List<String[]> nounPPT = new ArrayList<String[]>();
        List<String[]> transitiveT = new ArrayList<String[]>();
        List<String[]> inTransitivePPT = new ArrayList<String[]>();
        List<String[]> attibutiveT = new ArrayList<String[]>();
        List<String[]> gradableT = new ArrayList<String[]>();
        Integer index = 0;
        Set<String>duplicates=new HashSet<String>();
        for (String[] row : rows) {
            lexIndex=lexIndex+2;
            try {
                String doubleValueString = row[0];
                String modifiedLinguisticPattern = row[1].toLowerCase();
                String frame = row[2];
                String nGram = row[4];
                String reference = row[7];
                String[]values=new String[5];
                
                if(!modifiedLinguisticPattern.contains("music by")){
                   continue; 
                }
                /*String confAB=row[10];
                String confBA=row[11];
                String supA=row[12];
                String supB=row[13];
                String supAB=row[14];
                String[]values=new String[]{confAB,confBA,supA,supB,supAB};*/
               
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
                    NounPPFrameBuilder nounPPFrame = new NounPPFrameBuilder(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, values,lexicalEntryHelper);
                    if (nounPPFrame.getFlag()) {
                        nounPPT.add(nounPPFrame.getObjRow());
                        nounPPT.add(nounPPFrame.getSubjRow());
                        System.out.println(nounPPFrame.getObjRow()[0]+" "+nounPPFrame.getObjRow()[2]);
                         System.out.println(nounPPFrame.getSubjRow()[0]+" "+nounPPFrame.getSubjRow()[2]);
                    }
                } else if (frame.contains(TransitiveFrame)) {
                    TransitiveFrameBuiler transitiveFrame = new TransitiveFrameBuiler(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, values,lexicalEntryHelper);
                    if (transitiveFrame.getFlag()) {
                        transitiveT.add(transitiveFrame.getObjektRow());
                        transitiveT.add(transitiveFrame.getSubjtRow());
                    }
                } else if (frame.equals(IntransitivePPFrame)||frame.equals("InTransitivePPFrame")) {
                    InTransitivePPFrameBuilder inTransitiveFrame = new InTransitivePPFrameBuilder(reference, modifiedLinguisticPattern, doubleValueString, frame, nGram, lexIndex, values,lexicalEntryHelper);
                    if (inTransitiveFrame.getFlag()) {
                        inTransitivePPT.add(inTransitiveFrame.getObjRow());
                        inTransitivePPT.add(inTransitiveFrame.getSubjRow());
                    }
                }
                /*else if (frame.contains(AttibutiveFrame)) {
                String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                resultAttibutiveFrame.add(nounRow);
            } else if (frame.contains(GradableFrame)) {
                String[] nounRow = inTransitivePPCsvRow(fileName, modifiedLinguisticPattern, doubleValue, frame);
                resultGradableFrame.add(nounRow);
            }*/

                
            } catch (Exception ex) {
                System.out.println("lexicon creation failed!!!" + row[0]);
                continue;
            }
           

        }
        if (!nounPPT.isEmpty()) {
            this.nounPP.addAll(nounPPT);

        }
        if (!transitiveT.isEmpty()) {
            this.transitive.addAll(transitiveT);

        }
        if (!inTransitivePPT.isEmpty()) {
            this.inTransitivePP.addAll(inTransitivePPT);

        }
                System.out.println("nounPPT.size()::"+nounPPT.size()+" "+"nounPP.size()::"+nounPP.size());

        return lexIndex;
    }

    public void write(String outputDir,String parameterPattern, Integer rankThresold) {
        // for unknown reason noun folder it gets wrong
        CsvFile outputCsvFile = new CsvFile();
        /*this.nounPP=filterDuplicate(this.nounPP,2);
        this.transitive=filterDuplicate(this.transitive,2);
        this.inTransitivePP=filterDuplicate(this.inTransitivePP,2);*/
        if (this.nounPP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir +parameterPattern+"-"+rankThresold + "-" + NounPPFrame + ".csv"), this.nounPP);

        }
        if (this.transitive.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir + parameterPattern+"-"+rankThresold + "-" + TransitiveFrame + ".csv"), this.transitive);
        }
        if (this.inTransitivePP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir +  parameterPattern+"-"+rankThresold + "-" +  IntransitivePPFrame + ".csv"), this.inTransitivePP);
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
