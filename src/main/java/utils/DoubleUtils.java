/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author elahi
 */
public class DoubleUtils {

    private static DecimalFormat df2 = new DecimalFormat("#.########");
    
    public static Double formatDouble(Double input){
        df2.setRoundingMode(RoundingMode.UP);
        String str=df2.format(input);   
        return Double.parseDouble(str);
    }

    public static void main(String[] args) {

        double input = 3.14159265359;
        System.out.println("double : " + input);
        System.out.println("double : " + df2.format(input));    //3.14

        // DecimalFormat, default is RoundingMode.HALF_EVEN
        df2.setRoundingMode(RoundingMode.DOWN);
        System.out.println("\ndouble : " + df2.format(input));  //3.14

        df2.setRoundingMode(RoundingMode.UP);
        System.out.println("double : " + df2.format(input));    //3.15

    }

}
