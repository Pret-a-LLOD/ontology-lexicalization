/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

/**
 *
 * @author elahi
 */
public class GetAllPermutations {  
    public static void getPermutations(Integer[] array){  
        helper(array, 0);  
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
    public static void main(String args[]) {
        Integer[] numbers1 = {5, 50, 100,200,300,400,500};
        getPermutations(numbers1);
       

    }
}  