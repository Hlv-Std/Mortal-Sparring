package test;

import view.GameWindow;

import javax.swing.*;

// TODO: Move out main from test classes
public class MainWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}
