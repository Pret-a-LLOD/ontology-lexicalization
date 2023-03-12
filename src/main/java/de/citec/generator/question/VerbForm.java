/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.generator.question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elahi
 */
public class VerbForm {

    private String form2ndPerson = null;
    private String form3rdPerson = null;
    private String formPast = null;
    private String formPerfect = null;

    private Map<String, VerbForm> forms = new HashMap<String, VerbForm>();
   
    public VerbForm(String fileName) {
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                line = line.replace(" ", " ");
                line = line.replace("  ", " ");
                line = line.replace("\t", " ");
                String[] lines = line.split(" ");
                VerbForm verbForms = new VerbForm(lines[0], lines[1], lines[2], lines[3]);
                forms.put(lines[0], verbForms);
                forms.put(lines[1], verbForms);;
                forms.put(lines[2], verbForms);;
                forms.put(lines[3], verbForms);;
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public VerbForm(String writtenFormInfinitive_2ndPerson, String writtenForm3rdPerson, String writtenFormPast, String writtenFormPerfect) {
        this.form2ndPerson = writtenFormInfinitive_2ndPerson;
        this.form3rdPerson = writtenForm3rdPerson;
        this.formPast = writtenFormPast;
        this.formPerfect = writtenFormPerfect;

    }

    public String getForm2ndPerson() {
        return form2ndPerson;
    }

    public String getForm3rdPerson() {
        return form3rdPerson;
    }

    public String getFormPast() {
        return formPast;
    }

    public String getFormPerfect() {
        return formPerfect;
    }

    public Map<String, VerbForm> getForm() {
        return forms;
    }

    @Override
    public String toString() {
        return "VerbForm{" + "form2ndPerson=" + form2ndPerson + ", form3rdPerson=" + form3rdPerson + ", formPast=" + formPast + ", formPerfect=" + formPerfect + '}';
    }

   
}