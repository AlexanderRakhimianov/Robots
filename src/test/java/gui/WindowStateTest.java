package gui;

import org.junit.jupiter.api.Test;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class WindowStateTest {
    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        // Подготовка
        WindowState original = new WindowState("TestWindow", 100, 200, 300, 400, true, false, false);

        // Сериализация
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(original);
        oos.close();

        // Десериализация
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        WindowState deserialized = (WindowState) ois.readObject();

        // Проверка
        assertEquals(original.getWindowType(), deserialized.getWindowType());
        assertEquals(original.getX(), deserialized.getX());
        assertEquals(original.isIconified(), deserialized.isIconified());
    }
}
