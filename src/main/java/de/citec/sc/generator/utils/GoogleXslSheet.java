/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

import de.citec.generator.core.LineInfo;
import de.citec.sc.generator.analyzer.TextAnalyzer;
import static de.citec.sc.generator.analyzer.TextAnalyzer.adjective;
import static de.citec.sc.generator.analyzer.TextAnalyzer.noun;

/**
 *
 * @author elahi
 */
public class GoogleXslSheet implements TextAnalyzer {

    public static Integer lemonEntryIndex = 0;
    public static Integer partOfSpeechIndex = 1;
    public static Integer writtenFormInfinitive = 2;
    public static Integer NounPPFrameSyntacticFrameIndex = 5;
    public static Integer TransitFrameSyntacticFrameIndex = 5;
    public static Integer InTransitFrameSyntacticFrameIndex = 6;
    public static String NounPPFrameStr = "NounPPFrame";
    public static String TransitiveFrameStr = "TransitiveFrame";
    public static String IntransitivePPFrameStr = "InTransitiveFrame";
    public static String AttributiveAdjectiveFrameStr = "AdjectiveFrame";
    public static String owl_Restriction = "owl:Restriction";
    public static String domain = "domain";
    public static String range = "range";
    public static String PredSynArg = "PredSynArg";
    public static String AttrSynArg = "AttrSynArg";
    
    private static String getLine(String[] row) {
        String str = "";
        for (Integer index = 0; index < row.length; index++) {
            String line = null;
            if (index == (row.length - 1)) {
                line = row[index];
            } else {
                line = row[index] + ",";
            }
            str += line;
        }
        return str;
    }

    public static class NounPPFrame {
        //LemonEntry	   partOfSpeech	writtenForm(singular)	writtenForm (plural)	preposition	SyntacticFrame	copulativeArg	prepositionalAdjunct	sense	reference	domain	range	GrammarRule1:question1	SPARQL	GrammarRule1: question2	SPARQL Question 2	GrammarRule 1: questions	SPARQL 	NP (Grammar Rule 2)		grammar rules	numberOfQuestions
        //birthPlace_of	   noun	        birth place	         -	                 of	        NounPPFrame	range	         domain	                1	dbo:birthPlace	dbo:Person	dbo:Place	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?	#NAME?		2	

        public static Integer writtenFormPluralIndex = 3;
        public static Integer prepositionIndex = 4;
        public static Integer syntacticFrameIndex = 5;
        public static Integer copulativeArgIndex = 6;
        public static Integer prepositionalAdjunctIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static String csvFileName = NounPPFrameStr + ".csv";


        public static String getRow(String id, String writtenForm, Integer rank, LineInfo lineInfo) {
            String[] row = new String[rangeIndex + 1];
            String predicate = lineInfo.getPredicateOriginal();
            String object = lineInfo.getObjectOriginal();
            row[lemonEntryIndex] = id;
            row[partOfSpeechIndex] = noun;
            row[writtenFormInfinitive] = writtenForm;
            row[writtenFormPluralIndex] = writtenForm;
            row[prepositionIndex] = writtenForm;
            row[syntacticFrameIndex] = NounPPFrameStr;
            row[copulativeArgIndex] = range;
            row[prepositionalAdjunctIndex] = domain;
            row[senseIndex] = rank.toString();
            row[referenceIndex] = predicate;
            row[domainIndex] = DomainAndRange.getDomain(predicate);
            row[rangeIndex] = DomainAndRange.getRange(predicate);
            return getLine(row);
        }

    }

    public static class TransitFrame {
        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	SyntacticFrame	subject	directObject	sense	reference	domain	range	GrammarRule 1:question1	GrammarRule 1:question2	GrammarRule 1:sparql	GrammarRule 2:question1	GrammarRule2: sparql
        //compose	        verb	compose	composes	composed	TransitiveFrame	range	domain	1	dbo:musicComposer	dbo:Work	dbo:Person	Which dbo:Person (X) composes,composed Y(dbo:Work)?	Who dbo:Person (X) composes,composed Y(dbo:Work)?	SELECT ?X WHERE { Y dbo:musicComposer ?X.}	Which dbo:Work(X) was composed by Y(dbo:Person)?	SELECT ?X WHERE { ?X dbo:Person Y.}

        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer syntacticIndex = 5;
        public static Integer subjectIndex = 6;
        public static Integer directObjectIndex = 7;
        public static Integer senseIndex = 8;
        public static Integer referenceIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        
        public static String getRow(String id, String writtenForm, Integer rank, LineInfo lineInfo) {
            String[] row = new String[rangeIndex + 1];
            String predicate = lineInfo.getPredicateOriginal();
            String object = lineInfo.getObjectOriginal();
            row[lemonEntryIndex] = id;
            row[writtenFormInfinitive] = writtenForm;
            row[partOfSpeechIndex] = noun;
            row[writtenForm3rdPerson] = writtenForm;
            row[writtenFormPast] = writtenForm;
            row[syntacticIndex] = NounPPFrameStr;
            row[subjectIndex] = domain;
            row[directObjectIndex] = range;
            row[senseIndex] = rank.toString();
            row[referenceIndex] = predicate;
            row[domainIndex] = DomainAndRange.getDomain(predicate);
            row[rangeIndex] = DomainAndRange.getRange(predicate);
            return getLine(row);
        }


    }

