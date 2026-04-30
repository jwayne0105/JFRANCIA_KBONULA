package com.progmemorymatch.ui;

import com.progmemorymatch.MemoryMatchApp;
import com.progmemorymatch.model.GameSettings;

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

public final class MainMenuPanel extends GradientPanel {
    private final MemoryMatchApp app;
    private final JPanel centerPanel = new JPanel();
    private final JLabel subtitleLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel hintLabel = new JLabel(
        "<html><center>All cards are revealed first for a short preview.<br>"
            + "Win a stage to unlock a harder grid.</center></html>"
    );
    private final ThreeDButton playButton = new ThreeDButton("Start Campaign");
    private final ThreeDButton settingsButton = new ThreeDButton("Settings");
    private final ThreeDButton muteButton = new ThreeDButton("Mute");
    private final ThreeDButton exitButton = new ThreeDButton("Exit");

    public MainMenuPanel(MemoryMatchApp app, ThemePalette palette) {
        super(palette);
        this.app = app;
        setLayout(new GridBagLayout());

        centerPanel.setOpaque(true);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(28, 40, 28, 40));
        centerPanel.setMaximumSize(new Dimension(560, 580));

        JLabel titleLabel = new JLabel("<html><center>BYTE QUEST<br>MEMORY FORGE</center></html>", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 44));
        titleLabel.setForeground(new Color(255, 241, 214));

        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        muteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        playButton.setPreferredSize(new Dimension(280, 56));
        settingsButton.setPreferredSize(new Dimension(280, 56));
        muteButton.setPreferredSize(new Dimension(280, 56));
        exitButton.setPreferredSize(new Dimension(280, 56));

        playButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            app.startGame();
        });
        settingsButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            app.openSettings();
        });
        muteButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            boolean muted = !app.getSettings().isMuted();
            app.getSettings().setMuted(muted);
            app.getSoundManager().setMuted(muted);
            refreshFromState();
        });
        exitButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            System.exit(0);
        });

        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalStrut(16));
        centerPanel.add(hintLabel);
        centerPanel.add(Box.createVerticalStrut(34));
        centerPanel.add(playButton);
        centerPanel.add(Box.createVerticalStrut(14));
        centerPanel.add(settingsButton);
        centerPanel.add(Box.createVerticalStrut(14));
        centerPanel.add(muteButton);
        centerPanel.add(Box.createVerticalStrut(14));
        centerPanel.add(exitButton);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(12, 12, 12, 12);
        add(centerPanel, constraints);

        setPalette(palette);
    }

    public void refreshFromState() {
        GameSettings settings = app.getSettings();
        subtitleLabel.setText(
            "Start Tier: " + settings.getDifficulty().getLabel()
                + "   |   Theme: " + settings.getVisualStyle().getLabel()
        );
        muteButton.setText(settings.isMuted() ? "Unmute" : "Mute");
    }

    @Override
    public void setPalette(ThemePalette palette) {
        super.setPalette(palette);
        centerPanel.setBackground(palette.panelColor);
        setForeground(palette.textPrimary);
        subtitleLabel.setForeground(palette.textSecondary);
        hintLabel.setForeground(palette.textSecondary);

        playButton.setColors(palette.accentTop, palette.accentBottom);
        settingsButton.setColors(new Color(84, 193, 244), new Color(43, 134, 211));
        muteButton.setColors(new Color(120, 149, 229), new Color(84, 111, 191));
        exitButton.setColors(new Color(245, 118, 89), new Color(201, 78, 44));
        refreshFromState();
    }
}
