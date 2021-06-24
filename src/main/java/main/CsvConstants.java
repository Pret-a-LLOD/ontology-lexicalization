/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import com.example.analyzer.TextAnalyzer;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

/**
 *
 * @author elahi
 */
public interface CsvConstants extends TextAnalyzer,NullInterestingness{

    public static String WORD = "word";
    public static String ID = "id";
    public static String PROPERTY = "property";
    public static String OBJECT = "object";
    public static String QUESTION = "question";
    public static String SPARQL = "sparql";

    public static String EXPERIMENT = "experiment";
    public static String EXPERIMENT_RESULT = "experResult";
    public static String POSTAG = "postag";
    public static String INTERESTINGNESS = "interestingness";
    public static String NUMBER_OF_LEXICON = "number of pattern in lexicon";

    public static Integer Wordindex = 0;
    public static Integer idIndex = 1;
    public static Integer propertyIndex = 2;
    public static Integer objectIndex = 3;
    public static Integer sparqlIndex = 4;
    public static Integer questionIndex = 5;

    
    //currently unknown
     public static Integer subjectIndex = -1;
    
    
    public static Integer CosineIndex = 1;
    public static Integer CoherenceIndex = 2;
    public static Integer AllConfIndex = 3;
    public static Integer MaxConfIndex = 4;
    public static Integer IRIndex = 5;
    public static Integer KulczynskiIndex = 6;


    public static String[] qaldHeader = {WORD, ID, OBJECT, PROPERTY, QUESTION, SPARQL};
    
    public static String[] experimentHeader = {EXPERIMENT,
        Cosine + "-" + NOUN, Cosine + "-" + ADJECTIVE, Cosine + "-" + VERB,
        Coherence + "-" + NOUN, Coherence + "-" + ADJECTIVE, Coherence + "-" + VERB,
        AllConf + "-" + NOUN, AllConf + "-" + ADJECTIVE, AllConf + "-" + VERB,
        MaxConf + "-" + NOUN, MaxConf + "-" + ADJECTIVE, MaxConf + "-" + VERB,
        IR + "-" + NOUN, IR + "-" + ADJECTIVE, IR + "-" + VERB,
        Kulczynski + "-" + NOUN, Kulczynski + "-" + ADJECTIVE, Kulczynski + "-" + VERB};
    
    public static Map<String, Integer> interesintnessIndex = Map.ofEntries(
            entry(Cosine, CosineIndex),
            entry(Coherence, CoherenceIndex),
            entry(AllConf, AllConfIndex),
            entry(MaxConf, MaxConfIndex),
            entry(IR, IRIndex),
            entry(Kulczynski, KulczynskiIndex)
    );

    public File getFilename();

    public String[] getQaldHeader();

    public Map<String, List<String[]>> getRow();

}
