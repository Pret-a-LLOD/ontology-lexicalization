/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class PropertyCSV {
    private String classNameIndex = "classNameIndex";
    private String ruletypeIndex = "ruletypeIndex";
    private String linguisticPatternIndex = "linguisticPatternIndex";
    private String orderOfArgumentsIndex = "orderOfArgumentsIndex";
    private String patterntypeIndex = "patterntypeIndex";
    private String subjectIndex = "subjectIndex";
    private String predicateIndex = "predicateIndex";
    private String objectIndex = "objectIndex";
    private String condABIndex = "condABIndex";
    private String condBAIndex = "condBAIndex";
    private String supAIndex = "supAIndex";
    private String supBIndex = "supBIndex";
    private String supABIndex = "supABIndex";
    private String AllConfIndex = "AllConfIndex";
    private String CoherenceIndex = "CoherenceIndex";
    private String CosineIndex = "CosineIndex";
    private String IRIndex = "IRIndex";
    private String KulczynskiIndex = "KulczynskiIndex";
    private String MaxConfIndex = "MaxConfIndex";
    private String stringIndex = "stringIndex";
    public static String localized="localized";
    public static String general="general";
    private Map<String,Integer> indexMap=new TreeMap<String,Integer> ();
    
    /*
0 class	
1 ruletype_longname	
2 ruletype_shortname	
3 linguistic pattern	
4 order of arguments	
5 patterntype	
6 subject	
7 predicate	
8 object	
9 condAB	
10 condBA
11 supA
12 supB	
13 supAB	
14 AllConf	
15 Coherence	
16 Cosine	
17 IR
18 Kulczynski	
19 MaxConf	
20 string
    */
    

    
    public PropertyCSV() {
        indexMap.put(classNameIndex, 0);
        indexMap.put(ruletypeIndex, 1);
        indexMap.put(linguisticPatternIndex, 3);
        indexMap.put(orderOfArgumentsIndex, 4);
        indexMap.put(patterntypeIndex, 5);
        indexMap.put(subjectIndex, 6);
        indexMap.put(predicateIndex, 7);
        indexMap.put(objectIndex, 8);
        indexMap.put(condABIndex, 9);
        indexMap.put(condBAIndex, 10);
        indexMap.put(supAIndex, 11);
        indexMap.put(supBIndex, 12);
        indexMap.put(supABIndex, 13);
        indexMap.put(AllConfIndex, 14);
        indexMap.put(CoherenceIndex, 15);
        indexMap.put(CosineIndex, 16);
        indexMap.put(IRIndex, 17);
        indexMap.put(KulczynskiIndex, 18);
        indexMap.put(MaxConfIndex, 19);
        indexMap.put(stringIndex, 20);
    }

    public Integer getClassNameIndex() {
        return indexMap.get(classNameIndex);
    }

    public Integer getRuletypeIndex() {
        return indexMap.get(ruletypeIndex);
    }

    public Integer getOrderOfArgumentsIndex() {
        return indexMap.get(orderOfArgumentsIndex);
    }

    public Integer getPatterntypeIndex() {
        return indexMap.get(patterntypeIndex);
    }

    public Integer getSubjectIndex() {
        return indexMap.get(subjectIndex);
    }

    public Integer getPredicateIndex() {
        return indexMap.get(predicateIndex);
    }

    public Integer getObjectIndex() {
        return indexMap.get(objectIndex);
    }

    public Integer getCondABIndex() {
        return indexMap.get(condABIndex);
    }

    public Integer getCondBAIndex() {
        return indexMap.get(condBAIndex);
    }

    public Integer getSupAIndex() {
        return indexMap.get(supAIndex);
    }

    public Integer getSupBIndex() {
        return indexMap.get(supBIndex);
    }

    public Integer getSupABIndex() {
        return indexMap.get(supABIndex);
    }

    public Integer getAllConfIndex() {
        return indexMap.get(AllConfIndex);
    }

    public Integer getCoherenceIndex() {
        return indexMap.get(CoherenceIndex);
    }

    public Integer getCosineIndex() {
        return indexMap.get(CosineIndex);
    }

    public Integer getIRIndex() {
        return indexMap.get(IRIndex);
    }

    public Integer getKulczynskiIndex() {
        return indexMap.get(KulczynskiIndex);
    }

    public Integer getMaxConfIndex() {
        return indexMap.get(MaxConfIndex);
    }

    public Integer getStringIndex() {
        return indexMap.get(stringIndex);
    }
   
    public Integer getLinguisticPatternIndex() {
        return indexMap.get(linguisticPatternIndex);
    }


    public static String getLocalized() {
        return localized;
    }

    public Map<String, Integer> getIndexMap() {
        return indexMap;
    }
    
    
    ////// without localize
     //Integer classIndex=0, ruletypeIndex=1,linguisticPatternIndex=2,patterntypeIndex=3,
            //subjectIndex=4,predicateIndex=5, objectIndex=6,stringIndex=18;
            // Integer condABIndex=7,condBAIndex=8,supAIndex=9,supBIndex=10,supABIndex=11,AllConfInex=12,CoherenceIndex=13,
            //CosineIndex=14,IRIndex=15,KulczynskiIndex=16,MaxConfINdex=17;stringIndex=18;
    
    ////localize
    
           // Integer classNameIndex=0, ruletypeIndex=1,linguisticPatternIndex=2, orderOfArgumentsIndex=3,	
           //         patterntypeIndex=4,	subjectIndex=5,	predicateIndex=6,	objectIndex=7,	condABIndex=8,	
            //        condBAIndex=9,	supAIndex=10,	supBIndex=11,	supABIndex=12,	AllConfIndex=13,	
     //CoherenceIndex=14,	CosineIndex=15,	IRINdex=16	KulczynskiIndex=17,	MaxConfIndex=18,	stringIndex=19;

    @Override
    public String toString() {
        return "PropertyCSV{" + "classNameIndex=" + classNameIndex + ", ruletypeIndex=" + ruletypeIndex + ", linguisticPatternIndex=" + linguisticPatternIndex + ", orderOfArgumentsIndex=" + orderOfArgumentsIndex + ", patterntypeIndex=" + patterntypeIndex + ", subjectIndex=" + subjectIndex + ", predicateIndex=" + predicateIndex + ", objectIndex=" + objectIndex + ", condABIndex=" + condABIndex + ", condBAIndex=" + condBAIndex + ", supAIndex=" + supAIndex + ", supBIndex=" + supBIndex + ", supABIndex=" + supABIndex + ", AllConfIndex=" + AllConfIndex + ", CoherenceIndex=" + CoherenceIndex + ", CosineIndex=" + CosineIndex + ", IRIndex=" + IRIndex + ", KulczynskiIndex=" + KulczynskiIndex + ", MaxConfIndex=" + MaxConfIndex + ", stringIndex=" + stringIndex + ", indexMap=" + indexMap + '}';
    }

   
    
}
