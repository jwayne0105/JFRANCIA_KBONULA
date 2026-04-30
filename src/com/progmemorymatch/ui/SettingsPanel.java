package com.progmemorymatch.ui;

import com.progmemorymatch.MemoryMatchApp;
import com.progmemorymatch.model.GameSettings;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public final class SettingsPanel extends GradientPanel {
    private final MemoryMatchApp app;

    private final JPanel cardPanel = new JPanel(new GridBagLayout());
    private final JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);

    private final JLabel difficultyLabel = new JLabel("Campaign Start Tier");
    private final JLabel styleLabel = new JLabel("Visual Style");
    private final JLabel delayLabel = new JLabel("Mismatch Reveal Delay (ms)");
    private final JLabel volumeLabel = new JLabel("Master Volume");
    private final JLabel campaignModeLabel = new JLabel(
        "<html>Campaign mode is always ON: after each win, the next stage has more cards and less preview time.</html>"
    );

    private final JComboBox<GameSettings.Difficulty> difficultyBox =
        new JComboBox<>(GameSettings.Difficulty.values());
    private final JComboBox<GameSettings.VisualStyle> styleBox =
        new JComboBox<>(GameSettings.VisualStyle.values());
    private final JSlider delaySlider = new JSlider(450, 1800, 900);
    private final JSlider volumeSlider = new JSlider(0, 100, 65);

    private final JLabel delayValueLabel = new JLabel("900 ms");
    private final JLabel volumeValueLabel = new JLabel("65%");

    private final JCheckBox learningCheck = new JCheckBox("Show learning panel during game", true);
    private final JCheckBox muteCheck = new JCheckBox("Mute all sounds", false);

    private final ThreeDButton saveButton = new ThreeDButton("Save Settings");
    private final ThreeDButton backButton = new ThreeDButton("Back To Menu");

    public SettingsPanel(MemoryMatchApp app, ThemePalette palette) {
        super(palette);
        this.app = app;
        setLayout(new GridBagLayout());
        cardPanel.setOpaque(true);
        cardPanel.setBorder(new EmptyBorder(20, 26, 20, 26));
        cardPanel.setPreferredSize(new Dimension(720, 610));

        titleLabel.setFont(new Font("Trebuchet MS", Font.BOLD, 36));
        titleLabel.setBorder(new EmptyBorder(6, 0, 10, 0));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        difficultyLabel.setFont(labelFont);
        styleLabel.setFont(labelFont);
        delayLabel.setFont(labelFont);
        volumeLabel.setFont(labelFont);
        campaignModeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        delaySlider.setMajorTickSpacing(150);
        delaySlider.setMinorTickSpacing(50);
        delaySlider.setPaintTicks(true);
        delaySlider.addChangeListener(event -> delayValueLabel.setText(delaySlider.getValue() + " ms"));

        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.addChangeListener(event -> volumeValueLabel.setText(volumeSlider.getValue() + "%"));

        styleBox.addActionListener(event -> {
            GameSettings.VisualStyle selected = (GameSettings.VisualStyle) styleBox.getSelectedItem();
            if (selected != null) {
                setPalette(ThemePalette.fromStyle(selected));
            }
        });

        saveButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            saveSettings();
            app.openMenu();
        });
        backButton.addActionListener(event -> {
            app.getSoundManager().playButtonClick();
            app.openMenu();
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(8, 8, 8, 8);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        cardPanel.add(titleLabel, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        cardPanel.add(difficultyLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        cardPanel.add(difficultyBox, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 1;
        cardPanel.add(styleLabel, constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 2;
        cardPanel.add(styleBox, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        cardPanel.add(campaignModeLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        cardPanel.add(delayLabel, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        cardPanel.add(delayValueLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        cardPanel.add(delaySlider, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        cardPanel.add(volumeLabel, constraints);
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        cardPanel.add(volumeValueLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        cardPanel.add(volumeSlider, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 3;
        cardPanel.add(learningCheck, constraints);

        constraints.gridy++;
        cardPanel.add(muteCheck, constraints);

        JPanel buttonRow = new JPanel();
        buttonRow.setOpaque(false);
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        buttonRow.add(saveButton);
        buttonRow.add(Box.createHorizontalStrut(10));
        buttonRow.add(backButton);

        constraints.gridy++;
        cardPanel.add(buttonRow, constraints);

        add(cardPanel);
        setPalette(palette);
    }

    public void reloadValuesFromSettings() {
        GameSettings settings = app.getSettings();
        difficultyBox.setSelectedItem(settings.getDifficulty());
        styleBox.setSelectedItem(settings.getVisualStyle());
        delaySlider.setValue(settings.getMismatchDelayMs());
        volumeSlider.setValue(Math.round(settings.getMasterVolume() * 100f));
        learningCheck.setSelected(settings.isShowLearningPanel());
        muteCheck.setSelected(settings.isMuted());
        delayValueLabel.setText(delaySlider.getValue() + " ms");
        volumeValueLabel.setText(volumeSlider.getValue() + "%");
        setPalette(ThemePalette.fromStyle(settings.getVisualStyle()));
    }

    @Override
    public void setPalette(ThemePalette palette) {
        super.setPalette(palette);
        cardPanel.setBackground(palette.panelColor);
        titleLabel.setForeground(palette.textPrimary);

        Color labelColor = palette.textPrimary;
        difficultyLabel.setForeground(labelColor);
        styleLabel.setForeground(labelColor);
        delayLabel.setForeground(labelColor);
        volumeLabel.setForeground(labelColor);
        campaignModeLabel.setForeground(palette.textSecondary);
        delayValueLabel.setForeground(palette.textSecondary);
        volumeValueLabel.setForeground(palette.textSecondary);
        learningCheck.setForeground(palette.textPrimary);
        muteCheck.setForeground(palette.textPrimary);

        learningCheck.setOpaque(false);
        muteCheck.setOpaque(false);

        saveButton.setColors(palette.accentTop, palette.accentBottom);
        backButton.setColors(brighten(palette.panelColor, 1.33f), brighten(palette.panelColor, 0.84f));
    }

    private void saveSettings() {
        GameSettings settings = app.getSettings();
        settings.setDifficulty((GameSettings.Difficulty) difficultyBox.getSelectedItem());
        settings.setVisualStyle((GameSettings.VisualStyle) styleBox.getSelectedItem());
        settings.setMismatchDelayMs(delaySlider.getValue());
        settings.setMasterVolume(volumeSlider.getValue() / 100f);
        settings.setShowLearningPanel(learningCheck.isSelected());
        settings.setMuted(muteCheck.isSelected());
        app.applySettings();
    }

    private Color brighten(Color base, float factor) {
        int red = Math.min(255, Math.round(base.getRed() * factor));
        int green = Math.min(255, Math.round(base.getGreen() * factor));
        int blue = Math.min(255, Math.round(base.getBlue() * factor));
        return new Color(red, green, blue, base.getAlpha());
    }
}
