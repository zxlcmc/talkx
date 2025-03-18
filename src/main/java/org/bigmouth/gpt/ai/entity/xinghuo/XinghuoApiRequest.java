package org.bigmouth.gpt.ai.entity.xinghuo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author huxiao
 * @date 2023/10/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class XinghuoApiRequest {

    private HeaderRequest header = new HeaderRequest();
    private ParameterRequest parameter = new ParameterRequest();
    private PayloadRequest payload = new PayloadRequest();

    /**
     * 返回此次请求的 tokens
     * @return tokens
     */
    @JSONField(serialize = false, deserialize = false)
    public int getTokens() {
        List<Text> textList = payload.getMessage().getText();
        int len = 0;
        for (Text text : textList) {
            len += text.getContent().length();
        }
        return len;
    }
}
