package Texture;

public class Texture {
    private final int id;
    private final int width;
    private final int height;
    private final String name;

    public Texture(int id, int width, int height, String name) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.name = name;
    }
    // Getters...
    public int getId() { return id; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getName() { return name; }
}
