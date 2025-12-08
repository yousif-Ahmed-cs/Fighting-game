import Texture.Texture;
import javax.media.opengl.GL;

public class Sprite {
    private Texture texture;
    private float x, y, width, height;
    // إضافة متغير الاتجاه في Sprite
    private boolean isFacingRight;

    // تحديث المُنشئ لاستلام الاتجاه
    public Sprite(Texture texture, float x, float y, float width, float height, boolean isFacingRight) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isFacingRight = isFacingRight;
    }

    public void draw(GL gl) {
        if (texture == null) return;

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getId());

        gl.glPushMatrix();
        gl.glTranslated(x, y, 0);

        // **الجزء الأهم: قلب الصورة أفقياً**
        // إذا كان يواجه اليسار (isFacingRight == false)، نطبق مقياس سالب على محور X
        // هذا يؤدي إلى قلب الصورة حول محور Y في مركز الكائن.
        if (!isFacingRight) {
            gl.glScalef(-width / 2.0f, height / 2.0f, 1);
        } else {
            gl.glScalef(width / 2.0f, height / 2.0f, 1);
        }

        gl.glBegin(GL.GL_QUADS);
        // إحداثيات النسيج (UV coordinates) تبقى كما هي 0.0f إلى 1.0f
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    // Setters للحفاظ على تحديث الموقع والحجم والاتجاه
    public void setPosition(float x, float y) { this.x = x; this.y = y; }
    public void setSize(float width, float height) { this.width = width; this.height = height; }
    public void setTexture(Texture newTexture) { this.texture = newTexture; }
    // Setter لتلقي البوليان الجديد من Player
    public void setFacingRight(boolean facingRight) { this.isFacingRight = facingRight; }
}
