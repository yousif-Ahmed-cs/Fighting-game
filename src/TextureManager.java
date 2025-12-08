import Texture.Texture;
import Texture.TextureReader; // الكلاس المساعد الذي أرسلته
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static final Map<String, Texture> textureCache = new HashMap<>();
    private static final String ASSETS_FOLDER = "Assets"; // تأكد من صحة مسار الأصول
    private GLU glu = new GLU();

    public Texture getTexture(GL gl, String fileName) {
        if (textureCache.containsKey(fileName)) {
            return textureCache.get(fileName);
        }
        Texture newTexture = loadTexture(gl, fileName);
        if (newTexture != null) {
            textureCache.put(fileName, newTexture);
        }
        return newTexture;
    }

    private Texture loadTexture(GL gl, String fileName) {
        try {
            TextureReader.Texture internalTextureData = TextureReader.readTexture(ASSETS_FOLDER + "//" + fileName, true);

            int[] textureIdArray = new int[1]; // تم الإصلاح هنا
            gl.glGenTextures(1, textureIdArray, 0);
            int textureId = textureIdArray[0]; // تم الإصلاح هنا

            gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

            glu.gluBuild2DMipmaps(
                    GL.GL_TEXTURE_2D,
                    GL.GL_RGBA,
                    internalTextureData.getWidth(),
                    internalTextureData.getHeight(),
                    GL.GL_RGBA,
                    GL.GL_UNSIGNED_BYTE,
                    internalTextureData.getPixels()
            );

            return new Texture(textureId, internalTextureData.getWidth(), internalTextureData.getHeight(), fileName);

        } catch (IOException e) {
            System.err.println("Error loading texture: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
}