    public static class InTransitFrame {

        //LemonEntry	partOfSpeech	writtenFormInfinitive/2ndPerson	writtenForm3rdPerson	writtenFormPast	preposition	SyntacticFrame	subject	prepositionalAdjunct	sense	reference	domain	range	GrammarRule 1 :question 1	GrammarRule 1 :question 2	GrammarRule 1 :sparql	Grammar rule 2: question1	Grammar rule 2: question2	sparql2
        //flow_through	verb	flow	flows	flowed	through	IntransitivePPFrame	domain	range	1	dbo:country	dbo:River	dbo:Country	What dbo:River(X) flows through Y(dbo:Country)?	FALSE	SELECT ?X WHERE { ?X dbo:country Y.}	Which dbo:Country(X) does Y(dbo:River) flow through?	FALSE	SELECT ?X WHERE { Y dbo:country ?X.}
        public static Integer writtenForm3rdPerson = 3;
        public static Integer writtenFormPast = 4;
        public static Integer preposition = 5;
        public static Integer SyntacticFrame = 6;
        public static Integer subject = 7;
        public static Integer prepositionalAdjunct = 8;
        public static Integer senseIndex = 9;
        public static Integer referenceIndex = 10;
        public static Integer domainIndex = 11;
        public static Integer rangeIndex = 12;
        public static String csvFileName = IntransitivePPFrameStr + ".csv";

        public static String getRow(String id, String writtenForm, Integer rank, LineInfo lineInfo) {
            String[] row = new String[rangeIndex + 1];
            String predicate = lineInfo.getPredicateOriginal();
            String object = lineInfo.getObjectOriginal();
            Integer senseNo=rank+1;
            row[lemonEntryIndex] = id;
            row[partOfSpeechIndex] = verb;
            row[writtenFormInfinitive] = writtenForm;
            row[writtenForm3rdPerson] = writtenForm;
            row[writtenFormPast] = writtenForm;
            row[preposition] = lineInfo.getPreposition();
            row[SyntacticFrame] = IntransitivePPFrameStr;
            row[subject] = domain;
            row[prepositionalAdjunct] = range;
            row[senseIndex] = senseNo.toString();
            row[referenceIndex] = predicate;
            row[domainIndex] = DomainAndRange.getDomain(predicate);
            row[rangeIndex] = DomainAndRange.getRange(predicate);
            return getLine(row);
        }

        public static String getCsvFileName() {
            return csvFileName;
        }

    }

    public static class AttributiveAdjectiveFrame {
//LemonEntry	partOfSpeech	writtenForm	SyntacticFrame	               copulativeSubject	attributiveArg	sense	reference	owl:onProperty	owl:hasValue	domain	range	question (attributive use)	sparql1	question2 (predicative use)	sparql2								
//algerian	adjective	Algerian	AdjectiveAttributiveFrame	PredSynArg	        AttrSynArg	1	owl:Restriction	dbo:nationality	res:Algeria	dbo:Person	dbo:Artist	Give me Algerian (dbo:nationality res:Algeria)(dbo:Artist,X).	SELECT ?X WHERE { X dbo:nationality res:Algeria ;  rdf:type dbo:Person}	which (dbo:Artist,X) is Algerian (dbo:nationality res:Algeria)?									        public static Integer writtenForm3rdPerson = 3;

        public static Integer SyntacticFrame = 3;
        public static Integer copulativeSubjectIndex = 4;
        public static Integer attributiveArgIndex = 5;
        public static Integer senseIndex = 6;
        public static Integer referenceIndex = 7;
        public static Integer owl_onPropertyIndex = 8;
        public static Integer owl_hasValueIndex = 9;
        public static Integer domainIndex = 10;
        public static Integer rangeIndex = 11;
        public static String csvFileName = AttributiveAdjectiveFrameStr + ".csv";

        public static String getRow(String id, String writtenForm, Integer rank, LineInfo lineInfo) {
            String[] row = new String[rangeIndex + 1];
            String predicate = lineInfo.getPredicateOriginal();
            String object = lineInfo.getObjectOriginal();
            row[lemonEntryIndex] = id;
            row[writtenFormInfinitive] = writtenForm;
            row[partOfSpeechIndex] = adjective;
            row[SyntacticFrame] = AttributiveAdjectiveFrameStr;
            row[copulativeSubjectIndex] = PredSynArg;
            row[attributiveArgIndex] = AttrSynArg;
            row[senseIndex] = rank.toString();
            row[owl_onPropertyIndex] = predicate;
            row[owl_hasValueIndex] = object;
            row[referenceIndex] = owl_Restriction;
            row[domainIndex] = DomainAndRange.getDomain(predicate);
            row[rangeIndex] = DomainAndRange.getRange(predicate);
            return getLine(row);
        }

      

        public static String getCsvFileName() {
            return csvFileName;
        }

        /*private static String modify(String kb) {
            if (kb.contains("http://dbpedia.org/ontology/")) {
                return "dbo:";
            } else if (kb.contains("http://dbpedia.org/ontology/")) {
                return "dbp:";
            } else {
                return kb;
            }
        }*/

    }

    public static class DomainAndRange {

        public static String getDomain(String predicateOriginal) {
            return "http://dbpedia.org/ontology/Person";
        }

        public static String getRange(String predicateOriginal) {
            return "https://www.w3.org/2001/XMLSchema#date";
        }

    }

}
