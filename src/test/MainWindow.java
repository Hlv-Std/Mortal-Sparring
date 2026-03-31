package test;

import view.GameWindow;

import javax.swing.*;

public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}
