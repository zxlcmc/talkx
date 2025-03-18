package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.utils.TikTokensUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author allen
 * @date 2023-04-20
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class OpenApiRequest {

    /**
     * ID of the model to use. See the <a href="https://platform.openai.com/docs/models/model-endpoint-compatibility">model endpoint compatibility</a>
     * table for details on which models work with the Chat API.
     */
    private String model;

    /**
     * A list of messages describing the conversation so far.
     */
    private List<Message> messages;

    /**
     * A list of functions the model may generate JSON inputs for.
     */
    private List<Function> functions;

    /**
     * Controls how the model responds to function calls. <code>none</code> means the model does not call a function, and responds to the end-user.
     * <code>auto</code> means the model can pick between an end-user or calling a function. Specifying a particular function via {"name": "my_function"}
     * forces the model to call that function. none is the default when no functions are present. auto is the default if functions are present.
     */
    @JSONField(name = "function_call")
    private String functionCall;

    /**
     * What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the output more random,
     * while lower values like 0.2 will make it more focused and deterministic.
     * <p>
     * We generally recommend altering this or {@code top_p} but not both.
     */
    private Double temperature = 1.0d;

    /**
     * An alternative to sampling with temperature, called nucleus sampling,
     * where the model considers the results of the tokens with top_p probability mass.
     * So 0.1 means only the tokens comprising the top 10% probability mass are considered.
     * <p>
     * We generally recommend altering this or {@code temperature} but not both.
     */
    @JSONField(name = "top_p")
    private Double topP = 1d;

    /**
     * How many chat completion choices to generate for each input message.
     */
    private int n = 1;

    /**
     * If set, partial message deltas will be sent, like in ChatGPT. Tokens will be sent
     * as data-only <a href="https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events#Event_stream_format">server-sent events</a>
     * as they become available, with the stream terminated by a {@code data: [DONE]} message. See the OpenAI Cookbook
     * for <a href="https://github.com/openai/openai-cookbook/blob/main/examples/How_to_stream_completions.ipynb">example code</a>.
     */
    private boolean stream = true;

    /**
     * Up to 4 sequences where the API will stop generating further tokens.
     */
    private List<String> stop;

    /**
     * The maximum number of <a href="https://platform.openai.com/tokenizer">tokens</a> to generate in the chat completion.
     * <p>
     * The total length of input tokens and generated tokens is limited by the model's context length.
     */
    @Deprecated
    @JSONField(name = "max_tokens")
    private Integer maxTokens;

    @JSONField(name = "max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
     * increasing the model's likelihood to talk about new topics.
     * <br>
     * <a href="https://platform.openai.com/docs/api-reference/parameter-details">See more information about frequency and presence penalties.</a>
     */
    @JSONField(name = "presence_penalty")
    private Double presencePenalty;
    /**
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far,
     * decreasing the model's likelihood to repeat the same line verbatim.
     * <br>
     * <a href="https://platform.openai.com/docs/api-reference/parameter-details">See more information about frequency and presence penalties.</a>
     */
    @JSONField(name = "frequency_penalty")
    private Double frequencyPenalty;

    /**
     * Modify the likelihood of specified tokens appearing in the completion.
     * <p>
     * Accepts a json object that maps tokens (specified by their token ID in the tokenizer)
     * to an associated bias value from -100 to 100. Mathematically, the bias is added to the logits generated
     * by the model prior to sampling. The exact effect will vary per model, but values between -1 and 1 should
     * decrease or increase likelihood of selection; values like -100 or 100 should result in a ban or exclusive
     * selection of the relevant token.
     */
    @JSONField(name = "logit_bias")
    private Map<String, Integer> logitBias;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     * <a href="https://platform.openai.com/docs/guides/safety-best-practices/end-user-ids">Learn more</a>.
     */
    private String user;

    @JSONField(name = "stream_options")
    private StreamOptions streamOptions;

    /**
     * 返回此次请求的 tokens
     * @return tokens
     */
    @JSONField(serialize = false, deserialize = false)
    public int getTokens() {
        return TikTokensUtils.tokens(getModel(), getMessages());
    }

    @JSONField(serialize = false, deserialize = false)
    public Integer getMaxTokens() {
        if (null != maxCompletionTokens) {
            return maxCompletionTokens;
        }
        return Optional.ofNullable(maxTokens).orElseGet(() -> Model.getOutputMaxTokens(getModel()));
    }
}
