package Util;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;

/**
 * 播放声音的类
 */
public class Audio {
    private URL path;

    public Audio(String wavFile) {
        path = getClass().getResource("/Sound/" + wavFile);
    }

    public void start() {
        CommonUtil.getInstance().startSingleThread(this::run);
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
        SourceDataLine sourceDataLine;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        try {
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        sourceDataLine.start();
        int nBytesRead = 0;
        // 这是缓冲
        byte[] abData = new byte[1024];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    sourceDataLine.write(abData, 0, nBytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sourceDataLine.drain();
            sourceDataLine.close();
        }

    }
}