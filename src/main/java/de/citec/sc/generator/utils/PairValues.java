/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.generator.utils;

/**
 *
 * @author elahi
 */
public class PairValues {

    private Boolean flag = false;
    private String key = null;
    private String value = null;

    public PairValues(Boolean flag, String key, String value) {
        this.flag = flag;
        this.key = key;
        this.value = value;

    }

    public PairValues(String key, String value) {
        this.key = key;
        this.value = value;

    }

    public Boolean getFlag() {
        return flag;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "PairValues{" + "key=" + key + ", value=" + value + '}';
    }

}
