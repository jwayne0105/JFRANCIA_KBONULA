package com.progmemorymatch.model;

import java.awt.Color;

public final class LanguageInfo {
    private final String name;
    private final String badge;
    private final String summary;
    private final String commonUse;
    private final Color color;

    public LanguageInfo(String name, String badge, String summary, String commonUse, Color color) {
        this.name = name;
        this.badge = badge;
        this.summary = summary;
        this.commonUse = commonUse;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getBadge() {
        return badge;
    }

    public String getSummary() {
        return summary;
    }

    public String getCommonUse() {
        return commonUse;
    }

    public Color getColor() {
        return color;
    }

    public String toLearningCardText() {
        return "Language: " + name + "\n\n"
            + "What it is:\n" + summary + "\n\n"
            + "Common use:\n" + commonUse;
    }
}
