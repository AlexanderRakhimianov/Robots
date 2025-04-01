package gui;

import util.ConfirmCloseDialog;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public abstract class BaseInternalFrame extends JInternalFrame {
    protected final ConfirmCloseDialog closeHelper = new ConfirmCloseDialog();

    public BaseInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        initializeClosingBehavior();
    }

    // поведение закрытия для JInternalFrame
    private void initializeClosingBehavior() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (confirmClose()) {
                    dispose();
                }
            }
        });
    }

    protected boolean confirmClose(){
        return closeHelper.showConfirmationDialog(this, "Вы действительно хотите закрыть окно?", "Подтверждение закрытия");
    };
}
