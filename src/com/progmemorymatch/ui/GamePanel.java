package com.progmemorymatch.ui;

import com.progmemorymatch.MemoryMatchApp;
import com.progmemorymatch.model.CardModel;
import com.progmemorymatch.model.DeckFactory;
import com.progmemorymatch.model.GameResult;
import com.progmemorymatch.model.GameSettings;
import com.progmemorymatch.model.LanguageInfo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GamePanel extends GradientPanel {
    private static final class StageConfig {
        private final String name;
        private final int rows;
        private final int columns;
        private final int previewMs;
        private final int moveLimit;
        private final int timeLimitSeconds;
        private final int mismatchTimePenaltySeconds;

        private StageConfig(
            String name,
            int rows,
            int columns,
            int previewMs,
            int moveLimit,
            int timeLimitSeconds,
            int mismatchTimePenaltySeconds
        ) {
            this.name = name;
            this.rows = rows;
            this.columns = columns;
            this.previewMs = previewMs;
            this.moveLimit = moveLimit;
            this.timeLimitSeconds = timeLimitSeconds;
            this.mismatchTimePenaltySeconds = mismatchTimePenaltySeconds;
        }

        private int getPairCount() {
            return (rows * columns) / 2;
        }
    }

    private static final List<StageConfig> CAMPAIGN_STAGES = List.of(
        new StageConfig("Warmup Grid", 2, 3, 3400, 8, 48, 4),
        new StageConfig("Loop Apprentice", 2, 4, 3000, 11, 56, 5),
        new StageConfig("OOP Builder", 3, 4, 2500, 16, 64, 6),
        new StageConfig("Bug Hunter", 4, 4, 2200, 20, 70, 7),
        new StageConfig("Algorithm Rush", 4, 5, 1900, 24, 76, 8),
        new StageConfig("Compiler Gauntlet", 4, 6, 1600, 28, 84, 9)
    );

    private final MemoryMatchApp app;

    private final JPanel statsPanel = new JPanel(new GridLayout(1, 5, 8, 8));
    private final JPanel controlsPanel = new JPanel();
    private final JPanel topPanel = new JPanel(new BorderLayout(10, 10));
    private final JPanel boardPanel = new JPanel();
    private final JPanel learningPanel = new JPanel(new BorderLayout(8, 8));
    private final JTextArea learningArea = new JTextArea();
    private final JLabel phaseLabel = new JLabel("Press Start Campaign to begin.", SwingConstants.CENTER);

    private final JLabel levelValue = new JLabel("L1", SwingConstants.CENTER);
    private final JLabel timerValue = new JLabel("0s", SwingConstants.CENTER);
    private final JLabel movesValue = new JLabel("0", SwingConstants.CENTER);
    private final JLabel scoreValue = new JLabel("0", SwingConstants.CENTER);
    private final JLabel pairsValue = new JLabel("0/0", SwingConstants.CENTER);

    private final ThreeDButton restartButton = new ThreeDButton("Restart");
    private final ThreeDButton menuButton = new ThreeDButton("Main Menu");
    private final ThreeDButton muteButton = new ThreeDButton("Mute");

    private final Map<Integer, CardButton> cardButtonsById = new HashMap<>();
    private final Deque<CardModel> openedCards = new ArrayDeque<>(2);
    private List<CardModel> deck = new ArrayList<>();

    private Timer gameClockTimer;
    private Timer concealTimer;
    private Timer previewTimer;
    private long startMillis;
    private int elapsedAtStop;
    private int remainingAtStop;
    private int mismatchPenaltySeconds;
    private int moves;
    private int score;
    private int matchedPairs;
    private int stageIndex;
    private boolean previewActive;
    private boolean sessionRunning;
    private boolean boardLocked;

    public GamePanel(MemoryMatchApp app, ThemePalette palette) {
        super(palette);
        this.app = app;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        buildTopPanel();
        buildBoardPanel();
        buildLearningPanel();

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(learningPanel, BorderLayout.EAST);

        restartButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            startStageSession(stageIndex);
        });
        menuButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Return to main menu? Current stage progress will be lost.",
                "Leave Game",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                app.openMenu();
            }
        });
        muteButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            boolean muted = !app.getSettings().isMuted();
            app.getSettings().setMuted(muted);
            app.getSoundManager().setMuted(muted);
            updateMuteButtonText();
        });

        setPalette(palette);
    }

    public void startCampaign() {
        stageIndex = getStartStageIndex(app.getSettings().getDifficulty());
        startStageSession(stageIndex);
    }

    public void startNextStageOrRestartCampaign() {
        if (stageIndex < CAMPAIGN_STAGES.size() - 1) {
            stageIndex++;
        } else {
            stageIndex = getStartStageIndex(app.getSettings().getDifficulty());
        }
        startStageSession(stageIndex);
    }

    public void retryCurrentStage() {
        startStageSession(stageIndex);
    }

    private void startStageSession(int targetStageIndex) {
        stopTimers();
        stageIndex = Math.max(0, Math.min(CAMPAIGN_STAGES.size() - 1, targetStageIndex));
        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);
        deck = DeckFactory.buildDeck(stage.rows, stage.columns);
        moves = 0;
        score = 0;
        matchedPairs = 0;
        elapsedAtStop = 0;
        remainingAtStop = stage.timeLimitSeconds;
        mismatchPenaltySeconds = 0;
        sessionRunning = true;
        boardLocked = true;
        previewActive = true;
        startMillis = 0L;
        openedCards.clear();
        cardButtonsById.clear();

        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(stage.rows, stage.columns, 10, 10));

        ThemePalette palette = app.getPalette();
        for (CardModel card : deck) {
            CardButton cardButton = new CardButton(card);
            card.setRevealed(true);
            cardButton.refreshVisual(palette);
            cardButton.addActionListener(event -> onCardSelected(card));
            cardButtonsById.put(card.getId(), cardButton);
            boardPanel.add(cardButton);
        }

        resetLearningText();
        updateLearningPanelVisibility();
        updateMuteButtonText();
        phaseLabel.setText(
            "Preview: memorize all cards (" + (stage.previewMs / 1000.0) + " sec) - "
                + stage.name
                + " | Moves: " + stage.moveLimit
                + " | Time: " + stage.timeLimitSeconds + "s"
        );
        updateHud();

        gameClockTimer = new Timer(250, event -> {
            updateHud();
            if (!sessionRunning || previewActive) {
                return;
            }
            if (getRemainingSeconds() <= 0 && matchedPairs < stage.getPairCount()) {
                finishSession(false, "Time ran out.");
            }
        });
        gameClockTimer.start();

        previewTimer = new Timer(stage.previewMs, event -> endPreviewPhase());
        previewTimer.setRepeats(false);
        previewTimer.start();

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void stopSession() {
        if (sessionRunning && startMillis > 0L) {
            elapsedAtStop = getElapsedSeconds();
            remainingAtStop = getRemainingSeconds();
        }
        sessionRunning = false;
        boardLocked = false;
        previewActive = false;
        stopTimers();
    }

    @Override
    public void setPalette(ThemePalette palette) {
        super.setPalette(palette);
        topPanel.setOpaque(true);
        topPanel.setBackground(palette.panelColor);
        statsPanel.setOpaque(false);
        controlsPanel.setOpaque(false);
        boardPanel.setBackground(new Color(0, 0, 0, 35));
        learningPanel.setOpaque(true);
        learningPanel.setBackground(palette.panelColor);
        phaseLabel.setForeground(palette.textSecondary);
        learningArea.setForeground(palette.textPrimary);
        learningArea.setCaretColor(palette.textPrimary);
        learningArea.setBackground(new Color(
            Math.max(0, palette.panelColor.getRed() - 20),
            Math.max(0, palette.panelColor.getGreen() - 20),
            Math.max(0, palette.panelColor.getBlue() - 20),
            225
        ));

        restartButton.setColors(palette.accentTop, palette.accentBottom);
        menuButton.setColors(new Color(84, 193, 244), new Color(43, 134, 211));
        muteButton.setColors(new Color(120, 149, 229), new Color(84, 111, 191));
        styleStatTiles(palette);

        for (CardButton cardButton : cardButtonsById.values()) {
            cardButton.refreshVisual(palette);
        }
    }

    private void buildTopPanel() {
        Font valueFont = new Font("Segoe UI", Font.BOLD, 19);
        Font captionFont = new Font("Segoe UI", Font.PLAIN, 12);

        statsPanel.add(createStatTile("LEVEL", levelValue, valueFont, captionFont));
        statsPanel.add(createStatTile("TIME LEFT", timerValue, valueFont, captionFont));
        statsPanel.add(createStatTile("MOVES", movesValue, valueFont, captionFont));
        statsPanel.add(createStatTile("SCORE", scoreValue, valueFont, captionFont));
        statsPanel.add(createStatTile("PAIRS", pairsValue, valueFont, captionFont));

        controlsPanel.setLayout(new GridLayout(1, 3, 8, 8));
        controlsPanel.add(restartButton);
        controlsPanel.add(muteButton);
        controlsPanel.add(menuButton);

        phaseLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(controlsPanel, BorderLayout.EAST);
        topPanel.add(phaseLabel, BorderLayout.SOUTH);
    }

    private JPanel createStatTile(String title, JLabel valueLabel, Font valueFont, Font captionFont) {
        JPanel tile = new JPanel();
        tile.setLayout(new BorderLayout());
        tile.setOpaque(true);
        tile.setBackground(new Color(0, 0, 0, 35));
        tile.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(captionFont);
        titleLabel.setForeground(new Color(210, 230, 224));
        titleLabel.putClientProperty("role", "caption");

        valueLabel.setFont(valueFont);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.putClientProperty("role", "value");

        tile.add(titleLabel, BorderLayout.NORTH);
        tile.add(valueLabel, BorderLayout.CENTER);
        return tile;
    }

    private void styleStatTiles(ThemePalette palette) {
        for (Component component : statsPanel.getComponents()) {
            if (!(component instanceof JPanel)) {
                continue;
            }
            JPanel tile = (JPanel) component;
            tile.setBackground(new Color(0, 0, 0, 48));
            for (Component tileChild : tile.getComponents()) {
                if (tileChild instanceof JLabel) {
                    JLabel text = (JLabel) tileChild;
                    Object role = text.getClientProperty("role");
                    if ("caption".equals(role)) {
                        text.setForeground(palette.textSecondary);
                    } else {
                        text.setForeground(palette.textPrimary);
                    }
                }
            }
        }
    }

    private void buildBoardPanel() {
        boardPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        boardPanel.setPreferredSize(new Dimension(760, 560));
    }

    private void buildLearningPanel() {
        learningPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        learningPanel.setPreferredSize(new Dimension(300, 560));

        JLabel headingLabel = new JLabel("Programming Knowledge", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 21));
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 10, 2));

        learningArea.setEditable(false);
        learningArea.setLineWrap(true);
        learningArea.setWrapStyleWord(true);
        learningArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        learningArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(learningArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 70), 1));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        learningPanel.add(headingLabel, BorderLayout.NORTH);
        learningPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void endPreviewPhase() {
        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);
        for (CardModel card : deck) {
            if (!card.isMatched()) {
                card.setRevealed(false);
                refreshCard(card);
            }
        }
        previewActive = false;
        boardLocked = false;
        startMillis = System.currentTimeMillis();
        phaseLabel.setText(
            "Stage " + (stageIndex + 1)
                + ": " + stage.moveLimit + " moves, " + stage.timeLimitSeconds
                + "s timer. Wrong guess: -" + stage.mismatchTimePenaltySeconds + "s"
        );
    }

    private void onCardSelected(CardModel card) {
        if (!sessionRunning || boardLocked || previewActive || card.isMatched() || card.isRevealed()) {
            return;
        }
        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);

        card.setRevealed(true);
        refreshCard(card);
        openedCards.addLast(card);

        if (openedCards.size() < 2) {
            updateHud();
            return;
        }

        moves++;
        CardModel first = openedCards.removeFirst();
        CardModel second = openedCards.removeFirst();
        boolean isMatch = first.getLanguage().getName().equals(second.getLanguage().getName());

        if (isMatch) {
            first.setMatched(true);
            second.setMatched(true);
            matchedPairs++;
            score += 26 + ((stageIndex + 1) * 6);
            score += Math.max(0, 12 - (getElapsedSeconds() / 10));
            app.getSoundManager().playMatch();
            refreshCard(first);
            refreshCard(second);
            showLearningFact(first.getLanguage());

            if (matchedPairs == deck.size() / 2) {
                finishSession(true, "Stage cleared.");
                return;
            }
        } else {
            score = Math.max(0, score - (5 + stageIndex));
            mismatchPenaltySeconds += stage.mismatchTimePenaltySeconds;
            boardLocked = true;
            app.getSoundManager().playMismatch();
            phaseLabel.setText(
                "Wrong match. Time penalty: -" + stage.mismatchTimePenaltySeconds + "s"
            );

            if (checkLoseConditionAfterTurn(stage)) {
                return;
            }

            if (concealTimer != null) {
                concealTimer.stop();
            }

            int delay = app.getSettings().getMismatchDelayMs();
            concealTimer = new Timer(delay, event -> {
                first.setRevealed(false);
                second.setRevealed(false);
                refreshCard(first);
                refreshCard(second);
                boardLocked = false;
                phaseLabel.setText(
                    "Stage " + (stageIndex + 1) + ": keep matching pairs before time or moves run out."
                );
            });
            concealTimer.setRepeats(false);
            concealTimer.start();
        }

        if (checkLoseConditionAfterTurn(stage)) {
            return;
        }
        updateHud();
    }

    private boolean checkLoseConditionAfterTurn(StageConfig stage) {
        if (matchedPairs >= stage.getPairCount()) {
            return false;
        }
        if (moves >= stage.moveLimit) {
            finishSession(false, "Move limit reached.");
            return true;
        }
        if (!previewActive && getRemainingSeconds() <= 0) {
            finishSession(false, "Time ran out.");
            return true;
        }
        return false;
    }

    private void finishSession(boolean cleared, String outcomeMessage) {
        elapsedAtStop = getElapsedSeconds();
        remainingAtStop = getRemainingSeconds();
        sessionRunning = false;
        boardLocked = true;
        previewActive = false;
        stopTimers();
        updateHud();

        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);
        int mistakePenalty = Math.max(0, moves - matchedPairs) * (5 + stageIndex);
        int timeBonus = Math.max(0, remainingAtStop * 3);
        int stageBonus = stageIndex * 38;
        int finalScore = cleared
            ? Math.max(0, score + timeBonus + stageBonus - mistakePenalty)
            : Math.max(0, score - 18);

        GameResult result = new GameResult(
            app.getSettings().getDifficulty(),
            stage.name,
            stageIndex + 1,
            CAMPAIGN_STAGES.size(),
            cleared && stageIndex < CAMPAIGN_STAGES.size() - 1,
            cleared,
            outcomeMessage,
            stage.moveLimit,
            stage.timeLimitSeconds,
            remainingAtStop,
            moves,
            elapsedAtStop,
            finalScore,
            matchedPairs
        );
        if (!cleared) {
            phaseLabel.setText("Game Over. " + outcomeMessage);
        } else if (result.hasNextStage()) {
            phaseLabel.setText("Stage complete. Next stage unlocked.");
        } else {
            phaseLabel.setText("Campaign complete. You cleared every stage.");
        }

        Timer delay = new Timer(650, event -> app.onGameFinished(result));
        delay.setRepeats(false);
        delay.start();
    }

    private void updateLearningPanelVisibility() {
        learningPanel.setVisible(app.getSettings().isShowLearningPanel());
    }

    private void resetLearningText() {
        learningArea.setText(
            "Every stage starts with a short all-card preview.\n"
                + "Use that memory window before the cards hide.\n\n"
                + "Match two identical programming language cards to reveal quick facts.\n\n"
                + "Tip:\n"
                + "- You have limited moves per stage.\n"
                + "- Wrong guess reduces remaining time.\n"
                + "- Observe positions before your second click.\n"
                + "- Fewer mistakes means higher score.\n"
                + "- Faster completion gives time bonus."
        );
        learningArea.setCaretPosition(0);
    }

    private void showLearningFact(LanguageInfo info) {
        learningArea.setText(info.toLearningCardText());
        learningArea.setCaretPosition(0);
    }

    private void refreshCard(CardModel card) {
        CardButton button = cardButtonsById.get(card.getId());
        if (button != null) {
            button.refreshVisual(app.getPalette());
        }
    }

    private void updateMuteButtonText() {
        muteButton.setText(app.getSettings().isMuted() ? "Unmute" : "Mute");
    }

    private void updateHud() {
        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);
        levelValue.setText("L" + (stageIndex + 1));
        timerValue.setText(getRemainingSeconds() + "s");
        movesValue.setText(moves + "/" + stage.moveLimit);
        scoreValue.setText(String.valueOf(score));
        pairsValue.setText(matchedPairs + "/" + stage.getPairCount());
    }

    private int getElapsedSeconds() {
        if (sessionRunning) {
            if (startMillis <= 0L) {
                return 0;
            }
            return (int) ((System.currentTimeMillis() - startMillis) / 1000L);
        }
        return elapsedAtStop;
    }

    private int getRemainingSeconds() {
        StageConfig stage = CAMPAIGN_STAGES.get(stageIndex);
        if (!sessionRunning) {
            return Math.max(0, remainingAtStop);
        }
        int remaining = stage.timeLimitSeconds - getElapsedSeconds() - mismatchPenaltySeconds;
        return Math.max(0, remaining);
    }

    private void stopTimers() {
        if (gameClockTimer != null) {
            gameClockTimer.stop();
        }
        if (concealTimer != null) {
            concealTimer.stop();
        }
        if (previewTimer != null) {
            previewTimer.stop();
        }
    }

    private int getStartStageIndex(GameSettings.Difficulty difficulty) {
        if (difficulty == GameSettings.Difficulty.INTERMEDIATE) {
            return 2;
        }
        if (difficulty == GameSettings.Difficulty.ADVANCED) {
            return 3;
        }
        return 0;
    }
}
