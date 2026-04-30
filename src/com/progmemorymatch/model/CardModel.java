package com.progmemorymatch.model;

public final class CardModel {
    private final int id;
    private final LanguageInfo language;
    private boolean revealed;
    private boolean matched;

    public CardModel(int id, LanguageInfo language) {
        this.id = id;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public LanguageInfo getLanguage() {
        return language;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
        if (matched) {
            this.revealed = true;
        }
    }
}
