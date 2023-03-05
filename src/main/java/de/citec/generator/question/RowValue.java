/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import java.util.List;

/**
 *
 * @author elahi
 */
public class RowValue {

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
    private Integer parameterIndex = 17;
    private Integer nGramIndex = 6;
    private Integer lingPatternIndex = 4;
    private Integer propertyIndex = 8;
    private Integer classIndex = 0;
    private String className = null;
    private String ruleName = null;
    private String property = null;
    private String lingPattern = null;
    private Double interestingnessValue = null;
    private String nGram = null;
    private Parameters parameters = null;

    public RowValue(String row[], String interestingnessType, LexicalEntryHelper lexicalEntryHelper) throws Exception {
        this.lingPattern = row[lingPatternIndex];
        this.interestingnessValue = Conversion.stringToDouble(row[parameterIndex]);
        this.className = lexicalEntryHelper.formatPropertyLongToShort(row[classIndex]);
        this.nGram = row[nGramIndex];
        this.property = row[propertyIndex];
        this.parameters = new Parameters(row, interestingnessType,interestingnessValue);
    }

    public Integer getParameterIndex() {
        return parameterIndex;
    }

    public Integer getnGramIndex() {
        return nGramIndex;
    }

    public Integer getLingPatternIndex() {
        return lingPatternIndex;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public Integer getClassIndex() {
        return classIndex;
    }

    public String getClassName() {
        return className;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getProperty() {
        return property;
    }

    public String getLingPattern() {
        return lingPattern;
    }

    public Double getValue() {
        return interestingnessValue;
    }

    public String getnGram() {
        return nGram;
    }

    public Parameters getParameters() {
        return parameters;
    }
    
    

}
