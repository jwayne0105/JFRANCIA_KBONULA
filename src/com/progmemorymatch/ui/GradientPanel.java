package com.progmemorymatch.ui;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;

public class GradientPanel extends JPanel {
    private ThemePalette palette;

    public GradientPanel(ThemePalette palette) {
        this.palette = palette;
        setOpaque(false);
    }

    public void setPalette(ThemePalette palette) {
        this.palette = palette;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(new GradientPaint(0, 0, palette.gradientStart, getWidth(), getHeight(), palette.gradientEnd));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
        super.paintComponent(graphics);
    }
}
