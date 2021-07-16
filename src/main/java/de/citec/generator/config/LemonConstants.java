/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.config;

import static de.citec.sc.lemon.vocabularies.LEXINFO.preposition;

/**
 *
 * @author elahi
 */
public interface LemonConstants {

    public String lexinfo = "http://www.lexinfo.net/ontology/2.0/lexinfo#";
    public String lemon = "http://lemon-model.net/lemon#";
    public String lexinfo_adjective = lexinfo + "/" + "adjective";
    public String lexinfo_verb = lexinfo + "/" + "verb";
    public String lexinfo_noun = lexinfo + "/" + "noun";
    public String AdjectivePredicateFrame = "AdjectivePredicateFrame";
    public String attributiveArg = "attributiveArg";
    public String AttrSynArg = "AttrSynArg";
    public String copulativeSubject = "copulativeSubject";
    public String PredSynArg = "PredSynArg";
    public String TransitiveFrame="TransitiveFrame";
    public String IntransitivePPFrame="IntransitivePPFrame";
    public String  prepositionalAdjunct="prepositionalAdjunct";
    public String object="object";
    public String subject="subject";
    public String subjOfProp="subjOfProp";
    public String objOfProp="objOfProp";
    public String  NounPPFrame="NounPPFrame";
    public String preposition="on";
    public String directObject="directObject";
}
