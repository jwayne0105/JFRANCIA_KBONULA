package com.progmemorymatch.model;

public final class GameResult {
    private final GameSettings.Difficulty difficulty;
    private final String stageName;
    private final int stageNumber;
    private final int totalStages;
    private final boolean hasNextStage;
    private final boolean cleared;
    private final String outcomeMessage;
    private final int moveLimit;
    private final int timeLimitSeconds;
    private final int remainingSeconds;
    private final int moves;
    private final int elapsedSeconds;
    private final int score;
    private final int matchedPairs;

    public GameResult(
        GameSettings.Difficulty difficulty,
        String stageName,
        int stageNumber,
        int totalStages,
        boolean hasNextStage,
        boolean cleared,
        String outcomeMessage,
        int moveLimit,
        int timeLimitSeconds,
        int remainingSeconds,
        int moves,
        int elapsedSeconds,
        int score,
        int matchedPairs
    ) {
        this.difficulty = difficulty;
        this.stageName = stageName;
        this.stageNumber = stageNumber;
        this.totalStages = totalStages;
        this.hasNextStage = hasNextStage;
        this.cleared = cleared;
        this.outcomeMessage = outcomeMessage;
        this.moveLimit = moveLimit;
        this.timeLimitSeconds = timeLimitSeconds;
        this.remainingSeconds = remainingSeconds;
        this.moves = moves;
        this.elapsedSeconds = elapsedSeconds;
        this.score = score;
        this.matchedPairs = matchedPairs;
    }

    public GameSettings.Difficulty getDifficulty() {
        return difficulty;
    }

    public String getStageName() {
        return stageName;
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public int getTotalStages() {
        return totalStages;
    }

    public boolean hasNextStage() {
        return hasNextStage;
    }

    public boolean isCleared() {
        return cleared;
    }

    public String getOutcomeMessage() {
        return outcomeMessage;
    }

    public int getMoveLimit() {
        return moveLimit;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public int getMoves() {
        return moves;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public int getScore() {
        return score;
    }

    public int getMatchedPairs() {
        return matchedPairs;
    }
}
