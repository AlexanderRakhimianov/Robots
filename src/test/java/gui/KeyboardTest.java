package gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class KeyboardTest {
    private GameVisualizer gameVisualizer;

    @BeforeEach
    void setUp() {
        gameVisualizer = new GameVisualizer();
        gameVisualizer.setSize(800, 600);
    }

    private KeyEvent createKeyEvent(int keyCode) {
        return new KeyEvent(gameVisualizer, KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(), 0, keyCode, (char)keyCode);
    }

    @Test
    void testKeyPressSetsFlagsCorrectly() {
        // Симулируем нажатие клавиш через handleKeyPress
        gameVisualizer.handleKeyPress(KeyEvent.VK_W, true);
        assertTrue(gameVisualizer.isWPressed());

        gameVisualizer.handleKeyPress(KeyEvent.VK_A, true);
        assertTrue(gameVisualizer.isAPressed());

        gameVisualizer.handleKeyPress(KeyEvent.VK_S, true);
        assertTrue(gameVisualizer.isSPressed());

        gameVisualizer.handleKeyPress(KeyEvent.VK_D, true);
        assertTrue(gameVisualizer.isDPressed());
    }

    @Test
    void testKeyReleaseResetsFlags() {
        gameVisualizer.handleKeyPress(KeyEvent.VK_W, true);
        gameVisualizer.handleKeyPress(KeyEvent.VK_W, false);
        assertFalse(gameVisualizer.isWPressed());
    }

    @Test
    void testRobotMovesForwardWhenWPressed() {
        double initialX = gameVisualizer.getRobotPositionX();
        double initialY = gameVisualizer.getRobotPositionY();

        gameVisualizer.m_robotDirection = Math.PI/4;

        gameVisualizer.handleKeyPress(KeyEvent.VK_W, true);
        for (int i = 0; i < 10; i++) {
            gameVisualizer.handleKeyboardControl();
        }
        assertNotEquals(initialX, gameVisualizer.getRobotPositionX(),
                "Robot X position should change when moving forward");
        assertNotEquals(initialY, gameVisualizer.getRobotPositionY(),
                "Robot Y position should change when moving forward");
    }
}
