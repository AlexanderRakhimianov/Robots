package gui;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;

import util.ConfirmCloseDialog;

import static org.mockito.Mockito.*;

public class ConfirmCloseDialogTest {

    private JInternalFrame mockFrame;
    private ConfirmCloseDialog closeHelper;

    @Before
    public void setUp() {
        mockFrame = Mockito.mock(JInternalFrame.class);
        closeHelper = new ConfirmCloseDialog();
    }

    // Тест закрытия окна при выборе "Да"
    @Test
    public void testInternalFrameClosing_YesOption() {
        try (MockedStatic<JOptionPane> mockedOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedOptionPane.when(() -> JOptionPane.showConfirmDialog(
                    eq(mockFrame),
                    eq("Вы действительно хотите закрыть окно?"),
                    eq("Подтверждение закрытия"),
                    eq(JOptionPane.YES_NO_OPTION)
            )).thenReturn(JOptionPane.YES_OPTION);

            closeHelper.internalFrameClosing(new InternalFrameEvent(mockFrame, InternalFrameEvent.INTERNAL_FRAME_CLOSING));

            verify(mockFrame, times(1)).dispose();
        }
    }

    // Тест не закрытия окна при выборе "Нет"
    @Test
    public void testInternalFrameClosing_NoOption() {
        try (MockedStatic<JOptionPane> mockedOptionPane = Mockito.mockStatic(JOptionPane.class)) {
            mockedOptionPane.when(() -> JOptionPane.showConfirmDialog(
                    eq(mockFrame),
                    eq("Вы действительно хотите закрыть окно?"),
                    eq("Подтверждение закрытия"),
                    eq(JOptionPane.YES_NO_OPTION)
            )).thenReturn(JOptionPane.NO_OPTION);

            closeHelper.internalFrameClosing(new InternalFrameEvent(mockFrame, InternalFrameEvent.INTERNAL_FRAME_CLOSING));

            verify(mockFrame, never()).dispose();
        }
    }
}