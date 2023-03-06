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
import java.util.Arrays;

public enum Language {
    BE,
    BG,
    BN,
    CS,
    DA,
    DE,
    EL,
    EN,
    ES,
    ET,
    FI,
    FR,
    GA,
    HI,
    HR,
    HU,
    IT,
    JA,
    KO,
    LV,
    NL,
    PL,
    PT,
    RO,
    RU,
    TA,
    SK,
    SL,
    SV;

    /**
     * Find the matching enum value for languageString or return EN as default
     *
     * @param languageString the language string e.g. "en" (not case-sensitive)
     * @return the matching enum value or default EN
     */
    public static Language stringToLanguage(String languageString) {
        return Arrays.stream(Language.values()).filter(
                language -> language.name().toLowerCase().equals(languageString.toLowerCase())
        ).findAny().orElse(EN);
    }

}

