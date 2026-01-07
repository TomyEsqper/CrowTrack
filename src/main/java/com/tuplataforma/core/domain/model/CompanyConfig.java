package com.tuplataforma.core.domain.model;

import java.util.Map;

public class CompanyConfig {
    private String timezone;
    private String language;
    private Map<String, String> extraSettings;

    public CompanyConfig(String timezone, String language, Map<String, String> extraSettings) {
        this.timezone = timezone;
        this.language = language;
        this.extraSettings = extraSettings;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getLanguage() {
        return language;
    }

    public Map<String, String> getExtraSettings() {
        return extraSettings;
    }
}
