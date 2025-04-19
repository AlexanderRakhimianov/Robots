package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import static org.junit.jupiter.api.Assertions.*;

public class ConfirmCloseDialogTest {
    private TestFrame testFrame;

    // Простой фрейм для тестирования
    static class TestFrame extends JInternalFrame {
        boolean disposed = false;

        @Override
        public void dispose() {
            disposed = true;
        }
    }

    @BeforeEach
    public void setUp() {
        testFrame = new TestFrame();
    }

    @Test
    public void testInternalFrameClosing_YesOption() {
        // Подменяем JOptionPane через наследование
        ConfirmCloseDialog customDialog = new ConfirmCloseDialog() {
            @Override
            public boolean showConfirmationDialog(Object parent, String message, String title) {
                return true; // Всегда отвечаем "Да"
            }
        };

        customDialog.internalFrameClosing(new InternalFrameEvent(testFrame, InternalFrameEvent.INTERNAL_FRAME_CLOSING));

        assertTrue(testFrame.disposed, "Окно должно закрыться при выборе 'Да'");
    }

    @Test
    public void testInternalFrameClosing_NoOption() {
        ConfirmCloseDialog customDialog = new ConfirmCloseDialog() {
            @Override
            public boolean showConfirmationDialog(Object parent, String message, String title) {
                return false; // Всегда отвечаем "Нет"
            }
        };

        customDialog.internalFrameClosing(new InternalFrameEvent(testFrame, InternalFrameEvent.INTERNAL_FRAME_CLOSING));

        assertFalse(testFrame.disposed, "Окно не должно закрыться при выборе 'Нет'");
    }

    @Test
    public void testConfirmCloseWithJComponent() {
        JPanel panel = new JPanel();
        ConfirmCloseDialog customDialog = new ConfirmCloseDialog() {
            @Override
            public boolean showConfirmationDialog(Object parent, String message, String title) {
                assertEquals(panel, parent, "Родительский компонент должен быть передан правильно");
                return true;
            }
        };

        assertTrue(customDialog.confirmClose(panel), "Должен вернуться true при подтверждении");
    }

    @Test
    public void testConfirmCloseWithJFrame() {
        JFrame frame = new JFrame();
        ConfirmCloseDialog customDialog = new ConfirmCloseDialog() {
            @Override
            public boolean showConfirmationDialog(Object parent, String message, String title) {
                assertEquals(frame, parent, "Родительский фрейм должен быть передан правильно");
                return true;
            }
        };

        assertTrue(customDialog.confirmClose(frame), "Должен вернуться true при подтверждении");
    }
}
