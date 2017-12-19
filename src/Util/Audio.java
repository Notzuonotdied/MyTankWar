package Util;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * 播放声音的类
 */
public class Audio {
    private URL path;

    public Audio(String wavFile) {
        path = getClass().getResource("/Sound/" + wavFile);
    }

    public void start() {
        CommonUtil.getInstance().startThread(this::run);
    }

    private void run() {

        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(path);
        } catch (Exception e1) {

            e1.printStackTrace();
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        auline.start();
        int nBytesRead = 0;
        // 这是缓冲
        byte[] abData = new byte[1024];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auline.drain();
            auline.close();
        }

    }
}