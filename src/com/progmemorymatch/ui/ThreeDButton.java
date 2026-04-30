package com.progmemorymatch.ui;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class ThreeDButton extends JButton {
    private Color topColor = new Color(248, 196, 110);
    private Color bottomColor = new Color(213, 127, 50);
    private Color shadowColor = new Color(58, 29, 12, 100);
    private Color borderColor = new Color(255, 230, 182, 200);
    private int radius = 26;

    public ThreeDButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(10, 20, 10, 20));
    }

    public void setColors(Color topColor, Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        repaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int shadowOffset = getModel().isPressed() ? 2 : 5;
        int pressOffset = getModel().isPressed() ? 2 : 0;

        g2.setColor(shadowColor);
        g2.fillRoundRect(0, shadowOffset, width - 1, height - shadowOffset - 1, radius, radius);

        Color drawTop = getModel().isRollover() ? brighten(topColor, 1.08f) : topColor;
        Color drawBottom = getModel().isRollover() ? brighten(bottomColor, 1.08f) : bottomColor;
        g2.setPaint(new GradientPaint(0, pressOffset, drawTop, 0, height, drawBottom));
        g2.fillRoundRect(0, pressOffset, width - 1, height - shadowOffset - 1, radius, radius);

        g2.setColor(new Color(255, 251, 228, 90));
        g2.fillRoundRect(3, pressOffset + 3, width - 7, Math.max(13, height / 2 - 1), radius - 10, radius - 10);

        g2.setColor(new Color(95, 51, 28, 55));
        g2.drawRoundRect(2, pressOffset + height / 2 - 2, width - 6, Math.max(10, height / 2 - 2), radius - 8, radius - 8);

        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, pressOffset, width - 2, height - shadowOffset - 2, radius, radius);

        g2.dispose();
        super.paintComponent(graphics);
    }

    private Color brighten(Color base, float factor) {
        int red = Math.min(255, Math.round(base.getRed() * factor));
        int green = Math.min(255, Math.round(base.getGreen() * factor));
        int blue = Math.min(255, Math.round(base.getBlue() * factor));
        return new Color(red, green, blue, base.getAlpha());
    }
}
