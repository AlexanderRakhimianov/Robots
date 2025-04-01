package util;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class ConfirmCloseDialog implements InternalFrameListener {

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        JInternalFrame frame = (JInternalFrame) e.getSource();
        if (confirmClose(frame)) {
            frame.dispose();
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    public boolean confirmClose(JComponent parentComponent) {
        return showConfirmationDialog(parentComponent, "Вы действительно хотите закрыть окно?", "Подтверждение закрытия");
    }

    public boolean confirmClose(JFrame parentFrame) {
        return showConfirmationDialog(parentFrame, "Вы действительно хотите закрыть приложение?", "Подтверждение выхода");
    }

    public boolean showConfirmationDialog(Object parent, String message, String title) {
        int option = JOptionPane.showConfirmDialog(
                parent instanceof JComponent ? (JComponent) parent : null,
                message,
                title,
                JOptionPane.YES_NO_OPTION
        );
        return option == JOptionPane.YES_OPTION;

    }
}