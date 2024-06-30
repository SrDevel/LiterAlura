package org.aluralatam.literalura.model;

public enum Language {
    ENGLISH("en", "Inglés"),
    SPANISH("es", "Español"),
    PORTUGUESE("pt", "Portugués"),
    FRENCH("fr", "Francés"),
    GERMAN("de", "Alemán"),
    ITALIAN("it", "Italiano"),
    DUTCH("nl", "Holandés"),
    RUSSIAN("ru", "Ruso"),
    CHINESE("zh", "Chino"),
    JAPANESE("ja", "Japonés"),
    KOREAN("ko", "Coreano"),
    ARABIC("ar", "Árabe"),
    HINDI("hi", "Hindi"),
    BENGALI("bn", "Bengalí"),
    PUNJABI("pa", "Panyabí"),
    TELUGU("te", "Telugu"),
    MARATHI("mr", "Marathi"),
    TAMIL("ta", "Tamil"),
    URDU("ur", "Urdu"),
    GUJARATI("gu", "Gujarati"),
    KANNADA("kn", "Kannada"),
    MALAYALAM("ml", "Malayalam"),
    ORIYA("or", "Oriya"),
    PERSIAN("fa", "Persa"),
    POLISH("pl", "Polaco"),
    TURKISH("tr", "Turco"),
    UKRAINIAN("uk", "Ucraniano"),
    ROMANIAN("ro", "Rumano"),
    GREEK("el", "Griego"),
    SWEDISH("sv", "Sueco"),
    DANISH("da", "Danés"),
    NORWEGIAN("no", "Noruego"),
    FINNISH("fi", "Finlandés"),
    CZECH("cs", "Checo"),
    SLOVAK("sk", "Eslovaco"),
    HUNGARIAN("hu", "Húngaro"),
    BULGARIAN("bg", "Búlgaro"),
    CROATIAN("hr", "Croata"),
    SERBIAN("sr", "Serbio"),
    SLOVENIAN("sl", "Esloveno"),
    LITHUANIAN("lt", "Lituano"),
    LATVIAN("lv", "Letón");

    private final String code;
    private final String spanishName;

    Language(String code, String spanishName) {
        this.code = code;
        this.spanishName = spanishName;
    }

    public static Language fromCode(String code) {
        for (Language language : Language.values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        return null;
    }

    public static Language fromSpanishName(String spanishName) {
        for (Language language : Language.values()) {
            if (language.spanishName.equals(spanishName)) {
                return language;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getSpanishName() {
        return spanishName;
    }
}

