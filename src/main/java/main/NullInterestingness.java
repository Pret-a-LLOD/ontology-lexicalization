/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 *
 * @author elahi
 */
public interface NullInterestingness {

    public static final String supA = "supA";
    public static final String supB = "supB";
    public static final String supAB = "supAB";
    public static final String condAB = "condAB";
    public static final String condBA = "condBA";
    public static final String numRule = "numRule";
    public static final String nGram = "nGram";

    public static final String AllConf = "AllConf";
    public static final String MaxConf = "MaxConf";
    public static final String IR = "IR";
    public static final String Kulczynski = "Kulczynski";
    public static final String Cosine = "Cosine";
    public static final String Coherence = "Coherence";

    public static final String LEXICON = "lexicon";
    public static final String QALD = "qald";

    public static final LinkedHashSet<String> interestingness = new LinkedHashSet(new ArrayList<String>(Arrays.asList(Cosine, Coherence, AllConf, MaxConf, IR, Kulczynski)));

    public static final String linguisticRule = "linguisticRule";
    public static final String kbRule = "linguisticRule";

    public static final String PREDICATE = "predicate";

    public static final String OBJECT = "object";
    public static CharSequence Interestingness="Interestingness";

}
