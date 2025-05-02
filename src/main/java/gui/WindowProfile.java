package gui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class WindowProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<WindowState> windowStates = new ArrayList<>();

    public void addWindowState(WindowState state) {
        windowStates.add(state);
    }

    public List<WindowState> getWindowStates() {
        return new ArrayList<>(windowStates);
    }
}

