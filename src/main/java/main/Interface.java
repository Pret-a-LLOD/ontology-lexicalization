/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import main.*;
import analyzer.PosAnalyzer;
import static analyzer.TextAnalyzer.POS_TAGGER_WORDS;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class Interface {

    //public static String outputDir = "../data/";
    public static String outputDir = "/var/www/html/ontologyLexicalization/data/";
    //public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    public static String javaScriptDir = "/home/melahi/";
    //public static String javaScriptDir ="../";
    //public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    public static String resourceDir = "/var/www/html/ontologyLexicalization/resources/";
    public static String configDir = resourceDir + "conf/";

    //public static String configDir = "src/main/resources/config/";
    public static String configFileName = "prefix.prop";
    public static String rulesFileName = "rules.prop";
    
    public static String javaScriptFileName = "table.js";

   
    private Map<String, String> prefixes = new TreeMap<String, String>();
    private Map<String, String> pattern_rules = new TreeMap<String, String>();

    /*private static String createTable(List<String> rows, String prediction,String interestingness) {
         String kbTitle=getKB(prediction); 
         String tableStr = "$(document).ready(function() {\n"
                + "    $('#example').DataTable( {\n"
                + "        data: dataSet,\n"
                + "        columns: [\n"
                + "            { title: \""+kbTitle+"\" },\n"
                + "            { title: \""+"Class"+"\" },\n"
                + "            { title: \""+interestingness+"\" },\n"
                + "            { title: \""+"condAB"+".\" },\n"
                + "            { title: \""+"condBA"+"\" },\n"
                + "            { title: \""+"supA"+"\" },\n"
                + "            { title: \""+"supB"+"\" },\n"
                + "            { title: \""+"PosTag"+"\" },\n"
                + "            { title: \""+"rule"+"\" }\n"
                + "        ]\n"
                + "    } );\n"
                + "} );";
        String start = "var dataSet = [";
        String end = "];";
        String str = "";
        for (Integer index = 0; index < rows.size(); index++) {
            String row = rows.get(index);
            row = row.replace("+", "\"" + ", " + "\"");
            String line = null;
            if (index == (rows.size() - 1)) {
                line = "[" + "\"" + row + "\"" + "]" + "\n";
            } else {
                line = "[" + "\"" + row + "\"" + "]," + "\n";
            }

            str += line;

        }
        str=start + str + end+"\n"+tableStr;
        
        return str;
       
    }*/
    private static String getKB(String prediction) {
        if (prediction.contains(PredictionRules.predict_po_for_s_given_localized_l)) {
            return "predicate object pair";
        }
        return "kb";
    }

    /* public static void main(String[] args) throws Exception {
        String term="australian";
        PosAnalyzer analyzer = new PosAnalyzer(term, POS_TAGGER_WORDS, 5);
        LineInfo LineInfo = new LineInfo(analyzer,term);
        System.out.println(LineInfo.getWord());
         System.out.println(LineInfo.getPosTag());
    }*/
    public static void main(String str[]) throws Exception {
        Logger LOGGER = Logger.getLogger(Interface.class.getName());
        String prediction = "predict_po_for_s_given_localized_l", interestingness = "Cosine", lexicalElement = "bear", parts_of_speech = "JJ";
        String stringAdd = "";
        Boolean flag = false;

        prediction = str[0]+"-";
        interestingness = str[1];
        lexicalElement = str[2];

        Map<String, String> prefixes = FileUtils.getHash(configDir + configFileName,"=");
        Map<String, String> pattern_rules = FileUtils.getHash(configDir + rulesFileName,"=>","=");
    
        PosAnalyzer analyzer = new PosAnalyzer(lexicalElement, POS_TAGGER_WORDS, 5);
        CheckPosTag checkPosTag = new CheckPosTag(analyzer, lexicalElement);
        if (checkPosTag.getFound()) {
            parts_of_speech = checkPosTag.getPosTag();

        } else {
            System.out.println("0");
            return;
        }

        /*checkPosTag.findRulePattern(pattern_rules, str[0]);
        if (checkPosTag.getFound()) {
            prediction = checkPosTag.getPrediction();
            System.out.println("prediction:"+prediction);
        } else {

            System.out.println("0");
            return;
        }*/


        //System.out.println(pattern_rules.values());


        //System.out.println("adjective:" + adjective.toString());
        //System.out.println("noun:" + noun.toString());
        //System.out.println("verb:" + verb.toString());
          
        //parts_of_speech=checkPosTag.getPosTag();
        //System.out.println(checkPosTag.getPosTag());

       
        //System.out.println(parts_of_speech);
        //System.out.println(prediction + " " + interestingness + " " + lexicalElement + " " + parts_of_speech);
        if (str.length < 3) {
            throw new Exception("less number of argument!!!");
        } else {
            lexicalElement = " \"" + lexicalElement + "\" ";
            if (lexicalElement != null) {
                Result result = new Result();
                result.resultStr(outputDir, lexicalElement, parts_of_speech, prediction, interestingness, prefixes);
                List<String> rows = result.getRows();
                System.out.println(result.getContent());

            }

        }
    }
    
}
