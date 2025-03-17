package log;

import org.junit.jupiter.api.Test;
import gui.LogWindow;

import static org.junit.jupiter.api.Assertions.*;

class TestLogs {

    // 1. Проверка ограничения количества сообщений
    @Test
    void testLogQueueSizeLimit() {
        LogWindowSource logSource = new LogWindowSource(3);

        logSource.append(LogLevel.Debug, "Сообщение 1");
        logSource.append(LogLevel.Debug, "Сообщение 2");
        logSource.append(LogLevel.Debug, "Сообщение 3");
        logSource.append(LogLevel.Debug, "Сообщение 4");

        assertEquals(3, logSource.size());

        Iterable<LogEntry> logs = logSource.all();
        for (LogEntry entry : logs) {
            assertNotEquals("Сообщение 1", entry.getMessage());
        }
    }

    // 2. Проверка на отсутствие утечки ресурсов
    @Test
    void testListenerUnregisterOnDispose() {
        LogWindowSource logSource = new LogWindowSource(4);

        // проверка, что слушатель добавляется
        LogWindow logWindow = new LogWindow(logSource);
        assertEquals(1, logSource.m_listeners.size());

        logWindow.dispose();

        // проверка, что слушатель отвязывается
        assertEquals(0, logSource.m_listeners.size());
    }
}
