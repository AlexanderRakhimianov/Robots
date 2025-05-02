package gui;

import util.ConfirmCloseDialog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseFrame extends JFrame {
    protected final ConfirmCloseDialog closeHelper = new ConfirmCloseDialog();

    public BaseFrame(String title) {
        super(title);
        initializeClosingBehaviorForFrame();
    }

    protected abstract void saveWindowProfile();

    // поведение закрытия для JFrame
    private void initializeClosingBehaviorForFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmClose()) {
                    saveWindowProfile();
                    System.exit(0);
                }
            }
        });
    }

    protected boolean confirmClose(){
        return closeHelper.confirmClose(this);
    };
}
