import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    private Clip backgroundMusic; // للموسيقى الخلفية

    // ميثود لتشغيل صوت مرة واحدة (Sound Effect)
    public void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("مشكلة في تشغيل الصوت: " + e.getMessage());
        }
    }

    // ميثود لتشغيل موسيقى خلفية (تتكرر)
    public void playBackgroundMusic(String musicFilePath) {
        try {
            stopBackgroundMusic(); // أوقف أي موسيقى شغالة

            File musicFile = new File(musicFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // تكرار مستمر
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("مشكلة في تشغيل الموسيقى: " + e.getMessage());
        }
    }

    // أوقف الموسيقى الخلفية
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    // تحكم في الصوت (Volume)
    public void setVolume(float volume) { // من 0.0 لـ 1.0
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        }
    }
}
