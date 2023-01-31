/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import static de.citec.generator.question.InduceConstants.GREP;
import static de.citec.generator.question.InduceConstants.outputDir;
import static de.citec.generator.question.InduceConstants.shHeading;
import de.citec.sc.generator.utils.FileFolderUtils;
import de.citec.sc.generator.utils.UriFilter;
import java.io.File;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class QaldUri {

    public static void qaldUrisExtract(String rulePattern, String rootDir, Map<String, Map<String, String>> frameUrisT) {

        for (String givenFrame : frameUrisT.keySet()) {
            Map<String, String> frameUris = frameUrisT.get(givenFrame);
            String str = shHeading;
            for (String key : frameUris.keySet()) {
                String url = UriFilter.getOriginal(key);
                url = "'" + url + "'";
                if (isExists(outputDir, key)) {
                    continue;
                }
                //System.out.println(key + "key...." + key);

                String outputFile = UriFilter.replaceColon(UriFilter.filter(key));
                String inputFile = outputDir + rulePattern + "*" + ".csv";
                outputFile = outputDir + "raw" + "-" + outputFile + ".csv";
                String grep = GREP + " " + url + " " + inputFile + ">>" + outputFile + "\n";
                str += grep;
                //System.out.println(outputFile);

            }
            //System.out.println(str);
            FileFolderUtils.stringToFiles(str, rootDir + "grep" + givenFrame + ".sh");

        }

    }

    private static boolean isExists(String outputDir, String key) {
        File fileDir = new File(outputDir);
        File[] files = fileDir.listFiles();
        key = key.replace(":", "_");
        for (File file : files) {
            if (file.getName().contains("rules-")) {
                continue;
            }
            if (file.getName().contains(key)) {
                return true;
            }
        }
        return false;
    }

}
