import Texture.Texture;
import Texture.TextureReader; // الكلاس المساعد الذي أرسلته
import javax.media.opengl.GL;
// import javax.media.opengl.glu.GLU; // لم نعد بحاجة لهذا السطر
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static final Map<String, Texture> textureCache = new HashMap<>();
    private static final String ASSETS_FOLDER = "Assets"; // تأكد من صحة مسار الأصول
    // private GLU glu = new GLU(); // تم حذف هذا السطر لأنه لم يعد مستخدماً

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

            int[] textureIdArray = new int[1];
            gl.glGenTextures(1, textureIdArray, 0);
            int textureId = textureIdArray[0];

            gl.glBindTexture(GL.GL_TEXTURE_2D, textureId);

            // --- التغيير الأساسي هنا ---
            // استخدام فلترة خطية بسيطة بدلاً من Mipmapping.
            // عملية إنشاء Mipmaps (باستخدام gluBuild2DMipmaps) بطيئة جداً وغير ضرورية للألعاب ثنائية الأبعاد.
            // هذا التغيير سيُسرّع تحميل الصور بشكل ملحوظ.
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

            // رفع بيانات الصورة إلى كرت الشاشة مباشرة باستخدام دالة OpenGL الأساسية.
            // هذه الطريقة أسرع بكثير من الطريقة القديمة.
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA,
                    internalTextureData.getWidth(), internalTextureData.getHeight(),
                    0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,
                    internalTextureData.getPixels());

            return new Texture(textureId, internalTextureData.getWidth(), internalTextureData.getHeight(), fileName);

        } catch (IOException e) {
            System.err.println("Error loading texture: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
    public static void clearCache() {
        textureCache.clear();
        System.out.println("Texture cache has been cleared."); // رسالة للتأكد
    }

}
