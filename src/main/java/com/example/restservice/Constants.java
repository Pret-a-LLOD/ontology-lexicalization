/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.restservice;

/**
 *
 * @author elahi
 */
public interface Constants {

    public static String predict_l_for_s_given_po = "predict_l_for_s_given_po";
    public static String predict_po_for_s_given_l = "predict_po_for_s_given_l";
    public static String predict_localized_l_for_s_given_po = "predict_localized_l_for_s_given_po";
    public static String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l";
    public static String predict_l_for_s_given_p = "predict_l_for_s_given_p";
    public static String predict_p_for_s_given_l = "predict_p_for_s_given_l";
    public static String predict_localized_l_for_s_given_p = "predict_localized_l_for_s_given_p";
    public static String predict_p_for_s_given_localized_l = "predict_p_for_s_given_localized_l";
    public static String predict_l_for_s_given_o = "predict_l_for_s_given_o";
    public static String predict_o_for_s_given_l = "predict_o_for_s_given_l";
    public static String predict_l_for_o_given_sp = "predict_l_for_o_given_sp";
    public static String predict_sp_for_o_given_l = "predict_sp_for_o_given_l";
    public static String predict_localized_l_for_o_given_sp = "predict_localized_l_for_o_given_sp";
    public static String predict_sp_for_o_given_localized_l = "predict_sp_for_o_given_localized_l";
    public static String predict_l_for_o_given_s = "predict_l_for_o_given_s";
    public static String predict_s_for_o_given_l = "predict_s_for_o_given_l";
    public static String predict_l_for_o_given_p = "predict_l_for_o_given_p";
    public static String predict_p_for_o_given_l = "predict_p_for_o_given_l";
    public static String predict_localized_l_for_o_given_p = "predict_localized_l_for_o_given_p";
    public static String predict_p_for_o_given_localized_l = "predict_p_for_o_given_localized_l";
    public static String dummylexicalEntry = "swedish";

    public static String dummyLemon = "@prefix :        <http://localhost:8080/#> .\n"
            + "\n"
            + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
            + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
            + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
            + "\n"
            + "@base            <http://localhost:8080#> .\n"
            + "\n"
            + ":lexicon_en a    lemon:Lexicon ;\n"
            + "  lemon:language \"en\" ;\n"
            + "  lemon:entry    :swedish ;\n"
            + "  lemon:entry    :swedish_res .\n"
            + "\n"
            + ":swedish a             lemon:LexicalEntry ;\n"
            + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
            + "  lemon:canonicalForm  :swedish_lemma ;\n"
            + "  lemon:synBehavior    :swedish_attrFrame, :swedish_predFrame ;\n"
            + "  lemon:sense          :swedish_sense .\n"
            + "\n"
            + ":swedish_lemma lemon:writtenRep \"Swedish\"@en .\n"
            + "\n"
            + ":swedish_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
            + "  lexinfo:copulativeSubject :swedish_PredSynArg .\n"
            + "\n"
            + ":swedish_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
            + "  lexinfo:attributiveArg :swedish_AttrSynArg .\n"
            + "\n"
            + ":swedish_sense a  lemon:LexicalSense ;\n"
            + "  lemon:reference :swedish_res ;\n"
            + "  lemon:isA       :swedish_AttrSynArg, :swedish_PredSynArg .\n"
            + "\n"
            + ":swedish_res a   owl:Restriction ;\n"
            + "  owl:onProperty <http://dbpedia.org/ontology/birthPlace> ;\n"
            + "  owl:hasValue   <http://dbpedia.org/resource/Sweden> .";

    public static String dummylexicalEntry2 = "spanish";

    public static String dummyLemon2 = "@prefix :        <http://localhost:8080/#> .\n"
            + "\n"
            + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
            + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
            + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
            + "\n"
            + "@base            <http://localhost:8080#> .\n"
            + "\n"
            + ":lexicon_en a    lemon:Lexicon ;\n"
            + "  lemon:language \"en\" ;\n"
            + "  lemon:entry    :spanish ;\n"
            + "  lemon:entry    :spanish_res .\n"
            + "\n"
            + ":spanish a             lemon:LexicalEntry ;\n"
            + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
            + "  lemon:canonicalForm  :spanish_lemma ;\n"
            + "  lemon:synBehavior    :spanish_attrFrame, :spanish_predFrame ;\n"
            + "  lemon:sense          :spanish_sense .\n"
            + "\n"
            + ":spanish_lemma lemon:writtenRep \"Spanish\"@en .\n"
            + "\n"
            + ":spanish_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
            + "  lexinfo:copulativeSubject :spanish_PredSynArg .\n"
            + "\n"
            + ":spanish_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
            + "  lexinfo:attributiveArg :spanish_AttrSynArg .\n"
            + "\n"
            + ":spanish_sense a  lemon:LexicalSense ;\n"
            + "  lemon:reference :spanish_res ;\n"
            + "  lemon:isA       :spanish_AttrSynArg, :spanish_PredSynArg .\n"
            + "\n"
            + ":spanish_res a   owl:Restriction ;\n"
            + "  owl:onProperty <http://dbpedia.org/ontology/country> ;\n"
            + "  owl:hasValue   <http://dbpedia.org/resource/Spain> .\n"
            + "";

    public static String dummylexicalEntry3 = "australian";

    public static String dummyLemon3 = "@prefix :        <http://localhost:8080/#> .\n"
            + "\n"
            + "@prefix lexinfo: <http://www.lexinfo.net/ontology/2.0/lexinfo#> .\n"
            + "@prefix lemon:   <http://lemon-model.net/lemon#> .\n"
            + "@prefix owl:     <http://www.w3.org/2002/07/owl#> .\n"
            + "\n"
            + "@base            <http://localhost:8080#> .\n"
            + "\n"
            + ":lexicon_en a    lemon:Lexicon ;\n"
            + "  lemon:language \"en\" ;\n"
            + "  lemon:entry    :belgian ;\n"
            + "  lemon:entry    :belgian_res .\n"
            + "\n"
            + ":belgian a             lemon:LexicalEntry ;\n"
            + "  lexinfo:partOfSpeech lexinfo:adjective ;\n"
            + "  lemon:canonicalForm  :belgian_lemma ;\n"
            + "  lemon:synBehavior    :belgian_attrFrame, :belgian_predFrame ;\n"
            + "  lemon:sense          :belgian_sense .\n"
            + "\n"
            + ":belgian_lemma lemon:writtenRep \"australian\"@en .\n"
            + "\n"
            + ":belgian_predFrame a        lexinfo:AdjectivePredicateFrame ;\n"
            + "  lexinfo:copulativeSubject :belgian_PredSynArg .\n"
            + "\n"
            + ":belgian_attrFrame a     lexinfo:AdjectiveAttributiveFrame ;\n"
            + "  lexinfo:attributiveArg :belgian_AttrSynArg .\n"
            + "\n"
            + ":belgian_sense a  lemon:LexicalSense ;\n"
            + "  lemon:reference :belgian_res ;\n"
            + "  lemon:isA       :belgian_AttrSynArg, :belgian_PredSynArg .\n"
            + "\n"
            + ":belgian_res a   owl:Restriction ;\n"
            + "  owl:onProperty <http://dbpedia.org/ontology/country> ;\n"
            + "  owl:hasValue   <http://dbpedia.org/resource/Australia> .\n"
            + "";

}
