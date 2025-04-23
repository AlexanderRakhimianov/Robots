package gui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class KeyboardTest {
    private Robot robot;

    @BeforeEach
    void setUp() {
        robot = new Robot(new GameVisualizer());
    }

    @Test
    void testKeyPressSetsFlagsCorrectly() {
        // Симулируем нажатие клавиш через handleKeyPress
        robot.handleKeyPress(KeyEvent.VK_W, true);
        assertTrue(robot.wPressed);

        robot.handleKeyPress(KeyEvent.VK_A, true);
        assertTrue(robot.aPressed);

        robot.handleKeyPress(KeyEvent.VK_S, true);
        assertTrue(robot.sPressed);

        robot.handleKeyPress(KeyEvent.VK_D, true);
        assertTrue(robot.dPressed);
    }

    @Test
    void testKeyReleaseResetsFlags() {
        robot.handleKeyPress(KeyEvent.VK_W, true);
        robot.handleKeyPress(KeyEvent.VK_W, false);
        assertFalse(robot.wPressed);
    }

    @Test
    void testRobotMovesForwardWhenWPressed() {
        double initialX = robot.positionX;
        double initialY = robot.positionY;;

        robot.direction = Math.PI/4;

        robot.handleKeyPress(KeyEvent.VK_W, true);
        for (int i = 0; i < 10; i++) {
            robot.handleKeyboardControl();
        }
        assertNotEquals(initialX, robot.positionX,
                "Robot X position should change when moving forward");
        assertNotEquals(initialY, robot.positionY,
                "Robot Y position should change when moving forward");
    }
}