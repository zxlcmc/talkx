package org.bigmouth.gpt.xiaozhi.audio;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.gagravarr.opus.OpusAudioData;
import org.gagravarr.opus.OpusFile;
import org.gagravarr.opus.OpusInfo;
import org.gagravarr.opus.OpusTags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Slf4j
public class OpusUtils {

    public static File writeToOgg(List<byte[]> opusBytes, String fileName) {
        FileOutputStream fos = null;
        OpusFile opusFile = null;
        try {
            int sampleRate = 48000;
            int frameDuration = 20;
            int channels = 1;

            int frameSize = sampleRate / 1000 * channels * frameDuration;

            String outputFilePath = System.getProperty("user.home") + java.io.File.separator + "Downloads" + File.separator + fileName + ".ogg";

            java.io.File outputFile = new java.io.File(outputFilePath);
            fos = new FileOutputStream(outputFile);

            OpusInfo info = new OpusInfo();
            info.setNumChannels(channels);
            info.setSampleRate(sampleRate);

            OpusTags tags = new OpusTags();
            tags.addComment("encoder", "TalkX");

            opusFile = new OpusFile(fos, info, tags);

            long totalSamples = frameSize;
            for (byte[] audioByte : opusBytes) {
                OpusAudioData audioData = new OpusAudioData(audioByte);
                audioData.setGranulePosition(totalSamples);
                opusFile.writeAudioData(audioData);
                totalSamples += frameSize;
            }

            return outputFile;
        } catch (FileNotFoundException e) {
            log.error("Error writing OGG file", e);
        } finally {
            IoUtil.close(opusFile);
            IoUtil.close(fos);
        }
        return null;
    }


    public static File writeToOgg2(byte[] opusBytes, String fileName) {
        FileOutputStream fos = null;
        OpusFile opusFile = null;
        try {
            int sampleRate = 16000;
            int frameDuration = 60;
            int channels = 1;

            int frameSize = sampleRate / 1000 * channels * frameDuration;

            String outputFilePath = System.getProperty("user.home") + java.io.File.separator + fileName + ".ogg";

            java.io.File outputFile = new java.io.File(outputFilePath);
            fos = new FileOutputStream(outputFile);

            opusFile = new OpusFile(fos);
            opusFile.getInfo().setNumChannels(channels);
            opusFile.getInfo().setSampleRate(sampleRate);
            opusFile.getTags().addComment("encoder", "TalkX");
            OpusAudioData audioData = new OpusAudioData(opusBytes);
            opusFile.writeAudioData(audioData);

            return outputFile;
        } catch (FileNotFoundException e) {
            log.error("Error writing OGG file", e);
        } finally {
            IoUtil.close(opusFile);
            IoUtil.close(fos);
        }
        return null;
    }
}
