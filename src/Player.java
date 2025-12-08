import Texture.Texture;
import javax.media.opengl.GL;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private float x, y, width, height;
    private Sprite sprite;
    private List<Texture> skins = new ArrayList<>();
    private int currIndex = 0;
    private float scale;
    // إضافة المتغير الجديد لاتجاه اللاعب
    private boolean isFacingRight = true;

    public Player(float x, float y, float width, float height, float scale) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
        // نمرر الاتجاه الأولي (يمين) عند إنشاء الـ Sprite
        this.sprite = new Sprite(null, x, y, width * scale, height * scale, isFacingRight);
    }

    public void loadSkins(GL gl, TextureManager manager, String[] skinNames) {
        // ... (هذا الجزء كما هو) ...
        for (String name : skinNames) {
            Texture tex = manager.getTexture(gl, name);
            if (tex != null) {
                skins.add(tex);
            }
        }
        if (!skins.isEmpty()) {
            sprite.setTexture(skins.get(0));
        }
    }

    public void draw(GL gl) {
        // تحديث الموقع والحجم وأيضاً الاتجاه في الـ Sprite قبل الرسم
        sprite.setPosition(x, y);
        sprite.setSize(width * scale, height * scale);
        sprite.setFacingRight(isFacingRight); // تمرير الاتجاه
        sprite.draw(gl);
    }

    public void switchImage() {
        // ... (هذا الجزء كما هو) ...
        if (!skins.isEmpty()) {
            currIndex = (currIndex + 1) % skins.size();
            sprite.setTexture(skins.get(currIndex));
        }
    }

    // دوال الحركة التي يستخدمها AnimGLEventListener4
    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;

        // تحديث اتجاه اللاعب بناءً على اتجاه الحركة الأفقية dx
        if (dx > 0) {
            isFacingRight = true; // يتحرك يمينًا
        } else if (dx < 0) {
            isFacingRight = false; // يتحرك يسارًا
        }
        // إذا كانت dx == 0 يبقى على نفس اتجاهه السابق
    }

    // Getters and setters (يمكن إضافتها حسب حاجتك)
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width * scale; }
    public float getHeight() { return height * scale; }

    // دالة لاستلام البوليان الذي تريد إرساله يدوياً (اختياري)
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }
}
