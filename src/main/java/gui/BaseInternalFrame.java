package gui;

import util.ConfirmCloseDialog;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public abstract class BaseInternalFrame extends JInternalFrame {
    private int normalX;
    private int normalY;
    private int normalWidth;
    private int normalHeight;

    protected final ConfirmCloseDialog closeHelper = new ConfirmCloseDialog();

    public BaseInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        initializeClosingBehavior();
        setupBoundsListener();

        saveNormalBounds();
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

    private void setupBoundsListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                saveNormalBounds();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                saveNormalBounds();
            }
        });
    }

    private void saveNormalBounds() {
        if (!isMaximum()) {
            normalX = getX();
            normalY = getY();
            normalWidth = getWidth();
            normalHeight = getHeight();
        }
    }

    public int getNormalX() { return normalX; }
    public int getNormalY() { return normalY; }
    public int getNormalWidth() { return normalWidth; }
    public int getNormalHeight() { return normalHeight; }
}