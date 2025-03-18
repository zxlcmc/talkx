package org.bigmouth.gpt.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

/**
 * @author huxiao
 * @date 2023/11/6
 * @since 1.0.0
 */
public class QRCodeGenerator {

    public static BufferedImage generate(String text) {
        int width = 300;
        int height = 300;

        try {
            // 设置二维码参数
            java.util.Map<EncodeHintType, Object> hints = new java.util.HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // 生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;
        } catch (Exception e) {
            return null;
        }
    }
}
