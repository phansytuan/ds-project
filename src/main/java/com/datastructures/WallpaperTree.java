package com.datastructures;

import javax.swing.*;
import java.awt.*;

public class WallpaperTree extends JPanel {
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color MATRIX_GREEN = new Color(0, 255, 65); // Classic Matrix Green
    private static final int TREE_BASE_Y_OFFSET = 50;
    private static final int TREE_DEPTH = 10;
    private static final double INITIAL_ANGLE_DEGREES = -90;
    private static final double BRANCH_ANGLE_OFFSET_DEGREES = 25;
    private static final double BRANCH_LENGTH_MULTIPLIER = 15.0;
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(MATRIX_GREEN);
        setBackground(BACKGROUND_COLOR);

        drawTree(g2d, getWidth() / 2, getHeight() - TREE_BASE_Y_OFFSET, INITIAL_ANGLE_DEGREES, TREE_DEPTH);
    }

    private void drawTree(Graphics2D g, int x, int y, double angle, int depth) {
        if (depth == 0) return;

        int x2 = x + (int) (Math.cos(Math.toRadians(angle)) * depth * BRANCH_LENGTH_MULTIPLIER);
        int y2 = y + (int) (Math.sin(Math.toRadians(angle)) * depth * BRANCH_LENGTH_MULTIPLIER);

        g.setStroke(new BasicStroke(depth));
        g.drawLine(x, y, x2, y2);

        drawTree(g, x2, y2, angle - BRANCH_ANGLE_OFFSET_DEGREES, depth - 1);
        drawTree(g, x2, y2, angle + BRANCH_ANGLE_OFFSET_DEGREES, depth - 1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Aesthetic");
        frame.add(new WallpaperTree());
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
