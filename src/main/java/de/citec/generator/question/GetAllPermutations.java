/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import de.citec.sc.generator.utils.FileFolderUtils;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author elahi
 */
public class GetAllPermutations {  
    public static void getPermutations(Integer[] array){  
        helper(array, 0);  
    }  
    
    public static void getPermutations(Double[] array){  
        helperD(array, 0);  
    }  
  
    private static void helper(Integer[] array, int pos){  
        if(pos >= array.length - 1){   
            System.out.print("[");  
            for(int i = 0; i < array.length - 1; i++){  
                System.out.print(array[i] + ", ");  
            }  
            if(array.length > 0)   
                System.out.print(array[array.length - 1]);  
            System.out.println("]");  
            return;  
        }  
  
        for(int i = pos; i < array.length; i++){   
          
            Integer t = array[pos];  
            array[pos] = array[i];  
            array[i] = t;  
  
            helper(array, pos+1);  
  
            t = array[pos];  
            array[pos] = array[i];  
            array[i] = t;  
        }  
    }  
     private static void helperD(Double[] array, int pos){  
        if(pos >= array.length - 1){   
            System.out.print("[");  
            for(int i = 0; i < array.length - 1; i++){  
                System.out.print(array[i] + ", ");  
            }  
            if(array.length > 0)   
                System.out.print(array[array.length - 1]);  
            System.out.println("]");  
            return;  
        }  
  
        for(int i = pos; i < array.length; i++){   
          
            Double t = array[pos];  
            array[pos] = array[i];  
            array[i] = t;  
  
            helperD(array, pos+1);  
  
            t = array[pos];  
            array[pos] = array[i];  
            array[i] = t;  
        }  
    }  
    
     public static Set<String> mergeParameters(String parameterFile,String thresoldFile) {
        Map<String, Set<String>> paramters1 = FileFolderUtils.findParameters(parameterFile);
        System.out.println(paramters1.size());
        Map<String, Set<String>> paramters2 = FileFolderUtils.findParameters(thresoldFile);
        System.out.println(paramters2.size());
        Set<String> mergeParameters = new TreeSet<String>();
        for (String string1 : paramters1.keySet()) {
            for (String string2 : paramters2.keySet()) {
                String merge = string1 + "-" + string2;
                System.out.println(merge);
                mergeParameters.add(merge);
            }
        }
        return mergeParameters;
    }
     
     public static void main(String args[]) {
        /*Integer[] numbers1 = {5, 5,50,50,250,500};
        getPermutations(numbers1);
        Double[] numbers2 = {0.1, 0.6, 0.9};
        getPermutations(numbers2);*/
        
        /*Integer[] numbers1 = {5, 5,50,50};
        getPermutations(numbers1);
        Double[] numbers2 = {0.04, 0.6, 0.9};
        getPermutations(numbers2);*/
        
        Integer[] numbers1 = {5, 5, 5,50,50,50,100};
        getPermutations(numbers1);
        Double[] numbers2 = {0.02, 0.02, 0.02, 0.09,0.04};
        getPermutations(numbers2);
       

    }
}  