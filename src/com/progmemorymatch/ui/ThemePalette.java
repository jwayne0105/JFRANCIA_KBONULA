package com.progmemorymatch.ui;

import com.progmemorymatch.model.GameSettings;

import java.awt.Color;

public final class ThemePalette {
    public final Color gradientStart;
    public final Color gradientEnd;
    public final Color panelColor;
    public final Color cardBackTop;
    public final Color cardBackBottom;
    public final Color accentTop;
    public final Color accentBottom;
    public final Color textPrimary;
    public final Color textSecondary;

    private ThemePalette(
        Color gradientStart,
        Color gradientEnd,
        Color panelColor,
        Color cardBackTop,
        Color cardBackBottom,
        Color accentTop,
        Color accentBottom,
        Color textPrimary,
        Color textSecondary
    ) {
        this.gradientStart = gradientStart;
        this.gradientEnd = gradientEnd;
        this.panelColor = panelColor;
        this.cardBackTop = cardBackTop;
        this.cardBackBottom = cardBackBottom;
        this.accentTop = accentTop;
        this.accentBottom = accentBottom;
        this.textPrimary = textPrimary;
        this.textSecondary = textSecondary;
    }

    public static ThemePalette fromStyle(GameSettings.VisualStyle style) {
        if (style == GameSettings.VisualStyle.ARCADE) {
            return new ThemePalette(
                new Color(116, 79, 54),
                new Color(64, 40, 29),
                new Color(129, 83, 59, 220),
                new Color(244, 212, 164),
                new Color(206, 151, 96),
                new Color(136, 239, 76),
                new Color(78, 190, 34),
                new Color(255, 244, 222),
                new Color(241, 210, 171)
            );
        }
        if (style == GameSettings.VisualStyle.COBALT) {
            return new ThemePalette(
                new Color(18, 28, 64),
                new Color(10, 9, 25),
                new Color(30, 39, 81, 220),
                new Color(44, 58, 109),
                new Color(24, 34, 74),
                new Color(93, 140, 255),
                new Color(48, 94, 220),
                new Color(244, 249, 255),
                new Color(182, 198, 231)
            );
        }
        if (style == GameSettings.VisualStyle.SUNSET) {
            return new ThemePalette(
                new Color(94, 35, 72),
                new Color(36, 19, 43),
                new Color(122, 57, 77, 210),
                new Color(164, 95, 91),
                new Color(116, 62, 66),
                new Color(255, 139, 72),
                new Color(208, 87, 35),
                new Color(255, 245, 237),
                new Color(255, 215, 197)
            );
        }
        return new ThemePalette(
            new Color(16, 47, 42),
            new Color(6, 22, 20),
            new Color(23, 74, 62, 210),
            new Color(41, 94, 82),
            new Color(22, 56, 50),
            new Color(88, 220, 162),
            new Color(40, 165, 116),
            new Color(236, 255, 248),
            new Color(167, 214, 197)
        );
    }
}
