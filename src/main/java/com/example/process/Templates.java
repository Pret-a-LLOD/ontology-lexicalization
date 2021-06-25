/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.process;

import java.util.*;

/**
 *
 * @author elahi
 */
public class Templates implements PredictionRules{
    private String resultStr=null;
    
    public Templates(String posTag,String lexicalEntry,String writtenForm){
        if(posTag.equals(posTag)){
            ClassSense classSense=new ClassSense("className","<http://dbpedia.org/ontology/country>","<http://dbpedia.org/resource/Spain>");
            this.resultStr=predict_po_given_l_template(lexicalEntry,writtenForm,classSense);
        }
        
    }
    
    public String predict_po_given_l_template(String lexicalEntry,String writtenForm,ClassSense classSense) {
        return "@prefix :        <http://localhost:8080/#> .\n"
                + "\n"
                + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
                + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
                + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
                + "\n"
                + "@base            <http://localhost:8080#> .\n"
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :"+lexicalEntry+" ;\n"
                + "  lemon:entry    :"+lexicalEntry+"_res .\n"
                + "\n"
                + ":"+lexicalEntry+" a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :"+lexicalEntry+"_lemma ;\n"
                + "  lemon:synBehavior    :"+lexicalEntry+"_attrFrame, :"+lexicalEntry+"_predFrame ;\n"
                + "  lemon:sense          :"+lexicalEntry+"_sense .\n"
                + "\n"
                + ":"+lexicalEntry+"_lemma lemon:writtenRep \""+writtenForm+"\"@en .\n"
                + "\n"
                + ":"+lexicalEntry+"_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                + "  lexinfo:copulativeSubject :"+lexicalEntry+"_PredSynArg .\n"
                + "\n"
                + ":"+lexicalEntry+"_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                + "  lexinfo:attributiveArg :"+lexicalEntry+"_AttrSynArg .\n"
                + "\n"
                + ":"+lexicalEntry+"_sense a  lemon:LexicalSense ;\n"
                + "  lemon:reference :"+classSense.getClassName()+"_res ;\n"
                + "  lemon:isA       :"+lexicalEntry+"_AttrSynArg, :"+lexicalEntry+"_PredSynArg .\n"
                + "\n"
                + ":"+classSense.getClassName()+"_res a   owl:Restriction ;\n"
                + "  owl:onProperty "+classSense.getProperty()+" ;\n"
                + "  owl:hasValue   "+classSense.getValue()+" .\n"
                + "";
    }

    public String getResultStr() {
        return resultStr;
    }
 
    /*private String header = "@prefix :        <http://localhost:8080/#> .\n"
            + "\n"
            + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
            + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
            + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
            + "\n"
            + "@base            <http://localhost:8080#> .\n";

    public String syntactic(String lexicalEntry) {
        return ":" + lexicalEntry + " a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :" + lexicalEntry + "_lemma ;\n"
                + "  lemon:synBehavior    :" + lexicalEntry + "_attrFrame, :" + lexicalEntry + "_predFrame ;\n";
    }

    public String sense(String lexicalEntry, List<ClassSense> classes) {
        String str = syntactic(lexicalEntry);
        String sense = "";

        List<ClassSense> cutClasses = classes.subList(0, classes.size() - 1);
        for (ClassSense classSense : cutClasses) {
            String classString=classSense.getClassName();
            sense = "  lemon:sense          :" + lexicalEntry + "_class_" + classString + " ;\n";
            str += sense;
        }
        sense = "  lemon:sense          :" + lexicalEntry + "_class_" + classes.get(classes.size() - 1) + " .\n";
        str += sense;
        return str;
    }

    public String syntactic2(String lexicalEntry) {
         return ":"+lexicalEntry+"_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
                + "  lexinfo:copulativeSubject :"+lexicalEntry+"_PredSynArg .\n"
                + "\n"
                + ":"+lexicalEntry+"_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
                + "  lexinfo:attributiveArg :"+lexicalEntry+"_AttrSynArg .\n"
                + "\n"
                + ":"+german+"_class_AdministrativeRegion a  lemon:LexicalSense ;\n"
                + "  lemon:reference :"+lexicalEntry+"_AdministrativeRegion ;\n"
                + "  lemon:isA       :"+lexicalEntry+"_AttrSynArg, :"+lexicalEntry+"_PredSynArg .\n";
    }

     
    public String predict_po_for_s_given_l_sense(String lexicalEntry, List<String> classes) {
        String first = ":" + lexicalEntry + " a             lemon:LexicalEntry ;\n"
                + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
                + "  lemon:canonicalForm  :" + lexicalEntry + "_lemma ;\n"
                + "  lemon:synBehavior    :" + lexicalEntry + "_attrFrame, :"+lexicalEntry+"_predFrame ;\n";

        String str = first;
        String sense = "";

        List<String> subStrings = classes.subList(0, classes.size() - 1);
        for (String classString : subStrings) {
            sense = "  lemon:sense          :" + lexicalEntry + "_class_" + classString + " ;\n";
            str += sense;
        }
        sense = "  lemon:sense          :" + lexicalEntry + "_class_" + classes.get(classes.size() - 1) + " .\n";
        str += sense;
        return str;

    }

    public String predict_po_for_s_given_l(String lexicalEntry,String writtenForm,List<String> classes) {
        return  header
                + "\n"
                + ":lexicon_en a    lemon:Lexicon ;\n"
                + "  lemon:language \"en\" ;\n"
                + "  lemon:entry    :" + lexicalEntry + " ;\n"
                + "  lemon:entry    :" + lexicalEntry + "_res .\n"
                + "\n"
                +  syntactic(lexicalEntry)
                +  sense(lexicalEntry,classes)
                + "\n"
                + ":"+lexicalEntry+"_lemma lemon:writtenRep \""+writtenForm+"\"@en .\n"
                + "\n"
                + syntactic2(lexicalEntry)
                + "\n"
                + ontology(lexicalEntry,)
                + "\n";
        
        
                + ":german_AdministrativeRegion a   owl:Restriction ;\n"
                + "  owl:onProperty <http://dbpedia.org/ontology/subdivision> ;\n"
                + "  owl:hasValue   <http://dbpedia.org/resource/Vienna> .\n"
                + "\n"
                + ":german_Airport a   owl:Restriction ;\n"
                + "  owl:onProperty <http://dbpedia.org/ontology/operator> ;\n"
                + "  owl:hasValue   <http://dbpedia.org/resource/German_Army> .\n"
                + "\n"
                + ":german_RugbyClub a   owl:Restriction ;\n"
                + "  owl:onProperty <http://dbpedia.org/ontology/location> ;\n"
                + "  owl:hasValue   <http://dbpedia.org/resource/Germany> .\n"
                + "\n"
                + ":german_PublicTransitSystem a   owl:Restriction ;\n"
                + "  owl:onProperty <http://dbpedia.org/property/transitType> ;\n"
                + "  owl:hasValue   <http://dbpedia.org/resource/S-Bahn> .\n"
                + "";
                
      
    }*/

  

}
