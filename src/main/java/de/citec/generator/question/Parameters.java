/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author elahi
 */
public class Parameters {

    private Integer supA = 0;
    private Integer supB = 0;
    private Integer supAB = 0;
    private Double confAB = 0.0;
    private Double confBA = 0.0;
    private Double interestingness = 0.0;
    private String interestingnessType = null;
    private String searchString = null;
    private List<Integer> rankThresolds = new ArrayList<Integer>();

    //using manual lines  
    //0: http://dbpedia.org/ontology/AcademicJournal,
    //1: predict_l_for_s_given_p,
    //2: "c_s
    //3: ,p => l_s",
    //4: "medical journal in the field",
    //5: ,
    //6: 5-gram,
    //7: ,
    //8: http://dbpedia.org/property/eissn,
    //9: ,
    //AB 10: 0.0024330900243309,
    //BA 11: 0.875,5754,
    //supA 12: 16,
    //supB 13: 14,
    //supAB 14: 0.0024330900243309,
    //Al 15: 0.0024263431542461,
    //coher 16: 0.0461405870280119,
    //cos 17: 0.996872828353023,
    //IR 18: 0.438716545012165,
    //Kul 19: 0.875,
    //Max 20: 
    //
    
    public Parameters(String[] row, String interestingnessType,Double interesitngnessValue) {
        this.supA = Integer.parseInt(row[12]);
        this.supB = Integer.parseInt(row[13]);
        this.supAB = Integer.parseInt(row[14]);
        this.confAB = Double.parseDouble(row[10]);
        this.confBA = Double.parseDouble(row[11]);
        this.interestingness = interesitngnessValue;
        this.interestingnessType = interestingnessType;
    }

    public Parameters(String rulePattern, String[] row, String interestingnessType, List<Integer> rankThresolds) {
        this.supA = Integer.parseInt(row[12]);
        this.supB = Integer.parseInt(row[13]);
        this.supAB = Integer.parseInt(row[14]);
        this.confAB = Double.parseDouble(row[10]);
        this.confBA = Double.parseDouble(row[11]);
        this.interestingness = Double.parseDouble(row[17]);
        this.interestingnessType = interestingnessType;
        this.rankThresolds.addAll(rankThresolds);
        this.searchString = rulePattern;
    }

    public Parameters(String rulePattern, String parameterString, String interestingnessTypeT, List<Integer> rankThresolds) {
        String[] info = parameterString.split("-");
        this.supA = Integer.parseInt(info[0]);
        this.supB = Integer.parseInt(info[1]);
        this.supAB = Integer.parseInt(info[2]);
        this.confAB = Double.parseDouble(info[3]);
        this.confBA = Double.parseDouble(info[4]);
        this.interestingness = Double.parseDouble(info[5]);
        this.interestingnessType = interestingnessTypeT;
        this.rankThresolds.addAll(rankThresolds);
        this.searchString = rulePattern + parameterString;
    }

    public Integer getSupA() {
        return supA;
    }

    public Integer getSupB() {
        return supB;
    }

    public Integer getSupAB() {
        return supAB;
    }

    public Double getConfAB() {
        return confAB;
    }

    public Double getConfBA() {
        return confBA;
    }

    public Double getInterestingness() {
        return interestingness;
    }

    public String getInterestingnessType() {
        return interestingnessType;
    }

    public String getSearchString() {
        return searchString;
    }

    public List<Integer> getRankThresolds() {
        return rankThresolds;
    }

    @Override
    public String toString() {
        return "Parameters{" + "supA=" + supA + ", supB=" + supB + ", supAB=" + supAB + ", confAB=" + confAB + ", confBA=" + confBA + ", interestingness=" + interestingness + ", interestingnessType=" + interestingnessType + ", searchString=" + searchString + ", rankThresolds=" + rankThresolds + '}';
    }

}
