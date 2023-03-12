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
public class All_Possible_Combinations {

    public static void main(String args[]) {

        for (int i = 97; i <= 122; i++) {
            for (int j = 97; j <= 122; j++) {
                for (int k = 97; k <= 122; k++) {
                    for (int l = 97; l <= 122; l++) {
                        for (int m = 97; m <= 122; m++) {
                            System.out.print((char) i);
                            System.out.print((char) j);
                            System.out.println((char) k);
                            System.out.println((char) l);
                            System.out.println((char) m);
                        }
                    }
                }
            }
        }

    }

}
