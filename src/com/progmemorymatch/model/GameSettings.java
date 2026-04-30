package com.progmemorymatch.model;

public final class GameSettings {
    public enum Difficulty {
        BEGINNER("Starter Tier (Campaign starts at 2 x 3)", 2, 3),
        INTERMEDIATE("Builder Tier (Campaign starts at 3 x 4)", 3, 4),
        ADVANCED("Pro Tier (Campaign starts at 4 x 4)", 4, 4);

        private final String label;
        private final int rows;
        private final int columns;

        Difficulty(String label, int rows, int columns) {
            this.label = label;
            this.rows = rows;
            this.columns = columns;
        }

        public String getLabel() {
            return label;
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    public enum VisualStyle {
        ARCADE("Arcade Cocoa"),
        CIRCUIT("Circuit Green"),
        COBALT("Cobalt Night"),
        SUNSET("Sunset Dev");

        private final String label;

        VisualStyle(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private Difficulty difficulty = Difficulty.BEGINNER;
    private VisualStyle visualStyle = VisualStyle.ARCADE;
    private int mismatchDelayMs = 900;
    private float masterVolume = 0.65f;
    private boolean muted;
    private boolean showLearningPanel = true;

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public VisualStyle getVisualStyle() {
        return visualStyle;
    }

    public void setVisualStyle(VisualStyle visualStyle) {
        this.visualStyle = visualStyle;
    }

    public int getMismatchDelayMs() {
        return mismatchDelayMs;
    }

    public void setMismatchDelayMs(int mismatchDelayMs) {
        this.mismatchDelayMs = Math.max(450, Math.min(1800, mismatchDelayMs));
    }

    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = Math.max(0.0f, Math.min(1.0f, masterVolume));
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isShowLearningPanel() {
        return showLearningPanel;
    }

    public void setShowLearningPanel(boolean showLearningPanel) {
        this.showLearningPanel = showLearningPanel;
    }
}
