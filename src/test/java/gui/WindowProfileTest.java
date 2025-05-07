package gui;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WindowProfileTest {
    @Test
    void testAddAndGetWindowStates() {
        WindowProfile profile = new WindowProfile();
        WindowState state1 = new WindowState("LogWindow", 10, 20, 300, 400, true, false, false);
        WindowState state2 = new WindowState("GameWindow", 50, 60, 500, 600, true, true, false);

        profile.addWindowState(state1);
        profile.addWindowState(state2);

        List<WindowState> states = profile.getWindowStates();
        assertEquals(2, states.size());
        assertTrue(states.contains(state1));
    }
}
