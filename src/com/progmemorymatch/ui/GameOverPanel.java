package com.progmemorymatch.ui;

import com.progmemorymatch.MemoryMatchApp;
import com.progmemorymatch.model.GameResult;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public final class GameOverPanel extends GradientPanel {
    private final MemoryMatchApp app;
    private GameResult lastResult;
    private final JPanel cardPanel = new JPanel();
    private final JLabel headlineLabel = new JLabel("STAGE CLEARED", SwingConstants.CENTER);
    private final JLabel stageLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel difficultyLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    private final JLabel timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);
    private final JLabel movesLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
    private final JLabel pairsLabel = new JLabel("Pairs: 0", SwingConstants.CENTER);
    private final JLabel rankLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel nextLabel = new JLabel("", SwingConstants.CENTER);

    private final ThreeDButton playAgainButton = new ThreeDButton("Next Level");
    private final ThreeDButton menuButton = new ThreeDButton("Main Menu");

    public GameOverPanel(MemoryMatchApp app, ThemePalette palette) {
        super(palette);
        this.app = app;
        setLayout(new GridBagLayout());

        cardPanel.setOpaque(true);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        cardPanel.setPreferredSize(new Dimension(620, 520));

        headlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headlineLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 44));
        stageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stageLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        movesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        movesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        pairsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pairsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setPreferredSize(new Dimension(260, 54));
        menuButton.setPreferredSize(new Dimension(260, 54));

        playAgainButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            if (lastResult != null && !lastResult.isCleared()) {
                app.retryCurrentStage();
            } else {
                app.startNextStage();
            }
        });
        menuButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            app.openMenu();
        });

        cardPanel.add(headlineLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(stageLabel);
        cardPanel.add(Box.createVerticalStrut(6));
        cardPanel.add(difficultyLabel);
        cardPanel.add(Box.createVerticalStrut(16));
        cardPanel.add(scoreLabel);
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(timeLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(movesLabel);
        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(pairsLabel);
        cardPanel.add(Box.createVerticalStrut(14));
        cardPanel.add(rankLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(nextLabel);
        cardPanel.add(Box.createVerticalStrut(26));
        cardPanel.add(playAgainButton);
        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(menuButton);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 12, 12, 12);
        add(cardPanel, constraints);

        setPalette(palette);
    }

    public void showResult(GameResult result) {
        lastResult = result;
        stageLabel.setText("Stage " + result.getStageNumber() + " / " + result.getTotalStages()
            + " - " + result.getStageName());
        difficultyLabel.setText("Start Tier: " + result.getDifficulty().getLabel());
        scoreLabel.setText("Score: " + result.getScore());
        timeLabel.setText("Time Left: " + result.getRemainingSeconds() + "s / " + result.getTimeLimitSeconds() + "s");
        movesLabel.setText("Moves: " + result.getMoves() + " / " + result.getMoveLimit());
        pairsLabel.setText("Pairs Matched: " + result.getMatchedPairs());
        rankLabel.setText("Rank: " + scoreRank(result.getScore()));
        if (!result.isCleared()) {
            headlineLabel.setText("GAME OVER");
            playAgainButton.setText("Retry Stage");
            nextLabel.setText(result.getOutcomeMessage());
        } else if (result.hasNextStage()) {
            headlineLabel.setText("STAGE CLEARED");
            playAgainButton.setText("Next Level");
            nextLabel.setText("Great run! Next stage is harder.");
        } else {
            headlineLabel.setText("CAMPAIGN COMPLETE");
            playAgainButton.setText("Restart Campaign");
            nextLabel.setText("You finished all stages. Start again for a new shuffle.");
        }
    }

    @Override
    public void setPalette(ThemePalette palette) {
        super.setPalette(palette);
        cardPanel.setBackground(palette.panelColor);
        headlineLabel.setForeground(palette.textPrimary);
        stageLabel.setForeground(palette.accentTop);
        difficultyLabel.setForeground(palette.textSecondary);
        scoreLabel.setForeground(palette.textPrimary);
        timeLabel.setForeground(palette.textSecondary);
        movesLabel.setForeground(palette.textSecondary);
        pairsLabel.setForeground(palette.textSecondary);
        rankLabel.setForeground(palette.accentTop);
        nextLabel.setForeground(palette.textSecondary);

        playAgainButton.setColors(palette.accentTop, palette.accentBottom);
        menuButton.setColors(new Color(84, 193, 244), new Color(43, 134, 211));
    }

    private String scoreRank(int score) {
        if (score >= 520) {
            return "Code Master";
        }
        if (score >= 420) {
            return "Bug Hunter";
        }
        if (score >= 300) {
            return "Logic Builder";
        }
        return "Learning Coder";
    }
}
