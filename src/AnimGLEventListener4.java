


import Texture.TextureReader;
import java.awt.event.*;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;

public class AnimGLEventListener4 extends AnimListener {

    int animationIndex = 0;
    int framecounter = 0;
    int maxWidth = 800;
    int maxHeight = 800;
    double x = maxWidth / 2, y = maxHeight / 2;
    double speed = 3.0;

    String textureNames[] = {
            "bunny1.png",
            "bunny2.png",
            "bunny3.png",
            "bunny4.png",
            "bunny5.png",
            "bunny6.png","Back.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels()
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();

        DrawBackground(gl);
        handleKeyPress();

        DrawSprite(gl, (int) x,(int) y, animationIndex, 1);
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

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);

        gl.glPushMatrix();

        gl.glTranslated(x, y, 0);


        gl.glScaled( 40* scale, 40 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);

        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); // أسفل يسار

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(800.0f, 0.0f, -1.0f); // أسفل يمين

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(800.0f, 800.0f, -1.0f); // أعلى يمين

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(0.0f, 800.0f, -1.0f); // أعلى يسار

        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void handleKeyPress() {
        boolean moving = false;
        int speed = 10;

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (x > 50) {
                x -= speed;
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (x < maxWidth - 50) {
                x += speed;
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (y > 50) {
                y -= speed;
                moving = true;
            }
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (y < maxHeight - 50) {
                y += speed;
                moving = true;
            }
        }
        if (moving) {
            framecounter++;
            if (framecounter >= 3) {
                framecounter = 0;
                animationIndex++;
                animationIndex %= 6;
            }
        }
    }

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

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }
}