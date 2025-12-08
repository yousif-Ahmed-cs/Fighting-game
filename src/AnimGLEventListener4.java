import java.awt.event.*;
import javax.media.opengl.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
// تأكد من وجود هذه الاستيرادات للعمل مع الهيكلية الجديدة
// import Player;
// import TextureManager;
import Texture.Texture;
public class AnimGLEventListener4 extends AnimListener implements MouseListener {

    int animationIndex = 0;
    int framecounter = 0;
    int maxWidth = 800;
    int maxHeight = 800;
    float x = maxWidth/2f;
    float y = maxHeight/2f;
    float x2 = (maxWidth/2f) + 10 ;
    float y2 = (maxHeight/2f)  ;

    Player player;
    Player player2;
    TextureManager textureManager = new TextureManager();

    boolean isPlaying = false;

    // تأكد من أن مسار الأصول صحيح في TextureManager
    String[] playerNames = { "bunny1.png", "bunny2.png", "bunny3.png",
            "bunny4.png", "bunny5.png", "bunny6.png"};
    private String[] backgroundNames = {
            "battleback1.png" // المستوى الثالث (تغيير الاسم حسب ملفك)
            , "battleback2.png"
            , "battleback3.png"
            , "battleback4.png"
            , "battleback5.png"
            , "battleback6.png"
            , "battleback7.png"
            , "battleback8.png"
            , "battleback9.png"
            , "battleback10.png"
    };
    private List<Texture> backgroundsList = new ArrayList<> ();

    // 3. مؤشر (Index) لتحديد الخلفية/المستوى الحالي
    private int currentLevelIndex = 0;

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
        for (String bgName : backgroundNames) {
            Texture tex = textureManager.getTexture(gl, bgName);
            if (tex != null) {
                backgroundsList.add(tex);
            }
        }

        player = new Player(x , y , 16 , 40 , 5.0f);
        player2 = new Player(x2 , y2 , 20 , 40 , 6.0f);

        player.loadSkins(gl, textureManager, playerNames);
        player2.loadSkins(gl, textureManager, playerNames);
    }
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0, 800, 0, 800, -1, 1);
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL ();
        gl.glClear ( GL.GL_COLOR_BUFFER_BIT );
        gl.glLoadIdentity ();
        drawBackground(gl);

        player.draw(gl);
         player2.draw(gl);

        handleKeyPress();
        // ... (بقية منطق القائمة/الخلفية هنا إذا كنت تستخدمه)
    }
    private void drawBackground(GL gl) {
        // التأكد من أن الفهرس الحالي صالح وأن القائمة ليست فارغة
        if (backgroundsList.isEmpty() || currentLevelIndex < 0 || currentLevelIndex >= backgroundsList.size()) {
            return;
        }

        // الحصول على كائن Texture الحالي من القائمة
        Texture currentBackground = backgroundsList.get(currentLevelIndex);

        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, currentBackground.getId());

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(maxWidth, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(maxWidth, maxHeight, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(0.0f, maxHeight, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();
        gl.glDisable(GL.GL_BLEND);
    }

    // ... reshape, displayChanged ...

    public void handleKeyPress() {
        boolean moving = false;
        boolean moving2 = false;
        float moveSpeed = 10.0f;
        if (isKeyPressed(KeyEvent.VK_A)) {
            if (player2.getX() > player2.getWidth() / 2) {
                player2.move(-moveSpeed, 0);
                moving2 = true;
            }
        }
        // استخدمنا الـ move() function في كلاس الـ Player
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (player.getX() > player.getWidth() / 2) {
                player.move(-moveSpeed, 0);
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (player.getX() < maxWidth - player.getWidth() / 2) {
                player.move(moveSpeed, 0);
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_D)) {
            if (player2.getX() < maxWidth - player2.getWidth() / 2) {
                player2.move(moveSpeed, 0);
                moving2 = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (player.getY() > player.getHeight() / 2) {
                player.move(0, -moveSpeed);
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_S)) {
            if (player2.getY() > player2.getHeight() / 2) {
                player2.move(0, -moveSpeed);
                moving2 = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (player.getY() < maxHeight - player.getHeight() / 2) {
                player.move(0, moveSpeed);
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_W)) {
            if (player2.getY() < maxHeight - player2.getHeight() / 2) {
                player2.move(0, moveSpeed);
                moving2 = true;
            }
        }


        if (moving||moving2) {
           if (moving){
            framecounter++;
            if (framecounter >= 3) {
                framecounter = 0;
                player.switchImage();
            }}
           if (moving2){
            framecounter++;
            if (framecounter >= 3) {
                framecounter = 0;
                player2.switchImage();
            }}
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            // نزيد الفهرس ونتأكد من عدم تخطي عدد الخلفيات المتاحة
            if (!backgroundsList.isEmpty()) {
                // استخدام علامة (flag) للتأكد من تغيير الخلفية مرة واحدة فقط عند الضغطة الواحدة
                if (!spacePressedLastFrame) {
                    currentLevelIndex = (currentLevelIndex + 1) % backgroundsList.size();
                    System.out.println("Changed background to level: " + (currentLevelIndex + 1));
                    spacePressedLastFrame = true; // نضع العلامة أن الزر مضغوط الآن
                }
            }
        } else {
            spacePressedLastFrame = false; // إذا لم يكن الزر مضغوطاً، نزيل العلامة
        }
    }
    private boolean spacePressedLastFrame = false;

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
    }

    /**
     * الدالة المطلوبة تم تعديلها لتكون جزءاً من الكلاس.
     * @param keyCode كود الزر المراد فحصه.
     * @return true إذا كان الزر مضغوطاً حالياً، false عدا ذلك.
     */
    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    // --- تنفيذ دوال الماوس ---
    @Override
    public void mouseClicked(MouseEvent e) {
        // ... (بقية كود الماوس هنا) ...
    }

    // الدوال الأخرى الفارغة المطلوبة من MouseListener
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
}
