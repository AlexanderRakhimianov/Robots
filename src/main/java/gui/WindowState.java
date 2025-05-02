package gui;
import java.io.Serializable;


class WindowState implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String windowType;
    private final int x, y, width, height;
    private final boolean isVisible, isIconified;

    public WindowState(String windowType, int x, int y, int width, int height, boolean isVisible, boolean isIconified) {
        this.windowType = windowType;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isVisible = isVisible;
        this.isIconified = isIconified;
    }

    public String getWindowType() { return windowType; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isVisible() { return isVisible; }
    public boolean isIconified() { return isIconified; }
}
