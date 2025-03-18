package org.bigmouth.gpt.utils;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.ai.entity.Message;
import org.bigmouth.gpt.ai.entity.Model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author allen
 * @date 2023/4/28
 * @since 1.0.0
 */
@Slf4j
public class TikTokensUtils {
    /**
     * 模型名称对应Encoding
     */
    private static final Map<String, Encoding> modelMap = new HashMap<>();
    /**
     * registry实例
     */
    private static final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    static {

        for (ModelType modelType : ModelType.values()) {
            modelMap.put(modelType.getName(), registry.getEncodingForModel(modelType));
        }
        modelMap.put(Model.GPT_3_5_TURBO_0613.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO));
        modelMap.put(Model.GPT_3_5_TURBO_16K_0613.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO_16K));
        modelMap.put(Model.GPT_3_5_TURBO_1106.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO_16K));
        modelMap.put(Model.GPT_3_5_TURBO_INSTRUCT.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO));
        modelMap.put(Model.GPT_4_0613.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(Model.GPT_4_32K_0613.getName(), registry.getEncodingForModel(ModelType.GPT_4_32K));
        modelMap.put(Model.GPT_4_1106_PREVIEW.getName(), registry.getEncoding(EncodingType.CL100K_BASE));
        modelMap.put(Model.GPT_4_VISION_PREVIEW.getName(), registry.getEncoding(EncodingType.CL100K_BASE));
        modelMap.put(Model.GPT_4_ALL.getName(), registry.getEncoding(EncodingType.CL100K_BASE));
    }

    public static BigDecimal computePrice(long tokens, BigDecimal pricePreThousand) {
        return BigDecimal.valueOf(tokens)
                .movePointLeft(3)
                .multiply(pricePreThousand);
    }

    /**
     * 通过Encoding和text获取编码数组
     *
     * @param enc  Encoding类型
     * @param text 文本信息
     * @return 编码数组
     */
    public static List<Integer> encode(@NotNull Encoding enc, String text) {
        return StringUtils.isBlank(text) ? new ArrayList<>() : enc.encode(text);
    }

    /**
     * 通过Encoding计算text信息的tokens
     *
     * @param enc  Encoding类型
     * @param text 文本信息
     * @return tokens数量
     */
    public static int tokens(@NotNull Encoding enc, String text) {
        return encode(enc, text).size();
    }


    /**
     * 通过Encoding和encoded数组反推text信息
     *
     * @param enc     Encoding
     * @param encoded 编码数组
     * @return 编码数组对应的文本信息
     */
    public static String decode(@NotNull Encoding enc, @NotNull List<Integer> encoded) {
        return enc.decode(encoded);
    }

    /**
     * 获取一个Encoding对象，通过Encoding类型
     *
     * @param encodingType encodingType
     * @return Encoding
     */
    public static Encoding getEncoding(@NotNull EncodingType encodingType) {
        Encoding enc = registry.getEncoding(encodingType);
        return enc;
    }

    /**
     * 获取encode的编码数组
     *
     * @param text 文本信息
     * @return 编码数组
     */
    public static List<Integer> encode(@NotNull EncodingType encodingType, String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(encodingType);
        List<Integer> encoded = enc.encode(text);
        return encoded;
    }

    public static int tokens(EncodingType encodingType, List<Message> messages) {
        if (CollectionUtils.isEmpty(messages)) {
            return 0;
        }
        StringBuilder text = new StringBuilder();
        messages.forEach(e -> {
            text.append(e.getContent());
        });
        return tokens(encodingType, text.toString());
    }

    /**
     * 计算指定字符串的tokens，通过EncodingType
     *
     * @param encodingType encodingType
     * @param text         文本信息
     * @return tokens数量
     */
    public static int tokens(@NotNull EncodingType encodingType, String text) {
        return encode(encodingType, text).size();
    }


    /**
     * 通过EncodingType和encoded编码数组，反推字符串文本
     *
     * @param encodingType encodingType
     * @param encoded      编码数组
     * @return 编码数组对应的字符串
     */
    public static String decode(@NotNull EncodingType encodingType, @NotNull List<Integer> encoded) {
        Encoding enc = getEncoding(encodingType);
        return enc.decode(encoded);
    }


    /**
     * 获取一个Encoding对象，通过模型名称
     *
     * @param modelName 模型名称
     * @return Encoding
     */
    public static Encoding getEncoding(@NotNull String modelName) {
        return modelMap.getOrDefault(modelName, registry.getEncoding(EncodingType.CL100K_BASE));
    }

    /**
     * 获取encode的编码数组，通过模型名称
     *
     * @param text 文本信息
     * @return 编码数组
     */
    public static List<Integer> encode(@NotNull String modelName, String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(modelName);
        if (Objects.isNull(enc)) {
            log.warn("[{}]模型不存在或者暂不支持计算tokens，直接返回tokens==0", modelName);
            return new ArrayList<>();
        }
        return enc.encode(text);
    }

    /**
     * 通过模型名称, 计算指定字符串的tokens
     *
     * @param modelName 模型名称
     * @param text      文本信息
     * @return tokens数量
     */
    public static int tokens(@NotNull String modelName, String text) {
        return encode(modelName, text).size();
    }


    /**
     * 通过模型名称计算messages获取编码数组
     * 参考官方的处理逻辑：
     * <a href=https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb>https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb</a>
     *
     * @param modelName 模型名称
     * @param messages  消息体
     * @return tokens数量
     */
    public static int tokens(@NotNull String modelName, @NotNull List<Message> messages) {
        Encoding encoding = getEncoding(modelName);
        int tokensPerMessage = 0;
        int tokensPerName = 0;
        //3.5统一处理
        if (modelName.equals(Model.GPT_3_5_TURBO.getName())
                || modelName.equals(Model.GPT_3_5_TURBO_0613.getName())
                || modelName.equals(Model.GPT_3_5_TURBO_16K.getName())
                || modelName.equals(Model.GPT_3_5_TURBO_16K_0613.getName())
                || modelName.equals(Model.GPT_3_5_TURBO_1106.getName())
                || modelName.equals(Model.GPT_3_5_TURBO_INSTRUCT.getName())) {
            tokensPerMessage = 4;
            tokensPerName = -1;
        }
        //4.0统一处理
        else if (modelName.equals(Model.GPT_4.getName())
                || modelName.equals(Model.GPT_4_0613.getName())
                || modelName.equals(Model.GPT_4_32K.getName())
                || modelName.equals(Model.GPT_4_32K_0613.getName())
                || modelName.equals(Model.GPT_4_1106_PREVIEW.getName())
                || modelName.equals(Model.GPT_4_VISION_PREVIEW.getName())
                || modelName.equals(Model.GPT_4_ALL.getName())) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        }
        else {
            tokensPerMessage = 3;
            tokensPerName = 1;
        }
        int sum = 0;
        for (Message msg : messages) {
            sum += tokensPerMessage;
            sum += tokens(encoding, msg.getContent());
            sum += tokens(encoding, msg.getRole());
            sum += tokens(encoding, msg.getName());
            if (StringUtils.isNotBlank(msg.getName())) {
                sum += tokensPerName;
            }
        }
        sum += 3;
        return sum;
    }

    /**
     * 通过模型名称和encoded编码数组，反推字符串文本
     *
     * @param modelName
     * @param encoded
     * @return
     */
    public static String decode(@NotNull String modelName, @NotNull List<Integer> encoded) {
        Encoding enc = getEncoding(modelName);
        return enc.decode(encoded);
    }
}
