package org.aluralatam.literalura.validations;

import org.aluralatam.literalura.model.Language;

public class DataValidator {
    public static boolean isPositiveInteger(String value) {
        return value.matches("\\d+");
    }

    public static boolean isValidString(String value) {
        // Validamos que no sea nulo y que contenga solo letras
        return value != null && value.matches("[a-zA-Z]+");
    }
}
