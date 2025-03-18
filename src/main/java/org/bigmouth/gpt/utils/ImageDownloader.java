package org.bigmouth.gpt.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Base64;

/**
 * @author TalkX
 */
public class ImageDownloader {

    /**
     * 从指定的URL下载图片并返回Base64编码的字符串。
     *
     * @param imageUrl 图片的URL地址
     * @return 包含Base64编码的图片数据URL字符串
     * @throws IOException 如果发生I/O错误
     */
    public static String downloadImageAsBase64(String imageUrl) throws IOException {
        // 创建CloseableHttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imageUrl);
        CloseableHttpResponse response = null;

        try {
            // 执行GET请求
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                throw new IOException("Failed to download image, HTTP status code: " + statusCode);
            }

            // 获取Content-Type头
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                throw new IOException("No content returned from the URL");
            }

            String contentType = entity.getContentType().getValue();
            if (!contentType.startsWith("image/")) {
                throw new IOException("URL does not point to an image. Content-Type: " + contentType);
            }

            // 获取图片字节数组
            byte[] imageBytes = EntityUtils.toByteArray(entity);

            // Base64编码
            String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);

            // 构建data URL
            String dataUrl = "data:" + contentType + ";base64," + base64Encoded;

            return dataUrl;
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
    }

    // 示例用法
    public static void main(String[] args) {
        String imageUrl = "https://web.talkx.cn/files/2024/12/25/7e9c7c6c8c9444fd81d87b7ca0272626.jpg";
        try {
            String base64Image = downloadImageAsBase64(imageUrl);
            System.out.println("Base64 Image: " + base64Image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
