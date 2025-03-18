package org.bigmouth.gpt.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.UUID;

/**
 * @author GPT
 * @date 2023/11/15
 * @since 1.0.0
 */
@Slf4j
public class AudioUtils {

    public static byte[] convertAudio(String ffmpeg, byte[] inputBytes) {
        File inputFile = null;
        File outputFile = null;

        try {
            // 为了确保文件名的唯一性，使用UUID生成临时文件名
            String tempDir = System.getProperty("java.io.tmpdir");
            String uniqueid = UUID.randomUUID().toString();
            inputFile = new File(tempDir, "input_" + uniqueid + ".tmp");
            outputFile = new File(tempDir, "output_" + uniqueid + ".webm");

            // 写入输入字节数组到临时文件
            try (FileOutputStream outputStream = new FileOutputStream(inputFile)) {
                outputStream.write(inputBytes);
            }

            // 使用FFmpeg进行转换
            ProcessBuilder builder = new ProcessBuilder(
                    ffmpeg, "-i", inputFile.getAbsolutePath(), "-c:a", "libopus", outputFile.getAbsolutePath()
            );
            Process process = builder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFmpeg failed with exit code " + exitCode);
            }

            // 读取输出文件到byte数组
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 FileInputStream fis = new FileInputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                return baos.toByteArray();
            }
        } catch (Exception e) {
            log.error("convertAudio: ", e);
            return null;
        } finally {
            // 清理临时文件，确保即使在并发情况下也不会相互影响
            if (inputFile != null && inputFile.exists()) {
                inputFile.delete();
            }
            if (outputFile != null && outputFile.exists()) {
                outputFile.delete();
            }
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("/Users/huxiao/Downloads/recording-3.mp4");
            byte[] bytes = convertAudio("/Users/huxiao/Documents/program/ffmpeg", FileUtils.readFileToByteArray(file));
            FileUtils.writeByteArrayToFile(new File("/Users/huxiao/Downloads/java-recording-3.webm"), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
