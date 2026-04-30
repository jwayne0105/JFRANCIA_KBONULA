package com.progmemorymatch.ui;

import com.progmemorymatch.model.CardModel;
import com.progmemorymatch.model.LanguageInfo;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public final class CardButton extends ThreeDButton {
    private final CardModel card;

    public CardButton(CardModel card) {
        super("?");
        this.card = card;
        setFont(new Font("Segoe UI", Font.BOLD, 20));
        setRadius(24);
        setFocusPainted(false);
        setHorizontalTextPosition(CENTER);
        setVerticalTextPosition(BOTTOM);
        renderHiddenState();
    }

    public CardModel getCard() {
        return card;
    }

    public void refreshVisual(ThemePalette palette) {
        if (card.isMatched()) {
            setEnabled(false);
            renderRevealedState(card.getLanguage(), true);
        } else if (card.isRevealed()) {
            setEnabled(true);
            renderRevealedState(card.getLanguage(), false);
        } else {
            setEnabled(true);
            renderHiddenState();
            setColors(palette.cardBackTop, palette.cardBackBottom);
            setForeground(new Color(239, 252, 248));
        }
    }

    private void renderHiddenState() {
        setText("?");
        setIcon(null);
        setToolTipText("Hidden tile");
    }

    private void renderRevealedState(LanguageInfo language, boolean matched) {
        String name = language.getName();
        setText("<html><center>" + name + "</center></html>");
        setIcon(createBadgeIcon(language));
        setToolTipText(language.getSummary());
        Color base = language.getColor();
        Color top = brighten(base, matched ? 1.28f : 1.16f);
        Color bottom = darken(base, matched ? 0.72f : 0.84f);
        setColors(top, bottom);
        setForeground(Color.WHITE);
    }

    private ImageIcon createBadgeIcon(LanguageInfo language) {
        int size = 36;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = language.getColor();
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(1, 2, size - 3, size - 3);
        g2.setColor(color);
        g2.fillOval(0, 0, size - 3, size - 3);
        g2.setColor(new Color(255, 255, 255, 70));
        g2.fillOval(2, 2, size - 7, (size - 7) / 2);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawOval(0, 0, size - 3, size - 3);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String badge = language.getBadge();
        int textWidth = g2.getFontMetrics().stringWidth(badge);
        int x = ((size - 3) - textWidth) / 2;
        int y = (size / 2) + 4;
        g2.drawString(badge, x, y);
        g2.dispose();
        return new ImageIcon(image);
    }

    private Color brighten(Color base, float factor) {
        int r = Math.min(255, Math.round(base.getRed() * factor));
        int g = Math.min(255, Math.round(base.getGreen() * factor));
        int b = Math.min(255, Math.round(base.getBlue() * factor));
        return new Color(r, g, b);
    }

    private Color darken(Color base, float factor) {
        int r = Math.max(0, Math.round(base.getRed() * factor));
        int g = Math.max(0, Math.round(base.getGreen() * factor));
        int b = Math.max(0, Math.round(base.getBlue() * factor));
        return new Color(r, g, b);
    }
}
