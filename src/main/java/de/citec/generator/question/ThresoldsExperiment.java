/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class ThresoldsExperiment implements NullInterestingness, ObjectParamters {

    private LinkedHashMap<String, ThresoldELement> thresoldELements = new LinkedHashMap<String, ThresoldELement>();
    public Map<String, List<Double>> interestingness = new TreeMap<String, List<Double>>();

    public List<Double> supAList = new ArrayList<Double>();
    public List<Double> supBList = new ArrayList<Double>();
    public List<Double> confABList = new ArrayList<Double>();
    public List<Double> confBAList = new ArrayList<Double>();
    public List<Double> CosineList = new ArrayList<Double>();
    public List<Double> AllConfList = new ArrayList<Double>();
    public List<Double> MaxConfList = new ArrayList<Double>();
    public List<Double> IrList = new ArrayList<Double>();
    public List<Double> KulczynskiList = new ArrayList<Double>();
    public List<Double> CoherenceList = new ArrayList<Double>();
    public static List<Integer> numberOfRules = new ArrayList<Integer>();
    public static List<Integer> nGram = new ArrayList<Integer>();

    public ThresoldsExperiment(String type, String associationRule) {
        setParameters(type);
        createExperiment(associationRule);
    }

    public ThresoldsExperiment(String type) {
        setParameters(type);
    }

    public void setParameters(String type) {
        if (type.contains(OBJECT)) {
            supAList = new ArrayList<Double>(ObjectParamters.supAList);
            supBList = new ArrayList<Double>(ObjectParamters.supBList);
            confABList = new ArrayList<Double>(ObjectParamters.confABList);
            confBAList = new ArrayList<Double>(ObjectParamters.confBAList);
            CosineList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            AllConfList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            MaxConfList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            IrList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            KulczynskiList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            CoherenceList = new ArrayList<Double>(ObjectParamters.probabiltyThresold);
            numberOfRules = new ArrayList<Integer>(ObjectParamters.numberOfRules);
            nGram = new ArrayList<Integer>(ObjectParamters.nGram);
        } else if (type.contains(PREDICATE)) {
            supAList = new ArrayList<Double>(PredicateParamters.supAList);
            supBList = new ArrayList<Double>(PredicateParamters.supBList);
            confABList = new ArrayList<Double>(PredicateParamters.confABList);
            confBAList = new ArrayList<Double>(PredicateParamters.confBAList);
            CosineList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            AllConfList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            MaxConfList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            IrList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            KulczynskiList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            CoherenceList = new ArrayList<Double>(PredicateParamters.probabiltyThresold);
            numberOfRules = new ArrayList<Integer>(PredicateParamters.numberOfRules);
            nGram = new ArrayList<Integer>(PredicateParamters.nGram);
        }
        interestingness.put(NullInterestingness.Cosine, CosineList);
        interestingness.put(NullInterestingness.AllConf, AllConfList);
        interestingness.put(NullInterestingness.MaxConf, MaxConfList);
        interestingness.put(NullInterestingness.IR, IrList);
        interestingness.put(NullInterestingness.Kulczynski, KulczynskiList);
        interestingness.put(NullInterestingness.Coherence, CoherenceList);
    }

    private void createExperiment(String associationRule) {
        Integer index = 0;
        for (Integer n_gram : nGram) {
            for (Integer numberOfRule : numberOfRules) {
                for (Double supA : supAList) {
                    for (Double supB : supBList) {
                        for (Double confAB : confABList) {
                            for (Double confBA : confBAList) {
                                for (Double probabiltyValue : this.getInterestingList(associationRule)) {
                                    index = index + 1;
                                    ThresoldELement thresoldELement = new ThresoldELement(supA, supB, confAB, confBA, associationRule, probabiltyValue, numberOfRule, n_gram);
                                    //String line=associationRule+index.toString()+"-"+ thresoldELement;
                                    String line = associationRule + "-" + thresoldELement;
                                    thresoldELements.put(line, thresoldELement);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public static Map<String, ThresoldsExperiment> createExperiments(String type, Set<String> interestingness) throws Exception {
        Map<String, ThresoldsExperiment> associationRulesExperiment = new TreeMap<String, ThresoldsExperiment>();
        for (String associationRule : interestingness) {
            ThresoldsExperiment thresold = new ThresoldsExperiment(type);
            associationRulesExperiment.put(associationRule, thresold);
        }
        return associationRulesExperiment;
    }

    public LinkedHashMap<String, ThresoldELement> getThresoldELements() {
        return thresoldELements;
    }

    private List<Double> getInterestingList(String type) {
        if (type.contains(AllConf)) {
            return AllConfList;
        } else if (type.contains(MaxConf)) {
            return MaxConfList;
        } else if (type.contains(IR)) {
            return IrList;
        } else if (type.contains(Kulczynski)) {
            return KulczynskiList;
        } else if (type.contains(Cosine)) {
            return CosineList;
        } else if (type.contains(Coherence)) {
            return CoherenceList;
        } else {
            return new ArrayList<Double>();
        }
    }

    public Map<String, List<Double>> getInterestingness() {
        return interestingness;
    }

    @Override
    public String toString() {
        return "ThresoldsExperiment{" + "thresoldELements=" + thresoldELements + '}';
    }

    public class ThresoldELement {

        private Integer rules = 0;
        private Integer n_gram = 0;
        private String type = null;
        private LinkedHashMap<String, Double> givenThresolds = new LinkedHashMap<String, Double>();

        public ThresoldELement(Double supA, Double supB, Double confAB, Double confBA, String type, Double probabiltyValue, Integer numberOfRules, Integer n_gram) {
            this.givenThresolds.put(NullInterestingness.supA, supA);
            this.givenThresolds.put(NullInterestingness.supB, supB);
            this.givenThresolds.put(NullInterestingness.condAB, confAB);
            this.givenThresolds.put(NullInterestingness.condBA, confBA);
            this.type = type;
            this.givenThresolds.put(type, probabiltyValue);
            this.rules = numberOfRules;
            this.n_gram = n_gram;
        }

        public ThresoldELement(Double supA, Double supB, Double confAB, Double confBA, String type, Double probabiltyValue, Integer numberOfRules) {
            this.givenThresolds.put(NullInterestingness.supA, supA);
            this.givenThresolds.put(NullInterestingness.supB, supB);
            this.givenThresolds.put(NullInterestingness.condAB, confAB);
            this.givenThresolds.put(NullInterestingness.condBA, confBA);
            this.type = type;
            this.givenThresolds.put(type, probabiltyValue);
            this.rules = numberOfRules;
        }

        public Integer getNumberOfRules() {
            return rules;
        }

        public Integer getN_gram() {
            return n_gram;
        }

        public LinkedHashMap<String, Double> getGivenThresolds() {
            return givenThresolds;
        }

        @Override
        public String toString() {
            String n_gramString = null;
            if (this.n_gram == 0) {
                n_gramString = "1-4";
            } else {
                n_gramString = this.n_gram.toString();
            }

            //nGram + "_" + n_gramString + "-"
            return nGram + "_" + n_gramString + "-"
                    + numRule + "_" + rules + "-"
                    + supA + "_" + givenThresolds.get(supA) + "-"
                    + supB + "_" + givenThresolds.get(supB) + "-"
                    + condAB + "_" + givenThresolds.get(condAB) + "-"
                    + condBA + "_" + givenThresolds.get(condBA) + "-"
                    + type + "_" + givenThresolds.get(type);
        }

    }
    
    public static void main(String[] args) {
        System.out.println("hellow world!!");
        List<String> rulePatterns = new ArrayList<String>(Arrays.asList("rules-predict_l_for_s_given_p-", "rules-predict_localized_l_for_s_given_p-"));
        for (String associationRule: rulePatterns) {
            ThresoldsExperiment thresoldsExperiment=new ThresoldsExperiment(PREDICATE, associationRule);
            System.out.println(thresoldsExperiment.thresoldELements);

        }
    }

}
