import Texture.Texture;
//import javax.media.opengl.GL;
import javax.media.opengl.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private float x, y, width, height;
    private Sprite sprite;
    private List<Texture> skins = new ArrayList<>();
    private List<Texture> fightingSkins = new ArrayList<>();
    private List<Texture> jumpingSkins = new ArrayList<>();
    private int currIndex = 0;
    private int fightIndex = 0;
    private int jumpIndex = 0;
    private float scale;
    private boolean isFacingRight = true;
    private boolean isFighting = false;
    private boolean isJumping = false;

    public Player(float x, float y, float width, float height, float scale) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.sprite = new Sprite(null, x, y, width * scale, height * scale, isFacingRight);
    }

    public void loadSkins(GL gl, TextureManager manager, String[] skinNames) {
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

    public void loadFightingSkins(GL gl, TextureManager manager, String[] fightSkinNames) {
        for (String name : fightSkinNames) {
            Texture tex = manager.getTexture(gl, name);
            if (tex != null) {
                fightingSkins.add(tex);
            }
        }
    }

    public void loadJumpingSkins(GL gl, TextureManager manager, String[] jumpSkinNames) {
        for (String name : jumpSkinNames) {
            Texture tex = manager.getTexture(gl, name);
            if (tex != null) {
                jumpingSkins.add(tex);
            }
        }
    }

    public void draw(GL gl) {
        sprite.setPosition(x, y);
        sprite.setSize(width * scale, height * scale);
        sprite.setFacingRight(isFacingRight);
        sprite.draw(gl);
    }

    public void switchImage() {
        if (!isFighting && !isJumping && !skins.isEmpty()) {
            currIndex = (currIndex + 1) % skins.size();
            sprite.setTexture(skins.get(currIndex));
        }
    }

    public void startFighting() {
        isFighting = true;
        fightIndex = 0;
        if (!fightingSkins.isEmpty()) {
            sprite.setTexture(fightingSkins.get(0));
        }
    }

    public boolean updateFighting() {
        if (!isFighting || fightingSkins.isEmpty()) {
            return true;
        }

        fightIndex++;
        if (fightIndex >= fightingSkins.size()) {
            return true; // Animation finished
        }

        sprite.setTexture(fightingSkins.get(fightIndex));
        return false; // Animation still playing
    }

    public void stopFighting() {
        isFighting = false;
        fightIndex = 0;
        currIndex = 0;
        if (!skins.isEmpty()) {
            sprite.setTexture(skins.get(currIndex));
        }
    }

    public void startJumping() {
        isJumping = true;
        jumpIndex = 0;
        if (!jumpingSkins.isEmpty()) {
            sprite.setTexture(jumpingSkins.get(0));
        }
    }

    public void updateJumping() {
        if (!isJumping || jumpingSkins.isEmpty()) {
            return;
        }

        jumpIndex++;
        if (jumpIndex >= jumpingSkins.size()) {
            jumpIndex = jumpingSkins.size() - 1; // Stay on last frame
        }

        sprite.setTexture(jumpingSkins.get(jumpIndex));
    }

    public void stopJumping() {
        isJumping = false;
        jumpIndex = 0;
        currIndex = 0;
        if (!skins.isEmpty()) {
            sprite.setTexture(skins.get(currIndex));
        }
    }

    public void move(float dx, float dy) {
        this.x += dx;
        this.y += dy;

        if (dx > 0) {
            isFacingRight = true;
        } else if (dx < 0) {
            isFacingRight = false;
        }
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width * scale; }
    public float getHeight() { return height * scale; }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }
}