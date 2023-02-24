/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.InduceConstants.InTransitivePPFrame;
import static de.citec.generator.question.InduceConstants.NounPPFrame;
import static de.citec.generator.question.InduceConstants.TransitiveFrame;
import de.citec.sc.generator.utils.CsvFile;
import de.citec.sc.generator.utils.FileFolderUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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
    private Integer parameter = 0;

    public SyntacticEntries(LexicalEntryHelper lexicalEntryHelper, Integer rankThresold,String rulePattern, String parameterPattern) throws Exception {
        this.lexicalEntryHelper = lexicalEntryHelper;
        this.nounPP.add(NounPPFrameBuilder.getHeader());
        this.inTransitivePP.add(TransitiveFrameBuiler.getHeader());
        this.transitive.add(InTransitivePPFrameBuilder.getHeader());
        this.parameter = rankThresold;
       

    }

    public void split(List<String[]> rows) {
        List<String[]> nounPP = new ArrayList<String[]>();
        List<String[]> transitive = new ArrayList<String[]>();
        List<String[]> inTransitivePP = new ArrayList<String[]>();
        List<String[]> attibutive = new ArrayList<String[]>();
        List<String[]> gradable = new ArrayList<String[]>();
        Integer index = 0;
        for (String[] row : rows) {

            try {
                String doubleValue = row[0];
                String modifiedLinguisticPattern = row[1];
                String frame = row[2];
                String nGram = row[4];
                String reference = row[7];

                if (frame.contains(NounPPFrame)) {
                    NounPPFrameBuilder nounPPFrame = new NounPPFrameBuilder(reference, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
                    if (nounPPFrame.getFlag()) {
                        nounPP.add(nounPPFrame.getRow());
                    }
                } else if (frame.contains(TransitiveFrame)) {
                    TransitiveFrameBuiler transitiveFrame = new TransitiveFrameBuiler(reference, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
                    if (transitiveFrame.getFlag()) {
                        transitive.add(transitiveFrame.getRow());
                    }
                } else if (frame.contains(InTransitivePPFrame)) {
                    InTransitivePPFrameBuilder inTransitiveFrame = new InTransitivePPFrameBuilder(reference, modifiedLinguisticPattern, doubleValue, frame, nGram, index, lexicalEntryHelper);
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

                if (index >= parameter) {
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

    }

    public void write(String outputDir, String rulePattern, String parameterPattern, Integer threshold) {
        CsvFile outputCsvFile = new CsvFile();
        if (this.nounPP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir + NounPPFrameBuilder.getDir() + rulePattern + parameterPattern + parameter + "-" + "NounPPFrame" + ".csv"), this.nounPP);
        }
        if (this.transitive.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir + TransitiveFrameBuiler.getDir() + rulePattern + parameterPattern + parameter + "-" + "TransitiveFrame" + ".csv"), this.transitive);
        }
        if (this.inTransitivePP.size() > 1) {
            outputCsvFile.writeToCSV(new File(outputDir + InTransitivePPFrameBuilder.getDir() + rulePattern + parameterPattern + "-" + parameter + "-" + "InTransitivePPFrame" + ".csv"), this.inTransitivePP);
        }
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

    @Override
    public String toString() {
        return "SyntacticEntries{" + "NounPPFrameResult=" + nounPP + ", TransitiveFrameResult=" + transitive + ", TransitivePPFrameResult=" + inTransitivePP + ", resultAttibutiveFrame=" + attibutive + ", resultGradableFrame=" + gradable + '}';
    }

}
