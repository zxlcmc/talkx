package org.bigmouth.gpt.xiaozhi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.theokanning.openai.completion.chat.ChatTool;
import com.theokanning.openai.completion.chat.ChatToolCall;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2025/3/4
 */
@Slf4j
public class IotConverter {

    public static void main(String[] args) {
        String json = "{\n" +
                "    \"clientId\": \"GID_test@@@a0_85_e3_e1_55_34\",\n" +
                "    \"descriptors\": [\n" +
                "        {\n" +
                "            \"description\": \"当前 AI 机器人的扬声器\",\n" +
                "            \"methods\": {\n" +
                "                \"SetVolume\": {\n" +
                "                    \"description\": \"设置音量\",\n" +
                "                    \"parameters\": {}\n" +
                "                }\n" +
                "            },\n" +
                "            \"name\": \"Speaker\",\n" +
                "            \"properties\": {\n" +
                "                \"volume\": {\n" +
                "                    \"description\": \"当前音量值\",\n" +
                "                    \"type\": \"number\"\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"description\": \"一个测试用的灯\",\n" +
                "            \"methods\": {\n" +
                "                \"TurnOn\": {\n" +
                "                    \"description\": \"打开灯\",\n" +
                "                    \"parameters\": {}\n" +
                "                },\n" +
                "                \"TurnOff\": {\n" +
                "                    \"description\": \"关闭灯\",\n" +
                "                    \"parameters\": {}\n" +
                "                }\n" +
                "            },\n" +
                "            \"name\": \"Lamp\",\n" +
                "            \"properties\": {\n" +
                "                \"power\": {\n" +
                "                    \"description\": \"灯是否打开\",\n" +
                "                    \"type\": \"boolean\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"p2pTopic\": \"devices/p2p/GID_test@@@a0_85_e3_e1_55_34\",\n" +
                "    \"session_id\": \"5d32b300\",\n" +
                "    \"type\": \"iot\"\n" +
                "}";
        List<ChatTool> chatTools = convertTools(json);
        System.out.println(chatTools);
    }

    public static List<ChatTool> convertTools(String iot) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(iot);
            ArrayNode descriptors = (ArrayNode) rootNode.get("descriptors");

            ArrayNode resultArray = mapper.createArrayNode();

            for (JsonNode descriptor : descriptors) {
                if (descriptor.has("methods")) {
                    JsonNode name = descriptor.get("name");
                    JsonNode methods = descriptor.get("methods");
                    JsonNode description = descriptor.get("description");
                    methods.fieldNames().forEachRemaining(methodName -> {
                        ObjectNode functionNode = mapper.createObjectNode();
                        functionNode.put("type", "function");

                        ObjectNode functionDetails = mapper.createObjectNode();
                        functionDetails.put("name", name.asText() + "_" + methodName);
                        functionDetails.put("description", description.asText() + " - " + methods.get(methodName).get("description").asText());

                        if (methods.get(methodName).has("parameters")) {
                            ObjectNode parametersNode = mapper.createObjectNode();
                            parametersNode.put("type", "object");
                            parametersNode.set("properties", methods.get(methodName).get("parameters"));
                            functionDetails.set("parameters", parametersNode);
                        }

                        functionNode.set("function", functionDetails);
                        resultArray.add(functionNode);
                    });
                }
            }

            String jsonB = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultArray);
            return mapper.readValue(jsonB, new TypeReference<List<ChatTool>>() {});
        } catch (JsonProcessingException e) {
            log.error("JSON转换失败", e);
            return null;
        }
    }

    public static String convertCommand(String sessionId, ChatToolCall chatToolCall) {
        try {
            // 转换逻辑
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode resultJson = mapper.createObjectNode();
            resultJson.put("type", "iot");
            resultJson.put("session_id", sessionId);

            ArrayNode commandsArray = mapper.createArrayNode();
            ObjectNode command = mapper.createObjectNode();

            // 假设函数名格式为 "设备名_方法名"
            String[] nameParts = chatToolCall.getFunction().getName().split("_");
            if (nameParts.length == 2) {
                command.put("name", nameParts[0]);
                command.put("method", nameParts[1]);
            }

            // 解析参数
            JsonNode argumentsNode = chatToolCall.getFunction().getArguments();
            command.set("parameters", argumentsNode);

            commandsArray.add(command);
            resultJson.set("commands", commandsArray);

            // 输出结果
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultJson);
        } catch (JsonProcessingException e) {
            log.error("JSON转换失败", e);
            return null;
        }
    }
}
