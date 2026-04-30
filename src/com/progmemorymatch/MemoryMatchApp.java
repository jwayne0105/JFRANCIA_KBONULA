package com.progmemorymatch;

import com.progmemorymatch.audio.SoundManager;
import com.progmemorymatch.model.GameResult;
import com.progmemorymatch.model.GameSettings;
import com.progmemorymatch.ui.GameOverPanel;
import com.progmemorymatch.ui.GamePanel;
import com.progmemorymatch.ui.MainMenuPanel;
import com.progmemorymatch.ui.SettingsPanel;
import com.progmemorymatch.ui.ThemePalette;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class MemoryMatchApp extends JFrame {
    private static final String SCREEN_MENU = "menu";
    private static final String SCREEN_SETTINGS = "settings";
    private static final String SCREEN_GAME = "game";
    private static final String SCREEN_GAME_OVER = "game_over";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenRoot = new JPanel(cardLayout);

    private final GameSettings settings = new GameSettings();
    private final SoundManager soundManager = new SoundManager();
    private ThemePalette palette = ThemePalette.fromStyle(settings.getVisualStyle());

    private final MainMenuPanel mainMenuPanel;
    private final SettingsPanel settingsPanel;
    private final GamePanel gamePanel;
    private final GameOverPanel gameOverPanel;

    public MemoryMatchApp() {
        super("Byte Quest: Memory Forge");
        setMinimumSize(new Dimension(1240, 760));
        setSize(1280, 800);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        mainMenuPanel = new MainMenuPanel(this, palette);
        settingsPanel = new SettingsPanel(this, palette);
        gamePanel = new GamePanel(this, palette);
        gameOverPanel = new GameOverPanel(this, palette);

        screenRoot.add(mainMenuPanel, SCREEN_MENU);
        screenRoot.add(settingsPanel, SCREEN_SETTINGS);
        screenRoot.add(gamePanel, SCREEN_GAME);
        screenRoot.add(gameOverPanel, SCREEN_GAME_OVER);
        setContentPane(screenRoot);
        setLocationRelativeTo(null);

        soundManager.applySettings(settings);
        soundManager.startBackgroundMusic();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                shutdownAndExit();
            }
        });

        openMenu();
    }

    public GameSettings getSettings() {
        return settings;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public ThemePalette getPalette() {
        return palette;
    }

    public void openMenu() {
        refreshPalette();
        gamePanel.stopSession();
        mainMenuPanel.refreshFromState();
        cardLayout.show(screenRoot, SCREEN_MENU);
    }

    public void openSettings() {
        refreshPalette();
        settingsPanel.reloadValuesFromSettings();
        cardLayout.show(screenRoot, SCREEN_SETTINGS);
    }

    public void startGame() {
        refreshPalette();
        gamePanel.startCampaign();
        cardLayout.show(screenRoot, SCREEN_GAME);
    }

    public void startNextStage() {
        refreshPalette();
        gamePanel.startNextStageOrRestartCampaign();
        cardLayout.show(screenRoot, SCREEN_GAME);
    }

    public void retryCurrentStage() {
        refreshPalette();
        gamePanel.retryCurrentStage();
        cardLayout.show(screenRoot, SCREEN_GAME);
    }

    public void onGameFinished(GameResult result) {
        refreshPalette();
        gameOverPanel.showResult(result);
        cardLayout.show(screenRoot, SCREEN_GAME_OVER);
    }

    public void applySettings() {
        soundManager.applySettings(settings);
        refreshPalette();
    }

    private void refreshPalette() {
        palette = ThemePalette.fromStyle(settings.getVisualStyle());
        mainMenuPanel.setPalette(palette);
        settingsPanel.setPalette(palette);
        gamePanel.setPalette(palette);
        gameOverPanel.setPalette(palette);
    }

    private void shutdownAndExit() {
        gamePanel.stopSession();
        soundManager.shutdown();
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryMatchApp app = new MemoryMatchApp();
            app.setVisible(true);
        });
    }
}
